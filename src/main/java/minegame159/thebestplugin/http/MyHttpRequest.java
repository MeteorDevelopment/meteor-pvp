package minegame159.thebestplugin.http;

import io.netty.handler.codec.http.HttpVersion;

import java.util.HashMap;
import java.util.Map;

public class MyHttpRequest {
    public HttpVersion version;

    public String path;

    public final Map<String, String> queryParams = new HashMap<>();
}
