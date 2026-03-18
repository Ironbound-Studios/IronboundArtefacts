package com.c446.ironbound_artefacts.perks.spellperks.fire;

import com.c446.ironbound_artefacts.perks.spellperks.PerkTree;
import com.c446.ironbound_artefacts.perks.spellperks.SpellPerk;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.spells.fire.FireballSpell;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;

import javax.swing.text.html.parser.Entity;
import java.util.function.Supplier;

@PerkTree(spellId = "irons_spellbooks:fireball")
public class FireballPerks {
    public static final String spellId = "irons_spellbooks:fireball";
    public static final Supplier<AbstractSpell> spell = SpellRegistry.FIREBALL_SPELL;

    @SpellPerk(perkId = "volatile_residue")
    public void volatileResidue(SpellDamageEvent event) {
        if (!event.getSpellDamageSource().spell().equals(spell.get())) return;

        if (event.getEntity() instanceof LivingEntity target) {
            int duration = 100; // 5 seconds
            int currentAmplifier = 0;

            if (target.hasEffect(MobEffectRegistry.REND)) {
                var active = target.getEffect(MobEffectRegistry.REND);
                if (active != null) {
                    currentAmplifier = Math.min(active.getAmplifier() + 1, 4); // Cap at 5 stacks
                }
            }

            target.addEffect(new MobEffectInstance(MobEffectRegistry.REND, duration, currentAmplifier));

            int fireTicks = Math.min((duration / 2) + (currentAmplifier * 20), 200);
            target.setRemainingFireTicks(fireTicks);

            if (target.level() instanceof ServerLevel level) {
                // Using the exact particle logic from your snippet
                MagicManager.spawnParticles(level, ParticleHelper.EMBERS,
                        target.getX(), target.getY() + target.getBbHeight() * .5f, target.getZ(),
                        10 + (currentAmplifier * 10), // More embers per stack
                        target.getBbWidth() * .5f, target.getBbHeight() * .5f, target.getBbWidth() * .5f,
                        .03, false);
            }
        }
    }}