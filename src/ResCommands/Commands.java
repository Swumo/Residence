package ResCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
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
import ResUtils.Utils;
import ResiListeners.Listeners;
import ResiListeners.ResListeners;

public class Commands implements TabExecutor{
	private Main plugin;
	
	public Commands(Main plugin, String command) {
		this.plugin = plugin;
		plugin.getCommand(command).setExecutor(this);
	}
	
	public static List<String> ruleList = Arrays.asList("allowBlockPlacing", "allowBlockBreaking", 
			"allowBlockInteraction", "allowEntityInteraction", "allowDamageEntities", 
			"allowVehicleDestroy", "allowTNTPlacing", "allowTeleport", "allowEntering");
	public static HashMap<Player, Block> block1 = new HashMap<Player, Block>();
	public static HashMap<Player, Block> block2 = new HashMap<Player, Block>();
	public static HashMap<Player, Boolean> block1LeftClicked = new HashMap<Player, Boolean>();
	public static HashMap<Player, Boolean> block2RightClicked = new HashMap<Player, Boolean>();
	public static HashMap<Player, Block> selectedBlock = new HashMap<Player, Block>();
	public static HashMap<UUID, HashMap<Residence, BukkitTask>> residenceAreaShow = new HashMap<>();
	public static BukkitTask testTask;
	
	public static String pluginPrefix = Main.getInstance().getConfig().getString("pluginPrefix");
	
	public static ItemStack giveWand(Player player) {
		ItemStack item = new ItemStack(Material.STICK);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(Utils.normal("&7Left Click to select 1st position"));
		lore.add(Utils.normal("&7Right Click to select 2nd position"));
		lore.add(Utils.normal("&7&oShift Right Click to deselect"));
		meta.setDisplayName(Utils.normal("&aClaim Wand"));	
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	private void resetVariables() {
		pluginPrefix = Main.getInstance().getConfig().getString("pluginPrefix");
		Main.greetingMessage = Main.getInstance().getConfig().getString("residenceGreetingMessage");
		Main.farewellMessage = Main.getInstance().getConfig().getString("residenceFarewellMessage");
		Main.particleColour = Main.getInstance().getConfig().getString("particleColour");
		Main.defaultAreaSize = Main.getInstance().getConfig().getInt("playerDefaultAreaSize");
		Main.maxResidences = Main.getInstance().getConfig().getInt("maxResidences");
		Main.protectOnlyWhileOffline = Main.getInstance().getConfig().getBoolean("protectOnlyWhileOffline");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			if(label.equalsIgnoreCase("res")) {
				if(args.length < 1 || args.length < 2 || args.length < 3 || args.length < 4) {
					return false;
				}
				if(args[0].equalsIgnoreCase("set")) {
					if(args[1].equalsIgnoreCase("parea")) {
						Player newPlayer = Bukkit.getPlayer(args[2]);
						int newArea = Integer.valueOf(args[3]);
						Methods.setPlayerDefaultAreaSize(newPlayer, newArea);
						newPlayer.sendMessage(Utils.normal(pluginPrefix+"&aYour new max area size has been set to &e" +newArea+"&a!"));
						sender.sendMessage(Main.ANSI_CYAN + "[Residence] " + Main.ANSI_RESET + Main.ANSI_YELLOW + newPlayer.getName() + "'s " + Main.ANSI_GREEN + "new max area has been set to " + Main.ANSI_YELLOW+ newArea + Main.ANSI_RESET);
						return true;
					}
				}
			}
			return false;
		}
		Player player = (Player) sender;
		
		if(args.length < 1) {
			player.sendMessage(Utils.normal("&7&m-----------------&e&lResidence &a4.0&7&m-----------------"));
			player.sendMessage(Utils.normal("&e/res | /res ? &7- &bDisplay this message"));
			player.sendMessage(Utils.normal("&e/res menu &7- &bOpen Residence Menu"));
			player.sendMessage(Utils.normal("&e/res wand <player> &7- &bGet/Give Residence Claim Wand &c[Admin Only]"));
//			player.sendMessage(Utils.normal("&e/res deselect &7- &bDeselect currently selected positions"));
			player.sendMessage(Utils.normal("&e/res create &7- &bCreate your Residence"));
			player.sendMessage(Utils.normal("&e/res add <residence name> <player> &7- &bAdd <player> to your Residence"));
			player.sendMessage(Utils.normal("&e/res remove <residence name> <player> &7- &bRemove <player> from your Residence"));
			player.sendMessage(Utils.normal("&e/res delete <residence name> &7- &bDelete your Residence"));
			player.sendMessage(Utils.normal("&e/res delall &7- &bDelete &cALL &bResidences &c[Admin Only]"));
			player.sendMessage(Utils.normal("&e/res list &7- &bList all Residences &c[Admin Only]"));
			player.sendMessage(Utils.normal("&e/res forceload &7- &bForce load every Residence &c[Admin Only]"));
			player.sendMessage(Utils.normal("&e/res set <residence name> greeting/farewell <message> | none | reset &7- &bSet greeting/farewell message to <message> | none | default"));
			player.sendMessage(Utils.normal("&e/res sethome &7- &bSet your Residence home"));
			player.sendMessage(Utils.normal("&e/res home &7- &bTeleport to your Residence home"));
			player.sendMessage(Utils.normal("&e/res oset <player> <blocks> &7- &bSet max claim area in blocks &c[Admin Only]"));
			player.sendMessage(Utils.normal("&e/res maxarea &7- &bCheck your max amount of claimable blocks"));
			player.sendMessage(Utils.normal("&e/res setname <newname> &7- &bSet a new name for your Residence"));
			player.sendMessage(Utils.normal("&e/res showarea <start/stop> <residence name> &7- &bShow the claimed area of a Residence to other players with particles"));
			player.sendMessage(Utils.normal("&7&m-----------------------------------------------"));
			return true;
		}
		if(args[0].equalsIgnoreCase("?")) {
			player.sendMessage(Utils.normal("&7&m-----------------&e&lResidence &a4.0&7&m-----------------"));
			player.sendMessage(Utils.normal("&e/res | /res ? &7- &bDisplay this message"));
			player.sendMessage(Utils.normal("&e/res menu &7- &bOpen Residence Menu"));
			player.sendMessage(Utils.normal("&e/res wand <player> &7- &bGet/Give Residence Claim Wand &c[Admin Only]"));
//			player.sendMessage(Utils.normal("&e/res deselect &7- &bDeselect currently selected positions"));
			player.sendMessage(Utils.normal("&e/res create &7- &bCreate your Residence"));
			player.sendMessage(Utils.normal("&e/res add <residence name> <player> &7- &bAdd <player> to your Residence"));
			player.sendMessage(Utils.normal("&e/res remove <residence name> <player> &7- &bRemove <player> from your Residence"));
			player.sendMessage(Utils.normal("&e/res delete <residence name> &7- &bDelete your Residence"));
			player.sendMessage(Utils.normal("&e/res delall &7- &bDelete &cALL &bResidences &c[Admin Only]"));
			player.sendMessage(Utils.normal("&e/res list &7- &bList all Residences &c[Admin Only]"));
			player.sendMessage(Utils.normal("&e/res forceload &7- &bForce load every Residence &c[Admin Only]"));
			player.sendMessage(Utils.normal("&e/res set <residence name> greeting/farewell <message> | none | reset &7- &bSet greeting/farewell message to <message> | none | default"));
			player.sendMessage(Utils.normal("&e/res sethome &7- &bSet your Residence home"));
			player.sendMessage(Utils.normal("&e/res home &7- &bTeleport to your Residence home"));
			player.sendMessage(Utils.normal("&e/res oset <player> <blocks> &7- &bSet max claim area in blocks &c[Admin Only]"));
			player.sendMessage(Utils.normal("&e/res maxarea &7- &bCheck your max amount of claimable blocks"));
			player.sendMessage(Utils.normal("&e/res setname <newname> &7- &bSet a new name for your Residence"));
			player.sendMessage(Utils.normal("&e/res showarea <start/stop> <residence name> &7- &bShow the claimed area of a Residence to other players with particles"));
			player.sendMessage(Utils.normal("&7&m-----------------------------------------------"));
			return true;
		}
		
		// Reload config
		if(args[0].equalsIgnoreCase("reload")) {
			plugin.reloadConfig();
			plugin.saveConfig();
			resetVariables();
			player.sendMessage(Utils.normal(pluginPrefix+"&aConfig reloaded!"));
			return true;
		}
//		
		// Menu
		
		if(args[0].equalsIgnoreCase("menu")) {
			ResidencePreMainMenu.openInv(player);
			return true;
		}
//		
//		
//		// Point showing/hiding
//		
//		if(args[0].equalsIgnoreCase("points")) {
//			if(!block1.containsKey(player) && !block2.containsKey(player)) {
//				player.sendMessage(Utils.normal(pluginPrefix+"&cYou must have at least 1 point selected"));
//				return false;
//			}
//			boolean areHidden = ResListeners.userHidingPoints.get(player.getName());
//			EntityHider entityHider = Main.getHider();
//			List<Player> otherPlayers = new ArrayList<Player>(Bukkit.getOnlinePlayers());
//			otherPlayers.remove(player);
//			if(args.length < 2) {
//				player.sendMessage(Utils.normal(pluginPrefix+"&cYou must specify either &eShow &cor &eHide&c!"));
//				return false;
//			}
//			if(args[1].equalsIgnoreCase("show")) {
//				if(areHidden == false) {
//					player.sendMessage(Utils.normal(pluginPrefix+"&cYour selection points are already visible!"));
//					return false;
//				}
//				World world = player.getWorld();
//				List<Entity> entities = world.getEntities();
//				for(int i = 0; i < entities.size(); i++) {
//					Entity ent = entities.get(i);
//					if(Methods.entities.contains(ent.getType())) {
//						if(ent.getCustomName() == null) continue;
//						if(ent.getCustomName().equals(player.getName()+"Block1") || ent.getCustomName().equals(player.getName()+"Block2")) {
//							for(Player p : otherPlayers) {
//								entityHider.showEntity(p, ent);
//							}
//						}	
//					}
//				}
//				ResListeners.userHidingPoints.put(player.getName(), false);
//				player.sendMessage(Utils.normal(pluginPrefix+"&eYour selection points are now &avisible &eto other players!"));
//				return true;
//			}
//			if(args[1].equalsIgnoreCase("hide")) {
//				if(areHidden == true) {
//					player.sendMessage(Utils.normal(pluginPrefix+"&cYour selection points are already hidden!"));
//					return false;
//				}
//				World world = player.getWorld();
//				List<Entity> entities = world.getEntities();
//				for(int i = 0; i < entities.size(); i++) {
//					Entity ent = entities.get(i);
//					if(Methods.entities.contains(ent.getType())) {
//						if(ent.getCustomName() == null) continue;
//						if(ent.getCustomName().equals(player.getName()+"Block1") || ent.getCustomName().equals(player.getName()+"Block2")) {
//							for(Player p : otherPlayers) {
//								entityHider.hideEntity(p, ent);
//							}
//						}	
//					}
//				}
//				ResListeners.userHidingPoints.put(player.getName(), true);
//				player.sendMessage(Utils.normal(pluginPrefix+"&eYour selection points are now &chidden &efrom other players!"));
//				return true;
//			}
//			return false;
//		}
//		
//		
		// Show area
		
		if(args[0].equalsIgnoreCase("showarea")) {
			LinkedList<Residence> residences = Residence.getResidences(player);
			if(residences == null || residences.isEmpty()) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cYou do not have any Residences!"));
				return false;
			}
			if(args.length < 2) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cPlease specify either &estart &cor &estop&c!"));
				return false;
			}
			if(args.length < 3) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cPlease specify a Residence name!"));
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
				player.sendMessage(Utils.normal(pluginPrefix+"&cResidence does not exist!"));
				return false;
			}
			String value = args[1];
			value = value.toLowerCase();
			if(value.equalsIgnoreCase("start")) {
				if(residenceAreaShow.get(player.getUniqueId()) != null && residenceAreaShow.get(player.getUniqueId()).containsKey(res)) {
					player.sendMessage(Utils.normal(pluginPrefix+"&cYou are already showing this Residence area to other players!"));
					return false;
				}
				Cuboid area = res.getArea();
				Location loc1 = area.getLowerNE();
				Location loc2 = area.getUpperSW();	
				List<Vector> edges = Utils.edges(loc1.toVector(), loc2.toVector());
				Methods.displayResidenceArea(player, edges, res);
				player.sendMessage(Utils.normal(pluginPrefix+"&aYou are now showing your Residence area to other players!"));
				return true;
			}
			if(value.equalsIgnoreCase("stop")) {
				if(residenceAreaShow.get(player.getUniqueId()) == null) {
					player.sendMessage(Utils.normal(pluginPrefix+"&cYou are not showing this Residence's area!"));
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
				player.sendMessage(Utils.normal(pluginPrefix+"&eYou have stopped showing your Residence area to other players!"));
				return true;
			}
			
			return true;
		}
		
		
		
		// Wand
		
		if(args[0].equalsIgnoreCase("wand")) {
			if(!player.isOp()) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cYou cannot run this command!"));
				return false;
			}
			if(args.length < 2) {
				ItemStack wand = giveWand(player);
				player.getInventory().addItem(wand);
				player.sendMessage(Utils.normal(pluginPrefix+"&eResidence Claim Wand was given to you!"));
				return true;
			}
			String name = args[1];
			Player p = Bukkit.getPlayer(name);
			ItemStack wand = giveWand(p);
			p.getInventory().addItem(wand);
			p.sendMessage(Utils.normal(pluginPrefix+"&eResidence Claim Wand was given to you!"));
			return true;
		}
		
		
		// Set home
		
		if(args[0].equalsIgnoreCase("sethome")) {
			Residence res = Residence.getResidence(player.getLocation());
			if(res == null) {
				player.sendMessage(Utils.normal(pluginPrefix +"&cYou must stand in your Residence!"));
				return false;
			}
			Location center = player.getLocation();
			Methods.saveHome(player, center, res);
//			Residence.save(player.getUniqueId(), res);
			Residence.saveResidenceData(player.getUniqueId(), res, false);
			player.sendMessage(Utils.normal(pluginPrefix +"&aResidence home set!"));
			return true;
		}
		
		// Set name
		
		if(args[0].equalsIgnoreCase("setname")) {
			Residence res = Residence.getResidence(player.getLocation());
			if(res == null || res.isOwner(player) == false) {
				player.sendMessage(Utils.normal(pluginPrefix +"&cYou must stand in your Residence!"));
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
				player.sendMessage(Utils.normal(pluginPrefix +"&cYou cannot have duplicate Residence names!"));
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
			player.sendMessage(Utils.normal(pluginPrefix +"&aResidence name set!"));
			return true;
		}
		
		// Home
			
		if(args[0].equalsIgnoreCase("home")) {
			if(args.length < 2) {
				player.sendMessage(Utils.normal(pluginPrefix +"&cPlease specify your Residence name!"));
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
			player.sendMessage(Utils.normal(pluginPrefix +"&cThat Residence does not exist!"));
			return false;
		}
//		
//		
//		// Deselect
//		
//		if(args[0].equalsIgnoreCase("deselect")) {
//			if(!block1.containsKey(player) && !block2.containsKey(player)) {
//				player.sendMessage(Utils.normal(pluginPrefix +"&cYou do not have any positions selected!"));
//				return false;
//			}
//			if(block1.containsKey(player)) {
//				block1.remove(player);	
//			}
//			if(block2.containsKey(player)) {
//				block2.remove(player);	
//			}
//			if(Listeners.task1 != null) {
//				Listeners.task1.cancel();
//			}
//			if(Listeners.task2 != null) {
//				Listeners.task2.cancel();	
//			}
//			Methods.ListenersRemoveGlowingBlock(player, 1);
//			Methods.ListenersRemoveGlowingBlock(player, 2);
//			player.sendMessage(Utils.normal(pluginPrefix +"&aSelection removed!"));
//			return true;
//		}
//		
//		
		// Add
		
		if(args[0].equalsIgnoreCase("add")) {
			if(args.length < 2) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cPlease specify a player!"));
				return false;
			}
			if(args.length < 3) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cPlease specify the name of the Residence!"));
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
				player.sendMessage(Utils.normal(pluginPrefix +"&cResidence does not exist!"));
				return false;
			}
			if(name.equals(player.getName())) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cYou cannot add yourself to your own Residence!"));
				return false;
			}
			Player playerToAdd = Bukkit.getPlayer(name);
			if(playerToAdd == null) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cPlayer is not online!"));
				return false;
			}
			res = Residence.addResident(player, playerToAdd, res);
//			Residence.saveResidenceData(player.getUniqueId(), res);
			Methods.setResidentPerms(playerToAdd, res);
			player.sendMessage(Utils.normal(pluginPrefix+"&e"+name+" &ahas been added to your Residence!"));
			playerToAdd.sendMessage(Utils.normal(pluginPrefix+"&eYou have been added to &6"+player.getName()+"'s &eResidence!"));
			return true;
		}
//		
//		
//		// Remove
//		
		if(args[0].equalsIgnoreCase("remove")) {
			if(args.length < 2) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cPlease specify a player!"));
				return false;
			}
			if(args.length < 3) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cPlease specify the name of the Residence!"));
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
				player.sendMessage(Utils.normal(pluginPrefix +"&cResidence does not exist!"));
				return false;
			}
			if(name.equals(player.getName())) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cYou cannot remove yourself from your own Residence!"));
				return false;
			}
			Player playerToRemove = Bukkit.getPlayer(name);
			if(playerToRemove == null) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cPlayer is not online!"));
				return false;
			}
			res = Residence.removeResident(player, playerToRemove, res);
//			Residence.saveResidenceData(player.getUniqueId(), res);
			Methods.removeResidentPerms(playerToRemove.getName(), res);
			player.sendMessage(Utils.normal(pluginPrefix+"&e"+name+" &ahas been removed to your Residence!"));
			playerToRemove.sendMessage(Utils.normal(pluginPrefix+"&eYou have been removed from &6"+player.getName()+"'s &eResidence!"));
			return true;
		}
		
		// Create
		
		if(args[0].equalsIgnoreCase("create")) {
			int defaultArea = Methods.getPlayerDefaultAreaSize(player);
			HashMap<UUID, LinkedList<Residence>> residences = Residence.getResidenceMap();
			LinkedList<Residence> resi = residences.get(player.getUniqueId());
			int maxRes = Methods.getPlayerMaxResidenceCount(player);
			if(resi != null) {
				if(resi.size() >= maxRes) {
					player.sendMessage(Utils.normal(pluginPrefix+"&cYou cannot have more than &e" +maxRes + " &cResidences!"));
					return false;
				}	
			}
			if(block1.isEmpty() || block2.isEmpty()) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cYou do not have an area selected!"));
				return false;
			}
			if(!block1.containsKey(player) || !block2.containsKey(player)) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cYou do not have an area selected!"));
				return false;
			}
			Location b1Loc = block1.get(player).getLocation();
			Location b2Loc = block2.get(player).getLocation();
			Cuboid area = new Cuboid(b1Loc, b2Loc);
			if(area.getBlocks().size() >= defaultArea) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cYou cannot have an area bigger than "+defaultArea+" blocks! (Currently: "+area.getBlocks().size())+")");
				return false;
			}
			UUID owner = player.getUniqueId();
			for(UUID playerOwner : residences.keySet()) {
				LinkedList<Residence> values = residences.get(playerOwner);
				for(Residence r : values) {
					List<Block> existingResBlocks = r.getArea().getBlocks();
					List<Block> creatingResBlocks = area.getBlocks();
					boolean isClear = Collections.disjoint(creatingResBlocks, existingResBlocks);
					if(isClear == false) {
						player.sendMessage(Utils.normal(pluginPrefix+"&cYour selected area claims another Residence! Please select a new area!"));
						block1.remove(player);
						block2.remove(player);
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
			if(!block1.containsKey(player) || !block2.containsKey(player)) {
				return false;
			}
			Residence res = new Residence(area, player.getName());
			player.sendMessage(Utils.normal(pluginPrefix+"&eResidence created!"));
			Residence.saveResidenceData(owner, res, true);
			if(block1.containsKey(player)) {
				block1.remove(player);	
			}
			if(block2.containsKey(player)) {
				block2.remove(player);	
			}
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
			if(args.length < 2) {
				player.sendMessage(Utils.normal(pluginPrefix +"&cPlease specify the name of the Residence!"));
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
				player.sendMessage(Utils.normal(pluginPrefix +"&cResidence does not exist!"));
				return false;
			}
			ResListeners.promptedUser.put(player.getName(), true);
			ResListeners.promptedUserResidence.put(player.getName(), res);
			player.sendMessage(Utils.normal(pluginPrefix+"&cAre you sure you want to delete your Residence? Type &aYes &cto delete your Residence or &4No &cto cancel Residence deletion"));
			return true;
		}
		
		// Delete all residences
		if(args[0].equalsIgnoreCase("delall")) {
			if(!player.isOp()) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cYou cannot run this command!"));
				return false;
			}
			HashMap<UUID, LinkedList<Residence>> residences = Residence.getResidenceMap();
			List<Residence> toDelete = new ArrayList<>();
			for(UUID owner : residences.keySet()) {
				int amount = 0;
				Player p = Bukkit.getPlayer(owner);
				LinkedList<Residence> res = Residence.getResidences(owner);
				for(int i = 0; i < res.size(); i++) {
					Residence r = res.get(i);
					if(r.getResidents() != null) {
						for(UUID id : r.getResidents()) {
							String resident = Bukkit.getOfflinePlayer(id).getName();
							Methods.removeResidentPerms(resident, r);
						}
					}
					toDelete.add(r);
					amount++;
				}
				
				if(p != null && p.isOnline()) {
					if(amount == 1) {
						p.sendMessage(Utils.normal(pluginPrefix+"&eYour Residence has been deleted by an Admin."));
					}
					if(amount > 1) {
						p.sendMessage(Utils.normal(pluginPrefix+"&eYour Residences have been deleted by an Admin."));
					}
				}
				Methods.removePermissionsExceptMeta(p);	
			}
			for(int i = 0; i < toDelete.size(); i++) {
				Residence r = toDelete.get(i);
				Main.deletePlayerResidence(Bukkit.getOfflinePlayer(r.getOwner()).getUniqueId(), r);
			}
			player.sendMessage(Utils.normal(pluginPrefix+"&aAll Residences successfully deleted!"));
			return true;
		}
	
		// List residences
		
		if(args[0].equalsIgnoreCase("list")) {
			if(!player.isOp()) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cYou cannot run this command!"));
				return false;
			}
			HashMap<UUID, LinkedList<Residence>> residences = Residence.getResidenceMap();
			if(residences.isEmpty()) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cThere are no Residences."));
				return false;
			}
			player.sendMessage(Utils.normal("&l &l &l &l &l &l &l &l &l &l &eResidence List"));
			player.sendMessage(Utils.normal("&7&oOwner --> [Name/Area/Owner/Residents]"));
			for(UUID id : residences.keySet()) {
				OfflinePlayer p = Bukkit.getOfflinePlayer(id); 
				player.sendMessage(Utils.normal("&b"+p.getName() + " &e--> &c" + residences.get(id).toString()));
			}
			return true;
		}		
	
		// Forceload
		
		if(args[0].equalsIgnoreCase("forceload")) {
			if(!player.isOp()) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cYou cannot run this command!"));
				return false;
			}
			Main.loadResidences();
			player.sendMessage(Utils.normal(pluginPrefix+"&eResidences successfully force loaded!"));
			return true;
		}

		// Set greeting/farewell
		if(args[0].equalsIgnoreCase("set")) {
			if(args.length < 2) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cPlease specify the name of the Residence!"));
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
			// If setting greeting message
			if(args[stoppedOn + 1].equalsIgnoreCase("greeting")) {
				if(stoppedOn + 2 >= args.length) {
					player.sendMessage(Utils.normal(pluginPrefix+"&cPlease specify a message! Or type &enone &cto remove it."));
					return false;
				}
				// If message was "none"
				if(args[stoppedOn + 2].equalsIgnoreCase("none")) {
					Residence res = Residence.getResidence(player, name);
					int index = Residence.removeResidenceFromList(player.getUniqueId(), res);
					res.setGreetingMessage("null");
					Residence.saveResidenceData(player.getUniqueId(), res, true, index);
					player.sendMessage(Utils.normal(pluginPrefix+"&eYour new Residence greeting message has been set!"));
					return true;
				}
				// If message was "reset"
				if(args[stoppedOn + 2].equalsIgnoreCase("reset")) {
					Residence res = Residence.getResidence(player, name);
					int index = Residence.removeResidenceFromList(player.getUniqueId(), res);
					res.setGreetingMessage(Main.greetingMessage);
					Residence.saveResidenceData(player.getUniqueId(), res, true, index);
					player.sendMessage(Utils.normal(pluginPrefix+"&eYour Residence greeting message has been reset!"));
					return true;
				}
				String message = new String();
				for(int i = stoppedOn + 2; i < args.length; i++) {
					message += args[i] + " ";
				}
				Residence res = Residence.getResidence(player, name);
				int index = Residence.removeResidenceFromList(player.getUniqueId(), res);
				res.setGreetingMessage(message);
				Residence.saveResidenceData(player.getUniqueId(), res, true, index);
				player.sendMessage(Utils.normal(pluginPrefix+"&eYour new Residence greeting message has been set!"));
				return true;
			}
			// If setting farewell message
			if(args[stoppedOn + 1].equalsIgnoreCase("farewell")) {
				if(stoppedOn + 2 >= args.length) {
					player.sendMessage(Utils.normal(pluginPrefix+"&cPlease specify a message! Or type &enone &cto remove it."));
					return false;
				}
				// If message was "none"
				if(args[stoppedOn + 2].equalsIgnoreCase("none")) {
					Residence res = Residence.getResidence(player, name);
					int index = Residence.removeResidenceFromList(player.getUniqueId(), res);
					res.setFarewellMessage("null");
					Residence.saveResidenceData(player.getUniqueId(), res, true, index);
					player.sendMessage(Utils.normal(pluginPrefix+"&eYour new Residence farewell message has been set!"));
					return true;
				}
				// If message was "reset"
				if(args[stoppedOn + 2].equalsIgnoreCase("reset")) {
					Residence res = Residence.getResidence(player, name);
					int index = Residence.removeResidenceFromList(player.getUniqueId(), res);
					res.setFarewellMessage(Main.farewellMessage);
					Residence.saveResidenceData(player.getUniqueId(), res, true, index);
					player.sendMessage(Utils.normal(pluginPrefix+"&eYour Residence farewell message has been reset!"));
					return true;
				}
				String message = new String();
				for(int i = stoppedOn + 2; i < args.length; i++) {
					message += args[i] + " ";
				}
				Residence res = Residence.getResidence(player, name);
				int index = Residence.removeResidenceFromList(player.getUniqueId(), res);
				res.setFarewellMessage(message);
				Residence.saveResidenceData(player.getUniqueId(), res, true, index);
				player.sendMessage(Utils.normal(pluginPrefix+"&eYour new Residence farewell message has been set!"));
				return true;
			}
			player.sendMessage(Utils.normal(pluginPrefix+"&cPlease specify either &egreeting &cor &efarewell&c!"));
			return false;
		}
		
		// OP Set blocks/Max residences
		if(args[0].equalsIgnoreCase("oset")) {
			if(!player.isOp()) {
				player.sendMessage(Utils.normal(pluginPrefix+"&cYou cannot run this command!"));
				return false;
			}
			if(args[1].equalsIgnoreCase("parea")) {
				if(args.length < 3) {
					player.sendMessage(Utils.normal(pluginPrefix+"&cPlease specify a player!"));
					return false;
				}
				if(args.length < 4) {
					player.sendMessage(Utils.normal(pluginPrefix+"&cPlease specify a new area in blocks!"));
					return false;
				}
				Player newPlayer = Bukkit.getPlayer(args[2]);
				int newArea = Integer.valueOf(args[3]);
				Methods.setPlayerDefaultAreaSize(newPlayer, newArea);
				newPlayer.sendMessage(Utils.normal(pluginPrefix+"&aAn Admin has set your new max area size to &e" +newArea+"&a!"));
				if(newPlayer != player) {
					player.sendMessage(Utils.normal(pluginPrefix+"&aYou have set &e"+newPlayer.getName()+"'s &anew max area size to &e"+newArea+"&a!"));					
				}
				return true;
			}
			if(args[1].equalsIgnoreCase("maxres")) {
				if(args.length < 3) {
					player.sendMessage(Utils.normal(pluginPrefix+"&cPlease specify a player!"));
					return false;
				}
				if(args.length < 4) {
					player.sendMessage(Utils.normal(pluginPrefix+"&cPlease specify a number!"));
					return false;
				}
				Player newPlayer = Bukkit.getPlayer(args[2]);
				int newRes = Integer.valueOf(args[3]);
				Methods.setPlayerMaxResidenceCount(newPlayer, newRes);
				newPlayer.sendMessage(Utils.normal(pluginPrefix+"&aAn Admin has set your new max Residence count to &e" +newRes+"&a!"));
				if(newPlayer != player) {
					player.sendMessage(Utils.normal(pluginPrefix+"&aYou have set &e"+newPlayer.getName()+"'s &anew max Residence count to &e"+newRes+"&a!"));					
				}
				return true;
			}
		}
		
		// Check your max area size
		if(args[0].equalsIgnoreCase("maxarea")) {
			int amount = Methods.getPlayerDefaultAreaSize(player);
			player.sendMessage(Utils.normal(pluginPrefix+"&aYour maximum amount of claimable blocks is &e"+amount+"&a!"));
			return true;
		}
		
		// Check your max residence count
		if(args[0].equalsIgnoreCase("maxres")) {
			int amount = Methods.getPlayerMaxResidenceCount(player);
			player.sendMessage(Utils.normal(pluginPrefix+"&aYour maximum amount of Residences is &e"+amount+"&a!"));
			return true;
		}
		
		// Save residence blocks
		if(args[0].equalsIgnoreCase("save")) {
//			Residence res = Residence.getResidence(player.getLocation());
//			if(res == null || res.isOwner(player) == false) {
//				player.sendMessage(Utils.normal(pluginPrefix +"&cYou must stand in your Residence!"));
//				return false;
//			}
//			BlockSaving.saveBlocks(player, res);
//			player.sendMessage(Utils.normal(pluginPrefix+"&aSaved!"));
//			return true;
		}
		
		// Load residence blocks from save
		if(args[0].equalsIgnoreCase("load")) {
//			Residence res = Residence.getResidence(player.getLocation());
//			if(res == null || res.isOwner(player) == false) {
//				player.sendMessage(Utils.normal(pluginPrefix +"&cYou must stand in your Residence!"));
//				return false;
//			}
//			if(BlockSaving.checkIfLoadExists(player, res) == false) {
//				player.sendMessage(Utils.normal(pluginPrefix +"&cThis Residence does not have a save!"));
//				return false;
//			}
//			List<Block> blocks = res.getArea().getBlocks();
//			List<List<Block>> split = Methods.splitList(blocks, 100);
//			for(List<Block> bList : split) {
//				BlockCheck a = new BlockCheck(player, res, bList, new HashMap<Location, Material>());
//				a.runTaskAsynchronously(Main.getInstance());
//			}
//			player.sendMessage(Utils.normal(pluginPrefix+"&aResidence loaded!"));
//			return true;
//			for(Block b : blocks) {
//				int exists = BlockSaving.checkBlockExists(player, res, b);
//				if(exists != -1) {
//					Material mat = BlockSaving.getBlock(player, res, exists);
//					player.getWorld().getBlockAt(b.getLocation()).setType(mat);
//					continue;
//				}
//			}
//			player.sendMessage(Utils.normal(pluginPrefix+"&aLoaded!"));
//			return true;
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
				firstArgs.add("deselect");
				firstArgs.add("menu");
				firstArgs.add("maxarea");
				firstArgs.add("maxres");
				firstArgs.add("set");
				if(player.isOp()) {
					firstArgs.add("delall");
					firstArgs.add("wand");
					firstArgs.add("forceload");
					firstArgs.add("list");
					firstArgs.add("reload");
					firstArgs.add("oset");
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
				firstArgs.add("deselect");
				firstArgs.add("delete");
				firstArgs.add("set");
				firstArgs.add("menu");
				firstArgs.add("sethome");
				firstArgs.add("home");
				firstArgs.add("maxarea");
				firstArgs.add("maxres");
				firstArgs.add("setname");
				firstArgs.add("showarea");
				if(player.isOp()) {
					firstArgs.add("delall");
					firstArgs.add("wand");
					firstArgs.add("forceload");
					firstArgs.add("list");
					firstArgs.add("reload");
					firstArgs.add("oset");
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
