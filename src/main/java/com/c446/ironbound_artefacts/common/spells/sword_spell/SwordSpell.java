package com.c446.ironbound_artefacts.common.spells.sword_spell;

import com.c446.ironbound_artefacts.IBA;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SwordSpell extends AbstractSpell {
    ResourceLocation loc = IBA.p("jce");

    @Override
    public ResourceLocation getSpellResource() {
        return loc;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return null;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
        //mod.chloeprime.aaaparticles.client.render.EffekRenderer.renderWorldEffeks();
        

    }


}
