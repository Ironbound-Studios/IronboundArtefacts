package com.c446.ironbound_artefacts.datagen;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.ItemRegistry;
import io.redspace.ironsspellbooks.spells.ender.StarfallSpell;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class ModModelsProvider extends ItemModelProvider {
    public ModModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, IronboundArtefact.MODID, existingFileHelper);
    }

    public ItemModelBuilder handHeld(Item item) {
        return handHeld(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    public ItemModelBuilder handHeld(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
    }

    @Override
    protected void registerModels() {
        basicItem(ItemRegistry.PROTECTION_RING.get());
        basicItem(ItemRegistry.MAGIC_DEFENSE_RING.get());
        basicItem(ItemRegistry.ELVEN_CHAINS.get());
        basicItem(ItemRegistry.ARCANE_PROTECTION_CLOAK.get());
        basicItem(ItemRegistry.THREE_WISHES.get());
        basicItem(ItemRegistry.AMULET_OF_HOLDING.get());
        //basicItem(ItemRegistry.AMULET_OF_MANA.get());
        basicItem(ItemRegistry.WEAVE_HELMET.get());
        basicItem(ItemRegistry.WEAVE_CHEST_PLATE.get());
        basicItem(ItemRegistry.WEAVE_LEGGINGS.get());
        basicItem(ItemRegistry.WEAVE_BOOTS.get());
        basicItem(ItemRegistry.PHYLACTERY.get());
        basicItem(ItemRegistry.DREAMS.get());
        basicItem(ItemRegistry.FC.get());
        basicItem(ItemRegistry.LIGHTNING_GLOVES.get());
        basicItem(ItemRegistry.ARCHMAGE_BOOTS.get());
        basicItem(ItemRegistry.ARCHMAGE_CHEST.get());
        basicItem(ItemRegistry.ARCHMAGE_HEAD.get());
        basicItem(ItemRegistry.ARCHMAGE_LEG.get());
        handHeld(ItemRegistry.WIZARDING_WAND.get());
        handHeld(ItemRegistry.TUNING_FORK_DEFERRED_HOLDER.get());

    }
}
