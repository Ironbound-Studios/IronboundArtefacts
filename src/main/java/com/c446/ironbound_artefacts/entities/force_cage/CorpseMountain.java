package com.c446.ironbound_artefacts.entities.force_cage;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.effects.CorpseMountainTargettedEffect;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import io.redspace.ironsspellbooks.api.events.CounterSpellEvent;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@EventBusSubscriber
public class CorpseMountain extends AoeEntity {
    public static abstract class CylinderParticleManager {
        private static final Random RANDOM = new Random();


        public CylinderParticleManager() {
        }

        public static void staticCircle(Level level, Entity entity, int particleCount, ParticleOptions particleType, double parameter) {
            if (!level.isClientSide) {
                double centerY = entity.getY() + (double) entity.getBbHeight() * 0.5;

                for (int i = 0; i < particleCount; ++i) {
                    double theta = 6.283185307179586 * RANDOM.nextDouble();
                    double phi = Math.acos(2.0 * RANDOM.nextDouble() - 1.0);
                    double xOffset = Math.sin(phi) * Math.cos(theta);
                    double yOffset = Math.sin(phi) * Math.sin(theta);
                    double zOffset = Math.cos(phi);
                    Vec3 directionVector;
                    xOffset *= parameter;
                    yOffset *= parameter;
                    zOffset *= parameter;
                    directionVector = Vec3.ZERO;
                    MagicManager.spawnParticles(level, particleType, entity.getX() + xOffset, centerY + yOffset, entity.getZ() + zOffset, 0, directionVector.x, directionVector.y, directionVector.z, 0.1, true);
                }
            }
        }

    }
    private int lifeTime = 12000;
    private int delay = 0;
    private int killCount = 0;
    private int noKillTimer = 0;

    private int debuffStack = 0;

    public CorpseMountain(EntityType<CorpseMountain> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setOwner(this);
    }

    public CorpseMountain(EntityType<? extends Projectile> pEntityType, Level pLevel, LivingEntity owner) {
        super(pEntityType, pLevel);
        this.setOwner(owner);
    }

    public float getRadius() {
        return 75;
    }

    public void tryIncrement(Entity tar) {
        if (this.delay != 0) {
            this.noKillTimer = 0;
            this.killCount++;
        }
    }

    @Override
    public int getDelay() {
        return this.lifeTime;
    }

    @Override
    public void tick() {
        super.tick();
        CylinderParticleManager.staticCircle(this.level(), this, 150, ParticleRegistry.BLOOD_PARTICLE.get(), 75);
        this.lifeTime--;
        this.delay--;
        this.noKillTimer++;

        if (this.lifeTime == 0) {
            this.discard();
        }

        if (this.tickCount % 20 == 0) {
            if (this.noKillTimer >= 300) {
                this.debuffStack++;
                this.noKillTimer = 0;
            }
            // Apply debuff based on debuffStack to the owner
            if (this.getOwner() != null && this.getOwner() instanceof LivingEntity l) {
                // Remove existing HP_DOWN effect to reapply with updated amplifier
                if (l.hasEffect(EffectsRegistry.HP_DOWN)) {
                    l.removeEffect(EffectsRegistry.HP_DOWN);
                }
                if (this.debuffStack > 0) {
                    l.addEffect(new MobEffectInstance(EffectsRegistry.HP_DOWN, 60, this.debuffStack - 1, false, false));
                }
            }
            if (this.getOwner() != null && this.getOwner() instanceof LivingEntity l) {
                if (l.hasEffect(EffectsRegistry.CORPSE_MOUNTAIN_BUFF)) {
                    l.removeEffect(EffectsRegistry.CORPSE_MOUNTAIN_BUFF);
                }
                int bloodSpellAmplifier = Math.min(this.killCount, 30);
                if (bloodSpellAmplifier > 0) {
                    l.addEffect(new MobEffectInstance(EffectsRegistry.CORPSE_MOUNTAIN_BUFF, 60, bloodSpellAmplifier - 1, false, false));
                }
            }

            var boundingBox = this.getBoundingBox().inflate(this.getRadius());
            level().getEntities(this, boundingBox, a -> a instanceof LivingEntity && !a.isSpectator()).forEach(entity -> {
                if (entity instanceof LivingEntity living) {
                    if (!living.hasEffect(EffectsRegistry.FIRE_HOLY_DEF_DOWN)) {
                        living.addEffect(new MobEffectInstance(EffectsRegistry.FIRE_HOLY_DEF_DOWN, 60, 2, false, false)); // Duration 3 seconds (60 ticks), amplifier 0
                    }

                    if (living.hasEffect(EffectsRegistry.INSIDE_CORPSE_MOUNTAIN) && living.getEffect(EffectsRegistry.INSIDE_CORPSE_MOUNTAIN) instanceof CorpseMountainTargettedEffect e) {
                        if (!e.getMountains().contains(this)) {
                            e.addMountain(this);
                        }
                    } else {
                        living.addEffect(new CorpseMountainTargettedEffect(EffectsRegistry.INSIDE_CORPSE_MOUNTAIN, 60).addMountain(this));
                    }
                }
            });
        }
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return false;
    }

    @Override
    public void applyEffect(LivingEntity livingEntity) {
    }

    @Override
    public float getParticleCount() {
        return 150;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        CylinderParticleManager.staticCircle(level(), this, 300, ParticleRegistry.BLOOD_PARTICLE.get(), 75d);
        return Optional.empty();
    }


    @SubscribeEvent
    public static void onCorpseMountainKill(@NotNull LivingDeathEvent event) {
        IronboundArtefact.LOGGER.debug("CorpseMountain kill event triggered for entity: {}", event.getEntity());

        if (event.getEntity() instanceof LivingEntity l && l.hasEffect(EffectsRegistry.INSIDE_CORPSE_MOUNTAIN) && l.getEffect(EffectsRegistry.INSIDE_CORPSE_MOUNTAIN) instanceof CorpseMountainTargettedEffect c) {
            IronboundArtefact.LOGGER.debug("Entity has Corpse Mountain effect, processing mountains...");

            c.getMountains().forEach(m -> {
                // Check if the killed entity is a summon and if its summoner is the owner of the Corpse Mountain
                if (event.getEntity() instanceof IMagicSummon) {
                    IronboundArtefact.LOGGER.debug("Entity is a magic summon, checking ownership...");

                    if (event.getSource().getEntity() != null && SummonManager.getOwner(l) instanceof Player owner) {
                        IronboundArtefact.LOGGER.debug("Summon owner: {}, Corpse Mountain owner: {}", owner, m.getOwner());

                        if (owner.equals(m.getOwner()) && !event.getSource().getEntity().equals(m.getOwner())){
                            IronboundArtefact.LOGGER.debug("Conditions met, incrementing Corpse Mountain counter");
                            m.tryIncrement(event.getEntity());
                        }
                    }
                } else {
                    IronboundArtefact.LOGGER.debug("Entity is not a summon, incrementing Corpse Mountain counter and reducing debuff stack");
                    m.tryIncrement(event.getEntity());
                    m.debuffStack -= 1; // Decrease debuff stack on kill
                    IronboundArtefact.LOGGER.debug("New debuff stack value: {}", m.debuffStack);
                }
            });
        } else {
            IronboundArtefact.LOGGER.debug("Entity does not meet Corpse Mountain effect conditions");
        }
    }

    @SubscribeEvent
    public static void onCounterspellSummon(@NotNull CounterSpellEvent event) {
        IronboundArtefact.LOGGER.debug("Counterspell event triggered for target: {}", event.target);

        if (event.target instanceof IMagicSummon && event.target instanceof LivingEntity l) {
            IronboundArtefact.LOGGER.debug("Target is a magic summon, checking conditions...");

            if (!Objects.equals(SummonManager.getOwner(l), event.caster) && event.caster.level() instanceof ServerLevel serverLevel) {
                IronboundArtefact.LOGGER.debug("Caster is not the summon owner and level is ServerLevel");

                if (l.hasEffect(EffectsRegistry.INSIDE_CORPSE_MOUNTAIN) && l.getEffect(EffectsRegistry.INSIDE_CORPSE_MOUNTAIN) instanceof CorpseMountainTargettedEffect e) {
                    IronboundArtefact.LOGGER.debug("Summon has Corpse Mountain effect, processing mountains...");
                    e.getMountains().forEach(m -> {
                        IronboundArtefact.LOGGER.debug("Incrementing Corpse Mountain counter for summon kill");
                        m.tryIncrement(l);
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSummonSummoned(@NotNull SpellSummonEvent<? extends IMagicSummon> event) {
        IronboundArtefact.LOGGER.debug("Summon summoned event triggered for creature: {}", event.getCreature());

        if (event.getCreature().getSummoner() instanceof LivingEntity l) {
            IronboundArtefact.LOGGER.debug("Summoner is living entity, checking for Corpse Mountain buff...");

            if (l.hasEffect(EffectsRegistry.CORPSE_MOUNTAIN_BUFF) && l.getEffect(EffectsRegistry.CORPSE_MOUNTAIN_BUFF) != null) {
                int amp = Objects.requireNonNull(l.getEffect(EffectsRegistry.CORPSE_MOUNTAIN_BUFF)).getAmplifier();
                IronboundArtefact.LOGGER.debug("Corpse Mountain buff found with amplifier: {}, applying health modifier", amp);

                Objects.requireNonNull(event.getCreature().getAttributes().getInstance(Attributes.MAX_HEALTH))
                        .addOrReplacePermanentModifier(new AttributeModifier(
                                IronboundArtefact.prefix("corpse_mountain_buff_summon"),
                                amp * 0.05 + 1,
                                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        ));
                IronboundArtefact.LOGGER.debug("Health modifier applied to summon");
            }
        }
    }
}