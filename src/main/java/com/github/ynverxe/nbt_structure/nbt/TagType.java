package com.github.ynverxe.nbt_structure.nbt;

import com.github.ynverxe.nbt_structure.nbt.io.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("rawtypes, unchecked, unused")
public class TagType<T extends NBT> {

    public static final TagType<NBT<NBTEnd>> END = new TagType<>(new NBTEndIOHandler(), NBTEnd.class, 0, 0);
    public static final TagType<NBT<Byte>> BYTE = new TagType<>(new NBTByteIOHandler(), Byte.class, 1, 1);
    public static final TagType<NBT<Short>> SHORT = new TagType<>(new NBTShortIOHandler(), Short.class, 2, 2);
    public static final TagType<NBT<Integer>> INT = new TagType<>(new NBTIntegerIOHandler(), Integer.class, 3, 4);
    public static final TagType<NBT<Long>> LONG = new TagType<>(new NBTLongIOHandler(), Long.class, 4, 8);
    public static final TagType<NBT<Float>> FLOAT = new TagType<>(new NBTFloatIOHandler(), Float.class, 5, 4);
    public static final TagType<NBT<Double>> DOUBLE = new TagType<>(new NBTDoubleIOHandler(), Double.class, 6, 8);
    public static final TagType<NBT<Byte[]>> BYTE_ARRAY = new TagType<>(new NBTByteArrayIOHandler(), Byte[].class, 7);
    public static final TagType<NBT<String>> STRING = new TagType<>(new NBTStringIOHandler(), String.class, 8);
    public static final TagType<NBTList> LIST = new TagType<>(
            new NBTListIOHandler(),
            NBTList.class,
            List.class,
            9
    );
    public static final TagType<NBTCompound> COMPOUND = new TagType<>(
            new NBTCompoundIOHandler(),
            NBTCompound.class,
            Map.class,
            10
    );
    public static final TagType<NBT<Integer[]>> INT_ARRAY = new TagType<>(new NBTIntArrayIOHandler(), Integer[].class, 11);
    public static final TagType<NBT<Long[]>> LONG_ARRAY = new TagType<>(new NBTLongArrayIOHandler(), Long[].class, 12);

    public static final Map<Class, TagType> VALUES = Collections.unmodifiableMap(mapOf(
            END,
            BYTE,
            SHORT,
            INT,
            LONG,
            FLOAT,
            DOUBLE,
            BYTE_ARRAY,
            STRING,
            LIST,
            COMPOUND,
            INT_ARRAY,
            LONG_ARRAY
    ));

    private final NBTIOHandler nbtioHandler;
    private final Class<T> representerType;
    private final Class<?> holdingType;
    private final int id;
    private final byte size;

    private TagType(NBTIOHandler nbtioHandler, Class representerType, Class<?> holdingType, int id, int size) {
        this.nbtioHandler = nbtioHandler;
        this.representerType = representerType;
        this.holdingType = holdingType;
        this.id = id;
        this.size = (byte) size;
    }

    private TagType(NBTIOHandler nbtioHandler, Class representerType, Class holdingType, int id) {
        this(nbtioHandler, representerType, holdingType, id, -1);
    }

    private TagType(NBTIOHandler nbtioHandler, Class holdingType, int id, int size) {
        this(nbtioHandler, NBT.class, holdingType, id, size);
    }

    private TagType(NBTIOHandler nbtioHandler, Class holdingType, int id) {
        this(nbtioHandler, NBT.class, holdingType, id);
    }

    public NBTIOHandler nbtioHandler() {
        return nbtioHandler;
    }

    public Class<T> representerType() {
        return representerType;
    }

    public Class<?> javaType() {
        return holdingType;
    }

    public int id() {
        return id;
    }

    public byte size() {
        return size;
    }

    public <R extends NBT<?>> R cast(NBT<?> nbt) {
        return nbt != null && nbt.type().equals(this) ? (R) nbt : null;
    }

    @Override
    public String toString() {
        return "NBTType{" +
                "representerType=" + representerType +
                ", holdingType=" + holdingType +
                ", id=" + id +
                ", size=" + size +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagType<?> tagType = (TagType<?>) o;
        return id == tagType.id && size == tagType.size && representerType.equals(tagType.representerType) && holdingType.equals(tagType.holdingType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(representerType, holdingType, id, size);
    }

    public static @Nullable <T extends NBT> TagType<T> matchType(@NotNull Class<?> clazz) {
        for (Map.Entry<Class, TagType> entry : VALUES.entrySet()) {
            if (entry.getKey().isAssignableFrom(clazz)) return entry.getValue();
        }

        return null;
    }

    public static @Nullable <T extends NBT> TagType<T> byId(int id) {
        for (Map.Entry<Class, TagType> entry : VALUES.entrySet()) {
            if (entry.getValue().id == id) return entry.getValue();
        }

        return null;
    }

    private static Map<Class, TagType> mapOf(TagType... types) {
        Map<Class, TagType> map = new HashMap<>();

        for (TagType type : types) {
            map.put(type.holdingType, type);
        }

        return map;
    }
}