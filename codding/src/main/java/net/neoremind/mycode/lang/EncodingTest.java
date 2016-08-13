package net.neoremind.mycode.lang;

import java.util.stream.Collectors;

import org.junit.Test;

import com.google.common.primitives.Bytes;

/**
 * 编码测试，见印象笔记内的《字符集与字符编码》一节
 *
 * @author zhangxu
 */
public class EncodingTest {

    /**
     * https://zh.wikibooks.org/wiki/Unicode/4000-4FFF
     */
    @Test
    public void test() throws Exception {
        String s = "a中文";
        System.out.println(s.length()); //3，char[]的长度就是3
        System.out.println(string2Unicode(s)); // 打印出来的unicode，以UCS-2标准的字符集，\u0061\u4e2d\u6587
        System.out.println(unicode2String(string2Unicode(s))); // 打印“a中文”
        System.out.println(toLiteralBytes(s.getBytes())); //下面三行都打印[61, e4, b8, ad, e6, 96, 87]，按照UTF-8进行编码
        System.out.println(toLiteralBytes(s.getBytes("UTF-8"))); //[61, e4, b8, ad, e6, 96, 87]
        System.out.println(toLiteralBytes(UTF8.encode(s)));  //[61, e4, b8, ad, e6, 96, 87]
        System.out.println(toLiteralBytes(s.getBytes("ISO8859-1"))); //[61, 3f, 3f] 3f是ASCII里面的问号？
        System.out.println(toLiteralBytes(s.getBytes("GBK"))); //[61, d6, d0, ce, c4]，完全兼容GB2312
        System.out.println(toLiteralBytes(s.getBytes("GB2312"))); //[61, d6, d0, ce, c4]

        String ss = new String(s.getBytes("GB2312"), "GBK");
        System.out.println(ss);  //a中文，中文简体完全兼容

        String chinese = "中文"; //java内部编码，class文件用UTF-8，JVM用UTF-16，JAVA语言内字符集用unicode
        String gbkChinese = new String(chinese.getBytes("GBK"), "ISO-8859-1");//转换成gbk编码
        String unicodeChinese = new String(gbkChinese.getBytes("ISO-8859-1"), "GBK");//java内部编码
        System.out.println(unicodeChinese);//中文，是可以还原的，因为ISO8859-1是单字节的，所以无缝还原
        String utf8Chinese = new String(unicodeChinese.getBytes("UTF-8"), "ISO-8859-1");//utf--8编码做encode
        System.out.println(utf8Chinese);//乱码，UTF-8输出是6个字节，ISO直接是6个字节输出，按照单字节理解，JAVA语言按照unicode来，乱码是ä¸­æ
        unicodeChinese = new String(utf8Chinese.getBytes("ISO-8859-1"), "UTF-8");//java内部编码
        //unicodeChinese = new String(utf8Chinese.getBytes("ASCII"), "UTF-8");//java内部编码，打印??????,6个问号
        System.out.println(unicodeChinese);//中文

        System.out.println(toLiteralBytes("a".getBytes("Unicode"))); //[fe, ff, 0, 61]
        System.out.println("a(Unicode)    ：" + "a".getBytes("Unicode").length); //4，2个额外的字节是大尾端的标记fe ff
        System.out.println("a(Unicode)    ：" + "aa".getBytes("Unicode").length); //6
        System.out.println("啊(Unicode)   ：" + "啊".getBytes("Unicode").length); //4
        System.out.println("啊啊(Unicode) ：" + "啊啊".getBytes("Unicode").length); //6
        System.out.println("");
        System.out.println("a(UTF-8)    ：" + "a".getBytes("UTF-8").length); //1
        System.out.println("aa(UTF-8)   ：" + "aa".getBytes("UTF-8").length); //2
        System.out.println("啊(UTF-8)   ：" + "啊".getBytes("UTF-8").length); //3
        System.out.println("啊啊(UTF-8) ：" + "啊啊".getBytes("UTF-8").length); //6
        System.out.println("");
        System.out.println("a(UTF-16)    ：" + "a".getBytes("UTF-16").length); //4
        System.out.println("aa(UTF-16)   ：" + "aa".getBytes("UTF-16").length); //6
        System.out.println("啊(UTF-16)   ：" + "啊".getBytes("UTF-16").length); //4
        System.out.println("啊啊(UTF-16) ：" + "啊啊".getBytes("UTF-16").length); //6
    }

    public String toLiteralBytes(byte[] bytes) {
        return Bytes.asList(bytes).stream()
                .map(b -> Integer.toHexString(b & 0xFF))
                .collect(Collectors.toList())
                .toString();
    }

    public String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }

    public String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            string.append((char) data);
        }
        return string.toString();
    }
}
