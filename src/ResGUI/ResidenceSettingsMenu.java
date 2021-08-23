package ResGUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ResClass.Residence;
import ResUtils.Utils;

public class ResidenceSettingsMenu {

	public static Inventory inv;
	public static String gui_name;
	public static int rows = 3 * 9;

	public static void initialize() {
		gui_name = Utils.normal("&6&lResidence &0Settings");
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
    		
    		boolean notifyOwn = res.getNotifyOwn();
    		boolean notifyOther = res.getNotifyOther();
    		if(notifyOwn == false) {
       			Utils.createItem(inv, Material.REDSTONE, 1, 12, "&b&lSelf-Notify", "&bCurrently: &c&lDISABLED", " ", "&7&oSend greeting/farewell messages to yourself");
       		}
       		if(notifyOther == false) {
       			Utils.createItem(inv, Material.REDSTONE, 1, 16, "&b&lSelf-Awareness", "&bCurrently: &c&lDISABLED", " ", "&7&oSend messages to you when someone enters your Residence");
       		}
       		if(notifyOwn == true) {
       			Utils.createItem(inv, Material.REDSTONE, 1, 12, "&b&lSelf-Notify", "&bCurrently: &a&lENABLED", " ", "&7&oSend greeting/farewell messages to yourself");
       		}
       		if(notifyOther == true) {
       			Utils.createItem(inv, Material.REDSTONE, 1, 16, "&b&lSelf-Awareness", "&bCurrently: &a&lENABLED", " ","&7&oSend messages to you when someone enters your Residence");
       		}
       		Utils.createItem(inv, Material.ARROW, 1, 19, "&cBack", "&7&oGo back to the Edit menu");
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
		
		boolean notifyOwn = res.getNotifyOwn();
		boolean notifyOther = res.getNotifyOther();
		if(notifyOwn == false) {
   			Utils.createItem(inv, Material.REDSTONE, 1, 12, "&b&lSelf-Notify", "&bCurrently: &c&lDISABLED", " ", "&7&oSend greeting/farewell messages to yourself");
   		}
   		if(notifyOther == false) {
   			Utils.createItem(inv, Material.REDSTONE, 1, 16, "&b&lSelf-Awareness", "&bCurrently: &c&lDISABLED", " ", "&7&oSend messages to you when someone enters your Residence");
   		}
   		if(notifyOwn == true) {
   			Utils.createItem(inv, Material.REDSTONE, 1, 12, "&b&lSelf-Notify", "&bCurrently: &a&lENABLED", " ", "&7&oSend greeting/farewell messages to yourself");
   		}
   		if(notifyOther == true) {
   			Utils.createItem(inv, Material.REDSTONE, 1, 16, "&b&lSelf-Awareness", "&bCurrently: &a&lENABLED", " ","&7&oSend messages to you when someone enters your Residence");
   		}
   		Utils.createItem(inv, Material.ARROW, 1, 19, "&cBack", "&7&oGo back to the Edit menu");
		toReturn.setContents(inv.getContents());
		return toReturn;
	}
	
	
	public static void openInv(Player player, Residence res) {
		Inventory inv = GUI(player, res);
		player.openInventory(inv);
	}
	
	
	
	public static void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal(" &l  &l "))) { 
			updateInventory(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&b&lSelf-Notify"))) {
			Residence res = ResidenceMainMenu.selectedResidence.get(player.getUniqueId());
			boolean notifyOwn = res.getNotifyOwn();
			if(notifyOwn == false) {
				Residence.removeResidenceFromList(player.getUniqueId(), res);
				res.setNotifyOwn(true);
				Residence.saveResidenceData(player.getUniqueId(), res, true);
				updateInventory(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
				return;
			}
			if(notifyOwn == true) {
				Residence.removeResidenceFromList(player.getUniqueId(), res);
				res.setNotifyOwn(false);
				Residence.saveResidenceData(player.getUniqueId(), res, true);
				updateInventory(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
				return;
			}
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&b&lSelf-Awareness"))) {
			Residence res = ResidenceMainMenu.selectedResidence.get(player.getUniqueId());
			boolean notifyOther = res.getNotifyOther();
			if(notifyOther == false) {
				Residence.removeResidenceFromList(player.getUniqueId(), res);
				res.setNotifyOther(true);
				Residence.saveResidenceData(player.getUniqueId(), res, true);
				updateInventory(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
				return;
			}
			if(notifyOther == true) {
				Residence.removeResidenceFromList(player.getUniqueId(), res);
				res.setNotifyOther(false);
				Residence.saveResidenceData(player.getUniqueId(), res, true);
				updateInventory(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
				return;
			}
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&cBack"))) {
			player.closeInventory();
			ResidenceEditMenu.openInv(player);
		}
	}
}
