package com.c446.ironbound_artefacts.client.entity_renderers;

import com.c446.ironbound_artefacts.common.entities.LastPrismEntity;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import static net.minecraft.client.renderer.RenderStateShard.*;

@OnlyIn(Dist.CLIENT)
public class LastPrismRenderer extends EntityRenderer<LastPrismEntity> {
    public static final int BEAM_COUNT = 7;
    public static final float MAX_SPREAD = 15.0F;
    public static final float BEAM_LENGTH = 72.0F;
    public static final float CHARGE_TICKS = 40.0F;
    public static final float ROTATION_SPEED = 0.12F;

    public static final RenderType PRISM_BEAM = RenderType.create(
            "prism_beam", DefaultVertexFormat.POSITION_COLOR,
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



    public LastPrismRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(LastPrismEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        Entity owner = pEntity.getOwner();
        if (owner == null) return;

        pPoseStack.pushPose();

        // 1. FIX STUTTERING: Relative Interpolation
        // Instead of using camPos, we calculate the delta between the Owner's eyes and the Entity's current position.
        // This keeps the beam "locked" to the player even if the entity's base position is slightly desynced.
        double ownerX = Mth.lerp(pPartialTicks, owner.xo, owner.getX());
        double ownerY = Mth.lerp(pPartialTicks, owner.yo, owner.getY()) + owner.getEyeHeight();
        double ownerZ = Mth.lerp(pPartialTicks, owner.zo, owner.getZ());

        double entityX = Mth.lerp(pPartialTicks, pEntity.xo, pEntity.getX());
        double entityY = Mth.lerp(pPartialTicks, pEntity.yo, pEntity.getY());
        double entityZ = Mth.lerp(pPartialTicks, pEntity.zo, pEntity.getZ());

        // Translate the PoseStack by the difference (moves beam from Entity root to Owner eyes)
        pPoseStack.translate(ownerX - entityX, ownerY - entityY, ownerZ - entityZ);

        // 2. SMOOTH ROTATION (Unchanged, ensures look direction is fluid)
        float yaw = owner.getViewYRot(pPartialTicks);
        float pitch = owner.getViewXRot(pPartialTicks);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(-yaw));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(pitch));

        VertexConsumer buffer = pBuffer.getBuffer(PRISM_BEAM);

        // 3. QUADRATIC PROGRESS & ROTATION
        float time = (float) pEntity.tickCount + pPartialTicks;
        float linearProgress = Mth.clamp(time / CHARGE_TICKS, 0.0F, 1.0F);

        // Quadratic Ease-In: The beam converges slowly at first, then snaps shut.
        float quadProgress = linearProgress * linearProgress;
        float currentSpread = MAX_SPREAD * (1.0F - quadProgress);

        // Quadratic Rotation: Speed increases over time (Acceleration)
        // Formula: (time^2) * base_speed
        float currentRotation = (time * time) * (ROTATION_SPEED * 0.05F);

        Vec3 eyePos = new Vec3(ownerX, ownerY, ownerZ);
        Vec3 smoothLookVec = Vec3.directionFromRotation(pitch, yaw);

        for (int i = 0; i < BEAM_COUNT; i++) {
            float ratio = (float) i / (float) BEAM_COUNT;
            float angle = (ratio * ((float) Math.PI * 2f)) + currentRotation;

            int color = (linearProgress >= 1.0F)
                    ? Mth.hsvToRgb((ratio + (time * 0.05F)) % 1.0F, 0.7F, 1.0F)
                    : Mth.hsvToRgb(ratio, 0.8F, 1.0F);

            // Raycast logic
            Vec3 beamDir = pEntity.calculateRayDirection(smoothLookVec, (float) Math.cos(angle) * currentSpread, (float) Math.sin(angle) * currentSpread, 1.0F);
            float dynamicLen = pEntity.getBeamLength(eyePos, beamDir, BEAM_LENGTH);

            pPoseStack.pushPose();
            pPoseStack.mulPose(Axis.ZP.rotation(angle));
            // Tilt the beam by the spread amount
            pPoseStack.mulPose(Axis.XP.rotationDegrees(currentSpread));

            renderStraightBeam(pPoseStack.last().pose(), buffer, dynamicLen, color, 0.5F, 0.25F);
            pPoseStack.popPose();
        }

        // 4. CENTER CORE (Quadratic Alpha/Width)
        if (linearProgress > 0.5F) {
            float coreAlpha = (quadProgress - 0.25F) * 1.33F; // Adjusting alpha curve
            float coreLen = pEntity.getBeamLength(eyePos, smoothLookVec, BEAM_LENGTH);
            renderStraightBeam(pPoseStack.last().pose(), buffer, coreLen, 0xFFFFFF, coreAlpha * 0.8F, 0.6F * quadProgress);
        }

        pPoseStack.popPose();
    }

    private void renderStraightBeam(Matrix4f matrix, VertexConsumer buffer, float length, int color, float alpha, float width) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        float hW = width / 2f;

        // Now we just draw a perfectly straight box along the Z axis.
        // Because the PoseStack is already rotated, this will point the right way.

        // Horizontal Quad
        buffer.addVertex(matrix, -hW, 0, 0).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, hW, 0, 0).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, hW, 0, length).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, -hW, 0, length).setColor(r, g, b, alpha);

        // Vertical Quad
        buffer.addVertex(matrix, 0, -hW, 0).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, 0, hW, 0).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, 0, hW, length).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, 0, -hW, length).setColor(r, g, b, alpha);
    }

    private void renderOrientedBeam(Matrix4f matrix, VertexConsumer buffer, float angle, float spread, float length, int color, float alpha, float width) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        float targetX = Mth.cos(angle) * spread;
        float targetY = Mth.sin(angle) * spread;
        float hW = width / 2f;

        float tx = -Mth.sin(angle) * hW;
        float ty = Mth.cos(angle) * hW;
        float nx = Mth.cos(angle) * hW;
        float ny = Mth.sin(angle) * hW;

        // Quad 1
        buffer.addVertex(matrix, -tx, -ty, 0).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, tx, ty, 0).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, targetX + tx, targetY + ty, length).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, targetX - tx, targetY - ty, length).setColor(r, g, b, alpha);

        // Quad 2
        buffer.addVertex(matrix, -nx, -ny, 0).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, nx, ny, 0).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, targetX + nx, targetY + ny, length).setColor(r, g, b, alpha);
        buffer.addVertex(matrix, targetX - nx, targetY - ny, length).setColor(r, g, b, alpha);
    }

    @Override
    public ResourceLocation getTextureLocation(LastPrismEntity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}