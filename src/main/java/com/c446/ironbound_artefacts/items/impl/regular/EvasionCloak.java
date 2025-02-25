package com.c446.ironbound_artefacts.items.impl.regular;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class EvasionCloak extends CurioBaseItem {
    public EvasionCloak(Properties properties) {
        super(properties);
    }

    int cooldown = 0;

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if ((slotContext.entity().tickCount - slotContext.entity().getLastHurtMobTimestamp()>= 20*4) && cooldown < 0){
            slotContext.entity().addEffect(new MobEffectInstance(MobEffectRegistry.EVASION, 200, 0));
            cooldown = 20*6;
        }
        cooldown --;
        super.curioTick(slotContext, stack);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var attr = super.getAttributeModifiers(slotContext, id, stack);
        attr.put(Attributes.ARMOR,new AttributeModifier(IronboundArtefact.prefix("protection_cloak"), 2.5, AttributeModifier.Operation.ADD_VALUE));

        return attr;
    }
}
