package ResGUI;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ResClass.Residence;
import ResMain.Main;
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
	
	
	private static void updateRule(Player owner, Player editing, String rule, Residence res) {
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(editing.getName());
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
			api.getUserManager().saveUser(user);
		}
		if(value == true) {
			PermissionNode node = PermissionNode.builder(rule).build();
			node = node.toBuilder().value(false).build();
			user.data().add(node);
			api.getUserManager().saveUser(user);
		}
		updateInventory(owner, editing, res);
		return;
	}
	
	public static void updateInventory(final Player player, Player editing, Residence res) {
		if(editing == null) return;
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
    		LuckPerms api = Main.getLP();
    		User user = api.getUserManager().getUser(editing.getName());
    		List<Node> nodes = (List<Node>) user.getNodes();
    		for(int i = 0; i < nodes.size(); i++) {
    			Node node = nodes.get(i);
    			if(node.getKey().contains(res.getName()+".")) {
    				String key = node.getKey();
    				key = key.replace(res.getName()+".", "");
    				Boolean value = node.getValue();
    				if(value == true) {
    					if(key.equalsIgnoreCase("allowTeleport")) {
    						Utils.createItem(inv, Material.FEATHER, 1, 19, "&e&lToggle &f&l"+editing.getName()+" &d&lTeleport", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowBlockPlacing")) {
    						Utils.createItem(inv, Material.GRASS_BLOCK, 1, 20, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock Placing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowBlockBreaking")) {
    						Utils.createItem(inv, Material.BARRIER, 1, 21, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &c&lBreaking", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowBlockInteraction")) {
    						Utils.createItem(inv, Material.CHEST, 1, 25, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &b&lInteraction", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes interacting with chests, furnaces, etc.", " ", "&7&oAlso includes buttons, trapdoors and pressure plates!");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowEntityInteraction")) {
    						Utils.createItem(inv, Material.ITEM_FRAME, 1, 26, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &a&lPlacing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes placing entities like item frames and armor stands");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowDamageEntities")) {
    						Utils.createItem(inv, Material.ARMOR_STAND, 1, 27, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &4&lDamage", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking entities like item frames and armor stands");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowVehicleDestroy")) {
    						Utils.createItem(inv, Material.MINECART, 1, 31, "&e&lToggle &f&l"+editing.getName()+" &6&lVehicle &c&lBreaking", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking minecarts and boats");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowTNTPlacing")) {
    						Utils.createItem(inv, Material.TNT, 1, 32, "&e&lToggle &f&l"+editing.getName()+" &c&lT&f&lN&c&lT &a&lPlacing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowEntering")) {
    						Utils.createItem(inv, Material.LEATHER_BOOTS, 1, 33, "&e&lToggle &f&l"+editing.getName()+" &e&lEntering", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    						continue;
    					}
    				}
    				if(value == false) {
    					if(key.equalsIgnoreCase("allowTeleport")) {
    						Utils.createItem(inv, Material.FEATHER, 1, 19, "&e&lToggle &f&l"+editing.getName()+" &d&lTeleport", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowBlockPlacing")) {
    						Utils.createItem(inv, Material.GRASS_BLOCK, 1, 20, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock Placing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowBlockBreaking")) {
    						Utils.createItem(inv, Material.BARRIER, 1, 21, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &c&lBreaking", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowBlockInteraction")) {
    						Utils.createItem(inv, Material.CHEST, 1, 25, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &b&lInteraction", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes interacting with chests, furnaces, etc.", " ", "&7&oAlso includes buttons, trapdoors and pressure plates!");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowEntityInteraction")) {
    						Utils.createItem(inv, Material.ITEM_FRAME, 1, 26, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &a&lPlacing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes placing entities like item frames and armor stands");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowDamageEntities")) {
    						Utils.createItem(inv, Material.ARMOR_STAND, 1, 27, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &4&lDamage", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking entities like item frames and armor stands");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowVehicleDestroy")) {
    						Utils.createItem(inv, Material.MINECART, 1, 31, "&e&lToggle &f&l"+editing.getName()+" &6&lVehicle &c&lBreaking", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking minecarts and boats");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowTNTPlacing")) {
    						Utils.createItem(inv, Material.TNT, 1, 32, "&e&lToggle &f&l"+editing.getName()+" &c&lT&f&lN&c&lT &a&lPlacing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    						continue;
    					}
    					if(key.equalsIgnoreCase("allowEntering")) {
    						Utils.createItem(inv, Material.LEATHER_BOOTS, 1, 33, "&e&lToggle &f&l"+editing.getName()+" &e&lEntering", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    						continue;
    					}
    				}
    			}
    		}
       		Utils.createItem(inv, Material.ARROW, 1, 46, "&cBack", "&7&oGo back to the Edit menu");
       		toReturn.setContents(inv.getContents());
    	}
	}
	
	
	
	public static Inventory GUI(Player player, Player editing, Residence res) {
		Inventory toReturn = Bukkit.createInventory(null, rows, gui_name);
		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
		for(int i = 0; i < rows; i++) {
			if(inv.getItem(i) == null) {
				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
			}
			else continue;
		}
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(editing.getName());
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if(node.getKey().contains(res.getName()+".")) {
				String key = node.getKey();
				key = key.replace(res.getName()+".", "");
				Boolean value = node.getValue();
				if(value == true) {
					if(key.equalsIgnoreCase("allowTeleport")) {
						Utils.createItem(inv, Material.FEATHER, 1, 19, "&e&lToggle &f&l"+editing.getName()+" &d&lTeleport", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
						continue;
					}
					if(key.equalsIgnoreCase("allowBlockPlacing")) {
						Utils.createItem(inv, Material.GRASS_BLOCK, 1, 20, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock Placing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
						continue;
					}
					if(key.equalsIgnoreCase("allowBlockBreaking")) {
						Utils.createItem(inv, Material.BARRIER, 1, 21, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &c&lBreaking", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
						continue;
					}
					if(key.equalsIgnoreCase("allowBlockInteraction")) {
						Utils.createItem(inv, Material.CHEST, 1, 25, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &b&lInteraction", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes interacting with chests, furnaces, etc.", " ", "&7&oAlso includes buttons, trapdoors and pressure plates!");
						continue;
					}
					if(key.equalsIgnoreCase("allowEntityInteraction")) {
						Utils.createItem(inv, Material.ITEM_FRAME, 1, 26, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &a&lPlacing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes placing entities like item frames and armor stands");
						continue;
					}
					if(key.equalsIgnoreCase("allowDamageEntities")) {
						Utils.createItem(inv, Material.ARMOR_STAND, 1, 27, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &4&lDamage", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking entities like item frames and armor stands");
						continue;
					}
					if(key.equalsIgnoreCase("allowVehicleDestroy")) {
						Utils.createItem(inv, Material.MINECART, 1, 31, "&e&lToggle &f&l"+editing.getName()+" &6&lVehicle &c&lBreaking", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking minecarts and boats");
						continue;
					}
					if(key.equalsIgnoreCase("allowTNTPlacing")) {
						Utils.createItem(inv, Material.TNT, 1, 32, "&e&lToggle &f&l"+editing.getName()+" &c&lT&f&lN&c&lT &a&lPlacing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
						continue;
					}
					if(key.equalsIgnoreCase("allowEntering")) {
						Utils.createItem(inv, Material.LEATHER_BOOTS, 1, 33, "&e&lToggle &f&l"+editing.getName()+" &e&lEntering", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
						continue;
					}
				}
				if(value == false) {
					if(key.equalsIgnoreCase("allowTeleport")) {
						Utils.createItem(inv, Material.FEATHER, 1, 19, "&e&lToggle &f&l"+editing.getName()+" &d&lTeleport", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
						continue;
					}
					if(key.equalsIgnoreCase("allowBlockPlacing")) {
						Utils.createItem(inv, Material.GRASS_BLOCK, 1, 20, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock Placing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
						continue;
					}
					if(key.equalsIgnoreCase("allowBlockBreaking")) {
						Utils.createItem(inv, Material.BARRIER, 1, 21, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &c&lBreaking", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
						continue;
					}
					if(key.equalsIgnoreCase("allowBlockInteraction")) {
						Utils.createItem(inv, Material.CHEST, 1, 25, "&e&lToggle &f&l"+editing.getName()+" &a&lBlock &b&lInteraction", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes interacting with chests, furnaces, etc.", " ", "&7&oAlso includes buttons, trapdoors and pressure plates!");
						continue;
					}
					if(key.equalsIgnoreCase("allowEntityInteraction")) {
						Utils.createItem(inv, Material.ITEM_FRAME, 1, 26, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &a&lPlacing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes placing entities like item frames and armor stands");
						continue;
					}
					if(key.equalsIgnoreCase("allowDamageEntities")) {
						Utils.createItem(inv, Material.ARMOR_STAND, 1, 27, "&e&lToggle &f&l"+editing.getName()+" &f&lEntity &4&lDamage", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking entities like item frames and armor stands");
						continue;
					}
					if(key.equalsIgnoreCase("allowVehicleDestroy")) {
						Utils.createItem(inv, Material.MINECART, 1, 31, "&e&lToggle &f&l"+editing.getName()+" &6&lVehicle &c&lBreaking", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle", " ", "&7&oIncludes breaking minecarts and boats");
						continue;
					}
					if(key.equalsIgnoreCase("allowTNTPlacing")) {
						Utils.createItem(inv, Material.TNT, 1, 32, "&e&lToggle &f&l"+editing.getName()+" &c&lT&f&lN&c&lT &a&lPlacing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
						continue;
					}
					if(key.equalsIgnoreCase("allowEntering")) {
						Utils.createItem(inv, Material.LEATHER_BOOTS, 1, 33, "&e&lToggle &f&l"+editing.getName()+" &e&lEntering", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
						continue;
					}
				}
			}
		}
   		
   		Utils.createItem(inv, Material.ARROW, 1, 46, "&cBack", "&7&oGo back to the Edit menu");
   		
		toReturn.setContents(inv.getContents());
		return toReturn;
	}
	
	
	public static void openInv(Player player, Player editing, Residence res) {
		Inventory inv = GUI(player, editing, res);
		player.openInventory(inv);
	}
	
	
	
	public static void clicked(Player player, Player editing, int slot, ItemStack clicked, Inventory inv) {
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
		}
	}
}
