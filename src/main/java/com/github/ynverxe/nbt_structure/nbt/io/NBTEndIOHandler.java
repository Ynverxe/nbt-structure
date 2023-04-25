package com.github.ynverxe.nbt_structure.nbt.io;

import com.github.ynverxe.nbt_structure.nbt.NBT;
import com.github.ynverxe.nbt_structure.nbt.NBTEnd;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTEndIOHandler extends AbstractNBTIOHandler {

    @Override
    public void write(NBT<?> nbtValue, DataOutput dataOutput, int currentDepth) throws IOException {
        super.write(nbtValue, dataOutput, currentDepth);
        dataOutput.writeByte(0);
    }

    @Override
    public NBT<?> read(DataInput dataInput, int currentDepth) throws IOException {
        super.read(dataInput, currentDepth);

        byte id = dataInput.readByte();

        if (id != 0)
            throw new RuntimeException("The tag type id found doesn't correspond to the END tag type id (0, found = " + id + ")");

        return new NBT<>(NBTEnd.VALUE);
    }
}