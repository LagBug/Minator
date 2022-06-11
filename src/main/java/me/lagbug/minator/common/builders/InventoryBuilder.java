package me.lagbug.minator.common.builders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.lagbug.minator.Minator;

public class InventoryBuilder {

	private YamlConfiguration config = null;

	private Map<String, String> toReplace = new HashMap<>();
	private Map<Integer, ItemStack> toAdd = new HashMap<>();
	private Inventory inventory;

	private int slots;
	private String title;

	public InventoryBuilder(YamlConfiguration config) {
		this.config = config;
	}

	public InventoryBuilder() {
	}

	public InventoryBuilder setSlots(int slots) {
		this.slots = slots;
		return this;
	}

	public InventoryBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public InventoryBuilder addItem(ItemStack item, int slot) {
		toAdd.put(slot, item);
		return this;
	}

	public ItemStack[] getItems() {
		return toAdd.values().toArray(new ItemStack[toAdd.size()]);
	}

	public InventoryBuilder replace(String key, String value) {
		toReplace.put(key, value);
		return this;
	}

	public Inventory build() {
		if (config != null) {
			String title = config.getString("title");
			
			for (String key : toReplace.keySet()) {
				if (title.contains(key)) {
					title = title.replace(key, toReplace.get(key));
				}
			}
			
			inventory = Bukkit.createInventory(null, config.getInt("slots"),
					ChatColor.translateAlternateColorCodes('&', title));
			
			for (String key : config.getConfigurationSection("contents").getKeys(false)) {
				String stackData[] = config.getString("contents." + key + ".item").split(";"), name = config.getString("contents." + key + ".name");
				List<String> lore = config.getStringList("contents." + key + ".lore");
				
				for (String key1 : toReplace.keySet()) {
					if (name.contains(key1)) {
						name = name.replace(key1, toReplace.get(key1));
					}
					
				    for (int i = 0; i < lore.size(); i++) {
				        if (lore.get(i).contains(key1)) {
				            lore.set(i, lore.get(i).replace(key1, toReplace.get(key1)));
				        } 
				    }
				}
				
				ItemStack item = new ItemBuilder(
						stackData[0].equals("FLASHING_GLASS") ? Material.valueOf("STAINED_GLASS_PANE")
								: Material.valueOf(stackData[0]),
						Integer.valueOf(stackData[1]),
						stackData[0].equals("FLASHING_GLASS") ? (byte) new Random().nextInt(10)
								: Byte.valueOf(stackData[2])).setDisplayName(name).setLore(lore).build();

				InventoryFill invf = new InventoryFill(inventory);
				new BukkitRunnable() {
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						if (inventory.getViewers().isEmpty()) {
							cancel();
						}

						if (stackData[0].equals("FLASHING_GLASS")) {
							item.setDurability((byte) new Random().nextInt(10));
						}

						switch (key.toLowerCase()) {
						case "border":
							invf.fillSidesWithItem(item);
							break;
						case "air":
							break;
						default:
							inventory.setItem(Integer.valueOf(key), item);
							break;
						}

					}
				}.runTaskTimer(Minator.getPlugin(Minator.class), 0, config.getInt("animationDelay"));
			}
			return inventory;
		}

		inventory = Bukkit.createInventory(null, slots, ChatColor.translateAlternateColorCodes('&', this.title));
		for (Integer i : toAdd.keySet()) {
			inventory.setItem(i, toAdd.get(i));
		}

		return inventory;
	}
}