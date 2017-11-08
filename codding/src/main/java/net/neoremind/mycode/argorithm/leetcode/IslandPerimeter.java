package net.neoremind.mycode.argorithm.leetcode;

/**
 * You are given a map in form of a two-dimensional integer grid where 1 represents land and 0 represents water. Grid
 * cells are connected horizontally/vertically (not diagonally). The grid is completely surrounded by water, and
 * there is exactly one island (i.e., one or more connected land cells). The island doesn't have "lakes" (water
 * inside that isn't connected to the water around the island). One cell is a square with side length 1. The grid is
 * rectangular, width and height don't exceed 100. Determine the perimeter of the island.
 * <p>
 * Example:
 * <p>
 * [[0,1,0,0],
 * [1,1,1,0],
 * [0,1,0,0],
 * [1,1,0,0]]
 * <p>
 * Answer: 16
 * Explanation: The perimeter is the 16 yellow stripes in the image below:
 *
 * @author xu.zhang
 */
public class IslandPerimeter {

    public int islandPerimeter2(int[][] grid) {
        int islands = 0, neighbours = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 1) {
                    islands++; // count islands
                    if (i < grid.length - 1 && grid[i + 1][j] == 1) neighbours++; // count down neighbours
                    if (j < grid[i].length - 1 && grid[i][j + 1] == 1) neighbours++; // count right neighbours
                }
            }
        }

        return islands * 4 - neighbours * 2;
    }

    public int islandPerimeter(int[][] grid) {
        if (grid == null) {
            return 0;
        }
        int count = 0;
        int rows = grid.length;
        if (rows == 0) { //corner case
            return 0;
        }
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 1 && !visited[i][j]) {
                    dfs(grid, visited, i, j, rows, cols);
                    count++;
                }
            }
        }
        return x;
    }

    int x = 0;

    void dfs(int[][] grid, boolean[][] visited, int i, int j, int rows, int cols) {
        if (i < 0 || j < 0 || i >= rows || j >= cols || grid[i][j] == 0) {
            x++;
            return;
        }
        if (visited[i][j]) {
            return;
        }
        visited[i][j] = true;
        dfs(grid, visited, i + 1, j, rows, cols);
        dfs(grid, visited, i, j + 1, rows, cols);
        dfs(grid, visited, i - 1, j, rows, cols);
        dfs(grid, visited, i, j - 1, rows, cols);
    }

}
