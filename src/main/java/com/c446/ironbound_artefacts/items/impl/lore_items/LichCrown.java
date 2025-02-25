package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.c446.ironbound_artefacts.registries.ItemRegistry;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.minecraft.tags.EntityTypeTags.UNDEAD;

public class LichCrown extends UserDependantCurios {
    public LichCrown(Properties p) {
        super(p);
        NeoForge.EVENT_BUS.addListener(LichCrown::doLichCrownStuff);
    }

    public static void doLichCrownStuff(LivingChangeTargetEvent event){
        AtomicBoolean isCrownPresent = new AtomicBoolean(false);

        if (event.getNewAboutToBeSetTarget() instanceof Player player) {
            CuriosApi.getCuriosInventory(player).ifPresent(inv -> {
                isCrownPresent.set(!inv.findCurios((new ItemStack(ItemRegistry.LICH_CROWN)).getItem()).isEmpty());

            });
            if (isCrownPresent.get() && event.getEntity() instanceof Mob) {
                if (event.getEntity().getType().is(UNDEAD) && !(event.getEntity() instanceof IMagicSummon)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @Override
    public boolean canEntityUseItem(Entity entity) {
        if (entity instanceof Player player) {
            return (player.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.ENDER) || entity.getName().getString().equals("Dev"));
        }
        return false;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var attributeModifier = ICurioItem.defaultInstance.getAttributeModifiers(slotContext, id);
        attributeModifier.put(AttributeRegistry.SUMMON_DAMAGE, new AttributeModifier(IronboundArtefact.prefix("emperor_crown"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        return attributeModifier;
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag tooltipFlag) {
        lines.add(Component.translatable("item.ironbounds_artefacts.emperor_crown.tooltip1"));
        lines.add(Component.translatable("item.ironbounds_artefacts.emperor_crown.tooltip2").withStyle(ChatFormatting.ITALIC));
        var affinity = AffinityData.getAffinityData(stack);
        var spell = affinity.getSpell();
        if (!spell.equals(SpellRegistry.none())) {
            lines.add(Component.empty());
            lines.add(Component.translatable("curios.modifiers.head").withStyle(ChatFormatting.GOLD));
            var name = spell.getDisplayName(MinecraftInstanceHelper.instance.player()).withStyle(spell.getSchoolType().getDisplayName().getStyle());
            lines.add(Component.literal(" ").append(
                    (affinity.bonus() == 1 ? Component.translatable("tooltip.irons_spellbooks.enhance_spell_level", name) : Component.translatable("tooltip.irons_spellbooks.enhance_spell_level_plural", affinity.bonus(), name))
                            .withStyle(ChatFormatting.YELLOW)));
        }
        super.appendHoverText(stack, context, lines, tooltipFlag);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        var copy = stack.copy();
        copy.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(SpellRegistry.RAISE_DEAD_SPELL.get().getSpellId(), 3));
        stack = copy;

        super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

}
