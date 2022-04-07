package ResUtils;

import org.bukkit.configuration.file.FileConfiguration;

import ResMain.Main;

public class ResidenceConfig {
	Main plugin;
	
	public ResidenceConfig(Main p) {
		plugin = p;
	}
	
	@SuppressWarnings("deprecation")
	public void loadConfig() {
		
		FileConfiguration config = plugin.getConfig();
		
		config.options().header("Author: Swumo");
		
		config.addDefault("pluginPrefix", "&3Residence &6| ");
		config.addDefault("residenceGreetingMessage", "&eYou have entered %owner% residence.");
		config.addDefault("residenceFarewellMessage", "&eYou have left %owner% residence.");
		config.addDefault("particleColour", "White");
		config.addDefault("playerDefaultAreaSize", 4000);
		config.addDefault("maxResidences", 5);
		config.addDefault("protectOnlyWhileOffline", true);
		config.options().copyDefaults(true);
		plugin.saveConfig();
		plugin.reloadConfig();
	}
}
