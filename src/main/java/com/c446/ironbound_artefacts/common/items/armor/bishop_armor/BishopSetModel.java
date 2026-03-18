package com.c446.ironbound_artefacts.common.items.armor.bishop_armor;

import com.c446.ironbound_artefacts.IBA;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class BishopSetModel extends DefaultedItemGeoModel<BishopSetItem> {

    public BishopSetModel() {
        super(IBA.p(""));
    }

    @Override
    public ResourceLocation getModelResource(BishopSetItem object) {


        return IBA.p("geo/priest_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BishopSetItem object) {
        return IBA.p("textures/armor/arcane_weave.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BishopSetItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "animations/wizard_armor_animation.json");
    }
//    public static String listOfBonesToString(List<IBone> list){
//        String s = "";
//        for (IBone o:list)
//            s += o.getName()+", ";
//        return s;
//    }
}