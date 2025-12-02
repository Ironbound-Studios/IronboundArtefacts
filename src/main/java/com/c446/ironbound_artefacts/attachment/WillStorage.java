package com.c446.ironbound_artefacts.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class WillStorage implements INBTSerializable<CompoundTag> {
    private float currentWill = 0;
    private long lastWill = 0;

    public float getCurrentWill() {
        return currentWill;
    }

    public long getLastWill() {
        return lastWill;
    }

    public void setCurrentWill(float currentWill) {
        this.currentWill = currentWill;
    }

    public void setLastWill(long lastWill) {
        this.lastWill = lastWill;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        var ctag = new CompoundTag();

        ctag.putFloat("curr", this.currentWill);
        ctag.putLong("last", this.lastWill);
        return ctag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.lastWill=nbt.getLong("last");
        this.currentWill = nbt.getFloat("curr");
    }
}
