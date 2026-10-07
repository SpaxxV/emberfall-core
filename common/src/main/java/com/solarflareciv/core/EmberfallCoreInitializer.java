package com.solarflareciv.core;

import com.solarflareciv.core.features.config.ConfigMap;

public interface EmberfallCoreInitializer {
	void createConfigs(ConfigMap defaults);
	void initializeDeathBan();
	void initializeStaffNotify();
	void initializeMaintenanceMode();
}
