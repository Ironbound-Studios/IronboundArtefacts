package com.c446.lines_and_particles_lib;


import com.c446.lines_and_particles_lib.network.packets.ArcPacket;
import com.c446.lines_and_particles_lib.network.packets.LinePacket;
import com.c446.lines_and_particles_lib.shapes.ArcData;
import com.c446.lines_and_particles_lib.shapes.LineData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.network.PacketDistributor;

public class Drawer {

    public static void drawLine(ServerLevel level, Vec3 pointA, Vec3 pointB, int color, int drawTimeInTick, int timeToLive, int decayTime) {
        long creationTick = level.getGameTime();
        LineData lineData = new LineData(pointA, pointB, color, drawTimeInTick, timeToLive, decayTime, creationTick);
        PacketDistributor.sendToAllPlayers(new LinePacket(lineData));
    }

    public static void drawArc(ServerLevel level, Vec3 center, Vec3 startPoint, Vec3 endPoint, double radius, int color, int drawTimeInTick, int timeToLive, int decayTime) {
        long creationTick = level.getGameTime();
        ArcData arcData = new ArcData(center, startPoint, endPoint, radius, color, drawTimeInTick, timeToLive, decayTime, creationTick);
        PacketDistributor.sendToAllPlayers(new ArcPacket(arcData));
    }

    public static void drawLineToPlayer(ServerPlayer player, Vec3 pointA, Vec3 pointB, int color, int drawTimeInTick, int timeToLive, int decayTime) {
        long creationTick = player.level().getGameTime();
        LineData lineData = new LineData(pointA, pointB, color, drawTimeInTick, timeToLive, decayTime, creationTick);
        PacketDistributor.sendToPlayer(player, new LinePacket(lineData));
    }

    public static void drawArcToPlayer(ServerPlayer player, Vec3 center, Vec3 startPoint, Vec3 endPoint, double radius, int color, int drawTimeInTick, int timeToLive, int decayTime) {
        long creationTick = player.level().getGameTime();
        ArcData arcData = new ArcData(center, startPoint, endPoint, radius, color, drawTimeInTick, timeToLive, decayTime, creationTick);
        PacketDistributor.sendToPlayer(player, new ArcPacket(arcData));
    }
}