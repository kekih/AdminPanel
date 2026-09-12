package me.kekih.adminpanel.gui;

import me.kekih.adminpanel.AdminPanel;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class MainMenu implements InventoryHolder {

    private final Inventory inventory;

    public MainMenu(AdminPanel plugin) {
        this.inventory = Bukkit.createInventory(this, 27, Component.text("§8§lАдмин Панель"));

        // Fill background
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, GuiUtils.createFiller());
        }

        // Players button
        inventory.setItem(11, GuiUtils.createItem(
                Material.PLAYER_HEAD,
                "§a§lИгроки",
                "§7Просмотр онлайн игроков",
                "§7и управление ими",
                "",
                "§eНажмите, чтобы открыть"
        ));

        // Reports button
        int reportCount = plugin.getReportManager().getCount();
        inventory.setItem(15, GuiUtils.createItem(
                Material.WRITABLE_BOOK,
                "§c§lРепорты",
                "§7Просмотр жалоб игроков",
                "",
                "§7Активных репортов: §e" + reportCount,
                "",
                "§eНажмите, чтобы открыть"
        ));

        // Close
        inventory.setItem(22, GuiUtils.createCloseButton());
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
