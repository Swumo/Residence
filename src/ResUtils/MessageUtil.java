package ResUtils;

import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

public class MessageUtil {
	
	private TreeMap<UUID, List<String>> messages = new TreeMap<UUID, List<String>>();
	
	public MessageUtil() {
		
	}
	
	public List<String> getMessages(UUID player){
		return this.messages.get(player);
	}
	
	public void putMessage(UUID player, List<String> message) {
		messages.put(player, message);
		return;
	}
	
}
