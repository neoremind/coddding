package net.neoremind.mycode.nio.nio.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.neoremind.mycode.nio.nio.netty.serializer.PbrpcMessageDeserializer;
import net.neoremind.mycode.nio.nio.netty.serializer.PbrpcMessageSerializer;

public class PbrpcServer {

    private static final Logger LOG = LoggerFactory.getLogger(PbrpcServer.class);

    private int port;

    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public PbrpcServer(int port) {
        this.port = port;
        bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("deser", new PbrpcMessageDeserializer());
                ch.pipeline().addLast(new PbrpcServerHandler());
                ch.pipeline().addLast("ser", new PbrpcMessageSerializer());
            }
        };
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024).childHandler(initializer);
        LOG.info("Pbrpc server init done");
    }

    public void start() {
        LOG.info("Pbrpc server is about to start on port " + port);
        try {
            bootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            LOG.error("Server failed to start, " + e.getMessage(), e);
        }
        LOG.info("Server started");
    }

    public void shutdown() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
