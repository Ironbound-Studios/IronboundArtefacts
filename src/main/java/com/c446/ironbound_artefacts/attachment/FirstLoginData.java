package com.c446.ironbound_artefacts.attachment;

import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class FirstLoginData implements INBTSerializable<CompoundTag> {
    public boolean hasLoggedIn;

    public FirstLoginData set(boolean value) {
        this.hasLoggedIn = value;
        return this;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        var tag = new CompoundTag();
        tag.putBoolean("is_true", this.hasLoggedIn);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
        this.hasLoggedIn = nbt.getBoolean("is_true");
    }
}
