package meteordevelopment.meteorpvp.listeners;

import meteordevelopment.meteorpvp.Config;
import meteordevelopment.meteorpvp.arenas.Regions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.HashMap;
import java.util.Map;

public class KitCreatorListener implements Listener {
    private boolean saving = false;

    // SAVE KITS

//    @EventHandler
//    private void onServerTickEnd(ServerTickEndEvent event) {
//        if (!saving && Kits.INSTANCE.modifiedTimestamp != 0) {
//            long time = System.currentTimeMillis() - Kits.INSTANCE.modifiedTimestamp;
//
//            if (time > 60 * 60 * 1000) {
//                saving = true;
//
//                Bukkit.getScheduler().runTaskAsynchronously(MeteorPvp.INSTANCE, () -> {
//                    Kits.INSTANCE.save();
//                    saving = false;
//                });
//            }
//        }
//    }

    // RIGHT CLICK ITEM FRAMES

    @EventHandler
    private void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        openKitCreatorGui(event, event.getEntity(), event.getDamager());
    }

    @EventHandler
    private void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        openKitCreatorGui(event, event.getRightClicked(), event.getPlayer());
    }

    private void openKitCreatorGui(Cancellable event, Entity entity, Entity damager) {
        if (Config.KIT_CREATOR_ENABLED && entity.getWorld() == Bukkit.getWorld("world") && damager instanceof Player && entity instanceof ItemFrame && entity.getLocation().distance(Config.KIT_CREATOR_LOCATION) <= 20) {
            event.setCancelled(true);

            ItemStack frameItemStack = ((ItemFrame) entity).getItem();
            if (frameItemStack.getType() == Material.AIR) return;

            Inventory gui = Bukkit.createInventory((Player) damager, 9, frameItemStack.getItemMeta().getDisplayName());
            Material item = frameItemStack.getType();

            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = new ItemStack(item, frameItemStack.getMaxStackSize());
                itemStack.setItemMeta(frameItemStack.getItemMeta());
                gui.setItem(i, itemStack);
            }

            ((Player) damager).openInventory(gui);
        }
    }

    // OPEN SHULKER BOXES

    private final Map<Player, EquipmentSlot> in = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null && event.getItem().getItemMeta() instanceof BlockStateMeta && ((BlockStateMeta) event.getItem().getItemMeta()).getBlockState() instanceof ShulkerBox shulkerBox && Regions.isIn(Regions.KIT_CREATOR, event.getPlayer())) {
            event.setCancelled(true);

            Inventory inv = Bukkit.createInventory(event.getPlayer(), 27, "Shulker Box");
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

        in.remove(event.getPlayer());
    }
}
