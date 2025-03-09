package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.items.UserDependantCurios;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class EmperorCrown extends UserDependantCurios {

    public EmperorCrown(Properties p) {
        super(p);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        stack.set(ComponentRegistry.AFFINITY_COMPONENT, new AffinityData(SpellRegistry.RAISE_DEAD_SPELL.get().getSpellId(), 3));
        super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public boolean canEntityUseItem(Entity entity) {
        return true;
    }


}
