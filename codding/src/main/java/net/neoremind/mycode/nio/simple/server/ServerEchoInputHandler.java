package net.neoremind.mycode.nio.simple.server;

import net.neoremind.mycode.nio.simple.InputHandler;
import net.neoremind.mycode.nio.simple.NioHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ServerEchoInputHandler implements InputHandler {

    private static final ThreadLocal<ByteBuffer> WRITE_BUFFER = ThreadLocal.withInitial(() -> ByteBuffer.allocateDirect(2048));

    @Override
    public void handle(NioHandler.ChannelContext context, int bodyLen, int logId) throws IOException {
        SocketChannel channel = context.getChannel();
        ByteBuffer readBuffer = context.getReadBuffer();
        // normal
//        byte[] input = new byte[bodyLen];
//        readBuffer.get(input);
//        String str = new String(input, "UTF-8");
//        byte[] response = str.getBytes(StandardCharsets.UTF_8);
//        ByteBuffer buffer = ByteBuffer.allocate(NioHandler.HEAD_LEN + response.length);
//        buffer.putInt(response.length);
//        buffer.putInt(logId);
//        buffer.put(response);
//        buffer.flip();
//        channel.write(buffer);

        // zero-copy
        ByteBuffer writeBuffer = WRITE_BUFFER.get();
        writeBuffer.clear();
        writeBuffer.putInt(bodyLen);
        writeBuffer.putInt(logId);
        for (int i = 0; i < bodyLen; i++) {
            writeBuffer.put(readBuffer.get());
        }
        writeBuffer.flip();
        channel.write(writeBuffer);
    }
}
