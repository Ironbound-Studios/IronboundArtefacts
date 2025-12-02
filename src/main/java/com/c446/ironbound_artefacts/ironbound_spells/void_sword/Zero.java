package com.c446.ironbound_artefacts.ironbound_spells.void_sword;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.CustomSpellRegistry;
import com.c446.ironbound_artefacts.registries.DamageSourcesReg;
import com.c446.ironbound_artefacts.registries.SchoolTypesRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.CounterSpellEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicProvider;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.LightningStrike;
import io.redspace.ironsspellbooks.spells.ender.CounterspellSpell;
import io.redspace.ironsspellbooks.spells.fire.FireballSpell;
import io.redspace.ironsspellbooks.spells.lightning.LightningBoltSpell;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@EventBusSubscriber
public class Zero extends VoidSpell {
    public static final ResourceLocation loc = IronboundArtefact.prefix("spell_zero");

    public static final DefaultConfig cfg = new DefaultConfig().setAllowCrafting(false).setMaxLevel(1).setCooldownSeconds(0).setMinRarity(SpellRarity.LEGENDARY).setSchoolResource(SchoolTypesRegistry.IMMORTAL_ART_LOC);

    public Zero() {
        this.baseManaCost = 450;
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
        this.castTime = 30;
    }

    @Override
    ResourceKey<DamageType> damageLoc() {
        return DamageSourcesReg.VOID_SWORD_ZERO;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return loc;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return cfg;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_TWO_HANDS;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    HitResult getHit(LivingEntity liv, Level lvl, int spellLevel) {
        var pow = this.getSpellPower(spellLevel, liv) * 20;

        return Utils.raycastForEntity(lvl, liv, 3 * pow, true, 1.5f);
    }

    public DamageSource dmgSource(Entity attacker) {
        return new DamageSource(DamageSourcesReg.getHolderFromResource(attacker, DamageSourcesReg.VOID_SWORD_ZERO), attacker);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity living, CastSource castSource, MagicData playerMagicData) {
        if (level instanceof ServerLevel s) {
            var loc = getHit(living, s, spellLevel).getLocation();

            s.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(loc, 20, 20, 20)).forEach(e -> {
                e.removeAllEffects();
                e.setHealth(e.getMaxHealth());
                MagicData.getPlayerMagicData(e).setMana((float) e.getAttributeValue(AttributeRegistry.MAX_MANA));
                e.hurt(this.getDamageSource(living), 40*this.getSpellPower(spellLevel, living));
            });


        }

        super.onCast(level, spellLevel, living, castSource, playerMagicData);
    }

    @Override
    public void onClientCast(Level level, int spellLevel, LivingEntity living, ICastData castData) {

        super.onClientCast(level, spellLevel, living, castData);
    }

    @Override
    public boolean canBeInterrupted(@Nullable Player player) {
        return super.canBeInterrupted(player);
    }

    @SubscribeEvent
    static void antiCounterSpell(CounterSpellEvent e) {
        if (e.target instanceof ServerPlayer p && CustomSpellRegistry.ZERO.get().getSpellResource().toString().contains(MagicData.getPlayerMagicData(p).getCastingSpellId())) {
            e.setCanceled(true);
        }
    }
}
