package com.c446.ironbound_artefacts.items.impl.lore_items;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.function.Supplier;

public class StaffOfMagi extends StaffItem implements IPresetSpellContainer {
    public static final ResourceLocation MARKOHESHKIR_ATTRIBUTE = IronboundArtefact.prefix("markoheshkir_attribute");
    public static HashMap<Supplier<SchoolType>, Holder<MobEffect>> MarkoMap = new HashMap<java.util.function.Supplier<io.redspace.ironsspellbooks.api.spells.SchoolType>, Holder<MobEffect>>();

    static {
        MarkoMap.put(SchoolRegistry.FIRE, EffectsRegistry.FIRE_AFFINITY);
        MarkoMap.put(SchoolRegistry.ICE, EffectsRegistry.ICE_AFFINITY);
        MarkoMap.put(SchoolRegistry.LIGHTNING, EffectsRegistry.LIGHTNING_AFFINITY);
        MarkoMap.put(SchoolRegistry.NATURE, EffectsRegistry.NATURE_AFFINITY);
    }

    public StaffOfMagi(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        //itemStack.set(ComponentRegistry.SPELL_CONTAINER, new SpellContainer(1, true, false, false, new SpellSlot[]{new SpellSlot(new SpellData(CustomSpellRegistry..get(), 1), 0)}));
    }

    @AutoSpellConfig
    public static class KereshkaFavor extends AbstractSpell {
        public final ResourceLocation spellId = IronboundArtefact.prefix("kereshka_favor");


        final DefaultConfig defaultConfig = new DefaultConfig().setCooldownSeconds(500).setMaxLevel(1).setMinRarity(SpellRarity.LEGENDARY).setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE).build();

        public KereshkaFavor() {
            this.spellPowerPerLevel = 0;
            this.baseSpellPower = 5;
        }

        @Override
        public ResourceLocation getSpellResource() {
            return spellId;
        }

        @Override
        public DefaultConfig getDefaultConfig() {
            return defaultConfig;
        }

        @Override
        public CastType getCastType() {
            return CastType.INSTANT;
        }

        @Override
        public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
            if (entity instanceof Player player && level instanceof ServerLevel) {
                boolean effectApplied = applyEffectIfFocus(player.getOffhandItem(), player, spellLevel);
                if (!effectApplied) {
                    for (ItemStack item : player.getInventory().items) {
                        if (applyEffectIfFocus(item, player, spellLevel)) {
                            break;
                        }
                    }
                }
            }
            super.onCast(level, spellLevel, entity, castSource, playerMagicData);
        }

        private boolean applyEffectIfFocus(ItemStack item, Player player, int spellLevel) {
            for (Supplier<SchoolType> school : MarkoMap.keySet()) {
                if (school.get().isFocus((item))) {
                    player.addEffect(new MobEffectInstance(MarkoMap.get(school), 60 * (int) (getSpellPower(spellLevel, player) * (baseSpellPower + spellPowerPerLevel)), 0, false, false));
                    return true;
                }
            }
            return false;
        }
    }
}
