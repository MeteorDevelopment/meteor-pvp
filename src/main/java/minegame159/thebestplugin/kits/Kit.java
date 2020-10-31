package minegame159.thebestplugin.kits;

import minegame159.thebestplugin.utils.ISerializable;
import minegame159.thebestplugin.utils.NBT;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Kit implements ISerializable<CompoundTag> {
    public String name;
    public UUID author;
    public boolean isPublic;

    public ItemStack[] items;

    public Kit(String name, Player player) {
        this.name = name;
        this.author = player.getUniqueId();
        this.items = new ItemStack[42];

        for (int i = 0; i < items.length; i++) {
            ItemStack itemStack = i == 41 ? player.getInventory().getItemInOffHand() : player.getInventory().getContents()[i];
            if (itemStack != null && itemStack.getType() != Material.AIR) items[i] = new ItemStack(itemStack);
        }
    }

    public Kit(CompoundTag tag) {
        fromTag(tag);
    }

    public void apply(HumanEntity player) {
        player.getInventory().clear();

        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                if (i == 41) player.getInventory().setItemInOffHand(new ItemStack(items[i]));
                else player.getInventory().setItem(i, new ItemStack(items[i]));
            }
        }
    }

    // Serialization

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        tag.putString("name", name);
        tag.putString("author", author.toString());
        tag.putBoolean("isPublic", isPublic);

        ListTag<CompoundTag> itemsTag = new ListTag<>(CompoundTag.class);
        for (int i = 0; i < items.length; i++) {
            ItemStack itemStack = items[i];
            if (itemStack == null) continue;

            CompoundTag t = new CompoundTag();

            t.putInt("slot", i);
            t.put("items", NBT.fromBytes(itemStack.serializeAsBytes()));

            itemsTag.add(t);
        }
        tag.put("items", itemsTag);

        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        name = tag.getString("name");
        author = UUID.fromString(tag.getString("author"));
        isPublic = tag.getBoolean("isPublic");

        items = new ItemStack[42];
        for (Tag<?> t : tag.getListTag("items")) {
            CompoundTag c = (CompoundTag) t;
            items[c.getInt("slot")] = ItemStack.deserializeBytes(NBT.toBytes(c.getCompoundTag("items")));
        }
    }
}
