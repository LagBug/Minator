package me.lagbug.minator.handlers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.lagbug.minator.Minator;
import me.lagbug.minator.utils.GUIHandler;

public class LoadingHandler extends GUIHandler {

	private final Minator plugin = Minator.getPlugin(Minator.class);
	private final YamlConfiguration file = plugin.getFile("guis/loading.yml");

	public LoadingHandler() {
		super.setGUIName(file.getString("title"));
	}

	@Override
	public void execute(InventoryClickEvent e, Player player) {
		/*
		 * Nothing shall be here. The event should just be cancelled so no items can be
		 * moved.
		 */
	}
}
