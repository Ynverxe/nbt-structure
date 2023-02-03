package com.github.ynverxe.nbt_structure.nbt;

import com.github.ynverxe.nbt_structure.nbt.tree.TreeNBTContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SuppressWarnings({"rawtypes", "unchecked", "UnusedReturnValue", "unused"})
public class NBTCompound extends NBT<Map<String, NBT>>
        implements Map<String, NBT>, TreeNBTContainer, Cloneable {

    public NBTCompound() {
        this(Collections.emptyMap());
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

    @Override
    public int size() {
        return value.size();
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return value.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.value.containsValue(value);
    }

    @Override
    public NBT get(Object key) {
        return value.get(key);
    }

    @Nullable
    @Override
    public NBT put(String key, NBT value) {
        return this.value.put(key, value);
    }

    @Override
    public NBT remove(Object key) {
        return value.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends NBT> m) {
        value.putAll(m);
    }

    @Override
    public void clear() {
        value.clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return value.keySet();
    }

    @NotNull
    @Override
    public Collection<NBT> values() {
        return value.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, NBT>> entrySet() {
        return value.entrySet();
    }
}