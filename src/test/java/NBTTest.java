import com.github.ynverxe.nbt_structure.nbt.*;
import com.github.ynverxe.nbt_structure.nbt.exception.InvalidNBTTypeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NBTTest {

    @Test
    public void testTag() {
        NBTCompound compound = new NBTCompound();

        NBTCompound air = NBT.compoundFromRawValues("itemType", "minecraft:air", "amount", 0, "damage", 0);
        compound.write("item", air);

        SomeProperties airProperties = new SomeProperties(
                "minecraft:air",
                0,
                (short) 0
        );

        TagScheme<SomeProperties, NBTCompound> tagScheme = TagScheme.builder("item", SomeProperties.class, TagType.COMPOUND)
                .interpreter(SomeProperties::new)
                .build();

        assertEquals(airProperties, compound.interpret(tagScheme));
    }

    @Test
    public void testDefaultsNBT() {
        NBTCompound nbtCompound = new NBTCompound();
        nbtCompound.write("string", "Text");
        nbtCompound.write("int", Integer.MAX_VALUE);
        nbtCompound.write("short", Short.MAX_VALUE);
        nbtCompound.write("byte", Byte.MAX_VALUE);
        nbtCompound.write("float", Float.MAX_VALUE);
        nbtCompound.write("long", Long.MAX_VALUE);
        nbtCompound.write("double", Double.MAX_VALUE);
        nbtCompound.write("byte-array", NBT.byPossibleRawRepresenter(new Byte[] {-128, 1, 2, 3, 4, 5, 127}));
        nbtCompound.write("list", NBT.listOfRawValues("One", "Two","Three"));
        nbtCompound.write("compound", NBT.compoundFromRawValues("One", 1, "Two", 2));
        nbtCompound.write("long-array", NBT.byPossibleRawRepresenter(new Long[] {Long.MIN_VALUE, 1L, 2L, 3L, 4L, 5L, Long.MAX_VALUE}));
        nbtCompound.write("int-array", NBT.byPossibleRawRepresenter(new Integer[] {Integer.MIN_VALUE, 1, 2, 3, 4, 5, Integer.MAX_VALUE}));
    }

    @Test
    public void testIncorrectArrayType() {
        assertThrows(InvalidNBTTypeException.class, () -> new NBTArray<>(new String[0]));
    }
}