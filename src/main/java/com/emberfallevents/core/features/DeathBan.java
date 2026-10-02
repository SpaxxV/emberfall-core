package com.emberfallevents.core.features;

import com.emberfallevents.core.EmberfallCore;
import com.emberfallevents.core.features.config.ConfigFile;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.core.util.Loader;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class DeathBan {
    public static void initialize() {

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof ServerPlayer player) {
                MinecraftServer server = player.getServer();
                if (!server.isSingleplayer()) {
                    String deathMsg = damageSource.getLocalizedDeathMessage(entity).getString();
                    EmberfallCore.LOGGER.info("Death ban " + player.getName().getString() + " for " + deathMsg);
                    Date now = new Date();
                    player.getServer().getPlayerList().getBans().add(
                            new UserBanListEntry(
                                    player.getGameProfile(), now, "efCore-DeathBan",
                                    new Date(now.getTime() + (long) ConfigFile.getConfig("emberfall-core").get("death_ban_duration").value),
                                    deathMsg));
                    if ((boolean) ConfigFile.getConfig("emberfall-core").get("enable_playerrevive_compat").value) {
                        if (Loader.isClassAvailable("team.creative.playerrevive.server.PlayerReviveServer")) {
                            try {
                                Loader.loadClass("team.creative.playerrevive.server.PlayerReviveServer").getMethod("revive", Player.class).invoke(null, player);
                            } catch (ClassNotFoundException | InvocationTargetException |
                                     IllegalAccessException ignored) {} catch (NoSuchMethodException e) {
                                EmberfallCore.LOGGER.warn("Failed to find revive method in PlayerReviveServer: " + e);
                            }
                        } else {
                            EmberfallCore.LOGGER.warn("Class PlayerReviveServer is missing with compat enabled");
                        }
                    }
                    player.connection.disconnect(Component.literal(deathMsg));
                }
            }
        });

    }
}
