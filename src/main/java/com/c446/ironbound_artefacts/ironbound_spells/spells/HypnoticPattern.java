package com.c446.ironbound_artefacts.ironbound_spells.spells;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.ironbound_spells.spells.enthrall.DominatedEffectInstance;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.TargetedTargetAreaCastData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@AutoSpellConfig
public class HypnoticPattern extends AbstractSpell {
    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.EPIC).setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE).setMaxLevel(4).setCooldownSeconds(30 * 20).build();
    ResourceLocation spellId = IronboundArtefact.prefix("hypnotic_pattern");


    public HypnoticPattern() {
        this.baseSpellPower = 40;
        this.spellPowerPerLevel = 20;
        this.baseManaCost = 300;
        this.manaCostPerLevel = 20;
        this.castTime = 20 * 10;
    }

    @Override
    public Vector3f getTargetingColor() {
        return SchoolRegistry.ENDER.get().getTargetingColor();
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.CLOUD_OF_REGEN_LOOP.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return super.getCastFinishSound();
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
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        if (!Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.35F, false)) {
            playerMagicData.setAdditionalCastData(new TargetEntityCastData(entity));
            if (entity instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.irons_spellbooks.spell_target_success_self", this.getDisplayName(serverPlayer)).withStyle(ChatFormatting.GREEN)));
            }
        }

        int rad = (int) (15 * spellLevel * getSpellPower(spellLevel, entity));
        LivingEntity target = ((TargetEntityCastData) playerMagicData.getAdditionalCastData()).getTarget((ServerLevel) level);
        TargetedAreaEntity area = TargetedAreaEntity.createTargetAreaEntity(level, target.position(), rad, Utils.packRGB(this.getTargetingColor()), target);
        playerMagicData.setAdditionalCastData(new TargetedTargetAreaCastData(target, area));
        return true;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);


        if (level instanceof ServerLevel serverLevel && playerMagicData.getAdditionalCastData() instanceof TargetedTargetAreaCastData targetAreaCastData && targetAreaCastData.getTarget(serverLevel) != null) {
            var targetEntity = targetAreaCastData.getTarget(serverLevel);
            int rad = (int) (15 * spellLevel * getSpellPower(spellLevel, entity));
            int targetMax = (int) (spellLevel * 5 * getSpellPower(spellLevel, entity));
            AtomicInteger targetNumber = new AtomicInteger(0);
            targetEntity.level().getEntitiesOfClass(LivingEntity.class, targetEntity.getBoundingBox().inflate(rad)).forEach(tar -> {
                if (!entity.isAlliedTo(tar) && !tar.hasEffect(EffectsRegistry.ENTHRALLED) && !(tar instanceof IMagicSummon) && !(tar instanceof Player) && !(tar.getType().is(Tags.EntityTypes.BOSSES)) && tar.getHealth() / getSpellPower(spellLevel, entity) <= 20) {
                    tar.addEffect(new DominatedEffectInstance(tar, entity, EffectsRegistry.ENTHRALLED, (int) (40 * (this.spellPowerPerLevel * spellLevel + this.baseSpellPower) * getSpellPower(spellLevel, entity)), 0));
                }
            });
        }
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }


}
