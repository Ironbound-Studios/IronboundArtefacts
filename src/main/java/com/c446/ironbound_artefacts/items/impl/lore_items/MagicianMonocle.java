package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
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
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

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
        double multiplier = 1;
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
            var copy = stack.copy();
            copy.set(ComponentRegistry.SPELL_CONTAINER, new SpellContainer(4, true, false, false, new SpellSlot[]{
                            new SpellSlot(new SpellData(SpellRegistry.OAKSKIN_SPELL.get(), 10, true), 0),
                            new SpellSlot(new SpellData(SpellRegistry.CHARGE_SPELL.get(), 5, true), 1),
                            new SpellSlot(new SpellData(SpellRegistry.SCULK_TENTACLES_SPELL.get(), 6, true), 2),
                            new SpellSlot(new SpellData(SpellRegistry.SONIC_BOOM_SPELL.get(), 5, true), 3)
                    })
            );
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a -> a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
        } else {
            var copy = stack.copy();
            copy.remove(ComponentRegistry.SPELL_CONTAINER);
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a -> a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
        }
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}
