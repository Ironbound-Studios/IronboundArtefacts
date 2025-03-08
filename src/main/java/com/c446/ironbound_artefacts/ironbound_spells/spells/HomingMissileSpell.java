package com.c446.ironbound_artefacts.ironbound_spells.spells;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.entities.AbstractHomingEntity;
import com.c446.ironbound_artefacts.registries.IBEntitiesReg;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class HomingMissileSpell extends AbstractSpell {
    public ResourceLocation spellId = IronboundArtefact.prefix("homing_missiles");
    public static final DefaultConfig config = new DefaultConfig()
            .setAllowCrafting(true)
            .setCooldownSeconds(20 * 10)
            .setSchoolResource(SchoolRegistry.ENDER_RESOURCE)
            .setMaxLevel(1)
            .setMinRarity(SpellRarity.LEGENDARY)
            .build();

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    public HomingMissileSpell() {
        this.spellPowerPerLevel = 1;
        this.baseSpellPower = 3;
        this.baseManaCost = 100;
        this.manaCostPerLevel = 20;
        this.castTime = 0;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return null;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    public int getDistance(){
        return (50);
    }



//    @Override
//    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
//        var e = new AbstractHomingEntity(level, null, 10);
//        Vec3 start = entity.getEyePosition();
//        Vec3 end = entity.getLookAngle().normalize().scale(this.getDistance()).add(start);
//        var result = Utils.raycastForEntityOfClass(level, entity, start, end, true, LivingEntity.class);
//        if (result.getType().equals(HitResult.Type.ENTITY) && result instanceof EntityHitResult hitResult && hitResult.getEntity() instanceof LivingEntity living){
//            e.target = living;
//        }
//
//        level.addFreshEntity(e);
//
//        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
//    }
}
