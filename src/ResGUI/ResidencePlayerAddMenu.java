package ResGUI;

import java.util.ArrayList;
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
import net.md_5.bungee.api.ChatColor;

public class ResidencePlayerAddMenu {
	public static Inventory inv;
	public static String gui_name;
	public static int rows = 2 * 9;

	public static void initialize() {
		gui_name = Utils.normal("&6&lResidence &0Player Add Menu");
		inv = Bukkit.createInventory(null, rows);
	}
	
	public static void updateInventory(final Player player, Residence res) {
    	Inventory inv = player.getOpenInventory().getTopInventory();
    	if(inv != null) {
    		
    		List<Player> players = new ArrayList<Player>(Bukkit.getOnlinePlayers());
    		players.remove(player);
    		List<UUID> residents = res.getResidents();
    		if(residents != null) {
    			for(UUID id : residents) {
        			Player playerToRemove = Bukkit.getOfflinePlayer(id).getPlayer();
        			for(int i = 0; i < players.size(); i++) {
        				Player p = players.get(i);
        				if(p.equals(playerToRemove)) {
        					players.remove(playerToRemove);
        					i = 0;
        				}
        			}
        		}	
    		}
    		if(players.size() > 8) rows = 3 * 9;
    		if(players.size() > 17) rows = 4 * 9;
    		if(players.size() > 26) rows = 5 * 9;
    		if(players.size() > 35) rows = 6 * 9;
    		
    		Inventory toReturn = Bukkit.createInventory(null, rows, gui_name);
    		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
    		for(int i = 0; i < rows; i++) {
    			if(inv.getItem(i) == null) {
    				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
    			}
    			else continue;
    		}
    		
    		if(players.isEmpty()) {
//    			Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
    			for(int i = 0; i < rows; i++) {
    				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l &l ");
    			}
    			Utils.createItem(inv, Material.ARROW, 1, 10, Utils.normal("&cBack"));
    			toReturn.setContents(inv.getContents());
    			return;
    		}
    		
    		for(int i = 0; i < players.size(); i++) {
				Player player1 = players.get(i);
				Utils.createPlayerHead(inv, player1, 1, i+1, "&a"+player1.getName(), "&7&oClick to add this player");
			}	
			//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
			for(int i = players.size(); i < rows; i++) {
				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " ");
			}
			switch (players.size()) {
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
		
		List<Player> players = new ArrayList<Player>(Bukkit.getOnlinePlayers());
		players.remove(player);
		List<UUID> residents = res.getResidents();
		if(residents != null) {
			for(UUID id : residents) {
				OfflinePlayer playerToRemove = Bukkit.getOfflinePlayer(id);
				for(int i = 0; i < players.size(); i++) {
					Player p = players.get(i);
					if(p.equals(playerToRemove)) {
						players.remove(playerToRemove);
						i = 0;
					}
				}
			}	
		}
		if(players.size() > 8) rows = 3 * 9;
		if(players.size() > 17) rows = 4 * 9;
		if(players.size() > 26) rows = 5 * 9;
		if(players.size() > 35) rows = 6 * 9;
		
		Inventory toReturn = Bukkit.createInventory(null, rows, gui_name);
		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
		for(int i = 0; i < rows; i++) {
			if(inv.getItem(i) == null) {
				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
			}
			else continue;
		}
		
		if(players.isEmpty()) {
//			Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
			for(int i = 0; i < rows; i++) {
				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l &l ");
			}
			Utils.createItem(inv, Material.ARROW, 1, 10, Utils.normal("&cBack"));
			toReturn.setContents(inv.getContents());
			return toReturn;
		}
		
		for(int i = 0; i < players.size(); i++) {
			Player player1 = players.get(i);
			Utils.createPlayerHead(inv, player1, 1, i+1, "&a"+player1.getName(), "&7&oClick to add this player");
		}	
		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
		for(int i = players.size(); i < rows; i++) {
			Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " ");
		}
		switch (players.size()) {
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
			Residence res = ResidenceMainMenu.selectedResidence.get(player.getUniqueId());
			String name = clicked.getItemMeta().getDisplayName();
			name = ChatColor.stripColor(name);
			Player playerToAdd = Bukkit.getPlayer(name);
			Residence.removeResidenceFromList(player.getUniqueId(), res);
			res = Residence.addResident(player, playerToAdd, res);
			Residence.saveResidenceData(player.getUniqueId(), res, false);
			Methods.setResidentPerms(playerToAdd, res);
			ResidenceMainMenu.selectedResidence.put(player.getUniqueId(), res);
			player.sendMessage(Utils.normal(Commands.pluginPrefix+"&e"+name+" &ahas been added to your Residence!"));
			playerToAdd.sendMessage(Utils.normal(Commands.pluginPrefix+"&eYou have been added to &6"+player.getName()+"'s &eResidence!"));
			updateInventory(player, res);
			return;
		}
	}
}
