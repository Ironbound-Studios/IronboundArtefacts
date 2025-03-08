package com.c446.ironbound_artefacts.items.armor.arcane_weave;

import com.c446.ironbound_artefacts.IronboundArtefact;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class ArcaneWeaveModel extends DefaultedItemGeoModel<ArcaneWeaveItem> {

    public ArcaneWeaveModel() {
        super(IronboundArtefact.prefix(""));
    }

    @Override
    public ResourceLocation getModelResource(ArcaneWeaveItem object) {


        return IronboundArtefact.prefix("geo/arcane_weave.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ArcaneWeaveItem object) {
        return IronboundArtefact.prefix("textures/armor/arcane_weave.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ArcaneWeaveItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "animations/wizard_armor_animation.json");
    }
//    public static String listOfBonesToString(List<IBone> list){
//        String s = "";
//        for (IBone o:list)
//            s += o.getName()+", ";
//        return s;
//    }
}