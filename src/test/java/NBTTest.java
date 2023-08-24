import static org.junit.jupiter.api.Assertions.*;

import com.github.ynverxe.nbt_structure.nbt.*;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class NBTTest {

  @Test
  public void testTag() {
    NBTCompound compound = new NBTCompound();

    NBTCompound air =
        NBT.compoundFromRawValues("itemType", "minecraft:air", "amount", 0, "damage", 0);
    compound.write("item", air);

    SomeProperties airProperties = new SomeProperties("minecraft:air", 0, (short) 0);

    assertEquals(airProperties, compound.interpret("item", TagType.COMPOUND, SomeProperties::new));
  }

  @Test
  public void testDefaultsNBT() {
    assertDoesNotThrow(this::allNBTTypes);
  }

  @Test
  public void testNBTArrayCreation() {
    NBT.byteArray(new byte[0]);
    NBT.byPossibleRawRepresenter(new byte[0]);
    NBT.byPossibleRawRepresenter(new Byte[0]);
  }

  @Test
  public void testEndNBTTagAddition() {
    NBTList list = new NBTList();

    list.add(new NBT<>("first element"));

    NBT<NBTEnd> endNBT = new NBT<>(NBTEnd.VALUE);
    assertThrows(RuntimeException.class, () -> list.add(endNBT));
    assertThrows(RuntimeException.class, () -> list.add(0, endNBT));
    assertThrows(RuntimeException.class, () -> list.append(endNBT));
    assertThrows(RuntimeException.class, () -> list.set(0, endNBT));
    assertThrows(RuntimeException.class, () -> list.replaceAll(nbt -> endNBT));
    assertThrows(RuntimeException.class, () -> list.addAll(Collections.singletonList(endNBT)));
    assertThrows(RuntimeException.class, () -> list.addAll(0, Collections.singletonList(endNBT)));
    assertThrows(RuntimeException.class, () -> new NBTList(TagType.END));

    NBTCompound compound = new NBTCompound();

    compound.write("key", new NBT<>("blah"));

    assertThrows(RuntimeException.class, () -> compound.write("", endNBT));
    assertThrows(RuntimeException.class, () -> compound.writeTag(new Tag<>("", endNBT)));
  }

  @Test
  public void cloneNBT() {
    NBTCompound clonedCompound = allNBTTypes().clone();

    assertEquals(clonedCompound, allNBTTypes());
  }

  protected NBTCompound allNBTTypes() {
    NBTCompound nbtCompound = new NBTCompound();
    nbtCompound.write("string", "Text");
    nbtCompound.write("int", Integer.MAX_VALUE);
    nbtCompound.write("short", Short.MAX_VALUE);
    nbtCompound.write("byte", Byte.MAX_VALUE);
    nbtCompound.write("float", Float.MAX_VALUE);
    nbtCompound.write("long", Long.MAX_VALUE);
    nbtCompound.write("double", Double.MAX_VALUE);
    nbtCompound.write(
        "byte-array", NBT.byPossibleRawRepresenter(new Byte[] {-128, 1, 2, 3, 4, 5, 127}));
    nbtCompound.write("list", NBT.listOfRawValues("One", "Two", "Three"));
    nbtCompound.write("compound", NBT.compoundFromRawValues("One", 1, "Two", 2));
    nbtCompound.write(
        "long-array",
        NBT.byPossibleRawRepresenter(
            new Long[] {Long.MIN_VALUE, 1L, 2L, 3L, 4L, 5L, Long.MAX_VALUE}));
    nbtCompound.write(
        "int-array",
        NBT.byPossibleRawRepresenter(
            new Integer[] {Integer.MIN_VALUE, 1, 2, 3, 4, 5, Integer.MAX_VALUE}));

    return nbtCompound;
  }
}
