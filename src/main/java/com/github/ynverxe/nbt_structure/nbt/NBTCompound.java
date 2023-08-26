package com.github.ynverxe.nbt_structure.nbt;

import com.github.ynverxe.nbt_structure.nbt.tree.TreeNBTContainer;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"rawtypes", "unchecked", "UnusedReturnValue", "unused"})
public class NBTCompound extends NBT<Map<String, NBT>> implements TreeNBTContainer, Cloneable {

  public NBTCompound() {
    this(new LinkedHashMap<>());
  }

  public NBTCompound(Map<String, NBT> value) {
    super(value);
  }

  @Override
  public @NotNull Map<String, NBT> set(Map<String, NBT> value) {
    value.values().forEach(this::checkNotEndType);
    return super.set(value);
  }

  @Override
  public NBTCompound clone() {
    return (NBTCompound) super.clone();
  }

  @Override
  public boolean primitive() {
    return false;
  }

  @Override
  public void toSNBT(Appendable appendable) throws IOException {
    appendable.append("{");
    Iterator<Tag<?>> iterator = iterator();

    while (iterator.hasNext()) {
      Tag<?> tag = iterator.next();

      appendable.append("\"").append(tag.name()).append("\"")
          .append(":")
          .append(tag.value().toSNBT());

      if (iterator.hasNext()) {
        appendable.append(",");
      }
    }

    appendable.append("}");
  }

  @Override
  public @NotNull Map<String, Object> normalize() {
    return value.entrySet().stream()
        .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().normalize()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public @Nullable NBT<?> write(@NotNull String key, @Nullable Object value) {
    if (value == null) return erase(key);

    NBT nbt = value instanceof NBT ? (NBT) value : byPossibleRawRepresenter(value);

    checkNotEndType(nbt);
    return this.value.put(key, nbt);
  }

  @Override
  public <N extends NBT<?>> N read(@NotNull String key) {
    return (N) value.get(key);
  }

  @Override
  public @Nullable NBT<?> erase(@NotNull String key) {
    return value.remove(key);
  }

  @Override
  public @NotNull Set<String> keys() {
    return value.keySet();
  }

  @Override
  public @NotNull NBTCompound toCompound() {
    return clone();
  }

  @Override
  public @NotNull TreeNBTContainer createNewBranch() {
    return new NBTCompound();
  }

  @Override
  public @NotNull Iterator<Tag<?>> iterator() {
    List<Tag<?>> list = new ArrayList<>();

    for (String key : keys()) {
      list.add(new Tag<>(key, read(key)));
    }

    return list.iterator();
  }

  public int size() {
    return value.size();
  }

  public boolean isEmpty() {
    return value.isEmpty();
  }

  public void clear() {
    value.clear();
  }

  private void checkNotEndType(NBT nbt) {
    if (nbt.type() == TagType.END) throw new IllegalArgumentException("Cannot store END nbt tags");
  }
}
