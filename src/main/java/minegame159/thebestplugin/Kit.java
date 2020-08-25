package minegame159.thebestplugin;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Kit {
    public final String name;
    public final UUID author;
    public final ItemStack[] items;

    public Kit(String name, Player player) {
        this.name = name;
        this.author = player.getUniqueId();
        this.items = new ItemStack[41];

        System.arraycopy(player.getInventory().getContents(), 0, this.items, 0, 41);
    }

    public Kit(String name, UUID author, ItemStack[] items) {
        this.name = name;
        this.author = author;
        this.items = items;
    }

    public void apply(Player player) {
        player.getInventory().clear();

        for (int i = 0; i < items.length; i++) {
            player.getInventory().setItem(i, items[i]);
        }
    }
}
