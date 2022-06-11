package me.lagbug.minator.handlers;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.markozajc.akiwrapper.AkiwrapperBuilder;

import me.lagbug.minator.Minator;
import me.lagbug.minator.common.builders.InventoryBuilder;
import me.lagbug.minator.common.utils.Utils;
import me.lagbug.minator.utils.GUIHandler;

public class HomeHandler extends GUIHandler {

	private final Minator plugin = Minator.getPlugin(Minator.class);
	private final YamlConfiguration file = plugin.getFile("guis/home.yml");

	public HomeHandler() {
		super.setGUIName(file.getString("title"));
	}

	@Override
	public void execute(InventoryClickEvent e, Player player) {
		for (String key : file.getConfigurationSection("contents").getKeys(false)) {
			if (Utils.isInteger(key) && e.getSlot() == Integer.parseInt(key) && file.contains("contents." + key + ".action") && file.getString("contents." + key + ".action").equals("PLAY")) {
				player.openInventory(new InventoryBuilder(getFile("loading")).build());
				
				CompletableFuture.runAsync(() -> {
					plugin.getAkiMap().put(player, new AkiwrapperBuilder().build());
				}).whenComplete((result, ex) -> {
					Bukkit.getScheduler().runTask(plugin,() -> 
						player.openInventory(ex == null ? new InventoryBuilder(getFile("question")).replace("%question%", plugin.getAkiMap().get(player).getCurrentQuestion().getQuestion()).build()
												: new InventoryBuilder(getFile("error")).replace("%error%", ex.getMessage()).build()));
				});
				break;
			}
		}

	}
}
