package com.c446.ironbound_artefacts.effects;

import com.c446.ironbound_artefacts.entities.force_cage.CorpseMountain;
import com.c446.ironbound_artefacts.entities.force_cage.CorpseMountainSpell;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.List;

public class CorpseMountainTargettedEffect extends MobEffectInstance {
    List<CorpseMountain> ORIGINS = new ArrayList<>();

    public List<CorpseMountain> getMountains() {
        return ORIGINS;
    }

    public CorpseMountainTargettedEffect(Holder<MobEffect> effect, int duration) {
        super(effect, duration);
    }

    public CorpseMountainTargettedEffect addMountain(CorpseMountain mountain){
        if (!(this.ORIGINS.contains(mountain)))this.ORIGINS.add(mountain);
        return this;
    }
}
