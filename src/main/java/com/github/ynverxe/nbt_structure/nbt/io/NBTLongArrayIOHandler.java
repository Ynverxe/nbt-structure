package com.github.ynverxe.nbt_structure.nbt.io;

import com.github.ynverxe.nbt_structure.nbt.NBT;
import com.github.ynverxe.nbt_structure.nbt.TagType;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTLongArrayIOHandler extends AbstractNBTIOHandler {

  @Override
  public void write(NBT<?> nbtValue, DataOutput dataOutput, int currentDepth) throws IOException {
    super.write(nbtValue, dataOutput, currentDepth);

    Long[] intArray = nbtValue.getAs(TagType.LONG_ARRAY);

    dataOutput.writeInt(intArray.length);
    for (long i : intArray) {
      dataOutput.writeLong(i);
    }
  }

  @Override
  public NBT<?> read(DataInput dataInput, int currentDepth) throws IOException {
    super.read(dataInput, currentDepth);

    long[] longArray = new long[dataInput.readInt()];

    for (int i = 0; i < longArray.length; i++) {
      longArray[i] = dataInput.readLong();
    }

    return NBT.longArray(longArray);
  }
}
