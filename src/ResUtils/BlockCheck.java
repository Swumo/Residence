package ResUtils;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import ResClass.Residence;
import ResMain.Main;
// Used in block saving/replacing (not used in final plugin)
public class BlockCheck extends BukkitRunnable{
	
	private final List<Block> blocks;
	private final Player player;
	private final Residence res;
	private final HashMap<Location, Material> toReplace;
	
	public BlockCheck(Player player, Residence res, List<Block> blocks, HashMap<Location, Material> toReplace) {
		this.player = player;
		this.res = res;
		this.blocks = blocks;
		this.toReplace = toReplace;
	}

	@Override
	public void run() {
		for(int i = 0; i < blocks.size(); i++) {
			int exists = BlockSaving.checkBlockExists(player, res, blocks.get(i));
			if(exists != -1) {
				Material mat = BlockSaving.getBlock(player, res, exists);
				toReplace.put(blocks.get(i).getLocation(), mat);
			}
		}
		
		new BukkitRunnable() {

			@Override
			public void run() {
				for(Location loc : toReplace.keySet()) {
					player.getWorld().getBlockAt(loc).setType(toReplace.get(loc));
				}
			}
			
		}.runTask(Main.getInstance());
	}
}
