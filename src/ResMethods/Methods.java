package ResMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import ResClass.Residence;
import ResCommands.Commands;
import ResGUI.ResidenceGeneralRulesMenu;
import ResGUI.ResidenceParticleMenu;
import ResGUI.ResidenceRuleMenu;
import ResMain.Main;
import ResUtils.Cuboid;
import ResUtils.Cuboid.CuboidDirection;
import ResUtils.Utils;
import ResiListeners.Listeners;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.util.Tristate;

public class Methods {
	
	/*
	 * 
	 * Variables
	 * 
	 */
	public static List<EntityType> entities = Arrays.asList(EntityType.values());
	
	/*
	 * 
	 * TELEPORTING PLAYER TO HOME
	 * 
	 */
	
	
	@SuppressWarnings("deprecation")
	public static void teleportToHome(String owner, String resName, Player toTp) {
		UUID id = Bukkit.getOfflinePlayer(owner).getUniqueId();
		Residence res = Residence.getResidence(id, resName);
		if(res == null) {
			toTp.sendMessage(Utils.normal(Commands.pluginPrefix +"&c" + owner + " does not have a Residence!"));
			return;
		}
		Location center = res.getCenter();
		center.setPitch(res.getPitch());
		center.setYaw(res.getYaw());
		toTp.sendMessage(Utils.normal(Commands.pluginPrefix+"&eYou will be teleported in &a5 &eseconds..."));
		new BukkitRunnable() {
			@Override
			public void run() {
				toTp.teleport(center);
				toTp.sendMessage(Utils.normal(Commands.pluginPrefix+"&eYou have been teleported to &6" + owner + "'s &eResidence!"));
			}
			
		}.runTaskLater(Main.getInstance(), 5 * 20);
		return;
	}
	
	public static void teleportToHome(Player player, String resName) {
		Residence res = Residence.getResidence(player, resName);
		if(res == null) {
			player.sendMessage(Utils.normal(Commands.pluginPrefix +"&cYou do not have a Residence!"));
			return;
		}
		Location center = res.getCenter();
		center.setPitch(res.getPitch());
		center.setYaw(res.getYaw());
		player.sendMessage(Utils.normal(Commands.pluginPrefix+"&eYou will be teleported in &a5 &eseconds..."));
		new BukkitRunnable() {
			@Override
			public void run() {
				player.teleport(center);
				player.sendMessage(Utils.normal(Commands.pluginPrefix+"&eYou have been teleported to your Residence!"));
			}
			
		}.runTaskLater(Main.getInstance(), 5 * 20);
		return;
	}
	
	
	
	
	/*
	 * 
	 * SAVING PLAYER HOME
	 * 
	 */
	

	public static void saveHome(Player player, Location loc, Residence res) {
		res.setCenter(loc);
		res.setYaw(loc.getYaw());
		res.setPitch(loc.getPitch());
//		Residence.save(player.getUniqueId(), res);
	}
	
	
	/*
	 * 
	 * REMOVING ALL PERMISSIONS EXCEPT META
	 * 
	 */
	
	
	public static void removePermissionsExceptMeta(Player player) {
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(player.getName());
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains("residence") || node.getKey().contains("Residence")) {
				if(node.getKey().contains("meta.residence\\.particlecolour.")) {
					continue;
				}
				if(node.getKey().contains("meta.residence\\.defaultarea")) {
					continue;
				}
				if(node.getKey().contains("meta.residence\\.maxresidences")) {
					continue;
				}
				user.data().remove(node);
				api.getUserManager().saveUser(user);
			}
		}
	}
	
	
	/*
	 * 
	 * SETTING RESIDENT PERMISSIONS
	 * 
	 */
	
	public static void setResidentPerms(Player player, Residence res) {
		LuckPerms api = Main.getLP();
		for(String perm : Commands.ruleList) {
			PermissionNode node = PermissionNode.builder(res.getName()+"."+perm).build();
			if(perm.equalsIgnoreCase("allowEntering")) {
				node = node.toBuilder().value(true).build();
				User user = api.getUserManager().getUser(player.getName());
				if(user == null) continue;
				if(user.data().contains(node, NodeEqualityPredicate.IGNORE_VALUE) == Tristate.TRUE) {
					continue;
				}
				user.data().add(node);
				api.getUserManager().saveUser(user);
				continue;
			}
			node = node.toBuilder().value(false).build();
			User user = api.getUserManager().getUser(player.getName());
			if(user == null) continue;
			if(user.data().contains(node, NodeEqualityPredicate.IGNORE_VALUE) == Tristate.TRUE) {
				continue;
			}
			user.data().add(node);
			api.getUserManager().saveUser(user);
		}
		
	}
	
	
	/*
	 * 
	 * REMOVING RESIDENT PERMISSIONS
	 * 
	 */
	
	/**
	 * Remove resident perm
	 * @param player - Player to remove
	 */
	public static void removeResidentPerms(Player player) {
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(player.getName());
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			String key = node.getKey();
			if(key.contains("allow")) {
				String name = key.substring(0, node.getKey().indexOf("."));
				Residence res = Residence.getResidence(name);
				if(res == null) {
					user.data().remove(node);
					api.getUserManager().saveUser(user);
				}	
			}
		}
	}
	
	/**
	 * Remove resident perms
	 * @param player - Name of the player to remove
	 * @param res - Residence
	 */
	public static void removeResidentPerms(String player, Residence res) {
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(player);
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains(res.getName())) {
				user.data().remove(node);
				api.getUserManager().saveUser(user);
			}
		}
	}
	
	
	
	/*
	 * 
	 * GETTING PLAYER DEFAULT AREA SIZE
	 * 
	 */
	
	public static int getPlayerDefaultAreaSize(Player player) {
		int area = 0;
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(player.getName());
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains("meta.residence\\.defaultarea")) {
				MetaNode mNode = (MetaNode) node;
				area = Integer.valueOf(mNode.getMetaValue());
			}
		}
		return area;
	}
	
	
	/*
	 * 
	 * SETTING DEFAULT PLAYER AREA SIZE
	 * 
	 */
	
	
	public static void setPlayerDefaultAreaSize(Player player, int amount) {
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(player.getName());
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains("meta.residence\\.defaultarea")) {
				user.data().remove(node);
				api.getUserManager().saveUser(user);
				break;
			}
		}
		MetaNode defAreaPerm = MetaNode.builder("Residence.defaultarea", String.valueOf(amount)).build();
		user.data().add(defAreaPerm);
		api.getUserManager().saveUser(user);
	}
	
	
	/*
	 * 
	 * GETTING PLAYER MAX RESIDENCE COUNT
	 * 
	 */
	
	public static int getPlayerMaxResidenceCount(Player player) {
		int count = 0;
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(player.getName());
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains("meta.residence\\.maxresidences")) {
				MetaNode mNode = (MetaNode) node;
				count = Integer.valueOf(mNode.getMetaValue());
			}
		}
		return count;
	}
	
	
	/*
	 * 
	 * SETTING MAX RESIDENCE COUNT FOR PLAYER
	 * 
	 */
	
	
	public static void setPlayerMaxResidenceCount(Player player, int amount) {
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(player.getName());
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains("meta.residence\\.maxresidences")) {
				user.data().remove(node);
				api.getUserManager().saveUser(user);
				break;
			}
		}
		MetaNode maxResPerm = MetaNode.builder("Residence.maxResidences", String.valueOf(amount)).build();
		user.data().add(maxResPerm);
		api.getUserManager().saveUser(user);
	}
	
	
	
	/*
	 * 
	 * UPDATING PLAYER PARTICLE COLOUR
	 * 
	 */
	
	public static void ParticleMenuUpdateColour(Player player, String colour) {
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(player.getName());
		List<Node> nodes = (List<Node>) user.getNodes();
		Node n = null;
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains("meta.residence\\.particlecolour.")) {
				n = node;
				break;
			}
		}
		user.data().remove(n);
		api.getUserManager().saveUser(user);
		MetaNode colourPerm = MetaNode.builder("Residence.particlecolour", colour).build();
		user.data().add(colourPerm);
		api.getUserManager().saveUser(user);
		ResidenceParticleMenu.updateInventory(player);
		return;
	}
	
	/*
	 * 
	 * UPDATING RESIDENT PERMISSIONS
	 * 
	 */
	
//	public static void EditMenuUpdateRule(Player owner, Player editing, String rule) {
//		LuckPerms api = Main.getLP();
//		User user = api.getUserManager().getUser(editing.getName());
//		List<Node> nodes = (List<Node>) user.getNodes();
//		Node n = null;
//		boolean value = false;
//		for(int i = 0; i < nodes.size(); i++) {
//			Node node = nodes.get(i);
//			if(node.getKey().contains(rule)) {
//				n = node;
//				value = node.getValue();
//				break;
//			}
//		}
//		user.data().remove(n);
//		if(value == false) {
//			PermissionNode node = PermissionNode.builder(rule).build();
//			node = node.toBuilder().value(true).build();
//			user.data().add(node);
//			api.getUserManager().saveUser(user);
//		}
//		if(value == true) {
//			PermissionNode node = PermissionNode.builder(rule).build();
//			node = node.toBuilder().value(false).build();
//			user.data().add(node);
//			api.getUserManager().saveUser(user);
//		}
//		ResidencePlayerEditMenu.updateInventory(owner, editing);
//		return;
//	}
	
	
	/*
	 * 
	 * GET RESIDENCE RULE VALUE
	 * 
	 */
	
	public static boolean GetRuleValue(Player owner, String rule) {
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(owner.getName());
		List<Node> nodes = (List<Node>) user.getNodes();
		boolean value = false;
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains(rule)) {
				value = node.getValue();
				break;
			}
		}
		return value;
	}
	
	public static boolean GetRuleValue(String owner, String rule) {
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(owner);
		List<Node> nodes = (List<Node>) user.getNodes();
		boolean value = false;
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains(rule)) {
				value = node.getValue();
				break;
			}
		}
		return value;
	}
	
	
	/*
	 * 
	 * UPDATE GUEST RULE
	 * 
	 */
	
	
	@SuppressWarnings("deprecation")
	public static void RuleMenuUpdateRule(Player player, String rule, Residence res) {
		switch(rule) {
		case "allowTeleport":
			boolean tp = res.getAllowTeleport();
			if(tp == false) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowTeleport(true);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			if(tp == true) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowTeleport(false);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			break;
		case "allowBlockPlacing":
			boolean place = res.getAllowBlockPlace();
			if(place == false) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowBlockPlace(true);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			if(place == true) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowBlockPlace(false);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			break;
		case "allowBlockBreaking":
			boolean breaking = res.getAllowBlockBreak();
			if(breaking == false) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowBlockBreak(true);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			if(breaking == true) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowBlockBreak(false);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			break;
		case "allowBlockInteraction":
			boolean interact = res.getAllowBlockInteraction();
			if(interact == false) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowBlockInteraction(true);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			if(interact == true) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowBlockInteraction(false);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			break;
		case "allowEntityInteraction":
			boolean einteract = res.getAllowEntityInteraction();
			if(einteract == false) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowEntityInteraction(true);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			if(einteract == true) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowEntityInteraction(false);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			break;
		case "allowDamageEntities":
			boolean damage = res.getAllowDamageEntities();
			if(damage == false) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowDamageEntities(true);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			if(damage == true) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowDamageEntities(false);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			break;
		case "allowVehicleDestroy":
			boolean destroy = res.getAllowVehicleDestroy();
			if(destroy == false) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowVehicleDestroy(true);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			if(destroy == true) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowVehicleDestroy(false);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			break;
		case "allowTNTPlacing":
			boolean tnt = res.getAllowTNTPlacing();
			if(tnt == false) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowTNTPlacing(true);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			if(tnt == true) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowTNTPlacing(false);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			break;
		case "allowEntering":
			boolean enter = res.getAllowEntering();
			if(enter == false) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowEntering(true);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			if(enter == true) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowEntering(false);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			break;
		}
		ResidenceRuleMenu.updateInventory(player, res);
		return;
	}
	
	
	/*
	 * 
	 * UPDATE GEN RULES
	 * 
	 */
	
	@SuppressWarnings("deprecation")
	public static void GenRuleMenuUpdateRule(Player player, String rule, Residence res) {
		switch(rule) {
		case "allowCreeperGriefing":
			boolean creeper = res.getCreeperGriefing();
			if(creeper == false) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowCreeperGriefing(true);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			if(creeper == true) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowCreeperGriefing(false);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			break;
		case "allowEndermanGriefing":
			boolean enderman = res.getEndermanGriefing();
			if(enderman == false) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowEndermanGriefing(true);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			if(enderman == true) {
				int index = Residence.removeResidenceFromList(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res);
				res.setAllowEndermanGriefing(false);
				Residence.saveResidenceData(Bukkit.getOfflinePlayer(res.getOwner()).getUniqueId(), res, true, index);
			}
			break;
		}
		ResidenceGeneralRulesMenu.updateInventory(player, res);
		return;
	}
	
	
	/*
	 * 
	 * GET LOCATION FROM VALUES
	 * 
	 */
	
	public static Location TeleportMenuGetLocationFromValues(Player player, String values) {
		if(values != null) {
			String[] data = values.split(",");
			double x = Double.parseDouble(data[0]);
			double y = Double.parseDouble(data[1]);
			double z = Double.parseDouble(data[2]);
			Location center = new Location(player.getWorld(), x, y, z);
			return center;
		}
		return null;
	}
	
	public static Location TeleportMenuGetLocationFromValues(OfflinePlayer player, String values) {
		if(values != null) {
			String[] data = values.split(",");
			double x = Double.parseDouble(data[0]);
			double y = Double.parseDouble(data[1]);
			double z = Double.parseDouble(data[2]);
			Location center = new Location(player.getPlayer().getWorld(), x, y, z);
			return center;
		}
		return null;
	}
	
	
	/*
	 * 
	 * RUNNABLE FOR LISTENING FOR AREA SELECTION TO DISPLAY PARTICLES
	 * 
	 */
	
	public static void listenForAreaSelection() {
		new BukkitRunnable() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
				if(!players.isEmpty()) {
					for(Player p : players) {
						String value = null;
						LuckPerms api = Main.getLP();
						User user = api.getUserManager().getUser(p.getName());
						List<Node> nodes = (List<Node>) user.getNodes();
						for(int i = 0; i < nodes.size(); i++) {
				   			Node node = nodes.get(i);
				   			if(node.getKey().contains("meta.residence\\.particlecolour.")) {
				   				MetaNode mNode = (MetaNode) node;
				   				value = mNode.getMetaValue();
				   			}
						}
						Color color = ListenersStringToColour(value);
						if(Commands.block1.containsKey(p) && Commands.block2.containsKey(p)) {
//							boolean isHiding = ResListeners.userHidingPoints.get(p.getName());
							Location b1Loc = Commands.block1.get(p).getLocation();
							Location b2Loc = Commands.block2.get(p).getLocation();
							List<Vector> edges = Utils.edges(b1Loc.toVector(), b2Loc.toVector());
							for(Vector edge : edges) {
								Location toSpawn = edge.toLocation(p.getWorld());
								Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
								p.spawnParticle(Particle.REDSTONE, toSpawn, 1, dustOptions);	
//								if(isHiding == true) {
//									Location toSpawn = edge.toLocation(p.getWorld());
//									Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
//									p.spawnParticle(Particle.REDSTONE, toSpawn, 1, dustOptions);	
//								}
//								if(isHiding == false) {
//									Location toSpawn = edge.toLocation(p.getWorld());
//									Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
//									toSpawn.getWorld().spawnParticle(Particle.REDSTONE, toSpawn, 1, dustOptions);
//								}
							}
						}
					}	
				}
			}
			
		}.runTaskTimer(Main.getInstance(), 20, 60);
	}
	
	
	
	public static void displayResidenceArea(Player player, List<Vector> edges, Residence res) {
		BukkitTask task = new BukkitRunnable() {
			@Override
			public void run() {
				String value = null;
				LuckPerms api = Main.getLP();
				User user = api.getUserManager().getUser(player.getName());
				List<Node> nodes = (List<Node>) user.getNodes();
				for(int i = 0; i < nodes.size(); i++) {
		   			Node node = nodes.get(i);
		   			if(node.getKey().contains("meta.residence\\.particlecolour.")) {
		   				MetaNode mNode = (MetaNode) node;
		   				value = mNode.getMetaValue();
		   			}
				}
				Color color = ListenersStringToColour(value);
				for(Vector edge : edges) {
					Location toSpawn = edge.toLocation(player.getWorld());
					Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
					player.getWorld().spawnParticle(Particle.REDSTONE, toSpawn, 1, dustOptions);	
				}
			}
			
		}.runTaskTimer(Main.getInstance(), 20, 60);
		if(Commands.residenceAreaShow.get(player.getUniqueId()) == null) {
			HashMap<Residence, BukkitTask> put = new HashMap<>();
			put.put(res, task);
			Commands.residenceAreaShow.put(player.getUniqueId(), put);
			return;
		}
		else {
			HashMap<Residence, BukkitTask> put = Commands.residenceAreaShow.get(player.getUniqueId());
			put.put(res, task);
			Commands.residenceAreaShow.put(player.getUniqueId(), put);
			return;
		}
	}
	
	/*
	 * 
	 * RUNNABLE FOR LISTENING FOR ILLEGAL ITEMS (ITEMS GLITCHED FROM MENU'S)
	 * 
	 */
	
	
	
	public static void listenForIllegalItems() {
		new BukkitRunnable() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
				if(!players.isEmpty()) {
					for(Player p : players) {
						Inventory inv = p.getInventory();
						for(int i = 0; i < inv.getContents().length; i++) {
							ItemStack item = inv.getItem(i);
							if(item == null) continue;
							if(item.getType() == Material.BARRIER || item.getType() == Material.PLAYER_HEAD) {
								inv.remove(item);
								continue;
							}
							if(item.getItemMeta().getDisplayName() == null) {
								continue;
							}
							if(item.getItemMeta().getDisplayName().contains(Utils.normal("&e&lEdit Residence")) 
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&c&lDelete Residence")) 
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&6&lResidence Info:")) 
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&e&lEdit Rules"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&b&lChange area size"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&e&lChange particle colour"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&cBack"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &a&lBlock Placing"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &a&lBlock &c&lBreaking"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &a&lBlock &b&lInteraction"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &f&lEntity &a&lPlacing"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &f&lEntity &4&lDamage"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &6&lVehicle &c&lBreaking"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &c&lT&f&lN&c&lT &a&lPlacing"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&aLime"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&bAqua"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&cRed"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&dPink"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&eYellow"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&2Green"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&3Cyan"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&fWhite"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&5Purple"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&6Orange"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&eResidence"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&b&lTeleport to Residence"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&a&lAdd Player"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&c&lRemove Player"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&e&lEdit Guest Rules"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&f&lSettings"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&eEdit General Rules"))
									|| item.getItemMeta().getDisplayName().contains(Utils.normal("&e&lEdit Resident Permissions"))) {
								inv.remove(item);
								continue;
							}
						}
					}	
				}
			}
			
		}.runTaskTimer(Main.getInstance(), 20, 1);
	}
	
	
	/*
	 * 
	 * REMOVE GLOWING SHULKER
	 * 
	 */
	
	
	public static void ListenersRemoveGlowingBlock(Player player, int blockNumber) {
		World world = player.getWorld();
		List<Entity> entities = world.getEntities();
		for(int i = 0; i < entities.size(); i++) {
			Entity ent = entities.get(i);
			if(ent.getCustomName() == null) continue;
			if(ent.getCustomName().equals(player.getName()+"Block"+blockNumber)) {
				ent.remove();
			}
			continue;
		}
	}
	
	
	/*
	 * 
	 * GET COLOR FROM COLOUR STRING
	 * 
	 */
	
	public static Color ListenersStringToColour(String colour) {
		if(colour == null) return null;
		switch (colour) {
		case "Lime":
			return Color.LIME;
		case "Aqua":
			return Color.AQUA;
		case "Red":
			return Color.RED;
		case "Pink":
			return Color.FUCHSIA;
		case "Yellow":
			return Color.YELLOW;
		case "Green":
			return Color.GREEN;
		case "Cyan":
			return Color.TEAL;
		case "Purple":
			return Color.PURPLE;
		case "Orange":
			return Color.ORANGE;
		case "White":
			return Color.WHITE;
		}
		return null;
	}
	
	
	/*
	 * 
	 * SPAWN GLOWING SHULKER WHEN SELECTING AREA
	 * 
	 */
	
	
	public static void ListenersSpawnGlowingBlock(Player player, Location loc, int blockNumber) {
//		boolean isHiding = ResListeners.userHidingPoints.get(player.getName());
//		EntityHider entityHider = Main.getHider();
		List<Player> otherPlayers = new ArrayList<Player>(Bukkit.getOnlinePlayers());
		otherPlayers.remove(player);
		if(blockNumber == 1) {
			Shulker sh = (Shulker) loc.getWorld().spawnEntity(loc.clone().add(0.5, 0, 0.5), EntityType.SHULKER);
			sh.teleport(loc);
			sh.setAI(false);
			sh.setInvisible(true);
			sh.setInvulnerable(true);
			sh.setGravity(false);
			sh.setPersistent(true);
			sh.setFallDistance(0);
			sh.setCustomName(player.getName()+"Block1");
//			if(isHiding == true) {
//				for(Player p : otherPlayers) {
//					entityHider.hideEntity(p, sh);
//				}	
//			}
			new BukkitRunnable() {

				@Override
				public void run() {
					sh.setGlowing(true);
				}
				
			}.runTaskLater(Main.getInstance(), 5);
			Listeners.task1 = new BukkitRunnable() {

					@Override
					public void run() {
						sh.setTicksLived(1);
					}
					
			}.runTaskTimer(Main.getInstance(), 20, 20);		
		}
		if(blockNumber == 2) {
			Shulker sh = (Shulker) loc.getWorld().spawnEntity(loc.clone().add(0.5, 0, 0.5), EntityType.SHULKER);
			sh.teleport(loc);
			sh.setAI(false);
			sh.setInvisible(true);
			sh.setInvulnerable(true);
			sh.setGravity(false);
			sh.setPersistent(true);
			sh.setFallDistance(0);
			sh.setCustomName(player.getName()+"Block2");
//			if(isHiding == true) {
//				for(Player p : otherPlayers) {
//					entityHider.hideEntity(p, sh);
//				}	
//			}
			new BukkitRunnable() {

				@Override
				public void run() {
					sh.setGlowing(true);
				}
				
			}.runTaskLater(Main.getInstance(), 5);
			Listeners.task2 = new BukkitRunnable() {

					@Override
					public void run() {
						sh.setTicksLived(1);
					}
					
			}.runTaskTimer(Main.getInstance(), 20, 20);
		}
	}
	
	
	/*
	 * 
	 * GET EXTENDED RESIDENCE AREA
	 * 
	 */
	
	public static Cuboid getPlayerExtendedResidenceArea(String values) {
		if(values != null) {
			Cuboid area = null;
			values = values.replace("Cuboid: ", "");
			String[] parts = values.split("/");
			String[] cuboidInfo = parts[0].split(",");
			World world = Bukkit.getWorld(cuboidInfo[0]);
			int x1 = Integer.valueOf(cuboidInfo[1]);
			int y1 = Integer.valueOf(cuboidInfo[2]);
			int z1 = Integer.valueOf(cuboidInfo[3]);
			int x2 = Integer.valueOf(cuboidInfo[4]);
			int y2 = Integer.valueOf(cuboidInfo[5]);
			int z2 = Integer.valueOf(cuboidInfo[6]);
			area = new Cuboid(world, x1, y1, z1, x2, y2, z2);
			return area;
		}
		return null;
	}
	
	
	/*
	 * 
	 * GET PLAYER RESIDENCE FROM STRING VALUES 
	 *
	 */
	
	public static Residence getPlayerResidenceFromValues(LinkedList<Object> values) {
		if(values != null) {
			// Needed variables
			Cuboid area = null;
			String owner = null;
			List<UUID> residents = new ArrayList<>();
			String name = null;
			Location center = null;
			String greeting, farewell;
			boolean allowBlockPlacing, allowBlockBreaking, allowBlockInteraction, allowEntityInteraction, allowDamageEntities,
			allowVehicleDestroy, allowTNTPlacing, allowTeleport, allowEntering, allowCreeperGriefing, allowEndermanGriefing,
			notifyOwn, notifyOther;
			float yaw, pitch;
			// Getting Cuboid area
			String areaString = String.valueOf(values.get(0));
			areaString = areaString.replace("Cuboid: ", "");
			String[] cuboidInfo = areaString.split(",");
			World world = Bukkit.getWorld(cuboidInfo[0]);
			int x1 = Integer.valueOf(cuboidInfo[1]);
			int y1 = Integer.valueOf(cuboidInfo[2]);
			int z1 = Integer.valueOf(cuboidInfo[3]);
			int x2 = Integer.valueOf(cuboidInfo[4]);
			int y2 = Integer.valueOf(cuboidInfo[5]);
			int z2 = Integer.valueOf(cuboidInfo[6]);
			area = new Cuboid(world, x1, y1, z1, x2, y2, z2);
			// Getting owner
			owner = String.valueOf(values.get(1));
			// Getting residents
			String resString = String.valueOf(values.get(2));
			if(resString.equalsIgnoreCase("null")) {
				residents = null;
			}
			else {
				String[] ids = resString.split("/");
				for(String id : ids) {
					residents.add(UUID.fromString(id));	
				}
			}
			// Getting residence name
			name = String.valueOf(values.get(3));
			// Getting residence center
			String centerString = String.valueOf(values.get(4));
			String[] centerParts = centerString.split(";");
			double x = Double.valueOf(centerParts[0]);
			double y = Double.valueOf(centerParts[1]);
			double z = Double.valueOf(centerParts[2]);
			center = new Location(world, x, y, z);
			// Getting greeting
			greeting = String.valueOf(values.get(5));
			// Getting farewell
			farewell = String.valueOf(values.get(6));
			// Getting rules
			allowBlockPlacing = Boolean.valueOf(String.valueOf(values.get(7)));
			allowBlockBreaking = Boolean.valueOf(String.valueOf(values.get(8)));
			allowBlockInteraction = Boolean.valueOf(String.valueOf(values.get(9)));
			allowEntityInteraction = Boolean.valueOf(String.valueOf(values.get(10)));
			allowDamageEntities = Boolean.valueOf(String.valueOf(values.get(11)));
			allowVehicleDestroy = Boolean.valueOf(String.valueOf(values.get(12)));
			allowTNTPlacing = Boolean.valueOf(String.valueOf(values.get(13)));
			allowTeleport = Boolean.valueOf(String.valueOf(values.get(14)));
			allowEntering = Boolean.valueOf(String.valueOf(values.get(15)));
			allowCreeperGriefing = Boolean.valueOf(String.valueOf(values.get(16)));
			allowEndermanGriefing = Boolean.valueOf(String.valueOf(values.get(17)));
			// Getting notifyOwn
			notifyOwn = Boolean.valueOf(String.valueOf(values.get(18)));
			// Getting notifyOther
			notifyOther = Boolean.valueOf(String.valueOf(values.get(19)));
			// Getting yaw
			yaw = Float.valueOf(String.valueOf(values.get(20)));
			// Getting pitch
			pitch = Float.valueOf(String.valueOf(values.get(21)));
			// Creating residence object
			Residence res = new Residence(area, owner, residents, name, center, greeting, farewell, allowBlockPlacing,
					allowBlockBreaking, allowBlockInteraction, allowEntityInteraction, allowDamageEntities, allowVehicleDestroy, allowTNTPlacing,
					allowTeleport, allowEntering, allowCreeperGriefing, allowEndermanGriefing, notifyOwn, notifyOther, yaw, pitch);
			return res;
		}
		return null;
	}
	
	
	/*
	 * 
	 * GET PLAYER RESIDENCE FROM MASTERLIST
	 * 
	 */
	
	
//	public static Residence getPlayerResidenceFromMasterList(Player player) {
//		HashMap<String, String> masterList = Residence.getResidenceMap();
//		for(String o : masterList.keySet()) {
//			if(o.equals(player.getName())) {
//				String values = masterList.get(o);
//				Residence res = getPlayerResidenceFromValues(values);
//				return res;
//			}
//		}
//		return null;
//	}
//	
//	@SuppressWarnings("deprecation")
//	public static Residence getPlayerResidenceFromMasterList(UUID playerUUID) {
//		HashMap<String, String> masterList = Residence.getResidenceMap();
//		for(String o : masterList.keySet()) {
//			Player player = Bukkit.getPlayer(o);
//			UUID uuid = null;
//			if(player == null) {
//				uuid = Bukkit.getOfflinePlayer(o).getUniqueId();
//			}
//			else {
//				uuid = player.getUniqueId();
//			}
//			if(uuid.equals(playerUUID)) {
//				String values = masterList.get(o);
//				Residence res = getPlayerResidenceFromValues(values);
//				return res;
//			}
//		}
//		return null;
//	}
	
	
	/*
	 * 
	 * EXPAND CUBOID AREA
	 * 
	 */
	
	
	public static Cuboid expandArea(Cuboid area, int amount) {
		if(area == null) return null;
		area = area.expand(CuboidDirection.East, amount);
		area = area.expand(CuboidDirection.West, amount);
		area = area.expand(CuboidDirection.North, amount);
		area = area.expand(CuboidDirection.South, amount);
		area = area.expand(CuboidDirection.Up, amount);
		area = area.expand(CuboidDirection.Down, amount);
		return area;
	}
	
	
	/*
	 * 
	 * LAUNCH PLAYER BACK WHEN ENTERING A RESIDENCE
	 * 
	 */
	
	public static void teleportPlayerBack(Player player, String dir) {
		if(dir == null) {
			player.setVelocity(new Vector(1, 0, 0));
			return;
		}
		switch(dir) {
		case "north":
			player.setVelocity(new Vector(0, 1, 2));
			break;
		case "east":
			player.setVelocity(new Vector(-2, 1, 0));
			break;
		case "south":
			player.setVelocity(new Vector(0, 1, -2));
			break;
		case "west":
			player.setVelocity(new Vector(2, 1, 0));
			break;
		case "down":
			player.setVelocity(new Vector(1, 0, 0));
			break;
		default:
			player.setVelocity(new Vector(2, 1, 2));
			break;
		}
	}
	
	
	/*
	 * 
	 * GET KEY FROM VALUE IN A MAP
	 * 
	 */
	
	
	public static <K, V> K getKey(Map<K, V> map, V value) {
	    for (Entry<K, V> entry : map.entrySet()) {
	        if (entry.getValue().equals(value)) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	
	
	/*
	 * 
	 * SET DEFAULT PERMISSIONS FOR PLAYER
	 * 
	 */
	
	public static void setDefaultPerms(Player player) {
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(player.getName());
		List<Node> nodes = (List<Node>) user.getNodes();
		boolean color = false;
		boolean defArea = false;
		boolean maxRes = false;
		for(int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			if(n.getKey().contains("meta.residence\\.particlecolour.")) {
				color = true;
			}
			if(n.getKey().contains("meta.residence\\.defaultarea")) {
				defArea = true;
			}
			if(n.getKey().contains("meta.residence\\.maxresidences")) {
				maxRes = true;
			}
		}
		if(color == false) {
			MetaNode mNode = MetaNode.builder("Residence.particlecolour", Main.particleColour).build();
			user.data().add(mNode);
			api.getUserManager().saveUser(user);
		}
		if(defArea == false) {
			MetaNode defAreaPerm = MetaNode.builder("Residence.defaultarea", String.valueOf(Main.defaultAreaSize)).build();
			user.data().add(defAreaPerm);
			api.getUserManager().saveUser(user);
		}
		if(maxRes == false) {
			MetaNode maxResPerm = MetaNode.builder("Residence.maxResidences", String.valueOf(Main.maxResidences)).build();
			user.data().add(maxResPerm);
			api.getUserManager().saveUser(user);
		}
	}
	
	/*
	 * 
	 * GET USER FROM UUID
	 * 
	 */
	
	
	public static User getUserFromOfflinePlayer(UUID uniqueId) {
		LuckPerms api = Main.getLP();
	    UserManager userManager = api.getUserManager();
	    CompletableFuture<User> userFuture = userManager.loadUser(uniqueId);

	    return userFuture.join();
	}
	
	
	/*
	 * 
	 * CHECK IF RESIDENT CAN
	 * 
	 */
	
	public static boolean checkIfResidentCan(Player builder, String rule, Residence res) {
		boolean residentValue = false;
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(builder.getUniqueId());
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains(res.getName()+ "."+rule)) {// || node.getKey().contains(owner2.getName() + "Residence.allowBlockPlacing")) {
				residentValue = node.getValue();
				break;
			}
		}
		return residentValue;
	}
	
	public static boolean checkIfResidentCan(Player builder, OfflinePlayer owner, String rule, Residence res) {
		boolean residentValue = false;
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(builder.getUniqueId());
		List<Node> nodes = (List<Node>) user.getNodes();
		if(owner != null) {
			for(int i = 0; i < nodes.size(); i++) {
				Node node = nodes.get(i);
				if(node.getKey().contains(res.getName() + "."+rule)) {// || node.getKey().contains(owner2.getName() + "Residence.allowBlockPlacing")) {
					residentValue = node.getValue();
					break;
				}
			}
			return residentValue;
		}
		return residentValue;
	}
	
	public static boolean checkIfResidentCan(Player builder, String owner, String rule, Residence res) {
		boolean residentValue = false;
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(builder.getUniqueId());
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains(res.getName() + "."+rule)) {// || node.getKey().contains(owner2.getName() + "Residence.allowBlockPlacing")) {
				residentValue = node.getValue();
				break;
			}
		}
		return residentValue;
	}
	
	
	public static void updateResidentPermissions(UUID resident, String oldName, String newName) {
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(resident);
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains(oldName + ".")) {// || node.getKey().contains(owner2.getName() + "Residence.allowBlockPlacing")) {
				boolean value = node.getValue();
				String rule = node.getKey().replace(oldName + ".", "");
				user.data().remove(node);
				api.getUserManager().saveUser(user);
				PermissionNode newNode = PermissionNode.builder(newName+"."+rule).build();
				newNode = newNode.toBuilder().value(value).build();
				user.data().add(newNode);
				api.getUserManager().saveUser(user);
			}
		}
	}
	
	/*
	 * 
	 * SPLIT A LIST INTO SUBLISTS
	 * 
	 */

	
	public static <T> List<List<T>> splitList(List<T> list, final int L) {
	    List<List<T>> parts = new ArrayList<List<T>>();
	    final int N = list.size();
	    for (int i = 0; i < N; i += L) {
	        parts.add(new ArrayList<T>(
	            list.subList(i, Math.min(N, i + L)))
	        );
	    }
	    return parts;
	}

	
}
