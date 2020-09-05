package minegame159.thebestplugin.listeners;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import minegame159.thebestplugin.Stats;
import minegame159.thebestplugin.commands.StatsCommand;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class StatsListener implements Listener {
    @EventHandler
    public void onTick(ServerTickStartEvent event) {
        if (Stats.INSTANCE.dirty) {
            if (Stats.INSTANCE.saveTimer <= 0) {
                Stats.INSTANCE.save();
            } else {
                Stats.INSTANCE.saveTimer--;
            }
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Stats.INSTANCE.save();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Stats.INSTANCE.deaths++;
            Stats.INSTANCE.changed();
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof EnderCrystal) {
            Stats.INSTANCE.blownCrystals++;
            Stats.INSTANCE.changed();
        }
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        if (!event.isCancelled()) {
            Stats.INSTANCE.poppedTotems++;
            Stats.INSTANCE.changed();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(StatsCommand.GUI_TITLE)) event.setCancelled(true);
    }
}
