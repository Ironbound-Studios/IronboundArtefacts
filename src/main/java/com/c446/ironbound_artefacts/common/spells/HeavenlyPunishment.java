package com.c446.ironbound_artefacts.common.spells;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.registries.RegistryEffects;
import com.c446.ironbound_artefacts.registries.RegistrySchoolTypes;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

@AutoSpellConfig
public class HeavenlyPunishment extends AbstractSpell {
    private final ResourceLocation loc = IBA.p("lightning_immortal_art");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(RegistrySchoolTypes.IMMORTAL_ART_LOC)
            .setMaxLevel(1)
            .setCooldownSeconds(60 * 30)
            .build();

    public HeavenlyPunishment() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 0;
        this.baseManaCost = 0;
    }

    @Override
    public float getSpellPower(int spellLevel, @Nullable Entity sourceEntity) {
        return (float) (super.getSpellPower(1, sourceEntity) * (sourceEntity instanceof LivingEntity l ? l.getAttributeValue(AttributeRegistry.LIGHTNING_SPELL_POWER) : 1));
    }

    @Override
    public ResourceLocation getSpellResource() {
        return loc;
    }

    @Override
    public SpellDamageSource getDamageSource(Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker);
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public MutableComponent getDisplayName(Player player) {
        return Component.translatable(getComponentId()).withStyle(ChatFormatting.AQUA);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
        entity.addEffect(new MobEffectInstance(RegistryEffects.LIGHTNING_VOICE, 20*60*30,0));

        /*
        if (level instanceof ServerLevel s){
            s.setWeatherParameters(0, 30*20*60, true, true);
        }
        */


    }
}
