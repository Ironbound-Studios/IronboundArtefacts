package com.c446.ironbound_artefacts.common.spells;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.common.entities.LastPrismEntity;
import com.c446.ironbound_artefacts.registries.RegistryEntities;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@AutoSpellConfig
public class LastPrismSpell extends AbstractSpell {
    private final ResourceLocation spellId = IBA.p("last_prism");
    private final DefaultConfig defaultConfig;

    public LastPrismSpell() {
        this.defaultConfig = new DefaultConfig()
                .setMinRarity(SpellRarity.LEGENDARY)
                .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
                .setMaxLevel(5)
                .setCooldownSeconds(20.0F)
                .build();

        this.manaCostPerLevel = 10;
        this.baseManaCost = 50;
        this.castTime = 200;
        this.baseSpellPower = 32;
        this.spellPowerPerLevel = 8;
    }

    @Override
    public CastType getCastType() {
        return CastType.CONTINUOUS;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        var li = new ArrayList<>(super.getUniqueInfo(spellLevel, caster));

        li.addFirst(Component.literal("\uE446F - Dev Spell - \uE446")
                .withStyle(ChatFormatting.LIGHT_PURPLE));

        System.out.println(li.getFirst().toString()+"\n"+ li.getFirst().getStyle());
        return li;
    }


    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData magicData) {
        if (level instanceof ServerLevel serverLevel) {
            // Check if we already have cast data (meaning the prism is already alive)
            if (!(magicData.getAdditionalCastData() instanceof LastPrismEntity.LastPrismCastData)) {
                LastPrismEntity prism = new LastPrismEntity(RegistryEntities.LAST_PRISM.get(), level);
                prism.setOwnerUUID(entity.getUUID());
                prism.damageFlat = getDamage(spellLevel, entity);
                prism.centerOnCaster(entity);

                level.addFreshEntity(prism);

                // Bind the entity to this specific cast session
                magicData.setAdditionalCastData(new LastPrismEntity.LastPrismCastData(prism));
            }
        }
        super.onCast(level, spellLevel, entity, castSource, magicData);
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData magicData) {
        super.onServerCastTick(level, spellLevel, entity, magicData);
        if (magicData != null && magicData.getAdditionalCastData() instanceof LastPrismEntity.LastPrismCastData castData) {
            // Keep the damage updated in case of buffs/level changes mid-cast
            castData.prism.damageFlat = getDamage(spellLevel, entity);
        }
    }


    public float getDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }
}