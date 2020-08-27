package minegame159.thebestplugin.duels;

import org.bukkit.entity.Player;

public class DuelRequest {
    public Player sender, receiver;
    public int timer;

    public DuelRequest(Player sender, Player receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.timer = 20 * 30;
    }
}
