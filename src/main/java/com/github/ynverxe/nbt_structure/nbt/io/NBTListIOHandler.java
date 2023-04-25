package com.github.ynverxe.nbt_structure.nbt.io;

import com.github.ynverxe.nbt_structure.nbt.NBT;
import com.github.ynverxe.nbt_structure.nbt.NBTList;
import com.github.ynverxe.nbt_structure.nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.rmi.RemoteException;

public class NBTListIOHandler extends AbstractNBTIOHandler {

    @Override
    public void write(NBT<?> nbtValue, DataOutput dataOutput, int currentDepth) throws IOException {
        super.write(nbtValue, dataOutput, currentDepth);

        NBTList list = (NBTList) nbtValue;

        TagType<?> elementType = list.elementType();
        dataOutput.writeByte(elementType != null ? elementType.id() : 0);
        dataOutput.writeInt(list.size());

        NBTIOHandler nbtioHandler = elementType.nbtioHandler();

        for (NBT nbt : list) {
            nbtioHandler.write(nbt, dataOutput, currentDepth);
        }
    }

    @Override
    public NBT<?> read(DataInput dataInput, int currentDepth) throws IOException {
        super.read(dataInput, currentDepth);

        byte elementTypeId = dataInput.readByte();
        int size = dataInput.readInt();

        if (elementTypeId == 0) {
            if (size != 0) {
                throw new RemoteException("Missing list element type");
            }

            return new NBTList();
        }

        TagType<?> elementType = TagType.byId(elementTypeId);
        NBTList list = new NBTList(elementType);

        NBTIOHandler nbtioHandler = elementType.nbtioHandler();
        for (int i = 0; i < size; i++) {
            list.add(nbtioHandler.read(dataInput, currentDepth));
        }

        return list;
    }
}