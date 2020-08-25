package minegame159.thebestplugin.json;

import com.google.gson.*;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Type;

public class NamespacedKeySerializer implements JsonSerializer<NamespacedKey>, JsonDeserializer<NamespacedKey> {
    @Override
    public JsonElement serialize(NamespacedKey src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("namespace", src.getNamespace());
        object.addProperty("key", src.getKey());

        return object;
    }

    @Override
    public NamespacedKey deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String namespace = object.get("namespace").getAsString();
        String key = object.get("key").getAsString();

        return new NamespacedKey(namespace, key);
    }
}
