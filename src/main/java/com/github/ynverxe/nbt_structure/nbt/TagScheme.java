package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unused", "UnusedReturnValue"})
public interface TagScheme<T, N extends NBT>  {

    @NotNull String name();

    @NotNull TagType<N> type();

    @NotNull Class<T> interpretationType();

    @NotNull Optional<N> createDefaultNBT();

    @NotNull Optional<T> createDefaultInterpretation();

    @Nullable Interpreter<T, N> defaultInterpreter();

    @NotNull List<String> path();

    @NotNull Builder<T, N> builder(@NotNull String key);

    @NotNull Builder<T, N> builder();

    default @NotNull String fullKey() {
        StringBuilder builder = new StringBuilder();
        for (String s : path()) {
            builder.append(s).append(".");
        }
        builder.append(name());
        return builder.toString();
    }

    default @NotNull Optional<T> interpretValue(N nbt) {
        Interpreter<T, N> interpreter = defaultInterpreter();

        if (nbt == null) nbt = createDefaultNBT().orElseThrow(() -> new IllegalArgumentException("null nbt"));

        if (interpreter != null) return Optional.of(interpreter.interpretIt(nbt));

        return Optional.empty();
    }

    static @NotNull <T, N extends NBT> TagScheme<T, N> of(
            @NotNull String key,
            @NotNull Class<T> interpretationClass,
            @NotNull TagType<N> type
    ) {
        return builder(key, interpretationClass, type)
                .resolvePathInKey()
                .build();
    }

    @SuppressWarnings("unused")
    static @NotNull <T, N extends NBT> Builder<T, N> builder(
            @NotNull String key,
            @Nullable Class<T> interpretationClass,
            @NotNull TagType<N> type
    ) {
        return new Builder<>(
                Objects.requireNonNull(key, "key"),
                Objects.requireNonNull(type, "type"),
                Objects.requireNonNull(interpretationClass, "interpretationClass")
        );
    }

    class Builder<T, N extends NBT> {

        private final TagType<N> type;
        private final Class<T> interpretationType;
        private final List<String> path = new ArrayList<>();
        private String key;
        private Supplier<N> auxiliaryNBTSupplier;
        private Supplier<T> auxiliaryInterpretationSupplier;
        private Interpreter<T, N> interpreter;

        public Builder(String key, TagType<N> type, Class<T> interpretationType) {
            this.key = key;
            this.type = type;
            this.interpretationType = interpretationType;
        }

        public @NotNull Builder<T, N> auxiliaryNBTSupplier(@NotNull Supplier<N> nbtSupplier) {
            this.auxiliaryNBTSupplier = nbtSupplier;
            return this;
        }

        public @NotNull Builder<T, N> auxiliaryInterpretationSupplier(@NotNull Supplier<T> auxiliaryInterpretationSupplier) {
            this.auxiliaryInterpretationSupplier = auxiliaryInterpretationSupplier;
            return this;
        }

        public @NotNull Builder<T, N> interpreter(@NotNull Interpreter<T, N> interpreter) {
            this.interpreter = interpreter;
            return this;
        }

        public @NotNull Builder<T, N> path(List<String> path) {
            this.path.clear();
            this.path.addAll(path);
            return this;
        }

        public @NotNull Builder<T, N> path(@NotNull String... path) {
            this.path.clear();
            this.path.addAll(Arrays.asList(path));
            return this;
        }

        public @NotNull Builder<T, N> resolvePathInKey() {
            String[] separated = key.split("\\.");

            if (separated.length <= 0) return this;

            int i;
            for (i = 0; i < separated.length - 1; i++) {
                String value = separated[i];
                separated[i] = null;
                this.path.add(value);
            }

            this.key = separated[i];
            return this;
        }

        public @NotNull TagScheme<T, N> build() {
            return new TagSchemeImpl<>(key, type, interpretationType, auxiliaryNBTSupplier, auxiliaryInterpretationSupplier, interpreter, Collections.unmodifiableList(path));
        }
    }

    interface Interpreter<T, N extends NBT> {
        @NotNull T interpretIt(@NotNull N nbt);
    }
}