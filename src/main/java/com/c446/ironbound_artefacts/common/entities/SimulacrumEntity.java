package com.c446.ironbound_artefacts.common.entities;

import com.c446.ironbound_artefacts.common.datagen.Tags;
import com.c446.ironbound_artefacts.registries.RegistryEntities;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.SupportMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class SimulacrumEntity extends NeutralWizard implements IMagicSummon, SupportMob {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SimulacrumEntity.class);

    /**
     * Synced to the client so the render thread can resolve the owning player.
     * SummonManager.getOwner() is server-side only; without this the client always
     * gets null and immediately discards the entity.
     */
    public static final EntityDataAccessor<Optional<UUID>> OWNER_UUID =
            SynchedEntityData.defineId(SimulacrumEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public float quality = 1F;
    public PlayerInfo playerInfo = null;
    private LivingEntity helpTarget;

    public SimulacrumEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setUUID(UUID.randomUUID());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(OWNER_UUID, Optional.empty());
    }

    public static AttributeSupplier.Builder createAttributes() {
        var attr = Player.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 30D)
                .add(Attributes.MOVEMENT_SPEED, .25f)
                .add(Attributes.MAX_HEALTH, 150);
        for (var attribute : BuiltInRegistries.ATTRIBUTE.registryKeySet()) {
            if (!attr.hasAttribute(BuiltInRegistries.ATTRIBUTE.getHolderOrThrow(attribute))) {
                var holder = BuiltInRegistries.ATTRIBUTE.getHolderOrThrow(attribute);
                attr.add(holder, holder.value().getDefaultValue());
            }
        }
        return attr;
    }

    /**
     * Use SummonManager as the authority on the server.
     * Fall back to the synced UUID on the client where SummonManager returns null.
     */
    @Override
    public Player getSummoner() {
        if (!level().isClientSide) {
            // Server: SummonManager is authoritative
            var owner = SummonManager.getOwner(this);
            LOGGER.debug("[Simulacrum] getSummoner() called | isClientSide=false | owner={} | ownerType={}",
                    owner, owner == null ? "NULL" : owner.getClass().getSimpleName());
            if (owner instanceof Player player) {
                return player;
            }
            LOGGER.warn("[Simulacrum] getSummoner() server-side owner was not a Player: {}", owner);
            return null;
        } else {
            // Client: SummonManager doesn't sync, so resolve via the synced UUID
            Optional<UUID> uuid = this.entityData.get(OWNER_UUID);
            LOGGER.debug("[Simulacrum] getSummoner() called | isClientSide=true | uuid={}", uuid);
            if (uuid.isEmpty()) {
                LOGGER.warn("[Simulacrum] getSummoner() client-side OWNER_UUID is empty!");
                return null;
            }
            Player player = level().getPlayerByUUID(uuid.get());
            if (player == null) {
                LOGGER.warn("[Simulacrum] getSummoner() client could not find player for UUID: {}", uuid.get());
            }
            return player;
        }
    }

    /**
     * @deprecated Use {@link SummonManager#setOwner(Entity, Entity)} after construction instead,
     * then write OWNER_UUID into entity data for client sync.
     */
    @Deprecated(forRemoval = true)
    public void setSummoner(Player player) {
        if (player != null) {
            SummonManager.setOwner(this, player);
        }
    }

    @Override
    public boolean isAngryAt(LivingEntity pTarget) {
        if (pTarget instanceof Player player && player.equals(this.getSummoner())) {
            return false;
        }
        return super.isAngryAt(pTarget);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity instanceof Player player) {
            return player.equals(this.getSummoner());
        }
        if (this.getSummoner() != null) {
            return pEntity.isAlliedTo(this.getSummoner());
        }
        return false;
    }

    public boolean isSlim() {
        PlayerInfo info = this.getPlayerInfo();
        return info != null && info.getSkin().model().equals(PlayerSkin.Model.SLIM);
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    @Override
    public void onUnSummon() {
        if (!level().isClientSide) {
            MagicManager.spawnParticles(level(), ParticleTypes.POOF, getX(), getY(), getZ(), 25, .4, .8, .4, .03, false);
            discard();
        }
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        IMagicSummon.super.onAntiMagic(playerMagicData);
    }

    @Override
    public boolean shouldIgnoreDamage(DamageSource damageSource) {
        return IMagicSummon.super.shouldIgnoreDamage(damageSource);
    }

    @Override
    public boolean isAlliedHelper(Entity entity) {
        return IMagicSummon.super.isAlliedHelper(entity);
    }

    @Override
    public void onDeathHelper() {
        IMagicSummon.super.onDeathHelper();
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        if (!super.shouldRender(pX, pY, pZ)) {
            return false;
        }
        // If the summoner is gone entirely, discard
        if (this.getSummoner() == null) {
            LOGGER.warn("[Simulacrum] shouldRender() summoner is gone — discarding.");
            if (level().isClientSide) discard();
            return false;
        }
        // PlayerInfo may not have resolved yet on the first few frames —
        // return false silently and wait rather than discarding the entity
        if (this.getPlayerInfo() == null) {
            LOGGER.debug("[Simulacrum] shouldRender() PlayerInfo not yet resolved, skipping frame.");
            return false;
        }
        return true;
    }

    public List<SpellSelectionManager.SelectionOption> getspelllist(Player player) {
        return new SpellSelectionManager(player).getAllSpells();
    }

    public ArrayList<AbstractSpell> simpleGetSpells(Player player) {
        var spells = this.getspelllist(player);
        ArrayList<AbstractSpell> listSpells = new ArrayList<>();
        spells.forEach(a -> {
            if (a.spellData.getSpell() != SpellRegistry.none()) {
                listSpells.add(a.spellData.getSpell());
            }
        });
        return listSpells;
    }

    public List<AbstractSpell> getOffensiveSpellsFromList(List<AbstractSpell> spells, Player player) {
        var list = new ArrayList<AbstractSpell>();
        for (var spell : spells) {
            SpellRegistry.REGISTRY.getHolder(spell.getSpellResource()).ifPresent(a -> {
                if (a.is(Tags.SpellTags.OFFENSIVE_SPELL)) list.add(spell);
            });
        }
        return list;
    }

    public List<AbstractSpell> getSupportSpells(List<AbstractSpell> spells, Player player) {
        var list = new ArrayList<AbstractSpell>();
        for (var spell : spells) {
            SpellRegistry.REGISTRY.getHolder(spell.getSpellResource()).ifPresent(a -> {
                if (a.is(Tags.SpellTags.DEFENSIVE_SPELL)) list.add(spell);
            });
        }
        return list;
    }

    public List<AbstractSpell> getDefensiveSpells(List<AbstractSpell> spells, Player player) {
        var list = new ArrayList<AbstractSpell>();
        for (var spell : spells) {
            SpellRegistry.REGISTRY.getHolder(spell.getSpellResource()).ifPresent(a -> {
                if (a.is(Tags.SpellTags.DEFENSIVE_SPELL)) list.add(spell);
            });
        }
        return list;
    }

    public List<AbstractSpell> getMovementSpells(List<AbstractSpell> spells, Player player) {
        var list = new ArrayList<AbstractSpell>();
        for (var spell : spells) {
            SpellRegistry.REGISTRY.getHolder(spell.getSpellResource()).ifPresent(a -> {
                if (a.is(Tags.SpellTags.MOUVEMENT_SPELL)) list.add(spell);
            });
        }
        return list;
    }

    public List<AbstractSpell> getUtilSpells(List<AbstractSpell> spells, Player player) {
        var list = new ArrayList<AbstractSpell>();
        for (var spell : spells) {
            SpellRegistry.REGISTRY.getHolder(spell.getSpellResource()).ifPresent(a -> {
                if (a.is(Tags.SpellTags.UTILITY_SPELL)) list.add(spell);
            });
        }
        return list;
    }

    protected void registerWizardGoals() {
        this.goalSelector.removeAllGoals(a -> a instanceof WizardAttackGoal || a instanceof WizardSupportGoal<?>);
        if (this.getSummoner() instanceof Player player) {
            this.goalSelector.addGoal(2, new WizardAttackGoal(this, 1.25f,
                    (int) (0.5 * 60 / this.getSummoner().getAttributeValue(AttributeRegistry.COOLDOWN_REDUCTION)),
                    (int) (60 / this.getSummoner().getAttributeValue(AttributeRegistry.COOLDOWN_REDUCTION)))
                    .setSpells(
                            getOffensiveSpellsFromList(simpleGetSpells(player), player),
                            getDefensiveSpells(simpleGetSpells(player), player),
                            getMovementSpells(simpleGetSpells(player), player),
                            getUtilSpells(simpleGetSpells(player), player))
                    .setSpellQuality(this.quality * 0.9f, this.quality));
            this.goalSelector.addGoal(2, new WizardSupportGoal<>(this, 1.25f, 100, 180)
                    .setSpells(getDefensiveSpells(simpleGetSpells(player), player), getUtilSpells(simpleGetSpells(player), player))
                    .setSpellQuality(this.quality * 0.75f, this.quality));
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return this.getSummoner().getDisplayName();
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new GenericFollowOwnerGoal(this, this::getSummoner, 0.9f, 8, 3, false, 25));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isHostileTowards));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, (mob) -> mob instanceof Enemy && !(mob instanceof Creeper)));
        this.targetSelector.addGoal(4, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isHostileTowards));
        this.targetSelector.addGoal(5, new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner()).setAlertOthers());
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new PatrolNearLocationGoal(this, 30, .75f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new WizardRecoverGoal(this));
    }

    public ResourceLocation getSkinTextureLocation() {
        PlayerInfo info = this.getPlayerInfo();
        LOGGER.debug("[Simulacrum] getSkinTextureLocation() | playerInfo={} | texture={}",
                info, info == null ? "NULL" : info.getSkin().texture());
        return info == null ? DefaultPlayerSkin.getDefaultTexture() : info.getSkin().texture();
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public PlayerInfo getPlayerInfo() {
        Player summoner = this.getSummoner();
        if (summoner == null) {
            LOGGER.error("[Simulacrum] getPlayerInfo() summoner is NULL!");
            return null;
        }
        if (this.playerInfo == null) {
            var connection = Minecraft.getInstance().getConnection();
            if (connection != null) {
                this.playerInfo = connection.getPlayerInfo(summoner.getUUID());
                LOGGER.debug("[Simulacrum] getPlayerInfo() resolved: {} for UUID {}",
                        this.playerInfo, summoner.getUUID());
            } else {
                LOGGER.error("[Simulacrum] getPlayerInfo() Minecraft connection is NULL!");
            }
        }
        return this.playerInfo;
    }

    @Override
    public @Nullable LivingEntity getSupportTarget() {
        Player summoner = this.getSummoner();
        if (summoner != null && this.getBoundingBox().inflate(20).intersects(summoner.getBoundingBox())) {
            this.helpTarget = summoner;
            return summoner;
        } else if (this.helpTarget == null) {
            AtomicReference<LivingEntity> target = new AtomicReference<>();
            this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(20)).forEach(a -> {
                if (summoner != null && a.isAlliedTo(summoner)) {
                    target.set(a);
                }
            });
            this.helpTarget = target.get();
            return target.get();
        }
        return this.helpTarget;
    }

    @Override
    public void setSupportTarget(LivingEntity target) {
        this.helpTarget = target;
    }
}