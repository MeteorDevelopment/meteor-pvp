package minegame159.meteorpvp.listeners;

import minegame159.meteorpvp.MeteorPvp;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

public class Listeners {
    public static void register() {
        Bukkit.getPluginManager().registerEvents(MeteorPvp.INSTANCE, MeteorPvp.INSTANCE);

        for (Class<? extends Listener> klass : new Reflections("minegame159.meteorpvp.listeners").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = klass.newInstance();
                Bukkit.getPluginManager().registerEvents(listener, MeteorPvp.INSTANCE);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
