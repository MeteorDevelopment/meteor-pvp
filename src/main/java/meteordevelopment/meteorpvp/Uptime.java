package meteordevelopment.meteorpvp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Uptime {
    public static void onEnable() {
        if (Config.UPTIME_URL == null) {
            System.out.println("Meteor Pvp: Uptime URL not configured, uptime requests will not be made");
            return;
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(MeteorPvp.INSTANCE, () -> {
            String url = Config.UPTIME_URL;
            if (url.endsWith("ping=")) url += getAveragePlayerPing();

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(url))
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();

                MeteorPvp.HTTP.sendAsync(request, HttpResponse.BodyHandlers.discarding());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }, 0, 60 * 20);
    }

    private static int getAveragePlayerPing() {
        int total = 0;
        int count = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            total += player.getPing();
            count++;
        }

        return count != 0 ? (total / count) : 0;
    }
}
