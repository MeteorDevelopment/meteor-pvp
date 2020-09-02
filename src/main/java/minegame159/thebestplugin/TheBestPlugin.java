package minegame159.thebestplugin;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import minegame159.thebestplugin.commands.*;
import minegame159.thebestplugin.duels.Duels;
import minegame159.thebestplugin.json.ItemStackSerializer;
import minegame159.thebestplugin.json.KitSerializer;
import minegame159.thebestplugin.json.KitsSerializer;
import minegame159.thebestplugin.json.NamespacedKeySerializer;
import minegame159.thebestplugin.listeners.AntiLogListener;
import minegame159.thebestplugin.listeners.KitCreatorShulkerListener;
import minegame159.thebestplugin.listeners.StatsListener;
import minegame159.thebestplugin.utils.Arenas;
import minegame159.thebestplugin.utils.EntityTimer;
import minegame159.thebestplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TheBestPlugin extends JavaPlugin implements Listener {
    public static TheBestPlugin INSTANCE;

    public static Location KIT_CREATOR_LOCATION;
    public static Location NETHER_LOCATION;

    public static File CONFIG_FOLDER;

    public static File STATS_FILE;
    public static Stats STATS;
    public static Duels DUELS;

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Kits.class, new KitsSerializer())
            .registerTypeAdapter(Kit.class, new KitSerializer())
            .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
            .registerTypeAdapter(NamespacedKey.class, new NamespacedKeySerializer())
            .create();

    public static boolean KIT_CREATOR_ENABLED = true;

    private final Pattern HELP_PATTERN = Pattern.compile("/help( \\d+)?", Pattern.CASE_INSENSITIVE);
    private final List<EntityTimer> entitiesToRemove = new ArrayList<>();

    @Override
    public void onEnable() {
        INSTANCE = this;

        CONFIG_FOLDER = getDataFolder();
        CONFIG_FOLDER.mkdirs();

        KIT_CREATOR_LOCATION = new Location((Bukkit.getWorld("world")), 100000, 101, 100000);
        NETHER_LOCATION = new Location((Bukkit.getWorld("world_nether")), 0, 117, 0);

        STATS_FILE = new File(CONFIG_FOLDER, "stats.json");
        if (STATS_FILE.exists()) {
            try {
                STATS = GSON.fromJson(new FileReader(STATS_FILE), Stats.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            STATS = new Stats();
        }

        DUELS = new Duels();

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Kits(), this);
        Bukkit.getPluginManager().registerEvents(new StatsListener(), this);
        Bukkit.getPluginManager().registerEvents(DUELS, this);
        Bukkit.getPluginManager().registerEvents(new KitCreatorShulkerListener(), this);
        Bukkit.getPluginManager().registerEvents(new AntiLogListener(), this);

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
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("duel").setExecutor(new DuelCommand());
        getCommand("nether").setExecutor(new NetherCommand());
        getCommand("trashcan").setExecutor(new TrashCanCommand());
        getCommand("cancelduel").setExecutor(new CancelDuelCommand());
        getCommand("accept").setExecutor(new AcceptCommand());
        getCommand("decline").setExecutor(new DeclineCommand());
        getCommand("cleararenas").setExecutor(new ClearArenasCommand());

        Utils.onEnable();
        Kits.INSTANCE.onEnable();
        Arenas.onEnable();
        ArenaClearer.onEnable();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        entitiesToRemove.clear();
        saveStats();
    }

    private void saveStats() {
        try {
            FileWriter writer = new FileWriter(STATS_FILE);
            GSON.toJson(STATS, writer);
            writer.close();

            STATS.dirty = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onTick(ServerTickStartEvent event) {
        synchronized (entitiesToRemove) {
            for (Iterator<EntityTimer> it = entitiesToRemove.iterator(); it.hasNext(); ) {
                EntityTimer entityTimer = it.next();

                if (entityTimer.timer <= 0) {
                    entityTimer.entity.remove();
                    it.remove();
                } else {
                    entityTimer.timer--;
                }
            }
        }

        if (STATS.dirty) {
            if (STATS.saveTimer <= 0) {
                saveStats();
            } else {
                STATS.saveTimer--;
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Item) {
            event.getEntity().setPersistent(false);

            if (event.getEntity().getWorld() == Bukkit.getWorld("world")) {
                if (Utils.isInRegion("kitcreator", event.getEntity()) || Utils.isInRegion("ow_spawn", event.getEntity())) {
                    synchronized (entitiesToRemove) {
                        entitiesToRemove.add(new EntityTimer(event.getEntity(), 20 * 5));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        synchronized (entitiesToRemove) {
            entitiesToRemove.removeIf(entityTimer -> entityTimer.entity == event.getEntity());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        TabList.update();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        TabList.update();
    }

    @EventHandler
    private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/help")) {
            event.setCancelled(true);
            Matcher matcher = HELP_PATTERN.matcher(event.getMessage());

            if (matcher.matches()) {
                String cmd = "help thebestplugin";
                if (matcher.group(1) != null) cmd += matcher.group(1);
                event.getPlayer().performCommand(cmd);
            }
        }
    }

    @EventHandler
    private void onTabComplete(TabCompleteEvent event) {
        if (event.getBuffer().startsWith("/help")) {
            event.getCompletions().clear();
            event.getCompletions().add("1");
            event.getCompletions().add("2");
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
