package com.c446.ironbound_artefacts.items.armor.bishop_armor;

import com.c446.ironbound_artefacts.items.ModArmorMaterial;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BishopSetItem extends ImbuableChestplateArmorItem {
    public BishopSetItem(Type type, Properties settings) {
        super(ModArmorMaterial.ARCANE_WEAVE_MATERIAL, type, settings, arcaneWeaveAttribute());
    }

    public static AttributeContainer[] arcaneWeaveAttribute() {
        return new AttributeContainer[]{new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE), new AttributeContainer(AttributeRegistry.HOLY_SPELL_POWER, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)};
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return new GenericCustomArmorRenderer<>(new BishopSetModel());
    }
}