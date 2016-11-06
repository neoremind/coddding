package net.neoremind.mycode.argorithm.other;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by helechen on 16/10/21.
 */
public class DecodeString {

    static Map<String, Integer> urlShorter = new HashMap<>();

    {
        urlShorter.put("abC1kLpO_S", 1234);
        urlShorter.put("iJ8jSwe_r", 4567);
    }

    static Integer decodeHelper(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        return helper(0, sb, s);
    }

    static Integer helper(int i, StringBuilder sb, String s) {
        if (i == s.length()) {
            return decode(sb.toString());
        }

        if (!Character.isLetter(s.charAt(i))) {
            sb.append(s.charAt(i));
            Integer result = helper(i + 1, sb, s);
            if (result != null) {
                return result;
            }
        } else {
            char ch = s.charAt(i);
            sb.append(Character.toUpperCase(ch));
            Integer result = helper(i + 1, sb, s);
            if (result != null) {
                return result;
            }
            sb.deleteCharAt(sb.length() - 1);

            sb.append(Character.toLowerCase(ch));
            result = helper(i + 1, sb, s);
            if (result != null) {
                return result;
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return null;
    }

    static Integer decode(String s) {
        return urlShorter.getOrDefault(s, null);
    }

    public static void main(String[] args) {
        assert decode("abC1kLpO_S") == 1234;
        assert decodeHelper("abC1kLpO_S") == 1234;
        assert decodeHelper("abC1kLpo_S") == 1234;
        assert decodeHelper("abc1kLpo_S") == 1234;
        assert decodeHelper("ABc1KlPO_s") == 1234;
        assert decodeHelper("ABc1Ks") == null;
    }

}
