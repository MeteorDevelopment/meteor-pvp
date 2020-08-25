package minegame159.thebestplugin.json;

import com.google.gson.*;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.Map;

public class ItemStackSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        System.out.println("hi4");
        JsonObject object = new JsonObject();

        object.addProperty("id", src.getType().toString());
        object.addProperty("count", src.getAmount());

        JsonArray enchantmentsArray = new JsonArray();
        Map<Enchantment, Integer> enchantments = src.getEnchantments();
        for (Enchantment enchantment : enchantments.keySet()) {
            JsonObject o = new JsonObject();

            o.add("id", context.serialize(enchantment.getKey()));
            o.addProperty("level", enchantments.get(enchantment));

            enchantmentsArray.add(o);
        }
        object.add("enchantments", enchantmentsArray);

        return object;
    }

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String id = object.get("id").getAsString();
        int count = object.get("count").getAsInt();

        ItemStack itemStack = new ItemStack(Material.getMaterial(id), count);

        JsonElement enchE = object.get("enchantments");
        if (enchE != null) {
            for (JsonElement e : enchE.getAsJsonArray()) {
                JsonObject o = e.getAsJsonObject();

                NamespacedKey id2 = context.deserialize(o.get("id"), NamespacedKey.class);
                int level = o.get("level").getAsInt();

                itemStack.addEnchantment(Enchantment.getByKey(id2), level);
            }
        }

        return itemStack;
    }
}
