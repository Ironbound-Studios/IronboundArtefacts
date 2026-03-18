package com.c446.ironbound_artefacts.common.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class StackAttachment implements INBTSerializable<CompoundTag> {
    int afterShockBuildup = 0;

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        var t = new CompoundTag();
        t.putInt("afterShockBuildup", afterShockBuildup);

        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.afterShockBuildup = nbt.getInt("afterShockBuildup");
    }
}
