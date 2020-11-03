package minegame159.meteorpvp.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

public class HttpServerResponseEncoder extends MessageToMessageEncoder<MyHttpResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MyHttpResponse msg, List<Object> out) throws Exception {
        ByteBuf body = msg.body == null ? Unpooled.EMPTY_BUFFER : Unpooled.copiedBuffer(msg.body, StandardCharsets.UTF_8);

        DefaultFullHttpResponse res = new DefaultFullHttpResponse(msg.version, HttpResponseStatus.valueOf(msg.statusCode), body);

        msg.headers.forEach((key, value) -> res.headers().set(key, value));
        res.headers().set(CONTENT_LENGTH, res.content().readableBytes());

        ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
    }
}
