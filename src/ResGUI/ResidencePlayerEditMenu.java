package ResGUI;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ResClass.Residence;
import ResMain.Main;
import ResMethods.Methods;
import ResUtils.Utils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;

public class ResidencePlayerEditMenu {
	public static Inventory inv;
	public static String gui_name;
	public static int rows = 6 * 9;

	public static void initialize() {
		gui_name = Utils.normal("&6&lResidence &0Player Config");
		inv = Bukkit.createInventory(null, rows);
	}
	
	
	private static void updateRule(Player owner, OfflinePlayer editing, String rule, Residence res) {
		String checkingRule = rule.substring(rule.indexOf(".")).replace(".", "");
		if(Methods.isRuleDisabled(checkingRule)) {
			return;
		}
		LuckPerms api = Main.getLP();
		User user = Methods.getUserFromOfflinePlayer(editing.getUniqueId());
		List<Node> nodes = (List<Node>) user.getNodes();
		Node n = null;
		boolean value = false;
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains(rule)) {
				n = node;
				value = node.getValue();
				break;
			}
		}
		user.data().remove(n);
		if(value == false) {
			PermissionNode node = PermissionNode.builder(rule).build();
			node = node.toBuilder().value(true).build();
			user.data().add(node);
			api.getUserManager().saveUser(user).join();
		}
		if(value == true) {
			PermissionNode node = PermissionNode.builder(rule).build();
			node = node.toBuilder().value(false).build();
			user.data().add(node);
			api.getUserManager().saveUser(user).join();
		}
		updateInventory(owner, editing, res);	
		return;
	}
	
	public static void updateInventory(final Player player, OfflinePlayer editing, Residence res) {
		if(editing == null) return;
    	Inventory inv = player.getOpenInventory().getTopInventory();
    	if(inv != null) {
    		User user = Methods.getUserFromOfflinePlayer(editing.getUniqueId());
    		List<Node> nodes = (List<Node>) user.getNodes();
    		for(int i = 0; i < nodes.size(); i++) {
    			Node node = nodes.get(i);
    			if(node.getKey().contains(res.getName()+".")) {
    				String key = node.getKey();
    				key = key.replace(res.getName()+".", "");
    				boolean isDisabled = Methods.isRuleDisabled(key);
    				boolean value = node.getValue();
    				switch(key) {
    				case "allowTeleport":
        				if(isDisabled) {
        					Utils.createItem(inv, Material.FEATHER, 1, 19, "&e&lToggle &f&l"+editing.getName()+" &d&lTeleport", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
        					break;
        				}
        				if(value == true) {
        					Utils.createItem(inv, Material.FEATHER, 1, 19, "&e&lToggle &f&l"+editing.getName()+" &d&lTeleport", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");	
        				}
        				if(value == false) {
    						Utils.createItem(inv, Material.FEATHER, 1, 19, "&e&lToggle &f&l"+editing.getName()+" &d&lTeleport", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
        				}
        				break;
        			case "allowBlockPlacing":
        				if(isDisabled) {
        					Utils.createItem(inv, Material.GRASS_BLOCK, 1, 20, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock Placing", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
        					break;
        				}
        				if(value == true) {
    						Utils.createItem(inv, Material.GRASS_BLOCK, 1, 20, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock Placing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
        				}
        				if(value == false) {
    						Utils.createItem(inv, Material.GRASS_BLOCK, 1, 20, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock Placing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
        				}
        				break;
        			case "allowBlockBreaking":
        				if(isDisabled) {
        					Utils.createItem(inv, Material.BARRIER, 1, 21, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &c&lBreaking", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
        					break;
        				}
        				if(value == true) {
    						Utils.createItem(inv, Material.BARRIER, 1, 21, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &c&lBreaking", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
        				}
        				if(value == false) {
    						Utils.createItem(inv, Material.BARRIER, 1, 21, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &c&lBreaking", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
        				}
        				break;
        			case "allowBlockInteraction":
        				if(isDisabled) {
        					Utils.createItem(inv, Material.CHEST, 1, 25, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &b&lInteraction", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
        					break;
        				}
        				if(value == true) {
    						Utils.createItem(inv, Material.CHEST, 1, 25, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &b&lInteraction", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes interacting with chests, furnaces, etc.", " ", "&7&oAlso includes buttons, trapdoors and pressure plates!");
        				}
        				if(value == false) {
    						Utils.createItem(inv, Material.CHEST, 1, 25, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &b&lInteraction", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes interacting with chests, furnaces, etc.", " ", "&7&oAlso includes buttons, trapdoors and pressure plates!");
        				}
        				break;
        			case "allowEntityInteraction":
        				if(isDisabled) {
        					Utils.createItem(inv, Material.ITEM_FRAME, 1, 26, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &a&lPlacing", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
        					break;
        				}
        				if(value == true) {
    						Utils.createItem(inv, Material.ITEM_FRAME, 1, 26, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &a&lPlacing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes placing entities like item frames and armor stands");
        				}
        				if(value == false) {
    						Utils.createItem(inv, Material.ITEM_FRAME, 1, 26, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &a&lPlacing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes placing entities like item frames and armor stands");
        				}
        				break;
        			case "allowDamageEntities":
        				if(isDisabled) {
        					Utils.createItem(inv, Material.ARMOR_STAND, 1, 27, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &4&lDamage", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
        					break;
        				}
        				if(value == true) {
    						Utils.createItem(inv, Material.ARMOR_STAND, 1, 27, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &4&lDamage", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking entities like item frames and armor stands");
        				}
        				if(value == false) {
    						Utils.createItem(inv, Material.ARMOR_STAND, 1, 27, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &4&lDamage", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking entities like item frames and armor stands");
        				}
        				break;
        			case "allowVehicleDestroy":
        				if(isDisabled) {
        					Utils.createItem(inv, Material.MINECART, 1, 31, "&e&lToggle &f&l"+editing.getName()+" &6&lVehicle &c&lBreaking", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
        					break;
        				}
        				if(value == true) {
    						Utils.createItem(inv, Material.MINECART, 1, 31, "&e&lToggle &f&l"+editing.getName()+" &6&lVehicle &c&lBreaking", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking minecarts and boats");
        				}
        				if(value == false) {
    						Utils.createItem(inv, Material.MINECART, 1, 31, "&e&lToggle &f&l"+editing.getName()+" &6&lVehicle &c&lBreaking", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking minecarts and boats");
        				}
        				break;
        			case "allowTNTPlacing":
        				if(isDisabled) {
        					Utils.createItem(inv, Material.TNT, 1, 32, "&e&lToggle &f&l"+editing.getName()+" &c&lT&f&lN&c&lT &a&lPlacing", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
        					break;
        				}
        				if(value == true) {
    						Utils.createItem(inv, Material.TNT, 1, 32, "&e&lToggle &f&l"+editing.getName()+" &c&lT&f&lN&c&lT &a&lPlacing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
        				}
        				if(value == false) {
    						Utils.createItem(inv, Material.TNT, 1, 32, "&e&lToggle &f&l"+editing.getName()+" &c&lT&f&lN&c&lT &a&lPlacing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
        				}
        				break;
        			case "allowEntering":
        				if(isDisabled) {
        					Utils.createItem(inv, Material.LEATHER_BOOTS, 1, 33, "&e&lToggle &f&l"+editing.getName()+" &e&lEntering", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
        					break;
        				}
        				if(value == true) {
    						Utils.createItem(inv, Material.LEATHER_BOOTS, 1, 33, "&e&lToggle &f&l"+editing.getName()+" &e&lEntering", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
        				}
        				if(value == false) {
    						Utils.createItem(inv, Material.LEATHER_BOOTS, 1, 33, "&e&lToggle &f&l"+editing.getName()+" &e&lEntering", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
        				}
        				break;
    				default:
    					break;
        			}
    			}
    		}
       		Utils.createItem(inv, Material.ARROW, 1, 46, "&cBack", "&7&oGo back to the Edit menu");
       		
       		Inventory toReturn = Bukkit.createInventory(null, rows, gui_name);
    		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
    		for(int i = 0; i < rows; i++) {
    			if(inv.getItem(i) == null) {
    				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
    			}
    			else continue;
    		}
       		toReturn.setContents(inv.getContents());
    	}
	}
	
	
	
	public static Inventory GUI(Player player, OfflinePlayer editing, Residence res) {
		User user = Methods.getUserFromOfflinePlayer(editing.getUniqueId());
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains(res.getName()+".")) {
				String key = node.getKey();
				key = key.replace(res.getName()+".", "");
				boolean isDisabled = Methods.isRuleDisabled(key);
				boolean value = node.getValue();
				switch(key) {
				case "allowTeleport":
    				if(isDisabled) {
    					Utils.createItem(inv, Material.FEATHER, 1, 19, "&e&lToggle &f&l"+editing.getName()+" &d&lTeleport", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
    					break;
    				}
    				if(value == true) {
    					Utils.createItem(inv, Material.FEATHER, 1, 19, "&e&lToggle &f&l"+editing.getName()+" &d&lTeleport", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");	
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.FEATHER, 1, 19, "&e&lToggle &f&l"+editing.getName()+" &d&lTeleport", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    				}
    				break;
    			case "allowBlockPlacing":
    				if(isDisabled) {
    					Utils.createItem(inv, Material.GRASS_BLOCK, 1, 20, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock Placing", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
    					break;
    				}
    				if(value == true) {
						Utils.createItem(inv, Material.GRASS_BLOCK, 1, 20, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock Placing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.GRASS_BLOCK, 1, 20, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock Placing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    				}
    				break;
    			case "allowBlockBreaking":
    				if(isDisabled) {
    					Utils.createItem(inv, Material.BARRIER, 1, 21, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &c&lBreaking", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
    					break;
    				}
    				if(value == true) {
						Utils.createItem(inv, Material.BARRIER, 1, 21, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &c&lBreaking", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.BARRIER, 1, 21, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &c&lBreaking", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    				}
    				break;
    			case "allowBlockInteraction":
    				if(isDisabled) {
    					Utils.createItem(inv, Material.CHEST, 1, 25, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &b&lInteraction", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
    					break;
    				}
    				if(value == true) {
						Utils.createItem(inv, Material.CHEST, 1, 25, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &b&lInteraction", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes interacting with chests, furnaces, etc.", " ", "&7&oAlso includes buttons, trapdoors and pressure plates!");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.CHEST, 1, 25, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &b&lInteraction", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes interacting with chests, furnaces, etc.", " ", "&7&oAlso includes buttons, trapdoors and pressure plates!");
    				}
    				break;
    			case "allowEntityInteraction":
    				if(isDisabled) {
    					Utils.createItem(inv, Material.ITEM_FRAME, 1, 26, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &a&lPlacing", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
    					break;
    				}
    				if(value == true) {
						Utils.createItem(inv, Material.ITEM_FRAME, 1, 26, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &a&lPlacing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes placing entities like item frames and armor stands");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.ITEM_FRAME, 1, 26, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &a&lPlacing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes placing entities like item frames and armor stands");
    				}
    				break;
    			case "allowDamageEntities":
    				if(isDisabled) {
    					Utils.createItem(inv, Material.ARMOR_STAND, 1, 27, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &4&lDamage", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
    					break;
    				}
    				if(value == true) {
						Utils.createItem(inv, Material.ARMOR_STAND, 1, 27, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &4&lDamage", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking entities like item frames and armor stands");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.ARMOR_STAND, 1, 27, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &4&lDamage", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking entities like item frames and armor stands");
    				}
    				break;
    			case "allowVehicleDestroy":
    				if(isDisabled) {
    					Utils.createItem(inv, Material.MINECART, 1, 31, "&e&lToggle &f&l"+editing.getName()+" &6&lVehicle &c&lBreaking", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
    					break;
    				}
    				if(value == true) {
						Utils.createItem(inv, Material.MINECART, 1, 31, "&e&lToggle &f&l"+editing.getName()+" &6&lVehicle &c&lBreaking", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking minecarts and boats");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.MINECART, 1, 31, "&e&lToggle &f&l"+editing.getName()+" &6&lVehicle &c&lBreaking", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking minecarts and boats");
    				}
    				break;
    			case "allowTNTPlacing":
    				if(isDisabled) {
    					Utils.createItem(inv, Material.TNT, 1, 32, "&e&lToggle &f&l"+editing.getName()+" &c&lT&f&lN&c&lT &a&lPlacing", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
    					break;
    				}
    				if(value == true) {
						Utils.createItem(inv, Material.TNT, 1, 32, "&e&lToggle &f&l"+editing.getName()+" &c&lT&f&lN&c&lT &a&lPlacing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.TNT, 1, 32, "&e&lToggle &f&l"+editing.getName()+" &c&lT&f&lN&c&lT &a&lPlacing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    				}
    				break;
    			case "allowEntering":
    				if(isDisabled) {
    					Utils.createItem(inv, Material.LEATHER_BOOTS, 1, 33, "&e&lToggle &f&l"+editing.getName()+" &e&lEntering", "&bCurrently: &4&lDISABLED", " ", "&4&lRule has been disabled");	
    					break;
    				}
    				if(value == true) {
						Utils.createItem(inv, Material.LEATHER_BOOTS, 1, 33, "&e&lToggle &f&l"+editing.getName()+" &e&lEntering", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.LEATHER_BOOTS, 1, 33, "&e&lToggle &f&l"+editing.getName()+" &e&lEntering", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    				}
    				break;
				default:
					break;
    			}
			}
		}
   		
   		Utils.createItem(inv, Material.ARROW, 1, 46, "&cBack", "&7&oGo back to the Edit menu");
   		Inventory toReturn = Bukkit.createInventory(null, rows, gui_name);
		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
		for(int i = 0; i < rows; i++) {
			if(inv.getItem(i) == null) {
				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
			}
			else continue;
		}
		toReturn.setContents(inv.getContents());
		return toReturn;
	}
	
	
	public static void openInv(Player player, OfflinePlayer editing, Residence res) {
		Inventory inv = GUI(player, editing, res);
		player.openInventory(inv);
	}
	
	
	
	public static void clicked(Player player, OfflinePlayer editing, int slot, ItemStack clicked, Inventory inv) {
		if(editing == null) {
			if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&cBack"))) {
				player.closeInventory();
				ResidenceEditMenu.openInv(player);
				return;
			}
			if(clicked.getItemMeta().getDisplayName().contains(Utils.normal(" &l  &l "))) { 
				updateInventory(player, editing, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
				return;
			}
		}
		if(editing != null) {
			Residence res = ResidenceMainMenu.selectedResidence.get(player.getUniqueId());
			if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &f&l"+editing.getName()+" &d&lTeleport"))) {
				updateRule(player, editing, res.getName()+".allowTeleport", res);
				return;
			}
			if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &f&l"+editing.getName()+" &a&lBlock Placing"))) {
				updateRule(player, editing, res.getName()+".allowBlockPlacing", res);
				return;
			}
			if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &f&l"+editing.getName()+" &a&lBlock &c&lBreaking"))) {
				updateRule(player, editing, res.getName()+".allowBlockBreaking", res);
				return;
			}
			if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &f&l"+editing.getName()+" &a&lBlock &b&lInteraction"))) {
				updateRule(player, editing, res.getName()+".allowBlockInteraction", res);
				return;
			}
			if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &f&l"+editing.getName()+" &f&lEntity &a&lPlacing"))) {
				updateRule(player, editing, res.getName()+".allowEntityInteraction", res);
				return;
			}
			if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &f&l"+editing.getName()+" &f&lEntity &4&lDamage"))) {
				updateRule(player, editing, res.getName()+".allowDamageEntities", res);
				return;
			}
			if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &f&l"+editing.getName()+" &6&lVehicle &c&lBreaking"))) {
				updateRule(player, editing, res.getName()+".allowVehicleDestroy", res);
				return;
			}
			if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &f&l"+editing.getName()+" &c&lT&f&lN&c&lT &a&lPlacing"))) {
				updateRule(player, editing, res.getName()+".allowTNTPlacing", res);
				return;
			}	
			if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &f&l"+editing.getName()+" &e&lEntering"))) {
				updateRule(player, editing, res.getName()+".allowEntering", res);
				return;
			}
			if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&cBack"))) {
				player.closeInventory();
				ResidenceEditMenu.openInv(player);
				return;
			}
			if(clicked.getItemMeta().getDisplayName().contains(Utils.normal(" &l  &l "))) { 
				updateInventory(player, editing, res);
				return;
			}
		}
	}
}
