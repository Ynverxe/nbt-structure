package com.github.ynverxe.nbt_structure.nbt.snbt;

import com.github.ynverxe.nbt_structure.nbt.NBT;
import com.github.ynverxe.nbt_structure.nbt.TagType;
import com.github.ynverxe.nbt_structure.nbt.exception.InvalidNBTTypeException;
import org.jetbrains.annotations.NotNull;

import static com.github.ynverxe.nbt_structure.nbt.TagType.*;
import static com.github.ynverxe.nbt_structure.nbt.NBTConstants.*;

public final class PrimitiveNBTValueConverter {

  private PrimitiveNBTValueConverter() {}

  public static @NotNull String convertToSNBT(@NotNull NBT<?> nbt) {
    if (!nbt.primitive()) {
      throw new InvalidNBTTypeException("NBT object doesn't have a primitive value");
    }

    TagType<?> type = nbt.type();
    Object value = nbt.value();

    if (type == STRING) {
      return stringToSNBT((String) nbt.value(), true);
    }

    return numberToSNBT(value);
  }

  public static @NotNull String stringToSNBT(@NotNull String value, boolean ensureEscape) {
    StringBuilder builder = new StringBuilder();
    builder.append(DOUBLE_QUOTE);
    for (char c : value.toCharArray()) {
      if (c == DOUBLE_QUOTE && ensureEscape) {
        builder.append("\\");
      }

      builder.append(c);
    }
    builder.append(DOUBLE_QUOTE);
    return builder.toString();
  }

  public static @NotNull String numberToSNBT(Object value) {
    TagType<?> type = matchType(value.getClass());

    if (type == INT) {
      return value.toString();
    }
    if (type == SHORT) {
      return value + "s";
    }
    if (type == BYTE) {
      return value + "b";
    }
    if (type == LONG) {
      return value + "L";
    }
    if (type == FLOAT) {
      return value + "f";
    }
    if (type == DOUBLE) {
      return value + "d";
    }

    throw new InvalidNBTTypeException(value + " is not a number");
  }
}