package net.neoremind.mycode.argorithm.leetcode;

/**
 * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this: (you may want to
 * display this pattern in a fixed font for better legibility)
 * <pre>
 * P   A   H   N
 * A P L S I I G
 * Y   I   R
 * </pre>
 * And then read line by line: "PAHNAPLSIIGYIR"
 * Write the code that will take a string and make this conversion given a number of rows:
 * <p/>
 * string convert(string text, int nRows);
 * convert("PAYPALISHIRING", 3) should return "PAHNAPLSIIGYIR".
 * <p/>
 * 所谓的zigzag就是按照某种pattern去打印字符串。这道题解的实际就是一个找“规律”的问题。
 * 我的解法是用笔和纸，按照numRows=2，3，4，5去尝试画出来实际的样子，然后计算
 * 1）首先要区分单元unit，就是所谓的一个一个小格子，比如
 * numRows=3，那么ABCD，EFGH，IJK各自是一个单元，单元内元素的个数等于(numRows - 1)*2。
 * 2）计算出一共有多少个单元unit。
 * 3）一个单元一个单元的打印出来，小于numRows的很好安置位置，大于numRows的元素有一个规律，
 * 就是他们是row--，col++的斜线往上走的，掌握的这个规律，就可以把一个char[][]的二维数组
 * 看出我们要打印出来的样子，去确定这个row和col，遍历字符串填充进去即可。
 * <p/>
 * //TODO 特别要说的，在leetcode提交这道题只打败了12.4%的JAVA程序，还可以有性能优化的空间。
 *
 * @author zhangxu
 * @see <a href="https://leetcode.com/problems/zigzag-conversion/">zigzag-conversion</a>
 */
public class ZigZagConversion {

    public static String convert(String s, int numRows) {
        if (s == null || s.length() == 0) {
            return "";
        }
        if (numRows == 1) {  // 行数小于1直接打印
            return s;
        }
        int totalCharNum = s.length();  // 字符串s的所有字符数：totalCharNum
        if (totalCharNum == 1) {  // 字符串只有一个，直接返回
            return s;
        }

        int unitColumnNum = (numRows - 1);  // 每个单元的列数：unitColumnNum
        int charNumPerUnit = (numRows - 1) * 2; // 每个单元内的字符数：charNumPerUnit
        int unitNum = totalCharNum / charNumPerUnit;  // 总的单元数
        if (totalCharNum % charNumPerUnit != 0) {
            unitNum++;
        }

        // 二维数组的列数
        int numColumns = unitNum * unitColumnNum;
        // 空的二维数组，元素都是'\u0000'
        char[][] charMatrix = new char[numRows][numColumns];

        // 遍历字符串s，按照单元外层循环
        for (int unitIndex = 0; unitIndex < unitNum; unitIndex++) {
            int strStartIndex = unitIndex * charNumPerUnit;  // 字符串的起始索引
            int strEndIndex = strStartIndex + charNumPerUnit;  // 字符串的终止索引
            int diagonalStartRow = numRows - 2;  // 斜线上的起始行
            int diagonalStartColumn = unitIndex * unitColumnNum + 1; // 斜线上的起始列
            for (int i = strStartIndex; i <= strEndIndex; i++) {  // 遍历单元内的字符
                if (i >= totalCharNum) {
                    break;
                }
                if (i < strStartIndex + numRows) {  // 字符小于numRows的，确定行和列
                    int x = i % charNumPerUnit;
                    int y = (numRows - 1) * unitIndex;
                    charMatrix[x][y] = s.charAt(i);
                } else { // 字符大于numRows的，确定行和列
                    int x = diagonalStartRow;
                    int y = diagonalStartColumn;
                    diagonalStartRow--;
                    diagonalStartColumn++;
                    charMatrix[x][y] = s.charAt(i);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numRows; i++, sb.append("\n")) {
            // for (int i = 0; i < numRows; i++) {  如果想正确提交leetcode请打开注释
            for (int j = 0; j < numColumns; j++) {
                char tmp = charMatrix[i][j];
                if (tmp == '\u0000') {
                    sb.append(" "); // 如果想正确提交leetcode请删除此行
                    continue;
                }
                sb.append(tmp);
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(convert(getStr(2), 3));
        System.out.println(convert(getStr(12), 4));
        System.out.println(convert(getStr(56), 5));
    }

    /**
     * 生成字符串，按照26个字符顺序、循环产出
     *
     * @param charNum 字符串数
     *
     * @return 待测试的字符串
     */
    private static String getStr(int charNum) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int times = charNum / 26;
        if (charNum % 26 != 0) {
            times++;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= times; i++) {
            sb.append(alphabet);
        }
        return sb.toString().substring(0, charNum);
    }
}
