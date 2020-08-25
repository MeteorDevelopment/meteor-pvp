package minegame159.thebestplugin.json;

import com.google.gson.*;
import minegame159.thebestplugin.Kit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.UUID;

public class KitSerializer implements JsonSerializer<Kit>, JsonDeserializer<Kit> {
    @Override
    public JsonElement serialize(Kit src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("name", src.name);
        object.add("author", context.serialize(src.author));

        JsonArray items = new JsonArray();
        for (int i = 0; i < src.items.length; i++) {
            ItemStack itemStack = src.items[i];
            if (itemStack == null) continue;

            JsonObject item = new JsonObject();
            item.addProperty("slot", i);
            item.addProperty("id", itemStack.getType().toString());
            item.addProperty("count", itemStack.getAmount());
            items.add(item);
        }
        object.add("items", items);

        return object;
    }

    @Override
    public Kit deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String name = object.get("name").getAsString();
        UUID author = context.deserialize(object.get("author"), UUID.class);
        ItemStack[] items = new ItemStack[41];

        for (JsonElement e : object.get("items").getAsJsonArray()) {
            JsonObject item = e.getAsJsonObject();

            int slot = item.get("slot").getAsInt();
            String id = item.get("id").getAsString();
            int count = item.get("count").getAsInt();

            items[slot] = new ItemStack(Material.getMaterial(id), count);
        }

        return new Kit(name, author, items);
    }
}
