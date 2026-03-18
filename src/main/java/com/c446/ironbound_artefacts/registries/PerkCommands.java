package com.c446.ironbound_artefacts.registries;

import com.c446.ironbound_artefacts.IBA;
import com.c446.ironbound_artefacts.perks.spellperks.PerkManager;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Collection;

@EventBusSubscriber(modid = IBA.MODID)
public class PerkCommands {


    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("perks").requires(source -> source.hasPermission(2)).then(Commands.literal("grant").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("perkId", ResourceLocationArgument.id()) // Use RL for ":" support
                .suggests((context, builder) -> SharedSuggestionProvider.suggestResource(PerkManager.getAllPerkIds().stream().map(ResourceLocation::parse), builder)).executes(context -> modifyPerk(context, true))))).then(Commands.literal("revoke").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("perkId", ResourceLocationArgument.id()) // Match the RL switch here
                .suggests((context, builder) -> SharedSuggestionProvider.suggestResource(PerkManager.getAllPerkIds().stream().map(ResourceLocation::parse), builder)).executes(context -> modifyPerk(context, false))))));
    }

    private static int modifyPerk(CommandContext<CommandSourceStack> context, boolean grant) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");

        // Retrieve as ResourceLocation to handle the namespace:path format correctly
        ResourceLocation perkRl = ResourceLocationArgument.getId(context, "perkId");
        String perkId = perkRl.toString();

        int count = 0;
        for (ServerPlayer player : targets) {
            var attachment = player.getData(RegistryAttachment.SPELL_PERKS);
            if (grant) {
                attachment.addPerk(perkId);
            } else {
                attachment.removePerk(perkId);
            }

            // Sync back to the player and mark as dirty for saving
            player.setData(RegistryAttachment.SPELL_PERKS, attachment);
            count++;
        }

        final int finalCount = count;
        String action = grant ? "Granted " : "Revoked ";
        context.getSource().sendSuccess(() -> Component.literal(action + perkId + " for " + finalCount + " players"), true);

        return count;
    }
}