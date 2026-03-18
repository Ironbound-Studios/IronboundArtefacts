package com.c446.ironbound_artefacts.common.items.impl.regular;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.common.items.UserDependantCurios;
import com.c446.ironbound_artefacts.registries.RegistryAttributes;
import com.google.common.collect.Multimap;
import dev.shadowsoffire.apothic_attributes.api.ALObjects;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class DeathAmulet extends UserDependantCurios {
    public DeathAmulet(Properties p) {
        super(p);
    }

    @Override
    public boolean canEntityUseItem(Entity entity) {
        if (entity instanceof Player player) {
            return (player.getStringUUID().equals(IBA.ContributorUUIDS.THEKILLAGER) || entity.getName().getString().equals("Dev"));
        }
        return false;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var attributeModifier = ICurioItem.defaultInstance.getAttributeModifiers(slotContext, id);
        int multiplier = (canEntityUseItem(slotContext.entity())) ? 2 : 1;

        var dat = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);

        if (dat != null) {
            dat.modifiers().forEach(s-> {
                attributeModifier.put(s.attribute(), s.modifier());
            });
        }

        attributeModifier.put(RegistryAttributes.VOID_DAMAGE_ATTRIBUTE, new AttributeModifier(id, multiplier, AttributeModifier.Operation.ADD_VALUE));
        return attributeModifier;
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        itemStack.set(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(List.of(
                new ItemAttributeModifiers.Entry(
                        ALObjects.Attributes.ARMOR_PIERCE.getDelegate(),
                        new AttributeModifier(IBA.p("flower_base_hp"), 0.1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.ANY)
        ), true));
    }
}
