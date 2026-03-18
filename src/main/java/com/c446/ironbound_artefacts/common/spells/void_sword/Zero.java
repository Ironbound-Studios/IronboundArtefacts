package com.c446.ironbound_artefacts.common.spells.void_sword;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.registries.RegistryDamageSources;
import com.c446.ironbound_artefacts.registries.RegistrySpells;
import com.c446.ironbound_artefacts.registries.RegistrySchoolTypes;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.CounterSpellEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber
public class Zero extends VoidSpell {
    public static final ResourceLocation loc = IBA.p("spell_zero");

    public static final DefaultConfig cfg = new DefaultConfig().setAllowCrafting(false).setMaxLevel(1).setCooldownSeconds(0).setMinRarity(SpellRarity.LEGENDARY).setSchoolResource(RegistrySchoolTypes.IMMORTAL_ART_LOC);

    public Zero() {
        this.baseManaCost = 450;
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
        this.castTime = 30;
    }

    @Override
    ResourceKey<DamageType> damageLoc() {
        return RegistryDamageSources.VOID_SWORD_ZERO;
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
        return new DamageSource(RegistryDamageSources.getHolderFromResource(attacker, RegistryDamageSources.VOID_SWORD_ZERO), attacker);
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
        if (e.target instanceof ServerPlayer p && RegistrySpells.ZERO.get().getSpellResource().toString().contains(MagicData.getPlayerMagicData(p).getCastingSpellId())) {
            e.setCanceled(true);
        }
    }
}
