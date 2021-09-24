package net.neoremind.mycode.temp;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class Main2 {

    /*
**Question**: Design and implement a solution to generate a book reading plan in Kindle, with following constraints:
* user will provide the number of days in which book should be finished.
* - chapter should start and end on the same day.
* - reading workload should be uniform across days.
* - chapters to be read in order.

input: book, user days

number of pages,


output: list<int[]> 0: 123 1:45  2:67

*/

    public static void main(String[] args) {
        Main2 main = new Main2();
        Book book = new Book();
//        book.numOfPages = 28;
//        book.chapterPages = Lists.newArrayList(8,9,4,7);
//        System.out.println(main.func(book, 3));

        book.numOfPages = 24;
        book.chapterPages = Lists.newArrayList(8,5,4,7);
        System.out.println(main.func(book, 3));

        book.numOfPages = 24;
        book.chapterPages = Lists.newArrayList(3,5,9,7);
        System.out.println(main.func(book, 3));
    }

    static class Book {
        int numOfPages;
        List<Integer> chapterPages;
    }

    /**
     * days: 3
     *     1        2             3            4
     * +------+--------------+------------+-------+
     *             |                |
     *
     * int[] a = {5, 10, 9, 4};
     * averagepage = 28 / 3 = 9
     *
     * day1: 1,2
     * day2: 3
     * day3: 4
     *
     *
     * int[] a = {8, 9, 4, 7};
     * average = 9
     *
     * 8
     * 8+9=17 exceed average, compare abs(17-9) between abs(9-8)
     *
     * Day1: 8
     * Day2: 9,4
     * Day3: 7
     *
     *
     *
     */
    List<List<Integer>> func(Book book, int days) {
        double avgPagesPerDay = book.numOfPages / days;
        List<List<Integer>> result = new ArrayList<>(days);
        int lastEndIndex = 0;
        int cumulativePages = 0;
        for (int i = 0; i < book.chapterPages.size(); i++) {
            int currPages = book.chapterPages.get(i);
            cumulativePages += currPages;
            if (cumulativePages > avgPagesPerDay) {
                // 8+9=17 exceed average, compare abs(17-9average) between abs(9average-8)
                if ((cumulativePages - avgPagesPerDay) >
                        Math.abs(avgPagesPerDay - book.chapterPages.get(i - 1))){
                    // add all pages except i to the result
                    build(lastEndIndex, i, result);
                    i--;
                    lastEndIndex = i + 1;
                } else {
                    // add pages including i to the result
                    build(lastEndIndex, i + 1, result);
                    lastEndIndex = i + 1;
                }
                cumulativePages = 0;
            }
        }
        if (cumulativePages > 0) {
            build(lastEndIndex, book.chapterPages.size(), result);
        }
        return result;
    }

    private void build(int lastEndIndex, int i, List<List<Integer>> result) {
        List<Integer> chapterToFinishCurrentDay = new ArrayList<>();
        for (int j = lastEndIndex; j < i; j++) {
            chapterToFinishCurrentDay.add(j);
        }
        result.add(chapterToFinishCurrentDay);
    }

}
