package com.c446.ironbound_artefacts.ironbound_spells.spells;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.components.UniversalPositionComponent;
import com.c446.ironbound_artefacts.datagen.Tags;
import com.c446.ironbound_artefacts.items.impl.lore_items.Phylactery;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.registries.*;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Objects;

import static io.redspace.ironsspellbooks.api.registry.AttributeRegistry.MAX_MANA;

@AutoSpellConfig
public class WishSpell extends AbstractSpell {
    private final ResourceLocation spellId = IronboundArtefact.prefix("wish");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE)
            .setMaxLevel(3)
            .setCooldownSeconds(600)
            .build();

    public WishSpell() {
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.baseManaCost = 250;
        this.manaCostPerLevel = 250;
        this.castTime = 60;
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
        return CastType.INSTANT;
    }

    @Override
    public float getSpellPower(int spellLevel, @Nullable Entity sourceEntity) {
        return spellLevel;
    }

    protected void consumeOneFromStack(ItemStack stack) {
        stack.setCount(stack.getCount() - 1);
    }

    public boolean applyItemEffect(ItemStack item, LivingEntity entity, int spellLevel, Level level, CastSource castSource) {
        ServerPlayer serverPlayer = entity instanceof ServerPlayer ? (ServerPlayer) entity : null;
        if (serverPlayer != null && item.is(ItemRegistry.ARCANE_ESSENCE.get())) {
            return applyEffect(entity, EffectsRegistry.MANA_REGEN, spellLevel, item);
        } else if (item.is(Items.PHANTOM_MEMBRANE)) {
            return applyEffect(entity, MobEffects.SLOW_FALLING, spellLevel, item);
        } else if (item.is(Items.MAGMA_CREAM)) {
            return applyEffect(entity, MobEffects.FIRE_RESISTANCE, spellLevel, item);
        } else if (item.is(Items.GOLD_BLOCK)) {
            return applyEffect(entity, MobEffects.DAMAGE_RESISTANCE, spellLevel, 4, item);
        } else if (item.is(Items.GHAST_TEAR)) {
            return applyEffect(entity, MobEffects.REGENERATION, spellLevel, item);
        } else if (item.has(ComponentRegistry.SPELL_CONTAINER)) {
            return handleSpellContainer(item, entity, serverPlayer, spellLevel, level, castSource);
        } else if (item.is(Items.ZOMBIE_HEAD) && item.getCount() > 3) {
            return applySummonEffects(level, entity, spellLevel);
        } else if (item.is(Items.WITHER_SKELETON_SKULL)) {
            return handleWitherSkull(level, entity);
        } else if (item.is(Tags.ItemTags.WISH_DUPLICABLE)) {
            return handleWishDuplicable(item);
        }
        return false;
    }

    private boolean applyEffect(LivingEntity entity, Holder<MobEffect> effect, int spellLevel, ItemStack item) {
        entity.addEffect(new MobEffectInstance(effect, 20 * 60 * 10 * spellLevel, (int) (1 / 2F * this.getSpellPower(spellLevel, entity))));
        consumeOneFromStack(item);
        return true;
    }

    private boolean applyEffect(LivingEntity entity, Holder<MobEffect> effect, int spellLevel, int maxStrength, ItemStack item) {
        entity.addEffect(new MobEffectInstance(effect, 20 * 60 * 10 * spellLevel, Math.min(maxStrength, (int) (1 / 2F * this.getSpellPower(spellLevel, entity)))));
        consumeOneFromStack(item);
        return true;
    }

    private boolean handleSpellContainer(ItemStack item, LivingEntity entity, ServerPlayer serverPlayer, int spellLevel, Level level, CastSource castSource) {
        IronboundArtefact.LOGGER.debug("found spell container.");
        var spellContainer = item.get(ComponentRegistry.SPELL_CONTAINER);
        if (spellContainer != null && item.is(ItemRegistry.SCROLL.get())) {
            var spells = spellContainer.getActiveSpells();
            if (spells != null && !spells.isEmpty()) {
                var num = level.random.nextIntBetweenInclusive(0, spells.size() - 1);
                var selectedSpell = spells.get(num);
                if (selectedSpell != null && selectedSpell.getSpell() != null && !Objects.equals(selectedSpell.getSpell().getSpellId(), this.getSpellId())) {
                    int spellLevel1 = selectedSpell.getLevel() + spellLevel;
                    System.out.println("casting spell " + selectedSpell.getSpell().getComponentId() + " at level : " + spellLevel1);
                    selectedSpell.getSpell().castSpell(
                            level,
                            spellLevel1,
                            (ServerPlayer) entity,
                            castSource,
                            false
                    );
                    return true;
                }
            }
        }
        return false;
    }

    private boolean applySummonEffects(Level level, LivingEntity entity, int spellLevel) {
        var entities = level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(150), a -> a instanceof IMagicSummon);
        for (LivingEntity l : entities) {
            if (l instanceof IMagicSummon s) {
                if (s.getSummoner().equals(entity)) {
                    l.addEffect(new MobEffectInstance(MobEffectRegistry.HASTENED, 60 * spellLevel, spellLevel));
                    l.addEffect(new MobEffectInstance(MobEffectRegistry.CHARGED, 60 * spellLevel, spellLevel));
                }
            }
        }
        return true;
    }

    private boolean handleWitherSkull(Level level, LivingEntity entity) {
        var result = Utils.raycastForEntity(level, entity, 150, true);
        if (result instanceof EntityHitResult result1 && result1.getEntity() instanceof LivingEntity l) {
            l.hurt(l.damageSources().wither(), l.getMaxHealth() * 2);
            return true;
        }
        return false;
    }

    private boolean handleWishDuplicable(ItemStack item) {
        var newMax = item.getCount() * 2;
        if (newMax > 64) {
            item.setCount(64);
        } else {
            item.setCount(newMax);
        }
        return true;
    }


    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        applyItemEffect(entity.getOffhandItem(), entity, spellLevel, level, castSource);
        applyItemEffect(entity.getMainHandItem(), entity, spellLevel, level, castSource);
        entity.addEffect(new MobEffectInstance(EffectsRegistry.VOID_POISON, 100, 4));

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
