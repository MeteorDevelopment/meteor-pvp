package minegame159.thebestplugin.listeners;

import minegame159.thebestplugin.kits.Kit;
import minegame159.thebestplugin.kits.Kits;
import minegame159.thebestplugin.kits.MaxKits;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public class KitsGuiListener implements Listener {
    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;

        switch (event.getView().getTitle()) {
            case Kits.GUI_TITLE:              onGui(event, this::onGuiMain); break;
            case Kits.GUI_PRIVATE_KITS_TITLE:
            case Kits.GUI_PUBLIC_KITS_TITLE:  onGui(event, null); break;
        }
    }

    private void onGui(InventoryClickEvent event, Consumer<InventoryClickEvent> handler) {
        event.setCancelled(true);

        if (event.getCurrentItem().getType() == Material.DIAMOND_SWORD) {
            Kit kit = Kits.INSTANCE.get(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));

            if (kit != null) {
                if (event.getClick() == ClickType.LEFT) {
                    kit.apply(event.getWhoClicked());
                    event.getWhoClicked().closeInventory();
                } else if (event.getClick() == ClickType.RIGHT && MaxKits.get(event.getWhoClicked()).canHavePublic && kit.author.equals(event.getWhoClicked().getUniqueId())) {
                    if (kit.isPublic) Kits.INSTANCE.removePublic(kit);
                    else {
                        Kits.INSTANCE.addPublic(kit);

                        for (Kit k : Kits.INSTANCE.getKits(event.getWhoClicked())) {
                            if (k.isPublic) {
                                k.isPublic = false;
                                Kits.INSTANCE.removePublic(k);
                            }
                        }
                    }

                    kit.isPublic = !kit.isPublic;
                    Kits.INSTANCE.save();
                    event.getWhoClicked().openInventory(Kits.INSTANCE.guiPrivateKits(event.getWhoClicked()));
                }
            }

            return;
        }

        if (handler != null) handler.accept(event);
    }

    private void onGuiMain(InventoryClickEvent event) {
        String name = event.getCurrentItem().getItemMeta().getDisplayName();

        if (name.equals(Kits.GUI_PRIVATE_KITS_TITLE)) {
            event.getWhoClicked().openInventory(Kits.INSTANCE.guiPrivateKits(event.getWhoClicked()));
        } else if (name.equals(Kits.GUI_PUBLIC_KITS_TITLE)) {
            event.getWhoClicked().openInventory(Kits.INSTANCE.guiPublicKits(event.getWhoClicked()));
        }
    }
}
