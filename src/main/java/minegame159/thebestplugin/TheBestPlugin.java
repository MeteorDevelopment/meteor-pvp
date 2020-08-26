package minegame159.thebestplugin;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import minegame159.thebestplugin.commands.*;
import minegame159.thebestplugin.utils.Uptime;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class TheBestPlugin extends JavaPlugin implements Listener {
    public static final Location SPAWN_LOCATION = new Location((Bukkit.getWorld("world")), 0, 100, 0);
    public static final Location KIT_CREATOR_LOCATION = new Location((Bukkit.getWorld("world")), 100000, 101, 100000);

    public static File CONFIG_FOLDER;

    public static boolean KIT_CREATOR_ENABLED = true;

    private Map<Entity, Integer> entitiesToRemove = new HashMap<>();

    @Override
    public void onEnable() {
        CONFIG_FOLDER = getDataFolder();
        CONFIG_FOLDER.mkdirs();

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Kits(), this);

        Bukkit.getScheduler().runTaskTimer(this, TabList::update, 0, 80);

        Bukkit.getPluginManager().addPermission(CreateKitCommand.UNLIMITED_KITS_PERM);
        Bukkit.getPluginManager().addPermission(DeleteKitCommand.DELETE_KIT_PERM);

        getCommand("gmc").setExecutor(new GmcCommand());
        getCommand("gms").setExecutor(new GmsCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("kits").setExecutor(new KitsCommand());
        getCommand("createkit").setExecutor(new CreateKitCommand());
        getCommand("kit").setExecutor(new KitCommand());
        getCommand("deletekit").setExecutor(new DeleteKitCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("kitcreator").setExecutor(new KitCreatorCommand());
        getCommand("suicide").setExecutor(new SuicideCommand());
        getCommand("togglekitcreator").setExecutor(new ToggleKitCreatorCommand());

        Uptime.onEnable();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        entitiesToRemove.clear();
    }

    @EventHandler
    public void onTick(ServerTickStartEvent event) {
        for (Entity entity : entitiesToRemove.keySet()) {
            int ticks = entitiesToRemove.get(entity);

            if (ticks <= 0) {
                entity.remove();
                entitiesToRemove.remove(entity);
            } else {
                ticks--;
                entitiesToRemove.put(entity, ticks);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getWorld() == Bukkit.getWorld("world") && event.getItemDrop().getLocation().distance(KIT_CREATOR_LOCATION) <= 20) {
            entitiesToRemove.put(event.getItemDrop(), 20 * 5);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        entitiesToRemove.remove(event.getEntity());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        TabList.update();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        TabList.update();
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        TabList.update();
    }

    @EventHandler
    private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/help")) {
            event.setCancelled(true);
            event.getPlayer().performCommand("help thebestplugin");
        }
    }

    @EventHandler
    private void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        openKitCreatorGui(event, event.getEntity(), event.getDamager());
    }
    
    @EventHandler
    private void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        openKitCreatorGui(event, event.getRightClicked(), event.getPlayer());
    }
    
    private void openKitCreatorGui(Cancellable event, Entity entity, Entity damager) {
        if (KIT_CREATOR_ENABLED && entity.getWorld() == Bukkit.getWorld("world") && damager instanceof Player && entity instanceof ItemFrame && entity.getLocation().distance(KIT_CREATOR_LOCATION) <= 20) {
            event.setCancelled(true);

            ItemStack frameItemStack = ((ItemFrame) entity).getItem();
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
}
