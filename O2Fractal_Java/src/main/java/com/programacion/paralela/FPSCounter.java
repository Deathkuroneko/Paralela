package com.programacion.paralela;

public class FPSCounter {

    private static int fps;
    private static int frame;
    private static long lasdTime;

    public FPSCounter() {
        fps = 0;
        frame = 0;
        lasdTime = System.currentTimeMillis();
    }

    public static int update(){
        frame ++;
        long now = System.currentTimeMillis();

        if(now-lasdTime>100){
            fps = frame;
            frame = 0;
            lasdTime = now;
        }
        return fps;
    }
}
