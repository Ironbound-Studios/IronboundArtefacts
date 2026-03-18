package com.c446.ironbound_artefacts.client.entity_renderers;

import com.c446.ironbound_artefacts.client.models.ArchmageModel;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ArchmageRenderer extends AbstractSpellCastingMobRenderer {
    public ArchmageRenderer(EntityRendererProvider.Context renderManager, AbstractSpellCastingMobModel model) {
        super(renderManager, new ArchmageModel());
    }
}
