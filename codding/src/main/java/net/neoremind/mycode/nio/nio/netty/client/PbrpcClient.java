package net.neoremind.mycode.nio.nio.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

import net.neoremind.mycode.nio.nio.netty.protocol.PbrpcMsg;
import net.neoremind.mycode.nio.nio.netty.serializer.PbrpcMessageDeserializer;
import net.neoremind.mycode.nio.nio.netty.serializer.PbrpcMessageSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PbrpcClient {

    private static final Logger LOG = LoggerFactory.getLogger(PbrpcClient.class);

    private Bootstrap bootstrap;
    private NioEventLoopGroup eventLoopGroup;
    private Channel channel;

    public PbrpcClient() {
        bootstrap = new Bootstrap();
        ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel aCh) throws Exception {
                aCh.pipeline().addLast(new PbrpcMessageSerializer());
                aCh.pipeline().addLast(new PbrpcMessageDeserializer());
                aCh.pipeline().addLast(new PbrpcClientHandler());
            }
        };
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(initializer);
    }

    public void connect(String ip, int port) {
        try {
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port)).sync();
            channel = future.channel();
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        LOG.info("Connection established");
                    } else {
                        LOG.warn("Connection attempt failed");
                        channelFuture.cause().printStackTrace();
                    }
                }
            });
        } catch (InterruptedException e) {
            LOG.error("Failed to connect due to " + e.getMessage(), e);
        }
    }

    public void doTransport(PbrpcMsg msg) throws Exception {
        if (channel != null) {
            LOG.info("Sending msg " + msg);
            channel.writeAndFlush(msg);
            LOG.info("Send msg " + msg + " done");
        } else {
            LOG.error("Socket channel is not well established, so failed to transport");
        }
    }

    public void shutdown() {
        try {
            eventLoopGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            LOG.error("Interrupted while waiting for timeout tasks termination", e);
        }
    }

}
