package net.neoremind.mycode.argorithm.other;

import java.util.Scanner;

/**
 * # 题目：小松鼠找名字
 *
 * UC小松鼠不光可爱，圆圆的大眼睛也是灵气十足，能够在纷杂的世界中捕捉到一切信息。每个小松鼠都有一个英文名字，现在小松鼠们参加UC Code大赛，每个人的名字都隐藏在一个字母墙里面，小松鼠们要动用眼里迅速找到自己的名字是否在墙里出现。
 *
 * 英文名字在2维的字母墙里面必须是一条连续的路径，顺序包括英文名字的所有字母。所谓连续是指下一个字母必须是上一个字母的“邻居”，上下左右都可以，但是已经在路径里面使用过的字母不能再次访问，即“不走回头路”。
 *
 * 例如小松鼠叫做ANDY，字母墙是：
 *
 * ```
 * OAN
 * AXD
 * ZUY
 * ```
 *
 * 那么可以找到名字一条连续的路径就是：
 *
 * ```
 * AN
 * D
 * Y
 * ```
 *
 * 例如小松鼠叫做JOHN，字母墙是：
 *
 * ```
 * OJO
 * XHI
 * ZMB
 * ```
 *
 * 那么就找不到名字了。
 *
 * 例如小松鼠叫做BOB，字母墙是：
 *
 * ```
 * BOX
 * JKL
 * ```
 *
 * 因为“不走回头路”，所以也找不到名字。
 *
 * ## Input Format
 *
 * 第一行是各个小松鼠的英文名字，按照逗号分隔。名字长度小于字母墙的width*hight。
 * 第二行是字母墙的大小，包含width、hight，空格分开，1<=width<=500，1<=hight<=500。
 * 从第三行开始就是字母墙了，每个字符是一个大写英文字母。
 *
 * ## Output Format
 * 各个小松鼠的名字是否可以在字母墙里找到。
 *
 * ## Sample Input
 *
 * 3只小松鼠分别叫做JACKMA,JACKBA,ACA，检查在一个4*3的字母墙中，是否可以找到名字。
 *
 * ```
 * JACKMA,JACKBA,ACA
 * 4 3
 * XJKO
 * CKCA
 * NYKM
 * ```
 *
 * ## Sample Output
 *
 * 例如马爸爸的小松鼠名字叫做JACKMA，可以找到名字。其他两只不行。
 *
 * ```
 * true
 * false
 * false
 * ```
 *
 * @author xu.zx
 */
public class SquirrelFindNames {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String[] names = scanner.nextLine().split(",");
    String[] rowsAndCols = scanner.nextLine().split(" ");
    int rows = Integer.parseInt(rowsAndCols[0]);
    int cols = Integer.parseInt(rowsAndCols[1]);
    char[][] board = new char[rows][cols];
    for (int i = 0; i < rows; i++) {
      String line = scanner.nextLine();
      for (int j = 0; j < cols; j++) {
        board[i][j] = line.charAt(j);
      }
    }
    for (String name : names) {
      System.out.println(exist(board, name));
    }
  }

  public static boolean exist(char[][] board, String word) {
    if (word == null || word.length() == 0) {
      return true;
    }
    int row = board.length;
    int col = board[0].length;
    boolean[][] visited = new boolean[row][col];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        if (dfs(board, visited, word, row, col, i, j, 0)) {
          return true;
        }
      }
    }
    return false;
  }

  static int[][] d = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

  static boolean dfs(char[][] board, boolean[][] visited, String word, int row, int col, int i, int j, int index) {
    if (index == word.length()) {
      return true;
    }
    if (i < 0 || i >= row || j < 0 || j >= col || visited[i][j]) {
      return false;
    }
    if (word.charAt(index) != board[i][j]) {
      return false;
    }
    visited[i][j] = true;
    for (int k = 0; k < 4; k++) {
      if (dfs(board, visited, word, row, col, i + d[k][0], j + d[k][1], index + 1)) {
        return true;
      }
    }
    visited[i][j] = false;
    return false;
  }

}
