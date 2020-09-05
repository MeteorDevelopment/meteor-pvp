package minegame159.thebestplugin;

import org.bukkit.Material;
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

        for (int i = 0; i < items.length; i++) {
            ItemStack itemStack = player.getInventory().getContents()[i];
            if (itemStack != null) items[i] = new ItemStack(itemStack);
        }
    }

    public Kit(String name, UUID author, ItemStack[] items) {
        this.name = name;
        this.author = author;
        this.items = items;
    }

    public void apply(Player player) {
        player.getInventory().clear();

        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) player.getInventory().setItem(i, new ItemStack(items[i]));
        }
    }

    public int count(Material item) {
        int count = 0;

        for (ItemStack itemStack : items) {
            if (itemStack != null && itemStack.getType() == item) count += itemStack.getAmount();
        }

        return count;
    }

    public int countBeds() {
        int count = 0;

        for (ItemStack itemStack : items) {
            if (itemStack != null && itemStack.getType().getKey().getKey().endsWith("_bed")) {
                count += itemStack.getAmount();
            }
        }

        return count;
    }
}
