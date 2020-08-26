package minegame159.thebestplugin;

public class Stats {
    public int kills = 0;
    public int blownCrystals = 0;
    public int poppedTotems = 0;
    public int playedDuels = 0;

    public transient boolean dirty = false;
    public transient int saveTimer = 0;

    public void changed() {
        if (!dirty) {
            dirty = true;
            saveTimer = 20 * 60;
        }
    }
}
