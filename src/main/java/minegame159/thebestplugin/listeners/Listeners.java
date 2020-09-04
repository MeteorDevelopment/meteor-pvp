package minegame159.thebestplugin.listeners;

import minegame159.thebestplugin.TheBestPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

public class Listeners {
    public static void register() {
        Bukkit.getPluginManager().registerEvents(TheBestPlugin.INSTANCE, TheBestPlugin.INSTANCE);

        for (Class<? extends Listener> klass : new Reflections("minegame159.thebestplugin.listeners").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = klass.newInstance();
                Bukkit.getPluginManager().registerEvents(listener, TheBestPlugin.INSTANCE);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
