package minegame159.meteorpvp.utils;

import org.bukkit.entity.Entity;

public class EntityTimer {
    public final Entity entity;
    public final int timer;

    public EntityTimer(Entity entity, int timer) {
        this.entity = entity;
        this.timer = timer;
    }
}
