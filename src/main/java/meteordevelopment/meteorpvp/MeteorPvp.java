package meteordevelopment.meteorpvp;

import meteordevelopment.meteorpvp.arenas.ArenaClearer;
import meteordevelopment.meteorpvp.chat.Ignores;
import meteordevelopment.meteorpvp.chat.Mutes;
import meteordevelopment.meteorpvp.commands.Commands;
import meteordevelopment.meteorpvp.duels.Duels;
import meteordevelopment.meteorpvp.kits.Kits;
import meteordevelopment.meteorpvp.listeners.Listeners;
import meteordevelopment.meteorpvp.arenas.Regions;
import meteordevelopment.meteorpvp.utils.Perms;
import meteordevelopment.meteorpvp.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MeteorPvp extends JavaPlugin implements Listener {
    public static MeteorPvp INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        long start = System.currentTimeMillis();
        
        Config.init();

        Perms.register();
        Commands.register();
        Listeners.register();

        Utils.onEnable();

        Regions.onEnable();
        ArenaClearer.onEnable();

        Kits.INSTANCE.init();
        Duels.INSTANCE.init();

        Ignores.load();
        Mutes.load();

        System.out.printf("Meteor PvP loaded in %sms.", System.currentTimeMillis() - start);
        System.out.println();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        Kits.INSTANCE.save();
        Ignores.save();
        Mutes.save();

        Config.save();
    }
}
