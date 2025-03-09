package com.c446.ironbound_artefacts.entities.electromancer;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.PatrolNearLocationGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class ElectromancerEntity extends AbstractSpellCastingMob {
    protected ElectromancerEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 100;
    }

    public static AttributeSupplier.Builder prepareAttribute() {
        return LivingEntity.createLivingAttributes()
                .add(AttributeRegistry.LIGHTNING_SPELL_POWER, 2.5)
                .add(Attributes.MAX_HEALTH, 120)
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5)
                .add(Attributes.MOVEMENT_SPEED, .25)
                .add(Attributes.FOLLOW_RANGE, 32);
    }

    public boolean isHostileTowards(LivingEntity e) {
        return this.isAlliedTo(e);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistry.ELECTROMANCER_HELMET));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ItemRegistry.ELECTROMANCER_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ItemRegistry.ELECTROMANCER_LEGGINGS));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemRegistry.ELECTROMANCER_BOOTS));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.LIGHTNING_ROD_STAFF));
        this.setDropChance(EquipmentSlot.HEAD, 0);
        this.setDropChance(EquipmentSlot.CHEST, 0);
        this.setDropChance(EquipmentSlot.LEGS, 0);
        this.setDropChance(EquipmentSlot.FEET, 0);
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, (new WizardAttackGoal(this, 1.25F, 25, 50)).setSpells(List.of(SpellRegistry.BALL_LIGHTNING_SPELL.get(), SpellRegistry.BALL_LIGHTNING_SPELL.get(), SpellRegistry.BALL_LIGHTNING_SPELL.get(), SpellRegistry.LIGHTNING_BOLT_SPELL.get(), SpellRegistry.LIGHTNING_LANCE_SPELL.get()), List.of(SpellRegistry.COUNTERSPELL_SPELL.get()), List.of(SpellRegistry.BURNING_DASH_SPELL.get()), List.of()).setDrinksPotions().setSpellQuality(0.75f, 1f));
        this.goalSelector.addGoal(3, new PatrolNearLocationGoal(this, 30.0F, 0.75F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new WizardRecoverGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isHostileTowards));
    }
}
