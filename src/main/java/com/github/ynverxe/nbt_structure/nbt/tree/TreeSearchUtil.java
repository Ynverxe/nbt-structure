package com.github.ynverxe.nbt_structure.nbt.tree;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public final class TreeSearchUtil {

  private TreeSearchUtil() {
    throw new UnsupportedOperationException("cannot instantiate");
  }

  public static <N> Object pathSearch(
      @NotNull Class<N> nodeClass,
      @NotNull String[] path,
      @NotNull N root,
      @NotNull BiFunction<N, String, Object> nodeSearch,
      @NotNull BiConsumer<N, Map.Entry<String, Object>> nodeMutate,
      @NotNull BiFunction<N, Map.Entry<String, Object>, Object> finalizationHandler,
      @NotNull Supplier<N> nodeSupplier) {
    List<String> paths = Arrays.asList(path);

    Iterator<String> iterator = paths.iterator();
    N current = root;

    while (iterator.hasNext()) {
      String next = iterator.next();
      Object found = nodeSearch.apply(current, next);

      if (!iterator.hasNext()) {
        return finalizationHandler.apply(current, new AbstractMap.SimpleEntry<>(next, found));
      }

      if (!nodeClass.isInstance(found)) {
        N newNode = nodeSupplier.get();

        if (newNode == null) return null;
        nodeMutate.accept(current, new AbstractMap.SimpleEntry<>(next, newNode));
        current = newNode;
      } else {
        current = (N) found;
      }
    }

    return null;
  }

  public static <N> Object pathSearch(
      @NotNull Class<N> nodeClass,
      @NotNull String path,
      @NotNull N root,
      @NotNull BiFunction<N, String, Object> nodeSearch,
      @NotNull BiConsumer<N, Map.Entry<String, Object>> nodeMutate,
      @NotNull BiFunction<N, Map.Entry<String, Object>, Object> finalizationHandler,
      @NotNull Supplier<N> nodeSupplier) {
    String[] paths = path.split("\\.");
    return pathSearch(
        nodeClass, paths, root, nodeSearch, nodeMutate, finalizationHandler, nodeSupplier);
  }
}
