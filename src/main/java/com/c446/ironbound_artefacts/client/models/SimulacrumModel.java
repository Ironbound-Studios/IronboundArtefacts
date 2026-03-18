package com.c446.ironbound_artefacts.client.models;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.common.entities.SimulacrumEntity;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

public class SimulacrumModel extends AbstractSpellCastingMobModel {

    // No transformStack redeclaration — use the parent's field directly

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return AbstractSpellCastingMob.modelResource;
    }

    /**
     * Single authoritative texture override. Safely casts since SimulacrumModel
     * is only ever registered for SimulacrumEntity. Falls back to the default
     * player skin while PlayerInfo is still resolving on the client.
     */
    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob mob) {
        if (mob instanceof SimulacrumEntity simulacrum) {
            ResourceLocation skin = simulacrum.getSkinTextureLocation();
            return skin != null ? skin : DefaultPlayerSkin.getDefaultTexture();
        }
        return DefaultPlayerSkin.getDefaultTexture();
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractSpellCastingMob animatable) {
        return AbstractSpellCastingMob.animationInstantCast;
    }

    // bobBone removed — the parent's float-offset signature is the correct one
    // and the parent's transformStack is what the animation pipeline actually reads
}