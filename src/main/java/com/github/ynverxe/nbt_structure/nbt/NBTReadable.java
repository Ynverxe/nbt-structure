package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface NBTReadable {

    <N extends NBT<?>> N read(@NotNull String key);

    default <N extends NBT<?>> @Nullable Tag<N> readTag(@NotNull String key, @NotNull TagType<N> type) {
        N found = read(key, type);

        if (found == null) return null;

        return new Tag<>(key, found);
    }

    default <N extends NBT<?>> @Nullable N read(@NotNull String key, @NotNull TagType<N> type) {
        return type.cast(read(key));
    }

    @SuppressWarnings("unchecked")
    default <T> @Nullable T readValue(@NotNull String key) {
        return (T) read(key).value();
    }

    default @Nullable Number readNumber(@NotNull String key) {
        return readValue(key);
    }

    default <T, N extends NBT<?>> @Nullable T interpret(@NotNull String key, @Nullable TagType<N> type, @NotNull Function<N, T> interpreter) {
        N found = read(key);

        return found == null ? null : interpreter.apply(found);
    }
}