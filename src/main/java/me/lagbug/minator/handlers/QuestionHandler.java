package me.lagbug.minator.handlers;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.markozajc.akiwrapper.Akiwrapper;
import com.markozajc.akiwrapper.Akiwrapper.Answer;
import com.markozajc.akiwrapper.core.entities.Guess;

import me.lagbug.minator.Minator;
import me.lagbug.minator.common.builders.InventoryBuilder;
import me.lagbug.minator.common.utils.Utils;
import me.lagbug.minator.utils.GUIHandler;

public class QuestionHandler extends GUIHandler {

	private final Minator plugin = Minator.getPlugin(Minator.class);
	private final YamlConfiguration file = plugin.getFile("guis/question.yml");

	public QuestionHandler() {
		super.setGUIName(file.getString("title"));
	}

	@Override
	public void execute(InventoryClickEvent e, Player player) {
		for (String key : file.getConfigurationSection("contents").getKeys(false)) {
			if (Utils.isInteger(key) && e.getSlot() == Integer.parseInt(key) && file.contains("contents." + key + ".action")) {
				String action = file.getString("contents." + key + ".action");
				player.openInventory(new InventoryBuilder(getFile("loading")).build());

				CompletableFuture.runAsync(() -> {
					Akiwrapper aw = plugin.getAkiMap().get(player);
					if (aw.getCurrentQuestion() != null) {
						try {
							if (action.equalsIgnoreCase("UNDO")) {
								aw.undoAnswer();
							} else {
								aw.answerCurrentQuestion(Answer.valueOf(action));
								Bukkit.getScheduler().runTask(plugin,() -> player.openInventory(new InventoryBuilder(file).replace("%question%", aw.getCurrentQuestion().getQuestion()).build()));
								
								for (Guess guess : aw.getGuessesAboveProbability(plugin.getConfigFile().getDouble("finishProbability"))) {
									if (guess.getProbability() >= plugin.getConfigFile().getDouble("finishProbability")) {
										Bukkit.getScheduler().runTaskLater(plugin,() -> player.openInventory(new InventoryBuilder(getFile("answer")).replace("%answer%", guess.getName()).build()), 5);
									}
								}
							}
						} catch (IOException ex) {
							Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(new InventoryBuilder(getFile("error")).replace("%error%", ex.getLocalizedMessage()).build()));
						}

					}
				}).whenComplete((result, ex) -> {
					Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(ex == null 
							? new InventoryBuilder(file).replace("%question%", plugin.getAkiMap().get(player).getCurrentQuestion().getQuestion()).build()
									: new InventoryBuilder(getFile("error")).replace("%error%", ex.getMessage().split(": ")[1]).build()));
				});
			}
		}
	}
}