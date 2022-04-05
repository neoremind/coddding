package net.neoremind.mycode.nio.simple;

import lombok.extern.slf4j.Slf4j;
import net.neoremind.mycode.nio.simple.client.CallbackContext;
import sun.nio.ch.DirectBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NioHandler {

    public static final int HEAD_LEN = 8;

    private final int maxBodyLen;

    private final HeaderResolver headerResolver = new HeaderResolver();

    private final InputHandler inputHandler;

    public NioHandler(int maxBodyLen, InputHandler inputHandler) {
        this.maxBodyLen = maxBodyLen;
        this.inputHandler = inputHandler;
    }

    private final ConcurrentHashMap<SocketChannel, ChannelContext> channelContextMap = new ConcurrentHashMap<>(64, 0.75f, 16);

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
    public void handle(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ChannelContext channelContext = channelContextMap.get(channel);
        if (channelContext == null) {
            throw new IllegalStateException("No channel wrapper found!");
        }
        ByteBuffer readBuffer = channelContext.getReadBuffer();
        // log.debug("Begin handle {}", channelContext);
        // If read buffer is not able to accommodate bytes with a length of even less than a header length.
        // compact read buffer and reset readerIndex to 0
        if (readBuffer.remaining() < HEAD_LEN) {
            // log.debug("[before] reset buffer due to cannot hold a header {}", channelContext);
            channelContext.compact();
            // log.debug("[after] reset buffer due to cannot hold a header {}", channelContext);
        }

        int leftover = channelContext.numOfReadableBytes();
        int numBytesRead = channel.read(readBuffer);
        channelContext.incWriterIndex(numBytesRead);
        // log.debug("Read from kernel done, numBytesRead={}, {}", numBytesRead, channelContext);

        // we only accumulate leftover bytes when there is not complete body, so we compact the buffer
        // in that way, we have to add up the bytes read but not processed in last partial packet.
        if (channelContext.getReaderIndex() == 0) {
            numBytesRead += leftover;
            // log.debug("Add leftover={}, {}", leftover, channelContext);
        }

        for (; ; ) {
            if (channelContext.numOfReadableBytes() < HEAD_LEN) {
                // log.debug("Header hasn't been read completely... {}", channelContext);
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
            headerResolver.parse(readBuffer);
            int bodyLen = headerResolver.getBodyLen();
            int logId = headerResolver.getLogId();
            if (bodyLen > maxBodyLen) {
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
                // log.debug("Body hasn't been read completely... need bodyLen={} but only {} in buffer, {}", bodyLen, channelContext.getWriterIndex() - channelContext.getReaderIndex(), channelContext);
                channelContext.decReaderIndex(HEAD_LEN);
                channelContext.compact();
                break;
            }
            // log.debug("bodyLen={}, {}", bodyLen, channelContext);
            inputHandler.handle(channelContext, bodyLen, logId);
            channelContext.incReaderIndex(bodyLen);
        }
    }

    public void initContext(SocketChannel channel, ByteBuffer readBuffer) {
        ChannelContext channelContext = new ChannelContext(channel, readBuffer);
        channelContextMap.put(channel, channelContext);
    }

    public static class ChannelContext {
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
}
