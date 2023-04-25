package com.github.ynverxe.nbt_structure.nbt.io;

import com.github.ynverxe.nbt_structure.nbt.NBT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface NBTIOHandler {

    void write(NBT<?> nbtValue, DataOutput dataOutput, int currentDepth) throws IOException;

    NBT<?> read(DataInput dataInput, int currentDepth) throws IOException;

}