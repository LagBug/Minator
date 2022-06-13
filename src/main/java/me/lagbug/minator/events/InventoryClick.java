package me.lagbug.minator.events;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.lagbug.minator.handlers.AnswerHandler;
import me.lagbug.minator.handlers.CorrectHandler;
import me.lagbug.minator.handlers.ErrorHandler;
import me.lagbug.minator.handlers.HomeHandler;
import me.lagbug.minator.handlers.IncorrectHandler;
import me.lagbug.minator.handlers.LoadingHandler;
import me.lagbug.minator.handlers.QuestionHandler;
import me.lagbug.minator.utils.GUIHandler;

public class InventoryClick implements Listener {

	private List<GUIHandler> handlers = Arrays.asList(new HomeHandler(), 
				new QuestionHandler(), new AnswerHandler(), new LoadingHandler(), new ErrorHandler(),
					new CorrectHandler(), new IncorrectHandler());

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getCurrentItem() == null || e.getClickedInventory() == null || e.getInventory() == null || e.getCurrentItem().getType().equals(Material.AIR) || !(e.getWhoClicked() instanceof Player)) {
			return;
		}
		
		handlers.forEach(handler -> {
			if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', handler.getGUIName()))) {
				handler.execute(e, (Player) e.getWhoClicked());
				e.setCancelled(true);
				return;
			}
		});
	}
}