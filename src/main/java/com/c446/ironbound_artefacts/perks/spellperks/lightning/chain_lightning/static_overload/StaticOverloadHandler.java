package com.c446.ironbound_artefacts.perks.spellperks.lightning.chain_lightning.static_overload;

import com.c446.ironbound_artefacts.perks.spellperks.SpellPerk;
import com.c446.ironbound_artefacts.perks.spellperks.lightning.chain_lightning.ChainLightningGraphExtractor;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber
public class StaticOverloadHandler {

    // Store both the damage power and the caster responsible for the chain
    public record SurgeData(float power, Entity caster) {}
    public static final Map<LivingEntity, SurgeData> PENDING_AFTERSHOCKS = new HashMap<>();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (PENDING_AFTERSHOCKS.isEmpty()) return;

        PENDING_AFTERSHOCKS.forEach((parent, data) -> {
            if (parent.isAlive() && parent.level() instanceof ServerLevel level) {
                Vec3 pos = parent.position();

                // 1. Visual Lightning
                LightningBolt visualBolt = EntityType.LIGHTNING_BOLT.create(level);
                if (visualBolt != null) {
                    visualBolt.setVisualOnly(true);
                    visualBolt.setPos(pos);
                    level.addFreshEntity(visualBolt);

                    // 2. Splash Damage Logic
                    float radius = 3.0f;
                    AABB bounds = AABB.ofSize(pos, radius * 2, radius * 2, radius * 2);

                    // Retrieve the specific damage source from the Lightning Bolt Spell
                    var lightningSpell = SpellRegistry.LIGHTNING_BOLT_SPELL.get();
                    var dmgSource = lightningSpell.getDamageSource(visualBolt, data.caster);

                    level.getEntities(parent, bounds, (target) -> canHit(parent, target))
                            .forEach(target -> {
                                double distanceSq = target.distanceToSqr(pos);
                                if (distanceSq < radius * radius) {
                                    target.invulnerableTime = 0;
                                    float finalDamage = (float) (data.power * (1 - distanceSq / (radius * radius)));

                                    // Use the specific spell damage source for attribution
                                    DamageSources.applyDamage(target, finalDamage, dmgSource);
                                }
                            });

                    // 3. Apply base damage to parent
                    parent.invulnerableTime = 0;
                    parent.hurt(dmgSource, data.power);
                }
            }
        });

        PENDING_AFTERSHOCKS.clear();
    }

    private static boolean canHit(Entity owner, Entity target) {
        return target != owner && target.isAlive() && target.isPickable() && !target.isSpectator();
    }
}