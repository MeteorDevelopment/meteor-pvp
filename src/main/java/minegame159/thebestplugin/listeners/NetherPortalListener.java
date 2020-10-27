package minegame159.thebestplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

public class NetherPortalListener implements Listener {
    @EventHandler
    private void onPortalCreate(PortalCreateEvent event) {
        event.setCancelled(true);
    }
}
