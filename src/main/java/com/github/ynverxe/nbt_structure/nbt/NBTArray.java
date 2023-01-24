package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NBTArray<T> extends NBT<T[]> {

    public NBTArray(T[] value) {
        super(value);
    }

    public @NotNull List<T> asList() {
        return new ArrayList<>(Arrays.asList(value));
    }
}