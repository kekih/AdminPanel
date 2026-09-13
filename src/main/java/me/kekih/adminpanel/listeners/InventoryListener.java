package me.kekih.adminpanel.listeners;

import me.kekih.adminpanel.AdminPanel;
import me.kekih.adminpanel.gui.*;
import me.kekih.adminpanel.models.Report;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryListener implements Listener {

    private final AdminPanel plugin;

    public InventoryListener(AdminPanel plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Важно: проверяем ТОЛЬКО верхний инвентарь (наше меню)
        Inventory top = event.getView().getTopInventory();
        InventoryHolder holder = top.getHolder();

        if (!(holder instanceof MainMenu
                || holder instanceof PlayersMenu
                || holder instanceof PlayerActionsMenu
                || holder instanceof ReportsMenu)) {
            return; // обычный инвентарь игрока — не трогаем
        }

        // Наше меню открыто — отменяем любые клики (включая нижний инвентарь)
        event.setCancelled(true);

        // Обрабатываем только клики по верхнему инвентарю
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= top.getSize()) {
            return;
        }

        if (holder instanceof MainMenu) {
            handleMainMenu(player, slot);
        } else if (holder instanceof PlayersMenu playersMenu) {
            handlePlayersMenu(player, playersMenu, slot);
        } else if (holder instanceof PlayerActionsMenu actionsMenu) {
            handleActionsMenu(player, actionsMenu, slot);
        } else if (holder instanceof ReportsMenu reportsMenu) {
            handleReportsMenu(player, reportsMenu, slot, event.getClick());
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        InventoryHolder holder = event.getView().getTopInventory().getHolder();
        if (holder instanceof MainMenu
                || holder instanceof PlayersMenu
                || holder instanceof PlayerActionsMenu
                || holder instanceof ReportsMenu) {
            event.setCancelled(true);
        }
    }

    private void handleMainMenu(Player player, int slot) {
        if (slot == 11) { // Players
            new PlayersMenu().open(player);
        } else if (slot == 15) { // Reports
            if (!player.hasPermission("adminpanel.reports")) {
                player.sendMessage("§cНет прав на просмотр репортов!");
                return;
            }
            new ReportsMenu(plugin).open(player);
        } else if (slot == 22) {
            player.closeInventory();
        }
    }

    private void handlePlayersMenu(Player player, PlayersMenu menu, int slot) {
        int size = menu.getInventory().getSize();
        if (slot == size - 5) { // Back
            new MainMenu(plugin).open(player);
            return;
        }
        if (slot == size - 1) {
            player.closeInventory();
            return;
        }

        Player target = menu.getPlayerAt(slot);
        if (target != null && target.isOnline()) {
            new PlayerActionsMenu(plugin, target).open(player);
        }
    }

    private void handleActionsMenu(Player player, PlayerActionsMenu menu, int slot) {
        Player target = menu.getTarget();
        if (target == null || !target.isOnline()) {
            player.sendMessage("§cИгрок больше не онлайн.");
            player.closeInventory();
            return;
        }

        if (slot == 22) { // Back
            new PlayersMenu().open(player);
            return;
        }

        // Защита от действий над собой
        if (target.equals(player)) {
            player.sendMessage("§cНельзя применять это действие к себе!");
            return;
        }

        switch (slot) {
            case 10 -> { // Ban
                if (!player.hasPermission("adminpanel.ban")) {
                    player.sendMessage("§cНет прав на бан!");
                    return;
                }
                if (target.hasPermission("adminpanel.use")) {
                    player.sendMessage("§cНельзя банить администраторов!");
                    return;
                }
                Bukkit.getBanList(BanList.Type.NAME).addBan(
                        target.getName(),
                        "Забанен администратором " + player.getName() + " через AdminPanel",
                        null,
                        player.getName()
                );
                target.kick(net.kyori.adventure.text.Component.text("§cВы были забанены!\n§7Причина: Админ-панель"));
                player.sendMessage("§aИгрок §e" + target.getName() + " §aзабанен.");
                player.closeInventory();
            }
            case 12 -> { // Kick
                if (!player.hasPermission("adminpanel.kick")) {
                    player.sendMessage("§cНет прав на кик!");
                    return;
                }
                target.kick(net.kyori.adventure.text.Component.text("§cВы были кикнуты администратором " + player.getName()));
                player.sendMessage("§aИгрок §e" + target.getName() + " §aкикнут.");
                player.closeInventory();
            }
            case 14 -> { // Mute toggle
                if (!player.hasPermission("adminpanel.mute")) {
                    player.sendMessage("§cНет прав на мут!");
                    return;
                }
                boolean nowMuted = plugin.getMuteManager().toggleMute(target.getUniqueId());
                if (nowMuted) {
                    player.sendMessage("§aИгрок §e" + target.getName() + " §aзамучен.");
                    target.sendMessage("§cВы были замучены администратором.");
                } else {
                    player.sendMessage("§aИгрок §e" + target.getName() + " §aразмучен.");
                    target.sendMessage("§aВы были размучены.");
                }
                // Refresh menu
                new PlayerActionsMenu(plugin, target).open(player);
            }
            case 16 -> { // Spectate
                if (!player.hasPermission("adminpanel.spectate")) {
                    player.sendMessage("§cНет прав на слежку!");
                    return;
                }
                player.closeInventory();
                plugin.getSpectateManager().startSpectate(player, target);
            }
        }
    }

    private void handleReportsMenu(Player player, ReportsMenu menu, int slot, ClickType click) {
        int size = menu.getInventory().getSize();
        if (slot == size - 5) {
            new MainMenu(plugin).open(player);
            return;
        }
        if (slot == size - 1) {
            player.closeInventory();
            return;
        }

        Report report = menu.getReportAt(slot);
        if (report == null) return;

        if (click.isLeftClick()) {
            // Teleport to target
            Player target = Bukkit.getPlayer(report.getTargetUuid());
            if (target != null && target.isOnline()) {
                player.teleport(target.getLocation());
                player.sendMessage("§aТелепортирован к §e" + target.getName());
            } else {
                player.sendMessage("§cИгрок §e" + report.getTargetName() + " §cоффлайн.");
            }
        } else if (click.isRightClick()) {
            // Close report
            plugin.getReportManager().removeReport(report.getId());
            player.sendMessage("§aРепорт закрыт.");
            new ReportsMenu(plugin).open(player);
        }
    }
}
