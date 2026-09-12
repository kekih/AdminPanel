package me.kekih.adminpanel.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public final class GuiUtils {

    private GuiUtils() {}

    public static Component color(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(text)
                .decoration(TextDecoration.ITALIC, false);
    }

    public static ItemStack createItem(Material material, String name, String... loreLines) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(color(name));
            if (loreLines.length > 0) {
                List<Component> lore = new ArrayList<>();
                for (String line : loreLines) {
                    lore.add(color(line));
                }
                meta.lore(lore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createPlayerHead(Player player, String name, String... loreLines) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            meta.displayName(color(name));
            if (loreLines.length > 0) {
                List<Component> lore = new ArrayList<>();
                for (String line : loreLines) {
                    lore.add(color(line));
                }
                meta.lore(lore);
            }
            head.setItemMeta(meta);
        }
        return head;
    }

    public static ItemStack createFiller() {
        return createItem(Material.GRAY_STAINED_GLASS_PANE, " ");
    }

    public static ItemStack createBackButton() {
        return createItem(Material.ARROW, "§c« Назад", "§7Вернуться в главное меню");
    }

    public static ItemStack createCloseButton() {
        return createItem(Material.BARRIER, "§cЗакрыть", "§7Закрыть меню");
    }
}
