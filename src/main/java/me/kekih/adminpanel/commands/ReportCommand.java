package me.kekih.adminpanel.commands;

import me.kekih.adminpanel.AdminPanel;
import me.kekih.adminpanel.models.Report;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReportCommand implements CommandExecutor {

    private final AdminPanel plugin;

    public ReportCommand(AdminPanel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cТолько для игроков!");
            return true;
        }

        if (!player.hasPermission("adminpanel.report")) {
            player.sendMessage("§cУ вас нет прав!");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§cИспользование: /report <ник> <причина>");
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            player.sendMessage("§cИгрок §e" + targetName + " §cне найден или оффлайн.");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage("§cНельзя пожаловаться на себя!");
            return true;
        }

        StringBuilder reasonBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reasonBuilder.append(args[i]).append(" ");
        }
        String reason = reasonBuilder.toString().trim();
        if (reason.isEmpty()) {
            player.sendMessage("§cУкажите причину!");
            return true;
        }

        Report report = new Report(
                player.getName(),
                player.getUniqueId(),
                target.getName(),
                target.getUniqueId(),
                reason
        );
        plugin.getReportManager().addReport(report);

        player.sendMessage("§aРепорт на §e" + target.getName() + " §aуспешно отправлен!");

        // Notify online admins
        String notify = "§c[Репорт] §e" + player.getName() + " §7пожаловался на §e" + target.getName() + "§7: §f" + reason;
        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (admin.hasPermission("adminpanel.reports") && !admin.equals(player)) {
                admin.sendMessage(notify);
            }
        }

        return true;
    }
}
