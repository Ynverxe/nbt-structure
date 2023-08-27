package com.github.ynverxe.nbt_structure;

import static org.junit.jupiter.api.Assertions.*;

import com.github.ynverxe.nbt_structure.nbt.NBT;
import com.github.ynverxe.nbt_structure.nbt.NBTCompound;
import com.github.ynverxe.nbt_structure.nbt.io.NBTIOUtil;
import java.io.*;
import org.junit.jupiter.api.Test;

public class NBTIOTest {

  @Test
  public void testCompoundIO() throws IOException {
    File file = new File("compound.nbt");

    NBTIOUtil.saveCompound(allNBTTypes(), true, file);

    NBTCompound nbtCompound = NBTIOUtil.readCompound(file);
    assertEquals(allNBTTypes(), nbtCompound);
  }

  private static NBTCompound allNBTTypes() {
    NBTCompound nbtCompound = new NBTCompound();
    nbtCompound.write("string", "Text");
    nbtCompound.write("int", Integer.MAX_VALUE);
    nbtCompound.write("short", Short.MAX_VALUE);
    nbtCompound.write("byte", Byte.MAX_VALUE);
    nbtCompound.write("float", Float.MAX_VALUE);
    nbtCompound.write("long", Long.MAX_VALUE);
    nbtCompound.write("double", Double.MAX_VALUE);
    nbtCompound.write(
        "byte-array", NBT.byPossibleRawRepresenter(new Byte[] {-128, 1, 2, 3, 4, 5, 127}));
    nbtCompound.write("list", NBT.listOfRawValues("One", "Two", "Three"));
    nbtCompound.write("compound", NBT.compoundFromRawValues("One", 1, "Two", 2));
    nbtCompound.write(
        "long-array",
        NBT.byPossibleRawRepresenter(
            new Long[] {Long.MIN_VALUE, 1L, 2L, 3L, 4L, 5L, Long.MAX_VALUE}));
    nbtCompound.write(
        "int-array",
        NBT.byPossibleRawRepresenter(
            new Integer[] {Integer.MIN_VALUE, 1, 2, 3, 4, 5, Integer.MAX_VALUE}));

    return nbtCompound;
  }
}
