package ResClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import ResMain.Main;
import ResMethods.Methods;
import ResUtils.ConfigWrapper;
import ResUtils.Cuboid;
import ResUtils.Cuboid.CuboidDirection;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.MetaNode;
@SerializableAs("Residence")
public class Residence {
	
	private Cuboid Area;
	private final String Owner;
	private List<UUID> Residents;
	private String Name;
	private Location Center;
	private String greetingMessage, farewellMessage;
	private boolean allowBlockPlacing, allowBlockBreaking, allowBlockInteraction, allowEntityInteraction, 
		allowDamageEntities, allowVehicleDestroy, allowTNTPlacing, allowTeleport, allowEntering, allowCreeperGriefing,
		allowEndermanGriefing, notifyOwn, notifyOther;
	private float yaw, pitch;
	
	
	private static final HashMap<UUID, LinkedList<Residence>> MasterList = new HashMap<>();
	private static final HashMap<UUID, LinkedList<Cuboid>> ExtendedAreas = new HashMap<>();
	private static final HashMap<UUID, LinkedList<HashMap<String, Cuboid>>> directions = new HashMap<>();
	public static List<String> allRules = Arrays.asList("allowBlockPlacing", "allowBlockBreaking", "allowBlockInteraction",
			"allowEntityInteraction", "allowDamageEntities", "allowVehicleDestroy", "allowTNTPlacing", "allowTeleport",
			"allowEntering", "allowCreeperGriefing", "allowEndermanGriefing");
	
	
	public Residence(Cuboid area, String owner) {
		this.Area = area;
		this.Owner = owner;
		this.Residents = null;
		if(MasterList.get(Bukkit.getPlayer(owner).getUniqueId()) == null) {
			this.Name = owner + "'s Residence1";
		}
		else {
			int size = MasterList.get(Bukkit.getPlayer(owner).getUniqueId()).size();
			this.Name = owner + "'s Residence"+(size+1);
		}
		this.Center = area.getCenter();
		this.greetingMessage = Main.greetingMessage;
		this.farewellMessage = Main.farewellMessage;
		this.allowBlockPlacing = false;
		this.allowBlockBreaking = false;
		this.allowBlockInteraction = false;
		this.allowEntityInteraction = false;
		this.allowDamageEntities = false;
		this.allowVehicleDestroy = false;
		this.allowTNTPlacing = false;
		this.allowTeleport = false;
		this.allowEntering = true;
		this.allowCreeperGriefing = true;
		this.allowEndermanGriefing = true;
		this.notifyOwn = true;
		this.notifyOther = true;
		this.yaw = area.getCenter().getYaw();
		this.pitch = area.getCenter().getPitch();
	}
	
	public Residence(Cuboid area, String owner, List<UUID> residents, 
			String name, Location center, String greetingMessage, 
			String farewellMessage, boolean allowBlockPlacing, boolean allowBlockBreaking, 
			boolean allowBlockInteraction, boolean allowEntityInteraction, boolean allowDamageEntities, 
			boolean allowVehicleDestroy, boolean allowTNTPlacing, boolean allowTeleport, 
			boolean allowEntering, boolean allowCreeperGriefing, boolean allowEndermanGriefing,
			boolean notifyOwn, boolean notifyOther, float yaw, float pitch) {
		this.Area = area;
		this.Owner = owner;
		this.Residents = residents;
		this.Name = name;
		this.Center = center;
		this.greetingMessage = greetingMessage;
		this.farewellMessage = farewellMessage;
		this.allowBlockPlacing = allowBlockPlacing;
		this.allowBlockBreaking = allowBlockBreaking;
		this.allowBlockInteraction = allowBlockInteraction;
		this.allowEntityInteraction = allowEntityInteraction;
		this.allowDamageEntities = allowDamageEntities;
		this.allowVehicleDestroy = allowVehicleDestroy;
		this.allowTNTPlacing = allowTNTPlacing;
		this.allowTeleport = allowTeleport;
		this.allowEntering = allowEntering;
		this.allowCreeperGriefing = allowCreeperGriefing;
		this.allowEndermanGriefing = allowEndermanGriefing;
		this.notifyOwn = notifyOwn;
		this.notifyOther = notifyOther;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	/**
	 * Get Residence area
	 * @return Cuboid
	 */
	public Cuboid getArea() {
		return this.Area;
	}
	
	/**
	 * Get Residence owner
	 * @return String
	 */
	public String getOwner() {
		return this.Owner;
	}
	
	/**
	 * Get Residence residents
	 * @return List
	 */
	public List<UUID> getResidents() {
		return this.Residents;
	}
	
	/**
	 * Get Residence name
	 * @return String
	 */
	public String getName() {
		return this.Name;
	}
	
	/**
	 * Get Residence center
	 * @return Location
	 */
	public Location getCenter() {
		return this.Center;
	}
	/**
	 * Get Residence greeting message
	 * @return
	 */
	public String getGreetingMessage() {
		return this.greetingMessage;
	}
	/**
	 * Get Residence farewell message
	 * @return
	 */
	public String getFarewellMessage() {
		return this.farewellMessage;
	}
	/*
	 * Rule getting
	 */
	public boolean getAllowBlockPlace() {
		return this.allowBlockPlacing;
	}
	public boolean getAllowBlockBreak() {
		return this.allowBlockBreaking;
	}
	public boolean getAllowBlockInteraction() {
		return this.allowBlockInteraction;
	}
	public boolean getAllowEntityInteraction() {
		return this.allowEntityInteraction;
	}
	public boolean getAllowDamageEntities() {
		return this.allowDamageEntities;
	}
	public boolean getAllowVehicleDestroy() {
		return this.allowVehicleDestroy;
	}
	public boolean getAllowTNTPlacing() {
		return this.allowTNTPlacing;
	}
	public boolean getAllowTeleport() {
		return this.allowTeleport;
	}
	public boolean getAllowEntering() {
		return this.allowEntering;
	}
	public boolean getCreeperGriefing() {
		return this.allowCreeperGriefing;
	}
	public boolean getEndermanGriefing() {
		return this.allowEndermanGriefing;
	}
	public boolean getNotifyOwn() {
		return this.notifyOwn;
	}
	public boolean getNotifyOther() {
		return this.notifyOther;
	}
	/*
	 * Yaw & Pitch getting
	 */
	public float getYaw() {
		return this.yaw;
	}
	public float getPitch() {
		return this.pitch;
	}
	
	/*
	 * Resident setting
	 */
	public void setResidents(List<UUID> residents) {
		this.Residents = residents;
	}
	
	/*
	 * Area setting
	 */
	public void setArea(Cuboid area) {
		this.Area = area;
	}
	/*
	 * Center setting
	 */
	public void setCenter(Location value) {
		this.Center = value;
	}
	/*
	 * Name setting
	 */
	public void setName(String value) {
		this.Name = value;
	}
	
	/**
	 * Set Residence greeting message
	 * @return
	 */
	public void setGreetingMessage(String message) {
		this.greetingMessage = message;
	}
	/**
	 * Set Residence farewell message
	 * @return
	 */
	public void setFarewellMessage(String message) {
		this.farewellMessage = message;
	}
	/*
	 * Rule setting
	 */
	public void setAllowBlockPlace(boolean value) {
		this.allowBlockPlacing = value;
	}
	public void setAllowBlockBreak(boolean value) {
		this.allowBlockBreaking = value;
	}
	public void setAllowBlockInteraction(boolean value) {
		this.allowBlockInteraction = value;
	}
	public void setAllowEntityInteraction(boolean value) {
		this.allowEntityInteraction = value;
	}
	public void setAllowDamageEntities(boolean value) {
		this.allowDamageEntities = value;
	}
	public void setAllowVehicleDestroy(boolean value) {
		this.allowVehicleDestroy = value;
	}
	public void setAllowTNTPlacing(boolean value) {
		this.allowTNTPlacing = value;
	}
	public void setAllowTeleport(boolean value) {
		this.allowTeleport = value;
	}
	public void setAllowEntering(boolean value) {
		this.allowEntering = value;
	}
	public void setAllowCreeperGriefing(boolean value) {
		this.allowCreeperGriefing = value;
	}
	public void setAllowEndermanGriefing(boolean value) {
		this.allowEndermanGriefing = value;
	}
	public void setNotifyOwn(boolean value) {
		this.notifyOwn = value;
	}
	public void setNotifyOther(boolean value) {
		this.notifyOther = value;
	}
	/*
	 * Yaw & Pitch setting
	 */
	public void setYaw(float value) {
		this.yaw = value;
	}
	public void setPitch(float value) {
		this.pitch = value;
	}
	
	/**
	 * Get master residence map
	 * @return
	 */
	public static HashMap<UUID, LinkedList<Residence>> getResidenceMap(){
		return MasterList;
	}
	
	/**
	 * Get extended areas map
	 * @return
	 */
	public static HashMap<UUID, LinkedList<Cuboid>> getExtendedAreasMap(){
		return ExtendedAreas;
	}
	
	/**
	 * Get direction map
	 * @return
	 */
	public static HashMap<UUID, LinkedList<HashMap<String, Cuboid>>> getDirections(){
		return directions;
	}
	 /**
	  * Clear master residence map
	  */
	public static void clearMap() {
		MasterList.clear();
		return;
	}
	
	/*
	 * Get map of all rules and values 
	 */
	public HashMap<String, Boolean> getAllRules(){
		HashMap<String, Boolean> a = new HashMap<>();
		a.put("allowBlockPlacing", this.allowBlockPlacing);
		a.put("allowBlockBreaking", this.allowBlockBreaking);
		a.put("allowBlockInteraction", this.allowBlockInteraction);
		a.put("allowEntityInteraction", this.allowEntityInteraction);
		a.put("allowDamageEntities", this.allowDamageEntities);
		a.put("allowVehicleDestroy", this.allowVehicleDestroy);
		a.put("allowTNTPlacing", this.allowTNTPlacing);
		a.put("allowTeleport", this.allowTeleport);
		a.put("allowEntering", this.allowEntering);
		a.put("allowCreeperGriefing", this.allowCreeperGriefing);
		a.put("allowEndermanGriefing", this.allowEndermanGriefing);
		return a;
	}
	
	/*
	 * Methods
	 */
	
	/**
	 * Delete all player residences
	 * @param player - Player
	 */
	public void deleteAll(Player player) {
		MasterList.remove(player.getUniqueId());
		ExtendedAreas.remove(player.getUniqueId());
	}
	
	/**
	 * Delete specified residence
	 * @param player - UUID
	 * @param res - Residence
	 */
	public static void delete(UUID player, Residence res) {
		LinkedList<Residence> newRes = new LinkedList<>();
		LinkedList<Residence> oldRes = MasterList.get(player);
		for(Residence resi : oldRes) {
			if(!res.getName().equalsIgnoreCase(resi.getName())) {
				newRes.add(resi);
			}
		}
		if(newRes.isEmpty() == false) {
			MasterList.put(player, newRes);	
		}
		else {
			MasterList.remove(player);
		}
	}
	
	/**
	 * Check if player is in a residence
	 * @param player - Specified player
	 * @return True or false
	 */
	public boolean playerInResidence(Player player) {
		Location pLoc = player.getLocation();
		return Area.contains(pLoc);
	}
	
	/**
	 * Check if entity is in a residence
	 * @param entity - Specified entity
	 * @return True or false
	 */
	public boolean entityInResidence(Entity entity) {
		Location loc = entity.getLocation();
		return Area.contains(loc);
	}
	
	/**
	 * Get Residence from location
	 * @param loc - Location
	 * @return Residence or null if there is no residence
	 */
	public static Residence getResidence(Location loc) {
		for(UUID id : MasterList.keySet()) {
			for(Residence res : MasterList.get(id)) {
				if(!res.getArea().contains(loc)) {
					continue;
				}
				if(res.getArea().contains(loc)) {
					return res;
				}
			}
		}
		return null;
	}
	
	/**
	 * Get Residence from Residence name
	 * @param name - Name of the Residence
	 * @return Residence or null if there is no residence
	 */
	public static Residence getResidence(String name) {
		for(UUID id : MasterList.keySet()) {
			for(Residence res : MasterList.get(id)) {
				if(res.getName().contains(name)) {
					return res;
				}
				else {
					continue;
				}
			}
		}
		return null;
	}
	
	/**
	 * Get player residence list from player object
	 * @param player - Player
	 * @return Residence list
	 */
	public static LinkedList<Residence> getResidences(Player player) {
		for(UUID owner : MasterList.keySet()) {
			if(owner.equals(player.getUniqueId())) {
				LinkedList<Residence> res = MasterList.get(owner);
				return res;
			}
		}
		return null;
	}
	
	/**
	 * Get player residence from player UUID
	 * @param id - Specified player UUID
	 * @return Residence list
	 */
	public static LinkedList<Residence> getResidences(UUID id) {
		for(UUID owner : MasterList.keySet()) {
			if(owner.equals(id)) {
				LinkedList<Residence> res = MasterList.get(owner);
				return res;
			}
		}
		return null;
	}
	
	/**
	 * Get specific Residence
	 * @param id - UUID of the owner
	 * @param resName - Name of the residence
	 * @return Residence
	 */
	public static Residence getResidence(UUID id, String resName) {
		for(UUID owner : MasterList.keySet()) {
			if(owner.equals(id)) {
				for(Residence r : MasterList.get(owner)) {
					if(r.getName().equalsIgnoreCase(resName)) {
						return r;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Get specific Residence
	 * @param player - Owner of the Residence
	 * @param resName - Residence name
	 * @return Residence
	 */
	public static Residence getResidence(Player player, String resName) {
		for(UUID owner : MasterList.keySet()) {
			if(owner.equals(player.getUniqueId())) {
				for(Residence r : MasterList.get(owner)) {
					if(r.getName().equalsIgnoreCase(resName)) {
						return r;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Check if a block is in the residence
	 * 
	 * @return True or false
	 */
	public boolean blockInResidence(Block block) {
		return Area.contains(block);
	}
	
	/**
	 * Return blocks that are in a residence in specific chunks
	 * @param chunks - Chunk list
	 * @param res - Residence
	 * @return Block list
	 */
	public static List<Block> getBlocksFromChunks(List<Chunk> chunks, Residence res){
		List<Block> resBlocks = res.getArea().getBlocks();
		for(Chunk chunk : chunks) {
			resBlocks.removeIf(block -> !block.getChunk().equals(chunk));
		}
		return resBlocks;
	}
	
	/**
	 * Check if specified player is the owner of the residence
	 * 
	 * @return True or false
	 */
	public boolean isOwner(Player player) {
		String pName = player.getName();
		return pName.equalsIgnoreCase(Owner);
	}
	
	
	/**
	 * Check if there is already a Residence with the given name
	 * @param id - Owner UUID
	 * @param name - String
	 * @return Boolean
	 */
	public static boolean hasName(UUID id, String name) {
		LinkedList<Residence> residences = MasterList.get(id);
		for(Residence res : residences) {
			if(res.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add a resident to a residence
	 * @param owner - Player who owns the Residence
	 * @param addingPlayer - Player who is getting added
	 * @param res - Residence
	 * @return Updated Residence
	 */
	public static Residence addResident(Player owner, Player addingPlayer, Residence res) {
		int index = 0;
		LinkedList<Residence> residences = Residence.getResidences(owner);
		LinkedList<Residence> newResidences = new LinkedList<>();
		if(residences != null) {
			for(int i = 0; i < residences.size(); i++) {
				Residence r = residences.get(i);
				if(r.getName().equalsIgnoreCase(res.getName())) {
					index = i;
					continue;
				}
				newResidences.add(r);
			}	
		}
		List<UUID> residents = res.getResidents();
		if(residents == null || residents.isEmpty()) {
			List<UUID> newRes = new ArrayList<>();
			newRes.add(addingPlayer.getUniqueId());
			res.setResidents(newRes);
			newResidences.add(index, res);
			MasterList.put(owner.getUniqueId(), newResidences);
			return res;
		}
		residents.add(addingPlayer.getUniqueId());
		res.setResidents(residents);
		newResidences.add(index, res);
		MasterList.put(owner.getUniqueId(), newResidences);
		return res;
	}
	
	/**
	 * Remove a resident from a residence
	 * @param owner - Player who owns the Residence
	 * @param removingPlayer - Player who is getting removed
	 * @param res - Residence
	 * @return Updated Residence
	 */
	public static Residence removeResident(Player owner, Player removingPlayer, Residence res) {
		int index = 0;
		LinkedList<Residence> residences = Residence.getResidences(owner);
		LinkedList<Residence> newResidences = new LinkedList<>();
		if(residences != null) {
			for(int i = 0; i < residences.size(); i++) {
				Residence r = residences.get(i);
				if(r.getName().equalsIgnoreCase(res.getName())) {
					index = i;
					continue;
				}
				newResidences.add(r);
			}	
		}
		List<UUID> residents = res.getResidents();
		residents.remove(removingPlayer.getUniqueId());
		if(residents == null || residents.isEmpty()) {
			List<UUID> newRes = null;
			res.setResidents(newRes);
			newResidences.add(index, res);
			MasterList.put(owner.getUniqueId(), newResidences);
			return res;
		}
		res.setResidents(residents);
		newResidences.add(index, res);
		MasterList.put(owner.getUniqueId(), newResidences);
		return res;
	}
	
	/**
	 * Remove a resident from a residence
	 * @param owner - Player who owns the Residence
	 * @param removingPlayer - Player who is getting removed
	 * @param res - Residence
	 * @return Updated Residence
	 */
	public static Residence removeResident(Player owner, OfflinePlayer removingPlayer, Residence res) {
		int index = 0;
		LinkedList<Residence> residences = Residence.getResidences(owner);
		LinkedList<Residence> newResidences = new LinkedList<>();
		if(residences != null) {
			for(int i = 0; i < residences.size(); i++) {
				Residence r = residences.get(i);
				if(r.getName().equalsIgnoreCase(res.getName())) {
					index = i;
					continue;
				}
				newResidences.add(r);
			}	
		}
		List<UUID> residents = res.getResidents();
		residents.remove(removingPlayer.getUniqueId());
		if(residents == null || residents.isEmpty()) {
			List<UUID> newRes = null;
			res.setResidents(newRes);
			newResidences.add(index, res);
			MasterList.put(owner.getUniqueId(), newResidences);
			return res;
		}
		res.setResidents(residents);
		newResidences.add(index, res);
		MasterList.put(owner.getUniqueId(), newResidences);
		return res;
	}
	

	/**
	 * Save player residence
	 * @param player - Player
	 * @param r - Residence
	 */
	public static void save(UUID player, Residence r) {
		LuckPerms api = Main.getLP();
		boolean color = false;
		boolean defArea = false;
		boolean hasResidences = false;
		int index = -1;
		LinkedList<Residence> resi = Residence.getResidences(player);
		LinkedList<HashMap<String, Cuboid>> extended = new LinkedList<>();
		LinkedList<Cuboid> tntAreas = new LinkedList<>();
		if(resi != null) {
			hasResidences = true;
			for(int i = 0; i < resi.size(); i++) {
				Residence res = resi.get(i);
				if(res.getName().equalsIgnoreCase(r.getName())) {
					index = i;
					continue;
				}
				Cuboid area = res.getArea();
				HashMap<String, Cuboid> dirs = dirs(area);
				if(i > extended.size()) {
					i = extended.size();
				}
				extended.add(i, dirs);
				Cuboid tntArea = Methods.expandArea(area, 10);
				if(i > tntAreas.size()) {
					i = tntAreas.size();
				}
				tntAreas.add(i, tntArea);
			}	
		}
		if(resi == null) {
			resi = new LinkedList<>();
			resi.add(index+1, r);
			extended.add(index+1, dirs(r.getArea()));
			tntAreas.add(index+1, Methods.expandArea(r.getArea(), 10));
		}
		if(resi != null) {
			if(hasResidences == true) {
				resi.add(r);
				extended.add(dirs(r.getArea()));
				tntAreas.add(Methods.expandArea(r.getArea(), 10));
			}
		}
		MasterList.put(player, resi);
		directions.put(player, extended);
		ExtendedAreas.put(player, tntAreas);
		User user = Methods.getUserFromOfflinePlayer(player);
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			if(n.getKey().contains("meta.residence\\.particlecolour.")) color = true;
			if(n.getKey().contains("meta.residence\\.defaultarea.")) defArea = true;
		}
		if(color == true) {
			return;	
		}
		if(color == false) {
			MetaNode colourPerm = MetaNode.builder("Residence.particlecolour", Main.particleColour).build();
			user.data().add(colourPerm);
			api.getUserManager().saveUser(user);
		}
		if(defArea == false) {
			MetaNode defAreaPerm = MetaNode.builder("Residence.defaultarea", String.valueOf(Main.defaultAreaSize)).build();
			user.data().add(defAreaPerm);
			api.getUserManager().saveUser(user);
		}
		checkDuplicates(player);
	}
	
	/**
	 * Save player Residence at the specified index in the master list
	 * @param player - Player
	 * @param r - Residence
	 * @param index - Index
	 */
	public static void save(UUID player, Residence r, int index) {
		LuckPerms api = Main.getLP();
		boolean color = false;
		boolean defArea = false;
		LinkedList<Residence> resi = Residence.getResidences(player);
		LinkedList<HashMap<String, Cuboid>> extended = new LinkedList<>();
		LinkedList<Cuboid> tntAreas = new LinkedList<>();
		if(resi != null) {
			for(int i = 0; i < resi.size(); i++) {
				Residence res = resi.get(i);
				if(res.getName().equalsIgnoreCase(r.getName())) {
					continue;
				}
				Cuboid area = res.getArea();
				HashMap<String, Cuboid> dirs = dirs(area);
				if(i > extended.size()) {
					i = extended.size();
				}
				extended.add(i, dirs);
				Cuboid tntArea = Methods.expandArea(area, 10);
				if(i > tntAreas.size()) {
					i = tntAreas.size();
				}
				tntAreas.add(i, tntArea);
			}	
		}
		if(resi == null) {
			resi = new LinkedList<>();
			resi.add(0, r);
			extended.add(0, dirs(r.getArea()));
			tntAreas.add(0, Methods.expandArea(r.getArea(), 10));
		}
		if(resi != null) {
			resi.add(index, r);
			extended.add(index, dirs(r.getArea()));
			tntAreas.add(index, Methods.expandArea(r.getArea(), 10));
		}
		MasterList.put(player, resi);
		directions.put(player, extended);
		ExtendedAreas.put(player, tntAreas);
		User user = Methods.getUserFromOfflinePlayer(player);
		List<Node> nodes = (List<Node>) user.getNodes();
		for(int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			if(n.getKey().contains("meta.residence\\.particlecolour.")) color = true;
			if(n.getKey().contains("meta.residence\\.defaultarea.")) defArea = true;
		}
		if(color == true) {
			return;	
		}
		if(color == false) {
			MetaNode colourPerm = MetaNode.builder("Residence.particlecolour", Main.particleColour).build();
			user.data().add(colourPerm);
			api.getUserManager().saveUser(user);
		}
		if(defArea == false) {
			MetaNode defAreaPerm = MetaNode.builder("Residence.defaultarea", String.valueOf(Main.defaultAreaSize)).build();
			user.data().add(defAreaPerm);
			api.getUserManager().saveUser(user);
		}
		checkDuplicates(player);
	}
	/**
	 * Check for any duplicate residences in the master list
	 * @param player - Player
	 */
	private static void checkDuplicates(UUID player) {
		LinkedList<Residence> residences = MasterList.get(player);
		LinkedList<Residence> newResidences = new LinkedList<>();
		for(Residence res : residences) {
			if(!newResidences.contains(res)) {
				newResidences.add(res);
			}
		}
		MasterList.put(player, newResidences);
	}
	/**
	 * Remove the specified residence from the master list
	 * @param player - Player
	 * @param res - Residence
	 * @return Index where the residence was found
	 */
	public static int removeResidenceFromList(UUID player, Residence res) {
		int index = 0;
		LinkedList<Residence> residences = MasterList.get(player);
		LinkedList<Residence> newResidences = new LinkedList<>();
		for(int i = 0; i < residences.size(); i++) {
			Residence resi = residences.get(i);
			if(resi.getName().equalsIgnoreCase(res.getName())) {
				index = i;
				continue;
			}
			if(!newResidences.contains(resi)) {
				newResidences.add(resi);
			}
		}
		MasterList.put(player, newResidences);
		ConfigWrapper data = Main.getResidenceFile();
		FileConfiguration c = data.getConfig();
		c.set("PlayerResidences."+player.toString()+"."+res.getName(), null);
		data.saveConfig();
		return index;
	}
	
	
	/**
	 * Save residence in the config
	 * @param id - Player UUID
	 * @param res - Residence
	 * @param saveList - True/False to save the master list as well
	 */
	public static void saveResidenceData(UUID id, Residence res, boolean saveList) {
		ConfigWrapper data = Main.getResidenceFile();
		FileConfiguration c = data.getConfig();
		String owner = id.toString();
		Map<String, Object> mapped = res.serialize();
		for(String key : mapped.keySet()) {
			c.set("PlayerResidences."+owner+"."+res.getName()+"."+key, mapped.get(key));
		}
		data.saveConfig();
		if(saveList == true) {
			save(id, res);
		}
	}
	
	/**
	 * Save residence in the config
	 * @param id - Player UUID
	 * @param res - Residence
	 * @param saveList - True/False to save the master list as well
	 * @param index - Index where the residence is located in the master list
	 */
	public static void saveResidenceData(UUID id, Residence res, boolean saveList, int index) {
		ConfigWrapper data = Main.getResidenceFile();
		FileConfiguration c = data.getConfig();
		String owner = id.toString();
		Map<String, Object> mapped = res.serialize();
		for(String key : mapped.keySet()) {
			c.set("PlayerResidences."+owner+"."+res.getName()+"."+key, mapped.get(key));
		}
		data.saveConfig();
		if(saveList == true) {
			save(id, res, index);
		}
	}
	
	@Override
	public String toString() {
		if(Residents == null) {
			return Name + "/" + Area.toString() + "/null";
		}
		String toReturn = "";
		toReturn += Name + "/" + Area.toString() + "/";
		for(UUID p : Residents) {
			toReturn += p + "/";
		}
		return toReturn;
	}
	
	
	public Map<String, Object> serialize() {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		StringBuilder residents = new StringBuilder();
		if(this.Residents == null || this.Residents.isEmpty() == true) {
			residents.append("null");
		}
		else {
			if(this.Residents.size() > 1) {
				for(UUID id : this.Residents) {
					residents.append(id.toString()+",");
				}	
			}
			else{
				residents.append(this.Residents.get(0).toString());
			}
		}
		String center = this.Center.getX() + ";" + this.Center.getY() + ";" + this.Center.getZ();
		result.put("Area", this.Area.toString());
		result.put("Owner", this.Owner);
		result.put("Residents", residents.toString());
		result.put("Name", this.Name);
		result.put("Center", center);
		result.put("GreetingMessage", this.greetingMessage);
		result.put("FarewellMessage", this.farewellMessage);
		result.put("allowBlockPlacing", this.allowBlockPlacing);
		result.put("allowBlockBreaking", this.allowBlockBreaking);
		result.put("allowBlockInteraction", this.allowBlockInteraction);
		result.put("allowEntityInteraction", this.allowEntityInteraction);
		result.put("allowDamageEntities", this.allowDamageEntities);
		result.put("allowVehicleDestroy", this.allowVehicleDestroy);
		result.put("allowTNTPlacing", this.allowTNTPlacing);
		result.put("allowTeleport", this.allowTeleport);
		result.put("allowEntering", this.allowEntering);
		result.put("allowCreeperGriefing", this.allowCreeperGriefing);
		result.put("allowEndermanGriefing", this.allowEndermanGriefing);
		result.put("notifyOwn", this.notifyOwn);
		result.put("notifyOther", this.notifyOther);
		result.put("yaw", this.yaw);
		result.put("pitch", this.pitch);
		return result;
	}
	
	private static HashMap<String, Cuboid> dirs(Cuboid original){
		Cuboid north = original.getFace(CuboidDirection.North);
		Cuboid west = original.getFace(CuboidDirection.West);
		Cuboid south = original.getFace(CuboidDirection.South);
		Cuboid east = original.getFace(CuboidDirection.East);
		Cuboid up = original.getFace(CuboidDirection.Up);
		Cuboid down = original.getFace(CuboidDirection.Down);
		HashMap<String, Cuboid> a = new HashMap<String, Cuboid>();
		a.put("north", north);
		a.put("west", west);
		a.put("south", south);
		a.put("east", east);
		a.put("up", up);
		a.put("down", down);
		return a;
	}
}
