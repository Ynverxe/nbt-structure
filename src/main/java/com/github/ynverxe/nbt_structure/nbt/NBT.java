package com.github.ynverxe.nbt_structure.nbt;

import com.github.ynverxe.nbt_structure.nbt.exception.InvalidNBTTypeException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class NBT<T> implements Cloneable {

    private final TagType<NBT<T>> type;
    protected T value;

    public NBT(T value, TagType<NBT<T>> type) {
        this.value = Objects.requireNonNull(value, "value");
        this.type = Objects.requireNonNull(type, "type");
    }

    public NBT(T value) {
        this.value = Objects.requireNonNull(value, "value");
        TagType<NBT<T>> type = TagType.matchType(value.getClass());
        if (type == null)
            throw new InvalidNBTTypeException(value.getClass() + " is not a valid NBT type");
        this.type = type;
    }

    public @NotNull TagType<NBT<T>> type() {
        return type;
    }

    public @NotNull T value() {
        return value;
    }

    public @NotNull T set(T value) {
        Objects.requireNonNull(value, "value");
        T previous = this.value;
        this.value = value;
        return previous;
    }

    public @NotNull <S> S getAs(@NotNull TagType<NBT<S>> type) {
        return (S) value;
    }

    public @NotNull <S> Optional<S> optionalGetAs(@NotNull TagType<NBT<S>> type) {
        if (!type.javaType().isInstance(value))
            return Optional.empty();

        return Optional.of((S) value);
    }

    public @NotNull Object normalize() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public NBT<T> clone() {
        try {
            return (NBT<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static @NotNull <T extends NBT<?>> T byPossibleRawRepresenter(@NotNull Object value) {
        if (value instanceof NBT) return (T) value;

        if (value instanceof NBTConvertible) {
            return (T) ((NBTConvertible<?>) value).toNBT();
        }

        if (value instanceof List) {
            NBTList list = new NBTList();

            ((List<?>) value).forEach(list::append);

            return (T) list;
        }

        if (value instanceof Map) {
            NBTCompound compound = new NBTCompound();

            ((Map<String, ?>) value).forEach(compound::write);

            return (T) compound;
        }

        if (value.getClass().isArray()) {
            Object[] rawValues = (Object[]) value;

            return (T) new NBTArray<>(rawValues);
        }

        return (T) new NBT<>(value);
    }

    public static @NotNull NBTList listOfNBTValues(@NotNull NBT... nbts) {
        return new NBTList(Arrays.asList(nbts));
    }

    public static @NotNull NBTList listOfRawValues(@NotNull Object... values) {
        NBTList list = new NBTList();

        list.append(values);

        return list;
    }

    public static @NotNull NBTCompound compoundFromNBTValues(@NotNull Map<String, NBT> nbtMap) {
        return new NBTCompound(nbtMap);
    }

    public static @NotNull NBTCompound compoundFromRawValues(@NotNull Object... pairs) {
        if (pairs.length % 2 != 0) throw new IllegalStateException("non-pair pairs");
        Map<String, NBT> nbtMap = new HashMap<>();
        for (int i = 0; i < pairs.length; i++) {
            nbtMap.put(pairs[i++].toString(), byPossibleRawRepresenter(pairs[i]));
        }
        return new NBTCompound(nbtMap);
    }
}