package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class TagSchemeImpl<T, N extends NBT<?>> implements TagScheme<T, N> {

    private final String key;
    private final TagType<N> type;
    private final Supplier<N> defaultNBTCreator;
    private final Supplier<T> defaultInterpretationCreator;
    private final Interpreter<T, N> interpreter;
    private final List<String> path;

    TagSchemeImpl(
            String key,
            TagType<N> type,
            Supplier<N> defaultNBTCreator,
            Supplier<T> defaultInterpretationCreator,
            Interpreter<T, N> interpreter,
            List<String> path
    ) {
        this.key = key;
        this.type = type;
        this.defaultNBTCreator = defaultNBTCreator;
        this.defaultInterpretationCreator = defaultInterpretationCreator;
        this.interpreter = interpreter;
        this.path = path;
    }

    @Override
    public @NotNull String name() {
        return key;
    }

    @Override
    public @NotNull TagType<N> type() {
        return type;
    }

    @Override
    public @NotNull Optional<N> createDefaultNBT() {
        return Optional.of(defaultNBTCreator)
                .map(Supplier::get);
    }

    @Override
    public @NotNull Optional<T> createDefaultInterpretation() {
        return Optional.of(defaultInterpretationCreator)
                .map(Supplier::get);
    }

    @Override
    public @Nullable Interpreter<T, N> defaultInterpreter() {
        return interpreter;
    }

    @Override
    public @NotNull List<String> path() {
        return path;
    }

    @Override
    public @NotNull Builder<T, N> builder() {
        return builder(key);
    }

    @Override
    public @NotNull Builder<T, N> builder(@NotNull String key) {
        Builder<T, N> builder = new Builder<>(key, type);

        builder.path(path);
        if (defaultNBTCreator != null) builder.defaultNBTCreator(defaultNBTCreator);
        if (defaultInterpretationCreator != null) builder.defaultInterpretationCreator(defaultInterpretationCreator);
        if (interpreter != null) builder.interpreter(interpreter);

        return builder;
    }
}