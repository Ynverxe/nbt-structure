package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"rawtypes", "unused", "UnusedReturnValue"})
public class NBTList extends NBT<List<NBT>> implements List<NBT>, Cloneable {

    public NBTList() {
        this(Collections.emptyList());
    }

    protected NBTList(List<NBT> value) {
        super(new ArrayList<>());

        append(value.toArray());
    }

    public @NotNull NBTList append(@NotNull Object... someValues) {
        for (Object someValue : someValues) {
            Object value = someValue;

            if (!(value instanceof NBT)) value = byPossibleRawRepresenter(value);

            this.value.add((NBT) value);
        }

        return this;
    }

    public <T extends NBT<?>> @Nullable T findTag(int index, @NotNull TagType<T> tagType) {
        if (index >= value.size()) return null;

        NBT nbt = value.get(index);

        return tagType.cast(nbt);
    }

    public <T extends NBT<?>> @NotNull Optional<T> optionalTagFind(int index, @NotNull TagType<T> tagType) {
        return Optional.ofNullable(findTag(index, tagType));
    }

    public <T> @Nullable T findTagValue(int index, @NotNull TagType<NBT<T>> tagType) {
        return optionalTagFind(index, tagType).map(NBT::value).orElse(null);
    }

    public <T> @NotNull Optional<T> optionalFindTagValue(int index, @NotNull TagType<NBT<T>> tagType) {
        return optionalTagFind(index, tagType).map(NBT::value);
    }

    @Override
    public NBTList clone() {
        return (NBTList) super.clone();
    }

    @Override
    public @NotNull Object normalize() {
        return value.stream()
                .map(NBT::normalize)
                .collect(Collectors.toList());
    }

    public int size() {
        return value.size();
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return value.contains(o);
    }

    @Override
    public @NotNull Iterator<NBT> iterator() {
        return value.iterator();
    }

    @Override
    public Object[] toArray() {
        return value.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return value.toArray(a);
    }

    @Override
    public boolean add(NBT nbt) {
        return value.add(nbt);
    }

    @Override
    public boolean remove(Object o) {
        return value.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return value.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends NBT> c) {
        return value.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends NBT> c) {
        return value.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return value.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return value.retainAll(c);
    }

    @Override
    public void clear() {
        value.clear();
    }

    @Override
    public NBT get(int index) {
        return value.get(index);
    }

    @Override
    public NBT set(int index, NBT element) {
        return value.set(index, element);
    }

    @Override
    public void add(int index, NBT element) {
        value.add(index, element);
    }

    @Override
    public NBT remove(int index) {
        return value.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return value.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return value.indexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<NBT> listIterator() {
        return value.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<NBT> listIterator(int index) {
        return value.listIterator();
    }

    @NotNull
    @Override
    public List<NBT> subList(int fromIndex, int toIndex) {
        return value.subList(fromIndex, toIndex);
    }
}