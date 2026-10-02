package com.emberfallevents.core.features;

import com.emberfallevents.core.EmberfallCore;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class MaintenanceMode {
    private static int enableMaintenance(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        MinecraftServer server = source.getServer();
        EmberfallCore.LOGGER.info("Toggling Maintenance Mode");

        boolean maintenanceMode = false;
        if (GlobalWorldData.efData.contains("maintenance")) {
            maintenanceMode = GlobalWorldData.efData.getBoolean("maintenance");
        }
        maintenanceMode = !maintenanceMode;

        if (maintenanceMode) {
            if (!server.isSingleplayer()) {
                List<ServerPlayer> players = server.getPlayerList().getPlayers();
                for (ServerPlayer player : players) {
                    if (!player.hasPermissions(1)) {
                        player.connection.disconnect(Component.literal("Maintenance mode has been enabled."));
                    }
                }
            }
            context.getSource().sendSuccess(() -> Component.literal("Enabled Maintenance Mode"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("Disabled Maintenance Mode"), false);
        }

        GlobalWorldData.efData.putBoolean("maintenance", maintenanceMode);
        return 1;
    }

    public static void initialize() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("maintenance").requires(context -> context.hasPermission(4)).executes(MaintenanceMode::enableMaintenance));
        });

        ServerPlayerEvents.JOIN.register((player) -> {
            boolean maintenanceMode = false;
            if (GlobalWorldData.efData.contains("maintenance")) {
                maintenanceMode = GlobalWorldData.efData.getBoolean("maintenance");
            }
            if (maintenanceMode && !player.hasPermissions(1) && !player.getServer().isSingleplayer()) player.connection.disconnect(Component.literal("Maintenance mode is currently enabled."));
        });

    }
}
