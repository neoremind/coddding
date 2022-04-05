package net.neoremind.mycode.nio.simple.client;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import net.neoremind.mycode.nio.simple.NioHandler;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class NioClient {

    private static final AtomicInteger ID_INDEX = new AtomicInteger(0);

    private Selector selector;

    private SocketChannel socketChannel;

    private final String host;

    private final int port;

    private final int readBufferSize;

    private final NioHandler nioHandler;

    private final CountDownLatch connectedLatch = new CountDownLatch(1);

    private static final ThreadLocal<ByteBuffer> WRITE_BUFFER = ThreadLocal.withInitial(() -> ByteBuffer.allocateDirect(2048));

    public NioClient(String host, int port, int readBufferSize) throws IOException {
        this.host = host;
        this.port = port;
        this.readBufferSize = readBufferSize;
        Preconditions.checkArgument(readBufferSize > NioHandler.HEAD_LEN);
        int maxBodyLen = readBufferSize - NioHandler.HEAD_LEN;
        ClientEchoInputHandler inputHandler = new ClientEchoInputHandler();
        this.nioHandler = new NioHandler(maxBodyLen, inputHandler);
        createSelector();
        Thread clientEpollThread = new Thread(new Runnable() {
            @Override
            public void run() {
                loop();
            }
        });
        clientEpollThread.setName("Client-" + ID_INDEX.getAndIncrement());
        clientEpollThread.start();
        try {
            connectedLatch.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.interrupted();
        }
    }

    public void createSelector() throws IOException {
        this.selector = Selector.open();
        this.socketChannel = SocketChannel.open();
        this.socketChannel.configureBlocking(false);
    }

    private void loop() {
        try {
            connect();
            while (true) {
                selector.select(100);
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = readyKeys.iterator();
                SelectionKey key = null;
                while (iter.hasNext()) {
                    try {
                        key = iter.next();
                        iter.remove();

                        if (key.isValid()) {
                            if (key.isConnectable()) {
                                if (socketChannel.finishConnect()) {
                                    ByteBuffer readBuffer = ByteBuffer.allocateDirect(readBufferSize);
                                    nioHandler.initContext(socketChannel, readBuffer);
                                    log.info("Connected to server {}", socketChannel.getLocalAddress());
                                    socketChannel.register(selector, SelectionKey.OP_READ);
                                    connectedLatch.countDown();
                                }
                            }

                            if (key.isReadable()) {
                                nioHandler.handle(key);
                            }
                        }
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
            log.error(e.getMessage(), e);
        }
    }

    public void connect() throws IOException {
        socketChannel.connect(new InetSocketAddress(host, port));
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }

    public CallFuture<byte[]> asyncSend(byte[] bytes) throws IOException {
        CallFuture<byte[]> future = CallFuture.newInstance();
        int logId = IdGenerator.genUUID();
        CallbackPool.put(logId, future);
        ByteBuffer writeBuff = WRITE_BUFFER.get();
        writeBuff.clear();
        writeBuff.putInt(bytes.length);
        writeBuff.putInt(logId);
        writeBuff.put(bytes);
        writeBuff.flip();
        synchronized (this) {
            socketChannel.write(writeBuff);
            //if (!writeBuff.hasRemaining()) {
            //System.out.println("Send message to server successfully!");
            //}
        }
        return future;
    }

    public static void main(String[] args) throws Exception {
        NioClient client = new NioClient("localhost", 8080, 64);
        Random random = new Random(System.currentTimeMillis());
        long times = 10000;
        for (int i = 0; i < times; i++) {
            int bodyLen = 1 + random.nextInt(40);
            String text = RandomStringUtils.randomAlphabetic(bodyLen);
            System.out.println(i + " request =" + text + " " + text.length());
            CallFuture<byte[]> future = client.asyncSend(text.getBytes(StandardCharsets.UTF_8));
            String response = new String(future.get());
            System.out.println(i + " response=" + response);
            if (!response.equals(text)) {
                throw new RuntimeException("Not equal!");
            }
        }
    }
}
