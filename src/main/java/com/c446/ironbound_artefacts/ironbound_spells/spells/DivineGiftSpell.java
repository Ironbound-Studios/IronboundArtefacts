package com.c446.ironbound_artefacts.ironbound_spells.spells;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.registries.EffectsRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class DivineGiftSpell extends AbstractSpell {
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(60 * 20)
            .build();
    private final ResourceLocation spellId = IronboundArtefact.prefix("divine_boon");

    public DivineGiftSpell() {
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.baseManaCost = 150;
        this.manaCostPerLevel = 50;
        this.castTime = 100;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.divine_gift.amount", Utils.stringTruncation(spellLevel, 0))
        );
    }

    @Override
    public boolean canBeCraftedBy(Player player) {
        return true;
    }

    @Override
    public int getSpellCooldown() {
        return 60 * 20 * 20;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (entity instanceof Player p) {
            var potion = new ItemStack(Items.POTION, spellLevel);
//            if (entity.getAttributeValue(AttributeRegistry.HOLY_SPELL_POWER) * entity.getAttributeValue(AttributeRegistry.SPELL_POWER) >= 2) {
            potion.set(DataComponents.POTION_CONTENTS, new PotionContents(EffectsRegistry.DIVINE_GIFT));
//            } else {
//                potion.set(DataComponents.POTION_CONTENTS, new PotionContents(EffectsRegistry.DIVINE_GIFT));
//            }
            p.addItem(potion);
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}
