package net.neoremind.mycode.nio.nio.netty.serializer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.neoremind.mycode.nio.nio.netty.protocol.NsHead;
import net.neoremind.mycode.nio.nio.netty.protocol.PbrpcMsg;
import net.neoremind.mycode.util.JsonMapper;

public class PbrpcMessageDeserializer extends ByteToMessageDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(PbrpcMessageDeserializer.class);

    private JsonMapper jsonMapper = JsonMapper.buildNormalMapper();

    private static final int NSHEAD_SIZE = NsHead.NSHEAD_LEN;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // Make sure if the length field was received.
        if (in.readableBytes() < NSHEAD_SIZE) {
            // The length field was not received yet - return null.
            // This method will be invoked again when more packets are
            // received and appended to the buffer.
            return;
        }

        // The length field is in the buffer.

        // Mark the current buffer position before reading the length field
        // because the whole frame might not be in the buffer yet.
        // We will reset the buffer position to the marked position if
        // there's not enough bytes in the buffer.
        in.markReaderIndex();

        // Read the RPC head
        long rpcMessageDecoderStart = System.nanoTime();
        // byte[] totalBytes = new byte[(int)nsHead.getBodyLen()];
        // in.readBytes(totalBytes, 0, (int)nsHead.getBodyLen());
        // ByteBuffer buffer = in.nioBuffer(in.readerIndex(), NSHEAD_SIZE);
        //
        // buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] bytes = new byte[NSHEAD_SIZE];
        // buffer.get(bytes);
        in.readBytes(bytes, 0, NSHEAD_SIZE);

        NsHead nsHead = new NsHead();
        nsHead.wrap(bytes);

        // get total message size
        // int messageSize = (int) nsHead.getBodyLen() + NsHead.NSHEAD_LEN;
        int bodySize = (int) nsHead.getBodyLen();

        // Make sure if there's enough bytes in the buffer.
        if (in.readableBytes() < bodySize) {
            // The whole bytes were not received yet - return null.
            // This method will be invoked again when more packets are
            // received and appended to the buffer.

            // Reset to the marked position to read the length field again
            // next time.
            in.resetReaderIndex();
            return;
        }

        in.markReaderIndex();

        // There's enough bytes in the buffer. Read it.
        byte[] msgBytes = new byte[bodySize];
        in.readBytes(msgBytes, 0, bodySize);

        String info = new String(msgBytes);
        PbrpcMsg decoded = jsonMapper.fromJson(info, PbrpcMsg.class);

        LOG.info("Deser data is " + decoded);
        long rpcMessageDecoderEnd = System.nanoTime();
        LOG.info("Deser using " + (rpcMessageDecoderEnd - rpcMessageDecoderStart) / 1000 + "ns");

        if (decoded != null) {
            out.add(decoded);
        }
    }
}
