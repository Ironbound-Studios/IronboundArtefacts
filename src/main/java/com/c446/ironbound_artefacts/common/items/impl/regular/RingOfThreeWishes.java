package com.c446.ironbound_artefacts.common.items.impl.regular;

import com.c446.ironbound_artefacts.registries.RegistrySpells;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RingOfThreeWishes extends CurioBaseItem {
    public RingOfThreeWishes(Properties properties) {
        super(properties.durability(3));
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity.tickCount % 20 == 0) {
            pStack.set(ComponentRegistry.SPELL_CONTAINER, new SpellContainer(1, true, true, false, new SpellSlot[]{new SpellSlot(new SpellData(RegistrySpells.WISH.get(), 5, true), 0),}));
        }
        if (pStack.getDamageValue() >= 3) {
            pStack.setCount(pStack.getCount() - 1);
        }

        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer instanceof ServerPlayer serverPlayer) {
            RegistrySpells.WISH.get().castSpell(pLevel, 5, serverPlayer, CastSource.SCROLL, false);
            setDamage(pPlayer.getItemInHand(pUsedHand), this.getDamage(pPlayer.getItemInHand(pUsedHand)) + 1);

        }
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
