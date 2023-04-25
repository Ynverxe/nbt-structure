package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@SuppressWarnings({"rawtypes", "unused", "UnusedReturnValue"})
public class NBTList extends NBT<List<NBT>> implements List<NBT>, Cloneable {

    private TagType<?> type;

    public NBTList() {
        this(null);
    }

    public NBTList(@Nullable TagType<?> type) {
        this(new ArrayList<>(), type);
    }

    public NBTList(@NotNull List<NBT> value, @Nullable TagType<?> type) {
        super(value);

        if (type == TagType.END)
            throw new IllegalArgumentException("cannot create a list of end tags");

        this.type = type;
    }

    @Override
    public @NotNull List<NBT> set(List<NBT> value) {
        value.forEach(this::checkIncomingElement);
        return super.set(value);
    }

    @Override
    public @NotNull List<Object> normalize() {
        return value.stream()
                .map(NBT::normalize)
                .collect(Collectors.toList());
    }

    @Override
    public NBTList clone() {
        return (NBTList) super.clone();
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
    public <T> T[] toArray(T @NotNull [] a) {
        return value.toArray(a);
    }

    @Override
    public boolean add(NBT nbt) {
        checkIncomingElement(nbt);
        return value.add(nbt);
    }

    @Override
    public boolean remove(Object o) {
        return value.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(value).containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends NBT> c) {
        c.forEach(this::checkIncomingElement);
        return value.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends NBT> c) {
        c.forEach(this::checkIncomingElement);
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
    public void replaceAll(UnaryOperator<NBT> operator) {
        List.super.replaceAll(nbt -> {
            NBT<?> newValue = operator.apply(nbt);

            checkIncomingElement(newValue);
            return newValue;
        });
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
        checkIncomingElement(element);
        return value.set(index, element);
    }

    @Override
    public void add(int index, NBT element) {
        checkIncomingElement(element);
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
        return value.lastIndexOf(o);
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

    public @Nullable TagType<?> elementType() {
        return type;
    }

    public @NotNull NBTList append(@NotNull Object... someValues) {
        for (Object someValue : someValues) {
            Object value = someValue;

            if (!(value instanceof NBT)) value = byPossibleRawRepresenter(value);

            checkIncomingElement((NBT) value);
            this.value.add((NBT) value);
        }

        return this;
    }

    public <T extends NBT<?>> @Nullable T findTag(int index, @NotNull TagType<T> tagType) {
        if (index >= value.size()) return null;

        NBT nbt = value.get(index);

        return tagType.cast(nbt);
    }

    private void checkIncomingElement(NBT value) {
        if (value.type() == TagType.END)
            throw new IllegalArgumentException("Lists cannot hold end nbt tags");

        checkForMissingElementType(value);

        if (value.type() != type)
            throw new IllegalArgumentException("This list only accepts values of type: " + value.type().id());
    }

    private void checkForMissingElementType(NBT value) {
        if (type != null) return;

        type = value.type();
    }
}