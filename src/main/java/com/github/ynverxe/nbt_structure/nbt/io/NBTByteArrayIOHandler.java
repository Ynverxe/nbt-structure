package com.github.ynverxe.nbt_structure.nbt.io;

import com.github.ynverxe.nbt_structure.nbt.NBT;
import com.github.ynverxe.nbt_structure.nbt.NBTArray;
import com.github.ynverxe.nbt_structure.nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTByteArrayIOHandler extends AbstractNBTIOHandler {

    @Override
    public void write(NBT<?> nbtValue, DataOutput dataOutput, int currentDepth) throws IOException {
        super.write(nbtValue, dataOutput, currentDepth);

        Byte[] bytes = nbtValue.getAs(TagType.BYTE_ARRAY);
        dataOutput.writeInt(bytes.length);
        for (Byte aByte : bytes) {
            dataOutput.write(aByte);
        }
    }

    @Override
    public NBT<?> read(DataInput dataInput, int currentDepth) throws IOException {
        super.read(dataInput, currentDepth);

        byte[] buffer = new byte[dataInput.readInt()];

        dataInput.readFully(buffer);

        return NBTArray.byteArray(buffer);
    }
}