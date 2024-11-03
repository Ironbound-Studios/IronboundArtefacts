package com.c446.ironbound_artefacts.events;


import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.*;
import com.c446.ironbound_artefacts.registries.ItemRegistry;
import com.google.common.collect.HashMultimap;
import io.redspace.ironsspellbooks.api.events.*;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.registries.*;
import io.redspace.ironsspellbooks.spells.blood.RaiseDeadSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.c446.ironbound_artefacts.registries.ItemRegistry.ARCHMAGE_SPELLBOOK;
import static com.c446.ironbound_artefacts.registries.ItemRegistry.STAFF_OF_POWER;
import static net.minecraft.tags.EntityTypeTags.UNDEAD;

@EventBusSubscriber
public class ServerEvents {
    @SubscribeEvent
    public static void spellLevelEvent(ModifySpellLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            AtomicInteger boost = new AtomicInteger(0);
            CuriosApi.getCuriosInventory(player).ifPresent((curioHandler) -> {
                if (curioHandler.isEquipped(ARCHMAGE_SPELLBOOK.get())) {
                    boost.addAndGet(1);
                }
            });

            if (player != null) {
                if (player.getMainHandItem() != null && player.getMainHandItem().is(STAFF_OF_POWER)) {
                    ISpellContainer mainHandSpellContainer = ISpellContainer.get(player.getMainHandItem());
                    if (mainHandSpellContainer != null && mainHandSpellContainer.getAllSpells() != null) {
                        Arrays.stream(mainHandSpellContainer.getAllSpells()).forEach(spell -> {
                            if (spell != null && spell.getSpell() != null && spell.getSpell().equals(event.getSpell())) {
                                boost.addAndGet(1);
                            }
                        });
                    }
                } else if (player.getOffhandItem() != null && player.getOffhandItem().is(STAFF_OF_POWER)) {
                    ISpellContainer offhandSpellContainer = ISpellContainer.get(player.getOffhandItem());
                    if (offhandSpellContainer != null && offhandSpellContainer.getAllSpells() != null) {
                        Arrays.stream(offhandSpellContainer.getAllSpells()).forEach(spell -> {
                            if (spell != null && spell.getSpell() != null && spell.getSpell().equals(event.getSpell())) {
                                boost.addAndGet(1);
                            }
                        });
                    }
                }
            }

            if (boost.get() > 2) {
                boost.set(2);
            }
            event.setLevel(event.getLevel() + boost.get());
        }
    }

    @SubscribeEvent
    public static void onServerStart(ServerStartedEvent event) {
        ServerConfigs.IMBUE_WHITELIST_ITEMS.add(STAFF_OF_POWER.get());

        event.getServer().getLevel(ServerLevel.OVERWORLD).setBlock(new BlockPos(30_000_100, 0, 30_000_100), BlockRegistry.SCROLL_FORGE_BLOCK.get().defaultBlockState(), 510);

    }

    @SubscribeEvent
    public static void onEntityDamaged(LivingDamageEvent.Post event) {
        CuriosApi.getCuriosInventory(event.getEntity().getLastAttacker()).ifPresent(inv -> {
            List<SlotResult> result = inv.findCurios(ItemRegistry.DEATH_AMULET.get());
            if (!result.isEmpty()) {
                event.getEntity().addEffect(new MobEffectInstance(EffectsRegistry.VOID_POISON, 3, 1));
            }
        });
    }

    @SubscribeEvent
    public static void tickEntity(EntityTickEvent.Post event) {
    }

    @SubscribeEvent
    public static void entityTriesTP(SpellTeleportEvent event) {
        if (event.getEntity() instanceof LivingEntity living && living.hasEffect(EffectsRegistry.TIME_STOP)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onManaRegen(io.redspace.ironsspellbooks.api.events.ChangeManaEvent event) {
        if (event.getEntity().hasEffect(EffectsRegistry.TIME_STOP)) {
            event.setCanceled(true);
        }
    }

    public static void onThrowItem(net.neoforged.neoforge.event.entity.item.ItemTossEvent event) {
        if (event.getPlayer().hasEffect(EffectsRegistry.TIME_STOP)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity().hasEffect(EffectsRegistry.TIME_STOP)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemUseLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity().hasEffect(EffectsRegistry.TIME_STOP)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void entityGoalChanges(LivingChangeTargetEvent event) {
        AtomicBoolean isCrownPresent = new AtomicBoolean(false);
        AtomicBoolean isHandPresent = new AtomicBoolean(false);
        if (event.getNewAboutToBeSetTarget() instanceof Player player) {
            CuriosApi.getCuriosInventory(player).ifPresent(inv -> {
                isCrownPresent.set(inv.findCurios((new ItemStack(ItemRegistry.LICH_CROWN)).getItem()).isEmpty());
                isHandPresent.set(inv.findCurios((new ItemStack(ItemRegistry.LICH_HAND)).getItem()).isEmpty());
            });
            if (isCrownPresent.get() || isHandPresent.get()) {
                if (event.getEntity().getType().is(UNDEAD)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSummoningThings(SpellSummonEvent<Monster> event) {


        System.out.println(event.getCreature().toString());
        var spell = SpellRegistry.getSpell(event.getSpellId());
        LivingEntity player = event.getCaster();
        var quality = 3 * event.getSpellLevel() + 15;

        // Check if the spell is an instance of RaiseDeadSpell and if the player has the full Lich set equipped
        if (spell instanceof RaiseDeadSpell) {
            boolean hasLichSet = CuriosApi.getCuriosInventory(player)
                    .map(inv -> !inv.findCurios(ItemRegistry.LICH_CROWN.get()).isEmpty() ||
                            !inv.findCurios(ItemRegistry.LICH_HAND.get()).isEmpty())
                    .orElse(false);

            if (hasLichSet) {
                var canGetNetherite = (player.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.TAR) || player.getStringUUID().equals(IronboundArtefact.ContributorUUIDS.ENDER));
                Monster creature = (equipCreatureBasedOnQuality(event.getCreature(), quality, canGetNetherite));
                System.out.println(creature.toString());
                HashMultimap<Holder<Attribute>, AttributeModifier> summonAttributes = HashMultimap.create();
                summonAttributes.put(Attributes.MAX_HEALTH, new AttributeModifier(IronboundArtefact.prefix("summon_health_boost"), 4 * SpellRegistry.RAISE_DEAD_SPELL.get().getSpellPower(event.getSpellLevel(), player), AttributeModifier.Operation.ADD_VALUE));
                summonAttributes.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                        IronboundArtefact.prefix("summon_damage"),
                        2 * SpellRegistry.RAISE_DEAD_SPELL.get().getSpellPower(event.getSpellLevel(), player),
                        AttributeModifier.Operation.ADD_VALUE
                ));

                creature.getAttributes().addTransientAttributeModifiers(summonAttributes);
                event.setCreature(creature);
            }
        }
    }


    private static Monster equipCreatureBasedOnQuality(Monster creature, int quality, boolean canGetNetherite) {
        if (quality > 40) {
            if (quality < 50) {
                equipWithDiamondGear(creature);
            } else {
                equipWithNetheriteGear(creature);
                setDropChancesToZero(creature);
            }
        }
        return (creature);
    }

    private static void equipWithDiamondGear(Mob creature) {
        creature.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
        creature.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
        creature.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
        creature.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
        setWeaponBasedOnType(creature, Items.DIAMOND_AXE, Items.BOW);
    }

    private static void equipWithNetheriteGear(Mob creature) {
        creature.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET));
        creature.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
        creature.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
        creature.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.NETHERITE_BOOTS));
        setWeaponBasedOnType(creature, Items.NETHERITE_AXE, Items.BOW);
    }

    private static void setWeaponBasedOnType(Mob creature, Item meleeWeapon, Item rangedWeapon) {
        if (creature.getType().equals(EntityRegistry.SUMMONED_ZOMBIE.get())) {
            creature.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(meleeWeapon));
        } else if (creature.getType().equals(EntityRegistry.SUMMONED_SKELETON.get())) {
            creature.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(rangedWeapon));
        }
    }

    private static void setDropChancesToZero(Mob creature) {
        creature.setDropChance(EquipmentSlot.HEAD, 0.0F);
        creature.setDropChance(EquipmentSlot.CHEST, 0.0F);
        creature.setDropChance(EquipmentSlot.LEGS, 0.0F);
        creature.setDropChance(EquipmentSlot.FEET, 0.0F);
    }

    public static void onCast(SpellPreCastEvent preCastEvent){
        if (preCastEvent.getEntity().hasEffect(EffectsRegistry.TIME_STOP)){
            preCastEvent.setCanceled(true);
        }
    }
}