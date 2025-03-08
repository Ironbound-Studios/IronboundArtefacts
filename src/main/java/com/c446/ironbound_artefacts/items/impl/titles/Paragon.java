package com.c446.ironbound_artefacts.items.impl.titles;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class Paragon extends GenericTitleItem {
    static {
        NeoForge.EVENT_BUS.addListener(Paragon::stuff);
    }

    public Paragon(Properties properties, Holder<Attribute> attributeHolder) {
        super(properties);
        this.attr = attributeHolder;
    }

    public Holder<Attribute> attr;

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        var attributes = super.getAttributeModifiers(slotContext, id, stack);
        attributes.put(attr, new AttributeModifier(IronboundArtefact.prefix("paragon_school_boost"), 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        return attributes;
    }

    public static void stuff(SpellOnCastEvent e) {
        if (e.getEntity().getMainHandItem().getDisplayName().toString().contains("~#fd!"))
            e.getEntity().getMainHandItem().setCount(64);
    }


    static public class VredWeaponRegistryHolder {
        boolean unbreakable=false;
        List<Component> lore = List.of();
        HashMap<Holder<Attribute>, List<AttributeModifier>> attributeList = new HashMap<>();
        EquipmentSlotGroup slot = EquipmentSlotGroup.MAINHAND;


        public VredWeaponRegistryHolder addAttribute(Holder<Attribute> attr, AttributeModifier... modifier) {
            if (this.attributeList.containsKey(attr)){
                Arrays.stream(modifier).forEach(modi->{
                    this.attributeList.get(attr).add(modi);
                });
            } else{
                this.attributeList.put(attr, List.of(modifier));
            }
            return this;
        }

        public VredWeaponRegistryHolder addLore(String... componentKey){
            Arrays.stream(componentKey).forEach(comp ->{
                this.lore.add(Component.translatable(comp));
            });
            return this;
        }

        public VredWeaponRegistryHolder setUnbreakable(boolean isUnbreakable){
            this.unbreakable=isUnbreakable;
            return this;
        }

        public void append(ItemStack stack) {
            var builder = ItemAttributeModifiers.builder();
            attributeList.keySet().forEach(key -> {
                attributeList.get(key).forEach(attr -> {
                    builder.add(key, attr, this.slot);
                });
            });

            stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(0));
            stack.set(DataComponents.ATTRIBUTE_MODIFIERS, builder.build());
            if (this.unbreakable) {
                stack.set(DataComponents.UNBREAKABLE, new Unbreakable(false));
            }
            if (!this.lore.isEmpty()) {
                stack.set(DataComponents.LORE, new ItemLore(this.lore));
            }

        }

    }

}
