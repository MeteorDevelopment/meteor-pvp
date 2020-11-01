package minegame159.thebestplugin.duels;

import org.bukkit.entity.HumanEntity;

public class DuelRequest {
    public DuelsMode mode;
    public HumanEntity sender, receiver;
    public int timer;

    public DuelRequest(DuelsMode mode, HumanEntity sender, HumanEntity receiver) {
        this.mode = mode;
        this.sender = sender;
        this.receiver = receiver;
        this.timer = 20 * 30;
    }
}
