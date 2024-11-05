package com.c446.ironbound_artefacts.items.impl;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.components.HermitComponentData;
import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import com.c446.ironbound_artefacts.registries.ItemRegistry;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellSlot;
import io.redspace.ironsspellbooks.block.scroll_forge.ScrollForgeTile;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.c446.ironbound_artefacts.registries.ComponentRegistry.HERMIT_SCROLL_FORGE_COMPONENT;

public class HermitEye extends UserDependantCurios {


    public HermitEye(Properties p) {
        super(p);
    }

    @Override
    public boolean canEntityUseItem(Entity entity) {
        return (entity.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.AMADHE) || entity.getName().getString().equals("Dev"));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag tooltipFlag) {
        lines.add(Component.translatable("item.ironbounds_artefacts.hermit_amulet.tooltip1"));
        lines.add(Component.translatable("item.ironbounds_artefacts.hermit_amulet.tooltip2").withStyle(ChatFormatting.ITALIC));
        var affinity = AffinityData.getAffinityData(stack);

        super.appendHoverText(stack, context, lines, tooltipFlag);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (canEntityUseItem(slotContext.entity())) {
            var copy = stack.copy();
            copy.set(ComponentRegistry.SPELL_CONTAINER, new SpellContainer(2, true, false, false, new SpellSlot[]{
                            new SpellSlot(new SpellData(SpellRegistry.TELEKINESIS_SPELL.get(), 10, true), 0),
                            new SpellSlot(new SpellData(SpellRegistry.TELEPORT_SPELL.get(), 10, true), 1)
                    })
            );
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a -> a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
        } else {
            var copy = stack.copy();
            copy.remove(ComponentRegistry.SPELL_CONTAINER);
            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a -> a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
        }
        super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = null;
        if (usedHand == InteractionHand.MAIN_HAND) {
            stack = player.getMainHandItem();
        } else if (usedHand == InteractionHand.OFF_HAND) {
            stack = player.getOffhandItem();
        }

        if (level instanceof ServerLevel serverLevel) {
            openMenuKey(player, serverLevel, stack);
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    public static void openMenuKey(Player entity, ServerLevel serverLevel, ItemStack stack) {
        if (stack.getItem() == ItemRegistry.HERMIT_EYE.get()) {
            GlobalPos pos = new GlobalPos(ServerLevel.OVERWORLD, new BlockPos(30_000_100, 0, 30_000_100));

            var overworld = serverLevel.getServer().getLevel(ServerLevel.OVERWORLD);
            var state = Objects.requireNonNull(overworld).getBlockEntity(pos.pos());

            if (state instanceof ScrollForgeTile scrollForgeTile) {
                scrollForgeTile.createMenu(serverLevel.random.nextInt(), entity.getInventory(), entity);
            }
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attributeMap = ICurioItem.defaultInstance.getAttributeModifiers(slotContext, id);
        double multiplier = 1;
        if (slotContext.entity() != null) {
            multiplier = Math.max(0, (40 - slotContext.entity().getAttributeValue(Attributes.ARMOR) - slotContext.entity().getAttributeValue(Attributes.ARMOR_TOUGHNESS)) / 10);
            if (canEntityUseItem(slotContext.entity())) {
                multiplier *= 2;
                attributeMap.put(AttributeRegistry.MAX_MANA, new AttributeModifier(id, 0.125 * multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
            attributeMap.put(AttributeRegistry.MANA_REGEN, new AttributeModifier(id, 0.125 * multiplier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
        return attributeMap;
    }
}
