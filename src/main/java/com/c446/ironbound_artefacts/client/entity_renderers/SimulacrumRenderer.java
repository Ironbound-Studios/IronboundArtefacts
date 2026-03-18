package com.c446.ironbound_artefacts.client.entity_renderers;

import com.c446.ironbound_artefacts.client.models.SimulacrumModel;
import com.c446.ironbound_artefacts.common.entities.SimulacrumEntity;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

public class SimulacrumRenderer extends AbstractSpellCastingMobRenderer {

    // SimulacrumRenderer.java — add to the top of the class
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SimulacrumRenderer.class);

    public SimulacrumRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SimulacrumModel());
    }

    // In getTextureLocation()
    @Override
    public ResourceLocation getTextureLocation(AbstractSpellCastingMob entity) {
        LOGGER.debug("[SimulacrumRenderer] getTextureLocation() | entity={} | isSimulacrum={}",
                entity, entity instanceof SimulacrumEntity);

        if (entity instanceof SimulacrumEntity simulacrum) {
            ResourceLocation skin = simulacrum.getSkinTextureLocation();
            LOGGER.debug("[SimulacrumRenderer] getTextureLocation() | skin={} | summoner={} | playerInfo={}",
                    skin,
                    simulacrum.getSummoner(),
                    simulacrum.getPlayerInfo());
            return skin != null ? skin : DefaultPlayerSkin.getDefaultTexture();
        }
        LOGGER.error("[SimulacrumRenderer] getTextureLocation() entity was not a SimulacrumEntity! Got: {}",
                entity.getClass().getName());
        return DefaultPlayerSkin.getDefaultTexture();
    }
}