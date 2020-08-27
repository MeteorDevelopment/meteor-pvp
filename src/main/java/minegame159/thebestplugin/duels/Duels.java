package minegame159.thebestplugin.duels;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class Duels implements Listener {
    public static final String MSG_PREFIX = ChatColor.BLUE + "[Duels]: " + ChatColor.WHITE;

    private final Stack<DuelArena> emptyArenas = new Stack<>();
    private final List<DuelArena> usedArenas = new ArrayList<>();

    private final Map<Player, DuelRequest> sentRequests = new HashMap<>();
    private final Map<Player, List<DuelRequest>> pendingRequests = new HashMap<>();

    public Duels() {
        int startX = 10000;
        int startZ = 10000;

        for (int i = 0; i < 50; i++) {
            emptyArenas.push(new DuelArena(startX, startZ + i * DuelArena.SIZE * 2));
        }
    }

    public DuelArena getDuel(Player player) {
        for (DuelArena arena : usedArenas) {
            if (arena.player1 == player || arena.player2 == player) return arena;
        }

        return null;
    }

    protected void addEmptyArena(DuelArena arena) {
        emptyArenas.add(arena);
    }

    // Requests

    public boolean sendRequest(Player sender, Player receiver) {
        DuelRequest request = new DuelRequest(sender, receiver);
        boolean sent = sentRequests.putIfAbsent(sender, request) == null;

        if (sent) {
            receiver.sendMessage(String.format("%s%s%s%s %swants to duel you.", MSG_PREFIX, ChatColor.YELLOW, ChatColor.BOLD, sender.getName(), ChatColor.GRAY));

            TextComponent accept = new TextComponent(ChatColor.GREEN + "[Accept]");
            String acceptCmd = "/accept " + sender.getName();
            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{ new TextComponent(acceptCmd) }));
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, acceptCmd));

            TextComponent decline = new TextComponent(ChatColor.RED + "[Decline]");
            String declineCmd = "/decline" + sender.getName();
            decline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{ new TextComponent(declineCmd) }));
            decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, declineCmd));

            receiver.sendMessage(new TextComponent(MSG_PREFIX), accept, new TextComponent(" "), decline);

            getPendingRequests(receiver).add(request);
        }

        return sent;
    }

    public boolean cancelRequest(Player sender) {
        return sentRequests.remove(sender) != null;
    }

    public boolean accept(Player sender, Player receiver) {
        DuelRequest request = getPendingRequest(sender, receiver);
        if (request == null) return false;

        DuelArena arena = emptyArenas.pop();
        arena.start(sender, receiver);
        usedArenas.add(arena);

        sentRequests.remove(sender);
        removePendingRequest(sender, receiver);
        return true;
    }

    public boolean decline(Player sender, Player receiver) {
        DuelRequest request = getPendingRequest(sender, receiver);
        if (request == null) return false;

        sender.sendMessage(MSG_PREFIX + receiver.getName() + " declined your duel.");
        receiver.sendMessage(MSG_PREFIX + "Declined duel from " + sender.getName() + ".");

        sentRequests.remove(sender);
        removePendingRequest(sender, receiver);
        return true;
    }

    private List<DuelRequest> getPendingRequests(Player receiver) {
        return pendingRequests.computeIfAbsent(receiver, player1 -> new ArrayList<>(1));
    }

    private DuelRequest getPendingRequest(Player sender, Player receiver) {
        List<DuelRequest> requests = getPendingRequests(receiver);

        for (DuelRequest request : requests) {
            if (request.sender == sender) return request;
        }

        return null;
    }

    private void removePendingRequest(Player sender, Player receiver) {
        getPendingRequests(receiver).removeIf(duelRequest -> duelRequest.sender == sender);
    }

    // Listeners

    @EventHandler
    public void onTick(ServerTickStartEvent event) {
        for (Iterator<DuelRequest> it = sentRequests.values().iterator(); it.hasNext(); ) {
            DuelRequest request = it.next();

            if (request.timer <= 0) {
                request.sender.sendMessage(MSG_PREFIX + "Your duel request expired.");
                request.receiver.sendMessage(MSG_PREFIX + "Duel request from " + request.sender.getName() + " expired.");

                it.remove();
                for (List<DuelRequest> requests : pendingRequests.values()) {
                    for (Iterator<DuelRequest> it2 = requests.iterator(); it.hasNext(); ) {
                        DuelRequest r = it2.next();

                        if (r == request) {
                            it2.remove();
                        }
                    }
                }
            } else {
                request.timer--;
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        sentRequests.remove(event.getPlayer());

        for (List<DuelRequest> requests : pendingRequests.values()) {
            requests.removeIf(request -> request.sender == event.getPlayer() || request.receiver == event.getPlayer());
        }
    }
}
