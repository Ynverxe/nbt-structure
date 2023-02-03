package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.BiConsumer;

public interface NBTContainer extends NBTWritable, NBTReadable {

    @NotNull Set<String> keys();

    default @NotNull NBTCompound toCompound() {
        NBTCompound compound = new NBTCompound();
        performFor((k, v) -> compound.write(k, v.clone()));
        return compound;
    }

    default void performFor(@NotNull BiConsumer<String, NBT<?>> consumer) {
        keys().forEach(key -> consumer.accept(key, read(key)));
    }
}