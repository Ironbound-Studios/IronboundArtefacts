package com.c446.ironbound_artefacts.items.impl;

import com.c446.ironbound_artefacts.components.TuningForkAttachment;
import com.c446.ironbound_artefacts.components.UniversalPositionComponent;
import com.c446.ironbound_artefacts.registries.ComponentRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TuningRod extends TuningFork{
    public TuningRod(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer instanceof ServerPlayer serverPlayer && pLevel instanceof ServerLevel serverLevel && serverPlayer.isCrouching()) {
            serverPlayer.getItemInHand(pUsedHand).set(ComponentRegistry.UNIVERSAL_POS, new UniversalPositionComponent(pPlayer.getX(),pPlayer.getY(),pPlayer.getZ(),serverLevel.dimension().toString()));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
