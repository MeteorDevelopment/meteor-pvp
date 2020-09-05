package minegame159.thebestplugin.duels;

import minegame159.thebestplugin.utils.Msgs;
import minegame159.thebestplugin.utils.Prefixes;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class Duels {
    public static final Duels INSTANCE = new Duels();

    private final Stack<DuelArena> emptyArenas = new Stack<>();
    private final List<DuelArena> usedArenas = new ArrayList<>();

    private final Map<Player, DuelRequest> sentRequests = new HashMap<>();
    private final Map<Player, List<DuelRequest>> pendingRequests = new HashMap<>();

    private Duels() {}

    public void init() {
        emptyArenas.clear();
        usedArenas.clear();
        sentRequests.clear();
        pendingRequests.clear();

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

    public Iterator<DuelRequest> sentRequestsIterator() { return sentRequests.values().iterator(); }
    public Iterable<List<DuelRequest>> pendingRequestsIterable() { return pendingRequests.values(); }

    public boolean sendRequest(Player sender, Player receiver) {
        DuelRequest request = new DuelRequest(sender, receiver);
        boolean sent = sentRequests.putIfAbsent(sender, request) == null;

        if (sent) {
            receiver.sendMessage(Prefixes.DUELS + Msgs.duelRequest(sender.getName()));

            TextComponent accept = new TextComponent(ChatColor.GREEN + "[Accept]");
            String acceptCmd = "/accept " + sender.getName();
            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{ new TextComponent(acceptCmd) }));
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, acceptCmd));

            TextComponent decline = new TextComponent(ChatColor.RED + "[Decline]");
            String declineCmd = "/decline " + sender.getName();
            decline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{ new TextComponent(declineCmd) }));
            decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, declineCmd));

            receiver.sendMessage(new TextComponent(Prefixes.DUELS), accept, new TextComponent(" "), decline);

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

        sender.sendMessage(Prefixes.DUELS + Msgs.playerDeclinedDuel(receiver.getName()));
        receiver.sendMessage(Prefixes.DUELS + Msgs.youDeclinedDuel(sender.getName()));

        sentRequests.remove(sender);
        removePendingRequest(sender, receiver);
        return true;
    }

    public void removeSentRequest(Player sender) {
        sentRequests.remove(sender);
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
}
