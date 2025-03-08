package com.c446.ironbound_artefacts.components;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public record UniversalPositionComponent(double x, double y, double z, String dimension) {


    public static UniversalPositionComponent create(LivingEntity player) {
        return new UniversalPositionComponent(player.getX(), player.getY(), player.getZ(), player.level().dimension().toString());
    }

    public Vec3 getPos() {
        return new Vec3(x, y, z);
    }

    public ServerLevel getServer(Player player) {
        var levels = Objects.requireNonNull(player.level().getServer()).getAllLevels();
        if (levels != null) {
            for (ServerLevel level : levels) {
                if (level.getLevel().dimension().toString().equals(dimension)) {
                    return level;
                }
            }

        }
        return null;
    }

    public void goTo(Player player) {
        player.canChangeDimensions(player.level(), this.getServer(player));
        player.setPos(this.getPos());
    }
}
