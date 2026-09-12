package me.kekih.adminpanel.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayersMenu implements InventoryHolder {

    private final Inventory inventory;
    private final List<Player> players;

    public PlayersMenu() {
        this.players = new ArrayList<>(Bukkit.getOnlinePlayers());
        int size = Math.max(27, ((players.size() + 8) / 9) * 9);
        if (size > 54) size = 54;

        this.inventory = Bukkit.createInventory(this, size, Component.text("§8§lИгроки онлайн §7(" + players.size() + ")"));

        for (int i = 0; i < size; i++) {
            inventory.setItem(i, GuiUtils.createFiller());
        }

        int slot = 0;
        for (Player p : players) {
            if (slot >= size - 9) break;
            inventory.setItem(slot, GuiUtils.createPlayerHead(
                    p,
                    "§e" + p.getName(),
                    "§7UUID: §f" + p.getUniqueId().toString().substring(0, 8) + "...",
                    "§7Мир: §f" + p.getWorld().getName(),
                    "§7Пинг: §f" + p.getPing() + " мс",
                    "",
                    "§aНажмите, чтобы управлять"
            ));
            slot++;
        }

        // Bottom row
        inventory.setItem(size - 5, GuiUtils.createBackButton());
        inventory.setItem(size - 1, GuiUtils.createCloseButton());
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public Player getPlayerAt(int slot) {
        if (slot < 0 || slot >= players.size()) return null;
        return players.get(slot);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
