package com.c446.ironbound_artefacts.common.items.armor.archmagi_weave;

import com.c446.ironbound_artefacts.IBA;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class ArchmagiWeaveModel extends DefaultedItemGeoModel<ArchmagiWeaveItem> {

    public ArchmagiWeaveModel() {
        super(IBA.p(""));
    }

    @Override
    public ResourceLocation getModelResource(ArchmagiWeaveItem object) {


        return IBA.p("geo/archmage_weave.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ArchmagiWeaveItem object) {
        return IBA.p("textures/armor/archmage_weave.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ArchmagiWeaveItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "animations/wizard_armor_animation.json");
    }
}