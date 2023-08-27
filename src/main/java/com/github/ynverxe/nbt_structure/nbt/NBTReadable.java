package com.github.ynverxe.nbt_structure.nbt;

import java.util.Optional;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NBTReadable {

  <N extends NBT<?>> N read(@NotNull String key);

  default <N extends NBT<?>> @Nullable Tag<N> readTag(
      @NotNull String key, @NotNull TagType<N> type) {
    N found = read(key, type);

    if (found == null) return null;

    return new Tag<>(key, found);
  }

  default <N extends NBT<?>> @Nullable N read(@NotNull String key, @NotNull TagType<N> type) {
    return type.cast(read(key));
  }

  @SuppressWarnings("unchecked")
  default <T> @Nullable T readValue(@NotNull String key) {
    return (T) read(key).value();
  }

  default <T> @Nullable T readValue(@NotNull String key, @NotNull TagType<NBT<T>> type) {
    NBT<?> found = read(key);

    if (found == null) return null;

    return found.getAs(type);
  }

  default @Nullable Number readNumber(@NotNull String key) {
    return readValue(key);
  }

  default <T, N extends NBT<?>> @Nullable T interpret(
      @NotNull String key, @Nullable TagType<N> type, @NotNull Function<N, T> interpreter) {
    N found = read(key);

    return found == null ? null : interpreter.apply(found);
  }

  default <N extends NBT<?>> @NotNull Optional<N> readOptional(
      @NotNull String key, @NotNull TagType<N> type) {
    return Optional.ofNullable(read(key, type));
  }

  default @NotNull Optional<NBT<?>> readOptional(@NotNull String key) {
    return Optional.ofNullable(readValue(key));
  }

  default <T> @NotNull Optional<T> readOptionalValue(
      @NotNull String key, @NotNull TagType<NBT<T>> type) {
    return Optional.ofNullable(readValue(key, type));
  }

  default @NotNull Optional<Object> readOptionalValue(
      @NotNull String key) {
    return Optional.ofNullable(readValue(key));
  }
}