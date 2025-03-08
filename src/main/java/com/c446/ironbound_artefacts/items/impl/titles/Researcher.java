package com.c446.ironbound_artefacts.items.impl.titles;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class Researcher extends GenericTitleItem {
    public int mult;

    public Researcher(Properties properties, int mult) {
        super(properties);
        this.rsrLoc = "thaumaturge_slot";
        this.identifier = "charm";
        this.mult = mult;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var attributes = super.getAttributeModifiers(slotContext, id, stack);
        attributes.put(AttributeRegistry.COOLDOWN_REDUCTION, new AttributeModifier(IronboundArtefact.prefix("thaumaturge"), mult * .1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(AttributeRegistry.CAST_TIME_REDUCTION, new AttributeModifier(IronboundArtefact.prefix("thaumaturge"), mult * .1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(AttributeRegistry.MAX_MANA, new AttributeModifier(IronboundArtefact.prefix("thaumaturge"), mult * .1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(AttributeRegistry.MANA_REGEN, new AttributeModifier(IronboundArtefact.prefix("thaumaturge"), mult * .1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return attributes;
    }
}
