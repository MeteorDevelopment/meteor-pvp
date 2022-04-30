package meteordevelopment.meteorpvp.listeners;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.InetAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AntiBotListener implements Listener {
    private final Object2IntMap<InetAddress> connections = new Object2IntOpenHashMap<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock rLock = lock.readLock();
    private final Lock wLock = lock.writeLock();

    @EventHandler
    private void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
        rLock.lock();
        int count = connections.getInt(event.getAddress());
        rLock.unlock();

        if (count >= 2) event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text(""));
        else {
            wLock.lock();
            connections.put(event.getAddress(), count + 1);
            wLock.unlock();
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        wLock.lock();
        connections.removeInt(event.getPlayer().getAddress().getAddress());
        wLock.unlock();
    }
}
