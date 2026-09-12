package me.kekih.adminpanel.commands;

import me.kekih.adminpanel.AdminPanel;
import me.kekih.adminpanel.gui.MainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ApanelCommand implements CommandExecutor {

    private final AdminPanel plugin;

    public ApanelCommand(AdminPanel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cТолько для игроков!");
            return true;
        }

        if (!player.hasPermission("adminpanel.use")) {
            player.sendMessage("§cУ вас нет прав!");
            return true;
        }

        // If spectating — stop
        if (plugin.getSpectateManager().isSpectating(player)) {
            plugin.getSpectateManager().stopSpectate(player);
            return true;
        }

        new MainMenu(plugin).open(player);
        return true;
    }
}
