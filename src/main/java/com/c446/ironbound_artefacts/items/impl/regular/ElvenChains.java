package com.c446.ironbound_artefacts.items.impl.regular;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class ElvenChains extends CurioBaseItem {
    public ElvenChains(Properties properties) {
        super(properties);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var attr = super.getAttributeModifiers(slotContext, id, stack);
        if (slotContext.entity() != null) {
            if (slotContext.entity().getAttributeValue(Attributes.ARMOR) <= 15) {
                attr.put(Attributes.ARMOR, new AttributeModifier(IronboundArtefact.prefix("elven_chains"), 15 - slotContext.entity().getAttributeValue(Attributes.ARMOR), AttributeModifier.Operation.ADD_VALUE));
            }
        }
        return attr;
    }
}
