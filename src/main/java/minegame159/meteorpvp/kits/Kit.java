package minegame159.meteorpvp.kits;

import minegame159.meteorpvp.utils.ISerializable;
import minegame159.meteorpvp.utils.NBT;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

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

            if (itemStack != null && itemStack.getType() != Material.AIR) {
                boolean enchantsOk = true;
                for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
                    if (itemStack.getEnchantments().get(enchantment) > enchantment.getMaxLevel()) {
                        enchantsOk = false;
                        break;
                    }
                }

                if ((itemStack.getMaxStackSize() != -1 && itemStack.getAmount() > itemStack.getMaxStackSize()) || !enchantsOk) {
                    if (i == 41) player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                    else player.getInventory().setItem(i, new ItemStack(Material.AIR));
                } else {
                    items[i] = new ItemStack(itemStack);
                }
            }
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
            itemStackToNbt(t, itemStack);

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
            items[c.getInt("slot")] = itemStackFromNbt(c);
        }
    }

    private void itemStackToNbt(CompoundTag tag, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        tag.putString("id", itemStack.getType().name());
        tag.putInt("count", itemStack.getAmount());

        // Enchantements
        if (itemStack.getEnchantments().size() > 0) {
            ListTag<CompoundTag> e = new ListTag<>(CompoundTag.class);

            for (Enchantment en : itemStack.getEnchantments().keySet()) {
                CompoundTag t = new CompoundTag();

                t.put("id", NBT.toTag(en.getKey()));
                t.putInt("level", itemStack.getEnchantments().get(en));

                e.add(t);
            }

            tag.put("enchantemets", e);
        }

        // Tipped Arrows / Potions
        if (itemMeta instanceof PotionMeta) {
            PotionMeta meta = (PotionMeta) itemMeta;

            PotionData data = meta.getBasePotionData();
            tag.putString("potion", data.getType().name());
            tag.putBoolean("extended", data.isExtended());
            tag.putBoolean("upgraded", data.isUpgraded());

            if (meta.getCustomEffects().size() > 0) {
                ListTag<CompoundTag> p = new ListTag<>(CompoundTag.class);

                for (PotionEffect potionEffect : meta.getCustomEffects()) {
                    CompoundTag t = new CompoundTag();

                    t.putString("id", potionEffect.getType().getName());
                    t.putInt("duration", potionEffect.getDuration());
                    t.putInt("amplifier", potionEffect.getAmplifier());

                    p.add(t);
                }

                tag.put("effects", p);
            }
        }

        // Shulker Boxes
        if (itemMeta instanceof BlockStateMeta) {
            BlockStateMeta meta = (BlockStateMeta) itemMeta;
            BlockState blockState = meta.getBlockState();

            if (blockState instanceof ShulkerBox) {
                ShulkerBox state = (ShulkerBox) blockState;
                ListTag<CompoundTag> l = new ListTag<>(CompoundTag.class);

                for (int i = 0; i < state.getInventory().getSize(); i++) {
                    ItemStack item = state.getInventory().getItem(i);
                    if (item == null || item.getType() == Material.AIR) continue;

                    CompoundTag t = new CompoundTag();

                    t.putInt("slot", i);
                    itemStackToNbt(t, item);

                    l.add(t);
                }

                tag.put("items", l);
            }
        }
    }

    private ItemStack itemStackFromNbt(CompoundTag tag) {
        Material type = Material.getMaterial(tag.getString("id"));
        if (type == null) return null;

        ItemStack itemStack = new ItemStack(type, tag.getInt("count"));

        // Enchantements
        ListTag<?> e = tag.getListTag("enchantemets");
        if (e != null) {
            for (Tag<?> tt : e) {
                CompoundTag t = (CompoundTag) tt;

                Enchantment en = Enchantment.getByKey(NBT.toKey(t.getListTag("id")));
                if (en != null) itemStack.addEnchantment(en, t.getInt("level"));
            }
        }

        // Tipped Arrows / Potions
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta instanceof PotionMeta) {
            PotionMeta meta = (PotionMeta) itemMeta;

            try {
                PotionType ptype = PotionType.valueOf(tag.getString("potion"));
                meta.setBasePotionData(new PotionData(ptype, tag.getBoolean("extended"), tag.getBoolean("upgraded")));
            } catch (IllegalArgumentException ignored) {}

            ListTag<?> p = tag.getListTag("effects");
            if (p != null) {
                for (Tag<?> tt : p) {
                    CompoundTag t = (CompoundTag) tt;

                    PotionEffectType petype = PotionEffectType.getByName(t.getString("id"));
                    if (petype != null) {
                        meta.addCustomEffect(new PotionEffect(petype, t.getInt("duration"), t.getInt("amplifier")), true);
                    }
                }
            }
        }

        // Shulker Boxes
        else if (itemMeta instanceof BlockStateMeta) {
            BlockStateMeta meta = (BlockStateMeta) itemMeta;
            BlockState blockState = meta.getBlockState();

            if (blockState instanceof ShulkerBox) {
                ShulkerBox state = (ShulkerBox) blockState;
                ListTag<?> l = tag.getListTag("items");

                for (Tag<?> tt : l) {
                    CompoundTag t = (CompoundTag) tt;

                    state.getInventory().setItem(t.getInt("slot"), itemStackFromNbt(t));
                }

                meta.setBlockState(blockState);
            }
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
