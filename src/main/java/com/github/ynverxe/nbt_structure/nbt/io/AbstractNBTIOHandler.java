package com.github.ynverxe.nbt_structure.nbt.io;

import com.github.ynverxe.nbt_structure.nbt.NBT;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AbstractNBTIOHandler implements NBTIOHandler {

    @Override
    public void write(NBT<?> nbtValue, DataOutput dataOutput, int currentDepth) throws IOException {
        if (currentDepth > NBTIOConstants.MAX_DEPTH) {
            throw new RuntimeException("NBT depth is too big! max = 512");
        }
    }

    @Override
    public NBT<?> read(DataInput dataInput, int currentDepth) throws IOException {
        if (currentDepth > NBTIOConstants.MAX_DEPTH) {
            throw new RuntimeException("NBT depth is too big! max = 512");
        }

        return null;
    }
}