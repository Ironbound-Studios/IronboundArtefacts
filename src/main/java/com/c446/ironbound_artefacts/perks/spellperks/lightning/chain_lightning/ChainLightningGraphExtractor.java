package com.c446.ironbound_artefacts.perks.spellperks.lightning.chain_lightning;

import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.ChainLightning;
import net.minecraft.world.entity.Entity;
import java.util.*;

public class ChainLightningGraphExtractor {

    public static class Graph {
        public final UUID graphId = UUID.randomUUID();
        public final Entity caster;

        // Adjacency list: Parent -> Children
        public final Map<Entity, Set<Entity>> edges = new HashMap<>();

        // Inverse lookup: Child -> Parent (Our Lookup Table)
        private final Map<Entity, Entity> parentCache = new HashMap<>();

        // Jump distance tracking
        public final Map<Entity, Integer> depths = new HashMap<>();

        public Graph(Entity caster) {
            this.caster = caster;
            this.depths.put(caster, 0);
        }

        /**
         * Adds an edge and immediately populates the parent lookup table and depth map.
         */
        public void addEdge(Entity from, Entity to) {
            // Standard Adjacency List
            edges.computeIfAbsent(from, k -> new HashSet<>()).add(to);

            // The Lookup Table: Mapping child back to parent for Backflow
            parentCache.put(to, from);

            // Track how many jumps away from caster we are
            int parentDepth = depths.getOrDefault(from, 0);
            depths.put(to, parentDepth + 1);
        }

        public Entity getParent(Entity child) {
            return parentCache.get(child);
        }

        public int getDepth(Entity entity) {
            return depths.getOrDefault(entity, 0);
        }
    }

    private static class Tracker {
        Graph graph;
        Set<Entity> currentWave = new HashSet<>();
        Set<Entity> nextWave = new HashSet<>();
        int lastTick = -1;
    }

    private static final Map<UUID, Tracker> ACTIVE = new HashMap<>();

    /**
     * Finds the parent of a node in O(1) time using the graph's internal lookup table.
     */
    public static Entity findParent(Entity child, Graph graph) {
        if (graph == null) return null;
        return graph.getParent(child);
    }

    public static Graph capture(SpellDamageEvent event) {
        SpellDamageSource source = event.getSpellDamageSource();
        Entity direct = source.getDirectEntity();

        if (!(direct instanceof ChainLightning chain)) return null;

        Entity victim = event.getEntity();
        UUID id = chain.getUUID();

        Tracker tracker = ACTIVE.computeIfAbsent(id, k -> {
            Tracker t = new Tracker();
            t.graph = new Graph(source.getEntity());
            return t;
        });

        // Idempotency: Don't process the same hit twice for the same event
        if (tracker.currentWave.contains(victim) || tracker.nextWave.contains(victim)) {
            return tracker.graph;
        }

        int tick = chain.tickCount;

        // Wave management based on projectile ticks
        if (tracker.lastTick != tick) {
            if (!tracker.nextWave.isEmpty()) {
                tracker.currentWave = new HashSet<>(tracker.nextWave);
                tracker.nextWave.clear();
            }
            tracker.lastTick = tick;
        }

        // First hit (Root)
        if (tracker.currentWave.isEmpty()) {
            tracker.graph.addEdge(tracker.graph.caster, victim);
            tracker.currentWave.add(victim);
        } else {
            // Find parent in the previous wave (closest proximity)
            Entity parent = tracker.currentWave.stream()
                    .min(Comparator.comparingDouble(e -> e.distanceToSqr(victim)))
                    .orElse(tracker.graph.caster);

            tracker.graph.addEdge(parent, victim);
            tracker.nextWave.add(victim);
        }

        // Cleanup: Remove tracker when chain is dead or removed
        if (chain.isRemoved() || !chain.isAlive()) {
            ACTIVE.remove(id);
        }

        return tracker.graph;
    }

    /**
     * Recursive debug printer for the console
     */
    public static void printGraph(Graph graph) {
        if (graph == null) return;
        System.out.println("=== Chain Lightning Graph: " + graph.graphId + " ===");
        Set<Entity> visited = new HashSet<>();
        printRecursive(graph.caster, graph, "", visited);
        System.out.println("===========================================");
    }

    private static void printRecursive(Entity node, Graph graph, String indent, Set<Entity> visited) {
        if (node == null || visited.contains(node)) return;
        visited.add(node);
        System.out.println(indent + node.getName().getString() + " [Depth: " + graph.getDepth(node) + "]");
        Set<Entity> children = graph.edges.get(node);
        if (children != null) {
            for (Entity child : children) {
                printRecursive(child, graph, indent + "  └─ ", visited);
            }
        }
    }
}