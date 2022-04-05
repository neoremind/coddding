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
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MultiReactorNioServer {

    private final String host;

    private final int port;

    private final int readBufferSize;

    private final SubReactor[] subReactors;

    private final AtomicInteger counter = new AtomicInteger(0);

    private volatile boolean isStop;

    public MultiReactorNioServer(String host, int port, int readBufferSize, int numOfSubReactor) {
        this.host = host;
        this.port = port;
        this.readBufferSize = readBufferSize;
        this.subReactors = new SubReactor[numOfSubReactor];
        for (int i = 0; i < numOfSubReactor; i++) {
            this.subReactors[i] = new SubReactor(i, readBufferSize);
            this.subReactors[i].start();
        }
    }

    public static void main(String[] args) throws IOException {
        new MultiReactorNioServer("localhost", 8080, 1024, 8).start();
    }

    public void run(String[] args) throws IOException {
        String host = args[1];
        int port = Integer.parseInt(args[2]);
        int readBufferSize = Integer.parseInt(args[3]);
        int numOfSubReactors = Integer.parseInt(args[4]);
        new MultiReactorNioServer(host, port, readBufferSize, numOfSubReactors).start();
    }

    public void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(host, port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        log.info("Server started at " + port);

        // TODO shutdown gracefully
        while (!isStop) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            SelectionKey key = null;
            while (iter.hasNext()) {
                try {
                    key = iter.next();
                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            int subReactorIndex = counter.getAndIncrement() % subReactors.length;
                            SubReactor subReactor = subReactors[subReactorIndex];
                            register(subReactor.selector, subReactor.nioHandler, serverSocket);
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

    public void stop() throws IOException {
        isStop = true;
        for (SubReactor subReactor : subReactors) {
            subReactor.stop();
        }
    }

    private void register(Selector selector, NioHandler nioHandler, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel channel = serverSocket.accept();
        channel.configureBlocking(false);
        ByteBuffer readBuffer = ByteBuffer.allocateDirect(readBufferSize);
        nioHandler.initContext(channel, readBuffer);
        selector.wakeup();
        channel.register(selector, SelectionKey.OP_READ);
        log.info("Connection accepted: {}", channel.getRemoteAddress());
    }

    class SubReactor {

        private final int id;

        private final Selector selector;

        private final Thread executor;

        private final NioHandler nioHandler;

        private volatile boolean isStop;

        public SubReactor(int id, int readBufferSize) {
            this.id = id;
            Preconditions.checkArgument(readBufferSize > NioHandler.HEAD_LEN);
            int maxBodyLen = readBufferSize - NioHandler.HEAD_LEN;
            ServerEchoInputHandler inputHandler = new ServerEchoInputHandler();
            this.nioHandler = new NioHandler(maxBodyLen, inputHandler);
            try {
                this.selector = Selector.open();
            } catch (IOException e) {
                throw new RuntimeException("Failed to open selector", e);
            }
            this.executor = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (!isStop) {
                            selector.select(100);
                            Set<SelectionKey> selectedKeys = selector.selectedKeys();
                            Iterator<SelectionKey> iter = selectedKeys.iterator();

                            SelectionKey key = null;
                            while (iter.hasNext()) {
                                try {
                                    key = iter.next();
                                    if (key.isValid()) {
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
                    } catch (Exception e) {
                        log.error("SubReactor-{} stops due to {}", id, e.getMessage(), e);
                    }
                }
            });
            this.executor.setName("SubReactor-" + id);
        }

        void start() {
            log.info("SubReactor-{} started", id);
            executor.start();
        }

        void stop() {
            isStop = true;
        }
    }
}
