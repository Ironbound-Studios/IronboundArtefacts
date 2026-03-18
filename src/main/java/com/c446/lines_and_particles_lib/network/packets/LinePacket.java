package com.c446.lines_and_particles_lib.network.packets;

import com.c446.ironbound_artefacts.IBA;
import com.c446.lines_and_particles_lib.shapes.LineData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class LinePacket implements CustomPacketPayload {
    public LineData lineData;
    public LinePacket(FriendlyByteBuf buf){
        this.lineData = new LineData(buf);
    }

    public LinePacket(LineData data){
        this.lineData=data;
    }


    public static final CustomPacketPayload.Type<LinePacket> TYPE = new CustomPacketPayload.Type<>(IBA.p("line_packet"));

    public static final StreamCodec<FriendlyByteBuf, LinePacket> STREAM_CODEC = CustomPacketPayload.codec(LinePacket::handleBuf, LinePacket::new);

    public void handleBuf(FriendlyByteBuf buf){
        this.lineData.writeToBuffer(buf);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type () {
        return TYPE;
    }
}