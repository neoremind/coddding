package net.neoremind.mycode.lang;

/**
 * From {@link sun.reflect.UTF8}
 *
 * @author zhangxu
 */
public class UTF8 {

    public static byte[] encode(String var0) {
        int var1 = var0.length();
        byte[] var2 = new byte[utf8Length(var0)];
        int var3 = 0;

        try {
            for (int var4 = 0; var4 < var1; ++var4) {
                int var5 = var0.charAt(var4) & '\uffff';
                if (var5 >= 1 && var5 <= 127) {
                    var2[var3++] = (byte) var5;
                } else if (var5 != 0 && (var5 < 128 || var5 > 2047)) {
                    var2[var3++] = (byte) (224 + (var5 >> 12));
                    var2[var3++] = (byte) (128 + (var5 >> 6 & 63));
                    var2[var3++] = (byte) (128 + (var5 & 63));
                } else {
                    var2[var3++] = (byte) (192 + (var5 >> 6));
                    var2[var3++] = (byte) (128 + (var5 & 63));
                }
            }

            return var2;
        } catch (ArrayIndexOutOfBoundsException var6) {
            throw new InternalError("Bug in sun.reflect bootstrap UTF-8 encoder", var6);
        }
    }

    private static int utf8Length(String var0) {
        int var1 = var0.length();
        int var2 = 0;

        for (int var3 = 0; var3 < var1; ++var3) {
            int var4 = var0.charAt(var3) & '\uffff';
            if (var4 >= 1 && var4 <= 127) {
                ++var2;
            } else if (var4 != 0 && (var4 < 128 || var4 > 2047)) {
                var2 += 3;
            } else {
                var2 += 2;
            }
        }

        return var2;
    }

}
