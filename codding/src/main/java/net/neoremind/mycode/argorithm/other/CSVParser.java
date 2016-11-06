package net.neoremind.mycode.argorithm.other;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * /*
 * John,Smith,john.smith@gmail.com,Los Angeles,1
 * Jane,Roberts,janer@msn.com,"San Francisco, CA",0
 * "Alexandra ""Alex""",Menendez,alex.menendez@gmail.com,Miami,1
 * """Alexandra Alex"""
 * John|Smith|john.smith@gmail.com|Los Angeles|1
 * Jane|Roberts|janer@msn.com|San Francisco, CA|0
 * Alexandra "Alex"|Menendez|alex.menendez@gmail.com|Miami|1
 * "Alexandra Alex"
 * <p>
 * <p>
 * Understand the problem:
 * For this problem,there are several cases need to consider:
 * 1. For comma,transform to|
 * 2. If comma is inside a quote,don't treat the comma as separated. Remove the comma and print the entire token. e.g. "San Francisco, CA" => San Francisco, CA
 * 3. If there are double quotes,remove one.e.g."Alexandra ""Alex"""=>Alexandra"Alex".
 * Note that"""Alexandra Alex"""becomes"Alexandra Alex"because we first remove the outer-most quote,and then remove one quote of the double quote.
 */
public class CSVParser {

    public static String parseCSV(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        List<String> res = new ArrayList<>(6); //estimate 6 columns
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < str.length()) {
            char ch = str.charAt(i);
            if (ch == '"') {
                int j = i;
                while (j < str.length()) {
                    if (str.charAt(j) == '"') {
                        if (j + 1 < str.length() && str.charAt(j + 1) == '"') {
                            sb.append('"');
                            j++;
                        } else {
                            if (sb.length() > 0 && sb.charAt(0) != '"') {
                                res.add(sb.toString());
                                sb.setLength(0);
                            }
                            break;
                        }
                    } else {
                        sb.append(str.charAt(j));
                    }
                    j++;
                }
                i = j;
            } else if (ch == ',') {
                if (sb.length() > 0) {
                    res.add(sb.toString());
                    sb.setLength(0);
                }
            } else {
                sb.append(ch);
            }
            i++;
        }

        // don't forget last if has
        if (sb.length() > 0) {
            res.add(sb.toString());
        }
        return res.stream().collect(Collectors.joining("|"));
    }

    public static void main(String[] args) {
        String input1 = "John,Smith,john.smith@gmail.com,Los Angeles,1";
        System.out.println(parseCSV(input1));

        String input2 = "Jane,Roberts,janer@msn.com,\"San Francisco, CA\",0";
        System.out.println(parseCSV(input2));

        String input3 = "\"Alexandra \"\"Alex\"\"\",Menendez,alex.menendez@gmail.com,Miami,1";
        System.out.println(parseCSV(input3));

        String input4 = "\"\"\"Alexandra Alex\"\"\"";
        System.out.println(parseCSV(input4));

//        String input5 = "\"\"\"\"Alexandra Alex\"\"\"\"";
//        System.out.println(parseCSV(input5));
//
//        String input6 = "\"\"\"\"Alexandra \"\"Alex\"\"\"\"\"\"";
//        System.out.println(parseCSV(input6));
    }

}
