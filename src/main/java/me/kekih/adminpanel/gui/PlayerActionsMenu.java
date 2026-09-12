package me.kekih.adminpanel.gui;

import me.kekih.adminpanel.AdminPanel;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class PlayerActionsMenu implements InventoryHolder {

    private final Inventory inventory;
    private final Player target;

    public PlayerActionsMenu(AdminPanel plugin, Player target) {
        this.target = target;
        this.inventory = Bukkit.createInventory(this, 27, Component.text("§8§lУправление: §e" + target.getName()));

        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, GuiUtils.createFiller());
        }

        // Head of target
        inventory.setItem(4, GuiUtils.createPlayerHead(
                target,
                "§e§l" + target.getName(),
                "§7Мир: §f" + target.getWorld().getName(),
                "§7Здоровье: §c" + String.format("%.1f", target.getHealth()) + " ❤",
                "§7Пинг: §f" + target.getPing() + " мс"
        ));

        // Ban
        inventory.setItem(10, GuiUtils.createItem(
                Material.RED_CONCRETE,
                "§c§lЗабанить",
                "§7Перманентный бан игрока",
                "",
                "§cНажмите, чтобы забанить"
        ));

        // Kick
        inventory.setItem(12, GuiUtils.createItem(
                Material.ORANGE_CONCRETE,
                "§6§lКикнуть",
                "§7Выгнать игрока с сервера",
                "",
                "§eНажмите, чтобы кикнуть"
        ));

        // Mute
        boolean muted = plugin.getMuteManager().isMuted(target.getUniqueId());
        inventory.setItem(14, GuiUtils.createItem(
                muted ? Material.GRAY_DYE : Material.LIME_DYE,
                muted ? "§a§lРазмутить" : "§e§lЗамутить",
                muted ? "§7Игрок сейчас в муте" : "§7Заглушить чат игрока",
                "",
                muted ? "§aНажмите, чтобы размутить" : "§eНажмите, чтобы замутить"
        ));

        // Spectate
        inventory.setItem(16, GuiUtils.createItem(
                Material.ENDER_EYE,
                "§b§lСлежка",
                "§7Невидимость + полёт",
                "§7Телепорт к игроку",
                "",
                "§bНажмите, чтобы начать слежку"
        ));

        inventory.setItem(22, GuiUtils.createBackButton());
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public Player getTarget() {
        return target;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
