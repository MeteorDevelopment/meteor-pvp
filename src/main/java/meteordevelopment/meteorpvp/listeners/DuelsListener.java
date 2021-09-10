package meteordevelopment.meteorpvp.listeners;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import meteordevelopment.meteorpvp.duels.DuelRequest;
import meteordevelopment.meteorpvp.duels.Duels;
import meteordevelopment.meteorpvp.chat.Msgs;
import meteordevelopment.meteorpvp.chat.Prefixes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Iterator;
import java.util.List;

public class DuelsListener implements Listener {
    @EventHandler
    public void onTick(ServerTickStartEvent event) {
        for (Iterator<DuelRequest> it = Duels.INSTANCE.sentRequestsIterator(); it.hasNext(); ) {
            DuelRequest request = it.next();

            if (request.timer <= 0) {
                request.sender.sendMessage(Prefixes.DUELS + Msgs.yourDuelExpired());
                request.receiver.sendMessage(Prefixes.DUELS + Msgs.duelExpired(request.sender.getName()));

                it.remove();
                for (List<DuelRequest> requests : Duels.INSTANCE.pendingRequestsIterable()) {
                    for (Iterator<DuelRequest> it2 = requests.iterator(); it.hasNext(); ) {
                        DuelRequest r = it2.next();

                        if (r == request) {
                            it2.remove();
                        }
                    }
                }
            }
            else {
                request.timer--;
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Duels.INSTANCE.removeSentRequest(event.getPlayer());

        for (List<DuelRequest> requests : Duels.INSTANCE.pendingRequestsIterable()) {
            requests.removeIf(request -> request.sender == event.getPlayer() || request.receiver == event.getPlayer());
        }
    }
}
