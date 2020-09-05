package minegame159.thebestplugin.json;

import com.google.gson.*;
import minegame159.thebestplugin.Stats;

import java.lang.reflect.Type;

public class StatsSerializer implements JsonSerializer<Stats>, JsonDeserializer<Stats> {
    @Override
    public JsonElement serialize(Stats src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty("deaths", src.deaths);
        object.addProperty("blownCrystals", src.blownCrystals);
        object.addProperty("poppedTotems", src.poppedTotems);
        object.addProperty("playedDuels", src.playedDuels);

        return object;
    }


    @Override
    public Stats deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        Stats.INSTANCE.deaths = object.get("deaths").getAsInt();
        Stats.INSTANCE.blownCrystals = object.get("blownCrystals").getAsInt();
        Stats.INSTANCE.poppedTotems = object.get("poppedTotems").getAsInt();
        Stats.INSTANCE.playedDuels = object.get("playedDuels").getAsInt();

        return null;
    }
}
