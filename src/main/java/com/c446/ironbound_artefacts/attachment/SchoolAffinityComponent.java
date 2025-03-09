package com.c446.ironbound_artefacts.attachment;

import com.c446.ironbound_artefacts.registries.AttachmentRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class SchoolAffinityComponent implements INBTSerializable<CompoundTag> {
    public String school = "";

    public static SchoolType getSchool(Player player) {
        if (player.hasData(AttachmentRegistry.MARKOHESHKIR_ATTACHMENT)) {
            return SchoolRegistry.getSchool(ResourceLocation.parse(player.getData(AttachmentRegistry.MARKOHESHKIR_ATTACHMENT).school));
        }
        return null;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        var tag = new CompoundTag();
        tag.putString("school", school);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.school = nbt.getString("school");
    }
}
