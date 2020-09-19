package minegame159.thebestplugin.listeners;

import minegame159.thebestplugin.Kit;
import minegame159.thebestplugin.Kits;
import minegame159.thebestplugin.utils.Arenas;
import minegame159.thebestplugin.utils.Msgs;
import minegame159.thebestplugin.utils.Prefixes;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.SkullMeta;

public class KitsListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(Kits.GUI_TITLE)) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        if (event.getCurrentItem().getType() == Material.DIAMOND_SWORD) {
            player.closeInventory();

            if (Arenas.isInAnyPvp(player)) {
                player.sendMessage(Prefixes.KITS + Msgs.cantUseThisInPvp());
                return;
            }

            Kit kit = Kits.INSTANCE.getKit(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
            if (kit != null) kit.apply(player);
        } else if (event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
            int page = Integer.parseInt(event.getInventory().getItem(9 * 5 + 4).getItemMeta().getDisplayName().split(" ")[1]);
            String owner = ((SkullMeta) event.getCurrentItem().getItemMeta()).getOwningPlayer().getName();

            if (owner != null) {
                if (owner.equals("MHF_ArrowLeft")) page--;
                else page++;

                Kits.INSTANCE.fillGui(event.getInventory(), page);
            }
        }
    }
}
