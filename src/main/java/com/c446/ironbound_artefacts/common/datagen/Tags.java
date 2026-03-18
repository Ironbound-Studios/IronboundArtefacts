package com.c446.ironbound_artefacts.common.datagen;

import com.c446.ironbound_artefacts.IBA;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import static net.minecraft.core.registries.Registries.ENTITY_TYPE;

public class Tags {
    public static class SpellTags {
        public static TagKey<AbstractSpell> OFFENSIVE_SPELL = create(IBA.p("offensive_spell"));
        public static TagKey<AbstractSpell> UTILITY_SPELL = create(IBA.p("utility_spell"));
        public static TagKey<AbstractSpell> DEFENSIVE_SPELL = create(IBA.p("defensive_spell"));
        public static TagKey<AbstractSpell> MOUVEMENT_SPELL = create(IBA.p("mouvement_spell"));
        public static TagKey<AbstractSpell> SUPPORT_SPELL = create(IBA.p("support_spell"));

        public static TagKey<AbstractSpell> ARCHMAGE_ALLOWED_SPELL = create(IBA.p("archmage_spells"));
        public static TagKey<AbstractSpell> ARCHMAGE_SINGLE_SPELL = create(IBA.p("archmage_single_spells"));
        public static TagKey<AbstractSpell> ARCHMAGE_BARRAGE_SPELL = create(IBA.p("archmage_barrage_spells"));
        public static TagKey<AbstractSpell> ARCHMAGE_COMMON = create(IBA.p("archmage_common"));

        public static TagKey<EntityType<?>> HYPNOTIC_PATTERN_IMMUNE = TagKey.create(ENTITY_TYPE, IBA.p("hypnotic_pattern_immune"));

        public static TagKey<AbstractSpell> WISH_UNCASTABLE = create(IBA.p("wish_banned"));

        public static TagKey<DamageType> IGNORE_SPELL_RES = TagKey.create(Registries.DAMAGE_TYPE, IBA.p("no_spell_res"));

        public static TagKey<AbstractSpell> create(ResourceLocation name) {
            return new TagKey<AbstractSpell>(SpellRegistry.SPELL_REGISTRY_KEY, name);
        }
    }

    public static class ItemTags {
        public static TagKey<Item> STAFF_COPY = net.minecraft.tags.ItemTags.create(IronsSpellbooks.id("staff"));
        public static TagKey<Item> WISH_DUPLICABLE = net.minecraft.tags.ItemTags.create(IBA.p("wish_duplicable"));
        public static TagKey<Item> IMMORTAL_ART_FOCUS = net.minecraft.tags.ItemTags.create(IBA.p("immortal_art_focus"));
    }
}
