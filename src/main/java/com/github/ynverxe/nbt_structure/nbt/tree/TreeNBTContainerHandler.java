package com.github.ynverxe.nbt_structure.nbt.tree;

import com.github.ynverxe.nbt_structure.nbt.NBT;
import com.github.ynverxe.nbt_structure.nbt.NBTContainer;
import com.github.ynverxe.nbt_structure.nbt.NBTReadable;
import com.github.ynverxe.nbt_structure.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;

import static com.github.ynverxe.nbt_structure.nbt.tree.TreeSearchUtil.*;

@SuppressWarnings("unchecked")
public class TreeNBTContainerHandler implements NBTContainer {

    private static final BiFunction<TreeNBTContainer, String, Object> TREE_VALUE_SEARCHER = NBTReadable::read;

    private static final BiConsumer<TreeNBTContainer, Map.Entry<String, Object>> PHANTOM_TREE_MUTATOR = (treeNBTContainer, entry) -> {};
    private static final BiConsumer<TreeNBTContainer, Map.Entry<String, Object>> TREE_MUTATOR = (treeNBTContainer, entry) -> treeNBTContainer.write(entry.getKey(), entry.getValue());

    private static final Supplier<TreeNBTContainer> PHANTOM_CREATOR = () -> null;
    private static final Function<TreeNBTContainer, Supplier<TreeNBTContainer>> BRANCH_CREATOR = treeNBTContainer -> treeNBTContainer::createNewBranch;

    private final TreeNBTContainer bridge;

    public TreeNBTContainerHandler(TreeNBTContainer bridge) {
        this.bridge = Objects.requireNonNull(bridge, "delegate");
    }

    @Override
    public <N extends NBT<?>> N read(@NotNull String key) {
        return (N) pathSearch(
                TreeNBTContainer.class,
                key,
                bridge,
                TREE_VALUE_SEARCHER,
                PHANTOM_TREE_MUTATOR,
                (treeNBTContainer, entry) -> treeNBTContainer.read(entry.getKey()),
                PHANTOM_CREATOR
        );
    }

    @Override
    public @Nullable NBT<?> write(@NotNull String key, @Nullable Object value) {
        return (NBT<?>) pathSearch(
                TreeNBTContainer.class,
                key,
                bridge,
                TREE_VALUE_SEARCHER,
                TREE_MUTATOR,
                (treeNBTContainer, entry) -> treeNBTContainer.write(entry.getKey(), value),
                BRANCH_CREATOR.apply(bridge)
        );
    }

    @Override
    public @NotNull Set<String> keys() {
        return bridge.keys();
    }

    @NotNull
    @Override
    public Iterator<Tag<?>> iterator() {
        return bridge.iterator();
    }
}