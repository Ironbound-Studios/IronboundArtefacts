package com.c446.ironbound_artefacts.common.items.impl.lore_items;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.common.items.UserDependantCurios;
import com.c446.ironbound_artefacts.registries.RegistryAttributes;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class InsightCharm extends UserDependantCurios {
    public InsightCharm(Properties p) {
        super(p);
    }

    @Override
    public boolean canEntityUseItem(Entity entity) {
        return entity.getStringUUID().equals(IBA.ContributorUUIDS.AMADHE) || entity.getStringUUID().equals(IBA.ContributorUUIDS.AMON);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pIsAdvanced.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("item.ironbounds_artefacts.insight_insignia.tooltip.1").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(Component.translatable("item.ironbounds_artefacts.insight_insignia.tooltip.2").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(Component.translatable("item.ironbounds_artefacts.insight_insignia.tooltip.3").withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (canEntityUseItem(pEntity) && !pStack.has(DataComponents.CUSTOM_NAME)) {
            pStack.set(DataComponents.CUSTOM_NAME, Component.translatable("item.ironbounds_artefacts.insight_insignia.unique"));
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var map = super.getAttributeModifiers(slotContext, id, stack);
        map.put(RegistryAttributes.INSIGHT, new AttributeModifier(IBA.p("insight_charm"), 3, AttributeModifier.Operation.ADD_VALUE));
        return map;
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        itemStack.set(ComponentRegistry.SPELL_CONTAINER, new SpellContainer(1,true,false, false, new SpellSlot[]{
                SpellSlot.of(new SpellData(SpellRegistry.PLANAR_SIGHT_SPELL.get(), 5, true),0)
        }));
    }
}
