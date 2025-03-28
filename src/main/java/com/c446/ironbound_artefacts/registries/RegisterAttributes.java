package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IronboundArtefact;
import com.c446.ironbound_artefacts.entities.archmage.ArchmageEntity;
import com.c446.ironbound_artefacts.entities.simulacrum.SimulacrumEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = IronboundArtefact.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RegisterAttributes {
    @SubscribeEvent
    public static void registerAttbitutes(EntityAttributeCreationEvent event) {
        event.put(IBEntitiesReg.SIMULACRUM.get(), SimulacrumEntity.createAttributes().build());
        event.put(IBEntitiesReg.ARCHMAGE.get(), ArchmageEntity.createAttributes().build());
    }
}
