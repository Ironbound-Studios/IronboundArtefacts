package com.c446.ironbound_artefacts.items.impl.regular;

import com.c446.ironbound_artefacts.items.UserDependantCurios;
import com.c446.ironbound_artefacts.registries.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.spells.NoneSpell;
import io.redspace.ironsspellbooks.spells.ender.PortalSpell;
import io.redspace.ironsspellbooks.spells.evocation.SpectralHammerSpell;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class DeckOfAllThings extends UserDependantCurios {
    public DeckOfAllThings(Properties p) {
        super(p);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand usedHand) {
        if (player instanceof ServerPlayer serverPlayer) {

            var effects = BuiltInRegistries.MOB_EFFECT.stream().toArray();
            var effectsLength = effects.length;
            var randeffect = level.random.nextIntBetweenInclusive(0, effectsLength);

            var spells = SpellRegistry.REGISTRY.stream().toArray();
            var randspell = level.random.nextIntBetweenInclusive(0, spells.length);
            AbstractSpell spell = (AbstractSpell) spells[randspell];

            //


            while (spell instanceof NoneSpell || spell instanceof PortalSpell || spell instanceof SpectralHammerSpell) {
                spell = (AbstractSpell) spells[level.random.nextIntBetweenInclusive(0, spells.length)];
            }

            spell.castSpell(level, spell.getMaxLevel(), serverPlayer, CastSource.SCROLL, false);
            player.getCooldowns().addCooldown(this, (int) (20 * (17 - player.getAttributeValue(AttributeRegistry.INSIGHT))));
        }
        return super.use(level, player, usedHand);
    }
}