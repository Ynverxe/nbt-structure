package com.github.ynverxe.nbt_structure.nbt;

import java.util.Arrays;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

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

  @Override
  public boolean primitive() {
    return false;
  }
}
