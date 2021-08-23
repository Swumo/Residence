package ResMain;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ResClass.Residence;
import ResCommands.Commands;
import ResGUI.ResidenceEditMenu;
import ResGUI.ResidenceGeneralRulesMenu;
import ResGUI.ResidenceMainMenu;
import ResGUI.ResidenceParticleMenu;
import ResGUI.ResidencePlayerAddMenu;
import ResGUI.ResidencePlayerEditMenu;
import ResGUI.ResidencePlayerPermMenu;
import ResGUI.ResidencePlayerRemoveMenu;
import ResGUI.ResidencePreMainMenu;
import ResGUI.ResidenceRuleMenu;
import ResGUI.ResidenceSettingsMenu;
import ResGUI.ResidenceTeleportMenu;
import ResMethods.Methods;
import ResUtils.BlockSaving;
import ResUtils.ConfigWrapper;
import ResUtils.ResidenceConfig;
import ResiListeners.Listeners;
import ResiListeners.ResListeners;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class Main extends JavaPlugin{
	
//	private static EntityHider entityHider;
	private static ConfigWrapper masterResidenceFile;
	private ResidenceConfig config;

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	public static String greetingMessage, farewellMessage, particleColour;
	public static int defaultAreaSize, maxResidences;
	public static boolean protectOnlyWhileOffline;
	
	public static Plugin getInstance() {
		return Bukkit.getServer().getPluginManager().getPlugin("Residence");
	}
	
	public static ConfigWrapper getResidenceFile() {
		return masterResidenceFile;
	}
	
	public static LuckPerms getLP() {
		LuckPerms api = LuckPermsProvider.get();
		return api;
	}
//	
//	public static EntityHider getHider() {
//		return entityHider;
//	}
	
	@Override
	public void onEnable() {
		config = new ResidenceConfig(this);
		masterResidenceFile = new ConfigWrapper(this, "", "playerResidences.yml");
		PluginManager pm = Bukkit.getServer().getPluginManager();
		if(pm.getPlugin("LuckPerms") == null) {
			System.out.println(ANSI_CYAN + "[Residence]" + ANSI_RESET + ANSI_RED + " LuckPerms is not installed on this server! It is required to run this plugin!" + ANSI_RESET);
			pm.disablePlugin(this);
			return;
		}
		System.out.println(ANSI_CYAN + "[Residence]" + ANSI_RESET + ANSI_YELLOW + " Setting up internal variables..." + ANSI_RESET);
		config.loadConfig();
		masterResidenceFile.createNewFile(null, "Residence playerResidences.yml\nPlease do not edit this file!");
		new Commands(this, "res");
		new Commands(this, "residence");
		new ResListeners(this);
		new Listeners(this);
		System.out.println(ANSI_CYAN + "[Residence]" + ANSI_RESET + ANSI_YELLOW + " Setting up menus..." + ANSI_RESET);
		ResidenceRuleMenu.initialize();
		ResidenceParticleMenu.initialize();
		ResidenceMainMenu.initialize();
		ResidenceEditMenu.initialize();
		ResidencePlayerAddMenu.initialize();
		ResidencePlayerRemoveMenu.initialize();
		ResidenceTeleportMenu.initialize();
		ResidencePlayerPermMenu.initialize();
		ResidencePlayerEditMenu.initialize();
		ResidenceSettingsMenu.initialize();
		ResidencePreMainMenu.initialize();
		ResidenceGeneralRulesMenu.initialize();
		System.out.println(ANSI_CYAN + "[Residence]" + ANSI_RESET + ANSI_YELLOW + " Running scheduler..." + ANSI_RESET);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				loadResidences();
				Methods.listenForAreaSelection();
				Methods.listenForIllegalItems();
				List<Player> players = (List<Player>) Bukkit.getServer().getOnlinePlayers();
				for(Player p : players) {
					ResListeners.playerInResidence.put(p.getName(), false);
					ResListeners.playerWasIn.put(p.getName(), "null");
					ResListeners.playerWasInResidence.put(p.getName(), "null");
					ResListeners.promptedUser.put(p.getName(), false);
					ResListeners.promptedUserResidence.put(p.getName(), null);
					ResListeners.userInAreaSelection.put(p.getName(), false);
//					ResListeners.userHidingPoints.put(p.getName(), true);
					ResListeners.userLaunched.put(p.getName(), false);
					Commands.block1LeftClicked.put(p, false);
					Commands.block2RightClicked.put(p, false);
					Methods.setDefaultPerms(p);
					Methods.ListenersRemoveGlowingBlock(p, 1);
					Methods.ListenersRemoveGlowingBlock(p, 2);
				}
			}
		});
		System.out.println(ANSI_CYAN + "[Residence]" + ANSI_RESET + ANSI_YELLOW + " Updating variables..." + ANSI_RESET);
		greetingMessage = Main.getInstance().getConfig().getString("residenceGreetingMessage");
		farewellMessage = Main.getInstance().getConfig().getString("residenceFarewellMessage");
		particleColour = Main.getInstance().getConfig().getString("particleColour");
		defaultAreaSize = Main.getInstance().getConfig().getInt("playerDefaultAreaSize");
		maxResidences = Main.getInstance().getConfig().getInt("maxResidences");
		protectOnlyWhileOffline = Main.getInstance().getConfig().getBoolean("protectOnlyWhileOffline");
		System.out.println(ANSI_CYAN + "[Residence]" + ANSI_RESET + ANSI_GREEN + " Successfully enabled!" + ANSI_RESET);
	}
	
	public void onDisable() {
		Bukkit.getServer().getScheduler().cancelTasks(this);
		saveConfig();
		masterResidenceFile.saveConfig();
		System.out.println(ANSI_CYAN + "[Residence]" + ANSI_RESET + ANSI_RED + " Successfully disabled!" + ANSI_RESET);
	}
	
//	private static void setConfig(CustomConfig conf) {
////		conf.set("pluginPrefix")
//		/*
//		 * 		config.addDefault("pluginPrefix", "&e&lResidence &6>> ");
//		config.addDefault("residenceGreetingMessage", "&eYou have entered <owner> residence.");
//		config.addDefault("residenceFarewellMessage", "&eYou have left <owner> residence.");
//		config.addDefault("particleColour", "White");
//		config.addDefault("playerDefaultAreaSize", 4000);
//		config.addDefault("maxResidences", 5);
//		 */
//	}
	
	public static void deletePlayerResidence(UUID player, String resName) {
		FileConfiguration c = masterResidenceFile.getConfig();
		if(c.getConfigurationSection("PlayerResidences") != null) {
			for(String uuid : c.getConfigurationSection("PlayerResidences").getKeys(false)) {
				UUID pUUID = UUID.fromString(uuid);
				if(player.equals(pUUID)) {
					c.set("PlayerResidences."+player.toString()+"."+resName, null);
					masterResidenceFile.saveConfig();
					Residence res = Residence.getResidence(player, resName);
					Residence.delete(player, res);
					if(Residence.getResidenceMap().get(player) == null || Residence.getResidenceMap().get(player).isEmpty()) {
						c.set("PlayerResidences."+player.toString(), null);
						masterResidenceFile.saveConfig();
					}
					BlockSaving.removeEntry(player, resName);
					break;
				}
			}
		}
		return;
	}
	
	public static void deletePlayerResidence(UUID player, Residence res) {
		FileConfiguration c = masterResidenceFile.getConfig();
		if(c.getConfigurationSection("PlayerResidences") != null) {
			for(String uuid : c.getConfigurationSection("PlayerResidences").getKeys(false)) {
				UUID pUUID = UUID.fromString(uuid);
				if(player.equals(pUUID)) {
					c.set("PlayerResidences."+player.toString()+"."+res.getName(), null);
					masterResidenceFile.saveConfig();
					Residence.delete(player, res);
					if(Residence.getResidenceMap().get(player) == null || Residence.getResidenceMap().get(player).isEmpty()) {
						c.set("PlayerResidences."+player.toString(), null);
						masterResidenceFile.saveConfig();
					}
					BlockSaving.removeEntry(player, res.getName());
					break;
				}
			}
		}
		return;
	}
	
	public static void loadResidences() {
		FileConfiguration c = masterResidenceFile.getConfig();
		int amountLoaded = 0;
		if(c.getConfigurationSection("PlayerResidences") == null) {
			System.out.println(ANSI_CYAN + "[Residence]" + ANSI_RESET + " No residences found, continuing!" + ANSI_RESET);
			return;
		}
		if(c.getConfigurationSection("PlayerResidences").getKeys(true).isEmpty()) {
			System.out.println(ANSI_CYAN + "[Residence]" + ANSI_RESET + " No residences found, continuing!" + ANSI_RESET);
			return;
		}
		for(String uuid : c.getConfigurationSection("PlayerResidences").getKeys(false)) {
			if(uuid == null) continue;
			Map<String, Object> map = c.getConfigurationSection("PlayerResidences."+uuid).getValues(true);
			LinkedList<Object> values = new LinkedList<>();
			for(String key : map.keySet()) {
				if(!key.contains(".")) {
					if(values.isEmpty() == false) {
						Residence res = Methods.getPlayerResidenceFromValues(values);
						Residence.save(UUID.fromString(uuid), res);
						amountLoaded++;
					}
					values.clear();
					continue;
				}
				values.add(map.get(key));
			}
			if(values.isEmpty() == false) {
				Residence res = Methods.getPlayerResidenceFromValues(values);
				Residence.save(UUID.fromString(uuid), res);
				amountLoaded++;
				values.clear();
			}
		}
		System.out.println(ANSI_CYAN + "[Residence] " + ANSI_RESET + ANSI_YELLOW + amountLoaded + " Residences loaded!" + ANSI_RESET);
	}
}
