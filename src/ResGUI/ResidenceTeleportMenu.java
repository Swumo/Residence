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
import ResCommands.Commands;
import ResMain.Main;
import ResMethods.Methods;
import ResUtils.Utils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.ChatColor;

public class ResidenceTeleportMenu {
	public static Inventory inv;
	public static String gui_name;
	public static int rows = 2 * 9;

	public static void initialize() {
		gui_name = Utils.normal("&6&lResidence &0Teleport Menu");
		inv = Bukkit.createInventory(null, rows);
	}
	
	public static void updateInventory(final Player player) {
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
    		
    		List<Residence> availableResidences = new ArrayList<Residence>();
    		
    		HashMap<UUID, LinkedList<Residence>> residences = Residence.getResidenceMap();
    		for(UUID owner : residences.keySet()) {
    			if(owner.equals(player.getUniqueId())) continue;
    			LinkedList<Residence> resi = residences.get(owner);
    			for(Residence res : resi) {
    				boolean allow = res.getAllowTeleport();
    				if(res.getResidents() == null) {
    					if(allow == true) {
        					if(!availableResidences.contains(res)) {
        						availableResidences.add(res);	
        					}
        				}
    				}
    				if(res.getResidents() != null) {
        				if(res.getResidents().contains(player.getUniqueId())) {
        					boolean can = Methods.checkIfResidentCan(player, res.getOwner(), "allowTeleport", res);
        					if(can == true) {
        						if(!availableResidences.contains(res)) {
            						availableResidences.add(res);	
            					}	
        					}
        				}	
        			}
    			}
    		}
    		if(!availableResidences.isEmpty() && availableResidences != null) {
    			for(int i = 0; i < availableResidences.size(); i++) {
        			Residence res = availableResidences.get(i);
        			Utils.createItem(inv, Material.GRASS_BLOCK, 1, i+1, "&e"+res.getName(), "&aOwner: &7"+res.getOwner(), "&7&oClick to teleport");
        		}	
    			for(int i = availableResidences.size(); i < rows; i++) {
        			if(inv.getItem(i).getType() != Material.ARROW) Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
        		}
    		} 
    		toReturn.setContents(inv.getContents());
    		
    		
    		if(availableResidences.size() > 8) rows = 3 * 9;
    		if(availableResidences.size() > 17) rows = 4 * 9;
    		if(availableResidences.size() > 26) rows = 5 * 9;
    		if(availableResidences.size() > 35) rows = 6 * 9;
    		
    		if(availableResidences == null || availableResidences.isEmpty()) {
    			Utils.createItem(inv, Material.ARROW, 1, 10, Utils.normal("&cBack"));
        		for(int i = 0; i < rows; i++) {
        			if(inv.getItem(i).getType() != Material.ARROW) Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
        		}
    			toReturn.setContents(inv.getContents());
    		}
    		
    		Utils.createItem(inv, Material.ARROW, 1, 10, Utils.normal("&cBack"));
    		toReturn.setContents(inv.getContents());
    	}
	}
	
	

	public static Inventory GUI(Player player) {
		Inventory toReturn = Bukkit.createInventory(null, rows, gui_name);
		//	Inventory | ItemID | (DataID) | Amount | Slot | Name | Lore
		for(int i = 0; i < rows; i++) {
			if(inv.getItem(i) == null) {
				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
			}
			else continue;
		}
		
		List<Residence> availableResidences = new ArrayList<Residence>();
		
		HashMap<UUID, LinkedList<Residence>> residences = Residence.getResidenceMap();
		for(UUID owner : residences.keySet()) {
			if(owner.equals(player.getUniqueId())) continue;
			LinkedList<Residence> resi = residences.get(owner);
			for(Residence res : resi) {
				boolean allow = res.getAllowTeleport();
				if(res.getResidents() == null) {
					if(allow == true) {
    					if(!availableResidences.contains(res)) {
    						availableResidences.add(res);	
    					}
    				}
				}
				if(res.getResidents() != null) {
    				if(res.getResidents().contains(player.getUniqueId())) {
    					boolean can = Methods.checkIfResidentCan(player, res.getOwner(), "allowTeleport", res);
    					if(can == true) {
    						if(!availableResidences.contains(res)) {
        						availableResidences.add(res);	
        					}	
    					}
    				}	
    			}
			}
		}

		if(!availableResidences.isEmpty() && availableResidences != null) {
			for(int i = 0; i < availableResidences.size(); i++) {
    			Residence res = availableResidences.get(i);
    			Utils.createItem(inv, Material.GRASS_BLOCK, 1, i+1, "&e"+res.getName(), "&aOwner: &7"+res.getOwner(), "&7&oClick to teleport");    		}	
			for(int i = availableResidences.size(); i < rows; i++) {
    			if(inv.getItem(i).getType() != Material.ARROW) Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
    		}
		} 
		toReturn.setContents(inv.getContents());
		
		
		if(availableResidences.size() > 8) rows = 3 * 9;
		if(availableResidences.size() > 17) rows = 4 * 9;
		if(availableResidences.size() > 26) rows = 5 * 9;
		if(availableResidences.size() > 35) rows = 6 * 9;
		
		if(availableResidences == null || availableResidences.isEmpty()) {
			Utils.createItem(inv, Material.ARROW, 1, 10, Utils.normal("&cBack"));
    		for(int i = 0; i < rows; i++) {
    			if(inv.getItem(i).getType() != Material.ARROW) Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
    		}
			toReturn.setContents(inv.getContents());
			return toReturn;
		}
		
		Utils.createItem(inv, Material.ARROW, 1, 10, Utils.normal("&cBack"));
		toReturn.setContents(inv.getContents());
		return toReturn;
		
	}
	
	
	public static void openInv(Player player) {
		Inventory inv = GUI(player);
		player.openInventory(inv);
	}
	
	
	
	public static void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		if(clicked.getType() == Material.BLACK_STAINED_GLASS_PANE) { 
			updateInventory(player);
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&cBack"))) {
			ResidenceMainMenu.openInv(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		else {
			player.closeInventory();
			String unmodname = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
			unmodname = ChatColor.stripColor(unmodname);
			Residence res = Residence.getResidence(unmodname);
			boolean allowTP = res.getAllowTeleport();
			if(allowTP == true) {
				Methods.teleportToHome(res.getOwner(), res.getName(), player);
			}
			else {
				Residence resi = Residence.getResidence(unmodname);
				List<UUID> residents = resi.getResidents();
				if(residents != null) {
					if(residents.contains(player.getUniqueId())) {
						boolean residentValue = false;
						LuckPerms api = Main.getLP();
						User user = api.getUserManager().getUser(player.getName());
						List<Node> nodes = (List<Node>) user.getNodes();
						for(int i = 0; i < nodes.size(); i++) {
							Node node = nodes.get(i);
							if(node.getKey().contains(resi.getName() + ".allowTeleport")) {
								residentValue = node.getValue();
								break;
							}
						}
						if(residentValue == false) {
								player.sendMessage(Utils.normal(Commands.pluginPrefix+"&6"+resi.getOwner()+" &cdoes not allow for guests to teleport!"));
								return;
						}
						if(residentValue == true) {
							Methods.teleportToHome(resi.getOwner(), resi.getName(), player);
							return;
						}
					}
				}
				player.sendMessage(Utils.normal(Commands.pluginPrefix+"&6"+resi.getOwner()+" &cdoes not allow for guests to teleport!"));
				return;
			}
		}
	}
}
