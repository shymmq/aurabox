package com.shymmq.aurabox;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.PI;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;
import static java.lang.Math.tan;

/**
 * Created by szyme on 24.01.2017.
 */

public class LineScene implements Scene {
    //    final private static double ANGLE = PI / 48;
    final private static double DIVISOR = 96;
    AuraboxBitmap bitmap = new AuraboxBitmap();
    Timer timer;
    int iter;

    @Override
    public Scene start(final AuraboxService service) {
        iter = 0;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                bitmap.fill(Color.BLACK);
                double a = (iter% DIVISOR) * 2 * PI / DIVISOR;
                for (int x = 0; x < 10; x++) {
                    double pos = x - 4.5;
                    double res = tan(a) * pos;
//                    if (res > -10 && res < 10) {
//                        res = round(16 * res) / 16;
//                    }

                    int startY = res > 5
                            ? 0
                            : res < -5
                            ? 10
                            : (int) round(4.5 - res);
//                    Log.d("aurabox-debug", "x: " + x + "\n" +
//                            "angle: " + toDegrees(a) + "\n" );
//                            "startY: " + startY + "\n" +
//                            "pos: " + pos + "\n" +
//                            "res: " + res + "\n");
                    if (a > PI / 2 && a <= 3 * PI / 2) {
                        startY = min(startY, 10);
                        for (int y = 0; y < startY; y++) {
                            bitmap.setPixel(x, y, true);
                        }
                    } else {
                        startY = max(startY, 0);
                        for (int y = startY; y < 10; y++) {
                            bitmap.setPixel(x, y, true);
                        }
                    }
                }
                service.send(bitmap);
                iter++;
            }
        }, 0, 50);

        return this;
    }

    @Override
    public Scene stop() {
        timer.cancel();
        return this;
    }

    @Override
    public String getName() {
        return "Line test";
    }
}
