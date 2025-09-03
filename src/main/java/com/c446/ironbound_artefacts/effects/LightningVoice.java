package com.c446.ironbound_artefacts.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class LightningVoice extends IronboundMobEffect.IgnoreAntimagic {
    public LightningVoice(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int p_295329_, int p_295167_) {
        return true;
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity living, int amplifier) {
        if (living.level() instanceof ServerLevel level) {
            level.setWeatherParameters(0, 20 * 30, true, true);
        }

        return super.applyEffectTick(living, amplifier);
    }
}
