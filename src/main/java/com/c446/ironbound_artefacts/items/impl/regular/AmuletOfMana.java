package com.c446.ironbound_artefacts.items.impl.regular;

import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class AmuletOfMana extends CurioBaseItem {
    public AmuletOfMana(Properties properties) {
        super(properties);

    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        if (slotContext.entity().tickCount % 20 == 0) {
            stack.setDamageValue((int) (stack.getDamageValue() + Math.log10((double) stack.getMaxDamage() / 2 - stack.getDamageValue())));
        }
    }
}
