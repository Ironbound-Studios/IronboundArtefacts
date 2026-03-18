package com.c446.ironbound_artefacts.perks.spellperks;

import com.c446.ironbound_artefacts.registries.RegistryAttachment;
import net.minecraft.world.entity.player.Player;

public class PerkUtils {
    public static boolean playerHasPerk(Player player, String perkId) {
        return player.getData(RegistryAttachment.SPELL_PERKS).hasPerk(perkId);
    }
}
