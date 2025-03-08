package com.c446.ironbound_artefacts.events;


import com.c446.ironbound_artefacts.entities.archmage.ArchmageModel;
import com.c446.ironbound_artefacts.entities.archmage.ArchmageRenderer;
import com.c446.ironbound_artefacts.entities.comet.AstralCometModel;
import com.c446.ironbound_artefacts.entities.comet.AstralCometRenderer;
import com.c446.ironbound_artefacts.entities.simulacrum.SimulacrumEntity;
import com.c446.ironbound_artefacts.entities.simulacrum.SimulacrumRenderer;
import com.c446.ironbound_artefacts.registries.IBEntitiesReg;
import com.c446.ironbound_artefacts.registries.ItemRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void renderRegisters(EntityRenderersEvent.RegisterRenderers event){
        ItemRegistry.ITEMS.getEntries().stream().filter(item -> item.get() instanceof SpellBook).forEach((item) -> CuriosRendererRegistry.register(item.get(), SpellBookCurioRenderer::new));
        event.registerEntityRenderer(IBEntitiesReg.SIMULACRUM.get(), SimulacrumRenderer::new);
        event.registerEntityRenderer(IBEntitiesReg.ARCHMAGE.get(), (EntityRendererProvider.Context renderManager) -> new ArchmageRenderer(renderManager, new ArchmageModel()));
        event.registerEntityRenderer(IBEntitiesReg.FORCE_WALL.get(), NoopRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(AstralCometModel.LAYER_LOCATION, AstralCometModel::createBodyLayer);


    }
}
