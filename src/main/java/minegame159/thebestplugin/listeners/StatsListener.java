package minegame159.thebestplugin.listeners;

import minegame159.thebestplugin.TheBestPlugin;
import minegame159.thebestplugin.commands.StatsCommand;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StatsListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            TheBestPlugin.STATS.deaths++;
            TheBestPlugin.STATS.changed();
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof EnderCrystal) {
            TheBestPlugin.STATS.blownCrystals++;
            TheBestPlugin.STATS.changed();
        }
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        if (!event.isCancelled()) {
            TheBestPlugin.STATS.poppedTotems++;
            TheBestPlugin.STATS.changed();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(StatsCommand.GUI_TITLE)) event.setCancelled(true);
    }
}
