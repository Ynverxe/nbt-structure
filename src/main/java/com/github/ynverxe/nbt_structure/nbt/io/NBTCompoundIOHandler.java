package com.github.ynverxe.nbt_structure.nbt.io;

import com.github.ynverxe.nbt_structure.nbt.NBT;
import com.github.ynverxe.nbt_structure.nbt.NBTCompound;
import com.github.ynverxe.nbt_structure.nbt.Tag;
import com.github.ynverxe.nbt_structure.nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.rmi.RemoteException;

public class NBTCompoundIOHandler extends AbstractNBTIOHandler {

    @Override
    public void write(NBT<?> nbtValue, DataOutput dataOutput, int currentDepth) throws IOException {
        super.write(nbtValue, dataOutput, currentDepth);

        NBTCompound compound = (NBTCompound) nbtValue;

        for (Tag<?> tag : compound) {
            String name = tag.name();
            NBT<?> value = tag.value();

            NBTIOUtil.writeNamedTag(name, value, dataOutput, currentDepth);
        }

        dataOutput.writeByte(0);
    }

    @Override
    public NBT<?> read(DataInput dataInput, int currentDepth) throws IOException {
        super.read(dataInput, currentDepth);

        byte id;

        NBTCompound compound = new NBTCompound();
        while ((id = dataInput.readByte()) != 0) {
            TagType<?> type = TagType.byId(id);

            if (type == null)
                throw new RemoteException("unknown tag id " + id);

            String tagName = dataInput.readUTF();

            NBTIOHandler handler = type.nbtioHandler();
            NBT<?> nbt = handler.read(dataInput, currentDepth + 1);
            compound.write(tagName, nbt);
        }

        return compound;
    }
}