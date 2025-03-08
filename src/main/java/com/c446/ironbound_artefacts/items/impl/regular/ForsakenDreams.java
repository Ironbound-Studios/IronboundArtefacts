package com.c446.ironbound_artefacts.items.impl.regular;

import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class ForsakenDreams extends CurioBaseItem {
    public ForsakenDreams(Properties properties) {
        super(properties);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        super.onEquip(slotContext, prevStack, stack);
    }


//    public void initBar(ItemStack stack, SlotContext slotContext) {
//        if (slotContext.entity() != null) {
//            this.bossLoc = ResourceLocation.parse(IronboundArtefact.prefix("boss_loc") + slotContext.entity().getStringUUID());
//            if (slotContext.entity().getServer() != null) {
//                slotContext.entity().getServer().getCustomBossEvents().getEvents().forEach(
//                        e -> {
//                            if (e != null && e.getTextId().equals(this.bossLoc)) {
//                                slotContext.entity().getServer().getCustomBossEvents().remove(e);
//                            }
//                        }
//                );
//            }
//            this.bar = new CustomBossEvent(bossLoc, Component.empty());
//            stack.set(ComponentRegistry.UUID_DATA_COMPONENT, new GenericUUIDComponent(slotContext.entity().getStringUUID()));
//            bar.setColor(BossEvent.BossBarColor.RED);
//            bar.setOverlay(BossEvent.BossBarOverlay.PROGRESS);
//        }
//    }

//    public void refreshBar(ItemStack stack, SlotContext context) {
//        if (context.entity() != null) {
//            double health = context.entity().getHealth();
//            double maxHealth = context.entity().getMaxHealth();
//            if (maxHealth > 0) { // Prevent division by zero
//                this.bar.setProgress((float) (health / maxHealth));
//            }
//        }
//    }

//    @Override
//    public void curioTick(SlotContext slotContext, ItemStack stack) {
//        if (slotContext.entity().tickCount % 20 == 0) {
//            if (stack.has(ComponentRegistry.UUID_DATA_COMPONENT)) {
//                if (slotContext.entity() instanceof ServerPlayer player && player.level() instanceof ServerLevel) {
//                    this.bossLoc = ResourceLocation.parse(IronboundArtefact.prefix("boss_loc") + stack.get(ComponentRegistry.UUID_DATA_COMPONENT).uuid());
//
//                    if (bar == null) {
//                        initBar(stack, slotContext);
//                    }
//
//                    var players = player.level().getEntitiesOfClass(ServerPlayer.class, player.getBoundingBox().inflate(100D, 100D, 100D), serverPlayer -> true);
//                    players.forEach(p -> {
//                        bar.addPlayer(p);
//                        IronboundArtefact.LOGGER.debug("attempting to add {}", p.getName());
//                    });
//                    this.bar.setMax((int) player.getMaxHealth());
//                    double maxHealth = player.getMaxHealth();
//                    if (maxHealth > 0) { // Prevent division by zero
//                        this.bar.setProgress((float) (player.getHealth() / maxHealth));
//                    }
//                }
//            }
//            else {
//                if (slotContext.entity() != null) {
//                    stack.set(ComponentRegistry.UUID_DATA_COMPONENT, new GenericUUIDComponent(slotContext.entity().getStringUUID()));
//                    initBar(stack, slotContext);
//                }
//            }
//            if (bar != null) {
//                refreshBar(stack,slotContext);
//            }
//        }
//        super.curioTick(slotContext, stack);
//    }


    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var attributes = super.getAttributeModifiers(slotContext, id, stack);
        attributes.put(AttributeRegistry.ELDRITCH_SPELL_POWER, new AttributeModifier(id, 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return attributes;
    }
}
