package com.c446.ironbound_artefacts.items.impl.regular;

import com.c446.ironbound_artefacts.items.UserDependantCurios;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class AmuletOfHolding extends CurioBaseItem {
    public AmuletOfHolding(Properties p) {
        super(p);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (ISpellContainer.isSpellContainer(stack)) {
            tooltipComponents.add(Component.translatable("item.ironbounds_artefacts.amulet_of_holding.tooltip1"));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
