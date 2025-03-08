package com.c446.ironbound_artefacts.entities.archmage;

import com.c446.ironbound_artefacts.registries.CustomSpellRegistry;
import com.c446.ironbound_artefacts.registries.IBEntitiesReg;
import com.c446.ironbound_artefacts.registries.ItemRegistry;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.SpellBarrageGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardRecoverGoal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArchmageEntity extends AbstractSpellCastingMob implements Enemy {
    public ArchmageEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.registerGoals();
    }

    public ArchmageEntity(Level level) {
        this(IBEntitiesReg.ARCHMAGE.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        var attr = Player.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 30D)
                .add(Attributes.MOVEMENT_SPEED, .25f)
                .add(Attributes.MAX_HEALTH, 600)
                .add(AttributeRegistry.LIGHTNING_SPELL_POWER, 2f)
                .add(AttributeRegistry.FIRE_SPELL_POWER, 2f)
                .add(AttributeRegistry.ICE_SPELL_POWER, 2f)
                .add(AttributeRegistry.NATURE_SPELL_POWER, 2f)
                .add(AttributeRegistry.ELDRITCH_SPELL_POWER, 2f)
                .add(AttributeRegistry.SPELL_POWER, 1.5f)
                .add(AttributeRegistry.SPELL_RESIST, 1.5f)
                .add(AttributeRegistry.CAST_TIME_REDUCTION, 1.5f)
                .add(AttributeRegistry.CASTING_MOVESPEED, 1.5f);

        for (var attribute : BuiltInRegistries.ATTRIBUTE.registryKeySet()) {
            if (!attr.hasAttribute(BuiltInRegistries.ATTRIBUTE.getHolderOrThrow(attribute))) {
                var holder = BuiltInRegistries.ATTRIBUTE.getHolderOrThrow(attribute);
                attr.add(holder, holder.value().getDefaultValue());
            }
        }
        return attr;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {
        var stuff = super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
        RandomSource randomsource = Utils.random;
        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
        return stuff;
    }

    //    public HashMap<TagKey<AbstractSpell>, List<AbstractSpell>> getSpellsFor(SchoolType schoolType) {
//        List<AbstractSpell> simpleSpells = new ArrayList<>();
//        List<AbstractSpell> barrageSpells = new ArrayList<>();
//        List<AbstractSpell> singleSpells = new ArrayList<>();
//        for (var thing : SpellRegistry.REGISTRY.entrySet()) {
//            var key = SpellRegistry.REGISTRY.getHolderOrThrow(thing.getKey());
//            var spell = thing.getValue();
//            if (spell.getSchoolType().equals(schoolType)) {
//                if (key.is(Tags.SpellTags.ARCHMAGE_ALLOWED_SPELL)   ) {
//                    simpleSpells.add(spell);
//                    // add
//                } else if (key.is(Tags.SpellTags.ARCHMAGE_BARRAGE_SPELL)) {
//                    barrageSpells.add(spell);
//                } else if (key.is(Tags.SpellTags.ARCHMAGE_SINGLE_SPELL)) {
//                    singleSpells.add(spell);
//                }
//            }
//        }
//        HashMap<TagKey<AbstractSpell>, List<AbstractSpell>> spells = new HashMap<>();
//        spells.put(Tags.SpellTags.ARCHMAGE_BARRAGE_SPELL, barrageSpells);
//        spells.put(Tags.SpellTags.ARCHMAGE_SINGLE_SPELL, singleSpells);
//        spells.put(Tags.SpellTags.ARCHMAGE_ALLOWED_SPELL, simpleSpells);
//        return spells;
//    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistry.ARCHMAGE_HEAD.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ItemRegistry.ARCHMAGE_CHEST.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ItemRegistry.ARCHMAGE_LEG.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemRegistry.ARCHMAGE_BOOTS.get()));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.STAFF_OF_POWER.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 1f);
        this.setDropChance(EquipmentSlot.HEAD, 0f);
        this.setDropChance(EquipmentSlot.CHEST, 0f);
        this.setDropChance(EquipmentSlot.LEGS, 0f);
        this.setDropChance(EquipmentSlot.FEET, 0f);
    }

    public void setSchoolGoal() {
        this.goalSelector.addGoal(1, new WizardAttackGoal(this, 1.25f, 10, 30).setSpellQuality(1, 1.3f).setSpells(
                        List.of(SpellRegistry.LIGHTNING_LANCE_SPELL.get(),
                                SpellRegistry.LIGHTNING_LANCE_SPELL.get(),
                                SpellRegistry.LIGHTNING_LANCE_SPELL.get(),
                                SpellRegistry.CHAIN_LIGHTNING_SPELL.get(),
                                SpellRegistry.CHAIN_LIGHTNING_SPELL.get(),
                                SpellRegistry.CHAIN_LIGHTNING_SPELL.get(),
                                SpellRegistry.CHAIN_LIGHTNING_SPELL.get(),
                                SpellRegistry.BALL_LIGHTNING_SPELL.get(),
                                SpellRegistry.BALL_LIGHTNING_SPELL.get(),
                                SpellRegistry.FIREBOLT_SPELL.get(),
                                SpellRegistry.SCULK_TENTACLES_SPELL.get(),
                                SpellRegistry.SCULK_TENTACLES_SPELL.get(),
                                SpellRegistry.SONIC_BOOM_SPELL.get(),
                                CustomSpellRegistry.TIME_STOP.get(),
                                CustomSpellRegistry.TIME_STOP.get(),
                                SpellRegistry.ROOT_SPELL.get(),
                                SpellRegistry.HEAT_SURGE_SPELL.get(),
                                SpellRegistry.COUNTERSPELL_SPELL.get(),
                                SpellRegistry.COUNTERSPELL_SPELL.get()
                        ),
                        List.of(SpellRegistry.ABYSSAL_SHROUD_SPELL.get(),
                                SpellRegistry.GREATER_HEAL_SPELL.get(),
                                SpellRegistry.GREATER_HEAL_SPELL.get(),
                                SpellRegistry.CHARGE_SPELL.get(),
                                SpellRegistry.HASTE_SPELL.get()),
                        List.of(),
                        List.of()
                )
        );
        this.goalSelector.addGoal(1, new WizardAttackGoal(this, 1.25f, 10, 50)
                .setSpells(
                        List.of(),
                        List.of(),
                        List.of(SpellRegistry.BLOOD_STEP_SPELL.get()),
                        List.of()
                ).setSpellQuality(.4f, .8f)
        );
        this.goalSelector.addGoal(1, new SpellBarrageGoal(this, SpellRegistry.LIGHTNING_BOLT_SPELL.get(), 5, 7, 20, 40, 5));
        this.goalSelector.addGoal(1, new SpellBarrageGoal(this, SpellRegistry.ELDRITCH_BLAST_SPELL.get(), 8, 12, 10, 20, 13));
        this.goalSelector.addGoal(1, new SpellBarrageGoal(this, SpellRegistry.BALL_LIGHTNING_SPELL.get(), 10, 15, 20, 50, 10));
        this.goalSelector.addGoal(1, new SpellBarrageGoal(this, SpellRegistry.FIREBOLT_SPELL.get(), 10, 15, 10, 20, 15));
        this.goalSelector.addGoal(2, new WizardRecoverGoal(this));
    }

    protected void registerGoals() {
        this.goalSelector.removeAllGoals((goal) -> true);
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractIllager.class, true));
        this.setSchoolGoal();
    }

}
