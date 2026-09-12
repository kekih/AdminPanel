package me.kekih.adminpanel.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.kekih.adminpanel.AdminPanel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    private final AdminPanel plugin;

    public ChatListener(AdminPanel plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        if (plugin.getMuteManager().isMuted(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cВы замучены и не можете писать в чат!");
        }
    }
}
