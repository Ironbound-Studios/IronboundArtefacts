package com.c446.ironbound_artefacts;


import com.c446.ironbound_artefacts.client.tooltip.FlamingClientTooltipComponent;
import com.c446.ironbound_artefacts.client.entity_renderers.SwordStormRenderer;
import com.c446.ironbound_artefacts.client.models.ArchmageModel;
import com.c446.ironbound_artefacts.client.entity_renderers.ArchmageRenderer;
import com.c446.ironbound_artefacts.client.models.AstralCometModel;
import com.c446.ironbound_artefacts.client.entity_renderers.LastPrismRenderer;
import com.c446.ironbound_artefacts.client.entity_renderers.SimulacrumRenderer;
import com.c446.ironbound_artefacts.client.tooltip.LightningClientTooltipComponent;
import com.c446.ironbound_artefacts.client.tooltip.LightningClientTooltipComponent.LightningTooltipData;
import com.c446.ironbound_artefacts.registries.RegistryEntities;
import com.c446.ironbound_artefacts.registries.RegistryItems;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import java.io.IOException;

import static com.c446.ironbound_artefacts.IBA.MODID;
import static com.c446.ironbound_artefacts.client.tooltip.FlamingClientTooltipComponent.*;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void renderRegisters(EntityRenderersEvent.RegisterRenderers event) {
        RegistryItems.ITEMS.getEntries().stream().filter(item -> item.get() instanceof SpellBook).forEach((item) -> CuriosRendererRegistry.register(item.get(), SpellBookCurioRenderer::new));
        event.registerEntityRenderer(RegistryEntities.SIMULACRUM.get(), SimulacrumRenderer::new);
        event.registerEntityRenderer(RegistryEntities.ARCHMAGE.get(), (EntityRendererProvider.Context renderManager) -> new ArchmageRenderer(renderManager, new ArchmageModel()));
        event.registerEntityRenderer(RegistryEntities.LAST_PRISM.get(), LastPrismRenderer::new);
        event.registerEntityRenderer(RegistryEntities.SWORD_STORM.get(), SwordStormRenderer::new);
    }

    // Register via RegisterShadersEvent
    public static ShaderInstance entityImpactShader;

    public static void onRegisterShaders(RegisterShadersEvent event) {
        try {
            event.registerShader(
                    new ShaderInstance(event.getResourceProvider(),
                            IBA.p("entity_impact"),
                            DefaultVertexFormat.NEW_ENTITY),
                    shader -> entityImpactShader = shader
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AstralCometModel.LAYER_LOCATION, AstralCometModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(LightningTooltipData.class, LightningClientTooltipComponent::new);
        event.register(FlamingTooltipData.class, FlamingClientTooltipComponent::new);
    }
}
