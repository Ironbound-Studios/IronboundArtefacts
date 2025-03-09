package com.c446.ironbound_artefacts.entities.comet;


import com.c446.ironbound_artefacts.IronboundArtefact;
import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.entity.spells.fireball.FireballRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.NotNull;

public class AstralCometRenderer extends FireballRenderer {
    public AstralCometRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, 1f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Projectile pEntity) {
        return ResourceLocation.fromNamespaceAndPath(IronboundArtefact.MODID, "textures/entities/comet.png");
    }

    @Override
    public void render(Projectile pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity instanceof AstralCometEntity comet) {
            //pMatrixStack.scale(comet.getSIZE_FACTOR(),comet.getSIZE_FACTOR(),comet.getSIZE_FACTOR());
            super.render(comet, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        }
    }
}
