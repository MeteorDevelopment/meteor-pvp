package minegame159.thebestplugin.listeners;

import minegame159.thebestplugin.utils.Arenas;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!Arenas.isInAnyPvp(event.getEntity())) {
            event.getDrops().clear();
            event.setNewExp(0);
            event.setDroppedExp(0);
        }
    }
}
