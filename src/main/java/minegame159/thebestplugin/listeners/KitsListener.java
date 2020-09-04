package minegame159.thebestplugin.listeners;

import minegame159.thebestplugin.Kit;
import minegame159.thebestplugin.Kits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class KitsListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(Kits.INSTANCE.GUI_TITLE)) return;
        if (event.getCurrentItem() == null) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        Kit kit = Kits.INSTANCE.getKit(event.getCurrentItem().getItemMeta().getDisplayName());
        if (kit != null) kit.apply(player);

        player.closeInventory();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Kits.INSTANCE.clearUsedKit(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Kits.INSTANCE.clearUsedKit(event.getEntity());
    }
}
