package net.neoremind.mycode.nio.simple;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SimpleServer {

    public static final int DEFAULT_BUFFER_SIZE = 64;

    public static final int HEAD_LEN = 4;

    private final ConcurrentHashMap<SocketChannel, ChannelWrapper> channelWrapperMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        new SimpleServer().run();
    }

    public void run() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", 8080));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        log.info("Server started");

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            SelectionKey key = null;
            while (iter.hasNext()) {
                try {
                    key = iter.next();
                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            register(selector, serverSocket);
                        }
                        if (key.isReadable()) {
                            handle(key);
                        }
                    }
                    iter.remove();
                } catch (Exception e) {
                    log.info(e.getMessage(), e);
                    if (key != null) {
                        key.cancel();
                        key.channel().close();
                    }
                }
            }
        }
    }

    private void handle(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ChannelWrapper channelWrapper = channelWrapperMap.get(channel);
        if (channelWrapper == null) {
            throw new IllegalStateException("No channel wrapper found!");
        }
        ByteBuffer readBuffer = channelWrapper.getReadBuffer();
        //                   pos    limit
        // +---------------------------+
        // |                    |      |
        // +---------------------------+
        //                     not enough to hold a header
        //
        if (readBuffer.remaining() < HEAD_LEN) {
            readBuffer.position(0);
            channelWrapper.setLastPos(0);
            // log.warn("reset buffer due to cannot hold a header");
        }
        int numBytesRead = channel.read(readBuffer);
        if (readBuffer.position() == channelWrapper.lastPos && numBytesRead < HEAD_LEN) {
            // log.warn("Header hasn't been read completely... only get " + numBytesRead + " " + readBuffer);
            if (numBytesRead < 0) {
                // remote close, so close channel in server-side
                key.cancel();
                channel.close();
                channelWrapperMap.remove(channel);
                log.warn("Channel reach end " + key.channel());
            }
            return;
        }
        //        lastPos  <- pos
        // +---------------------------+
        // |      |             |      |
        // +---------------------------+
        //  make read buffer position go back to where it reads last time
        //  because this time we already read something out of kernel buffer,
        //  so buffer proceeds.
        readBuffer.position(channelWrapper.lastPos);
        int bodyLen = readBuffer.getInt();
        if (bodyLen > DEFAULT_BUFFER_SIZE - HEAD_LEN) {
            throw new IllegalStateException("Cannot read body larger than limit, actual " + bodyLen);
        }
        int remaining = readBuffer.remaining();
        if (remaining < bodyLen) {
            //               <- pos
            // +---------------------------+
            // |      |  HEADER     |      |
            // +---------------------------+
            //  make read buffer position go back of header length since we will re-read next time.
            //
            // +---------------------------+
            // |  HEADER     |      |      |
            // +---------------------------+
            //  compact reader buffer to the beginning to accommodate more body
            readBuffer.position(readBuffer.position() - HEAD_LEN);
            readBuffer.compact();
            channelWrapper.setLastPos(0);
            // log.warn("Body hasn't been read completely... {}, need {} but got {}", readBuffer, bodyLen, remaining);
            return;
        }
        byte[] requestBody = new byte[bodyLen];
        readBuffer.get(requestBody);
        channelWrapper.incPos(HEAD_LEN + bodyLen);

        doBusinessLogic(channel, requestBody);
    }

    private void doBusinessLogic(SocketChannel channel, byte[] requestBody) throws IOException {
        String str = new String(requestBody, "UTF-8");
        str = str.toUpperCase();
        byte[] response = str.getBytes(StandardCharsets.UTF_8);
        send(channel, response);
    }

    private void send(SocketChannel channel, byte[] response) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(response);
        channel.write(buffer);
    }

    private void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel channel = serverSocket.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        ChannelWrapper channelWrapper = new ChannelWrapper();
        channelWrapper.setChannel(channel);
        channelWrapper.setReadBuffer(ByteBuffer.allocate(DEFAULT_BUFFER_SIZE));
        channelWrapperMap.put(channel, channelWrapper);
        log.info("Connection accepted: {}", channel.getRemoteAddress());
    }

    @Data
    static class ChannelWrapper {
        SocketChannel channel;
        ByteBuffer readBuffer;
        boolean partialRead = false;
        int lastPos = 0;

        public void incPos(int delta) {
            lastPos += delta;
        }
    }
}
