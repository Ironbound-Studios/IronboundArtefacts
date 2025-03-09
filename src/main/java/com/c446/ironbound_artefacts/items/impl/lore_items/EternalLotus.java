package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.items.UserDependantCurios;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

public class EternalLotus extends Item {
    public EternalLotus(Properties p) {
        super(p);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof Player player){
            CuriosApi.getCuriosInventory(pLivingEntity).ifPresent(i->{
                i.addPermanentSlotModifier("ring", IronboundArtefact.prefix("extra_ring_slot"), 1f, AttributeModifier.Operation.ADD_VALUE);
            });
        }
        pStack.setCount(pStack.getCount()-1);
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }


}