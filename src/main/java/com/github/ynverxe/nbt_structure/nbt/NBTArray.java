package com.github.ynverxe.nbt_structure.nbt;

import com.github.ynverxe.nbt_structure.nbt.snbt.PrimitiveNBTValueConverter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

import static com.github.ynverxe.nbt_structure.nbt.TagType.*;

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

  @Override
  public void toSNBT(Appendable appendable) throws IOException {
    appendable.append("[")
        .append(numberClassifierChar(type()))
        .append(";");

    for (int i = 0; i < value.length; i++) {
      appendable.append(PrimitiveNBTValueConverter.numberToSNBT(value[i]));

      if (i + 1 < value.length) {
        appendable.append(",");
      }
    }

    appendable.append("]");
  }

  private static char numberClassifierChar(TagType<?> type) {
    if (type == INT_ARRAY) {
      return 'I';
    }

    if (type == LONG_ARRAY) {
      return 'L';
    }

    if (type == BYTE_ARRAY) {
      return 'B';
    }

    throw new RuntimeException("Invalid NBT array type");
  }
}
