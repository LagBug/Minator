package me.lagbug.minator.handlers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.lagbug.minator.Minator;
import me.lagbug.minator.utils.GUIHandler;

public class IncorrectHandler extends GUIHandler {

	private final Minator plugin = Minator.getPlugin(Minator.class);
	private final YamlConfiguration file = plugin.getFile("guis/incorrect.yml");

	public IncorrectHandler() {
		super.setGUIName(file.getString("title"));
	}

	@Override
	public void execute(InventoryClickEvent e, Player player) {
		player.closeInventory();
	}
}
