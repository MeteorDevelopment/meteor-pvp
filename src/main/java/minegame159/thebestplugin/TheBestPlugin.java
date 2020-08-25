package minegame159.thebestplugin;

import minegame159.thebestplugin.utils.Uptime;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class TheBestPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getScheduler().runTaskTimer(this, TabList::update, 0, 80);

        Uptime.onEnable();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        TabList.update();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        TabList.update();
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        TabList.update();
    }
}
