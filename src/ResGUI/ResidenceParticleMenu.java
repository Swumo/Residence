package ResGUI;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ResCommands.Commands;
import ResMain.Main;
import ResMethods.Methods;
import ResUtils.Utils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.MetaNode;

public class ResidenceParticleMenu {


	public static Inventory inv;
	public static String gui_name;
	public static int rows = 4 * 9;

	public static void initialize() {
		gui_name = Utils.normal("&cColour Selector");
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
    				Utils.createItem(inv, Material.BLACK_STAINED_GLASS_PANE, 1, i+1, " &l  &l ");
    			}
    			else continue;
    		}
    		
    		LuckPerms api = Main.getLP();
    		User user = api.getUserManager().getUser(player.getName());
    		List<Node> nodes = (List<Node>) user.getNodes();
       		for(int i = 0; i < nodes.size(); i++) {
       			Node node = nodes.get(i);
       			if(node.getKey().contains("meta.residence\\.particlecolour.")) {
       				MetaNode mNode = (MetaNode) node;
    				String value = mNode.getMetaValue();
					switch (value) {
						case "Lime":
							Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!", " ", "&a&lSelected");
							Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!");
							Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
							Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
							Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
							Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
							Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
							Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
							Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
							Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
							break;
						case "Aqua":
							Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
							Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!", " ", "&a&lSelected");
							Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
							Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
							Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
							Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
							Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
							Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
							Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
							Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
							break;
						case "Red":
							Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
							Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!" );
							Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!", " ", "&a&lSelected");
							Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
							Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
							Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
							Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
							Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
							Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
							Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
							break;
						case "Pink":
							Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
							Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!" );
							Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
							Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!", " ", "&a&lSelected");
							Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
							Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
							Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
							Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
							Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
							Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
							break;
						case "Yellow":
							Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
							Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!" );
							Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
							Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
							Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!", " ", "&a&lSelected");
							Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
							Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
							Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
							Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
							Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
							break;
						case "Green":
							Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
							Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!" );
							Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
							Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
							Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
							Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!", " ", "&a&lSelected");
							Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
							Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
							Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
							Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
							break;
						case "Cyan":
							Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
							Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!" );
							Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
							Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
							Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
							Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
							Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!", " ", "&a&lSelected");
							Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
							Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
							Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
							break;
						case "White":
							Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
							Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!" );
							Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
							Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
							Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
							Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
							Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
							Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!", " ", "&a&lSelected");
							Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
							Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
							break;
						case "Purple":
							Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
							Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!" );
							Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
							Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
							Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
							Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
							Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
							Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
							Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!", " ", "&a&lSelected");
							Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
							break;
						case "Orange":
							Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
							Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!");
							Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
							Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
							Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
							Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
							Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
							Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
							Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
							Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!", " ", "&a&lSelected");
							break;
    				}
       			}
    		}
       		Utils.createItem(inv, Material.ARROW, 1, 28, "&cBack", "&7&oGo back to Edit menu");
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
		
		LuckPerms api = Main.getLP();
		User user = api.getUserManager().getUser(player.getName());
		List<Node> nodes = (List<Node>) user.getNodes();
   		for(int i = 0; i < nodes.size(); i++) {
   			Node node = nodes.get(i);
   			if(node.getKey().contains("meta.residence\\.particlecolour.")) {
   				MetaNode mNode = (MetaNode) node;
				String value = mNode.getMetaValue();
				switch (value) {
					case "Lime":
						Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!", " ", "&a&lSelected");
						Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!");
						Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
						Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
						Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
						Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
						Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
						Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
						Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
						Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
						break;
					case "Aqua":
						Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
						Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!", " ", "&a&lSelected");
						Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
						Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
						Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
						Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
						Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
						Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
						Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
						Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
						break;
					case "Red":
						Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
						Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!" );
						Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!", " ", "&a&lSelected");
						Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
						Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
						Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
						Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
						Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
						Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
						Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
						break;
					case "Pink":
						Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
						Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!" );
						Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
						Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!", " ", "&a&lSelected");
						Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
						Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
						Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
						Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
						Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
						Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
						break;
					case "Yellow":
						Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
						Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!" );
						Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
						Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
						Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!", " ", "&a&lSelected");
						Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
						Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
						Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
						Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
						Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
						break;
					case "Green":
						Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
						Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!" );
						Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
						Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
						Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
						Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!", " ", "&a&lSelected");
						Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
						Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
						Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
						Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
						break;
					case "Cyan":
						Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
						Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!" );
						Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
						Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
						Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
						Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
						Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!", " ", "&a&lSelected");
						Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
						Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
						Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
						break;
					case "White":
						Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
						Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!" );
						Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
						Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
						Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
						Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
						Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
						Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!", " ", "&a&lSelected");
						Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
						Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
						break;
					case "Purple":
						Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
						Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!" );
						Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
						Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
						Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
						Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
						Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
						Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
						Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!", " ", "&a&lSelected");
						Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!");
						break;
					case "Orange":
						Utils.createItem(inv, Material.LIME_STAINED_GLASS_PANE, 1, 12, "&aLime", "&7Click to set your particle colour to &aLime&7!");
						Utils.createItem(inv, Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, 13, "&bAqua", "&7Click to set your particle colour to &bAqua&7!");
						Utils.createItem(inv, Material.RED_STAINED_GLASS_PANE, 1, 14, "&cRed", "&7Click to set your particle colour to &cRed&7!");
						Utils.createItem(inv, Material.PINK_STAINED_GLASS_PANE, 1, 15, "&dPink", "&7Click to set your particle colour to &dPink&7!");
						Utils.createItem(inv, Material.YELLOW_STAINED_GLASS_PANE, 1, 16, "&eYellow", "&7Click to set your particle colour to &eYellow&7!");
						Utils.createItem(inv, Material.GREEN_STAINED_GLASS_PANE, 1, 21, "&2Green", "&7Click to set your particle colour to &2Green&7!");
						Utils.createItem(inv, Material.CYAN_STAINED_GLASS_PANE, 1, 22, "&3Cyan", "&7Click to set your particle colour to &3Cyan&7!");
						Utils.createItem(inv, Material.WHITE_STAINED_GLASS_PANE, 1, 23, "&fWhite", "&7Click to set your particle colour to &fWhite&7!");
						Utils.createItem(inv, Material.PURPLE_STAINED_GLASS_PANE, 1, 24, "&5Purple", "&7Click to set your particle colour to &5Purple&7!");
						Utils.createItem(inv, Material.ORANGE_STAINED_GLASS_PANE, 1, 25, "&6Orange", "&7Click to set your particle colour to &6Orange&7!", " ", "&a&lSelected");
						break;
				}
   			}
		}
   		Utils.createItem(inv, Material.ARROW, 1, 28, "&cBack", "&7&oGo back to Edit menu");
		
		toReturn.setContents(inv.getContents());
		return toReturn;
	}
	
	
	public static void openInv(Player player) {
		Inventory inv = GUI(player);
		player.openInventory(inv);
	}
	
	
	
	public static void clicked(Player player, int slot, ItemStack clicked, Inventory inv) {
		if(!clicked.getItemMeta().getDisplayName().contains(Utils.normal(" &l  &l "))) { 
			updateInventory(player);
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&aLime"))) {
			Methods.ParticleMenuUpdateColour(player, "Lime");
			player.sendMessage(Utils.normal(Commands.pluginPrefix+"&aColour successfully set!"));
			updateInventory(player);
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&bAqua"))) {
			Methods.ParticleMenuUpdateColour(player, "Aqua");
			player.sendMessage(Utils.normal(Commands.pluginPrefix+"&aColour successfully set!"));
			updateInventory(player);
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&cRed"))) {
			Methods.ParticleMenuUpdateColour(player, "Red");
			player.sendMessage(Utils.normal(Commands.pluginPrefix+"&aColour successfully set!"));
			updateInventory(player);
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&dPink"))) {
			Methods.ParticleMenuUpdateColour(player, "Pink");
			player.sendMessage(Utils.normal(Commands.pluginPrefix+"&aColour successfully set!"));
			updateInventory(player);
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&eYellow"))) {
			Methods.ParticleMenuUpdateColour(player, "Yellow");
			player.sendMessage(Utils.normal(Commands.pluginPrefix+"&aColour successfully set!"));
			updateInventory(player);
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&2Green"))) {
			Methods.ParticleMenuUpdateColour(player, "Green");
			player.sendMessage(Utils.normal(Commands.pluginPrefix+"&aColour successfully set!"));
			updateInventory(player);
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&3Cyan"))) {
			Methods.ParticleMenuUpdateColour(player, "Cyan");
			player.sendMessage(Utils.normal(Commands.pluginPrefix+"&aColour successfully set!"));
			updateInventory(player);
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&fWhite"))) {
			Methods.ParticleMenuUpdateColour(player, "White");
			player.sendMessage(Utils.normal(Commands.pluginPrefix+"&aColour successfully set!"));
			updateInventory(player);
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&5Purple"))) {
			Methods.ParticleMenuUpdateColour(player, "Purple");
			player.sendMessage(Utils.normal(Commands.pluginPrefix+"&aColour successfully set!"));
			updateInventory(player);
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&6Orange"))) {
			Methods.ParticleMenuUpdateColour(player, "Orange");
			player.sendMessage(Utils.normal(Commands.pluginPrefix+"&aColour successfully set!"));
			updateInventory(player);
			return;
		}
		if(clicked.getItemMeta().getDisplayName().contains(Utils.normal("&cBack"))) {
			player.closeInventory();
			ResidenceEditMenu.openInv(player);
			return;
		}
	}
}
