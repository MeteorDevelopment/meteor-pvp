package minegame159.thebestplugin.utils;

public class Utils {
    public static int getUsedRamMb() {
        return (int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
    }

    public static int getMaxRamMb() {
        return (int) (Runtime.getRuntime().maxMemory() / (1024 * 1024));
    }
}
