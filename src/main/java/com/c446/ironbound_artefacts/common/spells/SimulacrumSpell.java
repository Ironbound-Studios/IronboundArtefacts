// SimulacrumSpell.java
package com.c446.ironbound_artefacts.common.spells;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.common.entities.SimulacrumEntity;
import com.c446.ironbound_artefacts.registries.RegistryEntities;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonedEntitiesCastData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SimulacrumSpell extends AbstractSpell {
    private final ResourceLocation spellId = IBA.p("simulacrum");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(1200)
            .build();

    public SimulacrumSpell() {
        this.manaCostPerLevel = 50;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 300;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.summon_count", getSummonCount(spellLevel, caster)));
    }

    public int getSummonCount(int spellLevel, LivingEntity caster) {
        return spellLevel;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 1;
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (SummonManager.recastFinishedHelper(serverPlayer, recastInstance, recastResult, castDataSerializable)) {
            super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
        }
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new SummonedEntitiesCastData();
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity caster, CastSource castSource, MagicData playerMagicData) {
        if (world instanceof ServerLevel serverLevel && caster instanceof Player player) {
            var recasts = playerMagicData.getPlayerRecasts();

            if (!recasts.hasRecastForSpell(this)) {
                SummonedEntitiesCastData summonedEntitiesCastData = new SummonedEntitiesCastData();

                int summonTime = (int) (this.getSpellPower(spellLevel, player) * 1200 * (spellPowerPerLevel * spellLevel + baseSpellPower));
                int count = getSummonCount(spellLevel, player);
                float radius = 1.5f + .185f * count;

                for (int i = 0; i < count; i++) {
                    SimulacrumEntity simulacrum = new SimulacrumEntity(RegistryEntities.SIMULACRUM.get(), serverLevel);

                    // Use the new SummonManager API for owner assignment instead of the deprecated setSummoner
                    SummonManager.setOwner(simulacrum, player);
// SimulacrumSpell.java — inside the for loop, after SummonManager.setOwner(simulacrum, player)
                    simulacrum.getEntityData().set(SimulacrumEntity.OWNER_UUID, Optional.of(player.getUUID()));
                    // Pass quality after owner is set
                    simulacrum.quality = this.getSpellPower(spellLevel, player);

                    // Sync equipment from caster (drop chances zeroed inside equip())
                    equip(simulacrum, player);

                    // Positioning logic (matches RaiseDead)
                    var yrot = 6.281f / count * i + player.getYRot() * Mth.DEG_TO_RAD;
                    Vec3 spawn = Utils.moveToRelativeGroundLevel(world, player.getEyePosition().add(new Vec3(radius * Mth.cos(yrot), 0, radius * Mth.sin(yrot))), 10);
                    spawn = world.clip(new ClipContext(player.getEyePosition(), spawn, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty())).getLocation();

                    if (!world.noCollision(simulacrum.getBoundingBox().move(spawn))) {
                        spawn = Utils.moveToRelativeGroundLevel(world, spawn.add(player.getEyePosition().subtract(spawn).normalize().scale(player.getBbWidth() * 1.1)), 3);
                    }

                    simulacrum.setPos(spawn.x, spawn.y, spawn.z);
                    simulacrum.setYRot(player.getYRot());
                    simulacrum.setOldPosAndRot();

                    var finalEntity = NeoForge.EVENT_BUS.post(new SpellSummonEvent<>(player, simulacrum, this.spellId, spellLevel)).getCreature();

                    world.addFreshEntity(finalEntity);
                    SummonManager.initSummon(player, finalEntity, summonTime, summonedEntitiesCastData);
                    // SimulacrumSpell.java — inside the for loop in onCast(), after world.addFreshEntity(finalEntity)
                    IBA.LOGGER.debug("[SimulacrumSpell] Spawned simulacrum | entity={} | pos=({},{},{}) | owner={} | quality={} | summonTime={}",
                            finalEntity,
                            finalEntity.getX(), finalEntity.getY(), finalEntity.getZ(),
                            player.getName().getString(),
                            simulacrum.quality,
                            summonTime);
                }

                RecastInstance recastInstance = new RecastInstance(this.getSpellId(), spellLevel, getRecastCount(spellLevel, player), summonTime, castSource, summonedEntitiesCastData);
                recasts.addRecast(recastInstance, playerMagicData);
            }
        }
        super.onCast(world, spellLevel, caster, castSource, playerMagicData);
    }

    private void equip(SimulacrumEntity simulacrum, LivingEntity caster) {
        simulacrum.setItemSlot(EquipmentSlot.FEET, caster.getItemBySlot(EquipmentSlot.FEET).copy());
        simulacrum.setItemSlot(EquipmentSlot.LEGS, caster.getItemBySlot(EquipmentSlot.LEGS).copy());
        simulacrum.setItemSlot(EquipmentSlot.CHEST, caster.getItemBySlot(EquipmentSlot.CHEST).copy());
        simulacrum.setItemSlot(EquipmentSlot.HEAD, caster.getItemBySlot(EquipmentSlot.HEAD).copy());
        simulacrum.setItemSlot(EquipmentSlot.MAINHAND, caster.getItemBySlot(EquipmentSlot.MAINHAND).copy());
        simulacrum.setItemSlot(EquipmentSlot.OFFHAND, caster.getItemBySlot(EquipmentSlot.OFFHAND).copy());

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            simulacrum.setDropChance(slot, 0.0F);
        }
    }
}