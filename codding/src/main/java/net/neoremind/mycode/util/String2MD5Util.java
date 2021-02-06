package net.neoremind.mycode.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;

/**
 * @author xu.zx
 */
public class String2MD5Util {

    public static void main(String[] args) {
        System.out.println(string2MD5("41093474"));
    }

    public static String string2MD5(String inStr) {
        MessageDigest md;
        String algorithm = "MD5";
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new ProviderException(algorithm + " implementation provider NOT available!", ex);
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

}
