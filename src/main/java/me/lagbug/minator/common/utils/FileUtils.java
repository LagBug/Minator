package me.lagbug.minator.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class FileUtils {

	private JavaPlugin plugin;

	private Map<String, YamlConfiguration> files;
	private Map<String, File> filesData;

	public void initiate(JavaPlugin plugin) {
		this.plugin = plugin;
		this.files = new HashMap<>();
		this.filesData = new HashMap<>();
		
		if (!files.isEmpty() || !filesData.isEmpty()) {
			files.clear();
			filesData.clear();
		}

		String[] fileNames = {"config.yml", "data.yml", "lang/en_US.yml", "guis/home.yml", "guis/question.yml", "guis/answer.yml", "guis/loading.yml", "guis/error.yml", "guis/correct.yml", "guis/incorrect.yml"};

		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}
		
		for (String fileName : fileNames) {
			File file = new File(plugin.getDataFolder(), fileName);
			if (!file.isDirectory()) {
				if (!file.exists()) {
					plugin.saveResource(fileName, false);	
				}
				
				files.put(fileName, YamlConfiguration.loadConfiguration(file));
				filesData.put(fileName, file);
			}
		}
	}

	public void reloadFiles() {
		initiate(plugin);
	}

	public YamlConfiguration getFile(String path) {
		return files.get(path);
	}
	
	public File getFileData(String path) {
		return filesData.get(path);
	}
	
	public void saveFile(String path) {
		try {
			getFile(path).save(getFileData(path));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}