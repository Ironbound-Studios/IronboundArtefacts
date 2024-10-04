package com.c446.ironbound_artefacts.items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Objects;

public class DevilsFinger extends UserDependantCurios {

    public DevilsFinger(Properties p) {
        super(p);
    }

    @Override
    public boolean canEntityUseItem(Entity entity) {
        if (entity instanceof Player player) {
            return (player.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.ACE) || entity.getName().getString().equals("Dev"));
        }
        return false;
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
        attributeMap.put(AttributeRegistry.ELDRITCH_SPELL_POWER, new AttributeModifier(IronboundArtefact.prefix("devil_ring"), 0.2 * multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributeMap.put(AttributeRegistry.HOLY_SPELL_POWER, new AttributeModifier(IronboundArtefact.prefix("devil_ring"), -0.3 * 1 / multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributeMap.put(AttributeRegistry.HOLY_MAGIC_RESIST, new AttributeModifier(IronboundArtefact.prefix("devil_ring"), -0.3 * 1 / multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return attributeMap;
    }
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (canEntityUseItem(slotContext.entity()) && slotContext.entity().tickCount % 10 == 0){
            var copy = stack.copy();
            copy.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(SpellRegistry.ABYSSAL_SHROUD_SPELL.get().getSpellId(), 10));
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a->a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
        }
    }
}
