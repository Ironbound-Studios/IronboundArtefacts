package com.c446.ironbound_artefacts.common.items.impl.titles;

import com.c446.ironbound_artefacts.IBA;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

public class GenericTitleItem extends CurioBaseItem {
    public String rsrLoc = "";
    public String identifier = "";
    public GenericTitleItem(Properties properties) {
        super(properties);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            CuriosApi.addSlotModifier(super.getAttributeModifiers(slotContext, id, stack), identifier, IBA.p(rsrLoc), 1, AttributeModifier.Operation.ADD_VALUE);
        }
        return super.getAttributeModifiers(slotContext, id, stack);
    }
}
