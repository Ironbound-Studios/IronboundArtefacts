package com.c446.ironbound_artefacts.effects;

import com.c446.ironbound_artefacts.registries.AttachmentRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class MarkoEffect extends IronboundMobEffect {

    public MarkoEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onMobRemoved(LivingEntity pLivingEntity, int pAmplifier, Entity.RemovalReason pReason) {
        pLivingEntity.removeData(AttachmentRegistry.MARKOHESHKIR_ATTACHMENT);
        super.onMobRemoved(pLivingEntity, pAmplifier, pReason);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {

        return super.shouldApplyEffectTickThisTick(pDuration, pAmplifier);
    }
}
