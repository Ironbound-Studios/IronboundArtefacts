package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class JudgementScale extends UserDependantCurios {

    public JudgementScale(Properties p) {
        super(p);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canEntityUseItem(Entity entity) {
        if (entity instanceof Player player) {
            return (player.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.CATMOTH) || entity.getName().getString().equals("Dev"));
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag tooltipFlag) {
        lines.add(Component.translatable("item.ironbounds_artefacts.judgement_scale.tooltip1"));
        lines.add(Component.translatable("item.ironbounds_artefacts.judgement_scale.tooltip2").withStyle(ChatFormatting.ITALIC));
        var affinity = AffinityData.getAffinityData(stack);
        var spell = affinity.getSpell();
        if (!spell.equals(SpellRegistry.none())) {
            lines.add(Component.empty());
            lines.add(Component.translatable("curios.modifiers.hands").withStyle(ChatFormatting.GOLD));
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
            if (AffinityData.getAffinityData(stack).getSpell().getSpellId() == SpellRegistry.RAISE_DEAD_SPELL.get().getSpellId() && AffinityData.getAffinityData(stack).bonus() > 0) {
            }
            var copy = stack.copy();
            copy.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(SpellRegistry.RAISE_DEAD_SPELL.get().getSpellId(), 3));
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a -> a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
        } else {
            var copy = stack.copy();
            copy.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(SpellRegistry.none().getSpellId(), 0));
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a -> a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        int multiplier = 1;
        if (canEntityUseItem(slotContext.entity())) {
            multiplier = 2;
        }
        var modifiers = ICurioItem.defaultInstance.getAttributeModifiers(slotContext, id);
        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(IronboundArtefact.prefix("judgement_scale"), 0.125 * (multiplier + 1), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        modifiers.put(Attributes.ARMOR, new AttributeModifier(IronboundArtefact.prefix("judgement_scale"), -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        return modifiers;
    }
}
