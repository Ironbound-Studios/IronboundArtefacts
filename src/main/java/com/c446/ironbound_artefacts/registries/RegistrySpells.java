package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.common.spells.*;
import com.c446.ironbound_artefacts.common.spells.void_sword.SwordStormSpell;
import com.c446.ironbound_artefacts.common.spells.void_sword.Zero;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.c446.ironbound_artefacts.IBA.MODID;

public class RegistrySpells {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.REGISTRY, MODID);


    public static final Supplier<AbstractSpell> ZERO = registerSpell(new Zero());
    public static final Supplier<AbstractSpell> HOPE = registerSpell(new SwordStormSpell());

    public static final Supplier<AbstractSpell> TIME_STOP;
    public static final Supplier<AbstractSpell> WISH;
    public static final Supplier<AbstractSpell> DIVINE_BOON;
    public static final Supplier<AbstractSpell> PLANE_SHIFT;
    public static final Supplier<AbstractSpell> SIMULACRUM;
    public static final Supplier<AbstractSpell> HYPNOTIC_PATTERN;
    public static final Supplier<AbstractSpell> HEAVENLY_PUNISHMENT;
    public static final Supplier<AbstractSpell> LAST_PRISM_SPELL;

    static {
        LAST_PRISM_SPELL = registerSpell(new LastPrismSpell());
        TIME_STOP = registerSpell(new TimeStopSpell());
        WISH = registerSpell(new WishSpell());
        DIVINE_BOON = registerSpell(new DivineGiftSpell());
        PLANE_SHIFT = registerSpell(new PlaneShift());
        HYPNOTIC_PATTERN = registerSpell(new HypnoticPattern());
        SIMULACRUM = registerSpell(new SimulacrumSpell());
        HEAVENLY_PUNISHMENT=registerSpell(new HeavenlyPunishment());

    }

    private static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }
}
