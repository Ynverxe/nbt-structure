package com.github.ynverxe.nbt_structure.nbt;

import com.github.ynverxe.nbt_structure.nbt.tree.TreeNBTContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SuppressWarnings({"rawtypes", "unchecked", "UnusedReturnValue", "unused"})
public class NBTCompound extends NBT<Map<String, NBT>> implements Cloneable, TreeNBTContainer {

    public NBTCompound() {
        this(new ConcurrentHashMap<>());
    }

    protected NBTCompound(Map<String, NBT> value) {
        super(new ConcurrentHashMap<>());

        value.forEach(this::write);
    }

    @Override
    public NBTCompound clone() {
        return (NBTCompound) super.clone();
    }

    @Override
    public @NotNull Object normalize() {
        return value.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().normalize()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public @Nullable NBT<?> write(@NotNull String key, @Nullable Object value) {
        if (value == null) return erase(key);

        NBT nbt = value instanceof NBT ? (NBT) value : byPossibleRawRepresenter(value);

        return this.value.put(key, nbt);
    }

    @Override
    public <N extends NBT<?>> N read(@NotNull String key) {
        return (N) value.get(key);
    }

    @Override
    public @Nullable NBT<?> erase(@NotNull String key) {
        return value.remove(key);
    }

    @Override
    public @NotNull Set<String> keys() {
        return value.keySet();
    }

    @Override
    public @NotNull NBTCompound toCompound() {
        return clone();
    }

    @Override
    public TreeNBTContainer createNewBranch() {
        return new NBTCompound();
    }
}