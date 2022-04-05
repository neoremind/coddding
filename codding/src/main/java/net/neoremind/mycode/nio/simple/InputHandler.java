package net.neoremind.mycode.nio.simple;

import java.io.IOException;

public interface InputHandler {

    void handle(NioHandler.ChannelContext context, int bodyLen, int logId) throws IOException;
}
