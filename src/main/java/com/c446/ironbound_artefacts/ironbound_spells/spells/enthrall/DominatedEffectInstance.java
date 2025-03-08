package com.c446.ironbound_artefacts.ironbound_spells.spells.enthrall;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class DominatedEffectInstance extends MobEffectInstance {
    public LivingEntity emitter;
    public LivingEntity receiver;

    public DominatedEffectInstance(LivingEntity receiver, LivingEntity emitter, Holder<MobEffect> pEffect) {
        super(pEffect);
        this.emitter = emitter;
        this.receiver = receiver;
    }

    public DominatedEffectInstance(LivingEntity receiver, LivingEntity emitter, Holder<MobEffect> pEffect, int pDuration) {
        super(pEffect, pDuration);
        this.emitter = emitter;
        this.receiver = receiver;

    }

    public DominatedEffectInstance(LivingEntity receiver, LivingEntity emitter, Holder<MobEffect> pEffect, int pDuration, int pAmplifier) {
        super(pEffect, pDuration, pAmplifier);
        this.emitter = emitter;
        this.receiver = receiver;
    }

    public DominatedEffectInstance(LivingEntity receiver, LivingEntity emitter, Holder<MobEffect> pEffect, int pDuration, int pAmplifier, boolean pAmbient, boolean pVisible) {
        super(pEffect, pDuration, pAmplifier, pAmbient, pVisible);
        this.emitter = emitter;
        this.receiver = receiver;
    }

    public DominatedEffectInstance(LivingEntity receiver, LivingEntity emitter, Holder<MobEffect> pEffect, int pDuration, int pAmplifier, boolean pAmbient, boolean pVisible, boolean pShowIcon) {
        super(pEffect, pDuration, pAmplifier, pAmbient, pVisible, pShowIcon);
        this.emitter = emitter;

        this.receiver = receiver;
    }

    public DominatedEffectInstance(LivingEntity receiver, LivingEntity emitter, Holder<MobEffect> pEffect, int pDuration, int pAmplifier, boolean pAmbient, boolean pVisible, boolean pShowIcon, @Nullable MobEffectInstance pHiddenEffect) {
        super(pEffect, pDuration, pAmplifier, pAmbient, pVisible, pShowIcon, pHiddenEffect);
        this.emitter = emitter;
        this.receiver = receiver;
    }
}
