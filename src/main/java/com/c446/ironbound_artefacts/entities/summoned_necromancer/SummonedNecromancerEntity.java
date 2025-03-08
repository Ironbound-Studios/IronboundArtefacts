package com.c446.ironbound_artefacts.entities.summoned_necromancer;

import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.necromancer.NecromancerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;

public class SummonedNecromancerEntity extends NecromancerEntity implements IMagicSummon {
    public LivingEntity summoner;

    public SummonedNecromancerEntity(EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public LivingEntity getSummoner() {
        return summoner;
    }

    public void setSummoner(LivingEntity summoner) {
        this.summoner = summoner;
    }

    @Override
    public void onUnSummon() {
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }
}
