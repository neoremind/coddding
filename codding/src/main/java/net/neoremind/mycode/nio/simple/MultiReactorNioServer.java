package net.neoremind.mycode.nio.simple;

import lombok.extern.slf4j.Slf4j;
import sun.nio.ch.DirectBuffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MultiReactorNioServer {

    public static final int DEFAULT_BUFFER_SIZE = 1024;

    public static final int HEAD_LEN = 4;

    private final int port;

    private final int readBufferSize;

    private final ConcurrentHashMap<SocketChannel, ChannelContext> channelContextMap = new ConcurrentHashMap<>();

    private final SubReactor[] subReactors;

    private final AtomicInteger counter = new AtomicInteger(0);

    private volatile boolean isStop;

    public MultiReactorNioServer(int port, int readBufferSize, int numOfSubReactor) {
        this.port = port;
        this.readBufferSize = readBufferSize;
        this.subReactors = new SubReactor[numOfSubReactor];
        for (int i = 0; i < numOfSubReactor; i++) {
            this.subReactors[i] = new SubReactor(i);
            this.subReactors[i].start();
        }
    }

    public static void main(String[] args) throws IOException {
        new MultiReactorNioServer(8080, 1024, 8).start();
    }

    public void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        log.info("Server started at " + port);

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
                            register(subReactor.selector, serverSocket);
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
        for (Map.Entry<SocketChannel, ChannelContext> entry : channelContextMap.entrySet()) {
            entry.getKey().close();
            entry.getValue().clean();
        }
    }

    /**
     * Make use of long-lived read buffer to read data from TCP kernel space to user space buffer.
     * If we use a {@code HeapByteBuffer}, the data will be copied first to an off-heap {@code DirectByteBuffer},
     * to avoid one more memory copy operation, here we make {@code DirectByteBuffer} as read buffer.
     *
     * <p>We will read as much from kernel space as possible to fill the read buffer, our assumption is TCP
     * works in bi-directional way for input/output, so it doesn't have to be request-response model where one request
     * comes after the other, in opposite, there could be multiple packets in this read buffer like below.
     *
     * <pre>
     *                                     pos/limit
     *               +--------------------------------------+
     *  read buffer  |  req1    |  req2  | req3|            |
     *               +--------------------------------------+
     * </pre>
     *
     * <p>New packet will be appended in the read buffer. We maintain a <code>readerIndex</code> besides the buffer's native position,
     * because when we read data into the buffer, the position will be moved forward, so in order to process the newly added packet,
     * we have to go back to where we are last time.
     *
     * <p>We will do a loop, within each one loop, read one complete packet, then process.
     *
     * @param key selection key
     * @throws IOException
     */
    private void handle(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ChannelContext channelContext = channelContextMap.get(channel);
        if (channelContext == null) {
            throw new IllegalStateException("No channel wrapper found!");
        }
        ByteBuffer readBuffer = channelContext.getReadBuffer();
        log.debug("Begin handle {}", channelContext);
        // If read buffer is not able to accommodate bytes with a length of even less than a header length.
        // compact read buffer and reset readerIndex to 0
        if (readBuffer.remaining() < HEAD_LEN) {
            log.debug("[before] reset buffer due to cannot hold a header {}", channelContext);
            channelContext.compact();
            log.debug("[after] reset buffer due to cannot hold a header {}", channelContext);
        }

        int leftover = channelContext.numOfReadableBytes();
        int numBytesRead = channel.read(readBuffer);
        channelContext.incWriterIndex(numBytesRead);
        // log.debug("Read from kernel done, numBytesRead={}, {}", numBytesRead, channelContext);

        // we only accumulate leftover bytes when there is not complete body, so we compact the buffer
        // in that way, we have to add up the bytes read but not processed in last partial packet.
        if (channelContext.getReaderIndex() == 0) {
            numBytesRead += leftover;
            log.debug("Add leftover={}, {}", leftover, channelContext);
        }

        for (; ; ) {
            if (channelContext.numOfReadableBytes() < HEAD_LEN) {
                log.debug("Header hasn't been read completely... {}", channelContext);
                if (numBytesRead < 0) {
                    // remote close, so close channel in server-side
                    key.cancel();
                    channel.close();
                    channelContext.clean();
                    channelContextMap.remove(channel);
                    log.warn("Channel reach end " + key.channel());
                }
                break;
            }
            //      readerIndex  <- pos (where kernel reads data up to)
            // +---------------------------+
            // |      |             |      |
            // +---------------------------+
            //  go back to where it reads last time
            readBuffer.position(channelContext.getReaderIndex());
            int bodyLen = readBuffer.getInt();
            if (bodyLen > DEFAULT_BUFFER_SIZE - HEAD_LEN) {
                throw new IllegalStateException("Cannot read body larger than limit, actualLen=" + bodyLen + ", numBytesRead={}" + numBytesRead + ", " + channelContext);
            }
            channelContext.incReaderIndex(HEAD_LEN);
            int numOfReadableBytes = channelContext.numOfReadableBytes();
            if (numOfReadableBytes < bodyLen) {
                //      readerIndex pos  writerIndex
                // +---------------------------+
                // |      |  HEADER |      |   |
                // +---------------------------+
                //  make read buffer position go back of length of header since we will re-read next time.
                //
                //readerIndex       pos=writerIndex
                // +---------------------------+
                // |  HEADER  |      |         |
                // +---------------------------+
                //  compact reader buffer to the beginning to accommodate more body.
                log.debug("Body hasn't been read completely... need bodyLen={} but only {} in buffer, {}", bodyLen, channelContext.getWriterIndex() - channelContext.getReaderIndex(), channelContext);
                channelContext.decReaderIndex(HEAD_LEN);
                channelContext.compact();
                break;
            }
            // log.debug("bodyLen={}, {}", bodyLen, channelContext);
            byte[] requestBody = new byte[bodyLen];
            channelContext.incReaderIndex(bodyLen);
            readBuffer.get(requestBody);
            doBusinessLogic(channel, requestBody);
        }
    }

    private void doBusinessLogic(SocketChannel channel, byte[] requestBody) throws IOException {
        String str = new String(requestBody, "UTF-8");
        str = str.toUpperCase();
//        try {
//            Thread.sleep(600000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
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
        ByteBuffer readBuffer = ByteBuffer.allocateDirect(readBufferSize);
        ChannelContext channelContext = new ChannelContext(channel, readBuffer);
        channelContextMap.put(channel, channelContext);
        selector.wakeup();
        channel.register(selector, SelectionKey.OP_READ);
        log.info("Connection accepted: {}", channel.getRemoteAddress());
    }

    class ChannelContext {
        private final SocketChannel channel;
        private final ByteBuffer readBuffer;
        private int readerIndex = 0;
        private int writerIndex = 0;

        public ChannelContext(SocketChannel channel, ByteBuffer readBuffer) {
            this.channel = channel;
            this.readBuffer = readBuffer;
        }

        public SocketChannel getChannel() {
            return channel;
        }

        public ByteBuffer getReadBuffer() {
            return readBuffer;
        }

        public int getReaderIndex() {
            return readerIndex;
        }

        public void setReaderIndex(int readerIndex) {
            this.readerIndex = readerIndex;
        }

        public int getWriterIndex() {
            return writerIndex;
        }

        public void setWriterIndex(int writerIndex) {
            this.writerIndex = writerIndex;
        }

        public void incReaderIndex(int delta) {
            readerIndex += delta;
        }

        public void decReaderIndex(int delta) {
            readerIndex -= delta;
        }

        public void incWriterIndex(int delta) {
            writerIndex += delta;
        }

        public int numOfReadableBytes() {
            return writerIndex - readerIndex;
        }

        public void compact() {
            readBuffer.position(readerIndex);
            readBuffer.limit(writerIndex);
            readBuffer.compact();
            readerIndex = 0;
            writerIndex = readBuffer.position();
        }

        public void clean() {
            if (readBuffer != null && readBuffer instanceof DirectBuffer) {
                ((DirectBuffer) readBuffer).cleaner().clean();
            }
        }

        @Override
        public String toString() {
            return "ChannelContext{" +
                    readBuffer +
                    ", readerIdx=" + readerIndex +
                    ", writerIdx=" + writerIndex +
                    '}';
        }
    }

    class SubReactor {

        private final int id;

        private final Selector selector;

        private final Thread executor;

        private volatile boolean isStop;

        public SubReactor(int id) {
            this.id = id;
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
