package com.c446.lines_and_particles_lib.shapes;

import io.redspace.ironsspellbooks.api.network.ISerializable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class ArcData implements DrawableElement, ISerializable {
    public Vec3 center;
    public Vec3 startPoint;
    public Vec3 endPoint;
    public double radius;
    public int color;
    public int drawTimeInTick;
    public int timeToLive;
    public int decayTime;
    public long creationTick;

    public ArcData(FriendlyByteBuf buf) {
        this.readFromBuffer(buf);
    }

    public ArcData(Vec3 center, Vec3 startPoint, Vec3 endPoint, double radius, int color, int drawTimeInTick, int timeToLive, int decayTime, long creationTick) {
        this.center = center;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.radius = radius;
        this.color = color;
        this.drawTimeInTick = drawTimeInTick;
        this.timeToLive = timeToLive;
        this.decayTime = decayTime;
        this.creationTick = creationTick;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public int getDrawTimeInTick() {
        return drawTimeInTick;
    }

    @Override
    public int getTimeToLive() {
        return timeToLive;
    }

    @Override
    public int getDecayTime() {
        return decayTime;
    }

    @Override
    public long getCreationTick() {
        return creationTick;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeVec3(this.center);
        buf.writeVec3(this.startPoint);
        buf.writeVec3(this.endPoint);
        buf.writeDouble(this.radius);
        buf.writeInt(this.color);
        buf.writeInt(this.drawTimeInTick);
        buf.writeInt(this.timeToLive);
        buf.writeInt(this.decayTime);
        buf.writeLong(this.creationTick);
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf buf) {
        this.center = buf.readVec3();
        this.startPoint = buf.readVec3();
        this.endPoint = buf.readVec3();
        this.radius = buf.readDouble();
        this.color = buf.readInt();
        this.drawTimeInTick = buf.readInt();
        this.timeToLive = buf.readInt();
        this.decayTime = buf.readInt();
        this.creationTick = buf.readLong();
    }
}