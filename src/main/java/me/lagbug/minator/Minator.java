package me.lagbug.minator;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.markozajc.akiwrapper.Akiwrapper;

import me.lagbug.minator.commands.MinatorCommand;
import me.lagbug.minator.common.utils.FileUtils;
import me.lagbug.minator.common.utils.Metrics;
import me.lagbug.minator.common.utils.UpdateChecker;
import me.lagbug.minator.events.InventoryClick;

public class Minator extends JavaPlugin {

	private final ConsoleCommandSender cs = Bukkit.getConsoleSender();

	private FileConfiguration configFile, langFile, dataFile;
	private FileUtils fileUtils = new FileUtils();

	private Map<Player, Akiwrapper> aki = new HashMap<>(); 
	
	@Override
	public void onEnable() {
		fileUtils.initiate(this);
		initiate();
		
		getCommand("minator").setExecutor(new MinatorCommand());
		Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
		
		new Metrics(this);
		
		if (configFile.getBoolean("updateChecker")) {
			new UpdateChecker(this, 71185).schedule(120);
		}
		
		cs.sendMessage("--------------------------------------------------");
		cs.sendMessage(" --> " + getDescription().getName() + " v" + getDescription().getVersion() + " has been enabled successfully.");
		cs.sendMessage(" --> Using FREE version of the plugin. Enjoy!");
		cs.sendMessage("--------------------------------------------------");
	}

	@Override
	public void onDisable() {
		cs.sendMessage("--------------------------------------------------");
		cs.sendMessage(" --> " + getDescription().getName() + " v" + getDescription().getVersion() + " has been disabled successfully.");
		cs.sendMessage("--------------------------------------------------");
	}

	public void initiate() {
		configFile = fileUtils.getFile("config.yml");
		langFile = fileUtils.getFile("lang/en_US.yml");
		dataFile = fileUtils.getFile("data.yml");
	}

	public String getMessage(String path) {
		return ChatColor.translateAlternateColorCodes('&', langFile.getString(path).replace("%prefix%", configFile.getString("prefix")));
	}
	
	public FileConfiguration getConfigFile() {
		return configFile;
	}

	public FileConfiguration getDataFile() {
		return dataFile;
	}
	
	public YamlConfiguration getFile(String path) {
		return fileUtils.getFile(path);
	}

	public void saveConfigFile() {
		fileUtils.saveFile("config.yml");
	}

	public void saveDataFile() {
		fileUtils.saveFile("data.yml");
	}

	public void reloadFiles() {
		fileUtils.reloadFiles();
	}
	
	public Map<Player, Akiwrapper> getAkiMap() {
		return aki;
	}
}