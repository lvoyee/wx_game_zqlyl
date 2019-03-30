package com.wxgame.lianyilian;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;


/**
 * @Auther: lvoyee
 * @Date: 2019-02-22
 * @Description: NodeUtil
 */
public class NodeUtil {


    public final static char BAN = ' ', EMPTY = '□', EXIST = '■';

    private static int startH = 541, startW = 170, offsetH = 145, offsetW = 145, rowSize = 7, colSize = 6;


    // 自动玩
    public static void autoPlay() {
        try {
            if (Files.notExists(Paths.get("D:/link")))
                Files.createDirectory(Paths.get("D:/link"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            long startTime = System.currentTimeMillis();
            if (screenshot()) {
                GameStatus gameStatus = analysisScreenshot();
                if (gameStatus != null) {
                    NodeTree nodeTree = new NodeTree(gameStatus.getStatus(), gameStatus.getCount(), gameStatus.getStartRow(), gameStatus.getStartCol());
                    Node[] nodes = nodeTree.DFS();
                    if (nodes != null) {
                        play(nodes);
                        nextGame();
                    } else {
                        System.out.println("游戏搜索失败，重新开始");
                    }
                } else {
                    System.out.println("游戏读取失败，重新开始");
                }
                System.out.println("耗费总时长:" + (System.currentTimeMillis() - startTime) + "毫秒\n");
            }
        }
    }

    // 截屏到电脑
    private static boolean screenshot() {
        try {
            Runtime.getRuntime().exec("adb shell /system/bin/screencap -p /sdcard/most_link_link.png").waitFor();
            Runtime.getRuntime().exec("adb pull /sdcard/most_link_link.png D:/link").waitFor();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 分析游戏状态
    public static GameStatus analysisScreenshot() {
        GameStatus gameStatus = new GameStatus();
        char[][] status = new char[rowSize][colSize];
        int count = 0, startPoint = 0;
        try {
            BufferedImage screenshot = ImageIO.read(new File("D:/link/most_link_link.png"));
//			Graphics graphics = screenshot.getGraphics();
//			graphics.setColor(Color.red);
//			graphics.setFont(new Font("华文行楷", Font.BOLD, 100));
            for (int h = startH, rowIndex = 0; rowIndex < status.length && startPoint <= 1; h += offsetH, rowIndex++) {
                for (int w = startW, colIndex = 0; colIndex < status[rowIndex].length && startPoint <= 1; w += offsetW, colIndex++) {
                    int rgb = screenshot.getRGB(w, h);
//					graphics.drawString(".", w, h);
                    if (rgb == -3355444) { // -3355444灰色
                        status[rowIndex][colIndex] = EMPTY;
                        count++;
                    } else if (rgb != -14472389) { // -14472389背景色
                        status[rowIndex][colIndex] = EXIST;
                        count++;
                        gameStatus.setStartRow(rowIndex);
                        gameStatus.setStartCol(colIndex);
                        startPoint++;
                    } else {
                        status[rowIndex][colIndex] = BAN;
                    }
                }
            }

            gameStatus.setCount(count);
            gameStatus.setStatus(status);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            printStatus(status);
        }
        return startPoint == 1 ? gameStatus : null;
    }

    // 点击关卡灰格
    private static void play(Node[] nodes) {
        try {
            for (int i = 1; i < nodes.length; i++) {
                int j = i;
                while (j + 1 < nodes.length && (nodes[i].getRow() == nodes[j + 1].getRow() || nodes[i].getCol() == nodes[j + 1].getCol())) {
                    j++;
                }
                String command = String.format("adb shell input swipe %d %d %d %d", nodes[i].getCol() * offsetW + startW, nodes[i].getRow() * offsetH + startH, nodes[j].getCol() * offsetW + startW, nodes[j].getRow() * offsetH + startH);
				System.out.println(command);
                Runtime.getRuntime().exec(command).waitFor();
                i = j;
            }
            //Thread.sleep(300);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 下一关
    private static void nextGame() {
        try {
            //Runtime.getRuntime().exec("adb shell input tap 929 660").waitFor(); // 关闭双倍奖励
            //Runtime.getRuntime().exec("adb shell input tap 540 1600").waitFor(); // 下一关
            Runtime.getRuntime().exec("adb shell input tap 925 822").waitFor(); // 关闭双倍奖励
            Runtime.getRuntime().exec("adb shell input tap 540 1700").waitFor(); // 下一关
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // 打印盘面状态
    public static void printStatus(char[][] status) {
        for (int row = 0; row < status.length; row++) {
            for (int col = 0; col < status[row].length - 1; col++) {
                System.out.format("%c ", status[row][col]);
            }
            System.out.format("%c%n", status[row][status[row].length - 1]);
        }
    }

    private static class GameStatus {
        private char[][] status;
        private int count, startRow, startCol; // count方格数量

        public char[][] getStatus() {
            return status;
        }
        public void setStatus(char[][] status) {
            this.status = status;
        }
        public int getCount() {
            return count;
        }
        public void setCount(int count) {
            this.count = count;
        }
        public int getStartRow() {
            return startRow;
        }
        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }
        public int getStartCol() {
            return startCol;
        }
        public void setStartCol(int startCol) {
            this.startCol = startCol;
        }
    }


}
