package com.c446.ironbound_artefacts.items.impl;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MagicianMonocle extends UserDependantCurios {
    public MagicianMonocle(Properties p) {
        super(p);
    }

    public MagicianMonocle(Properties p, boolean showEnch) {
        super(p, showEnch);
    }

    @Override
    public boolean canEntityUseItem(Entity entity) {
        if (entity instanceof Player player) {
            return (player.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.AMON) || entity.getName().getString().equals("Dev"));
        }
        return false;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        int multiplier = 1;
        if (canEntityUseItem(slotContext.entity())) {
            multiplier = 2;
        }
        var attributeModifier = ICurioItem.defaultInstance.getAttributeModifiers(slotContext, id);
        attributeModifier.put(AttributeRegistry.SPELL_POWER, new AttributeModifier(IronboundArtefact.prefix("magicians_monocle"), 0.2 * multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        return attributeModifier;
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag tooltipFlag) {
        lines.add(Component.translatable("item.ironbounds_artefacts.magicians_monocle.tooltip"));
        var affinity = AffinityData.getAffinityData(stack);
        var spell = affinity.getSpell();
        if (!spell.equals(SpellRegistry.none())) {
            lines.add(Component.empty());
            lines.add(Component.translatable("curios.modifiers.head").withStyle(ChatFormatting.GOLD));
            var name = spell.getDisplayName(MinecraftInstanceHelper.instance.player()).withStyle(spell.getSchoolType().getDisplayName().getStyle());
            lines.add(Component.literal(" ").append(
                    (affinity.bonus() == 1 ? Component.translatable("tooltip.irons_spellbooks.enhance_spell_level", name) : Component.translatable("tooltip.irons_spellbooks.enhance_spell_level_plural", affinity.bonus(), name))
                            .withStyle(ChatFormatting.YELLOW)));
        }
        super.appendHoverText(stack, context, lines, tooltipFlag);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (canEntityUseItem(slotContext.entity())) {
            if (AffinityData.getAffinityData(stack).getSpell().getSpellId() == SpellRegistry.MAGIC_MISSILE_SPELL.get().getSpellId() && AffinityData.getAffinityData(stack).bonus() > 0) {
            }
            var copy = stack.copy();
//            var ComponentList = new ArrayList<AffinityData>();
//            ComponentList.add(new AffinityData(SpellRegistry.FIREBALL_SPELL.get().getSpellId(), 3));
//            ComponentList.add(new AffinityData(SpellRegistry.LIGHTNING_BOLT_SPELL.get().getSpellId(), 3));
//            ComponentList.add(new AffinityData(SpellRegistry.MAGIC_MISSILE_SPELL.get().getSpellId(), 3));
//            ComponentList.add(new AffinityData(SpellRegistry.OAKSKIN_SPELL.get().getSpellId(), 3));
//            ComponentList.add(new AffinityData(SpellRegistry.HASTE_SPELL.get().getSpellId(), 3));
//            copy.set(ComponentRegistry.AFFINITY_COMPONENT, ComponentList);

            copy.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(SpellRegistry.SCULK_TENTACLES_SPELL.get().getSpellId(), 7));
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a -> a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
        } else {
            var copy = stack.copy();
            copy.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(SpellRegistry.none().getSpellId(), 0));
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a -> a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
        }
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}
