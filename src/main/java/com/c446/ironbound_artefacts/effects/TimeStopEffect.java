package com.c446.ironbound_artefacts.effects;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.MagicHelper;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.registries.DataAttachmentRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.spells.ender.CounterspellSpell;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.EffectCure;

import java.util.Set;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class TimeStopEffect extends IronboundMobEffect {
    public TimeStopEffect(MobEffectCategory category, int color) {
        super(category, color);

    }

    @Override
    public TimeStopEffect addAttributeModifier(Holder<Attribute> pAttribute, ResourceLocation pId, double pAmount, AttributeModifier.Operation pOperation) {
        return (TimeStopEffect)super.addAttributeModifier(pAttribute, pId, pAmount, pOperation);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        livingEntity.setDeltaMovement(0, 0, 0);
        if (livingEntity.hasData(DataAttachmentRegistry.MAGIC_DATA) && livingEntity instanceof ServerPlayer serverPlayer) {
            Utils.serverSideCancelCast(serverPlayer, true);
            MagicData.getPlayerMagicData(serverPlayer).getPlayerRecasts().removeAll(RecastResult.COUNTERSPELL);
        }
        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        // Leave this empty to prevent milk or curatives from clearing
    }
}
