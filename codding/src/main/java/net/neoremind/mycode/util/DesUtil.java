package net.neoremind.mycode.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;

public class DesUtil {
    private static SecretKey key;
    //private static Cipher cipher;
    private static final String DES_KEY = "e1f90ef608e1bce503d1f34b9f8843b8e49c1a946d424c41155955c69aef4a16";

    public static void init() throws Exception {
        byte[] keyBytes = DatatypeConverter.parseHexBinary(DES_KEY);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
        key = factory.generateSecret(new DESedeKeySpec(keyBytes));
        //cipher = Cipher.getInstance("DESede");
    }

    public static String encrypt(String data) throws Exception {
        if (key == null) {
            init();
        }

        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedArray = cipher.doFinal(data.getBytes());
        return Base64.encodeBase64URLSafeString(encryptedArray);
    }

    public static String decrypt(String data) throws Exception {
        if (key == null) {
            init();
        }

        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, key);

        try {
            byte[] decodedArray = Base64.decodeBase64(data);
            byte[] decryptedArray = cipher.doFinal(decodedArray);
            return new String(decryptedArray);
        } catch (Exception e) {
            //Failed to decrypt data,the data may be encrypted by old format
        }

        // If we get here, it means we failed to decrypt with the new base64encoding way,
        // try to decrypt with the old base64 encoding
        // This should be deleted after all components are upgraded to latest version
        byte[] decodedArray = DatatypeConverter.parseBase64Binary(data);
        byte[] decryptedArray = cipher.doFinal(decodedArray);
        return new String(decryptedArray);
    }

    public static void main(String[] args) throws Exception {
        String tempString =
                "usiLOSvtw4qKEzG0Jq89HU_zBi9Rpn8FgXD6BNjBFAp1hr0gRGInwQsyFr"
                        +
                        "-48fD20WDTN2kceqyq1w5C4myK5ilrqr_v7u9JyIQ6dpnoY0TBk49Hs6MNmhGe9wbX9XbPcoMlU1_QA_arKMn4PaVDltC-gPAcE-7DH8hRlbQ1eZENyrOh-FPetNK13URMQwJrD4IUla3XEryGavfhZ1T9C-uO0P4gS6DqRBL5SV9aZo8l6JoXukIMDA";
        String decryptedMsg = decrypt(tempString);
        System.out.println(decryptedMsg);

    }
}

