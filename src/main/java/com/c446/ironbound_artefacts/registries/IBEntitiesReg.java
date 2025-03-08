package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.entities.AbstractHomingEntity;
import com.c446.ironbound_artefacts.entities.archmage.ArchmageEntity;
import com.c446.ironbound_artefacts.entities.comet.AstralCometEntity;
import com.c446.ironbound_artefacts.entities.force_cage.ForceWall;
import com.c446.ironbound_artefacts.entities.simulacrum.SimulacrumEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.common.Tags;
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
            EntityType.Builder.<ArchmageEntity>of(ArchmageEntity::new, MobCategory.MISC)
                    .sized(0.6f, 1.8f)
                    .setTrackingRange(30)
                    .noSave().noSummon().eyeHeight(1.75F)
                    .setShouldReceiveVelocityUpdates(true));

    public static final DeferredHolder<EntityType<?>, EntityType<ForceWall>> FORCE_WALL = registerEntity(
            "force_wall",
            EntityType.Builder.of(ForceWall::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .noSave()
                    .eyeHeight(0.25f/2)
                    .setShouldReceiveVelocityUpdates(true));

//    public static final DeferredHolder<EntityType<?>, EntityType<AbstractHomingEntity>> HOMING = registerEntity(
//            "homing_missile",
//            EntityType.Builder.<AstralCometEntity>of(AbstractHomingEntity::new, MobCategory.MISC)
//                    .sized(1.0f, 2.0f)
//                    .noSave()
//                    .setTrackingRange(10)
//                    .setShouldReceiveVelocityUpdates(true));

}
