package com.c446.ironbound_artefacts.common.spells.void_sword;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.common.entities.SwordStormEntity;
import com.c446.ironbound_artefacts.registries.RegistryEntities;
import com.c446.ironbound_artefacts.registries.RegistrySchoolTypes;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

@AutoSpellConfig
public class SwordStormSpell extends AbstractSpell {
    private final ResourceLocation spellId = IBA.p("sword_storm");
    private final DefaultConfig defaultConfig;

    public SwordStormSpell() {
        this.defaultConfig = new DefaultConfig()
                .setMinRarity(SpellRarity.EPIC)
                .setSchoolResource(RegistrySchoolTypes.IMMORTAL_ART_LOC) // Or your preferred school
                .setMaxLevel(5)
                .setCooldownSeconds(15.0F)
                .build();

        this.manaCostPerLevel = 5;
        this.baseManaCost = 25;
        this.castTime = 100; // Total duration in ticks (5 seconds)
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 2;
    }

    @Override
    public CastType getCastType() {
        return CastType.CONTINUOUS;
    }

/*@Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        var li = new ArrayList<>(super.getUniqueInfo(spellLevel, caster));
        li.add(Component.translatable("ui.ironbound_artefacts.sword_storm_damage", Utils.setPrecision(getDamage(spellLevel, caster), 1))
                .withStyle(ChatFormatting.GOLD));
        return li;
    }
*/
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData magicData) {
        if (level instanceof ServerLevel) {
            // If we don't have the storm entity active for this cast, spawn it
            if (!(magicData.getAdditionalCastData() instanceof SwordStormCastData)) {
                SwordStormEntity storm = new SwordStormEntity(RegistryEntities.SWORD_STORM.get(), level, entity);

                // Set the damage based on spell power
                // Note: Ensure your SwordStormEntity has a 'damage' field or setter
                storm.setDamage(getDamage(spellLevel, entity));

                level.addFreshEntity(storm);

                // Store the entity in MagicData so we can track/kill it later
                magicData.setAdditionalCastData(new SwordStormCastData(storm));
            }
        }
        super.onCast(level, spellLevel, entity, castSource, magicData);
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData magicData) {
        if (magicData != null && magicData.getAdditionalCastData() instanceof SwordStormCastData castData) {
            // Keep the entity updated or check if it's still valid
//            if (castData.storm.isRemoved()) {
//                magicData.getCastHistory().stopCast();
//            }
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

    /**
     * Cast Data helper to link the Entity to the Spell Channel
     */
    public static class SwordStormCastData implements ICastData {
        public final SwordStormEntity storm;

        public SwordStormCastData(SwordStormEntity storm) {
            this.storm = storm;
        }

        @Override
        public void reset() {
            if (storm != null && !storm.isRemoved()) {
                storm.discard();
            }
        }
    }
}