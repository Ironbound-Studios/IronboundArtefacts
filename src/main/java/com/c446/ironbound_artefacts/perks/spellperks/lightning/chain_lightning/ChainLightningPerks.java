package com.c446.ironbound_artefacts.perks.spellperks.lightning.chain_lightning;


import com.c446.ironbound_artefacts.perks.spellperks.PerkTree;
import com.c446.ironbound_artefacts.perks.spellperks.SpellPerk;
import com.c446.ironbound_artefacts.perks.spellperks.lightning.chain_lightning.static_overload.StaticOverloadHandler;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import static com.c446.ironbound_artefacts.perks.spellperks.lightning.LightningBoltPerks.spellId;
import static com.c446.ironbound_artefacts.perks.spellperks.lightning.chain_lightning.static_overload.StaticOverloadHandler.PENDING_AFTERSHOCKS;

@PerkTree(spellId = spellId)
public class ChainLightningPerks {
    public static final String spellId = "irons_spellbooks:chain_lightning";

    @SpellPerk(perkId = "static_overload")
    public void staticOverload(SpellDamageEvent event) {
        var graph = ChainLightningGraphExtractor.capture(event);
        if (graph == null) return;

        Entity victim = event.getEntity();
        Entity parent = ChainLightningGraphExtractor.findParent(victim, graph);

        if (parent instanceof LivingEntity livingParent && parent != graph.caster) {
            float backflowAmount = event.getAmount() * 0.30f;

            // Merge power, but keep the caster reference
            StaticOverloadHandler.PENDING_AFTERSHOCKS.merge(livingParent,
                    new StaticOverloadHandler.SurgeData(backflowAmount, graph.caster),
                    (oldData, newData) -> new StaticOverloadHandler.SurgeData(oldData.power() + newData.power(), oldData.caster())
            );
        }
    }


    @SpellPerk(perkId = "ground_zero")
    public void groundZero(SpellDamageEvent event) {
        var graph = ChainLightningGraphExtractor.capture(event);
        if (graph == null) return;

        Entity victim = event.getEntity();

        // The initial victim is the only one with a depth of 1
        // (since depth 0 is the caster)
        if (graph.getDepth(victim) == 1) {
            event.setAmount(event.getAmount() * 2.0f);

            // Optional: Add a 'thunder' sound effect for the initial boom
            victim.level().playSound(null, victim.getX(), victim.getY(), victim.getZ(),
                    SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.0f, 1.0f);
        }
    }
}