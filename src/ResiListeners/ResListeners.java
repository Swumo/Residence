package ResiListeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;

import ResClass.Residence;
import ResCommands.Commands;
import ResMain.Main;
import ResMethods.Methods;
import ResUtils.Cuboid;
import ResUtils.CustomFile;
import ResUtils.MessageUtil;
import ResUtils.Utils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ChatColor;

public class ResListeners implements Listener{
	private final Main plugin;
	
	public ResListeners(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	// Variables
	Player player;
	public static List<Material> applicableCrops = Arrays.asList(Material.WHEAT, Material.POTATOES, Material.CARROTS, Material.BEETROOTS);
	public static HashMap<String, Boolean> playerInResidence = new HashMap<String, Boolean>();
	public static HashMap<String, String> playerWasIn = new HashMap<String, String>();
	public static HashMap<String, String> playerWasInResidence = new HashMap<>();
	private static MessageUtil messageUtil = Main.getMessageUtil();
	private static final List<EntityType> entityBreak = Arrays.stream(EntityType.values()).filter(mat -> mat.name().contains("MINECART") 
			|| mat.name().contains("FRAME") || mat.name().contains("BOAT") || mat.name().contains("ARMOR_STAND") || mat.name().contains("")).toList();
//	private static final List<EntityType> entityBreak = Arrays.asList(
//			EntityType.BOAT, EntityType.ITEM_FRAME, EntityType.ARMOR_STAND, EntityType.MINECART, 
//			EntityType.MINECART_CHEST, EntityType.MINECART_COMMAND, EntityType.MINECART_FURNACE, 
//			EntityType.MINECART_HOPPER, EntityType.MINECART_TNT);
	private static final List<Material> interactiveBlocks = Arrays.stream(Material.values()).filter(mat -> (mat.name().contains("DISPENSER") 
			 || mat.name().contains("DROPPER") || mat.name().contains("CHEST") || mat.name().contains("FURNACE") || mat.name().contains("GRINDSTONE") 
			 || mat.name().contains("TABLE") || mat.name().contains("BOX") || mat.name().contains("ANVIL") || mat.name().contains("BED")
			 || mat.name().contains("LOOM") || mat.name().contains("COMPOSTER") || mat.name().contains("BARREL") || mat.name().contains("SMOKER")
			 || mat.name().contains("BELL") || mat.name().contains("LODESTONE")) || mat.name().contains("LECTERN") && (!mat.name().contains("BOAT") && !mat.name().contains("MINECART") 
					 && !mat.name().contains("BAMBOO") && !mat.name().contains("PLATE") && !mat.name().contains("ROCK") & !mat.name().contains("BOOK"))).toList();
//	private static final List<Material> interactiveBlocks = Arrays.asList(
//			Material.DISPENSER, Material.DROPPER, Material.CHEST, Material.BLAST_FURNACE, 
//			Material.ENDER_CHEST, Material.TRAPPED_CHEST, Material.FURNACE, Material.GRINDSTONE, 
//			Material.ENCHANTING_TABLE, Material.JUKEBOX, Material.ANVIL, Material.CHIPPED_ANVIL, 
//			Material.DAMAGED_ANVIL, Material.SHULKER_BOX, Material.BLACK_BED, Material.BLUE_BED, 
//			Material.BROWN_BED, Material.CYAN_BED, Material.GRAY_BED, Material.GREEN_BED, 
//			Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED, Material.LIME_BED, Material.MAGENTA_BED, 
//			Material.ORANGE_BED, Material.PINK_BED, Material.PURPLE_BED, Material.RED_BED, Material.WHITE_BED, 
//			Material.YELLOW_BED, Material.LOOM, Material.COMPOSTER, Material.BARREL, Material.SMOKER, 
//			Material.CARTOGRAPHY_TABLE, Material.CRAFTING_TABLE, Material.FLETCHING_TABLE, Material.SMITHING_TABLE, 
//			Material.BELL, Material.LODESTONE);
//	private static final List<Material> items = Arrays.asList(
//			Material.WATER_BUCKET, Material.LAVA_BUCKET, Material.ACACIA_SIGN, Material.ACACIA_WALL_SIGN, 
//			Material.BIRCH_SIGN, Material.BIRCH_WALL_SIGN, Material.CRIMSON_SIGN, Material.CRIMSON_WALL_SIGN, 
//			Material.DARK_OAK_SIGN, Material.DARK_OAK_WALL_SIGN, Material.JUNGLE_SIGN, Material.JUNGLE_WALL_SIGN, 
//			Material.OAK_SIGN, Material.OAK_WALL_SIGN, Material.SPRUCE_SIGN, Material.SPRUCE_WALL_SIGN, Material.WARPED_SIGN, 
//			Material.WARPED_WALL_SIGN, Material.ITEM_FRAME, Material.ARMOR_STAND);
	private static final List<Material> items = Arrays.stream(Material.values()).filter(mat -> (mat.name().contains("BUCKET") 
			|| mat.name().contains("SIGN") || mat.name().contains("FRAME") || mat.name().contains("ARMOR_STAND")) && !mat.name().contains("MILK")).toList();
//	private static final List<Material> physicalBlocks = Arrays.asList(
//			Material.ACACIA_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.CRIMSON_FENCE_GATE, Material.DARK_OAK_FENCE_GATE,
//			Material.JUNGLE_FENCE_GATE, Material.OAK_FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.WARPED_FENCE_GATE,    
//			Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.CRIMSON_DOOR, Material.DARK_OAK_DOOR, Material.IRON_DOOR, 
//			Material.JUNGLE_DOOR, Material.OAK_DOOR, Material.SPRUCE_DOOR, Material.WARPED_DOOR, Material.ACACIA_BUTTON, 
//			Material.BIRCH_BUTTON, Material.CRIMSON_BUTTON, Material.DARK_OAK_BUTTON, Material.JUNGLE_BUTTON, 
//			Material.OAK_BUTTON, Material.POLISHED_BLACKSTONE_BUTTON, Material.SPRUCE_BUTTON, Material.STONE_BUTTON, 
//			Material.WARPED_BUTTON, Material.ACACIA_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE, Material.CRIMSON_PRESSURE_PLATE, 
//			Material.DARK_OAK_PRESSURE_PLATE, Material.HEAVY_WEIGHTED_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE, 
//			Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.OAK_PRESSURE_PLATE, Material.POLISHED_BLACKSTONE_PRESSURE_PLATE, 
//			Material.SPRUCE_PRESSURE_PLATE, Material.STONE_PRESSURE_PLATE, Material.WARPED_PRESSURE_PLATE, Material.LEVER,
//			Material.ACACIA_TRAPDOOR, Material.BIRCH_TRAPDOOR, Material.CRIMSON_TRAPDOOR, Material.DARK_OAK_TRAPDOOR, 
//			Material.JUNGLE_TRAPDOOR, Material.OAK_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.WARPED_TRAPDOOR);
	private static final List<Material> physicalBlocks = Arrays.stream(Material.values()).filter(mat -> (mat.name().contains("GATE") 
			|| mat.name().contains("DOOR") || mat.name().contains("BUTTON") || mat.name().contains("PRESSURE") || mat.name().contains("LEVER")) && !mat.name().contains("END")).toList();
	public static HashMap<String, Boolean> promptedUser = new HashMap<>();
	public static HashMap<String, Residence> promptedUserResidence = new HashMap<>();
	public static HashMap<String, Boolean> userInAreaSelection = new HashMap<>();
	public static HashMap<String, Residence> userInAreaSelectionResidence = new HashMap<>();
	public static HashMap<String, Boolean> userLaunched = new HashMap<>();
	public static HashMap<Residence, Boolean> residenceDenyDamage = new HashMap<>();
	private static CustomFile messages = Main.getMessagesFile();
	
	
	
	// Denying block placing in residence
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if(block.getType() == Material.TNT) return;
		Location loc = block.getLocation();
		Residence res = Residence.getResidence(loc);
		if(res == null) return;
		Player owner = Bukkit.getPlayer(res.getOwner());
		OfflinePlayer offOwner = null;
		if(owner == null) {
			offOwner = Bukkit.getOfflinePlayer(res.getOwner());
		}
		if(Main.protectOnlyWhileOffline == true) {
			if(owner != null) {
				return;
			}
		}
		boolean value = res.getAllowBlockPlace();
		if(value == true) {
			return;
		}
		if(value == false) {
			boolean cancelEvent = handleEvent(player, block, res, owner, offOwner, "allowBlockPlacing");
			if(cancelEvent) {
				event.setCancelled(true);
			}
		}
	}

	private boolean handleEvent(Player player, Block block, Residence res, Player owner,
			OfflinePlayer offOwner, String rule) {
		if(Main.protectOnlyWhileOffline == true) {
			List<UUID> residents = res.getResidents();
			if(residents != null) {
				if(residents.contains(player.getUniqueId())) {
					boolean residentValue = false;
					if(offOwner != null) {
						residentValue = Methods.checkIfResidentCan(player, offOwner, rule, res);
						if(residentValue == false) {
							if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
								player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
								return true;
							}
						}
						if(residentValue == true) {
							return false;
						}	
					}
					if(owner == null && offOwner != null) {
						residentValue = Methods.checkIfResidentCan(player, offOwner, rule, res);
						if(residentValue == false) {
							if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
								player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
								return true;
							}
						}
						if(residentValue == true) {
							return false;
						}
					}
				}
				if(res.blockInResidence(block) == true && res.isOwner(player) == false && !residents.contains(player.getUniqueId())) {
					player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
					return true;
				}
			}
			if(residents == null){
				if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
					player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
					return true;
				}
				else if(res.blockInResidence(block) == true && res.isOwner(player) == true){
					return false;
				}
			}
			return false;
		}
		if(Main.protectOnlyWhileOffline == false) {
			List<UUID> residents = res.getResidents();
			if(residents != null) {
				if(residents.contains(player.getUniqueId())) {
					boolean residentValue = false;
					if(owner != null) {
						residentValue = Methods.checkIfResidentCan(player, offOwner, rule, res);
						if(residentValue == false) {
							if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
								player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
								return true;
							}
						}
						if(residentValue == true) {
							return false;
						}	
					}
					if(owner == null && offOwner != null) {
						residentValue = Methods.checkIfResidentCan(player, offOwner, rule, res);
						if(residentValue == false) {
							if(res.blockInResidence(block) == true && res.isOwner(player) == false) { 
								player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
								return true;
							}
						}
						if(residentValue == true) {
							return false;
						}
					}
				}
				if(res.blockInResidence(block) == true && res.isOwner(player) == false && !residents.contains(player.getUniqueId())) {
					player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
					return true;
				}
			}
			if(residents == null){
				if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
					player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
					return true;
				}
				else if(res.blockInResidence(block) == true && res.isOwner(player) == true){
					return false;
				}
			}
			return false;
		}
		return false;
	}
	
	
	// Handle tnt placing
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTntPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if(block.getType() != Material.TNT) return;
		Location loc = block.getLocation();
		Residence res = Residence.getResidence(loc);
		Cuboid tntArea = null;
		if(res == null) {
			HashMap<UUID, LinkedList<Cuboid>> extended = Residence.getExtendedAreasMap();
			HashMap<UUID, LinkedList<Residence>> residences = Residence.getResidenceMap();
			for(UUID o : extended.keySet()) {
				LinkedList<Cuboid> areas = extended.get(o);
				for(Cuboid a : areas) {
					for(UUID owner : residences.keySet()) {
						if(owner.equals(player.getUniqueId())) continue;
						LinkedList<Residence> resi = residences.get(owner);
						for(Residence r : resi) {
							if(a.contains(r.getArea().getCenter())) {
								res = r;
								tntArea = a;
								break;
							}
						}
					}
				}
				if(res != null) {
					break;
				}
			}
		}
		if(res == null) return;
		Player owner = Bukkit.getPlayer(res.getOwner());
		OfflinePlayer offOwner = null;
		if(owner == null) {
			offOwner = Bukkit.getOfflinePlayer(res.getOwner());
		}
		if(Main.protectOnlyWhileOffline == true) {
			if(owner != null) {
				return;
			}
		}
		boolean value = res.getAllowTNTPlacing();
		if(value == true) {
			return;
		}
		if(value == false) {
			boolean cancelEvent = handleTNTPlacing(player, block, res, tntArea, owner, offOwner, "allowTNTPlacing");
			if(cancelEvent) {
				event.setCancelled(true);
			}
		}
	}


	private boolean handleTNTPlacing(Player player, Block block, Residence res, Cuboid tntArea,
			Player owner, OfflinePlayer offOwner, String rule) {
		if(Main.protectOnlyWhileOffline == true) {
			// IF TNT AREA IS NULL BUT RES IS NOT NULL
			if(tntArea == null && res != null) {
				List<UUID> residents = res.getResidents();
				if(residents != null) {
					if(residents.contains(player.getUniqueId())) {
						boolean residentValue = false;
						if(offOwner != null) {
							residentValue = Methods.checkIfResidentCan(player, offOwner, rule, res);
							if(residentValue == false) {
								if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
									player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
									return true;
								}
							}
							if(residentValue == true) {
								return false;
							}	
						}
						if(owner == null && offOwner != null) {
							residentValue = Methods.checkIfResidentCan(player, offOwner.getName(), rule, res);
							if(residentValue == false) {
								if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
									player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
									return true;
								}
							}
							if(residentValue == true) {
								return false;
							}
						}
					}
					if(res.blockInResidence(block) == true && res.isOwner(player) == false && !residents.contains(player.getUniqueId())) {
						player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
						return true;
					}
				}
				if(residents == null){
					if(res.blockInResidence(block) == true) {
						if(res.isOwner(player) == false) {
							player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
							return false;
						}
					}
				}
			}
			//
			// IF TNT AREA AND RES ARE NOT NULL
			List<UUID> residents = res.getResidents();
			if(residents != null) {
				if(res.blockInResidence(block) == true || tntArea.contains(block) == true) {
					if(res.isOwner(player) == false) {
						if(!residents.contains(player.getUniqueId())) {
							player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
							return true;
						}
						boolean residentVal = Methods.checkIfResidentCan(player, offOwner.getName(), rule, res);
						if(residentVal == true) {
							return false;
						}
						if(residentVal == false) {
							player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
							return true;
						}
					}
				}
			}
			if(residents == null){
				if(res.blockInResidence(block) == true || tntArea.contains(block) == true) {
					if(res.isOwner(player) == false) {
						player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
						return true;
					}
				}
			}
			//
		}
		if(Main.protectOnlyWhileOffline == false) {
			// IF TNT AREA IS NULL BUT RES IS NOT NULL
			if(tntArea == null && res != null) {
				List<UUID> residents = res.getResidents();
				if(residents != null) {
					if(residents.contains(player.getUniqueId())) {
						boolean residentValue = false;
						if(owner != null) {
							residentValue = Methods.checkIfResidentCan(player, owner, rule, res);
							if(residentValue == false) {
								if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
									player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
									return true;
								}
							}
							if(residentValue == true) {
								return false;
							}	
						}
						if(owner == null && offOwner != null) {
							residentValue = Methods.checkIfResidentCan(player, offOwner.getName(), rule, res);
							if(residentValue == false) {
								if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
									player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
									return true;
								}
							}
							if(residentValue == true) {
								return false;
							}
						}
					}
					if(res.blockInResidence(block) == true && res.isOwner(player) == false && !residents.contains(player.getUniqueId())) {
						player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
						return true;
					}
				}
				if(residents == null){
					if(res.blockInResidence(block) == true) {
						if(res.isOwner(player) == false) {
							player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
							return true;
						}
					}
				}
			}
			//
			// IF TNT AREA AND RES ARE NOT NULL
			List<UUID> residents = res.getResidents();
			if(residents != null) {
				if(res.blockInResidence(block) == true || tntArea.contains(block) == true) {
					if(res.isOwner(player) == false) {
						if(!residents.contains(player.getUniqueId())) {
							player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
							return true;
						}
						boolean residentVal = Methods.checkIfResidentCan(player, offOwner.getName(), rule, res);
						if(residentVal == true) {
							return false;
						}
						if(residentVal == false) {
							player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
							return true;
						}
					}
				}
			}
			if(residents == null){
				if(res.blockInResidence(block) == true || tntArea.contains(block) == true) {
					if(res.isOwner(player) == false) {
						player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
						return true;
					}
				}
			}
			//
		}
		return true;
	}
	
	// Denying block breaking in a residence
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Location loc = block.getLocation();
		Residence res = Residence.getResidence(loc);
		if(res == null) return;
		Player owner = Bukkit.getPlayer(res.getOwner());
		OfflinePlayer offOwner = null;
		if(owner == null) {
			offOwner = Bukkit.getOfflinePlayer(res.getOwner());
		}
		if(Main.protectOnlyWhileOffline == true) {
			if(owner != null) {
				return;
			}
		}
		boolean value = res.getAllowBlockBreak();
		if(value == true) {
			return;
		}
		if(value == false) {
			boolean cancelEvent = handleEvent(player, block, res, owner, offOwner, "allowBlockBreaking");
			if(cancelEvent) {
				event.setCancelled(true);
			}
		}
	}
	
	// Loading player residence on join
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		LinkedList<Residence> saved = Residence.getResidences(player);
		if(saved == null) {
			List<Player> onlinePlayers = new ArrayList<Player>(Bukkit.getOnlinePlayers());
			onlinePlayers.remove(player);
			for(int i = 0; i < onlinePlayers.size(); i++) {
				Player p = onlinePlayers.get(i);
				Methods.removeResidentPerms(p);
			}
			playerInResidence.put(player.getName(), false);
			playerWasIn.put(player.getName(), "null");
			playerWasInResidence.put(player.getName(), "null");
			promptedUser.put(player.getName(), false);
			promptedUserResidence.put(player.getName(), null);
			userLaunched.put(player.getName(), false);
			Methods.setDefaultPerms(player);
			return;
		}
		for(Residence res : saved) {
			if(res != null) {
				List<Player> onlinePlayers = new ArrayList<Player>(Bukkit.getOnlinePlayers());
				onlinePlayers.remove(player);
				for(int i = 0; i < onlinePlayers.size(); i++) {
					Player p = onlinePlayers.get(i);
					Methods.removeResidentPerms(p);
				}
				playerInResidence.put(player.getName(), false);
				playerWasIn.put(player.getName(), "null");
				playerWasInResidence.put(player.getName(), "null");
				promptedUser.put(player.getName(), false);
				promptedUserResidence.put(player.getName(), null);
				userLaunched.put(player.getName(), false);
				Methods.setDefaultPerms(player);
				return;
			}
		}
		if(!messageUtil.getMessages(player.getUniqueId()).isEmpty()) {
			List<String> messages = messageUtil.getMessages(player.getUniqueId());
			for(int i = 0; i < messages.size(); i++) {
				player.sendMessage(messages.get(i));
			}
		}
	}
	
	
	// Removing positions on leave
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		LuckPerms api = Main.getLP();
		User user = Methods.getUserFromOfflinePlayer(player.getUniqueId());
		api.getUserManager().saveUser(user);
		Commands.block1.remove(player.getUniqueId());
		Commands.block2.remove(player.getUniqueId());
		Methods.ListenersRemoveGlowingBlock(player, 1);
		Methods.ListenersRemoveGlowingBlock(player, 2);
	}
	
	
	// Sending greeting/farewell messages
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(playerInResidence.get(player.getName()) == null) return;
		Location loc = player.getLocation();
		Location below = loc.clone().subtract(0, 1, 0);
		Location above = loc.clone().add(0, 2, 0);
		Residence resi = Residence.getResidence(loc);
		Residence resBelow = Residence.getResidence(below);
		Residence resAbove = Residence.getResidence(above);
		if(resi == null && resBelow == null && resAbove == null) {
			// FAREWELL MESSAGE
			if(playerInResidence.get(player.getName()) == true) {
				String owner = playerWasIn.get(player.getName());
				Player ownerPlayer = Bukkit.getPlayer(owner);
				if(ownerPlayer == null) {
					playerInResidence.put(player.getName(), false);
					playerWasIn.put(player.getName(), "null");
					String resName = playerWasInResidence.get(player.getName());
					Residence r = Residence.getResidence(resName);
					String message = r.getFarewellMessage();
					playerWasInResidence.put(player.getName(), "null");
					if(message.equalsIgnoreCase("null")) {
						boolean notifyOther = r.getNotifyOther();
						if(Boolean.valueOf(notifyOther) == true) {
							if(Bukkit.getOfflinePlayer(owner).isOnline() && !player.getName().equalsIgnoreCase(owner)) {
								Player o = Bukkit.getOfflinePlayer(owner).getPlayer();
								o.sendMessage(Utils.normal("&6"+player.getName() + " &eleft your Residence. &6(" + r.getName() + "&6)"));
							}
						}
						return;
					}
					if(message.contains("%owner%")){
						message = message.replace("%owner%", owner+"'s");
					}
					if(message.contains("%player%")) {
						message = message.replace("%player%", player.getName());
					}
					boolean notifyOther = r.getNotifyOther();
					if(Boolean.valueOf(notifyOther) == true) {
						if(Bukkit.getOfflinePlayer(owner).isOnline() && !player.getName().equalsIgnoreCase(owner)) {
							Player o = Bukkit.getOfflinePlayer(owner).getPlayer();
							o.sendMessage(Utils.normal("&6"+player.getName() + " &eleft your Residence. &6(" + r.getName() + "&6)"));
						}
					}
					boolean notifyOwn = r.getNotifyOwn();
					if(player.getName().equalsIgnoreCase(owner) && Boolean.valueOf(notifyOwn) == false) {
						return;
					}
					player.sendMessage(Utils.normal(message));
					return;		
				}
				playerInResidence.put(player.getName(), false);
				playerWasIn.put(player.getName(), "null");
				String resName = playerWasInResidence.get(player.getName());
				Residence r = Residence.getResidence(resName);
				if(r == null) return;
				String message = r.getFarewellMessage();
				playerWasInResidence.put(player.getName(), "null");
				if(message.equalsIgnoreCase("null")) {
					boolean notifyOther = r.getNotifyOther();
					if(Boolean.valueOf(notifyOther) == true) {
						if(Bukkit.getOfflinePlayer(owner).isOnline() && !player.getName().equalsIgnoreCase(owner)) {
							Player o = Bukkit.getOfflinePlayer(owner).getPlayer();
							o.sendMessage(Utils.normal("&6"+player.getName() + " &eleft your Residence. &6(" + r.getName() + "&6)"));
						}
					}
					return;
				}
				if(message.contains("%owner%")){
					message = message.replace("%owner%", owner+"'s");
				}
				if(message.contains("%player%")) {
					message = message.replace("%player%", player.getName());
				}
				boolean notifyOther = r.getNotifyOther();
				if(Boolean.valueOf(notifyOther) == true) {
					if(Bukkit.getOfflinePlayer(owner).isOnline() && !player.getName().equalsIgnoreCase(owner)) {
						Player o = Bukkit.getOfflinePlayer(owner).getPlayer();
						o.sendMessage(Utils.normal("&6"+player.getName() + " &eleft your Residence. &6(" + r.getName() + "&6)"));
					}
				}
				boolean notifyOwn = r.getNotifyOwn();
				if(player.getName().equalsIgnoreCase(owner) && Boolean.valueOf(notifyOwn) == false) {
					return;
				}
				player.sendMessage(Utils.normal(message));
				return;	
			}
			return;
		}
		// Variables to launch player
		String dir = null;
		HashMap<UUID, LinkedList<HashMap<String, Cuboid>>> directions = Residence.getDirections();
		// GREETING MESSAGE
		if(playerInResidence.get(player.getName()) == false) {
			if(playerWasIn.get(player.getName()) == "null") {
				for(LinkedList<HashMap<String, Cuboid>> val : directions.values()) {
					for(HashMap<String, Cuboid> vals : val) {
						for(Cuboid c : vals.values()) {
							if(c.contains(player.getLocation())) {
								dir = Methods.getKey(vals, c);
								break;
							}
						}
					}
				}
				String owner = null;
				Player ownerPlayer = null;
				if(resi == null) {
					if(resBelow == null) {
						owner = resAbove.getOwner();
						ownerPlayer = Bukkit.getPlayer(owner);
					}
					else {
						owner = resBelow.getOwner();
						ownerPlayer = Bukkit.getPlayer(owner);	
					}
				}
				else {
					owner = resi.getOwner();
					ownerPlayer = Bukkit.getPlayer(owner);
				}
				if(ownerPlayer == null) {
					if(resi == null) {
						if(resBelow == null) {
							resi = resAbove;
						}
						else{
							resi = resBelow;
						}
					}
					OfflinePlayer offOwnerPlayer = Bukkit.getOfflinePlayer(owner);
					boolean allow = resi.getAllowEntering();
					List<UUID> residents = resi.getResidents();
					if(residents == null) {
						if(allow == false && !player.getName().equalsIgnoreCase(owner)) {
							Methods.teleportPlayerBack(player, dir);
							userLaunched.put(player.getName(), true);
							player.sendMessage(Utils.normal("&cYou are not allowed to enter this Residence!"));
							return;
						}	
					}
					if(residents != null) {
						if(residents.contains(player.getUniqueId())) {
							boolean residentValue = Methods.checkIfResidentCan(player, offOwnerPlayer, "allowEntering", resi);
							if(residentValue == false) {
								if(resi.isOwner(player) == false) {
									Methods.teleportPlayerBack(player, dir);
									userLaunched.put(player.getName(), true);
									player.sendMessage(Utils.normal("&cYou are not allowed to enter this Residence!"));
									return;
								}
							}
							if(residentValue == true) {
								playerInResidence.put(player.getName(), true);
								playerWasIn.put(player.getName(), owner);
								playerWasInResidence.put(player.getName(), resi.getName());
								String message = resi.getGreetingMessage();
								if(message.equalsIgnoreCase("null")) {
									boolean notifyOther = resi.getNotifyOther();
									if(Boolean.valueOf(notifyOther) == true) {
										if(Bukkit.getOfflinePlayer(owner).isOnline() && !player.getName().equalsIgnoreCase(owner)) {
											Player o = Bukkit.getOfflinePlayer(owner).getPlayer();
											o.sendMessage(Utils.normal("&6"+player.getName() + " &eentered your Residence. &6(" + resi.getName() + ")"));
										}
									}
									return;
								}
								if(message.contains("%owner%")){
									message = message.replace("%owner%", owner+"'s");
								}
								if(message.contains("%player%")) {
									message = message.replace("%player%", player.getName());
								}
								boolean notifyOther = resi.getNotifyOther();
								if(notifyOther == true) {
									if(Bukkit.getOfflinePlayer(owner).isOnline() && !player.getName().equalsIgnoreCase(owner)) {
										Player o = Bukkit.getOfflinePlayer(owner).getPlayer();
										o.sendMessage(Utils.normal("&6"+player.getName() + " &eentered your Residence. &6(" + resi.getName() + ")"));
									}
								}
								boolean notifyOwn = resi.getNotifyOwn();
								if(player.getName().equalsIgnoreCase(owner) && Boolean.valueOf(notifyOwn) == false) {
									return;
								}
								player.sendMessage(Utils.normal(message));
								return;	
							}
						}
						if(resi.isOwner(player) == false && !residents.contains(player.getUniqueId())) {
							if(allow == false) {
								Methods.teleportPlayerBack(player, dir);
								userLaunched.put(player.getName(), true);
								player.sendMessage(Utils.normal("&cYou are not allowed to enter this Residence!"));
								return;
							}
						}	
					}
					playerInResidence.put(player.getName(), true);
					playerWasIn.put(player.getName(), owner);
					playerWasInResidence.put(player.getName(), resi.getName());
					String message = resi.getGreetingMessage();
					if(message.equalsIgnoreCase("null")) {
						boolean notifyOther = resi.getNotifyOther();
						if(notifyOther == true) {
							if(Bukkit.getOfflinePlayer(owner).isOnline() && !player.getName().equalsIgnoreCase(owner)) {
								Player o = Bukkit.getOfflinePlayer(owner).getPlayer();
								o.sendMessage(Utils.normal("&6"+player.getName() + " &eentered your Residence. &6(" + resi.getName() + ")"));
							}
						}
						return;
					}
					if(message.contains("%owner%")){
						message = message.replace("%owner%", owner+"'s");
					}
					if(message.contains("%player%")) {
						message = message.replace("%player%", player.getName());
					}
					boolean notifyOther = resi.getNotifyOther();
					if(notifyOther == true) {
						if(Bukkit.getOfflinePlayer(owner).isOnline() && !player.getName().equalsIgnoreCase(owner)) {
							Player o = Bukkit.getOfflinePlayer(owner).getPlayer();
							o.sendMessage(Utils.normal("&6"+player.getName() + " &eentered your Residence. &6(" + resi.getName() + ")"));
						}
					}
					boolean notifyOwn = resi.getNotifyOwn();
					if(player.getName().equalsIgnoreCase(owner) && Boolean.valueOf(notifyOwn) == false) {
						return;
					}
					player.sendMessage(Utils.normal(message));
					return;		
				}
				if(resi == null) {
					if(resBelow == null) {
						resi = resAbove;
					}
					else{
						resi = resBelow;
					}
				}
				boolean allow = resi.getAllowEntering();
				List<UUID> residents = resi.getResidents();
				if(residents == null) {
					if(allow == false && !player.getName().equalsIgnoreCase(owner)) {
						Methods.teleportPlayerBack(player, dir);
						userLaunched.put(player.getName(), true);
						player.sendMessage(Utils.normal("&cYou are not allowed to enter this Residence!"));
						return;
					}	
				}
				if(residents != null) {
					if(residents.contains(player.getUniqueId())) {
						boolean residentValue = Methods.checkIfResidentCan(player, resi.getOwner(), "allowEntering", resi);
						if(residentValue == false) {
							if(resi.isOwner(player) == false) {
								Methods.teleportPlayerBack(player, dir);
								userLaunched.put(player.getName(), true);
								player.sendMessage(Utils.normal("&cYou are not allowed to enter this Residence!"));
								return;
							}
						}
						if(residentValue == true) {
							playerInResidence.put(player.getName(), true);
							playerWasIn.put(player.getName(), owner);
							playerWasInResidence.put(player.getName(), resi.getName());
							String message = resi.getGreetingMessage();
							if(message.equalsIgnoreCase("null")) {
								boolean notifyOther = resi.getNotifyOther();
								if(notifyOther == true) {
									if(Bukkit.getOfflinePlayer(owner).isOnline() && !player.getName().equalsIgnoreCase(owner)) {
										Player o = Bukkit.getOfflinePlayer(owner).getPlayer();
										o.sendMessage(Utils.normal("&6"+player.getName() + " &eentered your Residence. &6(" + resi.getName() + ")"));
									}
								}
								return;
							}
							if(message.contains("%owner%")){
								message = message.replace("%owner%", owner+"'s");
							}
							if(message.contains("%player%")) {
								message = message.replace("%player%", player.getName());
							}
							boolean notifyOther = resi.getNotifyOther();
							if(Boolean.valueOf(notifyOther) == true) {
								if(Bukkit.getOfflinePlayer(owner).isOnline() && !player.getName().equalsIgnoreCase(owner)) {
									Player o = Bukkit.getOfflinePlayer(owner).getPlayer();
									o.sendMessage(Utils.normal("&6"+player.getName() + " &eentered your Residence. &6(" + resi.getName() + ")"));
								}
							}
							boolean notifyOwn = resi.getNotifyOwn();
							if(player.getName().equalsIgnoreCase(owner) && Boolean.valueOf(notifyOwn) == false) {
								return;
							}
							player.sendMessage(Utils.normal(message));
							return;	
						}
					}
					if(resi.isOwner(player) == false && !residents.contains(player.getUniqueId())) {
						if(allow == false) {
							Methods.teleportPlayerBack(player, dir);
							userLaunched.put(player.getName(), true);
							player.sendMessage(Utils.normal("&cYou are not allowed to enter this Residence!"));
							return;
						}
					}	
				}
				playerInResidence.put(player.getName(), true);
				playerWasIn.put(player.getName(), owner);
				playerWasInResidence.put(player.getName(), resi.getName());
				String message = resi.getGreetingMessage();
				if(message.equalsIgnoreCase("null")) {
					boolean notifyOther = resi.getNotifyOther();
					if(Boolean.valueOf(notifyOther) == true) {
						if(Bukkit.getOfflinePlayer(owner).isOnline() && !player.getName().equalsIgnoreCase(owner)) {
							Player o = Bukkit.getOfflinePlayer(owner).getPlayer();
							o.sendMessage(Utils.normal("&6"+player.getName() + " &eentered your Residence. &6(" + resi.getName() + ")"));
						}
					}
					return;
				}
				if(message.contains("%owner%")){
					message = message.replace("%owner%", owner+"'s");
				}
				if(message.contains("%player%")) {
					message = message.replace("%player%", player.getName());
				}
				boolean notifyOther = resi.getNotifyOther();
				if(Boolean.valueOf(notifyOther) == true) {
					if(Bukkit.getOfflinePlayer(owner).isOnline() && !player.getName().equalsIgnoreCase(owner)) {
						Player o = Bukkit.getOfflinePlayer(owner).getPlayer();
						o.sendMessage(Utils.normal("&6"+player.getName() + " &eentered your Residence. &6(" + resi.getName() + ")"));
					}
				}
				boolean notifyOwn = resi.getNotifyOwn();
				if(player.getName().equalsIgnoreCase(owner) && Boolean.valueOf(notifyOwn) == false) {
					return;
				}
				player.sendMessage(Utils.normal(message));
				return;		
			}
		}
		return;
	}
	
	
	// Handle fall damage when player was launched away from a residence
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDamage(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if(userLaunched.get(player.getName()) == null || userLaunched.get(player.getName()) == false) return;
		if(event.getCause() != DamageCause.FALL) return;
		if(userLaunched.get(player.getName()) == true && event.getCause() == DamageCause.FALL) {
			userLaunched.put(player.getName(), false);
			event.setCancelled(true);
			return;
		}
	}
	
	
	// Deny entities destroying crops
	@EventHandler
	public void onEntityInteract(EntityInteractEvent event) {
		Block block = event.getBlock();
		if(block == null) {
			return;
		}
		Residence res = Residence.getResidence(block.getLocation());
		if(res != null) {
			if(block.getType() == Material.FARMLAND) {
				event.setCancelled(true);
				return;
			}	
		}
	}
	
	
	// Deny placing/removing of certain entities
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(action.equals(Action.PHYSICAL)) {
			if(block.getType() == Material.FARMLAND) {
				event.setCancelled(true);
				return;
			}
		}
		if(event.getClickedBlock() == null) {
			return;
		}
		Location loc = block.getLocation();
		Residence res = Residence.getResidence(loc);
		ItemStack hand1 = player.getInventory().getItemInMainHand();
		ItemStack hand2 = player.getInventory().getItemInOffHand();
		if(res == null) return;
		Player owner = Bukkit.getPlayer(res.getOwner());
		OfflinePlayer offOwner = null;
		if(owner == null) {
			offOwner = Bukkit.getOfflinePlayer(res.getOwner());
		}
		if(Main.protectOnlyWhileOffline == true) {
			if(owner != null) {
				return;
			}
		}
		boolean value = res.getAllowBlockInteraction();
		if(value == true) {
			return;
		}
		if(value == false) {
			boolean cancelEvent = handleEvent(action, player, block, res, hand1, hand2, owner, offOwner, "allowBlockInteraction");
			if(cancelEvent) {
				event.setCancelled(true);
			}
		}
	}

	private boolean handleEvent(Action action, Player player, Block block, Residence res,
			ItemStack hand1, ItemStack hand2, Player owner, OfflinePlayer offOwner, String rule) {
		List<UUID> residents = res.getResidents();
		// BUCKETS ETC
		if(items.contains(hand1.getType()) || items.contains(hand2.getType())) {
			if(residents != null) {
				if(residents.contains(player.getUniqueId())) {
					boolean residentValue = false;
					if(owner != null) {
						residentValue = Methods.checkIfResidentCan(player, owner, rule, res);
						if(residentValue == false) {
							if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
								if(action.equals(Action.PHYSICAL)) {
									if(block.getType() == Material.FARMLAND) {
										return true;
									}
									if(block.getType().name().contains("PRESSURE")) {
										return true;
									}
								}
								if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
									if(physicalBlocks.contains(block.getType())) {
										return true;
									}
								}
								player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
								return true;
							}
						}
						if(residentValue == true) {
							return false;
						}
					}
					if(owner == null && offOwner != null) {
						residentValue = Methods.checkIfResidentCan(player, offOwner, rule, res);
						if(residentValue == false) {
							if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
								if(action.equals(Action.PHYSICAL)) {
									if(block.getType() == Material.FARMLAND) {
										return true;
									}
									if(block.getType().name().contains("PRESSURE")) {
										return true;
									}
								}
								if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
									if(physicalBlocks.contains(block.getType())) {
										return true;
									}
								}
								player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
								return true;
							}
						}
						if(residentValue == true) {
							return false;
						}	
					}
				}
				if(res.blockInResidence(block) == true && res.isOwner(player) == false && !residents.contains(player.getUniqueId())) {
					if(action.equals(Action.PHYSICAL)) {
						if(block.getType() == Material.FARMLAND) {
							return true;
						}
						if(block.getType().name().contains("PRESSURE")) {
							return true;
						}
					}
					if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
						if(physicalBlocks.contains(block.getType())) {
							return true;
						}
					}
					player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
					return true;
				}	
			}
			else {
				if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
					if(action.equals(Action.PHYSICAL)) {
						if(block.getType() == Material.FARMLAND) {
							return true;
						}
						if(block.getType().name().contains("PRESSURE")) {
							return true;
						}
					}
					if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
						if(physicalBlocks.contains(block.getType())) {
							return true;
						}
					}
					player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
					return true;
				}
			}
		}
		// FURNACES CHESTS ETC
		if(interactiveBlocks.contains(block.getType())) {
			if(res.isOwner(player)) {
				return false;
			}
			if(residents != null) {
				if(residents.contains(player.getUniqueId())) {
					boolean residentValue = false;
					if(owner != null) {
						residentValue = Methods.checkIfResidentCan(player, owner, rule, res);
						if(residentValue == false) {
							if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
								if(action.equals(Action.PHYSICAL)) {
									if(block.getType() == Material.FARMLAND) {
										return true;
									}
									if(block.getType().name().contains("PRESSURE")) {
										return true;
									}
								}
								if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
									if(physicalBlocks.contains(block.getType())) {
										return true;
									}
								}
								player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
								return true;
							}
						}
						if(residentValue == true) {
							return false;
						}	
					}
					if(owner == null && offOwner != null) {
						residentValue = Methods.checkIfResidentCan(player, offOwner, rule, res);
						if(residentValue == false) {
							if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
								if(action.equals(Action.PHYSICAL)) {
									if(block.getType() == Material.FARMLAND) {
										return true;
									}
									if(block.getType().name().contains("PRESSURE")) {
										return true;
									}
								}
								if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
									if(physicalBlocks.contains(block.getType())) {
										return true;
									}
								}
								player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
								return true;
							}
						}
						if(residentValue == true) {
							return false;
						}	
					}
				}
				if(res.blockInResidence(block) == true && res.isOwner(player) == false && !residents.contains(player.getUniqueId())) {
					if(action.equals(Action.PHYSICAL)) {
						if(block.getType() == Material.FARMLAND) {
							return true;
						}
						if(block.getType().name().contains("PRESSURE")) {
							return true;
						}
					}
					if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
						if(physicalBlocks.contains(block.getType())) {
							return true;
						}
					}
					player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
					return true;
				}	
			}
			else {
				if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
					if(action.equals(Action.PHYSICAL)) {
						if(block.getType() == Material.FARMLAND) {
							return true;
						}
						if(block.getType().name().contains("PRESSURE")) {
							return true;
						}
					}
					if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
						if(physicalBlocks.contains(block.getType())) {
							return true;
						}
					}
					player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
					return true;
				}
			}		
		}
		// BUTTONS AND ETC
		if(residents == null) {
			if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
				if(action.equals(Action.PHYSICAL)) {
					if(block.getType() == Material.FARMLAND) {
						return true;
					}
					if(block.getType().name().contains("PRESSURE")) {
						return true;
					}
				}
				if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
					if(physicalBlocks.contains(block.getType())) {
						return true;
					}
				}
			}	
		}
		if(residents != null) {
			if(res.blockInResidence(block) && res.isOwner(player) == false) {
				if(residents.contains(player.getUniqueId())) {
					boolean residentValue = false;
					if(owner != null) {
						residentValue = Methods.checkIfResidentCan(player, owner, rule, res);
						if(residentValue == false) {
							if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
								if(action.equals(Action.PHYSICAL)) {
									if(block.getType() == Material.FARMLAND) {
										return true;
									}
									if(block.getType().name().contains("PRESSURE")) {
										return true;
									}
								}
								if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
									if(physicalBlocks.contains(block.getType())) {
										return true;
									}
								}
							}
						}
						if(residentValue == true) {
							if(action.equals(Action.PHYSICAL)) {
								if(block.getType() == Material.FARMLAND) {
									return true;
								}
							}
							return false;
						}	
					}
					if(owner == null && offOwner != null) {
						residentValue = Methods.checkIfResidentCan(player, offOwner, rule, res);
						if(residentValue == false) {
							if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
								if(action.equals(Action.PHYSICAL)) {
									if(block.getType() == Material.FARMLAND) {
										return true;
									}
									if(block.getType().name().contains("PRESSURE")) {
										return true;
									}
								}
								if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
									if(physicalBlocks.contains(block.getType())) {
										return true;
									}
								}
							}
						}
						if(residentValue == true) {
							return false;
						}	
					}
				}
				if(res.blockInResidence(block) == true && res.isOwner(player) == false && !residents.contains(player.getUniqueId())) {
					if(action.equals(Action.PHYSICAL)) {
						if(block.getType() == Material.FARMLAND) {
							return true;
						}
						if(block.getType().name().contains("PRESSURE")) {
							return true;
						}
					}
					if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
						if(physicalBlocks.contains(block.getType())) {
							return true;
						}
					}
				}	
			}
		}
		return false;
	}
	
	// Deny breaking item frames (with arrows)
	@EventHandler(priority = EventPriority.HIGH)
	private void onHangingBreak(HangingBreakByEntityEvent event) {
		if(!(event.getRemover() instanceof Player)) return;
		Player player = (Player) event.getRemover();
		Entity entity = event.getEntity();
		Location loc = entity.getLocation();
		Residence res = Residence.getResidence(loc);
		if(res == null) return;
		Player owner = Bukkit.getPlayer(res.getOwner());
		OfflinePlayer offOwner = null;
		if(owner == null) {
			offOwner = Bukkit.getOfflinePlayer(res.getOwner());
		}
		if(Main.protectOnlyWhileOffline == true && owner != null) {
			return;
		}
		boolean value = res.getAllowDamageEntities();
		if(value == true) {
			return;
		}
		if(value == false) {
			boolean cancelEvent = handleEvent(player, entity, res, owner, offOwner, "allowDamageEntities");
			if(cancelEvent) {
				event.setCancelled(true);
			}
		}
	}

	private boolean handleEvent(Player player, Entity entity, Residence res, Player owner,
			OfflinePlayer offOwner, String rule) {
		if(Main.protectOnlyWhileOffline == true) {
			List<UUID> residents = res.getResidents();
			if(entityBreak.contains(entity.getType())) {
				if(residents != null) {
					if(residents.contains(player.getUniqueId())) {
						boolean residentValue = false;
						if(offOwner != null) {
							residentValue = Methods.checkIfResidentCan(player, offOwner, rule, res);
							if(residentValue == false) {
								if(res.entityInResidence(entity) == true && res.isOwner(player) == false) {
									player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
									return true;
								}
							}
							if(residentValue == true) {
								return false;
							}	
						}
						if(owner == null && offOwner != null) {
							residentValue = Methods.checkIfResidentCan(player, offOwner, rule, res);
							if(residentValue == false) {
								if(res.entityInResidence(entity) == true && res.isOwner(player) == false) {
									player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
									return true;
								}
							}
							if(residentValue == true) {
								return false;
							}
						}
					}
					if(res.entityInResidence(entity) == true && res.isOwner(player) == false && !residents.contains(player.getUniqueId())) {
						player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
						return true;
					}	
				}
				else {
					if(res.entityInResidence(entity) == true && res.isOwner(player) == false) {
						player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
						return true;
					}	
				}	
			}
		}
		if(Main.protectOnlyWhileOffline == false) {
			List<UUID> residents = res.getResidents();
			if(entityBreak.contains(entity.getType())) {
				if(residents != null) {
					if(residents.contains(player.getUniqueId())) {
						boolean residentValue = false;
						if(owner != null) {
							residentValue = Methods.checkIfResidentCan(player, owner, rule, res);
							if(residentValue == false) {
								if(res.entityInResidence(entity) == true && res.isOwner(player) == false) {
									player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
									return true;
								}
							}
							if(residentValue == true) {
								return false;
							}	
						}
						if(owner == null && offOwner != null) {
							residentValue = Methods.checkIfResidentCan(player, offOwner, rule, res);
							if(residentValue == false) {
								if(res.entityInResidence(entity) == true && res.isOwner(player) == false) {
									player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
									return true;
								}
							}
							if(residentValue == true) {
								return false;
							}
						}
					}
					if(res.entityInResidence(entity) == true && res.isOwner(player) == false && !residents.contains(player.getUniqueId())) {
						player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
						return true;
					}	
				}
				else {
					if(res.entityInResidence(entity) == true && res.isOwner(player) == false) {
						player.sendMessage(Utils.normal(messages.getConfigField("General.NOT"+rule)));
						return true;
					}	
				}	
			}
		}
		return false;
	}
	
	
	// Deny interacting with armor stands
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		Location loc = entity.getLocation();
		Residence res = Residence.getResidence(loc);
		if(res == null) return;
		Player owner = Bukkit.getPlayer(res.getOwner());
		OfflinePlayer offOwner = null;
		if(owner == null) {
			offOwner = Bukkit.getOfflinePlayer(res.getOwner());
		}
		if(Main.protectOnlyWhileOffline == true && owner != null) {
			return;
		}
		boolean value = res.getAllowEntityInteraction();
		if(value == true) {
			return;
		}
		if(value == false) {
			boolean cancelEvent = handleEvent(player, entity, res, owner, offOwner, "allowEntityInteraction");
			if(cancelEvent) {
				event.setCancelled(true);
			}
		}
	}
	
	// Deny interacting with item frames
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		Location loc = entity.getLocation();
		Residence res = Residence.getResidence(loc);
		if(res == null) return;
		Player owner = Bukkit.getPlayer(res.getOwner());
		OfflinePlayer offOwner = null;
		if(owner == null) {
			offOwner = Bukkit.getOfflinePlayer(res.getOwner());
		}
		if(Main.protectOnlyWhileOffline == true && owner != null) {
			return;
		}
		boolean value = res.getAllowEntityInteraction();
		if(value == true) {
			return;
		}
		if(value == false) {
			boolean cancelEvent = handleEvent(player, entity, res, owner, offOwner, "allowEntityInteraction");
			if(cancelEvent) {
				event.setCancelled(true);
			}
		}
	}
	
	// Deny breaking certain entities
	// And breaking item frames with arrows, popping items off with bows from item frames
	@EventHandler
	public void onPlayerInteractEntity(EntityDamageByEntityEvent event) {
		// Item frame stuff
		boolean isItemFrame = false;
		if(!(event.getDamager() instanceof Player)) {
			if(!(event.getEntity() instanceof ItemFrame)) return;
			Projectile proj = (Projectile) event.getDamager();
			if(proj.getShooter() instanceof Player) {
				isItemFrame = true;
			}
			else {
				return;
			}
		}
		Player player = null;
		if(isItemFrame) {
			player = (Player) ((Projectile)event.getDamager()).getShooter();
		}
		else {
			player = (Player) event.getDamager();
		}
		Entity entity = event.getEntity();
		Location loc = entity.getLocation();
		Residence res = Residence.getResidence(loc);
		if(res == null) return;
		Player owner = Bukkit.getPlayer(res.getOwner());
		OfflinePlayer offOwner = null;
		if(owner == null) {
			offOwner = Bukkit.getOfflinePlayer(res.getOwner());
		}
		if(Main.protectOnlyWhileOffline == true && owner != null) {
			return;
		}
		boolean value = res.getAllowDamageEntities();
		if(value == true) {
			return;
		}
		if(value == false) {
			boolean cancelEvent = handleEvent(player, entity, res, owner, offOwner, "allowDamageEntities");
			if(cancelEvent && isItemFrame) {
				event.setCancelled(true);
				event.getDamager().remove();
			}
			if(cancelEvent) {
				event.setCancelled(true);
			}
		}
	}
	
	
	// Deny breaking minecarts
	@EventHandler(priority = EventPriority.NORMAL)
	public void onVehicleDestroy(VehicleDestroyEvent event) {
		Entity attacker = event.getAttacker();
		if((attacker instanceof Player)) {
			Player player = (Player) attacker;
			Entity vehicle = event.getVehicle();
			Location loc = vehicle.getLocation();
			Residence res = Residence.getResidence(loc);
			if(res == null) return;
			Player owner = Bukkit.getPlayer(res.getOwner());
			OfflinePlayer offOwner = null;
			if(owner == null) {
				offOwner = Bukkit.getOfflinePlayer(res.getOwner());
			}
			if(Main.protectOnlyWhileOffline == true && owner != null) {
				return;
			}
			boolean value = res.getAllowVehicleDestroy();
			if(value == true) {
				return;
			}
			if(value == false) {
				boolean cancelEvent = handleEvent(player, vehicle, res, owner, offOwner, "allowVehicleDestroy");
				if(cancelEvent) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	
	// Deny creeper explosions in residence
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEntityExplode(ExplosionPrimeEvent event) {
		Entity ent = event.getEntity();
		HashMap<UUID, LinkedList<Residence>> res = Residence.getResidenceMap();
		for(LinkedList<Residence> r : res.values()) {
			for(Residence resi : r) {
				if(resi.entityInResidence(ent)) {
					boolean griefing = resi.getCreeperGriefing();
					if(griefing == true) return;
					event.setCancelled(true);
					ent.remove();
					break;
				}
			}
		}
		return;
	}
	
	// Deny enderman block pickup in residence
	@EventHandler(priority = EventPriority.HIGH)
	private void onEndermanGrief(EntityChangeBlockEvent event) {
		Entity ent = event.getEntity();
		if(!(ent instanceof Enderman)) return;
		Block block = event.getBlock();
		Residence res = Residence.getResidence(block.getLocation());
		if(res == null) return;
		boolean griefing = res.getEndermanGriefing();
		if(griefing == true) return;
		event.setCancelled(true);
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if(promptedUser.get(player.getName()) == null && userInAreaSelection.get(player.getName()) == null) return;
		boolean value = promptedUser.get(player.getName());
		if(value == true) {
			String message = event.getMessage();
			message = ChatColor.stripColor(message);
			event.setCancelled(true);
			if(message.equalsIgnoreCase("yes")) {
				event.setMessage(null);
				Residence res = promptedUserResidence.get(player.getName());
				List<UUID> residents = res.getResidents();
				if(residents != null) {
					for(int i = 0; i < residents.size(); i++) {
						OfflinePlayer resident = Bukkit.getOfflinePlayer(residents.get(i));
						Methods.removeResidentPerms(resident.getUniqueId(), res);
					}
				}
				Main.deletePlayerResidence(player.getUniqueId(), res);
				Methods.removePermissionsExceptMeta(player);
				player.sendMessage(Utils.normal(Commands.pluginPrefix+messages.getConfigField("General.ResDeleted")));
				promptedUser.put(player.getName(), false);
				return;
			}
			if(message.equalsIgnoreCase("no")) {
				event.setMessage(null);
				promptedUser.put(player.getName(), false);
				player.sendMessage(Utils.normal(Commands.pluginPrefix+messages.getConfigField("General.ResDeletionCancelled")));
				return;
			}
			else {
				event.setCancelled(true);
				event.setMessage(null);
				player.sendMessage(Utils.normal(Commands.pluginPrefix+messages.getConfigField("General.TypeYesNo")));
				return;
			}
		}
		if(userInAreaSelection.get(player.getName()) == null) return;
		boolean val = userInAreaSelection.get(player.getName());
		if(val == true) {
			String message = event.getMessage();
			message = ChatColor.stripColor(message);
			event.setCancelled(true);
			if(message.equalsIgnoreCase("done")) {
				event.setMessage(null);
				if(Commands.block1.containsKey(player.getUniqueId()) == false || Commands.block2.containsKey(player.getUniqueId()) == false) {
					player.sendMessage(Utils.normal(Commands.pluginPrefix+messages.getConfigField("General.TwoPoints")));
					return;
				}
				Residence playerRes = userInAreaSelectionResidence.get(player.getName());
				Location b1Loc = Commands.block1.get(player.getUniqueId()).getLocation();
				Location b2Loc = Commands.block2.get(player.getUniqueId()).getLocation();
				Cuboid newArea = new Cuboid(b1Loc, b2Loc);
				int maxArea = Methods.getPlayerDefaultAreaSize(player);
				if(newArea.getBlocks().size() >= maxArea) {
					player.sendMessage(Utils.normal(Commands.pluginPrefix+messages.getConfigField("General.ResAreaTooBig")));
					return;
				}
				int index = Residence.removeResidenceFromList(player.getUniqueId(), playerRes);
				playerRes.setArea(newArea);
				Residence.saveResidenceData(player.getUniqueId(), playerRes, true, index);
				player.sendMessage(Utils.normal(Commands.pluginPrefix+messages.getConfigField("General.ResAreaSet")));
				userInAreaSelection.put(player.getName(), false);
				Commands.block1.remove(player.getUniqueId());
				Commands.block2.remove(player.getUniqueId());	
				if(Listeners.task1 != null) {
					Listeners.task1.cancel();
				}
				if(Listeners.task2 != null) {
					Listeners.task2.cancel();	
				}
				Methods.ListenersRemoveGlowingBlock(player, 1);
				Methods.ListenersRemoveGlowingBlock(player, 2);
				return;
			}
			if(message.equalsIgnoreCase("cancel")) {
				event.setMessage(null);
				userInAreaSelection.put(player.getName(), false);
				player.sendMessage(Utils.normal(Commands.pluginPrefix+messages.getConfigField("General.ResAreaCancelled")));
				Commands.block1.remove(player.getUniqueId());
				Commands.block2.remove(player.getUniqueId());
				if(Listeners.task1 != null) {
					Listeners.task1.cancel();
				}
				if(Listeners.task2 != null) {
					Listeners.task2.cancel();	
				}
				Methods.ListenersRemoveGlowingBlock(player, 1);
				Methods.ListenersRemoveGlowingBlock(player, 2);
				return;
			}
			else {
				event.setMessage(null);
				player.sendMessage(Utils.normal(Commands.pluginPrefix+messages.getConfigField("General.TypeDoneCancel")));
				return;
			}
		}
	}
}
