package com.c446.ironbound_artefacts.common.effects;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.registries.DataAttachmentRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.EffectCure;

import java.util.Set;

public class TimeStopEffect extends IronboundMobEffect {
    public TimeStopEffect(MobEffectCategory category, int color) {
        super(category, color);

    }

    @Override
    public TimeStopEffect addAttributeModifier(Holder<Attribute> pAttribute, ResourceLocation pId, double pAmount, AttributeModifier.Operation pOperation) {
        return (TimeStopEffect) super.addAttributeModifier(pAttribute, pId, pAmount, pOperation);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level().isClientSide) {
            // If this is the 'Time Stop' caster or the central anchor
            // update the global tracker radius

            // Impact: Spawn 'freeze' particles around affected entities
            if (livingEntity.level().random.nextFloat() < 0.1f) {
                livingEntity.level().addParticle(ParticleTypes.SNOWFLAKE,
                        livingEntity.getRandomX(0.5D), livingEntity.getRandomY(), livingEntity.getRandomZ(0.5D),
                        0, 0, 0);
            }
        }

        //livingEntity.setDeltaMovement(0, 0, 0);
        if (livingEntity.hasData(DataAttachmentRegistry.MAGIC_DATA) && livingEntity instanceof ServerPlayer serverPlayer) {
            Utils.serverSideCancelCast(serverPlayer, true);
            MagicData.getPlayerMagicData(serverPlayer).getPlayerRecasts().removeAll(RecastResult.COUNTERSPELL);
        }
        livingEntity.setPos(new Vec3(livingEntity.xOld, livingEntity.yOld, livingEntity.zOld));
        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        // Leave this empty to prevent milk or curatives from clearing
    }
}
