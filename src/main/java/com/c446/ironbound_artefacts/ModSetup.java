package com.c446.ironbound_artefacts;

import com.c446.ironbound_artefacts.registries.*;
import io.redspace.ironsspellbooks.registries.CreativeTabRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class ModSetup {
    public ModSetup(IEventBus modEventBus, ModContainer modContainer) {
        ModSetup.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modEventBus.addListener(this::setup);
    }

    public static void register(IEventBus eventBus) {
        ItemRegistry.ITEMS.register(eventBus);
        AttributeRegistry.ATTRIBUTES.register(eventBus);
        EffectsRegistry.EFFECTS.register(eventBus);
        EffectsRegistry.POTIONS.register(eventBus);
        CustomSpellRegistry.SPELLS.register(eventBus);
        IBEntitiesReg.ENTITIES.register(eventBus);
        AttachmentRegistry.ATTACHMENT_TYPE_DEFERRED_REGISTER.register(eventBus);
        ComponentRegistry.register(eventBus);
        //ArmorMaterials.MATERIALS.register(eventBus);
        ModCreativeTabReg.CREATIVE_MOD_TABS.register(eventBus);
    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public void setup(final FMLCommonSetupEvent event) {

        // DO OTHER MODS CONFIG
    }

    protected static class ModCreativeTabReg {
        public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> THINGS = CREATIVE_MOD_TABS.register("ironbound_artefacts", () ->
                        CreativeModeTab.builder()
                                .withTabsAfter(CreativeTabRegistry.MATERIALS_TAB.getKey())
                                .title(Component.translatable("tab.ironbounds_artefacts.curios"))
                                .icon(() -> new ItemStack(ItemRegistry.THREE_WISHES))
                                .displayItems((enabledFeatures, entries) -> {
//                            //entries.accept(ItemRegistry.DEATH_AMULET.get());
//                            //entries.accept(ItemRegistry.DEVILS_FINGER.get());
//                            //entries.accept(ItemRegistry.MAGICIANS_MONOCLE.get());
//                            //entries.accept(ItemRegistry.JUDGEMENT_SCALE.get());
//                            //entries.accept(ItemRegistry.LICH_HAND.get());
//                            //entries.accept(ItemRegistry.LICH_CROWN.get());
//                            entries.accept(ItemRegistry.HERMIT_EYE.get());
//                            entries.accept(ItemRegistry.LICH_CROWN.get());
//                            //entries.accept(ItemRegistry.STOPWATCH.get());
//
//                            entries.accept(ItemRegistry.MAGIC_DEFENSE_RING.get());
//                            entries.accept(ItemRegistry.PROTECTION_RING.get());
//                            entries.accept(ItemRegistry.GREATER_SPELL_SLOT_UPGRADE.get());
//                            entries.accept(ItemRegistry.AMULET_OF_HOLDING.get());
//                            //entries.accept(ItemRegistry.DECK_OF_ALL_THINGS.get());
//                            //entries.accept(ItemRegistry.STAFF_OF_POWER.get());
//                            //entries.accept(ItemRegistry.ARCHMAGE_SPELLBOOK.get());
//
//                            entries.accept(ItemRegistry.ARCANE_PROTECTION_CLOAK.get());
//                            entries.accept(ItemRegistry.ELVEN_CHAINS.get());
//                            entries.accept(ItemRegistry.STAFF_OF_POWER.get());
//                            entries.accept(ItemRegistry.STAFF_OF_MAGI.get());
//                            entries.accept(ItemRegistry.WEAVE_HELMET.get());
//                            entries.accept(ItemRegistry.WEAVE_CHEST_PLATE.get());
//                            entries.accept(ItemRegistry.WEAVE_LEGGINGS.get());
//                            entries.accept(ItemRegistry.WEAVE_BOOTS.get());
                                    ItemRegistry.ITEMS.getEntries().forEach(i -> {
                                        if (!i.is(Tags.Items.HIDDEN_FROM_RECIPE_VIEWERS)) {
                                            entries.accept(i.get());
                                        }
                                    });
                                })
                                .build()
        );
    }
}

