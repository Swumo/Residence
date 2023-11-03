package ResiListeners;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import ResClass.Residence;
import ResCommands.Commands;
import ResGUI.ResidenceEditMenu;
import ResGUI.ResidenceGeneralRulesMenu;
import ResGUI.ResidenceMainMenu;
import ResGUI.ResidenceParticleMenu;
import ResGUI.ResidencePlayerAddMenu;
import ResGUI.ResidencePlayerEditMenu;
import ResGUI.ResidencePlayerPermMenu;
import ResGUI.ResidencePlayerRemoveMenu;
import ResGUI.ResidencePreMainMenu;
import ResGUI.ResidenceRuleMenu;
import ResGUI.ResidenceSettingsMenu;
import ResGUI.ResidenceTeleportMenu;
import ResMain.Main;
import ResMethods.Methods;
import ResUtils.Utils;
import net.md_5.bungee.api.ChatColor;

public class Listeners implements Listener{

	@SuppressWarnings("unused")
	private final Main plugin;
	
	public Listeners(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public static BukkitTask task1;
	public static BukkitTask task2;
	public static HashMap<UUID, List<Vector>> playerEdges = new HashMap<>();
	
	@EventHandler
	public void onWandClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		ItemStack wand = Commands.giveWand(player);
		ItemStack inHand = player.getInventory().getItemInMainHand();
		EquipmentSlot hand = event.getHand();
		if(hand == null) return;
		if(hand.equals(EquipmentSlot.HAND)) {
			if(inHand.equals(wand)) {
				if(player.isSneaking()) {
					if(action.equals(Action.RIGHT_CLICK_AIR)) {
						if(!Commands.block1.containsKey(player.getUniqueId()) && !Commands.block2.containsKey(player.getUniqueId())) {
							player.sendMessage(Utils.normal(Commands.pluginPrefix +"&cYou do not have any positions selected!"));
							return;
						}
						Commands.block1.remove(player.getUniqueId());
						Commands.block2.remove(player.getUniqueId());
						Commands.block1LeftClicked.remove(player.getUniqueId());
						Commands.block2RightClicked.remove(player.getUniqueId());
						Commands.selectedBlock.remove(player.getUniqueId());
						if(task1 != null) {
							task1.cancel();
						}
						if(task2 != null) {
							task2.cancel();	
						}
						Methods.ListenersRemoveGlowingBlock(player, 1);
						Methods.ListenersRemoveGlowingBlock(player, 2);
						playerEdges.remove(player.getUniqueId());
						player.sendMessage(Utils.normal(Commands.pluginPrefix +"&aSelection removed!"));
						return;
					}
					if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
						event.setCancelled(true);
						return;
					}
					if(action.equals(Action.LEFT_CLICK_BLOCK)) {
						event.setCancelled(true);
						return;
					}
				}
				if(action.equals(Action.LEFT_CLICK_BLOCK)) {
					Block block = event.getClickedBlock();
					Location loc = block.getLocation();
					Residence res = Residence.getResidence(loc);
					if(res != null) {
						if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
							event.setCancelled(true);
							player.sendMessage(Utils.normal(Commands.pluginPrefix+"&cYou cannot select a block which is in another claim!"));
							return;
						}	
					}
					Commands.block1.put(player.getUniqueId(), block);
					Commands.selectedBlock.put(player.getUniqueId(), block);
					Commands.block1LeftClicked.put(player.getUniqueId(), true);
					player.sendMessage(Utils.normal(Commands.pluginPrefix+"&aPosition 1 selected!"));
					Methods.ListenersRemoveGlowingBlock(player, 1);
					Methods.ListenersSpawnGlowingBlock(player, loc, 1);
					if(Commands.block2.get(player.getUniqueId()) != null) {
						Location b1Loc = Commands.block1.get(player.getUniqueId()).getLocation();
						Location b2Loc = Commands.block2.get(player.getUniqueId()).getLocation();
						List<Vector> edges = Utils.edges(b1Loc.toVector(), b2Loc.toVector());
						playerEdges.put(player.getUniqueId(), edges);
					}
					event.setCancelled(true);
					return;
				}
				if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
					Block block = event.getClickedBlock();
					Location loc = block.getLocation();
					Residence res = Residence.getResidence(loc);
					if(res != null) {
						if(res.blockInResidence(block) == true && res.isOwner(player) == false) {
							event.setCancelled(true);
							player.sendMessage(Utils.normal(Commands.pluginPrefix+"&cYou cannot select a block which is in another claim!"));
							return;
						}	
					}
					Commands.block2.put(player.getUniqueId(), block);
					Commands.selectedBlock.put(player.getUniqueId(), block);
					Commands.block2RightClicked.put(player.getUniqueId(), true);
					player.sendMessage(Utils.normal(Commands.pluginPrefix+"&aPosition 2 selected!"));
					Methods.ListenersRemoveGlowingBlock(player, 2);
					Methods.ListenersSpawnGlowingBlock(player, loc, 2);
					if(Commands.block1.get(player.getUniqueId()) != null) {
						Location b1Loc = Commands.block1.get(player.getUniqueId()).getLocation();
						Location b2Loc = Commands.block2.get(player.getUniqueId()).getLocation();
						List<Vector> edges = Utils.edges(b1Loc.toVector(), b2Loc.toVector());
						playerEdges.put(player.getUniqueId(), edges);
					}
					return;
				}
			}	
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	private void onGUIClick(InventoryClickEvent E) {
		String title = E.getView().getTitle();
		if(title.equals(ResidencePlayerEditMenu.gui_name) 
				|| title.equals(ResidencePlayerPermMenu.gui_name) 
				|| title.equals(ResidenceParticleMenu.gui_name) 
				|| title.equals(ResidencePlayerRemoveMenu.gui_name) 
				|| title.equals(ResidencePlayerAddMenu.gui_name) 
				|| title.equals(ResidencePreMainMenu.gui_name) 
				|| title.equals(ResidenceEditMenu.gui_name) 
				|| title.equals(ResidenceSettingsMenu.gui_name) 
				|| title.equals(ResidenceTeleportMenu.gui_name) 
				|| title.equals(ResidenceRuleMenu.gui_name) 
				|| title.equals(ResidenceMainMenu.gui_name)
				|| title.equals(ResidenceGeneralRulesMenu.gui_name)) {
			if(E.getCurrentItem() == null) {
				return;
			}
			if(E.getCurrentItem().getType() == Material.AIR ) {
				return;
			}
			if(title.equals(ResidenceRuleMenu.gui_name)) {
				E.setCancelled(true);
				ResidenceRuleMenu.clicked((Player) E.getWhoClicked(), E.getSlot(), E.getCurrentItem(), E.getInventory());
				return;
			}
			if(title.equals(ResidenceParticleMenu.gui_name)) {
				E.setCancelled(true);
				ResidenceParticleMenu.clicked((Player) E.getWhoClicked(), E.getSlot(), E.getCurrentItem(), E.getInventory());
				return;
			}
			if(title.equals(ResidencePreMainMenu.gui_name)) {
				E.setCancelled(true);
				ResidencePreMainMenu.clicked((Player) E.getWhoClicked(), E.getSlot(), E.getCurrentItem(), E.getInventory());
				return;
			}
			if(title.equals(ResidenceMainMenu.gui_name)) {
				E.setCancelled(true);
				ResidenceMainMenu.clicked((Player) E.getWhoClicked(), E.getSlot(), E.getCurrentItem(), E.getInventory());
				return;
			}
			if(title.equals(ResidenceEditMenu.gui_name)) {
				E.setCancelled(true);
				ResidenceEditMenu.clicked((Player) E.getWhoClicked(), E.getSlot(), E.getCurrentItem(), E.getInventory());
				return;
			}
			if(title.equals(ResidencePlayerAddMenu.gui_name)) {
				E.setCancelled(true);
				ResidencePlayerAddMenu.clicked((Player) E.getWhoClicked(), E.getSlot(), E.getCurrentItem(), E.getInventory());
				return;
			}
			if(title.equals(ResidencePlayerRemoveMenu.gui_name)) {
				E.setCancelled(true);
				ResidencePlayerRemoveMenu.clicked((Player) E.getWhoClicked(), E.getSlot(), E.getCurrentItem(), E.getInventory());
				return;
			}
			if(title.equals(ResidenceTeleportMenu.gui_name)) {
				E.setCancelled(true);
				ResidenceTeleportMenu.clicked((Player) E.getWhoClicked(), E.getSlot(), E.getCurrentItem(), E.getInventory());
				return;
			}
			if(title.equals(ResidencePlayerPermMenu.gui_name)) {
				E.setCancelled(true);
				ResidencePlayerPermMenu.clicked((Player) E.getWhoClicked(), E.getSlot(), E.getCurrentItem(), E.getInventory());
				return;
			}
			if(title.equals(ResidenceSettingsMenu.gui_name)) {
				E.setCancelled(true);
				ResidenceSettingsMenu.clicked((Player) E.getWhoClicked(), E.getSlot(), E.getCurrentItem(), E.getInventory());
				return;
			}
			if(title.equals(ResidenceGeneralRulesMenu.gui_name)) {
				E.setCancelled(true);
				ResidenceGeneralRulesMenu.clicked((Player) E.getWhoClicked(), E.getSlot(), E.getCurrentItem(), E.getInventory());
				return;
			}
			if(title.equals(ResidencePlayerEditMenu.gui_name)) {
				E.setCancelled(true);
				String playerToEditName = ChatColor.stripColor(E.getCurrentItem().getItemMeta().getDisplayName());
				if(playerToEditName.contains("Toggle")) {
					playerToEditName = playerToEditName.replace("Toggle ", "");
					playerToEditName = playerToEditName.substring(0, playerToEditName.indexOf(" "));
					OfflinePlayer editing = Bukkit.getOfflinePlayer(playerToEditName);
					ResidencePlayerEditMenu.clicked((Player) E.getWhoClicked(), editing, E.getSlot(), E.getCurrentItem(), E.getInventory());
					return;
				}
				if(playerToEditName.isBlank()) {
					OfflinePlayer editing = null;
					ResidencePlayerEditMenu.clicked((Player) E.getWhoClicked(), editing, E.getSlot(), E.getCurrentItem(), E.getInventory());
					return;
				}
				OfflinePlayer editing = Bukkit.getOfflinePlayer(playerToEditName);
				ResidencePlayerEditMenu.clicked((Player) E.getWhoClicked(), editing, E.getSlot(), E.getCurrentItem(), E.getInventory());
				return;	
			}
		}
	}
}
