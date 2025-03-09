package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import io.redspace.ironsspellbooks.item.CastingItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ArcaneWand extends CastingItem {
    public ArcaneWand(Properties p) {
        super(p);
    }

    public boolean canEntityUseItem(Entity entity) {
        return entity.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.AMADHE);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if (pTooltipFlag.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("item.ironbounds_artefacts.wizard_wand.tooltip1").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(Component.translatable("item.ironbounds_artefacts.wizard_wand.tooltip2").withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (canEntityUseItem(pEntity) && !pStack.has(DataComponents.CUSTOM_NAME)) {
            pStack.set(DataComponents.CUSTOM_NAME, Component.translatable("item.ironbounds_artefacts.wizard_wand"));
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }
}
