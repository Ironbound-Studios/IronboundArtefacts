package com.c446.ironbound_artefacts.common.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class SpellPerkAttachment implements INBTSerializable<CompoundTag> {
    private final Set<String> activePerks = new HashSet<>();

    public boolean hasPerk(String perkId) {
        return activePerks.contains(perkId);
    }

    public void addPerk(String perkId) {
        activePerks.add(perkId);
    }

    public void removePerk(String perkId) {
        activePerks.remove(perkId);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (String perk : activePerks) {
            list.add(StringTag.valueOf(perk));
        }
        tag.put("unlocked_perks", list);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt) {
        activePerks.clear();
        ListTag list = nbt.getList("unlocked_perks", Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            activePerks.add(list.getString(i));
        }
    }
}