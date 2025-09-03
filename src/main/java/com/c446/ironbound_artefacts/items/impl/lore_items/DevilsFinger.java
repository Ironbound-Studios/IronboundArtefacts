package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
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

import java.util.List;
import java.util.Map;

public class DevilsFinger extends UserDependantCurios implements IPresetSpellContainer {
    private boolean canUse = false;

    public DevilsFinger(Properties p) {
        super(p);
    }



    @Override
    public boolean canEntityUseItem(Entity entity) {
        if (entity instanceof Player player) {
            canUse = (player.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.ACE) || entity.getName().getString().equals("Dev"));
            return canUse;
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag tooltipFlag) {
        lines.add(Component.translatable("item.ironbounds_artefacts.devils_finger.tooltip1"));
        lines.add(Component.translatable("item.ironbounds_artefacts.devils_finger.tooltip2").withStyle(ChatFormatting.ITALIC));
        handleAffinityLines(stack, context, lines, tooltipFlag);
        super.appendHoverText(stack, context, lines, tooltipFlag);

    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        int multiplier = 1;
        if (canEntityUseItem(slotContext.entity())) {
            multiplier = 2;
        }
        Multimap<Holder<Attribute>, AttributeModifier> attributeMap = ICurioItem.defaultInstance.getAttributeModifiers(slotContext, id);
        attributeMap.put(AttributeRegistry.ELDRITCH_SPELL_POWER, new AttributeModifier(id, 0.25 * multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        AttributeModifier value = new AttributeModifier(id, -0.3 * 1 / multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        attributeMap.put(AttributeRegistry.HOLY_SPELL_POWER, value);
        attributeMap.put(AttributeRegistry.HOLY_MAGIC_RESIST, value);
        return attributeMap;
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        itemStack.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(Map.of(SpellRegistry.ELDRITCH_BLAST_SPELL.get().getSpellResource(), 2)));
    }
}
