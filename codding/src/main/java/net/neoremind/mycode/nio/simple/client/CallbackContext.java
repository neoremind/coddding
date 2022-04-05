package net.neoremind.mycode.nio.simple.client;

public class CallbackContext {

    private final int logId;

    private final long startTime;

    private final Callback<byte[]> callback;

    public CallbackContext(int logId, long startTime, Callback<byte[]> callback) {
        this.logId = logId;
        this.startTime = startTime;
        this.callback = callback;
    }

    public int getLogId() {
        return logId;
    }

    public long getStartTime() {
        return startTime;
    }

    public Callback<byte[]> getCallback() {
        return callback;
    }
}
