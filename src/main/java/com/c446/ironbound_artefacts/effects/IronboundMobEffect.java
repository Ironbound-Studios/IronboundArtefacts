package com.c446.ironbound_artefacts.effects;

import com.c446.ironbound_artefacts.registries.IronboundDamageSources;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class IronboundMobEffect extends MobEffect {


    public IronboundMobEffect(net.minecraft.world.effect.MobEffectCategory category, int color) {
        super(category, color);
    }

    protected IronboundMobEffect(net.minecraft.world.effect.MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color, particle);

    }

    public static class VoidPoison extends IronboundMobEffect {

        public VoidPoison(MobEffectCategory category, int color) {
            super(category, color);
        }

        @Override
        public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
            if (livingEntity.tickCount % (40 / ((amplifier * 2 == 0) ? (1) : (amplifier * 2))) == 0) {
                livingEntity.hurt(DamageSources.get(livingEntity.level(), IronboundDamageSources.VOID_DAMAGE), 1);
            }
            return super.applyEffectTick(livingEntity, amplifier);
        }

        @Override
        public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
            return true;
        }

        @Override
        public IronboundMobEffect addAttributeModifier(Holder<Attribute> pAttribute, ResourceLocation pId, double pAmount, AttributeModifier.Operation pOperation) {
            return (IronboundMobEffect) super.addAttributeModifier(pAttribute, pId, pAmount, pOperation);
        }
    }
}