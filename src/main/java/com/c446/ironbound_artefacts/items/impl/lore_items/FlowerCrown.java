package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.google.common.collect.Multimap;
import dev.shadowsoffire.apothic_attributes.api.ALObjects;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class FlowerCrown extends UserDependantCurios {
    public FlowerCrown(Properties p) {
        super(p);
    }

    @Override
    public boolean canEntityUseItem(Entity entity) {
        return true;
    }

    int timeOnGrass = 0;

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity() instanceof Player player) {
            if (player.level().getBlockState(player.getOnPos()).is(BlockTags.DIRT)) {
                timeOnGrass++;
                player.addEffect(new MobEffectInstance(ALObjects.MobEffects.VITALITY, 40, Math.min(timeOnGrass % 200, 2), true, true));
            } else {
                timeOnGrass = 0;
            }
        }
        super.curioTick(slotContext, stack);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var map = super.getAttributeModifiers(slotContext, id, stack);
        map.put(AttributeRegistry.MANA_REGEN, new AttributeModifier(id, 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return map;
    }
}
