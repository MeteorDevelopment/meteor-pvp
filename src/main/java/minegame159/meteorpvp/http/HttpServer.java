package minegame159.meteorpvp.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;

import java.util.HashMap;
import java.util.Map;

public class HttpServer {
    private final EventLoopGroup group;

    private final Map<String, HttpHandler> handlers = new HashMap<>();

    public HttpServer(int port) {
        group = new NioEventLoopGroup(1);

        ServerBootstrap b = new ServerBootstrap();
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new HttpServerCodec(),
                                new HttpServerExpectContinueHandler(),
                                new HttpServerRequestDecoder(),
                                new HttpServerResponseEncoder(),
                                new Handler()
                        );
                    }
                });

        try {
            b.bind(port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void handler(String path, HttpHandler handler) {
        handlers.put(path, handler);
    }

    public void close() {
        group.shutdownGracefully();
    }

    private class Handler extends SimpleChannelInboundHandler<MyHttpRequest> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, MyHttpRequest msg) throws Exception {
            MyHttpResponse res = new MyHttpResponse(msg);

            HttpHandler handler = handlers.get(msg.path);
            if (handler != null) handler.handle(msg, res);
            else res.statusCode = 404;

            ctx.writeAndFlush(res);
        }
    }
}
