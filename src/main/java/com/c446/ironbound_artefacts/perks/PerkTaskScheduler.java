package com.c446.ironbound_artefacts.perks;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
public class PerkTaskScheduler {
    private static final List<DelayedTask> TASKS = new ArrayList<>();

    private record DelayedTask(int delay, Runnable action) {}

    public static void schedule(int ticks, Runnable action) {
        TASKS.add(new DelayedTask(ticks, action));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (TASKS.isEmpty()) return;

        List<DelayedTask> toRun = new ArrayList<>();
        TASKS.replaceAll(task -> {
            if (task.delay <= 1) {
                toRun.add(task);
                return null;
            }
            return new DelayedTask(task.delay - 1, task.action);
        });

        TASKS.removeIf(java.util.Objects::isNull);
        toRun.forEach(task -> task.action.run());
    }
}