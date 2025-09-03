package com.c446.lines_and_particles_lib.shapes;

import io.redspace.ironsspellbooks.api.network.ISerializable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class LineData implements DrawableElement, ISerializable {
    public  Vec3 startPoint;
    public  Vec3 endPoint;
    public  int color;
    public  int drawTimeInTick;
    public  int timeToLive;
    public  int decayTime;
    public  long creationTick;

    public LineData(FriendlyByteBuf buf){
        this.readFromBuffer(buf);
    }

    public LineData(Vec3 pointA, Vec3 pointB, int color, int drawTimeInTick, int timeToLive, int decayTime, Long creationTick) {
        this.startPoint = pointA;
        this.endPoint = pointB;
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
        buf.writeVec3(this.startPoint);
        buf.writeVec3(this.endPoint);
        buf.writeInt(this.color);
        buf.writeInt(this.drawTimeInTick);
        buf.writeInt(this.timeToLive);
        buf.writeInt(this.decayTime);
        buf.writeLong(this.creationTick);
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf buf) {
        this.startPoint = buf.readVec3();
        this.endPoint = buf.readVec3();
        this.color = buf.readInt();
        this.drawTimeInTick = buf.readInt();
        this.timeToLive = buf.readInt();
        this.decayTime = buf.readInt();
        this.creationTick = buf.readLong();
    }
}
