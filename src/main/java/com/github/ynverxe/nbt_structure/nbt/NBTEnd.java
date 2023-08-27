package com.github.ynverxe.nbt_structure.nbt;

/** This class is a placeholder to identify an END Tag type. */
@SuppressWarnings("ALL")
public final class NBTEnd {

  public static final NBTEnd VALUE = new NBTEnd();

  private NBTEnd() {
    if (VALUE != null) throw new UnsupportedOperationException("non-instantiable");
  }
}
