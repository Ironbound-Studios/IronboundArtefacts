package com.c446.ironbound_artefacts.items.impl.regular;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class EvasionCloak extends CurioBaseItem {
    private int cooldown = 20 * 6;

    public EvasionCloak(Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        int ticksSinceLastHit = slotContext.entity().tickCount - slotContext.entity().getLastHurtMobTimestamp();

        if (ticksSinceLastHit >= 20 * 6 && cooldown <= 0) { // Ensure at least 6 seconds have passed
            if (slotContext.entity() != null && slotContext.entity().level() instanceof ServerLevel srLvl) {
                slotContext.entity().addEffect(new MobEffectInstance(MobEffectRegistry.EVASION, 200, 1));
                cooldown = 20 * 6; // Reset cooldown to prevent immediate reapplication
                if (slotContext.entity() instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.literal("you can use the thing now bozo")));
                }
            }
            if (cooldown > 0) {
                cooldown--;
            }
        }

        super.curioTick(slotContext, stack);
    }


    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var attr = super.getAttributeModifiers(slotContext, id, stack);
        attr.put(Attributes.ARMOR, new AttributeModifier(IronboundArtefact.prefix("protection_cloak"), 2.5, AttributeModifier.Operation.ADD_VALUE));

        return attr;
    }
}
