package com.c446.ironbound_artefacts.perks.spellperks;

import com.c446.ironbound_artefacts.IBA;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforgespi.language.ModFileScanData;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.util.*;

public final class PerkManager {

    private static final List<String> ALL_PERKS_ID = new ArrayList<>();
    private static final Map<Class<? extends Event>, List<PerkEntry>> CACHED_PERKS = new HashMap<>();

    public static void registerAll(ModFileScanData scanData) {
        IBA.LOGGER.info("DEBUG: Starting Perk Scan...");

        scanData.getAnnotatedBy(PerkTree.class, ElementType.TYPE).forEach(treeData -> {
            try {
                // Use the correct classloader for the mod
                Class<?> treeClass = Class.forName(treeData.clazz().getClassName());
                Object instance = treeClass.getDeclaredConstructor().newInstance();

                PerkTree treeAnno = treeClass.getAnnotation(PerkTree.class);
                // Replace ":" with "." to create a safe namespace segment
                String spellNamespace = treeAnno.spellId().replace(":", ".");

                for (Method method : treeClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(SpellPerk.class)) {
                        // CRITICAL: Ensure we can invoke the method even if it's not public
                        method.setAccessible(true);

                        SpellPerk perkAnno = method.getAnnotation(SpellPerk.class);
                        String namespacedPerkId = spellNamespace + ":" + perkAnno.perkId();

                        // Validation: Ensure the method has exactly one parameter
                        if (method.getParameterCount() != 1) {
                            IBA.LOGGER.error("Perk {} in {} has invalid parameters!",
                                    namespacedPerkId, treeClass.getSimpleName());
                            continue;
                        }

                        @SuppressWarnings("unchecked")
                        Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[0];

                        // Create entry and add to cache
                        PerkEntry entry = new PerkEntry(namespacedPerkId, treeAnno.spellId(), method, instance);
                        addPerk(eventClass, entry);

                        IBA.LOGGER.info("Registered Namespaced Perk: {}", namespacedPerkId);
                    }
                }
            } catch (Exception e) {
                IBA.LOGGER.error("DEBUG: Failed to load PerkTree class", e);
            }
        });

        // Register listeners using a helper to handle generic typing
        CACHED_PERKS.keySet().forEach(PerkManager::registerBusListener);
    }

    // Helper to satisfy NeoForge's generic requirements for dynamic listeners
    private static <T extends Event> void registerBusListener(Class<T> eventClass) {
        NeoForge.EVENT_BUS.addListener(eventClass, PerkManager::onGlobalEvent);
        IBA.LOGGER.info("DEBUG: Monitoring Bus for: {}", eventClass.getSimpleName());
    }

    private static void onGlobalEvent(Event event) {
        // --- SPELL SPECIFIC DEBUGGING ---
        if (event instanceof SpellOnCastEvent spellEvent) {
            String firedId = spellEvent.getSpellId();
            IBA.LOGGER.info("DEBUG: [BUS EVENT] SpellOnCastEvent fired for: {}", firedId);

            // Check if we even have this event class in our map
            if (!CACHED_PERKS.containsKey(SpellOnCastEvent.class)) {
                IBA.LOGGER.error("DEBUG: Map does NOT contain SpellOnCastEvent.class key!");
            }
        }

        List<PerkEntry> entries = CACHED_PERKS.get(event.getClass());
        if (entries == null) return;

        for (PerkEntry entry : entries) {
            Player player = entry.resolvePlayer(event);

            if (player != null) {
                boolean hasPerk = PerkUtils.playerHasPerk(player, entry.perkId());

                // Detailed check for the specific test perk
                if (entry.perkId().equals("test_perk")) {
                    IBA.LOGGER.info("DEBUG: Checking 'test_perk' for player {}. Result: {}", player.getName().getString(), hasPerk);
                }

                if (hasPerk) {
                    IBA.LOGGER.info("DEBUG: INVOKING Perk: {}", entry.perkId());
                    entry.invoke(event);
                }
            } else {
                IBA.LOGGER.warn("DEBUG: Could not resolve player for event: {}", event.getClass().getSimpleName());
            }
        }
    }

    public static void addPerk(Class<? extends Event> eventClass, PerkEntry entry) {
        CACHED_PERKS.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(entry);
        if (!ALL_PERKS_ID.contains(entry.perkId())) ALL_PERKS_ID.add(entry.perkId());
    }

    public static List<String> getAllPerkIds() {
        System.out.println("SUTGGESTION REQUESTED: " + ALL_PERKS_ID.size() + " perks available.");
        return Collections.unmodifiableList(ALL_PERKS_ID);
    }
}