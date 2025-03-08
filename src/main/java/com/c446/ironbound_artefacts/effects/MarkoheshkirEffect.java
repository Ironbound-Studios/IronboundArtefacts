package com.c446.ironbound_artefacts.effects;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class MarkoheshkirEffect extends IronboundMobEffect {
    public MarkoheshkirEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public MarkoheshkirEffect addAttributeModifier(Holder<Attribute> pAttribute, ResourceLocation pId, double pAmount, AttributeModifier.Operation pOperation) {
        return (MarkoheshkirEffect) super.addAttributeModifier(pAttribute, pId, pAmount, pOperation);
    }
}