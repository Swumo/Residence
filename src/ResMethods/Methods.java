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
import ResUtils.CustomFile;
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
	 * POPULATE MESSAGES.YML
	 * 
	 */
	/**
	 * Populate the messages.yml file
	 * @param messages
	 * @apiNote Should not be used on its own, as it could break some things!
	 */
	public static void populateMessages(CustomFile messages) {
		messages.setHeader("Author - Swumo\n"
				+ "Used variables\n"
				+ "%amount%\n"
				+ "%name%\n"
				+ "%size%\n"
				+ "%currently%\n"
				+ "%count%\n"
				+ "Please do not edit these variables, as it could break the plugin!");
		// Wand
		messages.getConfigField("Wand.Left", "&7&oLeft Click to select 1st position");
		messages.getConfigField("Wand.Right", "&7&oRight Click to select 2nd position");
		messages.getConfigField("Wand.Shift", "&7&oShift Right Click to deselect");
		// Help menu messages
		messages.getConfigField("Help.Res", "&bDisplay this message");
		messages.getConfigField("Help.Menu", "&bOpen Residence Menu");
		messages.getConfigField("Help.Wand", "&bGet/Give Residence Claim Wand &c[Admin Only]");
		messages.getConfigField("Help.Create", "&bCreate your Residence");
		messages.getConfigField("Help.Add", "&bAdd <player> to your Residence");
		messages.getConfigField("Help.Remove", "&bRemove <player> from your Residence");
		messages.getConfigField("Help.Delete", "&bDelete your Residence");
		messages.getConfigField("Help.Delall", "&bDelete &cALL &bResidences &c[Admin Only]");
		messages.getConfigField("Help.List", "&bList all Residences &c[Admin Only]");
		messages.getConfigField("Help.Forceload", "&bForce load every Residence &c[Admin Only]");
		messages.getConfigField("Help.Set", "&bSet greeting/farewell message to <message> | none | default");
		messages.getConfigField("Help.Sethome", "&bSet your Residence home");
		messages.getConfigField("Help.Home", "&bTeleport to your Residence home");
		messages.getConfigField("Help.Oset", "&bSet max claim area in blocks &c[Admin Only]");
		messages.getConfigField("Help.Maxarea", "&bCheck your max amount of claimable blocks");
		messages.getConfigField("Help.Setname", "&bSet a new name for your Residence");
		messages.getConfigField("Help.Showarea", "&bStart/Stop showing the claimed area of your specified Residence to other players with particles");
		// Command messages
		messages.getConfigField("Commands.Reload", "&aConfig reloaded!");
		messages.getConfigField("Commands.NoResidences", "&cYou do not have any Residences!");
		messages.getConfigField("Commands.StartOrStop", "&cPlease specify either &estart &cor &estop&c!");
		messages.getConfigField("Commands.Name", "&cPlease specify a Residence name!");
		messages.getConfigField("Commands.Exists", "&cResidence does not exist!");
		messages.getConfigField("Commands.ShowingArea", "&cYou are already showing this Residence area to other players!");
		messages.getConfigField("Commands.NowShowing", "&aYou are now showing your Residence area to other players!");
		messages.getConfigField("Commands.NotShowing", "&aYou are not showing this Residence's area!");
		messages.getConfigField("Commands.StoppedShowing", "&aYou have stopped showing your Residence area to other players!");
		messages.getConfigField("Commands.NoPerm", "&cYou cannot run this command!");
		messages.getConfigField("Commands.WandGiven", "&eResidence Claim Wand was given to you!");
		messages.getConfigField("Commands.MustStand", "&cYou must stand in your Residence!");
		messages.getConfigField("Commands.HomeSet", "&aResidence home set!");
		messages.getConfigField("Commands.DuplicateNames", "&cYou cannot have duplicate Residence names!");
		messages.getConfigField("Commands.NameSet", "&aResidence name set!");
		messages.getConfigField("Commands.NoPlayer", "&cPlease specify a player!");
		messages.getConfigField("Commands.OwnerAdd", "&cYou cannot add yourself to your own Residence!");
		messages.getConfigField("Commands.OfflinePlayer", "&cPlayer is not online!");
		messages.getConfigField("Commands.AddedPlayer", "&e%name% &ahas been added to your Residence!");
		messages.getConfigField("Commands.AddedTo", "&aYou have been added to &e%name%'s Residence!");
		messages.getConfigField("Commands.OwnerRemove", "&cYou cannot remove yourself from your own Residence!");
		messages.getConfigField("Commands.RemovedPlayer", "&e%name% &ahas been removed from your Residence!");
		messages.getConfigField("Commands.RemovedFrom", "&aYou have been removed from &e%name%'s Residence!");
		messages.getConfigField("Commands.MaxResidences", "&cYou cannot have more than &e%amount% &cResidences!");
		messages.getConfigField("Commands.NoArea", "&cYou do not have an area selected!");
		messages.getConfigField("Commands.AreaOverMax", "&cYou cannot have an area bigger than %size% blocks! (Currently: %currently%)");
		messages.getConfigField("Commands.SelectedAnother", "&cYour selected area claims another Residence! Please select a new area!");
		messages.getConfigField("Commands.Created", "&aResidence created!");
		messages.getConfigField("Commands.ConfirmDelete", "&eAre you sure you want to delete your Residence? Type &aYes &eto delete it or &cNo &eto cancel");
		messages.getConfigField("Commands.AdminDeletedOne", "&eYour Residence has been deleted by an Admin!");
		messages.getConfigField("Commands.AdminDeletedMore", "&eYour Residences have been deleted by an Admin!");
		messages.getConfigField("Commands.AllDeleted", "&aAll Residences successfully deleted!");
		messages.getConfigField("Commands.NoResidencesList", "&cThere are no Residences.");
		messages.getConfigField("Commands.ForceLoad", "&aResidences successfully force loaded!");
		messages.getConfigField("Commands.MessageOrNone", "&cPlease specify a message, &enone &cto remove it or &ereset &cto reset your message!");
		messages.getConfigField("Commands.GreetingSet", "&aYour new Residence greeting message has been set!");
		messages.getConfigField("Commands.FarewellSet", "&aYour new Residence farewell message has been set!");
		messages.getConfigField("Commands.GreetingOrFarewell", "&cPlease specify either &egreeting &cor &efarewell&c!");
		messages.getConfigField("Commands.NewArea", "&cPlease specify a new area in blocks!");
		messages.getConfigField("Commands.AdminSetAreaOther", "&aAn Admin has set your new max area size to &e%size%&a!");
		messages.getConfigField("Commands.AdminSetAreaOwn", "&aYou have set &e%name%'s &anew max area size to &e%size%&a!");
		messages.getConfigField("Commands.NoNumber", "&cPlease specify a number!");
		messages.getConfigField("Commands.AdminSetResidenceOther", "&aAn Admin has set your new max Residence count to &e%count%&a!");
		messages.getConfigField("Commands.AdminSetResidenceOwn", "&aYou have set &e%name%'s &anew max Residence count to &e%count%&a!");
		messages.getConfigField("Commands.MaxClaimBlocks", "&aYour maximum amount of claimable blocks is &e%amount%&a!");
		messages.getConfigField("Commands.MaxResidences", "&aYour maximum amount of Residences is &e%amount%&a!");
	}
	
	
//	/**
//	 * Populate the menuMessages.yml file
//	 * @param messages
//	 * @apiNote Should not be used on its own, as it could break some things!
//	 */
//	public static void populateMenuMessages(CustomFile messages) {
//		// General
//		messages.getConfigField("General.Back", "&cBack");
//		messages.getConfigField("General.Back.Description", "&7&oGo back to main menu");
//		// Edit Menu
//		messages.getConfigField("Edit.Name", "&6&lResidence &0Edit Menu");
//		messages.getConfigField("Edit.AddPlayer", "&a&lAdd Player");
//		messages.getConfigField("Edit.AddPlayer.Description", "&7&oAdd a player to your Residence");
//		messages.getConfigField("Edit.RemovePlayer", "&c&lRemove Player");
//		messages.getConfigField("Edit.RemovePlayer.Description", "&7&oRemove a player from your Residence");
//		messages.getConfigField("Edit.EditGuestRules", "&e&lEdit Guest Rules");
//		messages.getConfigField("Edit.EditGuestRules.Description", "&7&oEdit guest rules");
//		messages.getConfigField("Edit.ChangeAreaSize", "&b&lChange area size");
//		messages.getConfigField("Edit.ChangeAreaSize.Description", "&7&oChange your Residence area size");
//		messages.getConfigField("Edit.EditResidentPermissions", "&e&lEdit Resident Permissions");
//		messages.getConfigField("Edit.EditResidentPermissions.Description", "&7&oEdit per Resident permissions");
//		messages.getConfigField("Edit.Settings", "&f&lSettings");
//		messages.getConfigField("Edit.Settings.Description", "&7&oEdit settings");
//		messages.getConfigField("Edit.ChangeParticleColour", "&e&lChange particle colour when selecting area");
//		messages.getConfigField("Edit.ChangeParticleColour.Description", "&7&oChange particle colour when selecting area");
//		messages.getConfigField("Edit.GeneralRules", "&e&lEdit General Rules");
//		messages.getConfigField("Edit.GeneralRules.Description", "&7&oEdit general rules");
//		// General Rules Menu
//		messages.getConfigField("GenRule.Name", "&6&lResidence &0Gen. Rule Menu");
//		messages.getConfigField("GenRule.Menu", "&bOpen Residence Menu");
//		messages.getConfigField("Help.Wand", "&bGet/Give Residence Claim Wand &c[Admin Only]");
//		messages.getConfigField("Help.Create", "&bCreate your Residence");
//		messages.getConfigField("Help.Add", "&bAdd <player> to your Residence");
//		messages.getConfigField("Help.Remove", "&bRemove <player> from your Residence");
//		messages.getConfigField("Help.Delete", "&bDelete your Residence");
//		messages.getConfigField("Help.Delall", "&bDelete &cALL &bResidences &c[Admin Only]");
//		messages.getConfigField("Help.List", "&bList all Residences &c[Admin Only]");
//		messages.getConfigField("Help.Forceload", "&bForce load every Residence &c[Admin Only]");
//		messages.getConfigField("Help.Set", "&bSet greeting/farewell message to <message> | none | default");
//		messages.getConfigField("Help.Sethome", "&bSet your Residence home");
//		messages.getConfigField("Help.Home", "&bTeleport to your Residence home");
//		messages.getConfigField("Help.Oset", "&bSet max claim area in blocks &c[Admin Only]");
//		messages.getConfigField("Help.Maxarea", "&bCheck your max amount of claimable blocks");
//		messages.getConfigField("Help.Setname", "&bSet a new name for your Residence");
//		messages.getConfigField("Help.Showarea", "&bStart/Stop showing the claimed area of your specified Residence to other players with particles");
//		// Command messages
//		messages.getConfigField("Commands.Reload", "&aConfig reloaded!");
//		messages.getConfigField("Commands.NoResidences", "&cYou do not have any Residences!");
//		messages.getConfigField("Commands.StartOrStop", "&cPlease specify either &estart &cor &estop&c!");
//		messages.getConfigField("Commands.Name", "&cPlease specify a Residence name!");
//		messages.getConfigField("Commands.Exists", "&cResidence does not exist!");
//		messages.getConfigField("Commands.ShowingArea", "&cYou are already showing this Residence area to other players!");
//		messages.getConfigField("Commands.NowShowing", "&aYou are now showing your Residence area to other players!");
//		messages.getConfigField("Commands.NotShowing", "&aYou are not showing this Residence's area!");
//		messages.getConfigField("Commands.StoppedShowing", "&aYou have stopped showing your Residence area to other players!");
//		messages.getConfigField("Commands.NoPerm", "&cYou cannot run this command!");
//		messages.getConfigField("Commands.WandGiven", "&eResidence Claim Wand was given to you!");
//		messages.getConfigField("Commands.MustStand", "&cYou must stand in your Residence!");
//		messages.getConfigField("Commands.HomeSet", "&aResidence home set!");
//		messages.getConfigField("Commands.DuplicateNames", "&cYou cannot have duplicate Residence names!");
//		messages.getConfigField("Commands.NameSet", "&aResidence name set!");
//		messages.getConfigField("Commands.NoPlayer", "&cPlease specify a player!");
//		messages.getConfigField("Commands.OwnerAdd", "&cYou cannot add yourself to your own Residence!");
//		messages.getConfigField("Commands.OfflinePlayer", "&cPlayer is not online!");
//		messages.getConfigField("Commands.AddedPlayer", "&e%name% &ahas been added to your Residence!");
//		messages.getConfigField("Commands.AddedTo", "&aYou have been added to &e%name%'s Residence!");
//		messages.getConfigField("Commands.OwnerRemove", "&cYou cannot remove yourself from your own Residence!");
//		messages.getConfigField("Commands.RemovedPlayer", "&e%name% &ahas been removed from your Residence!");
//		messages.getConfigField("Commands.RemovedFrom", "&aYou have been removed from &e%name%'s Residence!");
//		messages.getConfigField("Commands.MaxResidences", "&cYou cannot have more than &e%amount% &cResidences!");
//		messages.getConfigField("Commands.NoArea", "&cYou do not have an area selected!");
//		messages.getConfigField("Commands.AreaOverMax", "&cYou cannot have an area bigger than %size% blocks! (Currently: %currently%)");
//		messages.getConfigField("Commands.SelectedAnother", "&cYour selected area claims another Residence! Please select a new area!");
//		messages.getConfigField("Commands.Created", "&aResidence created!");
//		messages.getConfigField("Commands.ConfirmDelete", "&eAre you sure you want to delete your Residence? Type &aYes &eto delete it or &cNo &eto cancel");
//		messages.getConfigField("Commands.AdminDeletedOne", "&eYour Residence has been deleted by an Admin!");
//		messages.getConfigField("Commands.AdminDeletedMore", "&eYour Residences have been deleted by an Admin!");
//		messages.getConfigField("Commands.AllDeleted", "&aAll Residences successfully deleted!");
//		messages.getConfigField("Commands.NoResidencesList", "&cThere are no Residences.");
//		messages.getConfigField("Commands.ForceLoad", "&aResidences successfully force loaded!");
//		messages.getConfigField("Commands.MessageOrNone", "&cPlease specify a message, &enone &cto remove it or &ereset &cto reset your message!");
//		messages.getConfigField("Commands.GreetingSet", "&aYour new Residence greeting message has been set!");
//		messages.getConfigField("Commands.FarewellSet", "&aYour new Residence farewell message has been set!");
//		messages.getConfigField("Commands.GreetingOrFarewell", "&cPlease specify either &egreeting &cor &efarewell&c!");
//		messages.getConfigField("Commands.NewArea", "&cPlease specify a new area in blocks!");
//		messages.getConfigField("Commands.AdminSetAreaOther", "&aAn Admin has set your new max area size to &e%size%&a!");
//		messages.getConfigField("Commands.AdminSetAreaOwn", "&aYou have set &e%name%'s &anew max area size to &e%size%&a!");
//		messages.getConfigField("Commands.NoNumber", "&cPlease specify a number!");
//		messages.getConfigField("Commands.AdminSetResidenceOther", "&aAn Admin has set your new max Residence count to &e%count%&a!");
//		messages.getConfigField("Commands.AdminSetResidenceOwn", "&aYou have set &e%name%'s &anew max Residence count to &e%count%&a!");
//		messages.getConfigField("Commands.MaxClaimBlocks", "&aYour maximum amount of claimable blocks is &e%amount%&a!");
//		messages.getConfigField("Commands.MaxResidences", "&aYour maximum amount of Residences is &e%amount%&a!");
//	}
//	
	/*
	 * 
	 * TELEPORTING PLAYER TO HOME
	 * 
	 */
	
	
	@SuppressWarnings("deprecation")
	/**
	 * Teleport player to a home
	 * @param owner - Owner of the residence
	 * @param resName - Name of the residence
	 * @param toTp - Player to teleport
	 */
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
	
	/**
	 * Teleport player to their home
	 * @param player - Player
	 * @param resName - Residence name
	 */
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
	
	/**
	 * Save player home
	 * @param player - Player
	 * @param loc - Location
	 * @param res - Residence
	 */
	public static void saveHome(Player player, Location loc, Residence res) {
		res.setCenter(loc);
		res.setYaw(loc.getYaw());
		res.setPitch(loc.getPitch());
	}
	
	
	/*
	 * 
	 * REMOVING ALL PERMISSIONS EXCEPT META
	 * 
	 */
	
	/**
	 * Remove LuckPerm permission nodes, except for Meta ones, which have the Particle Colour, the default area size and the max amount of residences
	 * @param player - Player
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
	
	/**
	 * Add resident permissions to a player
	 * @param player - Player to add permissions to
	 * @param res - Residence
	 */
	public static void setResidentPerms(Player player, Residence res) {
		LuckPerms api = Main.getLP();
		for(String perm : Residence.allRules) {
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
	 * Remove resident permissions
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
	 * Remove resident permissions
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
	
	/**
	 * Get the max amount of claimable blocks for a player
	 * @param player - Player
	 * @return - Integer
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
	
	/**
	 * Set the max amount of claimable blocks for a player
	 * @param player - Player
	 * @param amount - Integer
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
	
	/**
	 * Get the max amount of residences a player can have
	 * @param player - Player
	 * @return - Integer
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
	
	/**
	 * Set the max amount of residences a player can have
	 * @param player - Player
	 * @param amount - Integer
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
	
	/**
	 * Update the particle menu, used for selecting a preferred particle colour
	 * @param player - Player
	 * @param colour - String
	 * @apiNote Should not be used on its own, as it could break some things!
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
	 * GET RESIDENCE RULE VALUE
	 * 
	 */
	
	/**
	 * Get the residence rule value
	 * @param owner - Player
	 * @param rule - Rule
	 * @return Boolean
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
	
	/**
	 * Get the residence rule value
	 * @param owner - Name of owner
	 * @param rule - Name of the rule
	 * @return Boolean
	 */
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
	
	/**
	 * Update Rule menu when toggling residence rules
	 * @param player - Owner
	 * @param rule - Name of the rule
	 * @param res - Residence
	 * @apiNote Should not be used on its own, as it could break some things!
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
	
	/**
	 * Update Rules in General Rules menu
	 * @param player - Player
	 * @param rule - Name of the rule
	 * @param res - Residence
	 * @apiNote Should not be used on its own, as it could break some things!
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
	
	/**
	 * Get a location from config values
	 * @param player - Player
	 * @param values - String gotten from the config
	 * @return Location or null
	 * @apiNote Should not be used on its own, as it could break some things!
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
	
	/**
	 * Get a location from config values
	 * @param player - Offline player
	 * @param values - String gotten from the config
	 * @return Location or null
	 * @apiNote Should not be used on its own, as it could break some things!
	 */
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
							Location b1Loc = Commands.block1.get(p).getLocation();
							Location b2Loc = Commands.block2.get(p).getLocation();
							List<Vector> edges = Utils.edges(b1Loc.toVector(), b2Loc.toVector());
							for(Vector edge : edges) {
								Location toSpawn = edge.toLocation(p.getWorld());
								Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
								p.spawnParticle(Particle.REDSTONE, toSpawn, 1, dustOptions);	
							}
						}
					}	
				}
			}
			
		}.runTaskTimer(Main.getInstance(), 20, 60);
	}
	
	
	/**
	 * Task for displaying an area of a residence, chosen by the player
	 * @param player - Player
	 * @param edges - List of vectors
	 * @param res - Residence
	 * @apiNote Should not be used on its own, as it could break some things!
	 */
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
	
	/**
	 * Method for removing the glowing block, when selecting an area with the wand
	 * @param player - Player
	 * @param blockNumber - Integer (1-2)
	 * @apiNote Should not be used on its own, as it could break some things!
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
	
	/**
	 * Get a color object from a string
	 * @param colour - Name of the color
	 * @return Color
	 * @apiNote Should not be used on its own, as it could break some things!
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
	
	/**
	 * Spawn in a glowing shulker when selecting an area with the wand
	 * @param player - Player
	 * @param loc - Location
	 * @param blockNumber - Integer (1-2)
	 * @apiNote Should not be used on its own, as it could break some things!
	 */
	public static void ListenersSpawnGlowingBlock(Player player, Location loc, int blockNumber) {
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
	
	/**
	 * Get the extended area of a residence, used for TNT checking
	 * @param values - String of values
	 * @return Cuboid object
	 * @apiNote Should not be used on its own, as it could break some things!
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
	
	/**
	 * Get player residence from a list of objects
	 * @param values - Linked list of objects
	 * @return Residence
	 * @apiNote Should not be used on its own, as it could break some things!
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
	 * EXPAND CUBOID AREA
	 * 
	 */
	
	/**
	 * Expand an area by a specified amount
	 * @param area - Cuboid area
	 * @param amount - Amount of blocks to expand
	 * @return - Expanded Cuboid area
	 * @apiNote Should not be used on its own, as it could break some things!
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
	
	/**
	 * Launch a player back when they try to enter a Residence, which has "allowEntering" rule disabled
	 * @param player - Player
	 * @param dir - Direction to launch
	 * @apiNote Should not be used on its own, as it could break some things!
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
	
	/**
	 * Set the default permissions for a player
	 * @param player - Player
	 * @apiNote Should not be used on its own, as it could break some things!
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
	
	/**
	 * Check if a resident can do something based on the specified rule
	 * @param builder - Resident
	 * @param rule - Name of the rule
	 * @param res - Residence
	 * @return Boolean
	 * @apiNote Should not be used on its own, as it could break some things!
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
	
	/**
	 * Check if a resident can do something based on the specified rule
	 * @param builder - Resident
	 * @param owner - OfflinePlayer owner
	 * @param rule - Name of the rule
	 * @param res - Residence
	 * @return Boolean
	 * @apiNote Should not be used on its own, as it could break some things!
	 */
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
	
	/**
	 * Check if a resident can do something based on the specified rule
	 * @param builder - Resident
	 * @param owner - Name of the owner
	 * @param rule - Name of the rule
	 * @param res - Residence
	 * @return Boolean
	 * @apiNote Should not be used on its own, as it could break some things!
	 */
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
	
	/**
	 * Update resident permissions when the owner changed the residence name
	 * @param resident - UUID of the resident
	 * @param oldName - Old name of the residence
	 * @param newName - New name of the residence
	 * @apiNote Should not be used on its own, as it could break some things!
	 */
	public static void updateResidentPermissions(UUID resident, String oldName, String newName) {
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(resident);
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains(oldName + ".")) {
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
