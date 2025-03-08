package com.c446.ironbound_artefacts.entities.simulacrum;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.TransformStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SimulacrumModel<K extends SimulacrumEntity> extends DefaultedEntityGeoModel<K> {
    protected TransformStack transformStack = new TransformStack();

    public SimulacrumModel() {
        super(IronsSpellbooks.id("spellcastingmob"));
    }

    public ResourceLocation getModelResource(SimulacrumEntity object) {
        return AbstractSpellCastingMob.modelResource;
    }

    public ResourceLocation getTextureResource(SimulacrumEntity var1) {
        return var1.getSkinTextureLocation();
    }

    public ResourceLocation getAnimationResource(SimulacrumEntity animatable) {
        return AbstractSpellCastingMob.animationInstantCast;
    }

    public void setCustomAnimations(K entity, long instanceId, AnimationState<K> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
        if (!Minecraft.getInstance().isPaused() && entity.shouldBeExtraAnimated()) {
            float partialTick = animationState.getPartialTick();
            GeoBone head = this.getAnimationProcessor().getBone("head");
            GeoBone body = this.getAnimationProcessor().getBone("body");
            GeoBone torso = this.getAnimationProcessor().getBone("torso");
            GeoBone rightArm = this.getAnimationProcessor().getBone("right_arm");
            GeoBone leftArm = this.getAnimationProcessor().getBone("left_arm");
            GeoBone rightLeg = this.getAnimationProcessor().getBone("right_leg");
            GeoBone leftLeg = this.getAnimationProcessor().getBone("left_leg");
            if (!entity.isAnimating() || entity.shouldAlwaysAnimateHead()) {
                this.transformStack.pushRotation(head, Mth.lerp(partialTick, -entity.xRotO, -entity.getXRot()) * ((float) Math.PI / 180F), Mth.lerp(partialTick, Mth.wrapDegrees(-entity.yHeadRotO + entity.yBodyRotO) * ((float) Math.PI / 180F), Mth.wrapDegrees(-entity.yHeadRot + entity.yBodyRot) * ((float) Math.PI / 180F)), 0.0F);
            }

            WalkAnimationState walkAnimationState = entity.walkAnimation;
            float pLimbSwingAmount = 0.0F;
            float pLimbSwing = 0.0F;
            if (entity.isAlive()) {
                pLimbSwingAmount = walkAnimationState.speed(partialTick);
                pLimbSwing = walkAnimationState.position(partialTick);
                if (entity.isBaby()) {
                    pLimbSwing *= 3.0F;
                }

                if (pLimbSwingAmount > 1.0F) {
                    pLimbSwingAmount = 1.0F;
                }
            }

            float f = 1.0F;
            if (entity.getFallFlyingTicks() > 4) {
                f = (float) entity.getDeltaMovement().lengthSqr();
                f /= 0.2F;
                f *= f * f;
            }

            if (f < 1.0F) {
                f = 1.0F;
            }

            if (entity.isPassenger() && entity.getVehicle().shouldRiderSit()) {
                this.transformStack.pushRotation(rightLeg, 1.4137167F, (-(float) Math.PI / 10F), -0.07853982F);
                this.transformStack.pushRotation(leftLeg, 1.4137167F, ((float) Math.PI / 10F), 0.07853982F);
            } else if (!entity.isAnimating() || entity.shouldAlwaysAnimateLegs()) {
                float strength = 0.75F;
                Vec3 facing = entity.getForward().multiply(1.0F, 0.0F, 1.0F).normalize();
                Vec3 momentum = entity.getDeltaMovement().multiply(1.0F, 0.0F, 1.0F).normalize();
                Vec3 facingOrth = new Vec3(-facing.z, 0.0F, facing.x);
                float directionForward = (float) facing.dot(momentum);
                float directionSide = (float) facingOrth.dot(momentum) * 0.35F;
                float rightLateral = -Mth.sin(pLimbSwing * 0.6662F) * 4.0F * pLimbSwingAmount;
                float leftLateral = -Mth.sin(pLimbSwing * 0.6662F - (float) Math.PI) * 4.0F * pLimbSwingAmount;
                this.transformStack.pushPosition(rightLeg, rightLateral * directionSide, Mth.cos(pLimbSwing * 0.6662F) * 4.0F * strength * pLimbSwingAmount, rightLateral * directionForward);
                this.transformStack.pushRotation(rightLeg, Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount * strength, 0.0F, 0.0F);
                this.transformStack.pushPosition(leftLeg, leftLateral * directionSide, Mth.cos(pLimbSwing * 0.6662F - (float) Math.PI) * 4.0F * strength * pLimbSwingAmount, leftLateral * directionForward);
                this.transformStack.pushRotation(leftLeg, Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * pLimbSwingAmount * strength, 0.0F, 0.0F);
                if (entity.bobBodyWhileWalking()) {
                    this.transformStack.pushPosition(body, 0.0F, Mth.abs(Mth.cos((pLimbSwing * 1.2662F - ((float) Math.PI / 2F)) * 0.5F)) * 2.0F * strength * pLimbSwingAmount, 0.0F);
                }
            }

            if (!entity.isAnimating()) {
                this.transformStack.pushRotationWithBase(rightArm, Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 2.0F * pLimbSwingAmount * 0.5F / f, 0.0F, 0.0F);
                this.transformStack.pushRotationWithBase(leftArm, Mth.cos(pLimbSwing * 0.6662F) * 2.0F * pLimbSwingAmount * 0.5F / f, 0.0F, 0.0F);
                this.bobBone(rightArm, entity.tickCount, 1.0F);
                this.bobBone(leftArm, entity.tickCount, -1.0F);
                if (entity.isDrinkingPotion()) {
                    this.transformStack.pushRotation(entity.isLeftHanded() ? leftArm : rightArm, 0.61086524F, (float) (entity.isLeftHanded() ? -25 : 25) * ((float) Math.PI / 180F), (float) (entity.isLeftHanded() ? 15 : -15) * ((float) Math.PI / 180F));
                }
            } else if (entity.shouldPointArmsWhileCasting() && entity.isCasting()) {
                this.transformStack.pushRotation(leftArm, leftArm.getRotX() - entity.getXRot() * ((float) Math.PI / 180F), leftArm.getRotY(), leftArm.getRotZ());
                this.transformStack.pushRotation(rightArm, rightArm.getRotX() - entity.getXRot() * ((float) Math.PI / 180F), rightArm.getRotY(), rightArm.getRotZ());
            }

            this.transformStack.popStack();
        }
    }

    protected void bobBone(GeoBone bone, int offset, float multiplier) {
        float z = multiplier * (Mth.cos((float) offset * 0.09F) * 0.05F + 0.05F);
        float x = multiplier * Mth.sin((float) offset * 0.067F) * 0.05F;
        this.transformStack.pushRotation(bone, x, 0.0F, z);
    }
}
