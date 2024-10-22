package com.c446.ironbound_artefacts.items.impl;

import com.c446.ironbound_artefacts.items.UserDependantCurios;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class AmuletOfHolding extends UserDependantCurios {
    public AmuletOfHolding(Properties p) {
        super(p);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!ISpellContainer.isSpellContainer(stack)) {
            stack.set(ComponentRegistry.SPELL_CONTAINER, new SpellContainer(4, true, false, false));
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (ISpellContainer.isSpellContainer(stack)) {
            tooltipComponents.add(Component.translatable("item.ironbounds_artefacts.amulet_of_holding.tooltip1"));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}