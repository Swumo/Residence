package ResGUI;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ResClass.Residence;
import ResMethods.Methods;
import ResUtils.Utils;

public class ResidenceRuleMenu {

	public static Inventory inv;
	public static String gui_name;
	public static int rows = 6 * 9;

	public static void initialize() {
		gui_name = Utils.normal("&6&lResidence &0Rule Configuration");
		inv = Bukkit.createInventory(null, rows);
	}
	
//	1 	2 	3 	4 	5 	6 	7 	8 	9
//	10  11	12	13	14	15	16	17	18
//	19	20	21	22	23	24	25	26	27
//	28	29	30	31	32	33	34	35	36
//	37	38	39	40	41	42	43	44	45
// 	46	47	48	49	50	51	52	53	54
	
	public static void updateInventory(final Player player, Residence res) {
    	Inventory inv = player.getOpenInventory().getTopInventory();
    	if(inv != null) {
    		Inventory toReturn = Bukkit.createInventory(null, rows, gui_name);
    		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
    		for(int i = 0; i < rows; i++) {
    			if(inv.getItem(i) == null) {
    				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
    			}
    			else continue;
    		}
    		HashMap<String, Boolean> rules = res.getAllRules();
    		for(String key : rules.keySet()) {
    			boolean value = rules.get(key);
    			switch(key) {
    			case "allowTeleport":
    				if(value == true) {
    					Utils.createItem(inv, Material.FEATHER, 1, 13, "&e&lToggle &d&lTeleport", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");	
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.FEATHER, 1, 13, "&e&lToggle &d&lTeleport", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    				}
    				break;
    			case "allowBlockPlacing":
    				if(value == true) {
						Utils.createItem(inv, Material.GRASS_BLOCK, 1, 14, "&e&lToggle &a&lBlock Placing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.GRASS_BLOCK, 1, 14, "&e&lToggle &a&lBlock Placing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    				}
    				break;
    			case "allowBlockBreaking":
    				if(value == true) {
						Utils.createItem(inv, Material.BARRIER, 1, 15, "&e&lToggle &a&lBlock &c&lBreaking", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.BARRIER, 1, 15, "&e&lToggle &a&lBlock &c&lBreaking", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    				}
    				break;
    			case "allowBlockInteraction":
    				if(value == true) {
						Utils.createItem(inv, Material.CHEST, 1, 22, "&e&lToggle &a&lBlock &b&lInteraction", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes interacting with chests, furnaces, etc.", " ", "&7&oAlso includes buttons, trapdoors and pressure plates!");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.CHEST, 1, 22, "&e&lToggle &a&lBlock &b&lInteraction", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes interacting with chests, furnaces, etc.", " ", "&7&oAlso includes buttons, trapdoors and pressure plates!");
    				}
    				break;
    			case "allowEntityInteraction":
    				if(value == true) {
						Utils.createItem(inv, Material.ITEM_FRAME, 1, 23, "&e&lToggle &f&lEntity &a&lPlacing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes placing entities like item frames and armor stands");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.ITEM_FRAME, 1, 23, "&e&lToggle &f&lEntity &a&lPlacing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes placing entities like item frames and armor stands");
    				}
    				break;
    			case "allowDamageEntities":
    				if(value == true) {
						Utils.createItem(inv, Material.ARMOR_STAND, 1, 24, "&e&lToggle &f&lEntity &4&lDamage", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking entities like item frames and armor stands");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.ARMOR_STAND, 1, 24, "&e&lToggle &f&lEntity &4&lDamage", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking entities like item frames and armor stands");
    				}
    				break;
    			case "allowVehicleDestroy":
    				if(value == true) {
						Utils.createItem(inv, Material.MINECART, 1, 31, "&e&lToggle &6&lVehicle &c&lBreaking", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking minecarts and boats");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.MINECART, 1, 31, "&e&lToggle &6&lVehicle &c&lBreaking", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking minecarts and boats");
    				}
    				break;
    			case "allowTNTPlacing":
    				if(value == true) {
						Utils.createItem(inv, Material.TNT, 1, 32, "&e&lToggle &c&lT&f&lN&c&lT &a&lPlacing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.TNT, 1, 32, "&e&lToggle &c&lT&f&lN&c&lT &a&lPlacing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    				}
    				break;
    			case "allowEntering":
    				if(value == true) {
						Utils.createItem(inv, Material.LEATHER_BOOTS, 1, 33, "&e&lToggle &e&lEntering", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    				}
    				if(value == false) {
						Utils.createItem(inv, Material.LEATHER_BOOTS, 1, 33, "&e&lToggle &e&lEntering", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    				}
    				break;
				default:
					break;
    			}
    		}
       		Utils.createItem(inv, Material.ARROW, 1, 46, "&cBack", "&7&oGo back to the Edit menu");
       		toReturn.setContents(inv.getContents());
    	}
	}
	
	
	
	public static Inventory GUI(Player player, Residence res) {
		Inventory toReturn = Bukkit.createInventory(null, rows, gui_name);
		//HashMap<String, Boolean> rules = res.getRules(res);
		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
		for(int i = 0; i < rows; i++) {
			if(inv.getItem(i) == null) {
				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
			}
			else continue;
		}
		HashMap<String, Boolean> rules = res.getAllRules();
		for(String key : rules.keySet()) {
			boolean value = rules.get(key);
			switch(key) {
			case "allowTeleport":
				if(value == true) {
					Utils.createItem(inv, Material.FEATHER, 1, 13, "&e&lToggle &d&lTeleport", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");	
				}
				if(value == false) {
					Utils.createItem(inv, Material.FEATHER, 1, 13, "&e&lToggle &d&lTeleport", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
				}
				break;
			case "allowBlockPlacing":
				if(value == true) {
					Utils.createItem(inv, Material.GRASS_BLOCK, 1, 14, "&e&lToggle &a&lBlock Placing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
				}
				if(value == false) {
					Utils.createItem(inv, Material.GRASS_BLOCK, 1, 14, "&e&lToggle &a&lBlock Placing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
				}
				break;
			case "allowBlockBreaking":
				if(value == true) {
					Utils.createItem(inv, Material.BARRIER, 1, 15, "&e&lToggle &a&lBlock &c&lBreaking", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
				}
				if(value == false) {
					Utils.createItem(inv, Material.BARRIER, 1, 15, "&e&lToggle &a&lBlock &c&lBreaking", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
				}
				break;
			case "allowBlockInteraction":
				if(value == true) {
					Utils.createItem(inv, Material.CHEST, 1, 22, "&e&lToggle &a&lBlock &b&lInteraction", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes interacting with chests, furnaces, etc.", " ", "&7&oAlso includes buttons, trapdoors and pressure plates!");
				}
				if(value == false) {
					Utils.createItem(inv, Material.CHEST, 1, 22, "&e&lToggle &a&lBlock &b&lInteraction", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes interacting with chests, furnaces, etc.", " ", "&7&oAlso includes buttons, trapdoors and pressure plates!");
				}
				break;
			case "allowEntityInteraction":
				if(value == true) {
					Utils.createItem(inv, Material.ITEM_FRAME, 1, 23, "&e&lToggle &f&lEntity &a&lPlacing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes placing entities like item frames and armor stands");
				}
				if(value == false) {
					Utils.createItem(inv, Material.ITEM_FRAME, 1, 23, "&e&lToggle &f&lEntity &a&lPlacing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes placing entities like item frames and armor stands");
				}
				break;
			case "allowDamageEntities":
				if(value == true) {
					Utils.createItem(inv, Material.ARMOR_STAND, 1, 24, "&e&lToggle &f&lEntity &4&lDamage", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking entities like item frames and armor stands");
				}
				if(value == false) {
					Utils.createItem(inv, Material.ARMOR_STAND, 1, 24, "&e&lToggle &f&lEntity &4&lDamage", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking entities like item frames and armor stands");
				}
				break;
			case "allowVehicleDestroy":
				if(value == true) {
					Utils.createItem(inv, Material.MINECART, 1, 31, "&e&lToggle &6&lVehicle &c&lBreaking", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking minecarts and boats");
				}
				if(value == false) {
					Utils.createItem(inv, Material.MINECART, 1, 31, "&e&lToggle &6&lVehicle &c&lBreaking", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking minecarts and boats");
				}
				break;
			case "allowTNTPlacing":
				if(value == true) {
					Utils.createItem(inv, Material.TNT, 1, 32, "&e&lToggle &c&lT&f&lN&c&lT &a&lPlacing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
				}
				if(value == false) {
					Utils.createItem(inv, Material.TNT, 1, 32, "&e&lToggle &c&lT&f&lN&c&lT &a&lPlacing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
				}
				break;
			case "allowEntering":
				if(value == true) {
					Utils.createItem(inv, Material.LEATHER_BOOTS, 1, 33, "&e&lToggle &e&lEntering", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
				}
				if(value == false) {
					Utils.createItem(inv, Material.LEATHER_BOOTS, 1, 33, "&e&lToggle &e&lEntering", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
				}
				break;
			default:
				break;
			}
		}
   		
   		Utils.createItem(inv, Material.ARROW, 1, 46, "&cBack", "&7&oGo back to the Edit menu");
   		
		toReturn.setContents(inv.getContents());
		return toReturn;
	}
	
	
	public static void openInv(Player player, Residence res) {
		Inventory inv = GUI(player, res);
		player.openInventory(inv);
	}
	
	
	
	public static void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		if(!clicked.getItemMeta().getDisplayName().contains(Utils.normal(" &l  &l "))) { 
			updateInventory(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &d&lTeleport"))) {
			Methods.RuleMenuUpdateRule(player, "allowTeleport", ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &a&lBlock Placing"))) {
			Methods.RuleMenuUpdateRule(player, "allowBlockPlacing", ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &a&lBlock &c&lBreaking"))) {
			Methods.RuleMenuUpdateRule(player, "allowBlockBreaking", ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &a&lBlock &b&lInteraction"))) {
			Methods.RuleMenuUpdateRule(player, "allowBlockInteraction", ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &f&lEntity &a&lPlacing"))) {
			Methods.RuleMenuUpdateRule(player, "allowEntityInteraction", ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &f&lEntity &4&lDamage"))) {
			Methods.RuleMenuUpdateRule(player, "allowDamageEntities", ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &6&lVehicle &c&lBreaking"))) {
			Methods.RuleMenuUpdateRule(player, "allowVehicleDestroy", ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &c&lT&f&lN&c&lT &a&lPlacing"))) {
			Methods.RuleMenuUpdateRule(player, "allowTNTPlacing", ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &e&lEntering"))) {
			Methods.RuleMenuUpdateRule(player, "allowEntering", ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&cBack"))) {
			player.closeInventory();
			ResidenceEditMenu.openInv(player);
			return;
		}
	}
}
