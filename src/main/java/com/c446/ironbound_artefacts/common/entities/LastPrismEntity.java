package com.c446.ironbound_artefacts.common.entities;

import com.c446.ironbound_artefacts.client.entity_renderers.LastPrismRenderer;
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LastPrismEntity extends Projectile implements AntiMagicSusceptible {
    public static class LastPrismCastData implements ICastData {
        public final LastPrismEntity prism;

        public LastPrismCastData(LastPrismEntity prism) {
            this.prism = prism;
        }

        @Override
        public void reset() {
            if (prism != null && !prism.isRemoved()) {
                prism.discard();
            }
        }

        private boolean casting;

        void setIsCasting(boolean val) {
            this.casting = val;
        }

        boolean isCasting() {
            return casting;
        }
    }

    // Increased visibility or use a setter if needed for your spell class
    public float damageFlat = 0f;

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(LastPrismEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public LastPrismEntity(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OWNER_UUID, Optional.empty());
    }

    /**
     * Aligns the entity position and rotation with the caster.
     * Works for any LivingEntity (Players, Mobs, etc.)
     */
    public void centerOnCaster(LivingEntity caster) {
        // Use the exact eye position so the beam originates from the face
        Vec3 eyePos = caster.getEyePosition();
        this.setPos(eyePos.x, eyePos.y - .125F, eyePos.z);
        this.setXRot(caster.getXRot());
        this.setYRot(caster.getYRot());
    }

    @Override
    public void tick() {
        Entity entity = getOwner(); // Or LivingEntity owner if using my previous suggestion
        if (!(entity instanceof LivingEntity owner)) return;

        centerOnCaster(owner);
        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
            processBeamDamage(serverLevel, owner);
        }

        // REMOVE OR COMMENT OUT THIS LINE:
        // if (!this.level().isClientSide && this.tickCount > 100) this.discard();

        // Keep this check so if the caster somehow dies/logs out without clearing cast data, the beam dies.
        if (!this.level().isClientSide && owner == null) {
            this.discard();
        }

        super.tick();

        // Logic works for any LivingEntity now
        if (getOwner() instanceof LivingEntity livingOwner) {
            centerOnCaster(livingOwner);

            if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
                processBeamDamage(serverLevel, livingOwner);
            }
        } else if (!this.level().isClientSide) {
            // If owner is logged out or dead/removed, discard the beam
            this.discard();
        }

        if (!this.level().isClientSide && this.tickCount > 100) {
            this.discard();
        }

        if (this.tickCount == 1) {
            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(),
                    SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.NEUTRAL, 1.0F, 1.2F, false);
        }
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity target) {
        Entity owner = getOwner();
        // Generic ally check: works for mobs on teams or tamed animals
        return super.canHitEntity(target) && target != owner && (owner == null || !owner.isAlliedTo(target));
    }

    private void processBeamDamage(ServerLevel serverLevel, LivingEntity owner) {
        var spellHolder = RegistrySpells.LAST_PRISM_SPELL.get();

        // Get all entities currently intersecting the rays
        List<Entity> targets = getTouchingEntities(serverLevel, owner);

        for (Entity target : targets) {
            if (target instanceof LivingEntity livingTarget && canHitEntity(target)) {

                // Scaling Logic
                float progress = Mth.clamp((float) this.tickCount / LastPrismRenderer.CHARGE_TICKS, 0.0F, 1.0F);
                float focusMultiplier = (float) (1.0F + Math.pow(progress, 2));
                float finalDamage = this.damageFlat * focusMultiplier;

                // Apply Damage
                DamageSources.applyDamage(
                        livingTarget,
                        finalDamage,
                        spellHolder.getDamageSource(this, owner)
                );

                if (livingTarget.isOnFire()) {
                    livingTarget.setRemainingFireTicks(livingTarget.getRemainingFireTicks() + 1);
                }
            }
        }
    }

    public float getBeamLength(Vec3 start, Vec3 direction, float maxDist) {
        Vec3 end = start.add(direction.scale(maxDist));
        BlockHitResult result = this.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        return result.getType() != HitResult.Type.MISS ? (float) result.getLocation().distanceTo(start) : maxDist;
    }

    public List<Entity> getTouchingEntities(ServerLevel level, LivingEntity owner) {
        List<Entity> hitEntities = new ArrayList<>();

        // 1. MATCH RENDERER MATH (Quadratic)
        float time = (float) this.tickCount;
        float progress = Mth.clamp(time / LastPrismRenderer.CHARGE_TICKS, 0.0F, 1.0F);
        float quadProgress = progress * progress;
        float currentSpread = LastPrismRenderer.MAX_SPREAD * (1.0F - quadProgress);
        float rotationOffset = (time * time) * (LastPrismRenderer.ROTATION_SPEED * 0.05F);

        Vec3 start = owner.getEyePosition();
        Vec3 lookVec = owner.getLookAngle();
        float maxLen = LastPrismRenderer.BEAM_LENGTH;
        float padding = 1.5F; // 1.5 Blocks

        // 2. GET CANDIDATES
        // Grab all entities in a large AABB around the beam's reach to avoid per-ray overhead
        AABB searchBox = new AABB(start, start.add(lookVec.scale(maxLen))).inflate(padding + currentSpread);
        List<Entity> candidates = level.getEntities(owner, searchBox, EntitySelector.NO_SPECTATORS);

        for (Entity entity : candidates) {
            Vec3 entityPos = entity.getBoundingBox().getCenter();
            Vec3 relPos = entityPos.subtract(start);

            // Project entity onto the look vector to find distance along the beam
            double distAlongBeam = relPos.dot(lookVec);
            if (distAlongBeam < 0 || distAlongBeam > maxLen) continue;

            // Find the point on the center-line and the distance from it
            Vec3 projectOnAxis = lookVec.scale(distAlongBeam);
            double distFromAxis = relPos.subtract(projectOnAxis).length();

            // The "Target Radius" at this specific distance along the beam
            // Since spread is an offset at 1.0 units forward, radius = dist * spread
            double currentBeamRadius = distAlongBeam * (currentSpread / 1.0F);

            boolean isHit = false;

            // 3. PHASE LOGIC
            if (progress > 0.7F) {
                // FULL CONE: Anything inside the radius + padding
                if (distFromAxis <= currentBeamRadius + padding) {
                    isHit = true;
                }
            }
            else if (progress > 0.3F) {
                // EMPTY CONE (RING): Anything near the perimeter of the circle
                double distToRingEdge = Math.abs(distFromAxis - currentBeamRadius);
                if (distToRingEdge <= padding) {
                    isHit = true;
                }
            }
            else {
                // INDIVIDUAL RAYS: Check distance to each of the 7 discrete lines
                for (int i = 0; i < LastPrismRenderer.BEAM_COUNT; i++) {
                    float angle = (i * ((float) Math.PI * 2F) / LastPrismRenderer.BEAM_COUNT) + rotationOffset;

                    // Direction of this specific ray
                    Vec3 rayDir = calculateRayDirection(lookVec, Mth.cos(angle) * currentSpread, Mth.sin(angle) * currentSpread, 1.0F);

                    // Distance from entity to this specific ray line
                    double d = relPos.subtract(rayDir.scale(relPos.dot(rayDir))).length();
                    if (d <= padding) {
                        isHit = true;
                        break;
                    }
                }
            }

            if (isHit) {
                // Final check: Is there a solid block between the eye and the entity?
                BlockHitResult blockCheck = level.clip(new ClipContext(start, entityPos, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, owner));
                if (blockCheck.getType() == HitResult.Type.MISS || blockCheck.getLocation().distanceTo(start) > entityPos.distanceTo(start)) {
                    hitEntities.add(entity);
                }
            }
        }

        return hitEntities;
    }

    public Vec3 calculateRayDirection(Vec3 lookVec, float localX, float localY, float length) {
        Vec3 right = lookVec.cross(new Vec3(0, 1, 0)).normalize();
        if (right.lengthSqr() < 0.001) right = new Vec3(1, 0, 0);
        Vec3 trueUp = right.cross(lookVec).normalize();
        return lookVec.add(right.scale(localX)).add(trueUp.scale(localY)).normalize().scale(length);
    }

    @Override
    public @Nullable Entity getOwner() {
        // Use the built-in level entity lookup which is more flexible than getPlayerByUUID
        return this.entityData.get(OWNER_UUID)
                .map(uuid -> {
                    if (this.level() instanceof ServerLevel serverLevel) {
                        return serverLevel.getEntity(uuid);
                    }
                    return null;
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
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.entityData.get(OWNER_UUID).ifPresent(uuid -> tag.putUUID("Owner", uuid));
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.discard(); // Prism should probably dissipate if hit with anti-magic
    }

    public UUID getOwnerUUID() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }
}