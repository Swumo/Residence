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
	
	
//	1 	2 	3 	4 	5 	6 	7 	8 	9
//	10  11	12	13	14	15	16	17	18
//	19	20	21	22	23	24	25	26	27
//	28	29	30	31	32	33	34	35	36
//	37	38	39	40	41	42	43	44	45
// 	46	47	48	49	50	51	52	53	54
	
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
    		
//    			else {
//    				String toSave = new String();
//    				List<UUID> residents = res.getResidents();
//    				if(residents.size() > 1) {
//    					for(UUID id : residents) {
//        					OfflinePlayer p = Bukkit.getOfflinePlayer(id);
//        					toSave += p.getName() + ",";
//        				}	
//    				}
//    				else {
//    					toSave += Bukkit.getOfflinePlayer(residents.get(0)).getName();
//    				}
//        			Utils.createPlayerHead(inv, player, 1, 14, "&6&lResidence Info:", "&aArea: &7"+res.getArea().toStringMenu(), "&bResidents: &7" + toSave);
//    			}
    		}
    		// 30 34 37
//    		Utils.createItem(inv, Material.REDSTONE, 1, 30, "&e&lEdit Residence", "&7&oEdit your residence");
//    		Utils.createItem(inv, Material.OAK_SIGN, 1, 32, "&b&lTeleport to Residence", "&7&oTeleport to Residence");
//    		Utils.createItem(inv, Material.TNT, 1, 34, "&c&lDelete Residence", "&7&oDelete your residence");
//    		Utils.createItem(inv, Material.BARRIER, 1, 37, "&cClose", "&7&oClose this menu");
    		
       	
    		
    		toReturn.setContents(inv.getContents());
    	}
	}
	
	
	
	public static Inventory GUI(Player player) {
		Inventory toReturn = Bukkit.createInventory(null, rows, gui_name);
		//HashMap<String, Boolean> rules = res.getRules(res);
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
