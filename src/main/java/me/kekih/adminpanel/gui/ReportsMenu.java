package me.kekih.adminpanel.gui;

import me.kekih.adminpanel.AdminPanel;
import me.kekih.adminpanel.models.Report;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportsMenu implements InventoryHolder {

    private final Inventory inventory;
    private final List<Report> reports;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm");

    public ReportsMenu(AdminPanel plugin) {
        this.reports = plugin.getReportManager().getReports();
        int size = Math.max(27, ((reports.size() + 8) / 9) * 9);
        if (size > 54) size = 54;

        this.inventory = Bukkit.createInventory(this, size, Component.text("§8§lРепорты §7(" + reports.size() + ")"));

        for (int i = 0; i < size; i++) {
            inventory.setItem(i, GuiUtils.createFiller());
        }

        int slot = 0;
        for (Report report : reports) {
            if (slot >= size - 9) break;

            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(GuiUtils.color("§cРепорт на §e" + report.getTargetName()));
                List<Component> lore = new ArrayList<>();
                lore.add(GuiUtils.color("§7От: §f" + report.getReporterName()));
                lore.add(GuiUtils.color("§7Причина: §f" + report.getReason()));
                lore.add(GuiUtils.color("§7Время: §f" + dateFormat.format(new Date(report.getTimestamp()))));
                lore.add(GuiUtils.color(""));
                lore.add(GuiUtils.color("§aЛКМ §7— телепортироваться"));
                lore.add(GuiUtils.color("§cПКМ §7— закрыть репорт"));
                meta.lore(lore);
                item.setItemMeta(meta);
            }
            inventory.setItem(slot, item);
            slot++;
        }

        inventory.setItem(size - 5, GuiUtils.createBackButton());
        inventory.setItem(size - 1, GuiUtils.createCloseButton());
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public Report getReportAt(int slot) {
        if (slot < 0 || slot >= reports.size()) return null;
        return reports.get(slot);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
