package net.neoremind.mycode.nio.simple.server;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import net.neoremind.mycode.nio.simple.NioHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class NioServer {

    private final String host;

    private final int port;

    private final int readBufferSize;

    private final NioHandler nioHandler;

    public NioServer(String host, int port, int readBufferSize) {
        this.host = host;
        this.port = port;
        this.readBufferSize = readBufferSize;
        Preconditions.checkArgument(readBufferSize > NioHandler.HEAD_LEN);
        int maxBodyLen = readBufferSize - NioHandler.HEAD_LEN;
        ServerEchoInputHandler inputHandler = new ServerEchoInputHandler();
        this.nioHandler = new NioHandler(maxBodyLen, inputHandler);
    }

    public static void main(String[] args) throws IOException {
        new NioServer("localhost", 8080, 1024).start();
    }

    public void run(String[] args) throws IOException {
        String host = args[1];
        int port = Integer.parseInt(args[2]);
        int readBufferSize = Integer.parseInt(args[3]);
        new NioServer(host, port, readBufferSize).start();
    }

    public void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(host, port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        log.info("Server started at " + port);

        // TODO shutdown gracefully
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
                            nioHandler.handle(key);
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

    private void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel channel = serverSocket.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        ByteBuffer readBuffer = ByteBuffer.allocateDirect(readBufferSize);
        nioHandler.initContext(channel, readBuffer);
        log.info("Connection accepted: {}", channel.getRemoteAddress());
    }
}
