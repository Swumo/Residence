package ResUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import ResClass.Residence;
import ResMain.Main;
//Used in block saving/replacing (not used in final plugin)
public class BlockSaving {

	
	@SuppressWarnings("unchecked")
	public static List<String> saveBlocks(Player player, Residence res) {
		File playerFile = new File("plugins/Residence/data", player.getUniqueId()+".yml");
		if(playerFile.exists()) {
			YamlConfiguration c = YamlConfiguration.loadConfiguration(playerFile);
			List<Block> blocks = res.getArea().getBlocks();
			List<String> content = (List<String>) c.getList("Blocks."+res.getName(), new ArrayList<String>());
			content.clear();
			for(Block b : blocks) {
				String world = b.getWorld().getName();
				int x = b.getX();
				int y = b.getY();
				int z = b.getZ();
				Material mat = b.getType();
				String toSave = world + "/" + x + "/" + y + "/" + z + "/" + mat;
				content.add(toSave);
			}
			c.set("Blocks."+res.getName(), content);
			try {
				c.save(playerFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return content;
		}
        YamlConfiguration c = new YamlConfiguration();
        List<Block> blocks = res.getArea().getBlocks();
        List<String> content = new ArrayList<>();
        for(Block b : blocks) {
			String world = b.getWorld().getName();
			int x = b.getX();
			int y = b.getY();
			int z = b.getZ();
			Material mat = b.getType();
			String toSave = world + "/" + x + "/" + y + "/" + z + "/" + mat;
			content.add(toSave);
		}
        c.set("Blocks."+res.getName(), content);
        try {
			c.save(new File("plugins/Residence/data", player.getUniqueId()+".yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        return content;
	}
	
	
    @SuppressWarnings("unchecked")
	public static int checkBlockExists(Player player, Residence res, Block block) throws NullPointerException {
		YamlConfiguration c = YamlConfiguration.loadConfiguration(new File("plugins/Residence/data", player.getUniqueId()+".yml"));
		List<String> content = (List<String>) c.getList("Blocks."+res.getName(), new ArrayList<String>());
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		for(int i = 0; i < content.size(); i++) {
			String a = content.get(i);
			if(!a.contains(x+"/"+y+"/"+z)) continue;
			String[] split = a.split("/");
			World w = Bukkit.getWorld(split[0]);
			int x1 = Integer.valueOf(split[1]);
			int y1 = Integer.valueOf(split[2]);
			int z1 = Integer.valueOf(split[3]);
			Material m = Material.valueOf(split[4]);
			Location l = new Location(w, x1, y1, z1);
			if(w.getBlockAt(l).getType() == m) {
				return -1;
			}
			if(w.getBlockAt(l).getType() != m) {
				return i;
			}
		}
		return -2;
    }
    
    
    @SuppressWarnings("unchecked")
	public static Material getBlock(Player player, Residence res, int index) throws NullPointerException {
		YamlConfiguration c = YamlConfiguration.loadConfiguration(new File("plugins/Residence/data", player.getUniqueId()+".yml"));
		List<String> content = (List<String>) c.getList("Blocks."+res.getName(), new ArrayList<String>());
		for(int i = 0; i < content.size(); i++) {
			if(i == index) {
				String a = content.get(i);
				String[] split = a.split("/");
				Material mat = Material.valueOf(split[4]);
				return mat;
			}
		}
		return null;
    }
   
    
  	public static boolean checkIfLoadExists(Player player, Residence res) throws NullPointerException {
		YamlConfiguration c = YamlConfiguration.loadConfiguration(new File("plugins/Residence/data", player.getUniqueId()+".yml"));
		return c.get("Blocks." + res.getName()) != null;
	}
  	
  	
  	public static void removeEntry(UUID player, String resName) {
  		File file = new File("plugins/Residence/data", player + ".yml");
		YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
		if(c.get("Blocks."+resName) != null) {
			c.set("Blocks."+resName, null);
			try {
				c.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		new BukkitRunnable() {

			@Override
			public void run() {
				if(c.getConfigurationSection("Blocks") == null) return;
				if(c.getConfigurationSection("Blocks").getKeys(false).size() == 0) {
					delete(file);	
				}
			}
			
		}.runTaskLater(Main.getInstance(), 10);
  	}
  	
  	private static void delete(File file) {
  		file.delete();
  	}
    
}
