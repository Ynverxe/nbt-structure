package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;

public interface NBTReadable {

    <N extends NBT<?>> N read(@NotNull String key);

    default <N extends NBT<?>> N read(@NotNull String key, @NotNull TagType<N> type) {
        return read(TagScheme.of(key, type));
    }

    default <N extends NBT<?>> N read(@NotNull TagScheme<?, N> tagScheme) {
        NBT<?> nbt = read(tagScheme.name());

        if (nbt == null) return null;

        return tagScheme.type().cast(nbt);
    }

    @SuppressWarnings("unchecked")
    default <T> T readValue(@NotNull String key) {
        return (T) read(key).value();
    }

    default Number readNumber(@NotNull String key) {
        return readValue(key);
    }

    default <T, N extends NBT<?>> T interpret(@NotNull TagScheme<T, N> tagScheme) {
        N nbt = this.read(tagScheme);
        return nbt == null ? null : tagScheme.interpretValue(nbt).orElse(null);
    }
}