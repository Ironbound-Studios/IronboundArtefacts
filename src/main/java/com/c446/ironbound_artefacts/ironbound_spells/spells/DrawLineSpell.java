package com.c446.ironbound_artefacts.ironbound_spells.spells;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import com.c446.lines_and_particles_lib.Drawer;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@AutoSpellConfig
public class DrawLineSpell extends AbstractSpell {
    private final ResourceLocation loc = IronboundArtefact.prefix("draw_line");

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.ENDER_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(1)
            .build();

    public DrawLineSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 4;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 30;
    }


    @Override
    public ResourceLocation getSpellResource() {
        return loc;
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (level instanceof ServerLevel serLvl && entity instanceof ServerPlayer player){

            var ang = entity.getLookAngle();
            System.out.println("x " + ang.x() +" y "+   ang.y() + " z " + ang.z());
            //System.out.println("x rot " + entity.getXRot() + " y rot : "  + entity.getYRot());

            var start = entity.getEyePosition().add(entity.getLookAngle().yRot(-45*Mth.DEG_TO_RAD).scale(3));
            var end = entity.getEyePosition().add(entity.getLookAngle().yRot(45*Mth.DEG_TO_RAD).scale(3));

            //serLvl.sendParticles(player, )

            System.out.println(start);
            System.out.println(end);

            Drawer.drawLine(serLvl, start, end, EffectsRegistry.rgbToInt(0,160,200), 20*10,20*30,0);
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
