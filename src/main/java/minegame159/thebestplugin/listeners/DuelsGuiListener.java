package minegame159.thebestplugin.listeners;

import minegame159.thebestplugin.duels.Duels;
import minegame159.thebestplugin.duels.DuelsGui;
import minegame159.thebestplugin.duels.DuelsMode;
import minegame159.thebestplugin.utils.Msgs;
import minegame159.thebestplugin.utils.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class DuelsGuiListener implements Listener {
    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(DuelsGui.TITLE)) return;
        event.setCancelled(true);

        Player receiver = Bukkit.getPlayer(event.getView().getTitle().replace(DuelsGui.TITLE, ""));
        if (receiver == null) {
            event.getWhoClicked().sendMessage(Prefixes.KITS + Msgs.playerNotOnline());
            return;
        }

        String name = event.getCurrentItem().getItemMeta().getDisplayName();
        if (name.equals(DuelsGui.OVERWORLD)) {
            onClick(Duels.INSTANCE.overworldNormal, event.getWhoClicked(), receiver);
        } else if (name.equals(DuelsGui.OVERWORLD_FLAT)) {
            onClick(Duels.INSTANCE.overworldFlat, event.getWhoClicked(), receiver);
        } else if (name.equals(DuelsGui.NETHER)) {
            onClick(Duels.INSTANCE.netherNormal, event.getWhoClicked(), receiver);
        } else if (name.equals(DuelsGui.NETHER_FLAT)) {
            onClick(Duels.INSTANCE.netherFlat, event.getWhoClicked(), receiver);
        }
    }

    private void onClick(DuelsMode mode, HumanEntity player, Player receiver) {
        if (!mode.isAvailable()) {
            player.sendMessage(Prefixes.DUELS + Msgs.noAvailableArenas());
            return;
        }

        Duels.INSTANCE.sendRequest(mode, player, receiver);
    }
}
