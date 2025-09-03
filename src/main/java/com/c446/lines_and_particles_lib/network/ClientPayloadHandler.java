package com.c446.lines_and_particles_lib.network;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.lines_and_particles_lib.ClientLineRenderer;
import com.c446.lines_and_particles_lib.network.packets.ArcPacket;
import com.c446.lines_and_particles_lib.network.packets.LinePacket;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import javax.sound.sampled.Line;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = IronboundArtefact.MODID)
public class ClientPayloadHandler {
    @SubscribeEvent
    public static void registerClient(net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent  event) {
        final var r = event.registrar(IronboundArtefact.MODID).versioned("1.0.0").optional();
        r.playToClient(ArcPacket.TYPE, ArcPacket.STREAM_CODEC, ClientPayloadHandler::handleArcPacket);
        r.playToClient(LinePacket.TYPE, LinePacket.STREAM_CODEC, ClientPayloadHandler::handleLinePacket);
        System.out.println("registering IBA packets");
    }

    public static void handleLinePacket(final LinePacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientLineRenderer.addDrawable(packet.lineData);
        });
    }

    public static void handleArcPacket(final ArcPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientLineRenderer.addDrawable(packet.arcData);
        });
    }
}