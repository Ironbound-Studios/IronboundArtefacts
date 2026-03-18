package com.c446.ironbound_artefacts.perks.spellperks;

import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.lang.reflect.Method;

public record PerkEntry(String perkId, String targetSpellId, Method method, Object instance) {
    public PerkEntry {
        if (method.getParameterCount() != 1 || !Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
            throw new RuntimeException("CRITICAL ERROR: Perk method " + method.getName() +
                    " in " + instance.getClass().getSimpleName() + " must have exactly ONE Event parameter!");
        }
    }

    public void invoke(Event event) {
        try {
            method.invoke(instance, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Player resolvePlayer(Event event) {
        // Logic to extract Player from different event types (LivingEvent, SpellEvent, etc.)
        if (event instanceof SpellDamageEvent spellEvent && spellEvent.getSpellDamageSource().get().getEntity() instanceof Player p) return p;
        if (event instanceof LivingIncomingDamageEvent damageEvent && damageEvent.getSource().getEntity() instanceof Player p) return p;
        if (event instanceof LivingEvent livingEvent && livingEvent.getEntity() instanceof Player p) return p;
        return null;
    }
}