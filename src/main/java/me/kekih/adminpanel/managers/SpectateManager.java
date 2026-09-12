package me.kekih.adminpanel.managers;

import me.kekih.adminpanel.AdminPanel;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpectateManager {

    private final AdminPanel plugin;
    private final Map<UUID, SpectateData> active = new HashMap<>();

    public SpectateManager(AdminPanel plugin) {
        this.plugin = plugin;
    }

    public void startSpectate(Player admin, Player target) {
        if (active.containsKey(admin.getUniqueId())) {
            stopSpectate(admin);
        }

        SpectateData data = new SpectateData(
                admin.getGameMode(),
                admin.getAllowFlight(),
                admin.isFlying(),
                admin.getLocation()
        );
        active.put(admin.getUniqueId(), data);

        admin.setGameMode(GameMode.SURVIVAL);
        admin.setAllowFlight(true);
        admin.setFlying(true);
        admin.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false, false));
        admin.teleport(target.getLocation());

        admin.sendMessage("§aВы начали слежку за §e" + target.getName() + "§a. Используйте /apanel снова или напишите в чат, чтобы выйти.");
    }

    public void stopSpectate(Player admin) {
        SpectateData data = active.remove(admin.getUniqueId());
        if (data == null) return;

        admin.removePotionEffect(PotionEffectType.INVISIBILITY);
        admin.setGameMode(data.gameMode());
        admin.setAllowFlight(data.allowFlight());
        admin.setFlying(data.flying());
        admin.teleport(data.location());

        admin.sendMessage("§cСлежка завершена.");
    }

    public boolean isSpectating(Player admin) {
        return active.containsKey(admin.getUniqueId());
    }

    public void restoreAll() {
        for (UUID uuid : active.keySet()) {
            Player p = plugin.getServer().getPlayer(uuid);
            if (p != null) {
                stopSpectate(p);
            }
        }
        active.clear();
    }

    private record SpectateData(GameMode gameMode, boolean allowFlight, boolean flying, org.bukkit.Location location) {}
}
