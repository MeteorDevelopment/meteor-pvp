package minegame159.thebestplugin.listeners;

import minegame159.thebestplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.HashMap;
import java.util.Map;

public class KitCreatorShulkerListener implements Listener {
    private Map<Player, EquipmentSlot> in = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem().getItemMeta() instanceof BlockStateMeta && ((BlockStateMeta) event.getItem().getItemMeta()).getBlockState() instanceof ShulkerBox && Utils.isInKitCreator(event.getPlayer())) {
            event.setCancelled(true);

            Inventory inv = Bukkit.createInventory(event.getPlayer(), 27, "Shulker Box");
            ShulkerBox shulkerBox = ((ShulkerBox) ((BlockStateMeta) event.getItem().getItemMeta()).getBlockState());
            inv.setContents(shulkerBox.getInventory().getContents());
            event.getPlayer().openInventory(inv);

            in.put(event.getPlayer(), event.getHand());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        EquipmentSlot hand = in.get(event.getPlayer());
        ItemStack itemStack = hand == EquipmentSlot.HAND ? event.getPlayer().getInventory().getItemInMainHand() : event.getPlayer().getInventory().getItemInOffHand();

        if (hand != null) {
            BlockStateMeta itemMeta = (BlockStateMeta) itemStack.getItemMeta();
            ShulkerBox shulkerBox = (ShulkerBox) itemMeta.getBlockState();

            shulkerBox.getInventory().setContents(event.getInventory().getContents());
            shulkerBox.getSnapshotInventory().setContents(event.getInventory().getContents());

            itemMeta.setBlockState(shulkerBox);
            itemStack.setItemMeta(itemMeta);
        }

        if (in.containsKey(event.getPlayer())) in.remove(event.getPlayer());
    }
}
