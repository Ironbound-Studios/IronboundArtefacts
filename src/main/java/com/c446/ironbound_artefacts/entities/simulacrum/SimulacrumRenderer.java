package com.c446.ironbound_artefacts.entities.simulacrum;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SimulacrumRenderer extends LivingEntityRenderer<SimulacrumEntity, PlayerModel<SimulacrumEntity>> {

    public SimulacrumRenderer(EntityRendererProvider.Context context) {
        this(context, false);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(SimulacrumEntity simulacrumEntity) {
        return simulacrumEntity.getSkinTextureLocation();
    }

    private final PlayerModel<SimulacrumEntity> playerModel;
    private final PlayerModel<SimulacrumEntity> playerModelSlim;

    public SimulacrumRenderer(EntityRendererProvider.Context context, boolean slim) {
        super(context, new PlayerModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim), 0.5F);
        playerModel = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER),false);
        playerModelSlim = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new ArrowLayer<>(context, this));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
    }

    public void render(@NotNull SimulacrumEntity simulacrumEntity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int p_225623_6_) {
        this.setModelProperties(simulacrumEntity);
        super.render(simulacrumEntity, entityYaw, partialTicks, poseStack, bufferSource, p_225623_6_);
    }

    public @NotNull Vec3 getRenderOffset(SimulacrumEntity simulacrumEntity, float PartialTicks) {
        return simulacrumEntity.isCrouching() ? new Vec3(0.0D, -0.125D, 0.0D) : super.getRenderOffset(simulacrumEntity, PartialTicks);
    }

    private void setModelProperties(SimulacrumEntity simulacrumEntity) {
        if (simulacrumEntity.isSlim()){
            this.model = playerModelSlim;
        }else {
            this.model = playerModel;
        }

        PlayerModel<SimulacrumEntity> playermodel = this.getModel();
        if (simulacrumEntity.isSpectator()) {
            playermodel.setAllVisible(false);
            playermodel.head.visible = true;
            playermodel.hat.visible = true;
        } else {
            playermodel.setAllVisible(true);
            playermodel.crouching = simulacrumEntity.isCrouching();
            HumanoidModel.ArmPose bipedmodel$armpose = getArmPose(simulacrumEntity, InteractionHand.MAIN_HAND);
            HumanoidModel.ArmPose bipedmodel$armpose1 = getArmPose(simulacrumEntity, InteractionHand.OFF_HAND);
            if (bipedmodel$armpose.isTwoHanded()) {
                bipedmodel$armpose1 = simulacrumEntity.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
            }

            if (simulacrumEntity.getMainArm() == HumanoidArm.RIGHT) {
                playermodel.rightArmPose = bipedmodel$armpose;
                playermodel.leftArmPose = bipedmodel$armpose1;
            } else {
                playermodel.rightArmPose = bipedmodel$armpose1;
                playermodel.leftArmPose = bipedmodel$armpose;
            }
        }

    }

    private static HumanoidModel.ArmPose getArmPose(SimulacrumEntity simulacrumEntity, InteractionHand hand) {
        ItemStack itemstack = simulacrumEntity.getItemInHand(hand);
        if (itemstack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if (simulacrumEntity.getUsedItemHand() == hand && simulacrumEntity.getUseItemRemainingTicks() > 0) {
                UseAnim useaction = itemstack.getUseAnimation();
                if (useaction == UseAnim.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }

                if (useaction == UseAnim.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }

                if (useaction == UseAnim.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }

                if (useaction == UseAnim.CROSSBOW && hand == simulacrumEntity.getUsedItemHand()) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }
            } else if (!simulacrumEntity.swinging && itemstack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack)) {
                return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }

            return HumanoidModel.ArmPose.ITEM;
        }
    }


    protected void scale(@NotNull SimulacrumEntity simulacrumEntity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    public void renderRightHand(PoseStack poseStack, MultiBufferSource bufferSource, int i, SimulacrumEntity simulacrumEntity) {
        this.renderHand(poseStack, bufferSource, i, simulacrumEntity, (this.model).rightArm, (this.model).rightSleeve);
    }

    public void renderLeftHand(PoseStack poseStack, MultiBufferSource bufferSource, int i, SimulacrumEntity simulacrumEntity) {
        this.renderHand(poseStack, bufferSource, i, simulacrumEntity, (this.model).leftArm, (this.model).leftSleeve);
    }


    @Override
    protected void renderNameTag(SimulacrumEntity pEntity, @NotNull Component pDisplayName, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, float pPartialTick) {
        if(!pEntity.shouldShowName()){
            return;
        }
        super.renderNameTag(pEntity, pDisplayName, pPoseStack, pBuffer, pPackedLight, pPartialTick);
    }

    private void renderHand(PoseStack p_229145_1_, MultiBufferSource p_229145_2_, int p_229145_3_, SimulacrumEntity p_229145_4_, ModelPart p_229145_5_, ModelPart p_229145_6_) {
        PlayerModel<SimulacrumEntity> playermodel = this.getModel();
        this.setModelProperties(p_229145_4_);
        playermodel.attackTime = 0.0F;
        playermodel.crouching = false;
        playermodel.swimAmount = 0.0F;
        playermodel.setupAnim(p_229145_4_, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        p_229145_5_.xRot = 0.0F;
        p_229145_5_.render(p_229145_1_, p_229145_2_.getBuffer(RenderType.entitySolid(p_229145_4_.getSkinTextureLocation())), p_229145_3_, OverlayTexture.NO_OVERLAY);
        p_229145_6_.xRot = 0.0F;
        p_229145_6_.render(p_229145_1_, p_229145_2_.getBuffer(RenderType.entityTranslucent(p_229145_4_.getSkinTextureLocation())), p_229145_3_, OverlayTexture.NO_OVERLAY);
    }

    @Override
    protected void setupRotations(SimulacrumEntity simulacrumEntity, @NotNull PoseStack poseStack, float setupRotations, float rotations, float rotations1, float pScale) {
        float f = simulacrumEntity.getSwimAmount(rotations1);
        if (simulacrumEntity.isFallFlying()) {
            super.setupRotations(simulacrumEntity, poseStack, setupRotations, rotations, rotations1, pScale);
            float f1 = (float) simulacrumEntity.getFallFlyingTicks() + rotations1;
            float f2 = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
            if (!simulacrumEntity.isAutoSpinAttack()) {
                poseStack.mulPose(Axis.XP.rotationDegrees(f2 * (-90.0F - simulacrumEntity.getXRot())));
            }

            Vec3 vector3d = simulacrumEntity.getViewVector(rotations1);
            Vec3 vector3d1 = simulacrumEntity.getDeltaMovement();
            double d0 = vector3d1.horizontalDistanceSqr();
            double d1 = vector3d.horizontalDistanceSqr();
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (vector3d1.x * vector3d.x + vector3d1.z * vector3d.z) / Math.sqrt(d0 * d1);
                double d3 = vector3d1.x * vector3d.z - vector3d1.z * vector3d.x;
                poseStack.mulPose(Axis.YP.rotation((float) (Math.signum(d3) * Math.acos(d2))));
            }
        } else if (f > 0.0F) {
            super.setupRotations(simulacrumEntity, poseStack, setupRotations, rotations, rotations1, pScale);
            float f3 = simulacrumEntity.isInWater() ? -90.0F - simulacrumEntity.getXRot() : -90.0F;
            float f4 = Mth.lerp(f, 0.0F, f3);
            poseStack.mulPose(Axis.XP.rotationDegrees(f4));
            if (simulacrumEntity.isVisuallySwimming()) {
                poseStack.translate(0.0D, -1.0D, 0.3F);
            }
        } else {
            super.setupRotations(simulacrumEntity, poseStack, setupRotations, rotations, rotations1, pScale);
        }

    }
}
