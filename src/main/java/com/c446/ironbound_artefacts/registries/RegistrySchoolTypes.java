package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.common.datagen.Tags;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegistrySchoolTypes {
    public static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, IBA.MODID);

    public static final ResourceLocation IMMORTAL_ART_LOC = IBA.p("immortal_art");

    public final static Holder<SchoolType> IMMORTAL_ART = SCHOOLS.register("immortal_art", () ->
            new SchoolType(
                    IMMORTAL_ART_LOC,
                    Tags.ItemTags.IMMORTAL_ART_FOCUS,
                    Component.translatable("school.irons_spellbooks.immortal_art").withColor(RegistryEffects.rgbToInt(255, 255, 255)),
                    RegistryAttributes.IMMORTAL_ART_POWER,
                    RegistryAttributes.IMMORTAL_ART_RESIST,
                    SoundRegistry.DIVINE_SMITE_CAST,
                    RegistryDamageSources.IMMORTAL_ART,
                    true,
                    false
            )
    );
}
