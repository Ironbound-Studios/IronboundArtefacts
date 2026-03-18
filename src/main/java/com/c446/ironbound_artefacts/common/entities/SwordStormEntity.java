package com.c446.ironbound_artefacts.common.entities;

import com.c446.ironbound_artefacts.registries.RegistrySpells;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class SwordStormEntity extends Projectile implements AntiMagicSusceptible {

    // Cast Data for the Spell System
    public static class SwordStormCastData implements ICastData {
        public final SwordStormEntity storm;

        public SwordStormCastData(SwordStormEntity storm) {
            this.storm = storm;
        }

        @Override
        public void reset() {
            if (storm != null && !storm.isRemoved()) {
                storm.discard();
            }
        }
    }

    public float damageFlat = 0f;
    public void setDamage(float d) {
        damageFlat=d;
    }
    private static final float STORM_RADIUS = 4.5F;
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(SwordStormEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public SwordStormEntity(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public SwordStormEntity(EntityType<? extends Projectile> type, Level level, LivingEntity owner) {
        this(type, level);
        this.setOwner(owner);
        this.setOwnerUUID(owner.getUUID());
        this.centerOnCaster(owner);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OWNER_UUID, Optional.empty());
    }

    public void centerOnCaster(LivingEntity caster) {
        // Position at the chest/center of the player
        Vec3 pos = caster.position().add(0, caster.getBbHeight() / 2, 0);
        this.setPos(pos.x, pos.y, pos.z);
    }

    @Override
    public void tick() {
        // 1. Resolve Owner
        Entity ownerEntity = getOwner();
        if (!(ownerEntity instanceof LivingEntity owner)) {
            if (!this.level().isClientSide) this.discard();
            return;
        }

        // 2. Follow Owner
        centerOnCaster(owner);

        // 3. Server-Side Damage Logic
        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            // Damage every 4 ticks (0.2s) for a rapid "slashing" feel
            if (this.tickCount % 4 == 0) {
                processStormDamage(serverLevel, owner);

                // Play a subtle "swish" sound periodically
                if (this.tickCount % 12 == 0) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.NEUTRAL, 0.8F, 1.2F + (random.nextFloat() * 0.4F));
                }
            }
        }

        // 4. Initial Sound
        if (this.tickCount == 1) {
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(),
                    SoundEvents.IRON_GOLEM_ATTACK, SoundSource.NEUTRAL, 1.0F, 1.5F, false);
        }

        super.tick();
    }

    private void processStormDamage(ServerLevel level, LivingEntity owner) {
        // Link to the Sword Storm spell for damage source attribution
        var spell = RegistrySpells.HOPE.get();

        AABB hitBox = this.getBoundingBox().inflate(STORM_RADIUS);

        level.getEntities(owner, hitBox, entity -> entity instanceof LivingEntity && canHitEntity(entity))
                .forEach(target -> {
                    LivingEntity livingTarget = (LivingEntity) target;

                    // Apply Irons Spellbooks Damage
                    DamageSources.applyDamage(
                            livingTarget,
                            this.damageFlat,
                            spell.getDamageSource(this, owner)
                    );

                    // Visual feedback: Sparkle/Hit particles at target's eyes
                    level.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT,
                            livingTarget.getX(), livingTarget.getEyeY(), livingTarget.getZ(),
                            3, 0.2, 0.2, 0.2, 0.1);
                });
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity target) {
        Entity owner = getOwner();
        return super.canHitEntity(target) && target != owner && (owner == null || !owner.isAlliedTo(target));
    }

    @Override
    public @Nullable Entity getOwner() {
        return this.entityData.get(OWNER_UUID)
                .map(uuid -> {
                    if (this.level() instanceof ServerLevel serverLevel) {
                        return serverLevel.getEntity(uuid);
                    }
                    return this.level().getPlayerByUUID(uuid);
                })
                .orElse(super.getOwner());
    }

    public void setOwnerUUID(UUID uuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Owner")) setOwnerUUID(tag.getUUID("Owner"));
        if (tag.contains("Damage")) this.damageFlat = tag.getFloat("Damage");
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.entityData.get(OWNER_UUID).ifPresent(uuid -> tag.putUUID("Owner", uuid));
        tag.putFloat("Damage", this.damageFlat);
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.discard();
    }
}