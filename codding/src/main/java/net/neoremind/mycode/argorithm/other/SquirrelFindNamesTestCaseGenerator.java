package net.neoremind.mycode.argorithm.other;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * SquirrelFindNamesTestCaseGenerator
 *
 * @author xu.zx
 */
public class SquirrelFindNamesTestCaseGenerator {

  private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public static void main(String[] args) {
    SquirrelFindNamesTestCaseGenerator generator = new SquirrelFindNamesTestCaseGenerator();
    generator.gen(300, 300, 2, 3);
  }

  public void gen(int rows, int cols, int namesCount, int minLength) {
    char[][] board = createBoard(rows, cols);
    Random random = new Random(System.nanoTime());
    int nameLengthLimit = Math.min(rows * cols / 3, 200);
    List<String> names = new ArrayList<>(namesCount);
    for (int i = 0; i < namesCount; i++) {
      names.add(createPathRandomly(board, minLength + random.nextInt(nameLengthLimit)));
    }
    System.out.println(names.stream().collect(Collectors.joining(",")));
    System.out.println(rows + " " + cols);
    print(board, rows, cols);
  }

  public String createPathRandomly(char[][] board, int pathLength) {
    System.out.println(pathLength + "=");
    int rows = board.length;
    int cols = board[0].length;
    if (pathLength < 1) {
      return "";
    }
    if (pathLength > rows * cols) {
      throw new IllegalArgumentException("path too long");
    }
    Random random = new Random(System.nanoTime());
    int row = random.nextInt(rows);
    int col = random.nextInt(cols);
    int[][] d = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    StringBuilder str = new StringBuilder();
    boolean[][] visited = new boolean[rows][cols];
    backtrack(row, col, rows, cols, board, d, random, str, visited, pathLength);
    if (str.length() != pathLength) {
      throw new IllegalStateException("create path failed");
    }
    return str.toString();
  }

  boolean backtrack(int row, int col, int rows, int cols, char[][] board, int[][] d, Random random, StringBuilder str, boolean[][] visited, int pathLength) {
    if (str.length() == pathLength) {
      return true;
    }
    int directionIdx = random.nextInt(4);
    for (int i = 0; i < 4; i++) {
      int[] next = getNextPoint(row, col, rows, cols, d[directionIdx][0], d[directionIdx][1]);
      if ((next[0] == row && next[1] == col) || visited[next[0]][next[1]]) {
        ++directionIdx;
        if (directionIdx == 4) {
          directionIdx = 0;
        }
        continue;
      }
      visited[next[0]][next[1]] = true;
      str.append(board[next[0]][next[1]]);
      if (backtrack(next[0], next[1], rows, cols, board, d, random, str, visited, pathLength)) {
        return true;
      }
      visited[next[0]][next[1]] = false;
      str.deleteCharAt(str.length() - 1);
    }
    return false;
  }

  int[] getNextPoint(int row, int col, int rows, int cols, int incrX, int incrY) {
    if (row + incrX >= 0 && row + incrX < rows && col + incrY >= 0 && col + incrY < cols) {
      row += incrX;
      col += incrY;
    }
    return new int[]{row, col};
  }

  public void print(char[][] board, int rows, int cols) {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        System.out.print(board[i][j]);
      }
      System.out.println();
    }
  }

  public char[][] createBoard(int rows, int cols) {
    Random random = new Random(System.nanoTime());
    char[][] board = new char[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        board[i][j] = ALPHABET.charAt(random.nextInt(26));
      }
    }
    return board;
  }

}
