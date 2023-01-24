package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@SuppressWarnings({"rawtypes"})
public class NBTList extends NBT<List<NBT>> implements Cloneable {

    public NBTList() {
        this(new CopyOnWriteArrayList<>());
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
}