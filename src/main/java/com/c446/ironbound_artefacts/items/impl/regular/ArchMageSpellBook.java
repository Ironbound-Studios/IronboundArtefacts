package com.c446.ironbound_artefacts.items.impl.regular;

import com.c446.ironbound_artefacts.IronboundArtefact;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class ArchMageSpellBook extends SpellBook {

    public ArchMageSpellBook() {
        this(1);
    }

    public ArchMageSpellBook(int maxSpellSlots) {
        this(maxSpellSlots, ItemPropertiesHelper.equipment().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public ArchMageSpellBook(int maxSpellSlots, Item.Properties pProperties) {
        super(maxSpellSlots, pProperties);
    }

    public static boolean canEntityUseItem(Entity entity) {
        if (entity instanceof Player player) {
            return (IronboundArtefact.ContributorUUIDS.CONTRIBUTOR_LIST.contains(player.getStringUUID()) || entity.getName().getString().equals("Dev"));
        }
        return false;
    }

    public SpellBook withAttribute(Holder<Attribute> attribute, double value) {
        return (SpellBook) this.withAttributes(Curios.SPELLBOOK_SLOT, new AttributeContainer[]{new AttributeContainer(attribute, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)});
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, TooltipContext context, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {

        super.appendHoverText(itemStack, context, lines, flag);
    }

//    @Override
//    public void curioTick(SlotContext slotContext, ItemStack stack) {
//        //if (slotContext.entity().getStringUUID())
//        if (canEntityUseItem(slotContext.entity())) {
//            var copy = stack.copy();
//            copy.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(SpellRegistry.WOLOLO_SPELL.get().getSpellId(), 1));
//            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a -> a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
//        } else {
//            var copy = stack.copy();
//            copy.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(SpellRegistry.none().getSpellId(), 0));
//            CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(a -> a.setEquippedCurio(slotContext.identifier(), slotContext.index(), copy));
//        }
//        super.curioTick(slotContext, stack);
//    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }


}
