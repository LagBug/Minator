package me.lagbug.minator.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.lagbug.minator.Minator;

public abstract class GUIHandler {

	private final Minator plugin = Minator.getPlugin(Minator.class);
	private String guiName;
	
	public abstract void execute(InventoryClickEvent e, Player player);
	
	protected void setGUIName(String name) {
		this.guiName = name;
	}
	
	public String getGUIName() {
		return guiName;
	}
	
	public YamlConfiguration getFile(String path) {
		return plugin.getFile("guis/" + path + ".yml");
	}
}
