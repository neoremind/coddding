package net.neoremind.mycode.argorithm.disjsets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 迷宫小程序，使用不相交集{@link DisjSets}算法实现
 *
 * @author zhangxu
 */
public class Maze extends JFrame {

    private static final long serialVersionUID = 4581809619616228704L;

    /**
     * 迷宫尺寸-宽，高
     */
    int m = 30, n = 30;

    /**
     * 迷宫房间尺寸(正方形)
     */
    int rSize = 10;

    /**
     * 迷宫起点距离窗口距离(离左、上的距离d*rSize)
     */
    int d = 10;

    /**
     * 窗口尺寸-高
     */
    static int windowHeight = 700;

    /**
     * 窗口尺寸-宽
     */
    static int windowWidth = 700;

    /**
     * 面板panel
     */
    JPanel panel = new JPanel();

    public Maze() {
        panel.setLayout(null);
        add(panel, BorderLayout.CENTER);
    }

    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, windowWidth, windowHeight);
        g.setColor(Color.black);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                g.drawRect(rSize * (i + d), rSize * (j + d), rSize, rSize);
            }
        }
        //迷宫的出口和入口打开
        g.setColor(Color.white);
        //入口横墙      -1、+1是为了不在隔壁墙上留下小缺口(后面画线时同理)
        g.drawLine(rSize * d, rSize * d, rSize * (d + 1) - 1, rSize * d);
        //入口竖墙
        g.drawLine(rSize * d, rSize * d, rSize * d, rSize * (d + 1) - 1);
        //出口横墙
        g.drawLine((n - 1 + d) * rSize + 1, (m + d) * rSize, (n + d) * rSize, (m + d) * rSize);
        //出口竖墙
        g.drawLine((n + d) * rSize, (m - 1 + d) * rSize + 1, (n + d) * rSize, (m + d) * rSize);

        //不相交集定义
        DisjSets dSets = new DisjSets(m * n);
        Random random = new Random();

        //房间0、m*n-1没有连通
        while (dSets.find(0) != dSets.find(m * n - 1)) {
            //随机生成一个房间号a  0<=a<=m*n-1
            int a = random.nextInt(m * n);
            //a的相邻房间用list存放
            List<Integer> neighbor = new ArrayList<>(6);
            //分别判断a的上、右、下、左房间是否存在，若存在放入neighbor
            if (a - n >= 0) {
                neighbor.add(a - n);
            }
            if (a + 1 < ((int) (a / n) + 1) * n) {
                neighbor.add(a + 1);
            }
            if (a + n < m * n) {
                neighbor.add(a + n);
            }
            if (a - 1 >= ((int) (a / n)) * n) {
                neighbor.add(a - 1);
            }
            //生成随机数index, 0<=index<=neighbor.size()-1
            int index = random.nextInt(neighbor.size());
            //b房间是a的相邻房间号，我们考察这两个房间是否连通
            int b = neighbor.get(index);

            //a、b是否连通
            if (dSets.find(a) == dSets.find(b)) {//a、b连通的话重新考察新的房间
                continue;
            } else {
                //a、b不连通，union它们的集合
                int seta = dSets.find(a);
                int setb = dSets.find(b);
                dSets.union(seta, setb);
                //接下来拆掉它们之间的"墙"
                //首先得到较小房间号
                int s = Math.min(a, b);
                //计算"墙"的坐标
                int x1, y1, x2, y2;
                //两房间编号差是1，隔开它们的是竖墙
                if (Math.abs(a - b) == 1) {
                    //起点
                    if (s < n) {
                        x1 = (s + 1 + d) * rSize;
                    } else {
                        x1 = (s % n + 1 + d) * rSize;
                    }
                    y1 = ((int) (s / n) + d) * rSize + 1;
                    //终点
                    x2 = x1;
                    y2 = ((int) (s / n) + 1 + d) * rSize - 1;
                } else {
                    //否则是横墙
                    //起点
                    if (s < n) {
                        x1 = (s + d) * rSize + 1;
                    } else {
                        x1 = (s % n + d) * rSize + 1;
                    }
                    y1 = ((int) (s / n) + 1 + d) * rSize;
                    //终点
                    if (s < n) {
                        x2 = (s + 1 + d) * rSize - 1;
                    } else {
                        x2 = (s % n + 1 + d) * rSize - 1;
                    }
                    y2 = y1;
                }
                //拆墙，实际上是用白线把墙抹掉
                g.setColor(Color.white);
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }

    public static void main(String[] args) {
        Maze maze = new Maze();
        maze.setTitle("迷宫");
        maze.setSize(windowWidth, windowHeight);
        maze.setVisible(true);
        maze.setLocationRelativeTo(null);
        maze.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

