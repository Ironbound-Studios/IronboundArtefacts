package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.entities.archmage.ArchmageEntity;
import com.c446.ironbound_artefacts.entities.comet.AstralCometEntity;
import com.c446.ironbound_artefacts.entities.simulacrum.SimulacrumEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.c446.ironbound_artefacts.IronboundArtefact.MODID;

public class IBEntitiesReg {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);

    static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(MODID + ":" + name));
    }

    public static final DeferredHolder<EntityType<?>, EntityType<SimulacrumEntity>> SIMULACRUM = registerEntity(
            "simulacrum",
            EntityType.Builder.<SimulacrumEntity>of(SimulacrumEntity::new, MobCategory.MISC)
                    .sized(0.6f, 1.8f)
                    .setTrackingRange(30)
                    .noSave().noSummon().eyeHeight(1.75F)
                    .setShouldReceiveVelocityUpdates(true));

    public static final DeferredHolder<EntityType<?>, EntityType<ArchmageEntity>> ARCHMAGE = registerEntity(
            "archmage",
            EntityType.Builder.of(ArchmageEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f)
                    .setTrackingRange(30)
                    .eyeHeight(1.75F)
                    .setShouldReceiveVelocityUpdates(true));

    public static final DeferredHolder<EntityType<?>, EntityType<AstralCometEntity>> COMET = registerEntity(
            "comet",
            EntityType.Builder.<AstralCometEntity>of(AstralCometEntity::new, MobCategory.MISC)
                    .sized(1.0f, 2.0f)
                    .noSave()
                    .setTrackingRange(10)
                    .setShouldReceiveVelocityUpdates(true));
}
