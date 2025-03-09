package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class BelligerentSkies extends CurioBaseItem {
    public BelligerentSkies(Properties p) {
        super(p);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var map = super.getAttributeModifiers(slotContext, id, stack);
        map.put(AttributeRegistry.LIGHTNING_SPELL_POWER, new AttributeModifier(IronboundArtefact.prefix("lightning_glove"), 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        map.put(AttributeRegistry.SPELL_POWER, new AttributeModifier(IronboundArtefact.prefix("lightning_glove"), 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        map.put(AttributeRegistry.ICE_MAGIC_RESIST, new AttributeModifier(IronboundArtefact.prefix("lightning_glove"), -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return map;
    }
}
