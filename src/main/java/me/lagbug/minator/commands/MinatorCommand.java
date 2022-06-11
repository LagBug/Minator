package me.lagbug.minator.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.lagbug.minator.Minator;
import me.lagbug.minator.common.builders.InventoryBuilder;
import me.lagbug.minator.common.commands.SpigotCommand;
import me.lagbug.minator.common.commands.SubCommand;
import me.lagbug.minator.utils.Permissions;

public class MinatorCommand extends SpigotCommand {

	private final static Minator plugin = Minator.getPlugin(Minator.class);

	public MinatorCommand() {
		super(Permissions.USE, 0, (SubCommand) null);
		super.setUsage("minator");
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(plugin.getMessage("errors.onlyPlayers"));
			return;
		}
		
		((Player) sender).openInventory(new InventoryBuilder(plugin.getFile("guis/home.yml")).build());
	}
}