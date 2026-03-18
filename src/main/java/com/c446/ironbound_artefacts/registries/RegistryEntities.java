package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.common.entities.ArchmageEntity;
import com.c446.ironbound_artefacts.common.entities.LastPrismEntity;
import com.c446.ironbound_artefacts.common.entities.SimulacrumEntity;
import com.c446.ironbound_artefacts.common.entities.SwordStormEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.c446.ironbound_artefacts.IBA.MODID;

@EventBusSubscriber(modid = IBA.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RegistryEntities {


    @SubscribeEvent
    public static void registerAttbitutes(EntityAttributeCreationEvent event) {
        event.put(RegistryEntities.SIMULACRUM.get(), SimulacrumEntity.createAttributes().build());
        event.put(RegistryEntities.ARCHMAGE.get(), ArchmageEntity.createAttributes().build());
    }

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);
    public static final DeferredHolder<EntityType<?>, EntityType<SimulacrumEntity>> SIMULACRUM = registerEntity(
            "simulacrum",
            EntityType.Builder.<SimulacrumEntity>of(SimulacrumEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f)
                    .setTrackingRange(30)
                    .noSave().noSummon().eyeHeight(1.75F)
                    .setShouldReceiveVelocityUpdates(true));
    public static final DeferredHolder<EntityType<?>, EntityType<ArchmageEntity>> ARCHMAGE = registerEntity(
            "archmage",
            EntityType.Builder.<ArchmageEntity>of(ArchmageEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f)
                    .setTrackingRange(30)
                    .noSave().noSummon().eyeHeight(1.75F)
                    .setShouldReceiveVelocityUpdates(true));

    public static final DeferredHolder<EntityType<?>, EntityType<LastPrismEntity>> LAST_PRISM =
            ENTITIES.register("prism_lightning", () -> EntityType.Builder.of(LastPrismEntity::new, MobCategory.MISC)
                    .sized(.25F, .25F) // Hitbox size (usually small for visual beams)
                    .eyeHeight(0.125F)
                    .clientTrackingRange(16)
                    .updateInterval(2)
                    .build("prism_lightning"));

    public static final DeferredHolder<EntityType<?>, EntityType<SwordStormEntity>> SWORD_STORM =
            ENTITIES.register("sword_storm", () -> EntityType.Builder.<SwordStormEntity>of(SwordStormEntity::new, MobCategory.MISC)
                    .sized(.25F, .25F) // Hitbox size (usually small for visual beams)
                    .eyeHeight(0.125F)
                    .clientTrackingRange(16)
                    .updateInterval(2)
                    .build("sword_storm"));

    static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(MODID + ":" + name));
    }

//    public static final DeferredHolder<EntityType<?>, EntityType<AbstractHomingEntity>> HOMING = registerEntity(
//            "homing_missile",
//            EntityType.Builder.<AstralCometEntity>of(AbstractHomingEntity::new, MobCategory.MISC)
//                    .sized(1.0f, 2.0f)
//                    .noSave()
//                    .setTrackingRange(10)
//                    .setShouldReceiveVelocityUpdates(true));

}
