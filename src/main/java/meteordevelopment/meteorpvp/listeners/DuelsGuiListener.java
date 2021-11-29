package meteordevelopment.meteorpvp.listeners;

import meteordevelopment.meteorpvp.utils.Msgs;
import meteordevelopment.meteorpvp.utils.Prefixes;
import meteordevelopment.meteorpvp.duels.Duels;
import meteordevelopment.meteorpvp.duels.DuelsGui;
import meteordevelopment.meteorpvp.duels.DuelsMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DuelsGuiListener implements Listener {
    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().startsWith(DuelsGui.TITLE)) return;
        event.setCancelled(true);

        Player receiver = Bukkit.getPlayer(event.getView().getTitle().replace(DuelsGui.TITLE, ""));
        if (receiver == null) {
            event.getWhoClicked().sendMessage(Prefixes.KITS + Msgs.playerNotOnline());
            return;
        }

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        String name = item.getItemMeta().getDisplayName();
        if (name.equals(DuelsGui.OVERWORLD)) {
            onClick(event, Duels.INSTANCE.overworldNormal, event.getWhoClicked(), receiver);
        }
        else if (name.equals(DuelsGui.OVERWORLD_FLAT)) {
            onClick(event, Duels.INSTANCE.overworldFlat, event.getWhoClicked(), receiver);
        }
        else if (name.equals(DuelsGui.NETHER)) {
            onClick(event, Duels.INSTANCE.netherNormal, event.getWhoClicked(), receiver);
        }
        else if (name.equals(DuelsGui.NETHER_FLAT)) {
            onClick(event, Duels.INSTANCE.netherFlat, event.getWhoClicked(), receiver);
        }
    }

    private void onClick(InventoryClickEvent event, DuelsMode mode, HumanEntity player, Player receiver) {
        if (!mode.anyArenasAvailable()) {
            player.sendMessage(Prefixes.DUELS + Msgs.noAvailableArenas());
            return;
        }

        Duels.INSTANCE.sendRequest(mode, player, receiver);
        event.getWhoClicked().closeInventory();
    }
}
