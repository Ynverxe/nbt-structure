package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NBTWritable {

    @Nullable NBT<?> write(@NotNull String key, @Nullable Object value);

    default @Nullable NBT<?> write(@NotNull TagScheme<?, ?> tagScheme) {
        NBT<?> nbt = tagScheme.createDefaultNBT().orElseThrow(() -> new IllegalArgumentException("null nbt"));

        return write(tagScheme.fullKey(), nbt);
    }

    default @Nullable NBT<?> erase(@NotNull String key) {
        return write(key, null);
    }
}