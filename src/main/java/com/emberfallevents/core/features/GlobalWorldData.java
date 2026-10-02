package com.emberfallevents.core.features;

import com.emberfallevents.core.EmberfallCore;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;
import java.io.IOException;

public class GlobalWorldData {
    public static CompoundTag efData = new CompoundTag();
    public static void initialize() {

        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            File root = server.getWorldPath(LevelResource.ROOT).toFile();
            try {
                File efDataF = new File(root, "efData.dat");
                if (efDataF.exists()) {
                    efData = NbtIo.read(efDataF.toPath());
                    EmberfallCore.LOGGER.info("Successfully read efData from world.");
                } else {
                    EmberfallCore.LOGGER.info("No efData loaded from world.");
                }
            } catch (IOException e) {
                EmberfallCore.LOGGER.warn("Failed to load efData from world: " + e);
            }
        });

        ServerLifecycleEvents.BEFORE_SAVE.register((server, a, b) -> {
            File root = server.getWorldPath(LevelResource.ROOT).toFile();
            try {
                File efDataF = new File(root, "efData.dat");
                NbtIo.write(efData, efDataF.toPath());
            } catch (IOException e) {
                EmberfallCore.LOGGER.warn("Failed to save efData to world: " + e);
            }
        });

    }
}
