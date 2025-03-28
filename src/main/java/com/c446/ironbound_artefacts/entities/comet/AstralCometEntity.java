package com.c446.ironbound_artefacts.entities.comet;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.fireball.MagicFireball;
import io.redspace.ironsspellbooks.network.particles.FieryExplosionParticlesPacket;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

public class AstralCometEntity extends MagicFireball {
    //private final EntityDataAccessor<Float> SIZE_FACTOR = SynchedEntityData.defineId(AstralCometEntity.class, EntityDataSerializers.FLOAT);

    public AstralCometEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
//        this.getEntityData().set(this.SIZE_FACTOR, 1f);
    }

    /*
        public float getSIZE_FACTOR() {
            return this.getEntityData().get(SIZE_FACTOR);
        }

        public void setSIZE_FACTOR(Float size) {
            this.getEntityData().set(SIZE_FACTOR, size);
        }
    */
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        //pBuilder.define(SIZE_FACTOR, 1f);
        //super.defineSynchedData(pBuilder);
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = getDeltaMovement();
        double d0 = this.getX() - vec3.x;
        double d1 = this.getY() - vec3.y;
        double d2 = this.getZ() - vec3.z;
        var count = Mth.clamp((int) (vec3.lengthSqr() * 4), 1, 4);
        for (int i = 0; i < count; i++) {
            Vec3 random = Utils.getRandomVec3(.25);
            var f = i / ((float) count);
            var x = Mth.lerp(f, d0, this.getX());
            var y = Mth.lerp(f, d1, this.getY());
            var z = Mth.lerp(f, d2, this.getZ());
            this.level().addParticle(ParticleTypes.ENCHANTED_HIT, x - random.x, y + 0.5D - random.y, z - random.z, random.x * .5f, random.y * .5f, random.z * .5f);
            this.level().addParticle(ParticleHelper.UNSTABLE_ENDER, x - random.x, y + 0.5D - random.y, z - random.z, random.x * .5f, random.y * .5f, random.z * .5f);
        }
    }

    @Override
    public float getSpeed() {
        return 0;
    }

    @Override
    public Optional<Holder<SoundEvent>> getImpactSound() {
        return Optional.of(SoundEvents.GENERIC_EXPLODE);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!this.level().isClientSide) {
            impactParticles(xOld, yOld, zOld);
            float explosionRadius = getExplosionRadius();
            var explosionRadiusSqr = explosionRadius * explosionRadius;
            var entities = level().getEntities(this, this.getBoundingBox().inflate(explosionRadius));
            Vec3 losPoint = Utils.raycastForBlock(level(), this.position(), this.position().add(0, 2, 0), ClipContext.Fluid.NONE).getLocation();
            for (Entity entity : entities) {
                double distanceSqr = entity.distanceToSqr(hitResult.getLocation());
                if (distanceSqr < explosionRadiusSqr && canHitEntity(entity) && Utils.hasLineOfSight(level(), losPoint, entity.getBoundingBox().getCenter(), true)) {
                    double p = (1 - distanceSqr / explosionRadiusSqr);
                    float damage = (float) (this.damage * p);
                    DamageSources.applyDamage(entity, damage, SpellRegistry.FIREBALL_SPELL.get().getDamageSource(this, getOwner()));
                }
            }
            if (ServerConfigs.SPELL_GREIFING.get()) {
                Explosion explosion = new Explosion(
                        level(),
                        null,
                        SpellRegistry.FIREBALL_SPELL.get().getDamageSource(this, getOwner()),
                        null,
                        this.getX(), this.getY(), this.getZ(),
                        this.getExplosionRadius() / 2,
                        true,
                        Explosion.BlockInteraction.DESTROY,
                        ParticleTypes.EXPLOSION,
                        ParticleTypes.EXPLOSION_EMITTER,
                        SoundEvents.GENERIC_EXPLODE);
                if (!NeoForge.EVENT_BUS.post(new ExplosionEvent.Start(level(), explosion)).isCanceled()) {
                    explosion.explode();
                    explosion.finalizeExplosion(false);
                }
            }
            PacketDistributor.sendToPlayersTrackingEntity(this, new FieryExplosionParticlesPacket(new Vec3(getX(), getY() + .15f, getZ()), getExplosionRadius()));
            playSound(SoundEvents.GENERIC_EXPLODE.value(), 4.0F, (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F);
            this.discard();
        }
    }
}
