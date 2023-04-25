package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Supplier;

public interface NBTContainer extends NBTWritable, NBTReadable, Iterable<Tag<?>> {

    @NotNull Set<String> keys();

    default <T extends NBT<?>> @NotNull T writeIfAbsent(@NotNull String key, @NotNull Supplier<T> nbtSupplier) {
        T found = read(key);

        if (found == null) write(key, found = nbtSupplier.get());

        return found;
    }

    default @NotNull NBTCompound toCompound() {
        NBTCompound compound = new NBTCompound();
        forEach(compound::writeTag);
        return compound;
    }
}