package me.lagbug.minator.handlers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.lagbug.minator.Minator;
import me.lagbug.minator.common.builders.InventoryBuilder;
import me.lagbug.minator.common.utils.Utils;
import me.lagbug.minator.utils.GUIHandler;

public class AnswerHandler extends GUIHandler {

	private final Minator plugin = Minator.getPlugin(Minator.class);
	private final YamlConfiguration file = plugin.getFile("guis/answer.yml");

	public AnswerHandler() {
		super.setGUIName(file.getString("title"));
	}

	@Override
	public void execute(InventoryClickEvent e, Player player) {
		for (String key : file.getConfigurationSection("contents").getKeys(false)) {
			if (Utils.isInteger(key) && e.getSlot() == Integer.parseInt(key) && file.contains("contents." + key + ".action")) {
				String action = file.getString("contents." + key + ".action");
				
				switch (action) {
				case "CORRECT":
					player.openInventory(new InventoryBuilder(getFile("correct")).build());
					break;
				case "INCORRECT":
					player.openInventory(new InventoryBuilder(getFile("incorrect")).build());
					break;
				default:
					player.closeInventory();
				}
				
			}	
		}				
	}
}
