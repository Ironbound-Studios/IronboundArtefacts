package com.c446.ironbound_artefacts.entities.force_cage;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.IBEntitiesReg;
import com.c446.ironbound_artefacts.registries.SchoolTypesRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.ChangeManaEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Arrays;

public class CorpseMountainSpell extends AbstractSpell {
    ResourceLocation id= IronboundArtefact.prefix("corpse_mountain");
    DefaultConfig defaultCfg = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setCooldownSeconds(600)
            .setAllowCrafting(true)
            .setMaxLevel(1)
            .setSchoolResource(SchoolTypesRegistry.IMMORTAL_ART_LOC);

    @Override
    public ResourceLocation getSpellResource() {
        return id;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultCfg;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }


    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (entity instanceof ServerPlayer serverPlayer && level instanceof ServerLevel serverLevel){

            var m = new CorpseMountain(IBEntitiesReg.CORPSE_MOUNTAIN.get(), serverLevel, serverPlayer);
            var pos = serverPlayer.getEyePosition();
            pos.subtract(0, serverPlayer.getEyeHeight(),0);
            m.setPos(pos);
            serverLevel.addFreshEntity(m);
        }



        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
