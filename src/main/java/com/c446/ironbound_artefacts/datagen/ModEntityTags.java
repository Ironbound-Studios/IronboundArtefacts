package com.c446.ironbound_artefacts.datagen;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.IBEntitiesReg;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ModEntityTags extends TagsProvider<EntityType<?>> {
    ModEntityTags(PackOutput pOutput, ResourceKey<? extends Registry<EntityType<?>>> pRegistryKey, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pRegistryKey, pLookupProvider, IronboundArtefact.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(com.c446.ironbound_artefacts.datagen.Tags.SpellTags.HYPNOTIC_PATTERN_IMMUNE).addTag(
                Tags.EntityTypes.BOSSES
        ).addOptional(IBEntitiesReg.ARCHMAGE.getId())
                .add(Objects.requireNonNull(EntityRegistry.PYROMANCER.getKey()))
                .add(Objects.requireNonNull(EntityRegistry.CULTIST.getKey()))
                .add(Objects.requireNonNull(EntityRegistry.CRYOMANCER.getKey()))
                .add(Objects.requireNonNull(EntityRegistry.NECROMANCER.getKey()))
                .add(Objects.requireNonNull(EntityRegistry.DEAD_KING_CORPSE.getKey()))
                .add(Objects.requireNonNull(EntityRegistry.APOTHECARIST.getKey()))
                .add(Objects.requireNonNull(EntityRegistry.ARCHEVOKER.getKey()))


                ;
    }
}
