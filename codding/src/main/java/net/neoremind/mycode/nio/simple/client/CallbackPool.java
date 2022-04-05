package net.neoremind.mycode.nio.simple.client;

import java.util.concurrent.ConcurrentHashMap;

public class CallbackPool {

    private static final int INITIAL_CAPACITY = 128 * 4 / 3;

    private static final float LOAD_FACTOR = 0.75f;

    private static final int CONCURRENCY_LEVEL = 16;

    private static ConcurrentHashMap<Integer, CallbackContext> CALLBACK_MAP = new ConcurrentHashMap<Integer, CallbackContext>(
            INITIAL_CAPACITY, LOAD_FACTOR, CONCURRENCY_LEVEL);

    public static CallbackContext getContext(int logId) {
        CallbackContext callbackContext = CALLBACK_MAP.get(logId);
        return callbackContext == null ? null : callbackContext;
    }

    public static void put(int logId, Callback callback) {
        CALLBACK_MAP.putIfAbsent(logId, new CallbackContext(logId, System.currentTimeMillis(), callback));
    }

    public static void remove(int logId) {
        CALLBACK_MAP.remove(logId);
    }

    public static void clear() {
        CALLBACK_MAP.clear();
    }
}
