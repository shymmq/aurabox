package com.shymmq.aurabox;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by szyme on 24.01.2017.
 */

public class ScrollTestScene implements Scene {
    private static final long FPS = 30;
    private final int WIDTH = 30;
    private boolean[][] cells = new boolean[10][WIDTH];
    private Timer timer = new Timer();
    private int scroll = 0;

    @Override
    public Scene start(final AuraboxService service) {
        for (int i = 0; i < WIDTH; i++) {
            double sin = Math.sin(i * 2 * Math.PI / WIDTH);
            double height = (sin + 1) * 9 / 2;
            cells[(int) Math.round(height)][i] = true;
//            int top = (int) Math.ceil(height);
//            int bot = (int) Math.floor(height);
//            cells[bot][i] = true;
//            cells[top][i] = true;
        }
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                service.send(new AuraboxBitmap(cells, scroll, 0));
                scroll++;
            }
        }, 0, 1000 / FPS);
        return this;
    }

    @Override
    public Scene stop() {
        timer.cancel();
        return this;
    }

    @Override
    public String getName() {
        return "Scroll test";
    }
}
