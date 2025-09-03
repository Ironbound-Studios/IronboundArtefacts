package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.entity.spells.ice_spike.IceSpikeEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import top.theillusivec4.curios.api.SlotContext;

import java.util.HashMap;
import java.util.List;

import static org.openjdk.nashorn.internal.objects.NativeMath.max;
import static org.openjdk.nashorn.internal.objects.NativeMath.min;

public class FourBlessing extends UserDependantCurios {
    public FourBlessing(Properties p) {
        super(p);
    }

    public FourBlessing(Properties p, boolean showEnch) {
        super(p, showEnch);
    }

    @Override
    public boolean canEntityUseItem(Entity entity) {
        return false;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var map = super.getAttributeModifiers(slotContext, id, stack);
        var attr = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);

        if (attr != null) {
            attr.modifiers().forEach(mod -> {
                map.put(mod.attribute(), mod.modifier());
            });
        }
        return map;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        var map = new HashMap<Holder<Attribute>, AttributeModifier>();
        var loc = IronboundArtefact.prefix("four_divine_blessing");
        if (slotContext.entity() != null && slotContext.entity() instanceof Player player) {
            var power = player.getAttributeValue(AttributeRegistry.SPELL_POWER);
            List<Holder<Attribute>> attr = List.of(
                    AttributeRegistry.FIRE_SPELL_POWER,
                    AttributeRegistry.ICE_SPELL_POWER,
                    AttributeRegistry.LIGHTNING_SPELL_POWER,
                    AttributeRegistry.NATURE_SPELL_POWER
            );

            attr.forEach(a -> {
                if (player.getAttributeValue(a) < power) {
                    map.put(
                            a, new AttributeModifier(
                                    loc,
                                    min(.5f,Math.max(0, (power - player.getAttributeValue(a)))),
                                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    );
                }
            });
        }

        var attributes = ItemAttributeModifiers.builder();
        map.keySet().forEach(key -> attributes.add(key, map.get(key), EquipmentSlotGroup.ARMOR));
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, attributes.build());
        super.curioTick(slotContext, stack);
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {

    }
}
