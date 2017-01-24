package com.shymmq.aurabox;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by szyme on 24.01.2017.
 */

public class GOLScene implements Scene {
    private String TAG = "aurabox-debug";
    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;
    private static final int SPAWN_RATE = 19 * 2;       //min 19
    private static final long GEN_PERIOD = 200;
    private boolean[][] cells = new boolean[HEIGHT][WIDTH];
    private boolean[][] newCells = new boolean[HEIGHT][WIDTH];
    private Timer timer = new Timer();
    private int generation = 0;

    @Override
    public GOLScene start(final AuraboxService service) {
        AuraboxBitmap bitmap = new AuraboxBitmap(cells);
        service.send(bitmap);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "Gen " + generation);
                newCells = new boolean[HEIGHT][WIDTH];
                for (int y = 0; y < HEIGHT; y++) {
                    for (int x = 0; x < WIDTH; x++) {
                        int neighbours = countNeighbours(x, y);
                        newCells[y][x] = neighbours == 3 || (cells[y][x] && neighbours == 2);
                    }
                }
                cells = newCells;
                if (generation % SPAWN_RATE == 0) {
                    spawnGlider(0, 0);
                }
                if (generation % SPAWN_RATE == SPAWN_RATE / 2) {
                    spawnInvertedGlider(WIDTH - 3, 0);
                }
                generation++;
                service.send(new AuraboxBitmap(cells, HEIGHT / 2 - 5, WIDTH / 2 - 5));
            }
        }, 0, GEN_PERIOD);
        return this;
    }

    @Override
    public GOLScene stop() {
        timer.cancel();
        return this;
    }

    @Override
    public String getName() {
        return "Game Of Life";
    }

    private void setCells(int startY, int startX, int... coords) {
        int i = 0;
        while (i < coords.length) {
            int y = coords[i] + startY;
            int x = coords[i + 1] + startX;
            cells[y][x] = true;
            i += 2;
        }
    }

    private int countNeighbours(int x, int y) {
        int res = 0;
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int nx = x + dx;
                int ny = y + dy;
                if (!(dx == 0 && dy == 0) &&
                        0 <= nx && nx < WIDTH &&
                        0 <= ny && ny < HEIGHT &&
                        cells[ny][nx]) {
                    res++;
                }
            }
        }
        return res;
    }

    private void spawnGlider(int x, int y) {
        setCells(y, x,
                0, 1,
                1, 2,
                2, 0,
                2, 1,
                2, 2);
    }

    private void spawnInvertedGlider(int x, int y) {
        setCells(y, x,
                0, 1,
                1, 0,
                2, 0,
                2, 1,
                2, 2);
    }
}
