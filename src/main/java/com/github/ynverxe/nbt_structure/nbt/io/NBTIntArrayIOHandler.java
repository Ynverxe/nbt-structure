package com.github.ynverxe.nbt_structure.nbt.io;

import com.github.ynverxe.nbt_structure.nbt.NBT;
import com.github.ynverxe.nbt_structure.nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTIntArrayIOHandler extends AbstractNBTIOHandler {

    @Override
    public void write(NBT<?> nbtValue, DataOutput dataOutput, int currentDepth) throws IOException {
        super.write(nbtValue, dataOutput, currentDepth);

        Integer[] intArray = nbtValue.getAs(TagType.INT_ARRAY);

        dataOutput.writeInt(intArray.length);
        for (int i : intArray) {
            dataOutput.writeInt(i);
        }
    }

    @Override
    public NBT<?> read(DataInput dataInput, int currentDepth) throws IOException {
        super.read(dataInput, currentDepth);

        int[] intArray = new int[dataInput.readInt()];

        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = dataInput.readInt();
        }

        return NBT.intArray(intArray);
    }
}