package com.solarflareciv.core.features;

import com.solarflareciv.core.EmberfallCore;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class MaintenanceMode {
    public static int toggleMaintenance(CommandContext<CommandSourceStack> context) {
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
    public static void onJoin(ServerPlayer player) {
        boolean maintenanceMode = false;
        if (GlobalWorldData.efData.contains("maintenance")) {
            maintenanceMode = GlobalWorldData.efData.getBoolean("maintenance");
        }
        if (maintenanceMode && !player.hasPermissions(1) && !player.getServer().isSingleplayer()) player.connection.disconnect(Component.literal("Maintenance mode is currently enabled."));
    }
}
