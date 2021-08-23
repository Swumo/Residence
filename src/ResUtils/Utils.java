package ResUtils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class Utils {

	public static String normal(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked" })
	public static ItemStack createItem(Inventory inv, Material materialId, int amount, int invSlot, String displayName, String... loreString) {
		
		ItemStack item;
		List<String> lore = new ArrayList();
		
		item = new ItemStack(materialId, amount);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.normal(displayName));
		for(String	s : loreString) {
			lore.add(Utils.normal(s));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);	
		
		inv.setItem(invSlot - 1, item);
		return item;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked", "deprecation" })
	public static ItemStack createPlayerHead(Inventory inv, OfflinePlayer player, int amount, int invSlot, String displayName, String... loreString) {
		
		ItemStack item;
		List<String> lore = new ArrayList();
		
		item = new ItemStack(Material.PLAYER_HEAD, amount);
		item.setDurability((short) 3);
		SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
		skullMeta.setOwningPlayer(player);
		
		skullMeta.setDisplayName(Utils.normal(displayName));
		for(String	s : loreString) {
			lore.add(Utils.normal(s));
		}
		skullMeta.setLore(lore);
		item.setItemMeta(skullMeta);
		
		inv.setItem(invSlot - 1, item);
		return item;
	}
	
	
	
	public static List<Vector> edges(Vector v1, Vector v2) {
        List<Vector> result = new ArrayList<Vector>();
        double minX = v1.getX();
        double minY = v1.getY();
        double minZ = v1.getZ();
        double maxX = v2.getX();
        double maxY = v2.getY();
        double maxZ = v2.getZ();
        // X
        if(v1.getX() >= v2.getX() && v1.getY() <= v2.getY() && v1.getZ() <= v2.getZ()) {
        	minX = v2.getX();
        	maxX = v1.getX();
            for (double x =  minX; x <= maxX + 1; x = x + 0.1) {
	            result.add(new Vector(x, v1.getBlockY(), v1.getBlockZ()));
	            result.add(new Vector(x, v1.getBlockY(), v2.getBlockZ()+1));
	            result.add(new Vector(x, v2.getBlockY()+1, v1.getBlockZ()));
	            result.add(new Vector(x, v2.getBlockY()+1, v2.getBlockZ()+1));
	        }
	        for (double y =  minY; y <= maxY + 1; y = y + 0.1) {
                result.add(new Vector(v1.getBlockX()+1, y, v1.getBlockZ()));
                result.add(new Vector(v1.getBlockX()+1, y, v2.getBlockZ()+1));
                result.add(new Vector(v2.getBlockX(), y, v1.getBlockZ()));
                result.add(new Vector(v2.getBlockX(), y, v2.getBlockZ()+1));
	        }
	        for (double z =  minZ; z <= maxZ + 1; z = z + 0.1) {
                result.add(new Vector(v1.getBlockX()+1, v1.getBlockY(), z));
                result.add(new Vector(v1.getBlockX()+1, v2.getBlockY()+1, z));
                result.add(new Vector(v2.getBlockX(), v1.getBlockY(), z));
                result.add(new Vector(v2.getBlockX(), v2.getBlockY()+1, z));
	        }
	        return result;
        }
        // Y
        if(v1.getX() <= v2.getX() && v1.getY() >= v2.getY() && v1.getZ() <= v2.getZ()) {
        	minY = v2.getY();
        	maxY = v1.getY();
           	for (double x =  minX; x <= maxX + 1; x = x + 0.1) {
	            result.add(new Vector(x, v1.getBlockY()+1, v1.getBlockZ()));
	            result.add(new Vector(x, v1.getBlockY()+1, v2.getBlockZ()+1));
	            result.add(new Vector(x, v2.getBlockY(), v1.getBlockZ()));
	            result.add(new Vector(x, v2.getBlockY(), v2.getBlockZ()+1));
	        }
	        for (double y =  minY; y <= maxY + 1; y = y + 0.1) {
                result.add(new Vector(v1.getBlockX(), y, v1.getBlockZ()));
                result.add(new Vector(v1.getBlockX(), y, v2.getBlockZ()+1));
                result.add(new Vector(v2.getBlockX()+1, y, v1.getBlockZ()));
                result.add(new Vector(v2.getBlockX()+1, y, v2.getBlockZ()+1));
	        }
	        for (double z =  minZ; z <= maxZ + 1; z = z + 0.1) {
                result.add(new Vector(v1.getBlockX(), v1.getBlockY()+1, z));
                result.add(new Vector(v1.getBlockX(), v2.getBlockY(), z));
                result.add(new Vector(v2.getBlockX()+1, v1.getBlockY()+1, z));
                result.add(new Vector(v2.getBlockX()+1, v2.getBlockY(), z));
	        }
	        return result;
        }
        // Z
        if(v1.getX() <= v2.getX() && v1.getY() <= v2.getY() && v1.getZ() >= v2.getZ()) {
        	minZ = v2.getZ();
        	maxZ = v1.getZ();
        	for (double x =  minX; x <= maxX + 1; x = x + 0.1) {
	            result.add(new Vector(x, v1.getBlockY(), v1.getBlockZ()+1));
	            result.add(new Vector(x, v1.getBlockY(), v2.getBlockZ()));
	            result.add(new Vector(x, v2.getBlockY()+1, v1.getBlockZ()+1));
	            result.add(new Vector(x, v2.getBlockY()+1, v2.getBlockZ()));
	        }
	        for (double y =  minY; y <= maxY + 1; y = y + 0.1) {
                result.add(new Vector(v1.getBlockX(), y, v1.getBlockZ()+1));
                result.add(new Vector(v1.getBlockX(), y, v2.getBlockZ()));
                result.add(new Vector(v2.getBlockX()+1, y, v1.getBlockZ()+1));
                result.add(new Vector(v2.getBlockX()+1, y, v2.getBlockZ()));
	        }
	        for (double z =  minZ; z <= maxZ + 1; z = z + 0.1) {
                result.add(new Vector(v1.getBlockX(), v1.getBlockY(), z));
                result.add(new Vector(v1.getBlockX(), v2.getBlockY()+1, z));
                result.add(new Vector(v2.getBlockX()+1, v1.getBlockY(), z));
                result.add(new Vector(v2.getBlockX()+1, v2.getBlockY()+1, z));
	        }
	        return result;
        }
        //XY
        if(v1.getX() >= v2.getX() && v1.getY() >= v2.getY() && v1.getZ() <= v2.getZ()) {
        	minX =  v2.getX();
        	maxX =  v1.getX();
        	minY =  v2.getY();
        	maxY =  v1.getY();
        	for (double x =  minX; x <= maxX + 1; x = x + 0.1) {
	            result.add(new Vector(x, v1.getBlockY()+1, v1.getBlockZ()));
	            result.add(new Vector(x, v1.getBlockY()+1, v2.getBlockZ()+1));
	            result.add(new Vector(x, v2.getBlockY(), v1.getBlockZ()));
	            result.add(new Vector(x, v2.getBlockY(), v2.getBlockZ()+1));
	        }
	        for (double y =  minY; y <= maxY + 1; y = y + 0.1) {
                result.add(new Vector(v1.getBlockX()+1, y, v1.getBlockZ()));
                result.add(new Vector(v1.getBlockX()+1, y, v2.getBlockZ()+1));
                result.add(new Vector(v2.getBlockX(), y, v1.getBlockZ()));
                result.add(new Vector(v2.getBlockX(), y, v2.getBlockZ()+1));
	        }
	        for (double z =  minZ; z <= maxZ + 1; z = z + 0.1) {
                result.add(new Vector(v1.getBlockX()+1, v1.getBlockY()+1, z));
                result.add(new Vector(v1.getBlockX()+1, v2.getBlockY(), z));
                result.add(new Vector(v2.getBlockX(), v1.getBlockY()+1, z));
                result.add(new Vector(v2.getBlockX(), v2.getBlockY(), z));
	        }
	        return result;
        }
        //YZ
        if(v1.getX() <= v2.getX() && v1.getY() >= v2.getY() && v1.getZ() >= v2.getZ()) {
        	minY =  v2.getY();
        	maxY =  v1.getY();
        	minZ =  v2.getZ();
        	maxZ =  v1.getZ();
        	for (double x =  minX; x <= maxX + 1; x = x + 0.1) {
	            result.add(new Vector(x, v1.getBlockY()+1, v1.getBlockZ()+1));
	            result.add(new Vector(x, v1.getBlockY()+1, v2.getBlockZ()));
	            result.add(new Vector(x, v2.getBlockY(), v1.getBlockZ()+1));
	            result.add(new Vector(x, v2.getBlockY(), v2.getBlockZ()));
	        }
	        for (double y =  minY; y <= maxY + 1; y = y + 0.1) {
                result.add(new Vector(v1.getBlockX(), y, v1.getBlockZ()+1));
                result.add(new Vector(v1.getBlockX(), y, v2.getBlockZ()));
                result.add(new Vector(v2.getBlockX()+1, y, v1.getBlockZ()+1));
                result.add(new Vector(v2.getBlockX()+1, y, v2.getBlockZ()));
	        }
	        for (double z =  minZ; z <= maxZ + 1; z = z + 0.1) {
                result.add(new Vector(v1.getBlockX(), v1.getBlockY()+1, z));
                result.add(new Vector(v1.getBlockX(), v2.getBlockY(), z));
                result.add(new Vector(v2.getBlockX()+1, v1.getBlockY()+1, z));
                result.add(new Vector(v2.getBlockX()+1, v2.getBlockY(), z));
	        }
	        return result;
        	
        }
        // XZ
        if(v1.getX() >= v2.getX() && v1.getY() <= v2.getY() && v1.getZ() >= v2.getZ()) {
        	minX =  v2.getX();
        	maxX =  v1.getX();
        	minZ =  v2.getZ();
        	maxZ =  v1.getZ();
        	for (double x =  minX; x <= maxX + 1; x = x + 0.1) {
	            result.add(new Vector(x, v1.getBlockY(), v1.getBlockZ()+1));
	            result.add(new Vector(x, v1.getBlockY(), v2.getBlockZ()));
	            result.add(new Vector(x, v2.getBlockY()+1, v1.getBlockZ()+1));
	            result.add(new Vector(x, v2.getBlockY()+1, v2.getBlockZ()));
	        }
	        for (double y =  minY; y <= maxY + 1; y = y + 0.1) {
                result.add(new Vector(v1.getBlockX()+1, y, v1.getBlockZ()+1));
                result.add(new Vector(v1.getBlockX()+1, y, v2.getBlockZ()));
                result.add(new Vector(v2.getBlockX(), y, v1.getBlockZ()+1));
                result.add(new Vector(v2.getBlockX(), y, v2.getBlockZ()));
	        }
	        for (double z =  minZ; z <= maxZ + 1; z = z + 0.1) {
                result.add(new Vector(v1.getBlockX()+1, v1.getBlockY(), z));
                result.add(new Vector(v1.getBlockX()+1, v2.getBlockY()+1, z));
                result.add(new Vector(v2.getBlockX(), v1.getBlockY(), z));
                result.add(new Vector(v2.getBlockX(), v2.getBlockY()+1, z));
	        }
	        return result;
        }
        // XYZ
        if(v1.getX() >= v2.getX() && v1.getY() >= v2.getY() && v1.getZ() >= v2.getZ()) {
        	minX =  v2.getX();
        	minY =  v2.getY();
        	minZ =  v2.getZ();
        	maxX =  v1.getX();
        	maxY =  v1.getY();
        	maxZ =  v1.getZ();
        	for (double x =  minX; x <= maxX + 1; x = x + 0.1) {
	            result.add(new Vector(x, v1.getBlockY()+1, v1.getBlockZ()+1));
	            result.add(new Vector(x, v1.getBlockY()+1, v2.getBlockZ()));
	            result.add(new Vector(x, v2.getBlockY(), v1.getBlockZ()+1));
	            result.add(new Vector(x, v2.getBlockY(), v2.getBlockZ()));
	        }
	        for (double y =  minY; y <= maxY + 1; y = y + 0.1) {
                result.add(new Vector(v1.getBlockX()+1, y, v1.getBlockZ()+1));
                result.add(new Vector(v1.getBlockX()+1, y, v2.getBlockZ()));
                result.add(new Vector(v2.getBlockX(), y, v1.getBlockZ()+1));
                result.add(new Vector(v2.getBlockX(), y, v2.getBlockZ()));
	        }
	        for (double z =  minZ; z <= maxZ + 1; z = z + 0.1) {
                result.add(new Vector(v1.getBlockX()+1, v1.getBlockY()+1, z));
                result.add(new Vector(v1.getBlockX()+1, v2.getBlockY(), z));
                result.add(new Vector(v2.getBlockX(), v1.getBlockY()+1, z));
                result.add(new Vector(v2.getBlockX(), v2.getBlockY(), z));
	        }
	        return result;
        }
        for (double x =  minX; x <= maxX + 1; x = x + 0.1) {
            result.add(new Vector(x, v1.getBlockY(), v1.getBlockZ()));
            result.add(new Vector(x, v1.getBlockY(), v2.getBlockZ()+1));
            result.add(new Vector(x, v2.getBlockY()+1, v1.getBlockZ()));
            result.add(new Vector(x, v2.getBlockY()+1, v2.getBlockZ()+1));
        }
        for (double y =  minY; y <= maxY + 1; y = y + 0.1) {
            result.add(new Vector(v1.getBlockX(), y, v1.getBlockZ()));
            result.add(new Vector(v1.getBlockX(), y, v2.getBlockZ()+1));
            result.add(new Vector(v2.getBlockX()+1, y, v1.getBlockZ()));
            result.add(new Vector(v2.getBlockX()+1, y, v2.getBlockZ()+1));
        }
        for (double z =  minZ; z <= maxZ + 1; z = z + 0.1) {
            result.add(new Vector(v1.getBlockX(), v1.getBlockY(), z));
            result.add(new Vector(v1.getBlockX(), v2.getBlockY()+1, z));
            result.add(new Vector(v2.getBlockX()+1, v1.getBlockY(), z));
            result.add(new Vector(v2.getBlockX()+1, v2.getBlockY()+1, z));
        }
        return result;
	}
}
