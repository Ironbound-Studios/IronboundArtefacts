package com.c446.ironbound_artefacts.common.effects;

import com.c446.ironbound_artefacts.registries.RegistryAttachment;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class MarkoEffect extends IronboundMobEffect {

    public MarkoEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onMobRemoved(LivingEntity pLivingEntity, int pAmplifier, Entity.RemovalReason pReason) {
        pLivingEntity.removeData(RegistryAttachment.MARKOHESHKIR_ATTACHMENT);
        super.onMobRemoved(pLivingEntity, pAmplifier, pReason);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {

        return super.shouldApplyEffectTickThisTick(pDuration, pAmplifier);
    }
}
