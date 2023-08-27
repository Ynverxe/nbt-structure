package com.github.ynverxe.nbt_structure;

import com.github.ynverxe.nbt_structure.nbt.NBT;
import com.github.ynverxe.nbt_structure.nbt.NBTCompound;
import com.github.ynverxe.nbt_structure.nbt.NBTList;
import com.github.ynverxe.nbt_structure.nbt.snbt.SNBTReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SNBTTest {

  private static final NBTCompound TEST_COMPOUND = new NBTCompound();
  private static final String TEST_SNBT = readFile(new File("src/test/compound.snbt"));
  private static final String TEST_SNBT_NO_WHITESPACES = readFile(new File("src/test/compound_no_whitespaces.snbt"));

  static {
    TEST_COMPOUND.write("bytes", new byte[] {1, 2, 3});
    TEST_COMPOUND.write("integers", new int[] {1, 2, 3});
    TEST_COMPOUND.write("empty_compound", new NBTCompound());

    NBTList objects = new NBTList();

    objects.add(NBT.compoundFromRawValues("someText", "blabla"));
    objects.add(NBT.compoundFromRawValues("someNumber", 1.18198));
    objects.add(NBT.compoundFromRawValues("emptyList", new NBTList()));

    TEST_COMPOUND.write("objects", objects);
  }

  @Test
  public void testTextToNBT() {
    assertDoesNotThrow(() -> assertEquals(TEST_COMPOUND, SNBTReader.readSNBT(TEST_SNBT)));
  }

  @Test
  public void testNBTToText() {
    assertEquals(TEST_SNBT_NO_WHITESPACES, TEST_COMPOUND.toSNBT());
  }

  private static String readFile(File file) {
    StringBuilder builder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String str;
      while ((str = reader.readLine()) != null) {
        builder.append(str);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return builder.toString();
  }
}
