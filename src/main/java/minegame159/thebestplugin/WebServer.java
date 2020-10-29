package minegame159.thebestplugin;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import spark.Request;

import java.util.UUID;
import java.util.function.Consumer;

import static spark.Spark.*;

public class WebServer {
    private static final String TOKEN = "M2LoLF4iZSAJt25kF84l";

    public static void enable() {
        port(8115);

        before((request, response) -> {
            String token = request.queryParams("token");
            if (token == null || !token.equals(TOKEN)) halt(401);
        });

        post("/adddonator", (request, response) -> {
            UUID uuid = getUUID(request);
            modifyUser(uuid, user -> user.data().add(InheritanceNode.builder(getDonatorGroup()).build()));
            return "";
        });

        post("/removedonator", (request, response) -> {
            UUID uuid = getUUID(request);
            modifyUser(uuid, user -> user.data().remove(InheritanceNode.builder(getDonatorGroup()).build()));
            return "";
        });
    }

    private static void modifyUser(UUID uuid, Consumer<? super User> action) {
        LuckPermsProvider.get().getUserManager().modifyUser(uuid, action);
    }

    private static Group getDonatorGroup() {
        return LuckPermsProvider.get().getGroupManager().loadGroup("donator").join().get();
    }

    private static UUID getUUID(Request request) {
        String uuid = request.queryParams("uuid");
        if (uuid == null) halt(400);
        return UUID.fromString(uuid);
    }

    public static void disable() {
        stop();
        awaitStop();
    }
}
