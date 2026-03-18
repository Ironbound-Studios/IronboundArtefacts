package com.c446.ironbound_artefacts.client.entity_renderers;

import com.c446.ironbound_artefacts.common.entities.SwordStormEntity;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import static net.minecraft.client.renderer.RenderStateShard.*;

@OnlyIn(Dist.CLIENT)
public class SwordStormRenderer extends EntityRenderer<SwordStormEntity> {
    public static final int SLASH_COUNT = 16; // Increased for density
    public static final float STORM_RADIUS = 3.5F;
    public static final float SLASH_LENGTH = 4.5F;
    public static final float SLASH_WIDTH = 0.55F;

    public static final RenderType STORM_ENERGY = RenderType.create(
            "sword_storm_energy", DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS, 512, false, false,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_LIGHTNING_SHADER)
                    .setWriteMaskState(COLOR_WRITE)
                    .setTransparencyState(LIGHTNING_TRANSPARENCY)
                    .setOutputState(TRANSLUCENT_TARGET)
                    .setCullState(NO_CULL)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .createCompositeState(false)
    );

    public SwordStormRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(SwordStormEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();

        float time = (float) pEntity.tickCount + pPartialTicks;
        VertexConsumer buffer = pBuffer.getBuffer(STORM_ENERGY);

        // Seeded random: Change slash positions every 3 ticks for a "frenzy" effect
        long frameSeed = (long)(pEntity.tickCount / 3);
        java.util.Random rand = new java.util.Random(frameSeed);

        for (int i = 0; i < SLASH_COUNT; i++) {
            pPoseStack.pushPose();

            // 1. Position in sphere
            float ox = (rand.nextFloat() - 0.5f) * STORM_RADIUS;
            float oy = (rand.nextFloat() - 0.5f) * STORM_RADIUS;
            float oz = (rand.nextFloat() - 0.5f) * STORM_RADIUS;
            pPoseStack.translate(ox, oy, oz);

            // 2. Rotation
            pPoseStack.mulPose(Axis.YP.rotationDegrees(rand.nextFloat() * 360f));
            pPoseStack.mulPose(Axis.XP.rotationDegrees(rand.nextFloat() * 360f));
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(rand.nextFloat() * 360f));

            // 3. Flicker & Dynamic Width
            float alphaBase = 0.3f + (Mth.sin(time * 0.5f + i) * 0.15f);
            float pulseWidth = SLASH_WIDTH * (0.9f + 0.2f * Mth.sin(time * 0.8f + i));

            // 4. Render Layers
            // Outer Glowing Shell (Cyan)
            renderVolumetricBlade(pPoseStack.last().pose(), buffer, SLASH_LENGTH, pulseWidth, 0x00FDFF, alphaBase * 0.5f);

            // Inner Hot Core (White - thinner and brighter)
            renderVolumetricBlade(pPoseStack.last().pose(), buffer, SLASH_LENGTH + 0.2f, pulseWidth * 0.3f, 0xFFFFFF, alphaBase * 0.9f);

            pPoseStack.popPose();
        }

        pPoseStack.popPose();
    }

    /**
     * Renders a 3D "blade" by intersecting 3 planes at 60 degree intervals.
     */
    private void renderVolumetricBlade(Matrix4f matrix, VertexConsumer buffer, float len, float width, int color, float alpha) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        float halfWidth = width / 2f;
        float halfLen = len / 2f;

        // Draw 3 quads rotated around the central axis of the slash
        for (int i = 0; i < 3; i++) {
            float angle = i * (Mth.PI / 3f);
            float xOffset = Mth.cos(angle) * halfWidth;
            float yOffset = Mth.sin(angle) * halfWidth;

            // Vertices are calculated to "extrude" the line into a plane
            // based on the current rotation angle.
            buffer.addVertex(matrix, -xOffset, -yOffset, -halfLen).setColor(r, g, b, alpha);
            buffer.addVertex(matrix, xOffset, yOffset, -halfLen).setColor(r, g, b, alpha);
            buffer.addVertex(matrix, xOffset, yOffset, halfLen).setColor(r, g, b, alpha);
            buffer.addVertex(matrix, -xOffset, -yOffset, halfLen).setColor(r, g, b, alpha);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SwordStormEntity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}