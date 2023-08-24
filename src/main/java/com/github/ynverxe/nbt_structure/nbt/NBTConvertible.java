package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;

public interface NBTConvertible<T extends NBT<?>> {

  @NotNull
  T toNBT();
}
