package meteordevelopment.meteorpvp.listeners;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import meteordevelopment.meteorpvp.Config;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.util.Tristate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class DonatorListener implements Listener {
    private final HttpClient client = HttpClient.newHttpClient();

    private Node donatorGroup;

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://meteorclient.com/api/account/getByUuid?uuid=" + event.getPlayer().getUniqueId()))
                    .header("Authorization", Config.SERVER_TOKEN)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()).thenAcceptAsync(http -> {
                JsonObject res = JsonParser.parseReader(new InputStreamReader(http.body())).getAsJsonObject();
                boolean donator = res.get("donator").getAsBoolean();

                User user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(event.getPlayer());
                boolean hasDonator = user.data().contains(getDonatorGroup(), NodeEqualityPredicate.EXACT) == Tristate.TRUE;

                if (donator != hasDonator) {
                    LuckPermsProvider.get().getUserManager().modifyUser(event.getPlayer().getUniqueId(), usr -> {
                        if (donator) usr.data().add(getDonatorGroup());
                        else usr.data().remove(getDonatorGroup());
                    });
                }
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Node getDonatorGroup() {
        if (donatorGroup == null) {
            donatorGroup = InheritanceNode.builder(Objects.requireNonNull(LuckPermsProvider.get().getGroupManager().getGroup("donator"))).build();
        }

        return donatorGroup;
    }
}
