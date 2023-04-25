package com.github.ynverxe.nbt_structure.nbt.io;

import com.github.ynverxe.nbt_structure.nbt.NBT;
import com.github.ynverxe.nbt_structure.nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTStringIOHandler extends AbstractNBTIOHandler {

    @Override
    public void write(NBT<?> nbtValue, DataOutput dataOutput, int currentDepth) throws IOException {
        super.write(nbtValue, dataOutput, currentDepth);

        dataOutput.writeUTF(nbtValue.getAs(TagType.STRING));
    }

    @Override
    public NBT<?> read(DataInput dataInput, int currentDepth) throws IOException {
        super.read(dataInput, currentDepth);

        return new NBT<>(dataInput.readUTF());
    }
}