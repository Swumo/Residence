package ResGUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ResClass.Residence;
import ResUtils.Utils;
import net.md_5.bungee.api.ChatColor;

public class ResidencePreMainMenu {

	public static Inventory inv;
	public static String gui_name;
	public static int rows = 2 * 9;

	public static void initialize() {
		gui_name = Utils.normal("&6&lResidence &0Select Menu");
		inv = Bukkit.createInventory(null, rows);
	}
	
	public static void updateInventory(final Player player) {
    	Inventory inv = player.getOpenInventory().getTopInventory();
    	if(inv != null) {
    		Inventory toReturn = Bukkit.createInventory(null, rows, gui_name);
    		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
    		for(int i = 0; i < rows; i++) {
    			if(inv.getItem(i) == null) {
    				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, "&l &l &l  &l ");
    			}
    			else continue;
    		}
    		

    		HashMap<UUID, LinkedList<Residence>> map = Residence.getResidenceMap();
    		List<Residence> residences = new ArrayList<>();
    		for(UUID id : map.keySet()) {
    			if(id.equals(player.getUniqueId())) {
    				LinkedList<Residence> resi = map.get(id);
    				for(Residence res : resi) {
    					if(!residences.contains(res)) {
    						residences.add(res);
    					}
    				}
    			}
    		}
    		if(residences == null || residences.isEmpty()) {
    			Inventory i = ResidenceMainMenu.GUI(player, null);
    			ResidenceMainMenu.selectedResidence.put(player.getUniqueId(), null);
    			toReturn.setContents(i.getContents());
    			return;
    		}
    		else {
    			for(int i = 0; i < residences.size(); i++) {
    				Utils.createItem(inv, Material.GRASS_BLOCK, 1, i+1, "&e"+residences.get(i).getName(), "&7&oClick to select");
    			}
    			for(int i = residences.size(); i < rows; i++) {
    				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, "&l &l &l  &l ");
    			}
    		
       	
    		
    		toReturn.setContents(inv.getContents());
    		}
    	}
	}
	
	
	
	public static Inventory GUI(Player player) {
		Inventory toReturn = Bukkit.createInventory(null, rows, gui_name);
		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
		for(int i = 0; i < rows; i++) {
			if(inv.getItem(i) == null) {
				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, "&l &l &l  &l ");
			}
			else continue;
		}
		
		HashMap<UUID, LinkedList<Residence>> map = Residence.getResidenceMap();
		List<Residence> residences = new ArrayList<>();
		for(UUID id : map.keySet()) {
			if(id.equals(player.getUniqueId())) {
				LinkedList<Residence> resi = map.get(id);
				for(Residence res : resi) {
					if(!residences.contains(res)) {
						residences.add(res);
					}
				}
			}
		}
		if(residences == null || residences.isEmpty()) {
			Inventory i = ResidenceMainMenu.GUI(player, null);
			ResidenceMainMenu.selectedResidence.put(player.getUniqueId(), null);
			return i;
		}
		else {
			for(int i = 0; i < residences.size(); i++) {
				Utils.createItem(inv, Material.GRASS_BLOCK, 1, i+1, "&e"+residences.get(i).getName(), "&7&oClick to select");
			}
			for(int i = residences.size(); i < rows; i++) {
				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, "&l &l &l  &l ");
			}
			residences.clear();
		
			toReturn.setContents(inv.getContents());
			return toReturn;	
		}
	}
	
	
	public static void openInv(Player player) {
		Inventory inv = GUI(player);
		player.openInventory(inv);
	}
	
	
	
	public static void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		if(!clicked.getItemMeta().getDisplayName().contains(Utils.normal("&l &r&l &r&l  &r&l "))) { 
			player.closeInventory();
			String clickName = clicked.getItemMeta().getDisplayName();
			clickName = ChatColor.stripColor(clickName);
			Residence res = Residence.getResidence(clickName);
			if(res == null) {
				return;
			}
			ResidenceMainMenu.selectedResidence.put(player.getUniqueId(), res);
			ResidenceMainMenu.openInv(player, res);
			return;
		}
		else {
			updateInventory(player);
		}
	}
}
