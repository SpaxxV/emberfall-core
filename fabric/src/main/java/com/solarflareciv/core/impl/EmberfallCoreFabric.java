package com.solarflareciv.core.impl;

import com.solarflareciv.core.EmberfallCore;
import com.solarflareciv.core.EmberfallCoreInitializer;
import com.solarflareciv.core.features.DeathBan;
import com.solarflareciv.core.features.GlobalWorldData;
import com.solarflareciv.core.features.MaintenanceMode;
import com.solarflareciv.core.features.StaffNotify;

import com.solarflareciv.core.features.config.ConfigFile;
import com.solarflareciv.core.features.config.ConfigMap;
import com.solarflareciv.core.features.config.elements.ConfigBoolean;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.Commands;

public class EmberfallCoreFabric implements EmberfallCoreInitializer, ModInitializer {

	@Override
	public void createConfigs(ConfigMap defaults) {
		EmberfallCore.efcConfig = ConfigFile.createConfig("emberfall-core",
				defaults.set("enable_playerrevive_compat", new ConfigBoolean(false)));
	}

	@Override
	public void initializeDeathBan() {
		DeathBan.isFabric = true;
		ServerLivingEntityEvents.AFTER_DEATH.register(DeathBan::onDeath);
	}

	@Override
	public void initializeStaffNotify() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(Commands.literal("staffnotify").executes(StaffNotify::executeStaffNotify));
		});
	}

	@Override
	public void initializeMaintenanceMode() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(Commands.literal("maintenance").requires(context -> context.hasPermission(4)).executes(MaintenanceMode::toggleMaintenance));
		});
		ServerPlayerEvents.JOIN.register(MaintenanceMode::onJoin);
	}

	@Override
	public void onInitialize() {
		EmberfallCore.LOGGER.info("Using Emberfall Core Fabric v1.3.0-1.21.1");
		EmberfallCore.configLocation = FabricLoader.getInstance().getConfigDir();

		ServerLifecycleEvents.SERVER_STARTED.register(GlobalWorldData::onLoad);
		ServerLifecycleEvents.BEFORE_SAVE.register((server, a, b) -> GlobalWorldData.onSave(server));

		EmberfallCore.onInitialize(this);
	}
}
