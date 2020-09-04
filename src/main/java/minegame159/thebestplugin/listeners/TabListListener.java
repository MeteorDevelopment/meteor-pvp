package minegame159.thebestplugin.listeners;

import minegame159.thebestplugin.TabList;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TabListListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        TabList.update();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        TabList.update();
    }
}
