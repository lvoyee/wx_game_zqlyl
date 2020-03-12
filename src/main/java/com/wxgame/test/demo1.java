package com.wxgame.test;

import java.io.IOException;

/**
 * @Auther: Administrator
 * @Date: 2019-10-07
 * @Description:
 */
public class demo1 {

    public static void main(String[] args) {
        screenshot();


        // news
//        click();
//
        for (int i=0;i<9999;i++){
            huadong();
            System.out.println("执行了第："+i+"次...");
        }

        // video
        //clickVideo();

        // 1800 金币  遍历 120 次即可  每次15 金币
        /*for (int i=1;i<121;i++){
            nextVideo();
            System.out.println("=============== now i:"+i);
        }
*/


        System.out.println("=============== task end ===============");

    }

    private static boolean screenshot() {
        try {
            Runtime.getRuntime()
                    .exec("adb shell /system/bin/screencap -p /sdcard/screenshot.png");
            Thread.sleep(1000);

            // 上传手机截图到电脑
            Runtime.getRuntime()
                    .exec("adb pull /sdcard/screenshot.png C:/Users/Administrator/Downloads/screenshot.png");

            System.out.println("=============== Get screenshot success ===============");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private static void click(){

        try {
            Runtime.getRuntime()
                    .exec("adb shell input tap 340 550");
            Thread.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static void huadong(){
        System.out.println("---------------");

        try {
            Runtime.getRuntime()
                    .exec("adb shell input swipe 540 480 540 100 ");
            int i = (int)(10000+Math.random()*(20000-19000));
            System.out.println("sleep time:"+i);
            Thread.sleep(i);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // --------------- Video ------------------


    // 视频点击
    private static void clickVideo(){

        try {
            Runtime.getRuntime()
                    .exec("adb shell input tap 540 510");
            Thread.sleep(60000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void nextVideo(){
        try {
            Runtime.getRuntime()
                    .exec("adb shell input tap 300 1365");
            Thread.sleep(59000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
