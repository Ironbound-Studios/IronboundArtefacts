package com.c446.lines_and_particles_lib.network.packets;

import com.c446.ironbound_artefacts.IBA;
import com.c446.lines_and_particles_lib.shapes.ArcData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class ArcPacket implements CustomPacketPayload {
    public static final Type<ArcPacket> TYPE = new Type<>(IBA.p("arc_packet"));
    public ArcData arcData;


    public ArcPacket(FriendlyByteBuf buf){
        this.arcData = new ArcData(buf);
    }

    public ArcPacket(ArcData data){
        this.arcData=data;
    }

    public static final StreamCodec<FriendlyByteBuf, ArcPacket> STREAM_CODEC = CustomPacketPayload.codec(ArcPacket::handleBuf, ArcPacket::new);

    public void handleBuf(FriendlyByteBuf buf){
        this.arcData.writeToBuffer(buf);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
