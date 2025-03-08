package com.c446.ironbound_artefacts.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

//credits for code go to: https://github.com/dkmk100/ArsOmega/blob/37c9ba1d9c76ccde893ca12a8987bf4bdc9e4ffa/src/main/java/com/dkmk100/arsomega/items/BasicItem.java
public abstract class BaseItem extends Item {
    boolean showEnch = false;
    String name;

    public BaseItem(Properties p) {
        super(p);
    }

    public BaseItem(Properties p, boolean showEnch) {
        super(p);
        this.showEnch = showEnch;
    }

    @Override
    public boolean isFoil(ItemStack s) {
        if (showEnch) {
            return true;
        } else {
            return super.isFoil(s);
        }
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
    }
}
