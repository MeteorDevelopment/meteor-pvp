package minegame159.meteorpvp.duels;

import minegame159.meteorpvp.utils.Msgs;
import minegame159.meteorpvp.utils.Prefixes;
import minegame159.meteorpvp.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.*;

public enum Duels {
    INSTANCE;

    public DuelsMode overworldNormal;
    public DuelsMode overworldFlat;
    
    public DuelsMode netherNormal;
    public DuelsMode netherFlat;

    final Map<HumanEntity, Duel> duels = new HashMap<>();

    private final Map<HumanEntity, DuelRequest> sentRequests = new HashMap<>();
    private final Map<HumanEntity, List<DuelRequest>> pendingRequests = new HashMap<>();

    public void init() {
        overworldNormal = new DuelsMode(Utils.OVERWORLD, "overworld", 1000000, 1000000, 1000192, 1000000, 1000384, 1000000, 1000576, 1000000, 1000768, 1000000);
        overworldFlat = new DuelsMode(Utils.OVERWORLD, "flat overworld", 1000000, 1000192, 1000192, 1000192, 1000384, 1000192, 1000576, 1000192, 1000768, 1000192);

        netherNormal = new DuelsMode(Utils.NETHER, "nether", 1000000, 1000000, 1000192, 1000000, 1000384, 1000000, 1000576, 1000000, 1000768, 1000000);
        netherFlat = new DuelsMode(Utils.NETHER, "flat nether", 1000000, 1000192, 1000192, 1000192, 1000384, 1000192, 1000576, 1000192, 1000768, 1000192);

        duels.clear();
        sentRequests.clear();
        pendingRequests.clear();
    }

    public Duel get(Player player) {
        return duels.get(player);
    }

    public Iterator<DuelRequest> sentRequestsIterator() { return sentRequests.values().iterator(); }
    public Iterable<List<DuelRequest>> pendingRequestsIterable() { return pendingRequests.values(); }

    public void removeSentRequest(Player sender) {
        sentRequests.remove(sender);
    }

    public boolean hasSentRequest(Player sender) {
        return sentRequests.get(sender) != null;
    }

    public void sendRequest(DuelsMode mode, HumanEntity sender, Player receiver) {
        DuelRequest request = new DuelRequest(mode, sender, receiver);
        sentRequests.putIfAbsent(sender, request);

        sender.sendMessage(Prefixes.DUELS + Msgs.duelRequestSent());
        receiver.sendMessage(Prefixes.DUELS + Msgs.duelRequest(sender.getName(), mode.arenaName));

        TextComponent accept = new TextComponent(ChatColor.GREEN + "[Accept]");
        String acceptCmd = "/accept " + sender.getName();
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(acceptCmd)));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, acceptCmd));

        TextComponent decline = new TextComponent(ChatColor.RED + "[Decline]");
        String declineCmd = "/decline " + sender.getName();
        decline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(declineCmd)));
        decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, declineCmd));

        receiver.sendMessage(new TextComponent(Prefixes.DUELS), accept, new TextComponent(" "), decline);

        getPendingRequests(receiver).add(request);
    }

    public boolean accept(Player sender, Player receiver) {
        DuelRequest request = getPendingRequest(sender, receiver);
        if (request == null) return false;

        if (!request.mode.isAvailable()) {
            sender.sendMessage(Prefixes.DUELS + Msgs.noAvailableArenas());
            receiver.sendMessage(Prefixes.DUELS + Msgs.noAvailableArenas());
        } else {
            Duel arena = request.mode.get();
            arena.start(sender, receiver);
        }

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

    public boolean cancelRequest(Player sender) {
        return sentRequests.remove(sender) != null;
    }

    private void removePendingRequest(Player sender, Player receiver) {
        getPendingRequests(receiver).removeIf(duelRequest -> duelRequest.sender == sender);
    }

    private DuelRequest getPendingRequest(Player sender, Player receiver) {
        List<DuelRequest> requests = getPendingRequests(receiver);

        for (DuelRequest request : requests) {
            if (request.sender == sender) return request;
        }

        return null;
    }

    private List<DuelRequest> getPendingRequests(Player receiver) {
        return pendingRequests.computeIfAbsent(receiver, player1 -> new ArrayList<>(1));
    }
}
