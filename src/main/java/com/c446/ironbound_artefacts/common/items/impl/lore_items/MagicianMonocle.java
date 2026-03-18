package com.c446.ironbound_artefacts.common.items.impl.lore_items;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.common.items.UserDependantCurios;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Map;

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
            return (player.getStringUUID().equals(IBA.ContributorUUIDS.AMON) || entity.getName().getString().equals("Dev"));
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
        attributeModifier.put(AttributeRegistry.ELDRITCH_SPELL_POWER, new AttributeModifier(id, 0.15 * Math.pow(multiplier, 2), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        return attributeModifier;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> lines, @NotNull TooltipFlag tooltipFlag) {
        lines.add(Component.translatable("item.ironbounds_artefacts.magicians_monocle.tooltip1"));
        lines.add(Component.translatable("item.ironbounds_artefacts.magicians_monocle.tooltip2").withStyle(ChatFormatting.ITALIC));
        handleAffinityLines(stack, context, lines, tooltipFlag);
        super.appendHoverText(stack, context, lines, tooltipFlag);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        itemStack.set(ComponentRegistry.SPELL_CONTAINER, new SpellContainer(3, true, false, false, new SpellSlot[]{
                new SpellSlot(new SpellData(SpellRegistry.PLANAR_SIGHT_SPELL.get(), 3, true), 1),
                new SpellSlot(new SpellData(SpellRegistry.POCKET_DIMENSION_SPELL.get(), 1, true), 2)
        }));
        itemStack.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(Map.of(
                SpellRegistry.LIGHTNING_BOLT_SPELL.get().getSpellResource(), 2,
                SpellRegistry.CHARGE_SPELL.get().getSpellResource(), 2)));

    }
}
