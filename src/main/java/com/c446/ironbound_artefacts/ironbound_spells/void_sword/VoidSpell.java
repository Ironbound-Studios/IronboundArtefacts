package com.c446.ironbound_artefacts.ironbound_spells.void_sword;

import com.c446.ironbound_artefacts.registries.DamageSourcesReg;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public abstract class VoidSpell extends AbstractSpell {
    ResourceKey<DamageType> damageLoc(){
        return DamageSourcesReg.VOID_SWORD_;
    }

    @Override
    public SpellDamageSource getDamageSource(Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker);
    }

    @Override
    public float getSpellPower(int spellLevel, @Nullable Entity sourceEntity) {
        return this.spellPowerPerLevel*spellLevel + this.baseSpellPower;
    }

//    abstract int willCost();
//
//    public void applyCosts(LivingEntity caster){
//
//    }
//
//    @Override
//    public CastResult canBeCastedBy(int spellLevel, CastSource castSource, MagicData playerMagicData, Player player) {
//        var res = super.canBeCastedBy(spellLevel, castSource, playerMagicData, player);
//        var mana = player.getData(AttachmentRegistry.WILL).getCurrentWill() >= this.willCost();
//        if (res.isSuccess() && mana){
//            // success
//        }
//    }
}
