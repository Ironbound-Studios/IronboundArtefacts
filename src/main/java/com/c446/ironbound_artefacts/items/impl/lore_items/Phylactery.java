package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.components.KillCounterComponent;
import com.c446.ironbound_artefacts.components.UniversalPositionComponent;
import com.c446.ironbound_artefacts.registries.ComponentRegistry;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

import static io.redspace.ironsspellbooks.registries.ComponentRegistry.SPELL_CONTAINER;


public class Phylactery extends CurioBaseItem {
    public Phylactery(Properties properties) {
        super(properties);
    }
    /*
     * Insight
     * CD
     * mana regen
     * */

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var map = super.getAttributeModifiers(slotContext, id, stack);
        if (slotContext.entity() != null) {
            if (slotContext.entity().getStringUUID().equals(IronboundArtefact.ContributorUUIDS.AMON)) {
                map.put(AttributeRegistry.MANA_REGEN, new AttributeModifier(id, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            } else {
                map.put(AttributeRegistry.BLOOD_SPELL_POWER, new AttributeModifier(id, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
        }
        return map;
    }


    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (!stack.has(ComponentRegistry.KILL_COUNT_COMPONENT)) {
            stack.set(ComponentRegistry.KILL_COUNT_COMPONENT, new KillCounterComponent(1));
        }
        if (!stack.has(SPELL_CONTAINER)) {
            stack.set(SPELL_CONTAINER, new SpellContainer(1, false, false, false, new SpellSlot[]{SpellSlot.of(new SpellData(SpellRegistry.HEARTSTOP_SPELL.get(), 10), 0)}));
        }
        super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.setRespawnPosition(level.dimension(), player.blockPosition(), player.getXRot(), true, true);
            level.playLocalSound(player.getX(), player.getEyeY(), player.getZ(), SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.MASTER, 1, 1, false);
        }

        return super.use(level, player, hand);

    }


    @Override
    public void appendHoverText(ItemStack pStack, @NotNull TooltipContext pContext, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag) {
        if (pStack.has(ComponentRegistry.KILL_COUNT_COMPONENT)) {
            pTooltipComponents.add(Component.translatable("item.ironbounds_artefacts.phylactery.tooltip1"));
            pTooltipComponents.add(Component.translatable("item.ironbounds_artefacts.phylactery.tooltip2"));
            pTooltipComponents.add(Component.translatable("item.ironbounds_artefacts.phylactery.tooltip3"));
            pTooltipComponents.add(Component.translatable("item.ironbounds_artefacts.phylactery.tooltip4"));
            pTooltipComponents.add(Component.translatable("item.ironbounds_artefacts.phylactery.souls", pStack.get(ComponentRegistry.KILL_COUNT_COMPONENT).killCount()));
            System.out.println();
        }

        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}
