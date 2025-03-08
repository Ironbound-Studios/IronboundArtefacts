package com.c446.ironbound_artefacts;

import com.min01.tickrateapi.util.TickrateUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

@Mod(IronboundArtefact.MODID)
public class IronboundArtefact {
    public static final HashMap<Entity, Integer> STOPPED_ENTITIES = new HashMap<>();
    public static final String MODID = "ironbounds_artefacts";
    public static final Logger LOGGER = LogManager.getLogger();

    public IronboundArtefact(IEventBus modEventBus, ModContainer modContainer) {
        ModSetup.register(modEventBus);
//        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        modEventBus.addListener(this::setup);
    }

    public static void freezeEntity(Entity entity, int timeOut) {
        STOPPED_ENTITIES.put(entity, timeOut);
        TickrateUtil.setTimer(entity, 0);
    }

    public static void tickMap() {
        for (var i : STOPPED_ENTITIES.keySet()) {
            if (STOPPED_ENTITIES.get(i) <= 0) {
                TickrateUtil.resetTickrate(i);

            } else {
                STOPPED_ENTITIES.put(i, STOPPED_ENTITIES.get(i) - 1);
            }
        }
    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public void setup(final FMLCommonSetupEvent event) {
    }

    public static final class ContributorUUIDS {
        public static final String ACE = "34e0c700-66dd-4932-8e0b-0083076609d5";
        public static final String AMADHE = "387aa6d4-f388-400f-b9c5-1514f238ceae";
        public static final String AMON = "2980a99e-8582-4f63-9b82-f7117bc8be2c";
        public static final String CATMOTH = "8330d120-2e4b-48c1-bfdc-8d4920dee119";
        public static final String ENDER = "6302e022-0de6-4254-bd2c-7c5cc7b27982";
        public static final String IRON = "c3adad79-e88a-4f15-bd61-c58766d725e9";
        public static final String TAR = "64d64b70-fde8-467e-ad70-271d5ddf26f6";
        public static final String THEKILLAGER = "0150f698-e592-43f5-b78f-b6d6d5755772";
        public static final String NINJA_FOX = "834a2387-6dec-4d57-8bd3-72be2a54ed54";
        public static final String STYLY = "03d1d7ca-657f-45ad-a51b-1f5dc85b2f4c";
        public static final String TOMATO = "5ff3e718-e002-4c48-bdc2-c638477620e4";
        public static ArrayList<String> CONTRIBUTOR_LIST = new ArrayList<>();

        static {
            CONTRIBUTOR_LIST.add(ACE);
            CONTRIBUTOR_LIST.add(NINJA_FOX);
            CONTRIBUTOR_LIST.add(AMADHE);
            CONTRIBUTOR_LIST.add(AMON);
            CONTRIBUTOR_LIST.add(CATMOTH);
            CONTRIBUTOR_LIST.add(ENDER);
            CONTRIBUTOR_LIST.add(IRON);
            CONTRIBUTOR_LIST.add(TAR);
            CONTRIBUTOR_LIST.add(TOMATO);
            CONTRIBUTOR_LIST.add(THEKILLAGER);
            CONTRIBUTOR_LIST.add(STYLY);
        }

        public static boolean isAdminOrDev(ServerPlayer player) {
            return player.hasPermissions(2) || player.getStringUUID().equals(AMON) || player.getStringUUID().equals(ACE);
        }
    }
}
