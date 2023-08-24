package com.github.ynverxe.nbt_structure.nbt.io;

import com.github.ynverxe.nbt_structure.nbt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.jetbrains.annotations.NotNull;

public final class NBTIOUtil {

  private NBTIOUtil() {}

  public static void saveCompound(NBTCompound compound, boolean compress, File file)
      throws IOException {
    OutputStream outputStream = Files.newOutputStream(file.toPath());

    if (compress) {
      outputStream = new GZIPOutputStream(outputStream);
    }

    try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
      writeUnnamedTag(compound, dataOutputStream);
    }
  }

  public static @NotNull NBTCompound readCompound(File file) throws IOException {
    InputStream inputStream = Files.newInputStream(file.toPath());

    // detect gzip compression
    try {
      inputStream = new GZIPInputStream(inputStream);
    } catch (IOException ignored) {
      inputStream.close();
      inputStream = Files.newInputStream(file.toPath());
    }

    try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
      return (NBTCompound) readUnnamedTag(dataInputStream);
    }
  }

  public static void writeUnnamedTag(@NotNull NBT<?> nbt, @NotNull DataOutput dataOutput)
      throws IOException {
    writeNamedTag("", nbt, dataOutput, 0);
  }

  public static void writeUnnamedTag(
      @NotNull NBT<?> nbt, @NotNull DataOutput dataOutput, int valueDepth) throws IOException {
    writeNamedTag("", nbt, dataOutput, valueDepth);
  }

  public static void writeNamedTag(
      @NotNull String key, @NotNull NBT<?> nbt, @NotNull DataOutput dataOutput) throws IOException {
    TagType<?> type = nbt.type();

    dataOutput.write(type.id());
    if (type == TagType.END) {
      return;
    }

    dataOutput.writeUTF(key);
    type.nbtioHandler().write(nbt, dataOutput, 0);
  }

  public static void writeNamedTag(
      @NotNull String key, @NotNull NBT<?> nbt, @NotNull DataOutput dataOutput, int valueDepth)
      throws IOException {
    TagType<?> type = nbt.type();

    dataOutput.write(type.id());
    if (type == TagType.END) {
      return;
    }

    dataOutput.writeUTF(key);
    type.nbtioHandler().write(nbt, dataOutput, valueDepth);
  }

  public static NBT<?> readUnnamedTag(@NotNull DataInput dataInput) throws IOException {
    return readUnnamedTag(dataInput, 0);
  }

  public static NBT<?> readUnnamedTag(@NotNull DataInput dataInput, int valueDepth)
      throws IOException {
    byte id = dataInput.readByte();

    if (id == 0) return new NBT<>(NBTEnd.VALUE);

    TagType<?> type = TagType.byId(id);

    if (type == null) throw new RuntimeException("Unknown tag id " + id);

    dataInput.readUTF(); // ignore

    return readValue(type, dataInput, valueDepth);
  }

  public static Tag<?> readNamedTag(@NotNull DataInput dataInput, int valueDepth)
      throws IOException {
    byte id = dataInput.readByte();

    if (id == 0) return new Tag<>("", new NBT<>(NBTEnd.VALUE));

    TagType<?> type = TagType.byId(id);

    if (type == null) throw new RuntimeException("Unknown tag id " + id);

    String key = dataInput.readUTF();

    return new Tag<>(key, readValue(type, dataInput, valueDepth));
  }

  public static <N extends NBT<?>> @NotNull N readValue(
      @NotNull TagType<N> type, @NotNull DataInput dataInput) throws IOException {
    return readValue(type, dataInput, 0);
  }

  @SuppressWarnings("unchecked")
  public static <N extends NBT<?>> @NotNull N readValue(
      @NotNull TagType<N> type, @NotNull DataInput dataInput, int valueDepth) throws IOException {
    NBTIOHandler nbtioHandler = type.nbtioHandler();

    return (N) nbtioHandler.read(dataInput, valueDepth);
  }
}
