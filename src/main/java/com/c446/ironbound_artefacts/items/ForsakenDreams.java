package com.c446.ironbound_artefacts.items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.components.GenericUUIDComponent;
import com.c446.ironbound_artefacts.registries.ComponentRegistry;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ForsakenDreams extends CurioBaseItem {
    private ResourceLocation bossLoc;


    //Component.empty(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true).setCreateWorldFog(true);
    private CustomBossEvent bar;

    public ForsakenDreams(Properties properties) {
        super(properties);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        initBar(stack, slotContext);
        super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (Objects.requireNonNull(slotContext.entity().level().getServer()).getCustomBossEvents().getEvents().contains(this.bar))
            Objects.requireNonNull(slotContext.entity().level().getServer()).getCustomBossEvents().remove(Objects.requireNonNull(slotContext.entity().level().getServer().getCustomBossEvents().get(bossLoc)));
        super.onUnequip(slotContext, newStack, stack);
    }

    public void refreshBar(ItemStack stack, SlotContext context) {
        if (context.entity() != null) {
            double health = context.entity().getHealth();
            double maxHealth = context.entity().getMaxHealth();
            if (maxHealth > 0) { // Prevent division by zero
                this.bar.setProgress((float) (health / maxHealth));
            }
        }
    }

    public void initBar(ItemStack stack, SlotContext slotContext) {
        if (slotContext.entity() != null) {
            this.bossLoc = ResourceLocation.parse(IronboundArtefact.prefix("boss_loc") + slotContext.entity().getStringUUID());
            if (slotContext.entity().getServer() != null) {
                slotContext.entity().getServer().getCustomBossEvents().getEvents().forEach(
                        e -> {
                            if (e != null && e.getTextId().equals(this.bossLoc)) {
                                slotContext.entity().getServer().getCustomBossEvents().remove(e);
                            }
                        }
                );
            }
            this.bar = new CustomBossEvent(bossLoc, Component.empty());
            stack.set(ComponentRegistry.UUID_DATA_COMPONENT, new GenericUUIDComponent(slotContext.entity().getStringUUID()));
            bar.setColor(BossEvent.BossBarColor.RED);
            bar.setOverlay(BossEvent.BossBarOverlay.PROGRESS);
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity().tickCount % 20 == 0) {
            if (stack.has(ComponentRegistry.UUID_DATA_COMPONENT)) {
                if (slotContext.entity() instanceof ServerPlayer player && player.level() instanceof ServerLevel) {
                    this.bossLoc = ResourceLocation.parse(IronboundArtefact.prefix("boss_loc") + stack.get(ComponentRegistry.UUID_DATA_COMPONENT).uuid());
    
                    if (bar == null) {
                        initBar(stack, slotContext);
                    }

                    var players = player.level().getEntitiesOfClass(ServerPlayer.class, player.getBoundingBox().inflate(100D, 100D, 100D), serverPlayer -> true);
                    players.forEach(p -> {
                        bar.addPlayer(p);
                        IronboundArtefact.LOGGER.debug("attempting to add {}", p.getName());
                    });
                    this.bar.setMax((int) player.getMaxHealth());
                    double maxHealth = player.getMaxHealth();
                    if (maxHealth > 0) { // Prevent division by zero
                        this.bar.setProgress((float) (player.getHealth() / maxHealth));
                    }
                }
            }
            else {
                if (slotContext.entity() != null) {
                    stack.set(ComponentRegistry.UUID_DATA_COMPONENT, new GenericUUIDComponent(slotContext.entity().getStringUUID()));
                    initBar(stack, slotContext);
                }
            }
            if (bar != null) {
                refreshBar(stack,slotContext);
            }
        }
        super.curioTick(slotContext, stack);
    }


    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var attributes = super.getAttributeModifiers(slotContext, id, stack);
        attributes.put(AttributeRegistry.MAX_MANA, new AttributeModifier(IronboundArtefact.prefix("boss_attribute"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(AttributeRegistry.MANA_REGEN, new AttributeModifier(IronboundArtefact.prefix("boss_attribute"), 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(AttributeRegistry.SPELL_POWER, new AttributeModifier(IronboundArtefact.prefix("boss_attribute"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(AttributeRegistry.SUMMON_DAMAGE, new AttributeModifier(IronboundArtefact.prefix("boss_attribute"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(AttributeRegistry.CAST_TIME_REDUCTION, new AttributeModifier(IronboundArtefact.prefix("boss_attribute"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        attributes.put(AttributeRegistry.COOLDOWN_REDUCTION, new AttributeModifier(IronboundArtefact.prefix("boss_attribute"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return attributes;
    }
}
