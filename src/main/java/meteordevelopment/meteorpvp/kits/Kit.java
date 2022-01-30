package meteordevelopment.meteorpvp.kits;

import meteordevelopment.meteorpvp.nbt.ISerializable;
import meteordevelopment.meteorpvp.nbt.NBT;
import meteordevelopment.meteorpvp.nbt.NbtTag;
import meteordevelopment.meteorpvp.nbt.NbtWriter;
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

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Kit implements ISerializable<CompoundTag> {
    public static final List<Material> KITCREATOR_ITEMS = List.of(
            Material.NETHERITE_HELMET,
            Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_LEGGINGS,
            Material.NETHERITE_BOOTS,

            Material.DIAMOND_HELMET,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_BOOTS,

            Material.NETHERITE_SWORD,
            Material.NETHERITE_PICKAXE,
            Material.NETHERITE_AXE,

            Material.DIAMOND_SWORD,
            Material.DIAMOND_PICKAXE,
            Material.DIAMOND_AXE,

            Material.SHIELD,
            Material.TRIDENT,

            Material.ENCHANTED_GOLDEN_APPLE,
            Material.GOLDEN_APPLE,
            Material.END_CRYSTAL,
            Material.TOTEM_OF_UNDYING,
            Material.CHORUS_FRUIT,
            Material.ENDER_PEARL,
            Material.ANVIL,
            Material.OAK_TRAPDOOR,
            Material.GOLDEN_CARROT,
            Material.BREAD,
            Material.EXPERIENCE_BOTTLE,
            Material.OBSIDIAN,
            Material.STRING,
            Material.ENDER_CHEST,
            Material.CRIMSON_PRESSURE_PLATE,
            Material.CRIMSON_BUTTON,

            Material.BOW,
            Material.CROSSBOW,
            Material.POTION,
            Material.ARROW,
            Material.TIPPED_ARROW,
            Material.GLOWSTONE,
            Material.RESPAWN_ANCHOR,
            Material.PURPLE_BED,
            Material.PISTON,
            Material.LEVER,
            Material.PURPLE_WOOL,
            Material.OAK_PLANKS,
            Material.CRAFTING_TABLE,
            Material.REDSTONE_BLOCK,

            Material.CRYING_OBSIDIAN,
            Material.NETHERITE_BLOCK,
            Material.CAKE,
            Material.FISHING_ROD,
            Material.WOODEN_HOE,
            Material.STICK
    );

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

            if (validItem(itemStack)) {
                items[i] = new ItemStack(itemStack);
            } else {
                if (i == 41) player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                else player.getInventory().setItem(i, new ItemStack(Material.AIR));
            }
        }
    }

    public Kit(CompoundTag tag) {
        fromTag(tag);
    }

    private boolean validItem(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (!KITCREATOR_ITEMS.contains(itemStack.getType())) return false;

        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            if (itemStack.getEnchantments().get(enchantment) > enchantment.getMaxLevel()) {
                return false;
            }
        }

        return itemStack.getMaxStackSize() == -1 || itemStack.getAmount() <= itemStack.getMaxStackSize();
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
    public void toTag(NbtWriter nbt) {
        nbt.writeCompoundStart();

        nbt.writeString("name", name);
        nbt.writeString("author", author.toString());
        nbt.writeBool("isPublic", isPublic);

        int itemCount = 0;
        for (ItemStack itemStack : items) {
            if (itemStack != null) itemCount++;
        }

        nbt.writeList("items", NbtTag.Compound, itemCount);
        for (int i = 0; i < items.length; i++) {
            ItemStack itemStack = items[i];
            if (itemStack == null) continue;

            nbt.writeCompoundStart();
            nbt.writeInt("slot", i);
            itemStackToNbt(nbt, itemStack);

            nbt.writeCompoundEnd();
        }

        nbt.writeCompoundEnd();
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

    private void itemStackToNbt(NbtWriter nbt, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        nbt.writeString("id", itemStack.getType().name());
        nbt.writeInt("count", itemStack.getAmount());

        // Enchantments
        Map<Enchantment, Integer> enchantments = itemStack.getEnchantments();
        if (enchantments.size() > 0) {
            nbt.writeList("enchantemets", NbtTag.Compound, enchantments.size());

            for (Enchantment en : enchantments.keySet()) {
                nbt.writeCompoundStart();

                NBT.toTag(nbt, "id", en.getKey());
                nbt.writeInt("level", enchantments.get(en));

                nbt.writeCompoundEnd();
            }
        }

        // Tipped Arrows / Potions
        if (itemMeta instanceof PotionMeta) {
            PotionMeta meta = (PotionMeta) itemMeta;

            PotionData data = meta.getBasePotionData();
            nbt.writeString("potion", data.getType().name());
            nbt.writeBool("extended", data.isExtended());
            nbt.writeBool("upgraded", data.isUpgraded());

            List<PotionEffect> effects = meta.getCustomEffects();
            if (effects.size() > 0) {
                nbt.writeList("effects", NbtTag.Compound, effects.size());

                for (PotionEffect potionEffect : effects) {
                    nbt.writeCompoundStart();

                    nbt.writeString("id", potionEffect.getType().getName());
                    nbt.writeInt("duration", potionEffect.getDuration());
                    nbt.writeInt("amplifier", potionEffect.getAmplifier());

                    nbt.writeCompoundEnd();
                }
            }
        }

        // Shulker Boxes
        if (itemMeta instanceof BlockStateMeta) {
            BlockStateMeta meta = (BlockStateMeta) itemMeta;
            BlockState blockState = meta.getBlockState();

            if (blockState instanceof ShulkerBox) {
                ShulkerBox state = (ShulkerBox) blockState;

                int itemCount = 0;
                for (ItemStack stack : state.getInventory()) {
                    if (stack != null && stack.getType() != Material.AIR) itemCount++;
                }

                nbt.writeList("items", NbtTag.Compound, itemCount);

                for (int i = 0; i < state.getInventory().getSize(); i++) {
                    ItemStack item = state.getInventory().getItem(i);
                    if (item == null || item.getType() == Material.AIR) continue;

                    nbt.writeCompoundStart();

                    nbt.writeInt("slot", i);
                    itemStackToNbt(nbt, item);

                    nbt.writeCompoundEnd();
                }
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
            } catch (IllegalArgumentException ignored) {
            }

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
        else if (itemMeta instanceof BlockStateMeta meta) {
            BlockState blockState = meta.getBlockState();

            if (blockState instanceof ShulkerBox state) {
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
