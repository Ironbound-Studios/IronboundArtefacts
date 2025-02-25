package com.c446.ironbound_artefacts.ironbound_spells.spells.enthrall;

import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

import java.util.Set;

public class EnthralledEffect extends MagicMobEffect {
    public EnthralledEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        NeoForge.EVENT_BUS.addListener(EnthralledEffect::onTarget);
        NeoForge.EVENT_BUS.addListener(EnthralledEffect::customAI);
    }

    public static void customAI(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LivingEntity && !event.getLevel().isClientSide) {
            if (event.getEntity() instanceof PathfinderMob mob && (mob.getNavigation() instanceof GroundPathNavigation || mob.getNavigation() instanceof FlyingPathNavigation)) {
                try {
                    mob.goalSelector.addGoal(2, new FollowOwnerGoal(mob, 1.5F, 3.0F, 1.2F));
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
    }

    public static void onTarget(LivingChangeTargetEvent event) {
        if (!(event.getNewAboutToBeSetTarget() instanceof Player player)) {
            return;
        }
        if (event.getEntity() instanceof Mob thrall && isEnthralledBy(thrall, player)) {
            if (player.getLastHurtMob() != null && player.getLastHurtMob() != thrall) {
                // target mob that player attacked
                event.setNewAboutToBeSetTarget(player.getLastHurtMob());
                if (thrall instanceof NeutralMob angyMob) {
                    angyMob.setPersistentAngerTarget(player.getLastHurtMob().getUUID());
                }
            } else if (player.getLastHurtByMob() != null && player.getLastHurtByMob() != thrall) {
                // target mob that attacked player
                event.setNewAboutToBeSetTarget(player.getLastHurtByMob());
                if (thrall instanceof NeutralMob angry)
                    angry.setPersistentAngerTarget(player.getLastHurtByMob().getUUID());
            } else {
                // no valid target.
                event.setNewAboutToBeSetTarget(null);
                if (thrall instanceof NeutralMob angry) angry.setRemainingPersistentAngerTime(0);
            }
        }
    }

    public static boolean isEnthralledBy(Mob thrall, Player player) {
        if (thrall.getEffect(EffectsRegistry.ENTHRALLED) instanceof DominatedEffectInstance dominatedEffectInstance && dominatedEffectInstance.receiver instanceof Mob mob && dominatedEffectInstance.emitter instanceof Player emitter) {
            return (emitter == player);
        }
        return false;
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
    }
}
