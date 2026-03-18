package com.c446.ironbound_artefacts.client;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.registries.RegistryEffects;
import net.acetheeldritchking.aces_spell_utils.network.AddShaderEffectPacket;
import net.acetheeldritchking.aces_spell_utils.network.RemoveShaderEffectPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@EventBusSubscriber(value = Dist.DEDICATED_SERVER)
public class TimeStopHandler {
    public static List<LivingEntity> TIME_STOPPER = new ArrayList<>();

    @SubscribeEvent
    public static void onTick(ServerTickEvent.Post event){
        TIME_STOPPER.forEach(ts -> {
            //pmo
        });

        TIME_STOPPER.clear();
    }

    public static  void addStopped(ServerPlayer sp) {
        PacketDistributor.sendToPlayer(sp, new AddShaderEffectPacket(IBA.MODID, "entity_impact"));
    }

    public static void remStopped(ServerPlayer sp) {
        PacketDistributor.sendToPlayer(sp, new RemoveShaderEffectPacket());
    }
}