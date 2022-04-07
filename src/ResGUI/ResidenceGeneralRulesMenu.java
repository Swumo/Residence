package ResGUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ResClass.Residence;
import ResMethods.Methods;
import ResUtils.Utils;

public class ResidenceGeneralRulesMenu {


	public static Inventory inv;
	public static String gui_name;
	public static int rows = 5 * 9;

	public static void initialize() {
		gui_name = Utils.normal("&6&lResidence &0Gen. Rule Menu");
		inv = Bukkit.createInventory(null, rows);
	}
	
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
    		boolean creeper = res.getCreeperGriefing();
    		boolean enderman = res.getEndermanGriefing();
    		if(creeper == true) {
    			Utils.createItem(inv, Material.CREEPER_HEAD, 1, 22, "&e&lToggle &2&lCreeper &b&lGriefing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    		}
    		if(creeper == false) {
    			Utils.createItem(inv, Material.CREEPER_HEAD, 1, 22, "&e&lToggle &2&lCreeper &b&lGriefing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    		}
    		if(enderman == true) {
    			Utils.createItem(inv, Material.ENDER_PEARL, 1, 24, "&e&lToggle &5&lEnderman &b&lGriefing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
    		}
    		if(enderman == false) {
    			Utils.createItem(inv, Material.ENDER_PEARL, 1, 24, "&e&lToggle &5&lEnderman &b&lGriefing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
    		}
       		Utils.createItem(inv, Material.ARROW, 1, 37, "&cBack", "&7&oGo back to the Edit menu");
       		toReturn.setContents(inv.getContents());
    	}
	}
	
	
	
	public static Inventory GUI(Player player, Residence res) {
		Inventory toReturn = Bukkit.createInventory(null, rows, gui_name);
		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
		for(int i = 0; i < rows; i++) {
			if(inv.getItem(i) == null) {
				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
			}
			else continue;
		}
		boolean creeper = res.getCreeperGriefing();
		boolean enderman = res.getEndermanGriefing();
		if(creeper == true) {
			Utils.createItem(inv, Material.CREEPER_HEAD, 1, 22, "&e&lToggle &2&lCreeper &b&lGriefing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
		}
		if(creeper == false) {
			Utils.createItem(inv, Material.CREEPER_HEAD, 1, 22, "&e&lToggle &2&lCreeper &b&lGriefing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
		}
		if(enderman == true) {
			Utils.createItem(inv, Material.ENDER_PEARL, 1, 24, "&e&lToggle &5&lEnderman &b&lGriefing", "&bCurrently: &a&lENABLED", " ", "&7&oClick to toggle");
		}
		if(enderman == false) {
			Utils.createItem(inv, Material.ENDER_PEARL, 1, 24, "&e&lToggle &5&lEnderman &b&lGriefing", "&bCurrently: &c&lDISABLED", " ", "&7&oClick to toggle");
		}
   		Utils.createItem(inv, Material.ARROW, 1, 37, "&cBack", "&7&oGo back to the Edit menu");
  
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
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &2&lCreeper &b&lGriefing"))) {
			Methods.GenRuleMenuUpdateRule(player, "allowCreeperGriefing", ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lToggle &5&lEnderman &b&lGriefing"))) {
			Methods.GenRuleMenuUpdateRule(player, "allowEndermanGriefing", ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&cBack"))) {
			player.closeInventory();
			ResidenceEditMenu.openInv(player);
			return;
		}
	}
}
