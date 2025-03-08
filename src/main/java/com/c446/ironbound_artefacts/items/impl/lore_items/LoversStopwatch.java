package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.c446.ironbound_artefacts.registries.CustomSpellRegistry;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class LoversStopwatch extends UserDependantCurios {

    public LoversStopwatch(Properties p) {
        super(p);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.ironbounds_artefacts.lovers_watch.tooltip1"));
        pTooltipComponents.add(Component.translatable("item.ironbounds_artefacts.lovers_watch.tooltip2").withStyle(ChatFormatting.ITALIC));
    }

    @Override
    public boolean canEntityUseItem(Entity entity) {
        return entity.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.AMON) || entity.getName().getString().equals("Dev");
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        super.onEquip(slotContext, prevStack, stack);
        if (canEntityUseItem(slotContext.entity())) {
            var copy = stack.copy();
            copy.set(ComponentRegistry.SPELL_CONTAINER, new SpellContainer(3, true, false, false, new SpellSlot[]{
                    new SpellSlot(new SpellData(CustomSpellRegistry.TIME_STOP.get(), 1, true), 0),
                    new SpellSlot(new SpellData(SpellRegistry.HASTE_SPELL.get(), 6, true), 1),
                    new SpellSlot(new SpellData(SpellRegistry.SLOW_SPELL.get(), 6, true), 2),}));
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a -> a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
        } else {
            var copy = stack.copy();
            copy.remove(ComponentRegistry.SPELL_CONTAINER);
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a -> a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
        }

    }


    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attributeMap = ICurioItem.defaultInstance.getAttributeModifiers(slotContext, id);
        double multiplier = 1;
        if (slotContext.entity() != null && canEntityUseItem(slotContext.entity())) {
            multiplier *= 2;
        }
        attributeMap.put(AttributeRegistry.COOLDOWN_REDUCTION, new AttributeModifier(id, 0.2 * multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributeMap.put(AttributeRegistry.ELDRITCH_SPELL_POWER, new AttributeModifier(id, 0.2 * multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return attributeMap;
    }
}
