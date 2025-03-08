package com.c446.ironbound_artefacts.entities;

import com.c446.ironbound_artefacts.registries.IBEntitiesReg;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.magic_missile.MagicMissileProjectile;
import mezz.jei.api.constants.Tags;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class AbstractHomingEntity extends AbstractMagicProjectile {
    public LivingEntity target;
    double distancePercentage = 0.2;
    private float rayTraceDistance = 50;


    @Override
    public void trailParticles() {

    }

    @Override
    public void impactParticles(double v, double v1, double v2) {

    }

    public float getSpeed() {
        // Example: Speed increases as the missile gets closer to the target
        double distance = this.distanceTo(target);
        return (float) Math.max(0.1, 1.0 / distance); // Ensure speed doesn't become too large
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.empty();
    }

    public AbstractHomingEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

//    public AbstractHomingEntity(Level level, LivingEntity owner, float damage) {
//        this(IBEntitiesReg.HOMING.get(), level);
//        this.setOwner(owner);
//        this.setDamage(damage);
//    }

    public void home() {
        if (this.target == null){
            return;
        }
        Vec3 targetPos = target.getEyePosition();

        double dX = targetPos.x - this.getX();
        double dY = targetPos.y - this.getY();
        double dZ = targetPos.z - this.getZ();

        double horizontalDistance = Math.sqrt(dX * dX + dZ * dZ);

        double yaw = Math.atan2(dZ, dX);
        double yawDegrees = Math.toDegrees(yaw) - 90; // Adjust for Minecraft's coordinate system

        double pitch = Math.atan2(horizontalDistance, dY);
        double pitchDegrees = Math.toDegrees(pitch) - 90; // Adjust for Minecraft's coordinate system

        this.setYRot((float) yawDegrees);
        this.setXRot((float) pitchDegrees);

    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    void updateTarget() {
        var entity = this.getOwner();
        Vec3 start = entity.getEyePosition();
        Vec3 end = entity.getLookAngle().normalize().scale(this.getRayTraceDistance()).add(start);
        var result = Utils.raycastForEntityOfClass(this.level(), entity, start, end, true, LivingEntity.class);
        if (result.getType().equals(HitResult.Type.ENTITY) && result instanceof EntityHitResult hitResult && hitResult.getEntity() instanceof LivingEntity living) {
            this.target = living;
        }
    }

    @Override
    public void tick() {
        if (this.getOwner() == null) {discard();}
        this.home();
        super.tick();
    }


    public float getRayTraceDistance() {
        return rayTraceDistance;
    }

    public void setRayTraceDistance(float rayTraceDistance) {
        this.rayTraceDistance = rayTraceDistance;
    }
}
