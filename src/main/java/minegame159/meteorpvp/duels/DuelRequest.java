package minegame159.meteorpvp.duels;

import org.bukkit.entity.HumanEntity;

public class DuelRequest {
    public final DuelsMode mode;
    public final HumanEntity sender, receiver;
    public int timer;

    public DuelRequest(DuelsMode mode, HumanEntity sender, HumanEntity receiver) {
        this.mode = mode;
        this.sender = sender;
        this.receiver = receiver;
        this.timer = 20 * 30;
    }
}
