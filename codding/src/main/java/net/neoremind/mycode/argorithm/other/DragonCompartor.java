package net.neoremind.mycode.argorithm.other;

import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Dataset1: Name, data1, data2 xxx,xxx,xxx … Dataset2: Name, data3, data4 xxx,xxx,xxx …. 让读两个文件，找到name 相同的entity。
 * 根据data2,和data3,以及他给的一个fomular算一个东西（每个entity都算）。 然后按照算出来的东西排序，最后输出name.
 * <p>
 * Dataset1 恐龙名字， 恐龙腿长， 恐龙食性（食草，食肉，两者皆吃，这个是无关feature)
 * Dataset2 恐龙名字， 恐龙某个feature(… 补充内容 (2016-2-4 03:04): Dataset2
 * 恐龙名字， 恐龙某个feature(暂名d1), Stance(两只脚，四只脚等）
 * <p>
 * file1： name,leg_length,diet file2: name,stride_length,stance 两个files里的恐龙的名字是对应的，但是不顺序
 * 要求是根据给定的一个公式（输入是leg_length和stride_length）计算出速度，从大到小输出直立行走的恐龙名字
 * <p>
 * <p>
 *
 * @author xu.zhang
 */
public class DragonCompartor {

    private Function<String[], Dragon> f1 = fields -> {
        String name = fields[0];
        if (name == null || name.length() == 0) {
            throw new DragonException("name invalid");
        }
        int legLen = Integer.parseInt(fields[1]);
        Dragon d = new Dragon();
        d.name = name;
        d.legLen = legLen;
        return d;
    };

    private Function<String[], Dragon> f2 = fields -> {
        String name = fields[0];
        if (name == null || name.length() == 0) {
            throw new DragonException("name invalid");
        }
        int d1 = Integer.parseInt(fields[1]);
        int stance = Integer.parseInt(fields[2]);
        Dragon d = new Dragon();
        d.name = name;
        d.d1 = d1;
        d.stance = stance;
        return d;
    };

    public List<String> doSmth(String path1, String path2) throws DragonException {
        try {
            List<Dragon> dragons1 = read(path1, 3, f1);
            Map<String, Dragon> name2Dragon = dragons1.stream().collect(Collectors.toMap(Dragon::getName,
                    Function.identity()));
            List<Dragon> dragons2 = read(path2, 3, f2);
            return dragons2.stream().filter(d -> d.stance == 2).map(d -> {
                Dragon dragonInFile1 = name2Dragon.get(d.name);
                if (dragonInFile1 != null) {
                    d.legLen = dragonInFile1.legLen;
                    d.score = d.legLen * d.d1;
                } else {
                    throw new DragonException("not found");
                }
                return d;
            }).sorted((d1, d2) -> Integer.compare(d2.score, d1.score)).map(d -> d.name).collect(Collectors.toList());
        } catch (DragonException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new DragonException(e);
        }
    }

    private List<Dragon> read(String path, int fieldLen, Function<String[], Dragon> func) {
        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {
            String line;
            List<Dragon> res = new ArrayList<>();
            while ((line = bf.readLine()) != null) {
                String[] fields = line.split(" +");
                if (fields == null || fieldLen != fields.length) {
                    throw new DragonException("file invalid, fields len = " + fields.length);
                }
                res.add(func.apply(fields));
            }
            return res;
        } catch (IOException e) {
            //log.error
            throw new DragonException(e);
        }
    }

    class Dragon {
        String name;
        int d1;
        int stance;
        int legLen;
        int score;

        public String getName() {
            return name;
        }

        public String toString() {
            return name + " " + d1 + " " + stance + " " + legLen + " " + score;
        }
    }

    class DragonException extends RuntimeException {
        DragonException(Exception e) {
            super(e);
        }

        DragonException(String msg) {
            super(msg);
        }
    }

    @Test
    public void test() {
        List<String> res = doSmth("/Users/xu.zhang/IdeaProjects/coddding/codding/src/main/java/net/neoremind/mycode" +
                        "/argorithm/other/file1",
                "/Users/xu.zhang/IdeaProjects/coddding/codding/src/main/java/net/neoremind/mycode/argorithm/other" +
                        "/file2");
        System.out.println(res);
    }

}
