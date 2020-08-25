package minegame159.thebestplugin.utils;

import java.util.concurrent.TimeUnit;

public class Uptime {
    private static long startTime;

    public static void onEnable() {
        startTime = System.currentTimeMillis();
    }

    public static int getHours() {
        long uptime = System.currentTimeMillis() - startTime;
        return (int) TimeUnit.MILLISECONDS.toHours(uptime);
    }

    public static int getDays() {
        long uptime = System.currentTimeMillis() - startTime;
        return (int) TimeUnit.MILLISECONDS.toDays(uptime);
    }
}
