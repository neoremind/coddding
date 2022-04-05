package net.neoremind.mycode.nio.simple.client;

import lombok.extern.slf4j.Slf4j;
import net.neoremind.mycode.nio.simple.InputHandler;
import net.neoremind.mycode.nio.simple.NioHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ClientEchoInputHandler implements InputHandler {

    @Override
    public void handle(NioHandler.ChannelContext context, int bodyLen, int logId) throws IOException {
        ByteBuffer readBuffer = context.getReadBuffer();
        byte[] input = new byte[bodyLen];
        readBuffer.get(input);
        CallbackContext callbackContext;
        Callback<byte[]> callback;
        try {
            callbackContext = CallbackPool.getContext(logId);
            callback = callbackContext.getCallback();
            callback.handleResult(input);
        } catch (NullPointerException e) {
            log.info("Skip NPE due to logId might be duplicate because it is only an integer " + logId);
            log.error(e.getMessage(), e);
        } finally {
            CallbackPool.remove(logId);
        }
    }
}
