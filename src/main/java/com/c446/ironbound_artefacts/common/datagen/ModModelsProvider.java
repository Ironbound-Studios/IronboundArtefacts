package com.c446.ironbound_artefacts.common.datagen;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.registries.RegistryItems;
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
        super(output, IBA.MODID, existingFileHelper);
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

        basicItem(RegistryItems.PROTECTION_RING.get());
        basicItem(RegistryItems.MAGIC_DEFENSE_RING.get());
        basicItem(RegistryItems.ELVEN_CHAINS.get());
        basicItem(RegistryItems.ARCANE_PROTECTION_CLOAK.get());
        basicItem(RegistryItems.THREE_WISHES.get());
        basicItem(RegistryItems.AMULET_OF_HOLDING.get());
        //basicItem(ItemRegistry.AMULET_OF_MANA.get());
        basicItem(RegistryItems.WEAVE_HELMET.get());
        basicItem(RegistryItems.WEAVE_CHEST_PLATE.get());
        basicItem(RegistryItems.WEAVE_LEGGINGS.get());
        basicItem(RegistryItems.WEAVE_BOOTS.get());
        basicItem(RegistryItems.PHYLACTERY.get());
        basicItem(RegistryItems.DREAMS.get());
        basicItem(RegistryItems.FC.get());
        basicItem(RegistryItems.LIGHTNING_GLOVES.get());
        basicItem(RegistryItems.ARCHMAGE_BOOTS.get());
        basicItem(RegistryItems.ARCHMAGE_CHEST.get());
        basicItem(RegistryItems.ARCHMAGE_HEAD.get());
        basicItem(RegistryItems.ARCHMAGE_LEG.get());
        handHeld(RegistryItems.WIZARDING_WAND.get());
        handHeld(RegistryItems.TUNING_FORK_DEFERRED_HOLDER.get());

    }
}
