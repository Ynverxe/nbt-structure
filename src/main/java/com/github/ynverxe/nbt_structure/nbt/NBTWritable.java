package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NBTWritable {

  NBT<?> write(@NotNull String key, @Nullable Object value);

  default NBT<?> writeTag(@NotNull Tag<?> tag) {
    return write(tag.name(), tag.value());
  }

  default NBT<?> erase(@NotNull String key) {
    return write(key, null);
  }
}
