package com.c446.ironbound_artefacts.entities.archmage;

import com.c446.ironbound_artefacts.IronboundArtefact;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import io.redspace.ironsspellbooks.entity.mobs.wizards.pyromancer.PyromancerModel;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.resources.ResourceLocation;

public class ArchmageModel extends AbstractSpellCastingMobModel {
    public ArchmageModel() {}
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(IronboundArtefact.MODID, "textures/entities/archmage_skin.png");

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob abstractSpellCastingMob) {

        return TEXTURE;
    }
}
