package me.kekih.adminpanel;

import me.kekih.adminpanel.commands.ApanelCommand;
import me.kekih.adminpanel.commands.ReportCommand;
import me.kekih.adminpanel.listeners.ChatListener;
import me.kekih.adminpanel.listeners.InventoryListener;
import me.kekih.adminpanel.managers.MuteManager;
import me.kekih.adminpanel.managers.ReportManager;
import me.kekih.adminpanel.managers.SpectateManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AdminPanel extends JavaPlugin {

    private static AdminPanel instance;
    private ReportManager reportManager;
    private MuteManager muteManager;
    private SpectateManager spectateManager;

    @Override
    public void onEnable() {
        instance = this;

        this.reportManager = new ReportManager();
        this.muteManager = new MuteManager();
        this.spectateManager = new SpectateManager(this);

        getCommand("apanel").setExecutor(new ApanelCommand(this));
        getCommand("report").setExecutor(new ReportCommand(this));

        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        getLogger().info("AdminPanel успешно загружен!");
    }

    @Override
    public void onDisable() {
        if (spectateManager != null) {
            spectateManager.restoreAll();
        }
        getLogger().info("AdminPanel выгружен.");
    }

    public static AdminPanel getInstance() {
        return instance;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }

    public MuteManager getMuteManager() {
        return muteManager;
    }

    public SpectateManager getSpectateManager() {
        return spectateManager;
    }
}
