package minegame159.thebestplugin;

import minegame159.thebestplugin.http.HttpServer;
import minegame159.thebestplugin.http.MyHttpRequest;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;

import java.util.UUID;
import java.util.function.Consumer;

public class WebServer {
    private static final String TOKEN = "M2LoLF4iZSAJt25kF84l";

    private static HttpServer server;

    public static void enable() {
        if (server != null) return;

        server = new HttpServer(8115);

        server.handler("/adddonator", (req, res) -> {
            if (auth(req)) {
                res.statusCode = 401;
                return;
            }

            UUID uuid = getUUID(req);
            if (uuid == null) res.statusCode = 401;

            modifyUser(uuid, user -> user.data().add(InheritanceNode.builder(getDonatorGroup()).build()));
        });

        server.handler("/removedonator", (req, res) -> {
            if (auth(req)) {
                res.statusCode = 401;
                return;
            }

            UUID uuid = getUUID(req);
            if (uuid == null) res.statusCode = 401;

            modifyUser(uuid, user -> user.data().remove(InheritanceNode.builder(getDonatorGroup()).build()));
        });
    }

    private static boolean auth(MyHttpRequest req) {
        String token = req.queryParams.get("token");
        return token == null || !token.equals(TOKEN);
    }

    private static void modifyUser(UUID uuid, Consumer<? super User> action) {
        LuckPermsProvider.get().getUserManager().modifyUser(uuid, action);
    }

    private static Group getDonatorGroup() {
        return LuckPermsProvider.get().getGroupManager().loadGroup("donator").join().get();
    }

    private static UUID getUUID(MyHttpRequest req) {
        String uuid = req.queryParams.get("uuid");
        return uuid == null ? null : UUID.fromString(uuid);
    }

    public static void disable() {
        if (server != null) {
            server.close();
            server = null;
        }
    }
}
