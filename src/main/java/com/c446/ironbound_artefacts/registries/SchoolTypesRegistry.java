package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.datagen.Tags;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.registries.DeferredRegister;
import shadows.apotheosis.adventure.affix.effect.DamageReductionAffix;

public class SchoolTypesRegistry {
    public static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, IronboundArtefact.MODID);

    public static final ResourceLocation IMMORTAL_ART_LOC = IronboundArtefact.prefix("immortal_art");

    public final static Holder<SchoolType> IMMORTAL_ART = SCHOOLS.register("immortal_art", () ->
            new SchoolType(
                    IMMORTAL_ART_LOC,
                    Tags.ItemTags.IMMORTAL_ART_FOCUS,
                    Component.translatable("school.irons_spellbooks.immortal_art").withColor(EffectsRegistry.rgbToInt(255, 255, 255)),
                    AttributeRegistry.IMMORTAL_ART_POWER,
                    AttributeRegistry.IMMORTAL_ART_RESIST,
                    SoundRegistry.DIVINE_SMITE_CAST,
                    DamageSourcesReg.IMMORTAL_ART,
                    true,
                    false
            )
    );
}
