package com.solarflareciv.core.features;

import com.solarflareciv.core.EmberfallCore;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class StaffNotify {
    public static int executeStaffNotify(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        EmberfallCore.LOGGER.info("Staff Notify from " + source.getTextName());
        List<ServerPlayer> players = source.getServer().getPlayerList().getPlayers();
        Vec3 sourcePos = source.getPosition();
        for (ServerPlayer player : players) {
            if (player.hasPermissions(1)) {
                player.sendSystemMessage(
                        Component.literal("[STAFF NOTIFY]").withStyle(style -> style.withColor(ChatFormatting.YELLOW))
                                .append(Component.literal(" Source: " + source.getTextName() + " "))
                                .append(Component.literal("[Take me there!]").withStyle(style -> style.withColor(ChatFormatting.GREEN)
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp @s " + sourcePos.x + " " + sourcePos.y + " " + sourcePos.z)))),
                        false);
            }
        }
        context.getSource().sendSuccess(() -> Component.literal("Staff successfully notified!"), false);
        return 1;
    }
}
