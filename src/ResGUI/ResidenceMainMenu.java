package ResGUI;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ResClass.Residence;
import ResCommands.Commands;
import ResMethods.Methods;
import ResUtils.Utils;
import ResiListeners.ResListeners;

public class ResidenceMainMenu {

	public static Inventory inv;
	public static String gui_name;
	public static int rows = 5 * 9;

	public static void initialize() {
		gui_name = Utils.normal("&6&lResidence &0Menu");
		inv = Bukkit.createInventory(null, rows);
	}
	
	public static HashMap<UUID, Residence> selectedResidence = new HashMap<>();
	
	
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
    		
    		if(res == null) {
    			Utils.createPlayerHead(inv, player, 1, 14, "&6&lResidence Info:", "&cYou do not have a Residence.");
    		}
    		else {
    			if(res.getResidents() == null) {
    				Utils.createPlayerHead(inv, player, 1, 14, "&6&lResidence Info:", "&eName: &7"+res.getName(), "&aArea: &7"+res.getArea().toStringMenu(), "&bResidents: &cNone");
    			}
    			else {
    				String toSave = "";
    				List<UUID> residents = res.getResidents();
    				if(residents.size() > 1) {
    					for(UUID id : residents) {
    						OfflinePlayer p = Bukkit.getOfflinePlayer(id);
    						toSave += p.getName() + ",";
    					}	
    				}
    				else {
    					toSave += Bukkit.getOfflinePlayer(residents.get(0)).getName();
    				}
    				Utils.createPlayerHead(inv, player, 1, 14, "&6&lResidence Info:", "&eName: &7"+res.getName(), "&aArea: &7"+res.getArea().toStringMenu(), "&bResidents: &7" + toSave);
    			}	
    		}
    		Utils.createItem(inv, Material.REDSTONE, 1, 30, "&e&lEdit Residence", "&7&oEdit your residence");
    		Utils.createItem(inv, Material.OAK_SIGN, 1, 32, "&b&lTeleport to Residence", "&7&oTeleport to Residence");
    		Utils.createItem(inv, Material.TNT, 1, 34, "&c&lDelete Residence", "&7&oDelete your residence");
    		Utils.createItem(inv, Material.BARRIER, 1, 37, "&cClose", "&7&oClose this menu");
    		
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
		
		if(res == null) {
			Utils.createPlayerHead(inv, player, 1, 14, "&6&lResidence Info:", "&cYou do not have a Residence.");
		}
		else {
			if(res.getResidents() == null) {
				Utils.createPlayerHead(inv, player, 1, 14, "&6&lResidence Info:", "&eName: &7"+res.getName(), "&aArea: &7"+res.getArea().toStringMenu(), "&bResidents: &cNone");
			}
			else {
				String toSave = "";
				List<UUID> residents = res.getResidents();
				if(residents.size() > 1) {
					for(UUID id : residents) {
						OfflinePlayer p = Bukkit.getOfflinePlayer(id);
						toSave += p.getName() + ",";
					}	
				}
				else {
					toSave += Bukkit.getOfflinePlayer(residents.get(0)).getName();
				}
				Utils.createPlayerHead(inv, player, 1, 14, "&6&lResidence Info:", "&eName: &7"+res.getName(), "&aArea: &7"+res.getArea().toStringMenu(), "&bResidents: &7" + toSave);
			}	
		}
		
		Utils.createItem(inv, Material.REDSTONE, 1, 30, "&e&lEdit Residence", "&7&oEdit your residence");
		Utils.createItem(inv, Material.OAK_SIGN, 1, 32, "&b&lTeleport to Residence", "&7&oTeleport to Residence");
		Utils.createItem(inv, Material.TNT, 1, 34, "&c&lDelete Residence", "&7&oDelete your residence");
		Utils.createItem(inv, Material.BARRIER, 1, 37, "&cClose", "&7&oClose this menu");
		
   		
		
		toReturn.setContents(inv.getContents());
		return toReturn;
	}
	
	
	public static void openInv(Player player, Residence res) {
		Inventory inv = GUI(player, res);
		player.openInventory(inv);
	}
	
	
	
	public static void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		if(!clicked.getItemMeta().getDisplayName().contains(Utils.normal(" &l  &l "))) { 
			updateInventory(player, selectedResidence.get(player.getUniqueId()));
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lEdit Residence"))) {
			player.closeInventory();
			ResidenceEditMenu.openInv(player);
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&c&lDelete Residence"))) {
			if(selectedResidence.get(player.getUniqueId()) == null) {
				return;
			}
			player.closeInventory();
			ResListeners.promptedUser.put(player.getName(), true);
			ResListeners.promptedUserResidence.put(player.getName(), selectedResidence.get(player.getUniqueId()));
			player.sendMessage(Utils.normal(Commands.pluginPrefix+"&cAre you sure you want to delete your Residence? Type &aYes &cto delete your residence or &4No &cto cancel residence deletion"));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&cClose"))) {
			player.closeInventory();
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&b&lTeleport to Residence"))) {
			if(Methods.isRuleDisabled("allowTeleport")) {
				player.sendMessage(Utils.normal(Commands.pluginPrefix+"&cTeleports have been disabled globally"));
				return;
			}
			player.closeInventory();
			ResidenceTeleportMenu.openInv(player);
			return;
		}
	}
}
