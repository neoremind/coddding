package net.neoremind.mycode.argorithm.other;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * @author xu.zx
 */
public class SnakeLadder {

  public static void main(String[] args) throws InterruptedException {
    Scanner scanner = new Scanner(System.in);
    int count = Integer.parseInt(scanner.nextLine());
    String[] str = scanner.nextLine().split(" ");
    int[] a = new int[count];
    for (int i = 0; i < count; i++) {
      a[i] = Integer.parseInt(str[i]);
    }
    System.out.println(getMinDiceThrows(a, count));
  }

  static int getMinDiceThrows(int move[], int count) {
    Queue<Vertex> queue = new LinkedList<>();
    Vertex starter = new Vertex(0, 0);
    queue.add(starter);
    boolean[] visited = new boolean[count];
    Vertex res = null;
    while (!queue.isEmpty()) {
      Vertex v = queue.poll();
      if (v.id == count - 1) {
        res = v;
        break;
      }
      for (int i = v.id + 1; i <= v.id + 6 && i < count; i++) {
        if (visited[i]) {
          continue;
        }
        if (i == count - 1) {
          return v.id + 1;
        }
        Vertex neighbour = new Vertex();
        if (move[i] != 0) {
          neighbour.id = move[i];
        } else {
          neighbour.id = i;
        }
        neighbour.dist = v.dist + 1;
        queue.add(neighbour);
        visited[i] = true;
      }
    }

    if (res == null) {
      return -1;
    }

    return res.dist;
  }

  static class Vertex {
    int id;
    int dist;

    public Vertex() {
    }

    public Vertex(int id, int dist) {
      this.id = id;
      this.dist = dist;
    }

    @Override
    public String toString() {
      return "Vertex{" +
          "id=" + id +
          ", dist=" + dist +
          '}';
    }
  }

}
