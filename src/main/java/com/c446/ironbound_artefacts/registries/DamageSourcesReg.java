package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IronboundArtefact;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class DamageSourcesReg {
    public static final ResourceKey<DamageType> VOID_DAMAGE = register("void_damage");
    public static final ResourceKey<DamageType> IMMORTAL_ART = register("immortal_arts");

    public static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, IronboundArtefact.prefix(name));
    }
}
