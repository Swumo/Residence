package ResGUI;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ResClass.Residence;
import ResUtils.Utils;
import net.md_5.bungee.api.ChatColor;

public class ResidencePlayerPermMenu {
	public static Inventory inv;
	public static String gui_name;
	public static int rows = 2 * 9;

	public static void initialize() {
		gui_name = Utils.normal("&6&lResidence &0Player Config Menu");
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
    		
    		List<UUID> residents = res.getResidents();
    		
    		if(residents == null || residents.isEmpty()) {
//    			Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
    			for(int i = 0; i < rows; i++) {
    				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l &l ");
    			}
    			Utils.createItem(inv, Material.ARROW, 1, 10, Utils.normal("&cBack"));
    			toReturn.setContents(inv.getContents());
    			return;
    		}
    		if(residents.size() > 8) rows = 3 * 9;
    		if(residents.size() > 17) rows = 4 * 9;
    		if(residents.size() > 26) rows = 5 * 9;
    		if(residents.size() > 35) rows = 6 * 9;
    		
    		for(int i = 0; i < residents.size(); i++) {
				Player player1 = Bukkit.getOfflinePlayer(residents.get(i)).getPlayer();
				Utils.createPlayerHead(inv, player1, 1, i+1, "&b"+player1.getName(), "&7&oClick to edit players permissions");
			}	
			//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
			for(int i = residents.size(); i < rows; i++) {
				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " ");
			}
			switch (residents.size()) {
			case 8:
				Utils.createItem(inv, Material.ARROW, 1, 19, Utils.normal("&cBack"));
				break;
			case 17:
				Utils.createItem(inv, Material.ARROW, 1, 28, Utils.normal("&cBack"));
				break;
			case 26:
				Utils.createItem(inv, Material.ARROW, 1, 37, Utils.normal("&cBack"));
				break;
			case 35:
				Utils.createItem(inv, Material.ARROW, 1, 46, Utils.normal("&cBack"));
				break;
			default:
				Utils.createItem(inv, Material.ARROW, 1, 10, Utils.normal("&cBack"));
			}
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
		
		List<UUID> residents = res.getResidents();
		
		if(residents == null || residents.isEmpty()) {
//			Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
			for(int i = 0; i < rows; i++) {
				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l &l ");
			}
			Utils.createItem(inv, Material.ARROW, 1, 10, Utils.normal("&cBack"));
			toReturn.setContents(inv.getContents());
			return toReturn;
		}
		if(residents.size() > 8) rows = 3 * 9;
		if(residents.size() > 17) rows = 4 * 9;
		if(residents.size() > 26) rows = 5 * 9;
		if(residents.size() > 35) rows = 6 * 9;
		
		for(int i = 0; i < residents.size(); i++) {
			OfflinePlayer player1 = Bukkit.getOfflinePlayer(residents.get(i));
			Utils.createPlayerHead(inv, player1, 1, i+1, "&b"+player1.getName(), "&7&oClick to edit players permissions");
		}	
		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
		for(int i = residents.size(); i < rows; i++) {
			Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l &l ");
		}
		switch (residents.size()) {
		case 8:
			Utils.createItem(inv, Material.ARROW, 1, 19, Utils.normal("&cBack"));
			break;
		case 17:
			Utils.createItem(inv, Material.ARROW, 1, 28, Utils.normal("&cBack"));
			break;
		case 26:
			Utils.createItem(inv, Material.ARROW, 1, 37, Utils.normal("&cBack"));
			break;
		case 35:
			Utils.createItem(inv, Material.ARROW, 1, 46, Utils.normal("&cBack"));
			break;
		default:
			Utils.createItem(inv, Material.ARROW, 1, 10, Utils.normal("&cBack"));
		}
		toReturn.setContents(inv.getContents());
		return toReturn;
	}
	
	
	public static void openInv(Player player, Residence res) {
		Inventory inv = GUI(player, res);
		player.openInventory(inv);
	}
	
	
	public static void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		if(clicked.getType() == Material.BLACK_STAINED_GLASS_PANE) { 
			updateInventory(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&cBack"))) {
			player.closeInventory();
			ResidenceEditMenu.openInv(player);
			return;
		}
		else {
			String playerToEditName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
			Player editing = Bukkit.getPlayer(playerToEditName);
			player.closeInventory();
			ResidencePlayerEditMenu.openInv(player, editing, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
	}
}
