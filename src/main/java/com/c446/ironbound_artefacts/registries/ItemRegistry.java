package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.items.armor.archmagi_weave.ArchmagiWeaveItem;
import com.c446.ironbound_artefacts.items.impl.TuningFork;
import com.c446.ironbound_artefacts.items.impl.regular.ForsakenDreams;
import com.c446.ironbound_artefacts.items.armor.arcane_weave.ArcaneWeaveItem;
import com.c446.ironbound_artefacts.items.impl.lore_items.*;
import com.c446.ironbound_artefacts.items.impl.regular.*;
import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.SpellSlotUpgradeItem;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.item.weapons.StaffTier;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class ItemRegistry {
    public static final StaffTier TIER_STAFF_OF_MAGI = new StaffTier(5, -2.4F, new AttributeContainer(AttributeRegistry.MANA_REGEN, 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final StaffTier TIER_STAFF_OF_POWER = new StaffTier(7, -2.4F,new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),  new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), new AttributeContainer(AttributeRegistry.MANA_REGEN, 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final StaffTier TIER_STAFF_OF_ARCANE = new StaffTier(5,-2.4F, new AttributeContainer(AttributeRegistry.CAST_TIME_REDUCTION,0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION,0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static  final StaffTier TIER_SUN_STAFF = new StaffTier(5, -2.4F, new AttributeContainer(AttributeRegistry.MANA_REGEN, 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer(AttributeRegistry.FIRE_SPELL_POWER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MODID);

    // WANDS
    public static final DeferredHolder<Item, StaffOfPower> STAFF_OF_POWER = ITEMS.register("staff_of_power", () -> new StaffOfPower(ItemPropertiesHelper.equipment(1).rarity(Rarity.EPIC).attributes(ExtendedSwordItem.createAttributes(TIER_STAFF_OF_POWER))));
    public static final DeferredHolder<Item, StaffItem> STAFF_OF_SUN = ITEMS.register("sun_staff", () -> new StaffItem(ItemPropertiesHelper.equipment(1).rarity(Rarity.COMMON).attributes(ExtendedSwordItem.createAttributes(TIER_SUN_STAFF))));
    //public static final DeferredHolder<Item, StaffOfMagi> STAFF_OF_MAGI = ITEMS.register("staff_of_magi", () -> new StaffOfMagi(ItemPropertiesHelper.equipment(1).rarity(Rarity.EPIC).attributes(ExtendedSwordItem.createAttributes(TIER_STAFF_OF_MAGI))));



    // HEADWEARS
    public static final DeferredHolder<Item, FlowerCrown> FC= ITEMS.register("flower_crown", ()->new FlowerCrown(ItemPropertiesHelper.equipment(1).rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, LichCrown> LICH_CROWN = ITEMS.register("emperor_crown", () -> new LichCrown(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

    // NECKLACES
    //public static final DeferredHolder<Item, InsightCharm> AMA_CHARM= ITEMS.register("ingisht_insignia", () -> new InsightCharm(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, Phylactery> PHYLACTERY = ITEMS.register("phylactery", () -> new Phylactery(ItemPropertiesHelper.equipment(1).rarity(Rarity.EPIC)));
    public static final DeferredHolder<Item, AmuletOfHolding> AMULET_OF_HOLDING = ITEMS.register("amulet_of_spell_storing", () -> new AmuletOfHolding(ItemPropertiesHelper.equipment(1).rarity(Rarity.RARE).component(ComponentRegistry.SPELL_CONTAINER, new SpellContainer(4, true, false, false))));

    // RINGS
    public static final DeferredHolder<Item, CurioBaseItem> MAGIC_DEFENSE_RING = ITEMS.register("magic_protection_ring", () -> new CurioBaseItem(ItemPropertiesHelper.equipment(1).rarity(Rarity.UNCOMMON)).withAttributes(Curios.RING_SLOT, new AttributeContainer(AttributeRegistry.SPELL_RESIST, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));
    public static final DeferredHolder<Item, CurioBaseItem> PROTECTION_RING = ITEMS.register("protection_ring", () -> new CurioBaseItem(ItemPropertiesHelper.equipment(1).rarity(Rarity.UNCOMMON)).withAttributes(Curios.RING_SLOT, new AttributeContainer(Attributes.ARMOR, 5, AttributeModifier.Operation.ADD_VALUE)));
    public static final DeferredHolder<Item, RingOfThreeWishes> THREE_WISHES = ITEMS.register("three_wishes", () -> new RingOfThreeWishes(ItemPropertiesHelper.equipment(1).rarity(Rarity.EPIC)));

    // BODY
    public static final DeferredHolder<Item, EvasionCloak> ARCANE_PROTECTION_CLOAK = ITEMS.register("cloak_of_evasion", () -> new EvasionCloak(ItemPropertiesHelper.equipment(1).rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, ElvenChains> ELVEN_CHAINS = ITEMS.register("elven_chains", () -> new ElvenChains(ItemPropertiesHelper.equipment(1).rarity(Rarity.RARE)));

    // GLOVES
    public static final DeferredHolder<Item, BelligerentSkies> LIGHTNING_GLOVES = ITEMS.register("lightning_glove", ()-> new BelligerentSkies(ItemPropertiesHelper.equipment(1).rarity(Rarity.RARE)));
    // BOOTS

    public static final DeferredHolder<Item, ArcaneWeaveItem> WEAVE_HELMET = ITEMS.register("arcane_weave_helmet", () -> new ArcaneWeaveItem(Type.HELMET, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).durability(ArmorItem.Type.HELMET.getDurability(55))));
    public static final DeferredHolder<Item, ArcaneWeaveItem> WEAVE_CHEST_PLATE = ITEMS.register("arcane_weave_chestplate", () -> new ArcaneWeaveItem(Type.CHESTPLATE, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).durability(Type.CHESTPLATE.getDurability(55))));
    public static final DeferredHolder<Item, ArcaneWeaveItem> WEAVE_LEGGINGS = ITEMS.register("arcane_weave_leggings", () -> new ArcaneWeaveItem(Type.LEGGINGS, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).durability(Type.LEGGINGS.getDurability(55))));
    public static final DeferredHolder<Item, ArcaneWeaveItem> WEAVE_BOOTS = ITEMS.register("arcane_weave_boots", () -> new ArcaneWeaveItem(Type.BOOTS, ItemPropertiesHelper.equipment().rarity(Rarity.RARE).stacksTo(1).durability(Type.BOOTS.getDurability(55))));

    public static final DeferredHolder<Item, ArchmagiWeaveItem> ARCHMAGE_HEAD = ITEMS.register("archmage_weave_helmet", () -> new ArchmagiWeaveItem(Type.HELMET, new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).durability(ArmorItem.Type.HELMET.getDurability(200))));
    public static final DeferredHolder<Item, ArchmagiWeaveItem> ARCHMAGE_CHEST = ITEMS.register("archmage_weave_chestplate", () -> new ArchmagiWeaveItem(Type.CHESTPLATE, new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).durability(Type.CHESTPLATE.getDurability(200))));
    public static final DeferredHolder<Item, ArchmagiWeaveItem> ARCHMAGE_LEG= ITEMS.register("archmage_weave_leggings", () -> new ArchmagiWeaveItem(Type.LEGGINGS, new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).durability(Type.LEGGINGS.getDurability(200))));
    public static final DeferredHolder<Item, ArchmagiWeaveItem> ARCHMAGE_BOOTS = ITEMS.register("archmage_weave_boots", () -> new ArchmagiWeaveItem(Type.BOOTS, ItemPropertiesHelper.equipment().rarity(Rarity.EPIC).stacksTo(1).durability(Type.BOOTS.getDurability(200))));
    public static final DeferredHolder<Item, ArcaneWand> WIZARDING_WAND = ITEMS.register("wizard_wand", () -> new ArcaneWand(ItemPropertiesHelper.equipment(1).rarity(Rarity.EPIC).attributes(ExtendedSwordItem.createAttributes(TIER_STAFF_OF_ARCANE))));

    public static final DeferredHolder<Item, Item> GREATER_SPELL_SLOT_UPGRADE = ITEMS.register("greater_spell_slot_upgrade", () -> new SpellSlotUpgradeItem(15));

    //public static final DeferredHolder<Item, MagicianMonocle> MAGICIANS_MONOCLE = ITEMS.register("magicians_monocle", () -> new MagicianMonocle(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)));
    /*DISABLED*/

    //public static final DeferredHolder<Item, DevilsFinger> DEVILS_FINGER = ITEMS.register("devils_finger", () -> new DevilsFinger(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)))
    //public static final DeferredHolder<Item, JudgementScale> JUDGEMENT_SCALE = ITEMS.register("judgement_scale", () -> new JudgementScale(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)))
    //public static final DeferredHolder<Item, DeathAmulet> DEATH_AMULET;
    //public static final DeferredHolder<Item, LoversStopwatch> STOPWATCH = ITEMS.register("stopwatch", () -> new LoversStopwatch(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)))
    public static final DeferredHolder<Item, ForsakenDreams> DREAMS = ITEMS.register("forsaken_dreams", () -> new ForsakenDreams(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));
    public static final DeferredHolder<Item, TuningFork> TUNING_FORK_DEFERRED_HOLDER = ITEMS.register("tuning_fork", ()-> new TuningFork(ItemPropertiesHelper.equipment(1)));

    public static final DeferredHolder<Item, CurioBaseItem> ARCHMAGE_SPELLBOOK = ITEMS.register("archmage_spellbook", () -> new ArchMageSpellBook(15).withSpellbookAttributes(new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE)));
    //public static final DeferredHolder<Item, AmuletOfMana> AMULET_OF_MANA;
    //public static final DeferredHolder<Item, DeckOfAllThings> DECK_OF_ALL_THINGS;




    //public static final DeferredHolder<Item, HermitEye> HERMIT_EYE = ITEMS.register("hermit_eye", () -> new HermitEye(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

    static {


        //AMULET_OF_MANA = ITEMS.register("amulet_of_mana_storing", () -> new AmuletOfMana(new Item.Properties().durability(1500)));

        //DECK_OF_ALL_THINGS = ITEMS.register("deck_of_many_things", () -> new DeckOfAllThings(new Item.Properties().rarity(Rarity.RARE)));
    }


}
