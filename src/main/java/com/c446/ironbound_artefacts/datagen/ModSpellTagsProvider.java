package com.c446.ironbound_artefacts.datagen;

import com.c446.ironbound_artefacts.IronboundArtefact;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static io.redspace.ironsspellbooks.api.registry.SpellRegistry.*;

public class ModSpellTagsProvider extends TagsProvider<AbstractSpell> {


    protected ModSpellTagsProvider(PackOutput pOutput, ResourceKey<? extends Registry<AbstractSpell>> pRegistryKey, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {

        super(pOutput, pRegistryKey, pLookupProvider, IronboundArtefact.MODID, existingFileHelper);
    }

    protected ModSpellTagsProvider(PackOutput pOutput, ResourceKey<? extends Registry<AbstractSpell>> pRegistryKey, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<AbstractSpell>> pParentProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pRegistryKey, pLookupProvider, pParentProvider, IronboundArtefact.MODID, existingFileHelper);
    }

    public ResourceKey<AbstractSpell> k(AbstractSpell spell) {
        return ResourceKey.create(SpellRegistry.SPELL_REGISTRY_KEY, ResourceLocation.parse(spell.getSpellId()));
    }

    public ResourceKey<AbstractSpell> k(Supplier<AbstractSpell> spell) {
        return ResourceKey.create(SPELL_REGISTRY_KEY, ResourceLocation.parse(spell.get().getSpellId()));
    }

    public ResourceKey<AbstractSpell> k(String spell) {
        return ResourceKey.create(SpellRegistry.SPELL_REGISTRY_KEY, ResourceLocation.parse(spell));
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(Tags.SpellTags.UTILITY_SPELL)
                .add(k(CHARGE_SPELL.get().getSpellId()))
                .add(k(HASTE_SPELL.get().getSpellId()))
                .add(k(HEARTSTOP_SPELL.get().getSpellId()))
                .add(k(HEAL_SPELL.get().getSpellId()))
                .add(k(GREATER_HEAL_SPELL.get().getSpellId()))
                .add(k(HEALING_CIRCLE_SPELL.get().getSpellId()))
                .add(k(FORTIFY_SPELL.get().getSpellId()))
                .add(k(BLESSING_OF_LIFE_SPELL.get().getSpellId()));

        tag(Tags.SpellTags.MOUVEMENT_SPELL)
                .add(k(TELEPORT_SPELL.get().getSpellId()))
                .add(k(BLOOD_STEP_SPELL.get().getSpellId()))
                .add(k(FROST_STEP_SPELL.get().getSpellId()))
                .add(k(ASCENSION_SPELL.get().getSpellId()))
                .add(k(BURNING_DASH_SPELL.get().getSpellId()));

        tag(Tags.SpellTags.DEFENSIVE_SPELL)
                .add(k(ABYSSAL_SHROUD_SPELL.get()))
                .add(k(OAKSKIN_SPELL.get()))
                .add(k(EVASION_SPELL.get()));

        tag(Tags.SpellTags.OFFENSIVE_SPELL)
//                .add(getKey(SpellRegistry.ACID_ORB_SPELL.get()))
                .add(k(ACUPUNCTURE_SPELL.get()))
                .add(k(BALL_LIGHTNING_SPELL.get()))
                .add(k(BLAZE_STORM_SPELL.get()))
                .add(k(BLACK_HOLE_SPELL.get().getSpellId()))
                .add(k(BLOOD_SLASH_SPELL.get().getSpellId()))
                .add(k(BLOOD_NEEDLES_SPELL.get().getSpellId()))
                .add(k(CHAIN_CREEPER_SPELL.get().getSpellId()))
                .add(k(CONE_OF_COLD_SPELL.get().getSpellId())).add(k(FIREBALL_SPELL.get().getSpellId()))
                .add(k(FIRE_BREATH_SPELL.get().getSpellId()))
                .add(k(FIRECRACKER_SPELL.get().getSpellId()))
                .add(k(FLAMING_BARRAGE_SPELL.get().getSpellId()))
                .add(k(FLAMING_STRIKE_SPELL.get().getSpellId()))
                .add(k(FROSTWAVE_SPELL.get().getSpellId()))
                .add(k(FIREBOLT_SPELL.get().getSpellId()))
                .add(k(GUST_SPELL.get().getSpellId()))
                .add(k(GUIDING_BOLT_SPELL.get().getSpellId()))
                .add(k(HEAT_SURGE_SPELL.get().getSpellId()))
                .add(k(BLIGHT_SPELL.get().getSpellId()))
                .add(k(LIGHTNING_BOLT_SPELL.get().getSpellId()))
                .add(k(LIGHTNING_LANCE_SPELL.get().getSpellId()))
                .add(k(LOB_CREEPER_SPELL.get().getSpellId()))
                .add(k(MAGIC_ARROW_SPELL.get().getSpellId()))
                .add(k(MAGIC_MISSILE_SPELL.get().getSpellId()))
                .add(k(MAGMA_BOMB_SPELL.get().getSpellId()))
                .add(k(POISON_ARROW_SPELL.get().getSpellId()))
                .add(k(POISON_BREATH_SPELL.get().getSpellId()))
                .add(k(POISON_SPLASH_SPELL.get().getSpellId()))
                .add(k(RAY_OF_SIPHONING_SPELL.get().getSpellId()))
                .add(k(RAY_OF_FROST_SPELL.get().getSpellId()))
                .add(k(RAISE_DEAD_SPELL.get().getSpellId()))
                .add(k(THUNDERSTORM_SPELL.get().getSpellId()))
                .add(k(TELEKINESIS_SPELL.get().getSpellId()))
                .add(k(WISP_SPELL.get().getSpellId()))
                .add(k(WALL_OF_FIRE_SPELL.get().getSpellId()))
                .add(k(WITHER_SKULL_SPELL.get().getSpellId()))
                .add(k(SUMMON_POLAR_BEAR_SPELL.get().getSpellId()))
                .add(k(SUMMON_VEX_SPELL.get().getSpellId()));


        tag(Tags.SpellTags.SUPPORT_SPELL)
                .add(k(HASTE_SPELL.get()))
                .add(k(BLESSING_OF_LIFE_SPELL.get()))
                .add(k(HEALING_CIRCLE_SPELL.get()));

        tag(Tags.SpellTags.ARCHMAGE_COMMON)
                .add(k(COUNTERSPELL_SPELL))
                .add(k(ABYSSAL_SHROUD_SPELL))
                .add(k(HEAL_SPELL))
                .add(k(CHARGE_SPELL))
                .add(k(HASTE_SPELL));

        tag(Tags.SpellTags.ARCHMAGE_ALLOWED_SPELL)
                // LIGHTNING
                .add(k(LIGHTNING_BOLT_SPELL))
                .add(k(LIGHTNING_LANCE_SPELL))
                .add(k(CHAIN_LIGHTNING_SPELL))
                .add(k(BALL_LIGHTNING_SPELL))
                .add(k(THUNDERSTORM_SPELL))
                // FIRE
                .add(k(FIREBALL_SPELL))
                .add(k(HEAT_SURGE_SPELL))
                .add(k(BURNING_DASH_SPELL))
                .add(k(FIRE_BREATH_SPELL))
                .add(k(FLAMING_BARRAGE_SPELL))
                // ICE
                .add(k(CONE_OF_COLD_SPELL))
                .add(k(ICE_SPIKES_SPELL))
                .add(k(ICE_BLOCK_SPELL))
                .add(k(SUMMON_POLAR_BEAR_SPELL));

        // BARRAGE
        tag(Tags.SpellTags.ARCHMAGE_BARRAGE_SPELL)
                .add(k(RAY_OF_FROST_SPELL))
                .add(k(MAGMA_BOMB_SPELL))
                .add(k(FIREBOLT_SPELL))
                .add(k(ICICLE_SPELL))
                .add(k(BALL_LIGHTNING_SPELL));
    }
}
