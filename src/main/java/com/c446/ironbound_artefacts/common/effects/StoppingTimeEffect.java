package com.c446.ironbound_artefacts.common.effects;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.client.TimeStopHandler;
import com.c446.ironbound_artefacts.common.spells.TimeStopSpell;
import com.c446.ironbound_artefacts.registries.RegistrySpells;
import com.c446.ironbound_artefacts.registries.RegistryEffects;
import net.acetheeldritchking.aces_spell_utils.network.AddShaderEffectPacket;
import net.acetheeldritchking.aces_spell_utils.network.RemoveShaderEffectPacket;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

public class StoppingTimeEffect extends IronboundMobEffect {
    public StoppingTimeEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    protected StoppingTimeEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color, particle);
    }

    public boolean isPlayerFriendTo(Player target, Player attacker) {
        return false;
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity caster, int spellLevel) {
        //System.out.println("time stopping effect triggered");
        var timeStopSpell = (TimeStopSpell) RegistrySpells.TIME_STOP.get();
        var area = timeStopSpell.getBoundingBox(caster, spellLevel);
        if (caster.level() instanceof ServerLevel level) {
            var entities = level.getEntities(caster, area);
            boolean finishSoon = Objects.requireNonNull(caster.getEffect(RegistryEffects.TIME_STOP_CASTER)).endsWithin(1);
            TimeStopHandler.TIME_STOPPER.add(caster);
            for (Entity e : entities) {
                if (!e.isAlliedTo(caster)) {
                    if (!finishSoon) {
                        IBA.freezeEntity(e, 1);
                        if (e instanceof Player player) {
                            player.addEffect(new MobEffectInstance(RegistryEffects.TIME_FROZEN, 20 * 3, (1), false, true));
                            if (e instanceof ServerPlayer sp)
                                TimeStopHandler.addStopped(sp);
                        }
                    } else {
                        if (e instanceof ServerPlayer sp) TimeStopHandler.remStopped(sp);
                    }
                }
            }

        }
        return super.applyEffectTick(caster, spellLevel);
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        // Leave this empty to prevent milk or curatives from clearing
    }

    @Override
    public StoppingTimeEffect addAttributeModifier(Holder<Attribute> pAttribute, ResourceLocation pId,
                                                   double pAmount, AttributeModifier.Operation pOperation) {
        return (StoppingTimeEffect) super.addAttributeModifier(pAttribute, pId, pAmount, pOperation);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }
}
