package minegame159.thebestplugin.listeners;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import minegame159.thebestplugin.utils.EntityTimer;
import minegame159.thebestplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemDespawnListener implements Listener {
    private final List<EntityTimer> entitiesToRemove = new ArrayList<>();

    @EventHandler
    public void onTick(ServerTickStartEvent event) {
        synchronized (entitiesToRemove) {
            for (Iterator<EntityTimer> it = entitiesToRemove.iterator(); it.hasNext(); ) {
                EntityTimer entityTimer = it.next();

                if (entityTimer.timer <= 0) {
                    entityTimer.entity.remove();
                    it.remove();
                } else {
                    entityTimer.timer--;
                }
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Item) {
            event.getEntity().setPersistent(false);

            if (event.getEntity().getWorld() == Bukkit.getWorld("world")) {
                if (Utils.isInRegion("kitcreator", event.getEntity()) || Utils.isInRegion("ow_spawn", event.getEntity())) {
                    synchronized (entitiesToRemove) {
                        entitiesToRemove.add(new EntityTimer(event.getEntity(), 20 * 5));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        synchronized (entitiesToRemove) {
            entitiesToRemove.removeIf(entityTimer -> entityTimer.entity == event.getEntity());
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        entitiesToRemove.clear();
    }
}
