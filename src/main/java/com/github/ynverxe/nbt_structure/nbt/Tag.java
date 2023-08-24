package com.github.ynverxe.nbt_structure.nbt;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("rawtypes")
public final class Tag<N extends NBT> {

  public static final Tag<NBT<NBTEnd>> END_TAG = new Tag<>("", new NBT<>(NBTEnd.VALUE));

  private final @NotNull String name;
  private final @NotNull N value;

  public Tag(@NotNull String name, @NotNull N value) {
    if (name.isEmpty() && value.type() != TagType.END) {
      throw new IllegalArgumentException("name cannot be empty");
    }

    this.name = name;
    this.value = value;
  }

  public String name() {
    return name;
  }

  public N value() {
    return value;
  }
}
