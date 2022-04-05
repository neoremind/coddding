package net.neoremind.mycode.nio.simple;

import java.nio.ByteBuffer;

/**
 * <pre>
 *       Byte/     0       |       1       |       2       |       3       |
 *          /              |               |               |               |
 *         |0 1 2 3 4 5 6 7|0 1 2 3 4 5 6 7|0 1 2 3 4 5 6 7|0 1 2 3 4 5 6 7|
 *         +---------------+---------------+---------------+---------------+
 *        0| body length                                                   |
 *         +---------------+---------------+---------------+---------------+
 *        4| log id                                                        |
 *         +---------------+---------------+---------------+---------------+
 *         Total 8 bytes
 * </pre>
 */
public class HeaderResolver {

    private int bodyLen;

    private int logId;

    public void parse(ByteBuffer buffer) {
        bodyLen = buffer.getInt();
        logId = buffer.getInt();
    }

    public int getBodyLen() {
        return bodyLen;
    }

    public void setBodyLen(int bodyLen) {
        this.bodyLen = bodyLen;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }
}
