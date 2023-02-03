import com.github.ynverxe.nbt_structure.nbt.*;
import com.github.ynverxe.nbt_structure.nbt.NBTCompound;
import com.github.ynverxe.nbt_structure.nbt.NBT;
import com.github.ynverxe.nbt_structure.nbt.tree.TreeNBTContainerHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompoundTest {

    @Test
    public void testCompoundTag() {
        NBTCompound compoundTag = new NBTCompound();
        compoundTag.write("name", new NBT<>("Ynverxe"));
        assertEquals("Ynverxe", compoundTag.readValue("name"));
    }

    @Test
    public void testCompoundTagWithPath() {
        TreeNBTContainerHandler handler = new NBTCompound().treeHandler();

        String nameKey = "compound.compound.tag";

        handler.write(nameKey, new NBT<>("Ynverxe"));
        TagScheme<String, NBT<String>> tagScheme = TagScheme.primitive(nameKey, TagType.STRING);
        assertEquals("Ynverxe", handler.readValue(tagScheme.fullKey()));
    }
}