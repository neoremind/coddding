package net.neoremind.mycode;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

public class HelloWorld {

    public static void main(String[] args) {
        List<Integer> list = Lists.newArrayList(0, 1, 2, 3, 4, 5);
        List<Integer> list1 = Lists.newArrayList();
        for (int i = 3; i < list.size(); i++) {
            list1.add(list.get(i));
        }
        System.out.println(list1);
        List<Integer> list2 = Lists.newArrayList();
        list2.addAll(list.subList(3, list.size()));
        System.out.println(list2);

        System.out.println("hello world");
        System.out.println(1 << 31);

        Random ran = new Random(100);
        for (int i = 0; i < 10; i++) {
            System.out.println(Math.abs(ran.nextInt()) % 100);
        }

        int start = 1;
        int end = 8;
        System.out.println((start + end) / 2);
        System.out.println((start + end) >>> 1);
        System.out.println((start + end) << 1);

        System.out.println((Integer.MAX_VALUE + 1) & (1 << 31)); //-2147483648
        System.out.println((Integer.MAX_VALUE) & (1 << 31)); //0
        System.out.println((Integer.MIN_VALUE >> 31) & 1); //1
        System.out.println((Integer.MIN_VALUE >> 32) & 1); //0
        System.out.println((Integer.MIN_VALUE >>> 31) & 1); //1
        System.out.println((Integer.MIN_VALUE >>> 32) & 1); //0
    }

}
