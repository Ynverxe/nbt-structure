import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.ynverxe.nbt_structure.nbt.NBT;
import com.github.ynverxe.nbt_structure.nbt.NBTCompound;
import com.github.ynverxe.nbt_structure.nbt.TagType;
import com.github.ynverxe.nbt_structure.nbt.tree.TreeNBTContainerHandler;
import org.junit.jupiter.api.Test;

public class CompoundTest {

  @Test
  public void testCompoundTag() {
    NBTCompound compoundTag = new NBTCompound();
    compoundTag.write("name", new NBT<>("Ynverxe"));
    assertEquals("Ynverxe", compoundTag.readValue("name", TagType.STRING));
  }

  @Test
  public void testCompoundTagWithPath() {
    TreeNBTContainerHandler handler = new NBTCompound().treeHandler();

    String nameKey = "compound.compound.tag";

    handler.write(nameKey, new NBT<>("Ynverxe"));
    assertEquals("Ynverxe", handler.readValue(nameKey, TagType.STRING));
  }
}
