package com.c446.ironbound_artefacts.ironbound_spells.spells;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static io.redspace.ironsspellbooks.api.registry.AttributeRegistry.ELDRITCH_SPELL_POWER;

@AutoSpellConfig
public class TimeStopSpell extends AbstractSpell {
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(1200)
            .build();
    private final ResourceLocation spellId = IronboundArtefact.prefix("time_stop");

    public TimeStopSpell() {
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 5;
        this.baseManaCost = 700;
        this.manaCostPerLevel = 100;
        this.castTime = 20;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.ANVIL_BREAK);
    }

    @Override
    public int getCastTime(int spellLevel) {
        return (this.castTime * (spellLevel));
    }

    @Override
    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
        return 20 + super.getEffectiveCastTime(spellLevel - 1, entity);
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        if (caster instanceof Player) {
            return List.of(
                    Component.translatable("ui.irons_spellbooks.duration", Utils.timeFromTicks((float) getTickDuration(spellLevel, caster), 1)),
                    Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(this.getAOE(spellLevel, caster), 1))
            );
        } else {
            return List.of(
                    Component.translatable("ui.irons_spellbooks.duration", Utils.timeFromTicks((float) getTickDuration(spellLevel), 1)),
                    Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(this.getAOE(spellLevel), 1))
            );

        }
    }

    public double getTickDuration(int spellLevel) {
        return 20 * spellLevel;
    }

    public double getTickDuration(int spellLevel, LivingEntity living) {
        return 20 * (spellLevel + living.getAttributeValue(ELDRITCH_SPELL_POWER));
    }

    public float getAOE(int spellLevel) {
        return (10 * spellLevel);
    }

    public float getAOE(int spellLevel, LivingEntity entity) {
        return (float) Math.max((8 * spellLevel * entity.getAttributeValue(ELDRITCH_SPELL_POWER)), 8 + 16 * (spellLevel - 1));
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }


    public AABB getBoundingBox(LivingEntity entity, int level) {
        return entity.getBoundingBox().inflate(this.getAOE(level, entity));
    }


    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(EffectsRegistry.TIME_STOP_CASTER, (int) this.getTickDuration(spellLevel, entity), spellLevel));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}