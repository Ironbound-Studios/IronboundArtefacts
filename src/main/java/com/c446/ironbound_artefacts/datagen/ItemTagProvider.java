package com.c446.ironbound_artefacts.datagen;

import com.c446.ironbound_artefacts.IronboundArtefact;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.c446.ironbound_artefacts.registries.ItemRegistry.*;
import static net.neoforged.neoforge.common.Tags.Items.*;

public class ItemTagProvider extends IntrinsicHolderTagsProvider<Item> {
    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, Registries.ITEM, future, item -> item.builtInRegistryHolder().key(), IronboundArtefact.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(Tags.ItemTags.WISH_DUPLICABLE)
                .add(ItemRegistry.ARCANE_ESSENCE.get())
                .add(ItemRegistry.CINDER_ESSENCE.get())
                .add(Items.GOLD_INGOT)
                .add(Items.IRON_INGOT)
                .add(Items.REDSTONE)
                .add(Items.AMETHYST_CLUSTER)
        ;
        tag(HIDDEN_FROM_RECIPE_VIEWERS)
                .add(com.c446.ironbound_artefacts.registries.ItemRegistry.DREAMS.get());

        tag(ARMORS)
                .add(ARCHMAGE_CHEST.get())
                .add(ARCHMAGE_BOOTS.get())
                .add(ARCHMAGE_HEAD.get())
                .add(ARCHMAGE_LEG.get())
                .add(WEAVE_HELMET.get())
                .add(WEAVE_CHEST_PLATE.get())
                .add(WEAVE_LEGGINGS.get())
                .add(WEAVE_BOOTS.get());
        tag(ItemTags.ARMOR_ENCHANTABLE)
                .add(ARCHMAGE_CHEST.get())
                .add(ARCHMAGE_BOOTS.get())
                .add(ARCHMAGE_HEAD.get())
                .add(ARCHMAGE_LEG.get())
                .add(WEAVE_HELMET.get())
                .add(WEAVE_CHEST_PLATE.get())
                .add(WEAVE_LEGGINGS.get())
                .add(WEAVE_BOOTS.get());
        tag(ItemTags.HEAD_ARMOR_ENCHANTABLE)
                .add(ARCHMAGE_HEAD.get())
                .add(WEAVE_HELMET.get());
        tag(ItemTags.FOOT_ARMOR_ENCHANTABLE)
                .add(ARCHMAGE_BOOTS.get())
                .add(WEAVE_BOOTS.get());
        tag(ItemTags.CHEST_ARMOR_ENCHANTABLE)
                .add(ARCHMAGE_CHEST.get())
                .add(WEAVE_CHEST_PLATE.get());
        tag(ItemTags.LEG_ARMOR_ENCHANTABLE)
                .add(ARCHMAGE_LEG.get()) // This might be a mistake; should it be ARCHMAGE_LEG?
                .add(WEAVE_LEGGINGS.get());
        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(ARCHMAGE_CHEST.get())
                .add(ARCHMAGE_BOOTS.get())
                .add(ARCHMAGE_HEAD.get())
                .add(ARCHMAGE_LEG.get())
                .add(WEAVE_HELMET.get())
                .add(WEAVE_CHEST_PLATE.get())
                .add(WEAVE_LEGGINGS.get())
                .add(WEAVE_BOOTS.get());
        tag(ItemTags.VANISHING_ENCHANTABLE)
                .add(ARCHMAGE_CHEST.get())
                .add(ARCHMAGE_BOOTS.get())
                .add(ARCHMAGE_HEAD.get())
                .add(ARCHMAGE_LEG.get())
                .add(WEAVE_HELMET.get())
                .add(WEAVE_CHEST_PLATE.get())
                .add(WEAVE_LEGGINGS.get())
                .add(WEAVE_BOOTS.get());
        tag(ARMORS)
                .add(ARCHMAGE_CHEST.get())
                .add(ARCHMAGE_BOOTS.get())
                .add(ARCHMAGE_HEAD.get())
                .add(ARCHMAGE_LEG.get())
                .add(WEAVE_HELMET.get())
                .add(WEAVE_CHEST_PLATE.get())
                .add(WEAVE_LEGGINGS.get())
                .add(WEAVE_BOOTS.get());
        tag(ENCHANTABLES)
                .add(ARCHMAGE_CHEST.get())
                .add(ARCHMAGE_BOOTS.get())
                .add(ARCHMAGE_HEAD.get())
                .add(ARCHMAGE_LEG.get())
                .add(WEAVE_HELMET.get())
                .add(WEAVE_CHEST_PLATE.get())
                .add(WEAVE_LEGGINGS.get())
                .add(WEAVE_BOOTS.get())
                .add(STAFF_OF_POWER.get())
                .add(STAFF_OF_SUN.get());
        tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .add(STAFF_OF_POWER.get())
                .add(STAFF_OF_SUN.get());
        tag(ItemTags.SWORD_ENCHANTABLE)
                .add(STAFF_OF_POWER.get())
                .add(STAFF_OF_SUN.get());
        tag(Tags.ItemTags.STAFF_COPY)
                .add(STAFF_OF_POWER.get())
                .add(STAFF_OF_SUN.get());
        tag(MELEE_WEAPON_TOOLS)
                .add(STAFF_OF_POWER.get())
                .add(STAFF_OF_SUN.get());
    }
}
