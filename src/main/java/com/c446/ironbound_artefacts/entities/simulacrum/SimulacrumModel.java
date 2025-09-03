package com.c446.ironbound_artefacts.entities.simulacrum;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.TransformStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.GeoBone;

public class SimulacrumModel extends AbstractSpellCastingMobModel {
    protected TransformStack transformStack = new TransformStack();

    public ResourceLocation getModelResource() {
        return AbstractSpellCastingMob.modelResource;
    }

    public ResourceLocation getTextureResource(@NotNull SimulacrumEntity var1) {
        return var1.getSkinTextureLocation();
    }

    public ResourceLocation getAnimationResource(SimulacrumEntity animatable) {
        return AbstractSpellCastingMob.animationInstantCast;
    }

    protected void bobBone(GeoBone bone, int offset, float multiplier) {
        float z = multiplier * (Mth.cos((float) offset * 0.09F) * 0.05F + 0.05F);
        float x = multiplier * Mth.sin((float) offset * 0.067F) * 0.05F;
        this.transformStack.pushRotation(bone, x, 0.0F, z);
    }

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob mob) {
        return ((SimulacrumEntity) mob).getSkinTextureLocation();
    }
}
