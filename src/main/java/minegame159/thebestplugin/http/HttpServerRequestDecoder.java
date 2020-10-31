package minegame159.thebestplugin.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

public class HttpServerRequestDecoder extends MessageToMessageDecoder<HttpRequest> {
    @Override
    protected void decode(ChannelHandlerContext ctx, HttpRequest msg, List<Object> out) throws Exception {
        MyHttpRequest req = new MyHttpRequest();

        req.version = msg.protocolVersion();

        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(msg.uri());
        Map<String, List<String>> params = queryStringDecoder.parameters();
        if (!params.isEmpty()) {
            params.forEach((key, value1) -> value1.forEach(value -> req.queryParams.put(key, value)));
        }

        req.path = queryStringDecoder.path();

        out.add(req);
    }
}
