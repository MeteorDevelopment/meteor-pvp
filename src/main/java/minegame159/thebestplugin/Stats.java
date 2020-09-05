package minegame159.thebestplugin;

import java.io.*;

public class Stats {
    public static final Stats INSTANCE = new Stats();
    public static File FILE;

    public int deaths = 0;
    public int blownCrystals = 0;
    public int poppedTotems = 0;
    public int playedDuels = 0;

    public boolean dirty = false;
    public int saveTimer = 0;

    private Stats() {}

    public void init() {
        FILE = new File(TheBestPlugin.CONFIG_FOLDER, "stats.json");

        deaths = 0;
        blownCrystals = 0;
        poppedTotems = 0;
        playedDuels = 0;
        dirty = false;
        saveTimer = 0;

        if (FILE.exists()) {
            try {
                TheBestPlugin.GSON.fromJson(new FileReader(FILE), Stats.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void changed() {
        if (!dirty) {
            dirty = true;
            saveTimer = 20 * 60;
        }
    }

    public void save() {
        try {
            FileWriter writer = new FileWriter(FILE);
            TheBestPlugin.GSON.toJson(this, writer);
            writer.close();

            dirty = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
