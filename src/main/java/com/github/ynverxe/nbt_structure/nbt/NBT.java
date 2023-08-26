package com.github.ynverxe.nbt_structure.nbt;

import com.github.ynverxe.nbt_structure.nbt.exception.InvalidNBTTypeException;
import java.util.*;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"rawtypes", "unchecked"})
public class NBT<T> implements Cloneable {

  private final @NotNull TagType<NBT<T>> type;
  protected @NotNull T value;

  public NBT(@NotNull T value, @NotNull TagType<NBT<T>> type) {
    this.value = Objects.requireNonNull(value, "value");
    this.type = Objects.requireNonNull(type, "type");
  }

  public NBT(@NotNull T value) {
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
    if (!type.javaType().isInstance(value)) return Optional.empty();

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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NBT<?> nbt = (NBT<?>) o;

    if (nbt.type.javaType().isArray()) {
      return Arrays.equals((Object[]) nbt.value, (Object[]) value);
    }

    return value.equals(nbt.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, value);
  }

  @Override
  public NBT<T> clone() {
    try {
      return (NBT<T>) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  public boolean primitive() {
    return true;
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

    Class clazz = value.getClass();
    Object primitiveArray = checkForPrimitiveArray(value, clazz);
    if (primitiveArray != null) return (T) primitiveArray;

    if (clazz.isArray()) {
      Object[] rawValues = (Object[]) value;
      return (T) new NBTArray<>(rawValues);
    }

    return (T) new NBT<>(value);
  }

  /**
   * Try to guess the array content type and return a {@link NBTArray}.
   *
   * @param raw The array content
   * @return a {@link NBTArray} with the contents of the provided array
   * @throws IllegalArgumentException If the array is empty or the first element is null
   * @throws InvalidNBTTypeException If the first element is not a valid NBT value, the type cannot
   *     be converted to an array or the array contains different types of NBT values
   * @throws NullPointerException If any array contains null values
   * @see NBTArray NBT Array types
   */
  public static @NotNull NBTArray<?> guessArrayByContent(Object[] raw)
      throws IllegalArgumentException, InvalidNBTTypeException, NullPointerException {
    if (raw.length == 0 || raw[0] == null) throw new IllegalArgumentException("empty array");

    Object first = raw[0];

    TagType<?> tagType = TagType.matchType(first.getClass());

    if (tagType == null)
      throw new InvalidNBTTypeException(first.getClass() + " is not a valid NBT type");

    NBTArray<?> array = null;

    if (tagType == TagType.BYTE) {
      array = new NBTArray<>(new Byte[raw.length]);
    }

    if (tagType == TagType.INT) {
      array = new NBTArray<>(new Integer[raw.length]);
    }

    if (tagType == TagType.LONG) {
      array = new NBTArray<>(new Long[raw.length]);
    }

    if (array == null) {
      throw new InvalidNBTTypeException(
          "Array content cannot be converted to a numeric array " + tagType);
    }

    for (Object element : raw) {
      if (element == null) throw new NullPointerException("Array cannot have null elements");

      if (!Objects.equals(TagType.matchType(element.getClass()), tagType)) {
        throw new InvalidNBTTypeException(element + " is not of the type " + tagType);
      }
    }

    System.arraycopy(raw, 0, array.value, 0, raw.length);
    return array;
  }

  private static NBTArray<?> checkForPrimitiveArray(Object value, Class clazz) {
    if (clazz == byte[].class) {
      return byteArray((byte[]) value);
    }

    if (clazz == int[].class) {
      return intArray((int[]) value);
    }

    if (clazz == long[].class) {
      return longArray((long[]) value);
    }

    return null;
  }

  public static @NotNull NBTList listOfNBTValues(@NotNull NBT... nbts) {
    return new NBTList().append((Object[]) nbts);
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

  public static @NotNull NBTArray<Byte> byteArray(byte[] bytes) {
    Byte[] byteArray = new Byte[bytes.length];

    for (int i = 0; i < bytes.length; i++) {
      byteArray[i] = bytes[i];
    }

    return new NBTArray<>(byteArray);
  }

  public static @NotNull NBTArray<Integer> intArray(int[] integers) {
    Integer[] intArray = new Integer[integers.length];

    for (int i = 0; i < integers.length; i++) {
      intArray[i] = integers[i];
    }

    return new NBTArray<>(intArray);
  }

  public static @NotNull NBTArray<Long> longArray(long[] longs) {
    Long[] longArray = new Long[longs.length];

    for (int i = 0; i < longs.length; i++) {
      longArray[i] = longs[i];
    }

    return new NBTArray<>(longArray);
  }
}
