package com.c446.ironbound_artefacts.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class IBSpellCasterData implements INBTSerializable<CompoundTag> {
    private int wish_recoil = 0;
    private int time_stop_recoil = 0;

    public int getTimeStopRecoil() {
        return time_stop_recoil;
    }

    public void setTimeStopRecoil(int time_stop_recoil) {
        this.time_stop_recoil = time_stop_recoil;
    }

    public int getWishRecoil() {
        return wish_recoil;
    }

    public void setWishRecoil(int wish_recoil) {
        this.wish_recoil = wish_recoil;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        var tag = new CompoundTag();
        tag.putInt("wish_cd", this.wish_recoil);
        tag.putInt("time_stop_cd", this.time_stop_recoil);

        return tag;

    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.wish_recoil = nbt.getInt("wish_cd");
        this.time_stop_recoil = nbt.getInt("time_stop_cd");
    }

}
