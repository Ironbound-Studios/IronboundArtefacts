package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.common.effects.IronboundMobEffect;
import com.c446.ironbound_artefacts.common.effects.MarkoheshkirEffect;
import com.c446.ironbound_artefacts.common.effects.StoppingTimeEffect;
import com.c446.ironbound_artefacts.common.effects.TimeStopEffect;
import com.c446.ironbound_artefacts.common.spells.enthrall.EnthralledEffect;
import com.c446.ironbound_artefacts.common.items.impl.lore_items.StaffOfMagi;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.c446.ironbound_artefacts.IBA.MODID;

public class RegistryEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, MODID);
    public static final DeferredHolder<MobEffect, EnthralledEffect> ENTHRALLED = EFFECTS.register("enthralled", () -> new EnthralledEffect(MobEffectCategory.HARMFUL, rgbToInt(255, 0, 255)));
    public static final DeferredHolder<MobEffect, IronboundMobEffect.VoidPoison> VOID_POISON = EFFECTS.register("void_poison", () -> new IronboundMobEffect.VoidPoison(MobEffectCategory.BENEFICIAL, rgbToInt(120, 0, 200)));
    public static final DeferredHolder<MobEffect, MobEffect> MANA_REGEN = EFFECTS.register("mana_regen", () -> new IronboundMobEffect(MobEffectCategory.BENEFICIAL, rgbToInt(0, 0, 200)).addAttributeModifier(AttributeRegistry.MANA_REGEN, IBA.p("mana_regen_effect"), 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final DeferredHolder<MobEffect, StoppingTimeEffect> TIME_STOP_CASTER = EFFECTS.register("stopping_time", () -> new StoppingTimeEffect(MobEffectCategory.BENEFICIAL, rgbToInt(120, 0, 200)));
    //public static final DeferredHolder<MobEffect, SummonTimer> SIMULACRUM_SUMMON = EFFECTS.register("summoned_simulacrum", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, rgbToInt(120, 0, 200)));


    public static final DeferredHolder<MobEffect, IronboundMobEffect.IgnoreAntimagic> AFTERSHOCK = EFFECTS.register("aftershock", () -> new IronboundMobEffect.IgnoreAntimagic(MobEffectCategory.BENEFICIAL, rgbToInt(0, 160, 160)));
    public static final DeferredHolder<MobEffect, IronboundMobEffect.IgnoreAntimagic> LIGHTNING_VOICE = EFFECTS.register("lightning_voice", () -> new IronboundMobEffect.IgnoreAntimagic(MobEffectCategory.BENEFICIAL, rgbToInt(0, 160, 160)));
    public static final DeferredHolder<MobEffect, IronboundMobEffect.IgnoreAntimagic> HEAVENLY_DESCENT = EFFECTS.register("heavenly_descent", () -> new IronboundMobEffect.IgnoreAntimagic(MobEffectCategory.BENEFICIAL, rgbToInt(0, 160, 160)));
    public static final DeferredHolder<MobEffect, IronboundMobEffect.IgnoreAntimagic> LIGHTNING_SHREDDED = EFFECTS.register("lightning_res_down",
            () -> (IronboundMobEffect.IgnoreAntimagic) new IronboundMobEffect
                    .IgnoreAntimagic(MobEffectCategory.BENEFICIAL, rgbToInt(0, 160, 160))
                    .addAttributeModifier(AttributeRegistry.LIGHTNING_MAGIC_RESIST, IBA.p("res_shredded_lightning"), -0.5d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));


    public static final DeferredHolder<MobEffect, IronboundMobEffect.IgnoreAntimagic> CORPSE_MOUNTAIN_BUFF = EFFECTS.register("corpse_mountain_buff",
            () -> (IronboundMobEffect.IgnoreAntimagic) new IronboundMobEffect.IgnoreAntimagic(MobEffectCategory.BENEFICIAL, rgbToInt(80,20,20))
                    .addAttributeModifier(AttributeRegistry.BLOOD_SPELL_POWER, IBA.p("corpse_mountain_buff"), 0.025, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(AttributeRegistry.SUMMON_DAMAGE, IBA.p("corpse_mountain_buff"), 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );
    public static final DeferredHolder<MobEffect, IronboundMobEffect.IgnoreAntimagic> FIRE_HOLY_DEF_DOWN = EFFECTS.register("corpse_mountain_debuff",
            () -> (IronboundMobEffect.IgnoreAntimagic) new IronboundMobEffect.IgnoreAntimagic(MobEffectCategory.BENEFICIAL, rgbToInt(80,20,20))
                    .addAttributeModifier(AttributeRegistry.FIRE_MAGIC_RESIST, IBA.p("corpse_mountain_debuff"), -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(AttributeRegistry.HOLY_MAGIC_RESIST, IBA.p("corpse_mountain_debuff"), -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

    public static final DeferredHolder<MobEffect, IronboundMobEffect.IgnoreAntimagic> HP_DOWN = EFFECTS.register("corpse_mountain_hp_down",
            () -> (IronboundMobEffect.IgnoreAntimagic) new IronboundMobEffect.IgnoreAntimagic(MobEffectCategory.BENEFICIAL, rgbToInt(80,20,20))
                    .addAttributeModifier(Attributes.MAX_HEALTH, IBA.p("corpse_mountain_hp_down"), -0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

    public static final DeferredHolder<MobEffect, MobEffect> INSIDE_CORPSE_MOUNTAIN = EFFECTS.register("inside_the_corpse_mountain", () -> new IronboundMobEffect.IgnoreAntimagic(MobEffectCategory.BENEFICIAL, rgbToInt(80, 0, 0)));

    //public static final DeferredHolder<MobEffect, PlaneShiftEffect> PLANE_SHIFT = EFFECTS.register("plane_shifting",  () -> new PlaneShiftEffect(MobEffectCategory.NEUTRAL, rgbToInt(255,0,255)));

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, MODID);
    public static final Holder<Potion> LESSER_DIVINE_GIFT = POTIONS.register("divine_favour_lesser", () -> new Potion(
                    new MobEffectInstance(MobEffects.HUNGER, 1, 0)
//            new MobEffectInstance(MobEffectRegistry.INSTANT_MANA, 1, 2),
//            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0),
//            new MobEffectInstance(MobEffectRegistry.ANGEL_WINGS, 6000, 0))
            )
    );

    public static final Holder<Potion> WINE = POTIONS.register("wine_potion", () -> new Potion(
            new MobEffectInstance(MobEffects.CONFUSION, 20 * 120, 0),
            new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 120, 0),
            new MobEffectInstance(MobEffects.HUNGER, 20 * 120, 0)
    ));

    public static final Holder<Potion> DIVINE_GIFT = POTIONS.register("divine_favour_greater", () -> new Potion(
                    new MobEffectInstance(MobEffects.HUNGER, 1, 0)
//            new MobEffectInstance(MobEffectRegistry.INSTANT_MANA, 1, 4),
//            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 12000, 2),
//            new MobEffectInstance(MobEffects.REGENERATION, 12000, 2),
//            new MobEffectInstance(MobEffectRegistry.ANGEL_WINGS, 12000, 0)
            )
    );

    public static final DeferredHolder<MobEffect, MarkoheshkirEffect> FIRE_AFFINITY = EFFECTS.register("fire_affinity", () ->
            new MarkoheshkirEffect(MobEffectCategory.BENEFICIAL, rgbToInt(150, 0, 0))
                    .addAttributeModifier(AttributeRegistry.FIRE_SPELL_POWER.getDelegate(), StaffOfMagi.MARKOHESHKIR_ATTRIBUTE, 0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(AttributeRegistry.FIRE_MAGIC_RESIST.getDelegate(), StaffOfMagi.MARKOHESHKIR_ATTRIBUTE, 0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));


    public static final DeferredHolder<MobEffect, MarkoheshkirEffect> ICE_AFFINITY = EFFECTS.register("ice_affinity", () ->
            new MarkoheshkirEffect(MobEffectCategory.BENEFICIAL, rgbToInt(0, 0, 150))
                    .addAttributeModifier(AttributeRegistry.ICE_SPELL_POWER.getDelegate(), StaffOfMagi.MARKOHESHKIR_ATTRIBUTE, 0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(AttributeRegistry.ICE_MAGIC_RESIST.getDelegate(), StaffOfMagi.MARKOHESHKIR_ATTRIBUTE, 0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, MarkoheshkirEffect> NATURE_AFFINITY = EFFECTS.register("nature_affinity", () ->
            new MarkoheshkirEffect(MobEffectCategory.BENEFICIAL, rgbToInt(0, 150, 0))
                    .addAttributeModifier(AttributeRegistry.NATURE_SPELL_POWER.getDelegate(), StaffOfMagi.MARKOHESHKIR_ATTRIBUTE, 0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(AttributeRegistry.NATURE_MAGIC_RESIST.getDelegate(), StaffOfMagi.MARKOHESHKIR_ATTRIBUTE, 0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, MarkoheshkirEffect> LIGHTNING_AFFINITY = EFFECTS.register("lightning_affinity", () ->
            new MarkoheshkirEffect(MobEffectCategory.BENEFICIAL, rgbToInt(150, 150, 0))
                    .addAttributeModifier(AttributeRegistry.LIGHTNING_SPELL_POWER.getDelegate(), StaffOfMagi.MARKOHESHKIR_ATTRIBUTE, 0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(AttributeRegistry.LIGHTNING_MAGIC_RESIST.getDelegate(), StaffOfMagi.MARKOHESHKIR_ATTRIBUTE, 0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<MobEffect, TimeStopEffect> TIME_FROZEN = EFFECTS.register("time_frozen", () -> new TimeStopEffect(MobEffectCategory.BENEFICIAL, rgbToInt(120, 0, 200)).addAttributeModifier(Attributes.MOVEMENT_SPEED, IBA.p("time_stop_speed_mod"), -1d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    /*
        public static final DeferredHolder<MobEffect, MobEffect> DEMO_EFFECT = EFFECTS.register("demo_name", ()->{
            return new MobEffect(MobEffectCategory.BENEFICIAL, rgbToInt(255,255,255))



        });*/
    public static int rgbToInt(int red, int green, int blue) {
        return ((red << 16) | (green << 8) | blue);
    }
}
