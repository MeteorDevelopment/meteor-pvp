package minegame159.thebestplugin.listeners;

import minegame159.thebestplugin.utils.Arenas;
import minegame159.thebestplugin.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class AntiLogListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (Utils.isIn(Arenas.OVERWORLD, event.getPlayer())) {
            Utils.dropItems(event.getPlayer());
        }

        Utils.resetToSpawn(event.getPlayer());
    }
}
