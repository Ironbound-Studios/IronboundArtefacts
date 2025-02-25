package com.c446.ironbound_artefacts.effects;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.ironbound_spells.spells.TimeStopSpell;
import com.c446.ironbound_artefacts.registries.CustomSpellRegistry;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.event.level.NoteBlockEvent;
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

    public boolean isPlayerFriendTo(Player target, Player attacker){
        return  false;
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity caster, int spellLevel) {
        System.out.println("time stopping effect triggered");
        var timeStopSpell = (TimeStopSpell) CustomSpellRegistry.TIME_STOP.get();
        var area = timeStopSpell.getBoundingBox(caster, spellLevel);
        if (caster.level() instanceof ServerLevel level) {
            var entities = level.getEntities(caster, area);
            boolean finishSoon = Objects.requireNonNull(caster.getEffect(EffectsRegistry.TIME_STOP_CASTER)).endsWithin(1);
            if (!finishSoon) {
                for (Entity e : entities) {
                    if (!e.isAlliedTo(caster)) {
                        IronboundArtefact.freezeEntity(e, 1);
                        if (e instanceof Player player){
                            player.addEffect(new MobEffectInstance(MobEffectRegistry.SLOWED, 20*3,(1+spellLevel*2),false,true));
                        }
                    }
                    //IronboundArtefact.tickMap();
                    /*
                    if (e instanceof LivingEntity l) {
                        if (!l.isAlliedTo(caster)){
                            var data = new MovementDeltaData();
                            data.setVec(l.getDeltaMovement());
                            l.setData(AttachmentRegistry.VECTOR_ATTACHMENT, data);
                            l.addEffect(new MobEffectInstance(EffectsRegistry.TIME_STOP, (int) (timeStopSpell.getTickDuration(spellLevel, caster)), 0, true, true));
                        }
                    } else {
                        if (e instanceof AbstractMagicProjectile projectile && projectile.getOwner() != caster){
                            var data = new MovementDeltaData();
                            data.setVec(e.getDeltaMovement());
                            e.setData(AttachmentRegistry.VECTOR_ATTACHMENT, data);
                            e.setDeltaMovement(0, 0, 0);
                            e.setNoGravity(true);
                        }
                    }
                }
            } else {
                for (Entity e : entities) {
                    e.setNoGravity(false);
                    e.setDeltaMovement(e.getData(AttachmentRegistry.VECTOR_ATTACHMENT).getVec());
                }*/
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
