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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SimpleServer {

    public static final int DEFAULT_BUFFER_SIZE = 1024;

    public static final int HEAD_LEN = 4;

    private final int port;

    private final int readBufferSize;

    private final ConcurrentHashMap<SocketChannel, ChannelContext> channelContextMap = new ConcurrentHashMap<>();

    public SimpleServer(int port) {
        this(port, DEFAULT_BUFFER_SIZE);
    }

    public SimpleServer(int port, int readBufferSize) {
        this.port = port;
        this.readBufferSize = readBufferSize;
    }

    public static void main(String[] args) throws IOException {
        new SimpleServer(8080, 64).run();
    }

    public void run() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", port));
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
        // If read buffer is not able to accommodate bytes with a length of even less than a header length.
        // set read buffer and reset readerIndex to 0
        if (readBuffer.remaining() < HEAD_LEN) {
            readBuffer.position(0);
            channelContext.resetReaderIndex();
            //log.warn("reset buffer due to cannot hold a header");
        }

        int leftover = channelContext.getReadBuffer().position();
        int numBytesRead = channel.read(readBuffer);

        // we only accumulate leftover bytes when there is not complete body, so we compact the buffer
        // in that way, we have to add up the bytes read but not processed in last partial packet.
        if (channelContext.readerIndex() == 0) {
            numBytesRead += leftover;
        }
        for (; ; ) {
            if (readBuffer.position() == channelContext.readerIndex() && numBytesRead < HEAD_LEN) {
                //log.warn("Header hasn't been read completely... only get " + numBytesRead + " " + readBuffer);
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
            //      readerIndex  <- pos
            // +---------------------------+
            // |      |             |      |
            // +---------------------------+
            //  go back to where it reads last time
            readBuffer.position(channelContext.readerIndex());
            int bodyLen = readBuffer.getInt();
            if (bodyLen > DEFAULT_BUFFER_SIZE - HEAD_LEN) {
                throw new IllegalStateException("Cannot read body larger than limit, actual " + bodyLen);
            }
            int numOfReadableBytes = channelContext.numOfReadableBytes();
            if (numOfReadableBytes < bodyLen) {
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
                channelContext.resetReaderIndex();
                //log.warn("Body hasn't been read completely... {}, need bodyLen={} but numOfReadableBytes={}", readBuffer, bodyLen, numOfReadableBytes);
                break;
            }
            byte[] requestBody = new byte[bodyLen];
            int payloadLength = HEAD_LEN + bodyLen;
            readBuffer.get(requestBody);
            channelContext.incReaderIndex(payloadLength);
            numBytesRead -= payloadLength;

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
        channel.register(selector, SelectionKey.OP_READ);
        ByteBuffer readBuffer = ByteBuffer.allocateDirect(readBufferSize);
        ChannelContext channelContext = new ChannelContext(channel, readBuffer);
        channelContextMap.put(channel, channelContext);
        log.info("Connection accepted: {}", channel.getRemoteAddress());
    }

    static class ChannelContext {
        private final SocketChannel channel;
        private final ByteBuffer readBuffer;
        private int readerIndex = 0;

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

        public void resetReaderIndex() {
            readerIndex = 0;
        }

        public void incReaderIndex(int delta) {
            readerIndex += delta;
        }

        public int readerIndex() {
            return readerIndex;
        }

        public int numOfReadableBytes() {
            return readBuffer.remaining();
        }

        public void clean() {
            if (readBuffer != null && readBuffer instanceof DirectBuffer) {
                ((DirectBuffer) readBuffer).cleaner().clean();
            }
        }
    }
}
