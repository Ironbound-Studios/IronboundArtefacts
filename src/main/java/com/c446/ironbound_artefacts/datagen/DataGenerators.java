package com.c446.ironbound_artefacts.datagen;


import com.c446.ironbound_artefacts.IronboundArtefact;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.core.registries.Registries.ENTITY_TYPE;

@EventBusSubscriber(modid = IronboundArtefact.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput outPut = gen.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        gen.addProvider(event.includeServer(), new ModSpellTagsProvider(outPut, SpellRegistry.SPELL_REGISTRY_KEY, provider, existingFileHelper));
        gen.addProvider(event.includeClient(), new ModEntityTags(outPut, ENTITY_TYPE, provider, existingFileHelper));
        var blockTags = new ModBlockTagProvider(outPut, provider, existingFileHelper);
        gen.addProvider(event.includeClient(), new ModModelsProvider(outPut, existingFileHelper));
        gen.addProvider(event.includeServer(), new ItemTagProvider(outPut, provider, existingFileHelper));

//        gen.addProvider(
//                event.includeServer(),
//                new WishDuplicableTagsProvider(out, provider, event., helper, "", helper)
//        );
    }

}
