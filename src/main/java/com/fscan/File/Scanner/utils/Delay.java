package com.fscan.File.Scanner.utils;

public class Delay {

    public static void delayInMin(double min){
        try {
            long milliSeconds = (long) (min * 60000);//
            Thread.sleep(milliSeconds);
        }catch (Exception e){
            return;
        }
    }
}
