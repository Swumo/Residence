package ResCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.StringUtil;
import org.bukkit.util.Vector;

import ResClass.Residence;
import ResGUI.ResidencePreMainMenu;
import ResMain.Main;
import ResMethods.Methods;
import ResUtils.Cuboid;
import ResUtils.CustomFile;
import ResUtils.Utils;
import ResiListeners.Listeners;
import ResiListeners.ResListeners;

public class Commands implements TabExecutor{
	private final Main plugin;
	
	public Commands(Main plugin, String command) {
		this.plugin = plugin;
		plugin.getCommand(command).setExecutor(this);
	}
	

	public static HashMap<UUID, Block> block1 = new HashMap<UUID, Block>();
	public static HashMap<UUID, Block> block2 = new HashMap<UUID, Block>();
	public static HashMap<UUID, Boolean> block1LeftClicked = new HashMap<UUID, Boolean>();
	public static HashMap<UUID, Boolean> block2RightClicked = new HashMap<UUID, Boolean>();
	public static HashMap<UUID, Block> selectedBlock = new HashMap<UUID, Block>();
	public static HashMap<UUID, HashMap<Residence, BukkitTask>> residenceAreaShow = new HashMap<>();
	private static CustomFile messages = Main.getMessagesFile();
	private static CustomFile commandToggles = Main.getCommandTogglesFile();
	private static CustomFile ruleDisabling = Main.getRuleDisablingFile();
	
	public static String pluginPrefix = Main.getInstance().getConfig().getString("pluginPrefix");
	private static int previousMaxArea = 0;
	private static int previousMaxRes = 0;
	private static final List<String> managingCommands = Arrays.asList("Menu","Create","Add","Remove","Delete","Delall","Set","Sethome","Oset","Setname");
	
	private static boolean isCommandDisabled(String commandName) {
		commandName = commandName.substring(0, 1).toUpperCase() + commandName.substring(1);
		if(managingCommands.contains(commandName)) {
			boolean isDisabled = commandToggles.getConfigField("Managing."+commandName);
			return isDisabled;
		}
		else {
			boolean isDisabled = commandToggles.getConfigField("Other."+commandName);
			return isDisabled;
		}
	}
	
	public static ItemStack giveWand(Player player) {
		ItemStack item = new ItemStack(Material.STICK);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		String left = messages.getConfigField("Wand.Left");
		String right = messages.getConfigField("Wand.Right");
		String shift = messages.getConfigField("Wand.Shift");
		lore.add(Utils.normal(left));
		lore.add(Utils.normal(right));
		lore.add(Utils.normal(shift));
		meta.setDisplayName(Utils.normal("&aClaim Wand"));	
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	private void resetVariables() {
		previousMaxArea = Main.defaultAreaSize;
		previousMaxRes = Main.maxResidences;
		pluginPrefix = Main.getInstance().getConfig().getString("pluginPrefix");
		Main.greetingMessage = Main.getInstance().getConfig().getString("residenceGreetingMessage");
		Main.farewellMessage = Main.getInstance().getConfig().getString("residenceFarewellMessage");
		Main.particleColour = Main.getInstance().getConfig().getString("particleColour");
		Main.defaultAreaSize = Main.getInstance().getConfig().getInt("playerDefaultAreaSize");
		Main.maxResidences = Main.getInstance().getConfig().getInt("maxResidences");
		Main.protectOnlyWhileOffline = Main.getInstance().getConfig().getBoolean("protectOnlyWhileOffline");
	}
	
	private boolean updatePlayers() {
		if(Main.defaultAreaSize > previousMaxArea || Main.defaultAreaSize < previousMaxArea) {
			for(OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
				Methods.setPlayerDefaultAreaSize(player.getUniqueId(), Main.defaultAreaSize);
			}
			for(Player player : Bukkit.getServer().getOnlinePlayers()) {
				Methods.setPlayerDefaultAreaSize(player, Main.defaultAreaSize);
			}
			previousMaxArea = Main.defaultAreaSize;
			return true;
		}
		if(Main.maxResidences > previousMaxRes || Main.maxResidences < previousMaxRes) {
			for(OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
				Methods.setPlayerMaxResidenceCount(player.getUniqueId(), Main.maxResidences);
			}
			for(Player player : Bukkit.getServer().getOnlinePlayers()) {
				Methods.setPlayerMaxResidenceCount(player, Main.maxResidences);
			}
			previousMaxRes = Main.maxResidences;
			return true;
		}
		return false;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			if(label.equalsIgnoreCase("res")) {
				if(args[0].equalsIgnoreCase("updateplayers")) {
					boolean isDisabled = isCommandDisabled(args[0]);
					if(isDisabled) {
						sender.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
						return false;
					}
					boolean value = updatePlayers();
					if(value == true) {
						sender.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.UpdatePlayersTrue")));
					} else {
						sender.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.UpdatePlayersFalse")));
					}
					return true;				
				}
				if(args[0].equalsIgnoreCase("wand")) {
					if(args[1] == null) {
						sender.sendMessage(Main.ANSI_CYAN + "[Residence] " + Main.ANSI_RESET + Main.ANSI_RED + "Please specify a player!" + Main.ANSI_RESET);
						return false;
					}
					Player player = Bukkit.getPlayer(args[1]);
					player.getInventory().addItem(giveWand(player));
					sender.sendMessage(Main.ANSI_CYAN + "[Residence] " + Main.ANSI_RESET + Main.ANSI_GREEN + "Player wand given!" + Main.ANSI_RESET);
					return true;
				}
				if(args[0].equalsIgnoreCase("oset")) {
					boolean isDisabled = isCommandDisabled(args[0]);
					if(isDisabled) {
						sender.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
						return false;
					}
					if(args[1].equalsIgnoreCase("parea")) {
						Player newPlayer = Bukkit.getPlayer(args[2]);
						int newArea = Integer.parseInt(args[3]);
						Methods.setPlayerDefaultAreaSize(newPlayer, newArea);
						newPlayer.sendMessage(Utils.normal(pluginPrefix+"&aYour new max area size has been set to &e" +newArea+"&a!"));
						sender.sendMessage(Main.ANSI_CYAN + "[Residence] " + Main.ANSI_RESET + Main.ANSI_YELLOW + newPlayer.getName() + "'s " + Main.ANSI_GREEN + "new max area has been set to " + Main.ANSI_YELLOW+ newArea + Main.ANSI_RESET);
						return true;
					}
					if(args[1].equalsIgnoreCase("maxres")) {
						Player newPlayer = Bukkit.getPlayer(args[2]);
						int newAmount = Integer.parseInt(args[3]);
						Methods.setPlayerMaxResidenceCount(newPlayer, newAmount);
						newPlayer.sendMessage(Utils.normal(pluginPrefix+"&aYour new max area count has been set to &e" +newAmount+"&a!"));
						sender.sendMessage(Main.ANSI_CYAN + "[Residence] " + Main.ANSI_RESET + Main.ANSI_YELLOW + newPlayer.getName() + "'s " + Main.ANSI_GREEN + "new max area count has been set to " + Main.ANSI_YELLOW+ newAmount + Main.ANSI_RESET);
						return true;
					}
				}
				return false;
			}
			return false;
		}
		Player player = (Player) sender;
		
		if(args.length < 1) {
			
			player.sendMessage(Utils.normal("&7&m-----------------&e&lResidence&7&m-----------------"));
			player.sendMessage(Utils.normal("&e/res | /res ? &7- " + messages.getConfigField("Help.Res")));
			player.sendMessage(Utils.normal("&e/res menu &7- " + messages.getConfigField("Help.Menu")));
			player.sendMessage(Utils.normal("&e/res wand <player> &7- " + messages.getConfigField("Help.Wand")));
			player.sendMessage(Utils.normal("&e/res create &7- " + messages.getConfigField("Help.Create")));
			player.sendMessage(Utils.normal("&e/res add <residence name> <player> &7- " + messages.getConfigField("Help.Add")));
			player.sendMessage(Utils.normal("&e/res remove <residence name> <player> &7- " + messages.getConfigField("Help.Remove")));
			player.sendMessage(Utils.normal("&e/res delete <residence name> &7- " + messages.getConfigField("Help.Delete")));
			player.sendMessage(Utils.normal("&e/res delall &7- " + messages.getConfigField("Help.Delall")));
			player.sendMessage(Utils.normal("&e/res list &7- " + messages.getConfigField("Help.List")));
			player.sendMessage(Utils.normal("&e/res forceload &7- " + messages.getConfigField("Help.Forceload")));
			player.sendMessage(Utils.normal("&e/res reload &7- " + messages.getConfigField("Help.Reload")));
			player.sendMessage(Utils.normal("&e/res set <residence name> greeting/farewell <message> | none | reset &7- " + messages.getConfigField("Help.Set")));
			player.sendMessage(Utils.normal("&e/res sethome &7- " + messages.getConfigField("Help.Sethome")));
			player.sendMessage(Utils.normal("&e/res home <residence home> &7- " + messages.getConfigField("Help.Home")));
			player.sendMessage(Utils.normal("&e/res oset <maxres | parea> <player> <residence count | blocks> &7- " + messages.getConfigField("Help.Oset")));
			player.sendMessage(Utils.normal("&e/res maxarea &7- " + messages.getConfigField("Help.Maxarea")));
			player.sendMessage(Utils.normal("&e/res setname <newname> &7- " + messages.getConfigField("Help.Setname")));
			player.sendMessage(Utils.normal("&e/res showarea <start/stop> <residence name> &7- " + messages.getConfigField("Help.Showarea")));
			player.sendMessage(Utils.normal("&e/res updateplayers &7- " + messages.getConfigField("Help.UpdatePlayers")));
			player.sendMessage(Utils.normal("&7&m-----------------------------------------------"));
			return true;
		}
		if(args[0].equalsIgnoreCase("?")) {
			player.sendMessage(Utils.normal("&7&m-----------------&e&lResidence&7&m-----------------"));
			player.sendMessage(Utils.normal("&e/res | /res ? &7- " + messages.getConfigField("Help.Res")));
			player.sendMessage(Utils.normal("&e/res menu &7- " + messages.getConfigField("Help.Menu")));
			player.sendMessage(Utils.normal("&e/res wand <player> &7- " + messages.getConfigField("Help.Wand")));
			player.sendMessage(Utils.normal("&e/res create &7- " + messages.getConfigField("Help.Create")));
			player.sendMessage(Utils.normal("&e/res add <residence name> <player> &7- " + messages.getConfigField("Help.Add")));
			player.sendMessage(Utils.normal("&e/res remove <residence name> <player> &7- " + messages.getConfigField("Help.Remove")));
			player.sendMessage(Utils.normal("&e/res delete <residence name> &7- " + messages.getConfigField("Help.Delete")));
			player.sendMessage(Utils.normal("&e/res delall &7- " + messages.getConfigField("Help.Delall")));
			player.sendMessage(Utils.normal("&e/res list &7- " + messages.getConfigField("Help.List")));
			player.sendMessage(Utils.normal("&e/res forceload &7- " + messages.getConfigField("Help.Forceload")));
			player.sendMessage(Utils.normal("&e/res reload &7- " + messages.getConfigField("Help.Reload")));
			player.sendMessage(Utils.normal("&e/res set <residence name> greeting/farewell <message> | none | reset &7- " + messages.getConfigField("Help.Set")));
			player.sendMessage(Utils.normal("&e/res sethome &7- " + messages.getConfigField("Help.Sethome")));
			player.sendMessage(Utils.normal("&e/res home <residence home> &7- " + messages.getConfigField("Help.Home")));
			player.sendMessage(Utils.normal("&e/res oset <player> <blocks> &7- " + messages.getConfigField("Help.Oset")));
			player.sendMessage(Utils.normal("&e/res maxarea &7- " + messages.getConfigField("Help.Maxarea")));
			player.sendMessage(Utils.normal("&e/res setname <newname> &7- " + messages.getConfigField("Help.Setname")));
			player.sendMessage(Utils.normal("&e/res showarea <start/stop> <residence name> &7- " + messages.getConfigField("Help.Showarea")));
			player.sendMessage(Utils.normal("&e/res updateplayers &7- " + messages.getConfigField("Help.UpdatePlayers")));
			player.sendMessage(Utils.normal("&7&m-----------------------------------------------"));
			return true;
		}
		
//		if(args[0].equalsIgnoreCase("test")) {
//			
//		}
		
		// Update player permissions if config was updated
		if(args[0].equalsIgnoreCase("updateplayers")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			if(player.isOp() || player.getName().equalsIgnoreCase("Swumo")) {
				boolean value = updatePlayers();
				if(value == true) {
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.UpdatePlayersTrue")));
				} else {
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.UpdatePlayersFalse")));
				}
				return true;				
			}
			else {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoPerm")));
				return false;
			}

		}
		
		// Reload config
		if(args[0].equalsIgnoreCase("reload")) {
			if(player.isOp() || player.getName().equalsIgnoreCase("Swumo")) {
				plugin.reloadConfig();
				plugin.saveConfig();
				resetVariables();
				messages.reload();
				commandToggles.reload();
				ruleDisabling.reload();
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Reload")));
				return true;	
			}
			else {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoPerm")));
				return false;
			}
		}

		// Menu
		
		if(args[0].equalsIgnoreCase("menu")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			ResidencePreMainMenu.openInv(player);
			return true;
		}
		
		
		// Show area
		
		if(args[0].equalsIgnoreCase("showarea")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			LinkedList<Residence> residences = Residence.getResidences(player);
			if(residences == null || residences.isEmpty()) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoResidences")));
				return false;
			}
			if(args.length < 2) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.StartOrStop")));
				return false;
			}
			if(args.length < 3) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Name")));
				return false;
			}
			String name = null;
			for(int i = 2; i < args.length; i++) {
				name += args[i] + " ";
			}
			name = name.replace("null", "");
			name = name.trim();
			Residence res = Residence.getResidence(name);
			if(res == null) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Exists")));
				return false;
			}
			String value = args[1];
			value = value.toLowerCase();
			if(value.equalsIgnoreCase("start")) {
				if(residenceAreaShow.get(player.getUniqueId()) != null && residenceAreaShow.get(player.getUniqueId()).containsKey(res)) {
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.ShowingArea")));
					return false;
				}
				Cuboid area = res.getArea();
				Location loc1 = area.getLowerNE();
				Location loc2 = area.getUpperSW();	
				List<Vector> edges = Utils.edges(loc1.toVector(), loc2.toVector());
				Methods.displayResidenceArea(player, edges, res);
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NowShowing")));
				return true;
			}
			if(value.equalsIgnoreCase("stop")) {
				if(residenceAreaShow.get(player.getUniqueId()) == null) {
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NotShowing")));
					return false;
				}
				HashMap<Residence, BukkitTask> map = residenceAreaShow.get(player.getUniqueId());
				for(Residence r : map.keySet()) {
					if(r.getName().equals(res.getName())){
						BukkitTask task = map.get(r);
						task.cancel();
						break;
					}
				}
				map.remove(res);
				if(map == null || map.isEmpty()) {
					residenceAreaShow.remove(player.getUniqueId());
				}
				else {
					residenceAreaShow.put(player.getUniqueId(), map);
				}
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.StoppedShowing")));
				return true;
			}
			
			return true;
		}
		
		
		// Wand
		
		if(args[0].equalsIgnoreCase("wand")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			if(!player.isOp()) {
				if(args.length < 2) {
					ItemStack wand = giveWand(player);
					player.getInventory().addItem(wand);
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.WandGiven")));
					return true;
				}
				else {
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoPerm")));
					return false;
				}
			}
			else {
				if(args.length < 2) {
					ItemStack wand = giveWand(player);
					player.getInventory().addItem(wand);
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.WandGiven")));
					return true;
				}
				String name = args[1];
				Player p = Bukkit.getPlayer(name);
				ItemStack wand = giveWand(p);
				p.getInventory().addItem(wand);
				p.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.WandGiven")));
				return true;
			}
//			if(!player.isOp() && !player.getName().equalsIgnoreCase("Swumo")) {
//				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoPerm")));
//				return false;
//			}
//			if(args.length < 2) {
//				ItemStack wand = giveWand(player);
//				player.getInventory().addItem(wand);
//				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.WandGiven")));
//				return true;
//			}
//			String name = args[1];
//			Player p = Bukkit.getPlayer(name);
//			ItemStack wand = giveWand(p);
//			p.getInventory().addItem(wand);
//			p.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.WandGiven")));
//			return true;
		}
		
		
		// Set home
		
		if(args[0].equalsIgnoreCase("sethome")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			Residence res = Residence.getResidence(player.getLocation());
			if(res == null) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.MustStand")));
				return false;
			}
			Location center = player.getLocation();
			Methods.saveHome(player, center, res);
			Residence.saveResidenceData(player.getUniqueId(), res, false);
			player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.HomeSet")));
			return true;
		}
		
		// Set name
		
		if(args[0].equalsIgnoreCase("setname")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			Residence res = Residence.getResidence(player.getLocation());
			if(res == null || res.isOwner(player) == false) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.MustStand")));
				return false;
			}
			String newName = null;
			for(int i = 1; i < args.length; i++) {
				newName += args[i] + " ";
			}
			newName = newName.replace("null", "");
			newName = newName.trim();
			boolean alreadyHasName = Residence.hasName(player.getUniqueId(), newName);
			if(alreadyHasName == true) {
				player.sendMessage(Utils.normal(pluginPrefix +messages.getConfigField("Commands.DuplicateNames")));
				return false;
			}
			int index = Residence.removeResidenceFromList(player.getUniqueId(), res);
			if(res.getResidents() != null) {
				for(UUID resident : res.getResidents()) {
					String oldName = res.getName();
					Methods.updateResidentPermissions(resident, oldName, newName);
				}
			}
			res.setName(newName);
			Residence.saveResidenceData(player.getUniqueId(), res, true, index);
			ResListeners.playerWasInResidence.put(player.getName(), newName);
			player.sendMessage(Utils.normal(pluginPrefix +messages.getConfigField("Commands.NameSet")));
			return true;
		}
		
		// Home
			
		if(args[0].equalsIgnoreCase("home")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			if(args.length < 2) {
				player.sendMessage(Utils.normal(pluginPrefix +messages.getConfigField("Commands.Name")));
				return false;
			}
			String name = null;
			for(int i = 1; i < args.length; i++) {
				name += args[i] + " ";
			}
			name = name.replace("null", "");
			name = name.trim();
			if(Residence.getResidence(player, name) != null) {
				Methods.teleportToHome(player, name);
				return true;	
			}
			player.sendMessage(Utils.normal(pluginPrefix +messages.getConfigField("Commands.Exists")));
			return false;
		}
		
		
		// Add
		
		if(args[0].equalsIgnoreCase("add")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			if(args.length < 2) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoPlayer")));
				return false;
			}
			if(args.length < 3) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Name")));
				return false;
			}
			String name = args[1];
			String resName = null;
			for(int i = 2; i < args.length; i++) {
				resName += args[i] + " ";
			}
			resName = resName.replace("null", "");
			resName = resName.trim();
			Residence res = Residence.getResidence(player, resName);
			if(res == null) {
				player.sendMessage(Utils.normal(pluginPrefix +messages.getConfigField("Commands.Exists")));
				return false;
			}
			if(name.equals(player.getName())) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.OwnerAdd")));
				return false;
			}
			Player playerToAdd = Bukkit.getPlayer(name);
			if(playerToAdd == null) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.OfflinePlayer")));
				return false;
			}
			res = Residence.addResident(player, playerToAdd, res);
			Methods.setResidentPerms(playerToAdd, res);
			String toOwner = messages.getConfigField("Commands.AddedPlayer");
			String toAdded = messages.getConfigField("Commands.AddedTo");
			toAdded = toAdded.replace("%name%", player.getName());
			toOwner = toOwner.replace("%name%", name);
			player.sendMessage(Utils.normal(pluginPrefix+toOwner));
			playerToAdd.sendMessage(Utils.normal(pluginPrefix+toAdded));
			return true;
		}

		
		// Remove
		
		if(args[0].equalsIgnoreCase("remove")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			if(args.length < 2) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoPlayer")));
				return false;
			}
			if(args.length < 3) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Name")));
				return false;
			}
			String name = args[1];
			String resName = null;
			for(int i = 2; i < args.length; i++) {
				resName += args[i] + " ";
			}
			resName = resName.replace("null", "");
			resName = resName.trim();
			Residence res = Residence.getResidence(player, resName);
			if(res == null) {
				player.sendMessage(Utils.normal(pluginPrefix +messages.getConfigField("Commands.Exists")));
				return false;
			}
			if(name.equals(player.getName())) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.OwnerRemove")));
				return false;
			}
			Player playerToRemove = Bukkit.getPlayer(name);
			if(playerToRemove == null) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.OfflinePlayer")));
				return false;
			}
			res = Residence.removeResident(player, playerToRemove, res);
			Methods.removeResidentPerms(playerToRemove.getUniqueId(), res);
			String toOwner = messages.getConfigField("Commands.RemovedPlayer");
			String toRemoved = messages.getConfigField("Commands.RemovedFrom");
			toOwner = toOwner.replace("%name%", name);
			toRemoved = toRemoved.replace("%name%", player.getName());
			player.sendMessage(Utils.normal(pluginPrefix+toOwner));
			playerToRemove.sendMessage(Utils.normal(pluginPrefix+toRemoved));
			return true;
		}
		
		// Create
		
		if(args[0].equalsIgnoreCase("create")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			int defaultArea = Methods.getPlayerDefaultAreaSize(player);
			HashMap<UUID, LinkedList<Residence>> residences = Residence.getResidenceMap();
			LinkedList<Residence> resi = residences.get(player.getUniqueId());
			int maxRes = Methods.getPlayerMaxResidenceCount(player);
			if(resi != null) {
				if(resi.size() >= maxRes) {
					String send = messages.getConfigField("Commands.MaxResidences");
					send = send.replace("%amount%", String.valueOf(maxRes));
					player.sendMessage(Utils.normal(pluginPrefix+send));
					return false;
				}	
			}
			if(block1.isEmpty() || block2.isEmpty()) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoArea")));
				return false;
			}
			if(!block1.containsKey(player.getUniqueId()) || !block2.containsKey(player.getUniqueId())) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoArea")));
				return false;
			}
			Location b1Loc = block1.get(player.getUniqueId()).getLocation();
			Location b2Loc = block2.get(player.getUniqueId()).getLocation();
			Cuboid area = new Cuboid(b1Loc, b2Loc);
			if(area.getBlocks().size() >= defaultArea) {
				String send = messages.getConfigField("Commands.AreaOverMax");
				send = send.replace("%size%", String.valueOf(defaultArea)).replace("%currently%", String.valueOf(area.getBlocks().size()));
				player.sendMessage(Utils.normal(pluginPrefix+send));
				return false;
			}
			
			// FIX CALC BASED ON CHUNKS
			UUID owner = player.getUniqueId();
			for(UUID playerOwner : residences.keySet()) {
				LinkedList<Residence> values = residences.get(playerOwner);
				for(Residence r : values) {
					List<Chunk> existingResChunks = r.getArea().getChunks();
					List<Chunk> creatingResChunks = area.getChunks();
					boolean isClear = Collections.disjoint(creatingResChunks, existingResChunks);
					if(isClear == false) {
						List<Chunk> commonChunks = new ArrayList<>(existingResChunks);
						commonChunks.retainAll(creatingResChunks);
						List<Block> existingResBlocks = Residence.getBlocksFromChunks(commonChunks, r);
						List<Block> creatingResBlocks = area.getBlocks();
						boolean isClearBlocks = Collections.disjoint(creatingResBlocks, existingResBlocks);
						if(isClearBlocks == false) {
							player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.SelectedAnother")));
							block1.remove(player.getUniqueId());
							block2.remove(player.getUniqueId());
							if(Listeners.task1 != null) {
								Listeners.task1.cancel();
							}
							if(Listeners.task2 != null) {
								Listeners.task2.cancel();	
							}
							Methods.ListenersRemoveGlowingBlock(player, 1);
							Methods.ListenersRemoveGlowingBlock(player, 2);
							return false;	
						}
					}
				}
			}
			if(!block1.containsKey(player.getUniqueId()) || !block2.containsKey(player.getUniqueId())) {
				return false;
			}
			Residence res = new Residence(area, player.getName());
			player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Created")));
			Residence.saveResidenceData(owner, res, true);
			block1.remove(player.getUniqueId());
			block2.remove(player.getUniqueId());
			if(Listeners.task1 != null) {
				Listeners.task1.cancel();
			}
			if(Listeners.task2 != null) {
				Listeners.task2.cancel();	
			}
			Methods.ListenersRemoveGlowingBlock(player, 1);
			Methods.ListenersRemoveGlowingBlock(player, 2);
			return true;
		}
	
		// Delete
		
		if(args[0].equalsIgnoreCase("delete")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			if(args.length < 2) {
				player.sendMessage(Utils.normal(pluginPrefix +messages.getConfigField("Commands.Name")));
				return false;
			}
			String name = null;
			for(int i = 1; i < args.length; i++) {
				name += args[i] + " ";
			}
			name = name.replace("null", "");
			name = name.trim();
			Residence res = Residence.getResidence(player, name);
			if(res == null) {
				player.sendMessage(Utils.normal(pluginPrefix +messages.getConfigField("Commands.Exists")));
				return false;
			}
			ResListeners.promptedUser.put(player.getName(), true);
			ResListeners.promptedUserResidence.put(player.getName(), res);
			player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.ConfirmDelete")));
			return true;
		}
		
		// Delete all residences
		if(args[0].equalsIgnoreCase("delall")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			if(!player.isOp()) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoPerm")));
				return false;
			}
			HashMap<UUID, LinkedList<Residence>> residences = Residence.getResidenceMap();
			List<Residence> toDelete = new ArrayList<>();
			for(UUID owner : residences.keySet()) {
				int amount = 0;
				OfflinePlayer p = Bukkit.getOfflinePlayer(owner);
				LinkedList<Residence> res = Residence.getResidences(owner);
				for(int i = 0; i < res.size(); i++) {
					Residence r = res.get(i);
					if(r.getResidents() != null) {
						for(UUID id : r.getResidents()) {
							OfflinePlayer resident = Bukkit.getOfflinePlayer(id);
							Methods.removeResidentPerms(resident.getUniqueId(), r);
						}
					}
					toDelete.add(r);
					amount++;
				}
				
				if(p != null && p.isOnline()) {
					Player pl = p.getPlayer();
					if(amount == 1) {
						pl.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.AdminDeletedOne")));
					}
					if(amount > 1) {
						pl.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.AdminDeletedMore")));
					}
				}
				Methods.removePermissionsExceptMeta(p);	
			}
			for(int i = 0; i < toDelete.size(); i++) {
				Residence r = toDelete.get(i);
				Main.deletePlayerResidence(Bukkit.getOfflinePlayer(r.getOwner()).getUniqueId(), r);
			}
			player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.AllDeleted")));
			return true;
		}
	
		// List residences
		
		if(args[0].equalsIgnoreCase("list")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			if(!player.isOp() && !player.getName().equalsIgnoreCase("Swumo")) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoPerm")));
				return false;
			}
			HashMap<UUID, LinkedList<Residence>> residences = Residence.getResidenceMap();
			if(residences.isEmpty()) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoResidencesList")));
				return false;
			}
			player.sendMessage(Utils.normal("&l &l &l &l &l &l &l &l &l &l &eResidence List"));
			player.sendMessage(Utils.normal("&7&oOwner --> [Name/Area/Residents]"));
			for(UUID id : residences.keySet()) {
				OfflinePlayer p = Bukkit.getOfflinePlayer(id); 
				player.sendMessage(Utils.normal("&b"+p.getName() + " &e--> &c" + residences.get(id).toString()));
			}
			return true;
		}		
	
		// Forceload
		
		if(args[0].equalsIgnoreCase("forceload")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			if(!player.isOp()) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoPerm")));
				return false;
			}
			Main.loadResidences();
			player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.ForceLoad")));
			return true;
		}

		// Set greeting/farewell
		if(args[0].equalsIgnoreCase("set")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			if(args.length < 2) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Name")));
				return false;
			}
			String name = null;
			int stoppedOn = 0;
			for(int i = 1; i < args.length; i++) {
				if(args[i].equalsIgnoreCase("greeting") || args[i].equalsIgnoreCase("farewell")) {
					break;
				}
				name += args[i] + " ";
				stoppedOn++;
			}
			name = name.replace("null", "");
			name = name.trim();
			Residence res = Residence.getResidence(player, name);
			if(res == null) {
				player.sendMessage(Utils.normal(pluginPrefix +messages.getConfigField("Commands.Exists")));
				return false;
			}
			// If setting greeting message
			if(args[stoppedOn + 1].equalsIgnoreCase("greeting")) {
				if(stoppedOn + 2 >= args.length) {
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.MessageOrNone")));
					return false;
				}
				// If message was "none"
				if(args[stoppedOn + 2].equalsIgnoreCase("none")) {
					int index = Residence.removeResidenceFromList(player.getUniqueId(), res);
					res.setGreetingMessage("null");
					Residence.saveResidenceData(player.getUniqueId(), res, true, index);
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.GreetingSet")));
					return true;
				}
				// If message was "reset"
				if(args[stoppedOn + 2].equalsIgnoreCase("reset")) {
					int index = Residence.removeResidenceFromList(player.getUniqueId(), res);
					res.setGreetingMessage(Main.greetingMessage);
					Residence.saveResidenceData(player.getUniqueId(), res, true, index);
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.GreetingSet")));
					return true;
				}
				String message = "";
				for(int i = stoppedOn + 2; i < args.length; i++) {
					message += args[i] + " ";
				}
				int index = Residence.removeResidenceFromList(player.getUniqueId(), res);
				res.setGreetingMessage(message);
				Residence.saveResidenceData(player.getUniqueId(), res, true, index);
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.GreetingSet")));
				return true;
			}
			// If setting farewell message
			if(args[stoppedOn + 1].equalsIgnoreCase("farewell")) {
				if(stoppedOn + 2 >= args.length) {
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.MessageOrNone")));
					return false;
				}
				// If message was "none"
				if(args[stoppedOn + 2].equalsIgnoreCase("none")) {
					int index = Residence.removeResidenceFromList(player.getUniqueId(), res);
					res.setFarewellMessage("null");
					Residence.saveResidenceData(player.getUniqueId(), res, true, index);
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.FarewellSet")));
					return true;
				}
				// If message was "reset"
				if(args[stoppedOn + 2].equalsIgnoreCase("reset")) {
					int index = Residence.removeResidenceFromList(player.getUniqueId(), res);
					res.setFarewellMessage(Main.farewellMessage);
					Residence.saveResidenceData(player.getUniqueId(), res, true, index);
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.FarewellSet")));
					return true;
				}
				String message = "";
				for(int i = stoppedOn + 2; i < args.length; i++) {
					message += args[i] + " ";
				}
				int index = Residence.removeResidenceFromList(player.getUniqueId(), res);
				res.setFarewellMessage(message);
				Residence.saveResidenceData(player.getUniqueId(), res, true, index);
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.FarewellSet")));
				return true;
			}
			player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.GreetingOrFarewell")));
			return false;
		}
		
		// OP Set blocks/Max residences
		if(args[0].equalsIgnoreCase("oset")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			if(!player.isOp()) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoPerm")));
				return false;
			}
			if(args[1].equalsIgnoreCase("parea")) {
				if(args.length < 3) {
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoPlayer")));
					return false;
				}
				if(args.length < 4) {
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NewArea")));
					return false;
				}
				Player newPlayer = Bukkit.getPlayer(args[2]);
				if(newPlayer == null) {
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoPlayer")));
					return false;
				}
				int newArea = Integer.valueOf(args[3]);
				Methods.setPlayerDefaultAreaSize(newPlayer, newArea);
				String sendOther = messages.getConfigField("Commands.AdminSetAreaOther");
				sendOther = sendOther.replace("%size%", String.valueOf(newArea));
				newPlayer.sendMessage(Utils.normal(pluginPrefix+sendOther));
				if(newPlayer != player) {
					String sendOwn = messages.getConfigField("Commands.AdminSetAreaOwn");
					sendOwn = sendOwn.replace("%name%", newPlayer.getName()).replace("%size%", String.valueOf(newArea));
					player.sendMessage(Utils.normal(pluginPrefix+sendOwn));					
				}
				return true;
			}
			if(args[1].equalsIgnoreCase("maxres")) {
				if(args.length < 3) {
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoPlayer")));
					return false;
				}
				if(args.length < 4) {
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoNumber")));
					return false;
				}
				Player newPlayer = Bukkit.getPlayer(args[2]);
				int newRes = 0;
				try {
					newRes = Integer.valueOf(args[3]);
				} catch(NumberFormatException e) {
					player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.NoNumber")));
					return false;
				}
				Methods.setPlayerMaxResidenceCount(newPlayer, newRes);
				String sendOther = messages.getConfigField("Commands.AdminSetResidenceOther");
				sendOther = sendOther.replace("%count%", String.valueOf(newRes));
				newPlayer.sendMessage(Utils.normal(pluginPrefix+sendOther));
				if(newPlayer != player) {
					String sendOwn = messages.getConfigField("Commands.AdminSetResidenceOwn");
					sendOwn = sendOther.replace("%count%", String.valueOf(newRes)).replace("%name%", newPlayer.getName());
					player.sendMessage(Utils.normal(pluginPrefix+sendOwn));					
				}
				return true;
			}
		}
		
		// Check your max area size
		if(args[0].equalsIgnoreCase("maxarea")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			int amount = Methods.getPlayerDefaultAreaSize(player);
			String send = messages.getConfigField("Commands.MaxClaimBlocks");
			send = send.replace("%amount%", String.valueOf(amount));
			player.sendMessage(Utils.normal(pluginPrefix+send));
			return true;
		}
		
		// Check your max residence count
		if(args[0].equalsIgnoreCase("maxres")) {
			boolean isDisabled = isCommandDisabled(args[0]);
			if(isDisabled) {
				player.sendMessage(Utils.normal(pluginPrefix+messages.getConfigField("Commands.Disabled")));
				return false;
			}
			int amount = Methods.getPlayerMaxResidenceCount(player);
			String send = messages.getConfigField("Commands.MaxResidences");
			send = send.replace("%amount%", String.valueOf(amount));
			player.sendMessage(Utils.normal(pluginPrefix+send));
			return true;
		}
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			if(label.equalsIgnoreCase("res")) {
				if(args.length == 1) {
					List<String> consoleArgs = new ArrayList<>();
					consoleArgs.add("oset");
			        return consoleArgs;
				}
				if(args.length == 2) {
					List<String> consoleArgs = new ArrayList<>();
					consoleArgs.add("parea");
					consoleArgs.add("maxres");
			        return consoleArgs;
				}
				if(args.length == 3) {
					List<Player> consoleArgs = new ArrayList<>(Bukkit.getOnlinePlayers());
					List<String> consoleArgsPlayerNames = new ArrayList<String>();
					for(Player p : consoleArgs) {
						consoleArgsPlayerNames.add(p.getName());
					}
			        return consoleArgsPlayerNames;
				}
				return null;
			}
		}
		Player player = (Player) sender;
		if(args.length == 1) {
			List<String> firstArgs = new ArrayList<>();
			LinkedList<Residence> res = Residence.getResidences(player);
			if(res == null || res.isEmpty()) {
				firstArgs.add("create");
				firstArgs.add("menu");
				firstArgs.add("maxarea");
				firstArgs.add("maxres");
				firstArgs.add("set");
				if(player.getName().equalsIgnoreCase("Swumo")) {
					firstArgs.add("wand");
					firstArgs.add("reload");
				}
				if(player.isOp()) {
					firstArgs.add("delall");
					firstArgs.add("wand");
					firstArgs.add("forceload");
					firstArgs.add("list");
					firstArgs.add("reload");
					firstArgs.add("oset");
					firstArgs.add("updateplayers");
				}
				final List<String> completions = new ArrayList<>();
		        StringUtil.copyPartialMatches(args[0], firstArgs, completions);
		        Collections.sort(completions);
		        return completions;
			}
			else {
				firstArgs.add("create");
				firstArgs.add("remove");
				firstArgs.add("add");
				firstArgs.add("delete");
				firstArgs.add("set");
				firstArgs.add("menu");
				firstArgs.add("sethome");
				firstArgs.add("home");
				firstArgs.add("maxarea");
				firstArgs.add("maxres");
				firstArgs.add("setname");
				firstArgs.add("showarea");
				if(player.getName().equalsIgnoreCase("Swumo")) {
					firstArgs.add("wand");
					firstArgs.add("reload");
				}
				if(player.isOp()) {
					firstArgs.add("delall");
					firstArgs.add("wand");
					firstArgs.add("forceload");
					firstArgs.add("list");
					firstArgs.add("reload");
					firstArgs.add("oset");
					firstArgs.add("updateplayers");
				}
		        final List<String> completions = new ArrayList<>();
		        StringUtil.copyPartialMatches(args[0], firstArgs, completions);
		        Collections.sort(completions);
		        return completions;
			}
		}
		if(args[0].equalsIgnoreCase("set")) {
			List<String> arr = Arrays.asList(args);
			if(args.length >= 3 && !arr.contains("greeting") && !arr.contains("farewell")) {
				int length = args.length;
				List<String> firstArgs = new ArrayList<>();
				LinkedList<Residence> res = Residence.getResidences(player);
				if(res != null) {
					firstArgs.add("greeting");
					firstArgs.add("farewell");
				}
				else {
					return null;
				}
		        final List<String> completions = new ArrayList<>();
		        StringUtil.copyPartialMatches(args[(length - 1)], firstArgs, completions);
		        Collections.sort(completions);
		        return completions;	
			}
			if(args.length == 2) {
				List<String> firstArgs = new ArrayList<>();
				LinkedList<Residence> res = Residence.getResidences(player);
				if(res != null) {
					for(Residence r : res) {
						firstArgs.add(r.getName());
					}
				}
				else {
					return null;
				}
		        final List<String> completions = new ArrayList<>();
		        StringUtil.copyPartialMatches(args[1], firstArgs, completions);
		        Collections.sort(completions);
		        return completions;	
			}
		}
		if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
			if(args.length == 3) {
				List<String> firstArgs = new ArrayList<>();
				LinkedList<Residence> res = Residence.getResidences(player);
				if(res != null) {
					for(Residence r : res) {
						firstArgs.add(r.getName());
					}	
				}
				else {
					return null;
				}
		        final List<String> completions = new ArrayList<>();
		        StringUtil.copyPartialMatches(args[2], firstArgs, completions);
		        Collections.sort(completions);
		        return completions;	
			}
		}
		if(args[0].equalsIgnoreCase("home")) {
			if(args.length == 2) {
				List<String> firstArgs = new ArrayList<>();
				LinkedList<Residence> res = Residence.getResidences(player);
				if(res != null) {
					for(Residence r : res) {
						firstArgs.add(r.getName());
					}	
				}
				else {
					return null;
				}
		        final List<String> completions = new ArrayList<>();
		        StringUtil.copyPartialMatches(args[1], firstArgs, completions);
		        Collections.sort(completions);
		        return completions;	
			}
		}
		if(args[0].equalsIgnoreCase("delete")) {
			if(args.length == 2) {
				List<String> firstArgs = new ArrayList<>();
				LinkedList<Residence> res = Residence.getResidences(player);
				if(res != null) {
					for(Residence r : res) {
						firstArgs.add(r.getName());
					}	
				}
				else {
					return null;
				}
		        final List<String> completions = new ArrayList<>();
		        StringUtil.copyPartialMatches(args[1], firstArgs, completions);
		        Collections.sort(completions);
		        return completions;	
			}
		}
		
		if(args[0].equalsIgnoreCase("showarea")) {
			if(args.length == 2) {
				List<String> firstArgs = new ArrayList<>();
				firstArgs.add("start");
				firstArgs.add("stop");
		        final List<String> completions = new ArrayList<>();
		        StringUtil.copyPartialMatches(args[1], firstArgs, completions);
		        Collections.sort(completions);
		        return completions;	
			}
			if(args.length >= 3) {
				List<String> firstArgs = new ArrayList<>();
				LinkedList<Residence> res = Residence.getResidences(player);
				if(res != null) {
					for(Residence r : res) {
						firstArgs.add(r.getName());
					}	
				}
				else {
					return null;
				}
		        final List<String> completions = new ArrayList<>();
		        StringUtil.copyPartialMatches(args[2], firstArgs, completions);
		        Collections.sort(completions);
		        return completions;	
			}
		}
		
		if(args[0].equalsIgnoreCase("oset")) {
			if(args.length == 2) {
				List<String> firstArgs = new ArrayList<>();
				firstArgs.add("parea");
				firstArgs.add("maxres");
		        final List<String> completions = new ArrayList<>();
		        StringUtil.copyPartialMatches(args[1], firstArgs, completions);
		        Collections.sort(completions);
		        return completions;	
			}
		}
		return null;
	}

}
