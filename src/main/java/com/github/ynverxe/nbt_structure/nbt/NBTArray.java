package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class NBTArray<T> extends NBT<T[]> implements Iterable<T> {

    NBTArray(T[] value) {
        super(value);
    }

    @Override
    public String toString() {
        return Arrays.toString(value);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return Arrays.asList(value).iterator();
    }
}