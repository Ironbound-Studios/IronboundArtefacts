package com.c446.ironbound_artefacts.items.impl.regular;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.c446.ironbound_artefacts.registries.AttributeRegistry;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class DeathAmulet extends UserDependantCurios {
    public DeathAmulet(Properties p) {
        super(p);
    }

    @Override
    public boolean canEntityUseItem(Entity entity) {
        if (entity instanceof Player player) {
            return (player.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.THEKILLAGER) || entity.getName().getString().equals("Dev"));
        }
        return false;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var attributeModifier = ICurioItem.defaultInstance.getAttributeModifiers(slotContext, id);
        int multiplier = (canEntityUseItem(slotContext.entity())) ? 2 : 1;

        attributeModifier.put(AttributeRegistry.VOID_DAMAGE_ATTRIBUTE, new AttributeModifier(id, multiplier, AttributeModifier.Operation.ADD_VALUE));
        return attributeModifier;
    }
}
