package net.neoremind.mycode.fj;

import static fj.data.List.list;

import fj.F;
import fj.Function;
import fj.Ord;
import fj.data.List;
import fj.data.TreeMap;

public final class List_fold {

    public static void main(final String... args) {
        foldLeftDemo();
    }

    /**
     * 计算所有的单词中字母的个数
     */
    private static void foldLeftDemo() {
        System.out.println("foldLeftDemo");
        final List<String> words = list("Hello", "World", "how", "are", "your", "doing");
        F<Integer, F<String, Integer>> len = accNum -> (word -> word.length() + accNum);
        int totalCharNum = words.foldLeft(len, 0);
        System.out.println(totalCharNum);
    }

}
