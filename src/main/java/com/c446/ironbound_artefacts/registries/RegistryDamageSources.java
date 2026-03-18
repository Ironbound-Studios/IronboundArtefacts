package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IBA;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryDamageSources {
    public static final ResourceKey<DamageType> VOID_DAMAGE = register("void_damage");
    public static final ResourceKey<DamageType> IMMORTAL_ART = register("immortal_arts");

    public static final ResourceKey<DamageType> VOID_SWORD_ = register("void_sword");
    public static final ResourceKey<DamageType> VOID_SWORD_ZERO = register("void_sword_zero_damage");
    public static final ResourceKey<DamageType> VOID_SWORD_LIGHT = register("void_sword_light_damage");
    public static final ResourceKey<DamageType> VOID_SWORD_HEAVEN = register("void_sword_heaven_damage");
    public static final ResourceKey<DamageType> VOID_SWORD_UPPER = register("void_sword_upper_damage");
    public static final ResourceKey<DamageType> VOID_SWORD_FUTURE = register("void_sword_future_damage");
    public static final ResourceKey<DamageType> VOID_SWORD_HOPE = register("void_sword_hope_damage");

    public static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, IBA.p(name));
    }

    //TODO: need a better way to get the registry access without going through the level each time
    private static final Map<ResourceKey<DamageType>, Holder<DamageType>> CACHE = new ConcurrentHashMap<>();

    public static Holder<DamageType> getHolderFromResource(Entity entity, ResourceKey<DamageType> damageTypeResourceKey) {
        // Return from cache if already stored
        return CACHE.computeIfAbsent(damageTypeResourceKey, key -> {
            var option = entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolder(key);

            // If present, store & return
            if (option.isPresent()) {
                return option.get();
            }

            // Otherwise cache the fallback value
            return entity.level().damageSources().genericKill().typeHolder();
        });
    }
}
