package minegame159.thebestplugin.json;

import com.google.gson.*;
import minegame159.thebestplugin.Kit;
import minegame159.thebestplugin.Kits;
import org.bukkit.Bukkit;

import java.lang.reflect.Type;

public class KitsSerializer implements JsonSerializer<Kits>, JsonDeserializer<Kits> {
    @Override
    public JsonElement serialize(Kits src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray array = new JsonArray();

        for (Kit kit : src.getKits()) {
            array.add(context.serialize(kit));
        }

        return array;
    }

    @Override
    public Kits deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        for (JsonElement e : json.getAsJsonArray()) {
            Kit kit = context.deserialize(e, Kit.class);
            Kits.INSTANCE.addKit(kit, false);
        }

        return null;
    }
}
