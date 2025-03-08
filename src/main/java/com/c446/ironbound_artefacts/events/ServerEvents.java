package com.c446.ironbound_artefacts.events;


import com.c446.ironbound_artefacts.Config;
import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.attachment.FirstLoginData;
import com.c446.ironbound_artefacts.components.KillCounterComponent;
import com.c446.ironbound_artefacts.entities.simulacrum.SimulacrumEntity;
import com.c446.ironbound_artefacts.ironbound_spells.spells.enthrall.DominatedEffectInstance;
import com.c446.ironbound_artefacts.ironbound_spells.spells.enthrall.EnthralledEffect;
import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.c446.ironbound_artefacts.items.impl.lore_items.LichCrown;
import com.c446.ironbound_artefacts.items.impl.lore_items.Phylactery;
import com.c446.ironbound_artefacts.registries.*;
import com.c446.ironbound_artefacts.registries.ItemRegistry;
import com.google.common.collect.HashMultimap;
import dev.shadowsoffire.apothic_attributes.impl.AttributeEvents;
import io.redspace.ironsspellbooks.api.events.*;
import io.redspace.ironsspellbooks.api.events.ModifySpellLevelEvent;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.events.SpellTeleportEvent;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.effect.AngelWingsEffect;
import io.redspace.ironsspellbooks.effect.ThunderstormEffect;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.SummonedSkeleton;
import io.redspace.ironsspellbooks.entity.mobs.SummonedZombie;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.registries.*;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.spells.blood.RaiseDeadSpell;
import io.redspace.ironsspellbooks.spells.holy.AngelWingsSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.commands.BossBarCommands;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static com.c446.ironbound_artefacts.IronboundArtefact.ContributorUUIDS.*;
import static com.c446.ironbound_artefacts.registries.ComponentRegistry.KILL_COUNT_COMPONENT;
import static com.c446.ironbound_artefacts.registries.EffectsRegistry.ENTHRALLED;
import static com.c446.ironbound_artefacts.registries.ItemRegistry.*;
import static net.minecraft.tags.EntityTypeTags.UNDEAD;

@EventBusSubscriber
public class ServerEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void spellLevelEvent(ModifySpellLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            AtomicInteger boost = new AtomicInteger(0);
            /*CuriosApi.getCuriosInventory(player).ifPresent((curioHandler) -> {
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
            }*/

            if (boost.get() > 2) {
                boost.set(2);
            }
            event.setLevel(event.getLevel() + boost.get());
        }
    }


//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public static void onKill(LivingDeathEvent event) {
//        if (event.getSource().getEntity() instanceof Player player) {
//            //System.out.println(player.getDisplayName() + "killed" + event.getEntity().getDisplayName());
//            CuriosApi.getCuriosInventory(player).ifPresent(inv -> {
//                var slot = inv.findFirstCurio(stack -> stack.getItem() instanceof Phylactery);
//                if (slot.isPresent()) {
//                    var ring = slot.get();
//                    if (ring.stack().has(KILL_COUNT_COMPONENT)) {
//                        ring.stack().set(KILL_COUNT_COMPONENT, new KillCounterComponent(Objects.requireNonNull(ring.stack().get(KILL_COUNT_COMPONENT)).killCount() + 1));
//                    } else {
//                        ring.stack().set(KILL_COUNT_COMPONENT, new KillCounterComponent(1));
//                    }
//                }
//            });
//        }
//    }

    @SubscribeEvent
    public static void NBTSaveCopy(PlayerEvent.Clone event) {
        var entity = event.getOriginal();
        var newEntity = event.getEntity();
        if (entity.hasData(AttachmentRegistry.PLAYER_FIRST_LOGIN_ATTACHMENT_IB_ARTEFACTS)) {
            newEntity.setData(AttachmentRegistry.PLAYER_FIRST_LOGIN_ATTACHMENT_IB_ARTEFACTS, entity.getData(AttachmentRegistry.PLAYER_FIRST_LOGIN_ATTACHMENT_IB_ARTEFACTS));
        }
    }

    @SubscribeEvent
    public static void entityJoins(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LivingEntity living && living.hasEffect(ENTHRALLED)) {
            if (living.getEffect(ENTHRALLED) instanceof DominatedEffectInstance dom && dom.emitter != null && dom.receiver != null) {
                // do nothing
            } else {
                living.removeEffect(ENTHRALLED);
            }

        }
    }

    @SubscribeEvent
    public static void PhylacteryHandler(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            CuriosApi.getCuriosInventory(player).ifPresent(inv -> {
                var phylacteries = inv.findFirstCurio(stack -> stack.getItem() instanceof Phylactery);
                if (player instanceof ServerPlayer serverPlayer && phylacteries.isPresent() && phylacteries.get().stack().has(KILL_COUNT_COMPONENT)) {
                    var killCount = Objects.requireNonNull(phylacteries.get().stack().get(KILL_COUNT_COMPONENT)).killCount();
                    if (killCount >= 5) {
                        var stack = phylacteries.get().stack();
                        if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
                            stack.set(KILL_COUNT_COMPONENT, new KillCounterComponent(Math.max(0, killCount * 2 / 3 - 5)));
                            if (Objects.requireNonNull(serverPlayer.getServer()).getLevel(serverPlayer.getRespawnDimension()) != null && serverPlayer.getRespawnPosition() != null) {
                                var dim = serverPlayer.getServer().getLevel(serverPlayer.getRespawnDimension());

                                if (dim != null) {
                                    serverPlayer.changeDimension(new DimensionTransition(dim, new Vec3(serverPlayer.getRespawnPosition().getX(), serverPlayer.getRespawnPosition().getY(), serverPlayer.getRespawnPosition().getZ()), Vec3.ZERO, serverPlayer.getXRot(), serverPlayer.getYRot(), false, DimensionTransition.DO_NOTHING));
                                    player.setHealth((float) (player.getMaxHealth() * 0.5));
                                    event.setCanceled(true);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onSummonDamage(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof IMagicSummon summon && summon.getSummoner() != null) {
            event.setNewDamage(
                    (float) (event.getOriginalDamage() * summon.getSummoner().getAttributeValue(AttributeRegistry.SUMMON_DAMAGE)
                    )
            );
        }
    }

    @SubscribeEvent
    public static void onSummonSpellDamage(SpellDamageEvent event) {
        if (event.getEntity() instanceof IMagicSummon summon && summon.getSummoner() != null) {
            event.setAmount(
                    (float) (event.getAmount() * summon.getSummoner().getAttributeValue(AttributeRegistry.SUMMON_DAMAGE)
                    )
            );
        }
    }


//    @SubscribeEvent
//    static public void tick(PlayerTickEvent.Pre e) {
//        e.getEntity().getMainHandItem().getAttributeModifiers().forEach(EquipmentSlotGroup.MAINHAND, (a, b) -> {
//            System.out.println(b.id() + " : " + b.amount() + e.getEntity().getMainHandItem().getItem());
//        });
//
//        e.getEntity().getOffhandItem().getAttributeModifiers().forEach(EquipmentSlotGroup.MAINHAND, (a, b) -> {
//            System.out.println(b.id() + " : " + b.amount() + e.getEntity().getOffhandItem().getItem());
//        });
//    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void grantItemsOnJoin(PlayerEvent.PlayerLoggedInEvent event) {
        var uuid = event.getEntity().getStringUUID();
        var entity = event.getEntity();
        
        if (IronboundArtefact.ContributorUUIDS.CONTRIBUTOR_LIST.contains(entity.getStringUUID()) && !entity.getData(AttachmentRegistry.PLAYER_FIRST_LOGIN_ATTACHMENT_IB_ARTEFACTS).hasLoggedIn) {
            event.getEntity().setData(AttachmentRegistry.PLAYER_FIRST_LOGIN_ATTACHMENT_IB_ARTEFACTS, new FirstLoginData().set(true));

            switch (uuid) {
                case AMON -> {
                    entity.getInventory().add(new ItemStack(STAFF_OF_POWER));
                    entity.getInventory().add(new ItemStack(ARCHMAGE_SPELLBOOK));
                    entity.getInventory().add(new ItemStack(LIGHTNING_GLOVES));
                }
                case NINJA_FOX -> entity.getInventory().add(new ItemStack(FC));
                //case ACE -> entity.getInventory().add(new ItemStack(DEVILS_FINGER));
                case AMADHE -> entity.getInventory().add(new ItemStack(WIZARDING_WAND));
                //case CATMOTH -> entity.getInventory().add(new ItemStack(JUDGEMENT_SCALE));
                case ENDER, TAR -> {
                    entity.getInventory().add(new ItemStack(LICH_CROWN));
                    entity.getInventory().add(new ItemStack(PHYLACTERY));
                }
                //case THEKILLAGER -> entity.getInventory().add(new ItemStack(DEATH_AMULET));

                /// /case STYLY -> entity.getInventory().add(new ItemStack(ItemRegistry.HERMIT_EYE));
                /// /                case TOMATO -> entity.getInventory().add(new ItemStack(ItemRegistry.HERMIT_EYE));
            }
        }
    }

    /*@SubscribeEvent
    public static void onEntityDamaged(LivingDamageEvent.Pre event) {
        CuriosApi.getCuriosInventory(event.getEntity().getLastAttacker()).ifPresent(inv -> {
            List<SlotResult> result = inv.findCurios(DEATH_AMULET.get());
            if (!result.isEmpty()) {
                event.getEntity().addEffect(new MobEffectInstance(EffectsRegistry.VOID_POISON, 3, 1));
            }
        });
        if (event.getSource().getEntity() instanceof Player player) {
            System.out.println("attacker is player");
            CuriosApi.getCuriosInventory(player).ifPresent(i -> {
                System.out.println("attacker has curios inv");
                if (i.isEquipped(ItemRegistry.STOPWATCH.get()) && STOPWATCH.value().canEntityUseItem(player)) {
                    System.out.println("attacker has stopwatch");
                    event.getEntity().invulnerableTime = 5;
                }
            });
            //event.getEntity().invulnerableTime = Config.iframeCount;
        }
    }*/
    @SubscribeEvent
    public static void levelTick(ServerTickEvent.Pre event) {
        IronboundArtefact.tickMap();
    }

    @SubscribeEvent
    public static void entityTriesTP(SpellTeleportEvent event) {
        if (event.getEntity() instanceof LivingEntity living && living.hasEffect(EffectsRegistry.TIME_STOP)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onServerStop(ServerStartedEvent event) {
        event.getServer().getAllLevels().forEach(level -> {
            level.getEntitiesOfClass(SimulacrumEntity.class, AABB.INFINITE).forEach(SimulacrumEntity::discard);
        });
    }

    @SubscribeEvent
    public static void onManaRegen(ChangeManaEvent event) {
        if (event.getEntity().hasEffect(EffectsRegistry.TIME_STOP)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onThrowItem(ItemTossEvent event) {
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
    public static void onSummoningThings(SpellSummonEvent<LivingEntity> event) {
        System.out.println(event.getCreature().toString());
        var spell = SpellRegistry.getSpell(event.getSpellId());
        LivingEntity player = event.getCaster();
        var quality = 3 * event.getSpellLevel() + 15;

        // Check if the spell is an instance of RaiseDeadSpell and if the player has the full Lich set equipped
        if (spell instanceof RaiseDeadSpell && event.getCreature() instanceof SummonedZombie) {
            // Check if the player has equipped the Lich Crown
            boolean hasLichCrown = CuriosApi.getCuriosInventory(player)
                    .map(inv -> !inv.findCurios(ItemRegistry.LICH_CROWN.get()).isEmpty())
                    .orElse(false);

            if (hasLichCrown) {
                Monster creature = equipCreatureBasedOnQuality((Monster) event.getCreature(), quality, true);
                System.out.println(creature);

                // Create a Multimap for attribute modifiers
                HashMultimap<Holder<Attribute>, AttributeModifier> summonAttributes = HashMultimap.create();
                summonAttributes.put(Attributes.MAX_HEALTH, new AttributeModifier(IronboundArtefact.prefix("summon_health_boost"), 30, AttributeModifier.Operation.ADD_VALUE));
                summonAttributes.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(IronboundArtefact.prefix("summon_damage"), 10, AttributeModifier.Operation.ADD_VALUE));

                // Apply the attribute modifiers to the creature
                creature.getAttributes().addTransientAttributeModifiers(summonAttributes);
                event.setCreature(creature);
            }
        }
    }

    @SubscribeEvent
    public static void onAttributesAddedToItem(ItemAttributeModifierEvent event) {
//        if (event.getItemStack().getItem().equals(ItemRegistry.STAFF_OF_MAGI.get()) && event.getItemStack().has(ComponentRegistry.UPGRADE_DATA)) {
//            var data = event.getItemStack().get(ComponentRegistry.UPGRADE_DATA);
//            for (var upgrade : data.getUpgrades().keySet()) {
//                var amount = data.getUpgrades().get(upgrade);
//                AtomicReference<EquipmentSlotGroup> slot = new AtomicReference<>();
//                AtomicReference<AttributeModifier> modifier = new AtomicReference<>();
//
//                event.getModifiers().forEach(a -> {
//                    if (a.modifier().id().equals(upgrade.getId())) {
//                        slot.set(a.slot());
//                        modifier.set(a.modifier());
//                    }
//                });
//                if (slot.get() != null && modifier.get() != null) {
//                    //event.removeModifier(upgrade.getAttribute(), upgrade.getId());
//                    //event.addModifier(upgrade.getAttribute(), modifier.get(), slot.get());
//                }
//            }
//        }
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

    @SubscribeEvent
    public static void onCast(SpellPreCastEvent preCastEvent) {
        if (preCastEvent.getEntity().hasEffect(EffectsRegistry.TIME_STOP)) {
            preCastEvent.setCanceled(true);
        }
    }
}