package com.c446.ironbound_artefacts.perks.spellperks.lightning;


import com.c446.ironbound_artefacts.perks.PerkTaskScheduler;
import com.c446.ironbound_artefacts.perks.spellperks.PerkTree;
import com.c446.ironbound_artefacts.perks.spellperks.SpellPerk;
import com.c446.ironbound_artefacts.registries.RegistryEffects;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.LightningStrike;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

import static com.c446.ironbound_artefacts.perks.spellperks.lightning.LightningBoltPerks.*;

@PerkTree(spellId = spellId)
public class LightningBoltPerks {
    public static final String spellId = "irons_spellbooks:lightning_bolt";

    @SpellPerk(perkId = "arctic_discharge")
    public void freezeTargets(SpellDamageEvent event) {
        // Ensure we are only affecting the Lightning Bolt spell
        if (!event.getSpellDamageSource().spell().getSpellId().equals("irons_spellbooks:lightning_bolt")) return;

        LivingEntity target = event.getEntity();
        // 1. Reset I-frames to ensure the 'cold' sets in
        target.invulnerableTime = 0;

        // 2. Apply Vanilla Freezing (Powder Snow effect)
        // 140 ticks is enough to start the freezing damage heart-shaking
        target.setTicksFrozen(target.getTicksFrozen() + 140);

        // 3. Optional: If you have Iron's Spells installed, apply the actual Frozen effect
        // This physically stops the mob from moving.
        // living.addEffect(new MobEffectInstance(MobRegistry.FROZEN.get(), 60, 0));

        // 4. Visuals: Iris-colored "Ice Spark"
        if (target.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SNOWFLAKE,
                    target.getX(), target.getEyeY(), target.getZ(),
                    10, 0.3, 0.3, 0.3, 0.05);
        }
    }

    @SpellPerk(perkId = "lightning_affinity")
    void lightningAffinity(LivingDeathEvent death) {
        if (death.getSource() instanceof SpellDamageSource src){
            if (src.spell().equals(SpellRegistry.LIGHTNING_BOLT_SPELL.get())) {
                if (src.getEntity() instanceof LivingEntity l)  {
                    l.addEffect(new MobEffectInstance(RegistryEffects.LIGHTNING_AFFINITY, 20*30,0));
                }
            }
        }
    }

    @SpellPerk(perkId = "echo_round")
    public void ancientDragonWrath(SpellDamageEvent event) {
        // Only trigger on the Lightning Bolt spell
        if (!event.getSpellDamageSource().spell().equals(SpellRegistry.LIGHTNING_BOLT_SPELL.get())) return;

        Entity caster = event.getSpellDamageSource().getEntity();
        if (!(caster instanceof LivingEntity livingCaster) || !(caster.level() instanceof ServerLevel level)) return;

        // Configuration for the concentric rings (Radius, Bolt Count)
        float[] rings = {4.0f, 8.5f, 13.0f};
        int[] boltsPerRing = {5, 8, 12};
        float baseDamage = event.getAmount();
        Vec3 origin = livingCaster.position();

        for (int i = 0; i < rings.length; i++) {
            final int ringIndex = i;
            // Schedule each wave 10 ticks (0.5s) apart for the "ripple" effect
            PerkTaskScheduler.schedule(i * 10, () -> {
                spawnLightningRing(level, livingCaster, origin, rings[ringIndex], boltsPerRing[ringIndex], baseDamage);
            });
        }
    }

    private void spawnLightningRing(ServerLevel level, LivingEntity caster, Vec3 center, float radius, int count, float damage) {
        for (int j = 0; j < count; j++) {
            double angle = (2 * Math.PI / count) * j + (level.random.nextDouble() * 0.5);
            double x = center.x + radius * Math.cos(angle);
            double z = center.z + radius * Math.sin(angle);

            Vec3 strikePos = Utils.moveToRelativeGroundLevel(level, new Vec3(x, center.y, z), 10);

            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, strikePos.x, strikePos.y + 0.1, strikePos.z,
                    15, 0.4, 0.2, 0.4, 0.1);

            LightningStrike strike = new LightningStrike(level);
            strike.setOwner(caster);

            strike.setDamage(damage * 0.65f);
            strike.setPos(strikePos);

            level.addFreshEntity(strike);
        }
    }

}

