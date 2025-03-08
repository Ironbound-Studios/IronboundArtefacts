package com.c446.ironbound_artefacts.datagen;

import com.c446.ironbound_artefacts.IronboundArtefact;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class Tags {
    public static class SpellTags {
        public static TagKey<AbstractSpell> OFFENSIVE_SPELL = create(IronboundArtefact.prefix("offensive_spell"));
        public static TagKey<AbstractSpell> UTILITY_SPELL = create(IronboundArtefact.prefix("utility_spell"));
        public static TagKey<AbstractSpell> DEFENSIVE_SPELL = create(IronboundArtefact.prefix("defensive_spell"));
        public static TagKey<AbstractSpell> MOUVEMENT_SPELL = create(IronboundArtefact.prefix("mouvement_spell"));
        public static TagKey<AbstractSpell> SUPPORT_SPELL = create(IronboundArtefact.prefix("support_spell"));
        public static TagKey<AbstractSpell> WISH_BANNED = create(IronboundArtefact.prefix("wish_blacklisted"));

        public static TagKey<AbstractSpell> ARCHMAGE_ALLOWED_SPELL = create(IronboundArtefact.prefix("archmage_spells"));
        public static TagKey<AbstractSpell> ARCHMAGE_SINGLE_SPELL = create(IronboundArtefact.prefix("archmage_single_spells"));
        public static TagKey<AbstractSpell> ARCHMAGE_BARRAGE_SPELL = create(IronboundArtefact.prefix("archmage_barrage_spells"));
        public static TagKey<AbstractSpell> ARCHMAGE_COMMON = create(IronboundArtefact.prefix("archmage_common"));

        public static TagKey<AbstractSpell> WISH_UNCASTABLE = create(IronboundArtefact.prefix("wish_banned"));


        public static TagKey<AbstractSpell> create(ResourceLocation name) {
            return new TagKey<AbstractSpell>(SpellRegistry.SPELL_REGISTRY_KEY, name);
        }

    }
    public static class ItemTags{
        public static TagKey<Item> STAFF_COPY= net.minecraft.tags.ItemTags.create(IronsSpellbooks.id("staff"));
        public static TagKey<Item> WISH_DUPLICABLE = net.minecraft.tags.ItemTags.create(IronboundArtefact.prefix("wish_duplicable"));
    }
}
