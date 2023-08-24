import com.github.ynverxe.nbt_structure.nbt.NBTCompound;
import java.util.Objects;

public class SomeProperties {

  private final String itemType;
  private final int amount;
  private final short damage;

  public SomeProperties(String itemType, int amount, short damage) {
    this.itemType = itemType;
    this.amount = amount;
    this.damage = damage;
  }

  public SomeProperties(NBTCompound compound) {
    this(
        compound.readValue("itemType"),
        compound.readValue("amount"),
        compound.readNumber("damage").shortValue());
  }

  @Override
  public String toString() {
    return "SomeProperties{"
        + "itemType='"
        + itemType
        + '\''
        + ", amount="
        + amount
        + ", damage="
        + damage
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SomeProperties that = (SomeProperties) o;
    return amount == that.amount && damage == that.damage && itemType.equals(that.itemType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemType, amount, damage);
  }
}
