package com.shymmq.aurabox;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by szyme on 24.01.2017.
 */

public class ScrollTestScene implements Scene {
    private static final long FPS = 30;
    private final int WIDTH = 30;
    private Timer timer;
    private int scroll = 0;
    private AuraboxBitmap bitmap = new AuraboxBitmap(Color.BLACK, WIDTH, 10);

    @Override
    public Scene start(final AuraboxService service) {
        timer = new Timer();
        for (int i = 0; i < WIDTH; i++) {
            bitmap.setPixel(i, getHeight(i), Color.WHITE);
            bitmap.setPixel(i, getHeight(i+WIDTH/2), Color.CYAN);
//            int top = (int) Math.ceil(height);
//            int bot = (int) Math.floor(height);
//            cells[bot][i] = true;
//            cells[top][i] = true;
        }
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                service.send(bitmap.subBitmap(scroll, 0));
                scroll++;
            }
        }, 0, 1000 / FPS);
        return this;
    }

    private int getHeight(int i) {
        double sin = Math.sin(i * 2 * Math.PI / WIDTH);
        double height = (sin + 1) * 9 / 2;
        return (int) Math.round(height);
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
