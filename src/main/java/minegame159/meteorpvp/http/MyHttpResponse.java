package minegame159.meteorpvp.http;

import io.netty.handler.codec.http.HttpVersion;

import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;

public class MyHttpResponse {
    public HttpVersion version;

    public int statusCode = 200;
    public String body = null;

    public Map<String, String> headers = new HashMap<>();

    public MyHttpResponse(MyHttpRequest req) {
        version = req.version;

        headers.put(CONNECTION.toString(), CLOSE.toString());
        headers.put(CONTENT_TYPE.toString(), TEXT_PLAIN.toString() + "; charset=UTF-8");
    }
}
