package net.neoremind.mycode.nio.simple.client;

import com.google.common.primitives.Ints;

import java.security.SecureRandom;

public class IdGenerator {

    private static final ThreadLocal<byte[]> UUID = ThreadLocal.withInitial(() -> new byte[4]);

    private static class Holder {
        static final SecureRandom numberGenerator = new SecureRandom();
    }

    public static int genUUID() {
        SecureRandom ng = Holder.numberGenerator;
        byte[] randomBytes = UUID.get();
        ng.nextBytes(randomBytes);
        //randomBytes[6]  &= 0x0f;  /* clear version        */
        //randomBytes[6]  |= 0x40;  /* set to version 4     */
        //randomBytes[8]  &= 0x3f;  /* clear variant        */
        //randomBytes[8]  |= 0x80;  /* set to IETF variant  */
        return Ints.fromByteArray(randomBytes);
    }
}