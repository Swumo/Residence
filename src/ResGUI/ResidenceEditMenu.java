package ResGUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ResCommands.Commands;
import ResUtils.Utils;
import ResiListeners.ResListeners;

public class ResidenceEditMenu {

	public static Inventory inv;
	public static String gui_name;
	public static int rows = 4 * 9;

	public static void initialize() {
		gui_name = Utils.normal("&6&lResidence &0Edit Menu");
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
    		
    		Utils.createItem(inv, Material.PLAYER_HEAD, 1, 10, "&a&lAdd Player", "&7&oAdd a player to your residence");
    		Utils.createItem(inv, Material.PLAYER_HEAD, 1, 12, "&c&lRemove Player", "&7&oRemove a player from your residence");
    		Utils.createItem(inv, Material.REDSTONE_TORCH, 1, 14, "&e&lEdit Guest Rules", "&7&oEdit guest rules");
    		Utils.createItem(inv, Material.GRASS_BLOCK, 1, 16, "&b&lChange area size", "&7&oChange your residence area size");
    		Utils.createItem(inv, Material.NAME_TAG, 1, 20, "&e&lEdit Resident Permissions", "&7&oEdit per Resident permissions");
    		Utils.createItem(inv, Material.TRIPWIRE_HOOK, 1, 26, "&f&lSettings", "&7&oEdit settings");
    		Utils.createItem(inv, Material.ARROW, 1, 28, "&cBack", "&7&oGo back to main menu");
    		Utils.createItem(inv, Material.REDSTONE, 1, 18, "&e&lChange particle colour", "&7&oChange particle colour when selecting area");
    		Utils.createItem(inv, Material.TORCH, 1, 32, "&e&lEdit General Rules", "&7&oEdit general rules");
    		
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
		
		Utils.createItem(inv, Material.PLAYER_HEAD, 1, 10, "&a&lAdd Player", "&7&oAdd a player to your residence");
		Utils.createItem(inv, Material.PLAYER_HEAD, 1, 12, "&c&lRemove Player", "&7&oRemove a player from your residence");
		Utils.createItem(inv, Material.REDSTONE_TORCH, 1, 14, "&e&lEdit Guest Rules", "&7&oEdit guest rules");
		Utils.createItem(inv, Material.GRASS_BLOCK, 1, 16, "&b&lChange area size", "&7&oChange your residence area size");
		Utils.createItem(inv, Material.NAME_TAG, 1, 20, "&e&lEdit Resident Permissions", "&7&oEdit per Resident permissions");
		Utils.createItem(inv, Material.TRIPWIRE_HOOK, 1, 26, "&f&lSettings", "&7&oEdit settings");
		Utils.createItem(inv, Material.ARROW, 1, 28, "&cBack", "&7&oGo back to main menu");
		Utils.createItem(inv, Material.REDSTONE, 1, 18, "&e&lChange particle colour", "&7&oChange particle colour when selecting area");
		Utils.createItem(inv, Material.TORCH, 1, 32, "&e&lEdit General Rules", "&7&oEdit general rules");
		
		toReturn.setContents(inv.getContents());
		return toReturn;
	}
	
	
	public static void openInv(Player player) {
		Inventory inv = GUI(player);
		player.openInventory(inv);
	}
	
	
	
	public static void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal(" &l  &l "))) { 
			updateInventory(player);
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&cBack"))) {
			player.closeInventory();
			ResidenceMainMenu.openInv(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&b&lChange area size"))) {
			if(ResidenceMainMenu.selectedResidence.get(player.getUniqueId()) == null) {
				player.sendMessage(Utils.normal(Commands.pluginPrefix+"&cYou do not have a Residence."));
				return;
			}
			player.closeInventory();
			ResListeners.userInAreaSelection.put(player.getName(), true);
			ResListeners.userInAreaSelectionResidence.put(player.getName(), ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			player.sendMessage(Utils.normal(Commands.pluginPrefix+"&ePlease select a new area for your residence with your Claim Wand. Type &aDone &eonce you're done or &cCancel &eto cancel area selection"));
			return;	
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lEdit Guest Rules"))) {
			if(ResidenceMainMenu.selectedResidence.get(player.getUniqueId()) == null) {
				player.sendMessage(Utils.normal(Commands.pluginPrefix+"&cYou do not have a Residence."));
				return;
			}
			player.closeInventory();
			ResidenceRuleMenu.openInv(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&a&lAdd Player"))) {
			if(ResidenceMainMenu.selectedResidence.get(player.getUniqueId()) == null) {
				player.sendMessage(Utils.normal(Commands.pluginPrefix+"&cYou do not have a Residence."));
				return;
			}
			player.closeInventory();
			ResidencePlayerAddMenu.openInv(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&c&lRemove Player"))) {
			if(ResidenceMainMenu.selectedResidence.get(player.getUniqueId()) == null) {
				player.sendMessage(Utils.normal(Commands.pluginPrefix+"&cYou do not have a Residence."));
				return;
			}
			player.closeInventory();
			ResidencePlayerRemoveMenu.openInv(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lEdit Resident Permissions"))) {
			if(ResidenceMainMenu.selectedResidence.get(player.getUniqueId()) == null) {
				player.sendMessage(Utils.normal(Commands.pluginPrefix+"&cYou do not have a Residence."));
				return;
			}
			if(ResidenceMainMenu.selectedResidence.get(player.getUniqueId()).getResidents() == null) {
				player.sendMessage(Utils.normal(Commands.pluginPrefix+"&cYou do not have any residents."));
				return;
			}
			player.closeInventory();
			ResidencePlayerPermMenu.openInv(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lChange particle colour"))) {
			player.closeInventory();
			ResidenceParticleMenu.openInv(player);
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&f&lSettings"))) {
			if(ResidenceMainMenu.selectedResidence.get(player.getUniqueId()) == null) {
				player.sendMessage(Utils.normal(Commands.pluginPrefix+"&cYou do not have a Residence."));
				return;
			}
			player.closeInventory();
			ResidenceSettingsMenu.openInv(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&e&lEdit General Rules"))) {
			if(ResidenceMainMenu.selectedResidence.get(player.getUniqueId()) == null) {
				player.sendMessage(Utils.normal(Commands.pluginPrefix+"&cYou do not have a Residence."));
				return;
			}
			player.closeInventory();
			ResidenceGeneralRulesMenu.openInv(player, ResidenceMainMenu.selectedResidence.get(player.getUniqueId()));
			return;
		}
	}
}
