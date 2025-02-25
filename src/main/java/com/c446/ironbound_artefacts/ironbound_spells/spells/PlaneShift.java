package com.c446.ironbound_artefacts.ironbound_spells.spells;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MultiTargetEntityCastData;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.spells.TargetAreaCastData;
import io.redspace.ironsspellbooks.spells.TargetedTargetAreaCastData;
import io.redspace.ironsspellbooks.spells.eldritch.EldritchBlastSpell;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.font.providers.UnihexProvider;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@AutoSpellConfig
public class PlaneShift extends AbstractSpell {

    public static final ResourceLocation spellId = IronboundArtefact.prefix("plane_shift");

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    public static final DefaultConfig config = new DefaultConfig().setAllowCrafting(true).setCooldownSeconds(20 * 100).setSchoolResource(SchoolRegistry.ENDER_RESOURCE).setMaxLevel(1).setMinRarity(SpellRarity.LEGENDARY).build();

    @Override
    public DefaultConfig getDefaultConfig() {
        return config;
    }

    public PlaneShift() {
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.baseManaCost = 500;
        this.manaCostPerLevel = 50;
        this.castTime = 10;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public boolean attemptInitiateCast(ItemStack stack, int spellLevel, Level level, Player player, CastSource castSource, boolean triggerCooldown, String castingEquipmentSlot) {
        return super.attemptInitiateCast(stack, spellLevel, level, player, castSource, triggerCooldown, castingEquipmentSlot);
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        if (!Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.35F, false)) {
            playerMagicData.setAdditionalCastData(new TargetEntityCastData(entity));
            if (entity instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer) entity;
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.irons_spellbooks.spell_target_success_self", new Object[]{this.getDisplayName(serverPlayer)}).withStyle(ChatFormatting.GREEN)));
            }
        }

        float radius = 3.0F;
        LivingEntity target = ((TargetEntityCastData) playerMagicData.getAdditionalCastData()).getTarget((ServerLevel) level);
        TargetedAreaEntity area = TargetedAreaEntity.createTargetAreaEntity(level, target.position(), radius, Utils.packRGB(this.getTargetingColor()), target);
        playerMagicData.setAdditionalCastData(new TargetedTargetAreaCastData(target, area));
        return true;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        //player.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.irons_spellbooks.spell_target_success_")))
        ICastData targetEntity = playerMagicData.getAdditionalCastData();
        if (level instanceof ServerLevel && entity instanceof ServerPlayer serverPlayer && targetEntity instanceof TargetedTargetAreaCastData targetData) {
            if (level instanceof ServerLevel serverLevel) {
                @Nullable var mHand = (serverPlayer.getMainHandItem().has(ComponentRegistry.TUNING_FORK_ATTACHMENT)) ? (serverPlayer.getMainHandItem().get(ComponentRegistry.TUNING_FORK_ATTACHMENT)) : null;
                @Nullable var oHand = (serverPlayer.getOffhandItem().has(ComponentRegistry.TUNING_FORK_ATTACHMENT)) ? (serverPlayer.getOffhandItem().get(ComponentRegistry.TUNING_FORK_ATTACHMENT)) : null;
                @Nullable var dim = (mHand != null) ? (mHand.dim()) : ((oHand != null) ? (oHand.dim()) : null);
                if (dim != null) {
                    AtomicReference<Optional<ServerLevel>> optionalServerLevel = new AtomicReference<>();
                    Objects.requireNonNull(serverPlayer.level().getServer()).getAllLevels().forEach(l -> {
                        if (l.dimension().toString().equals(dim)) {
                            optionalServerLevel.set(Optional.of(l));
                        }
                    });
                    var radius = this.getSpellPower(spellLevel, entity);
                    var tar = targetData.getTarget(serverLevel);
                    var targetList = tar.level().getEntitiesOfClass(LivingEntity.class, tar.getBoundingBox().inflate(radius));
                    targetList.forEach(target -> {
                        optionalServerLevel.get().ifPresent(nextDimension -> {
                            var trueTarget = serverLevel.getEntity(target.getUUID());
                            if (trueTarget != null) {
                                if (nextDimension.dimension().equals(ServerLevel.NETHER)) {
                                    trueTarget.teleportTo(nextDimension, trueTarget.getX() / 8, trueTarget.getY() / 2, trueTarget.getZ() / 8, Set.of(), trueTarget.getXRot(), trueTarget.getYRot());
                                } else if (serverLevel.dimension().equals(ServerLevel.NETHER)) {
                                    trueTarget.teleportTo(nextDimension, trueTarget.getX() * 8, trueTarget.getY() * 2, trueTarget.getZ() * 8, Set.of(), trueTarget.getXRot(), trueTarget.getYRot());
                                } else {
                                    trueTarget.teleportTo(nextDimension, trueTarget.getX(), trueTarget.getY(), trueTarget.getZ(), Set.of(), trueTarget.getXRot(), trueTarget.getYRot());
                                }
                            }
                        });
                    });
                }
            }
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.PORTAL_TRAVEL);
    }
}
