package com.github.ynverxe.nbt_structure.nbt.tree;

import com.github.ynverxe.nbt_structure.nbt.NBTContainer;
import org.jetbrains.annotations.NotNull;

public interface TreeNBTContainer extends NBTContainer {

    @NotNull TreeNBTContainer createNewBranch();

    default @NotNull TreeNBTContainerHandler treeHandler() {
        return new TreeNBTContainerHandler(this);
    }
}