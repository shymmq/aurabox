package com.shymmq.aurabox;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by szyme on 25.01.2017.
 */

public class FontTestScene implements Scene {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String SYMBOLS = "`~ !@#$%^&* +=-_,.;:0123456789 {}()[]<>\\|/?";
    private static final String POLISH_SYMBOLS = "ĄĆĘŁŃÓŚŹŻąćęłńóśźż";
    private static final String TEXT =
            ALPHABET + ALPHABET.toUpperCase() + POLISH_SYMBOLS + SYMBOLS;
    private static final int WIDTH = 4 * TEXT.length();
    private final Context context;
    private final static String TAG = "aurabox-debug";
    private Timer timer;
    private int scroll;

    public FontTestScene(Context context) {
        this.context = context;
    }

    @Override
    public Scene start(final AuraboxService service) {
        JSONObject charMap = loadJson("char_map.json");
        try {

            final AuraboxBitmap bitmap = new AuraboxBitmap(Color.BLACK, WIDTH, 10);
            for (int i = 0; i < TEXT.length(); i++) {
                char ch = TEXT.charAt(i);
                long charCode = Long.parseLong(charMap.getString(String.valueOf((int) ch)));
                boolean[][] arr = getBooleanArray(charCode);
                bitmap.print(arr, 4 * i, 2);
            }
            timer = new Timer();
            scroll = 0;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    service.send(bitmap.subBitmap(scroll % WIDTH, 0));
                    scroll++;
                }
            }, 0, 400);
//            service.send(bitmap);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    private boolean[][] getBooleanArray(long charCode) {
        boolean[][] res = new boolean[8][4];
        String log = "";
        for (int i = 0; i < 8; i++) {
            long row = charCode % 16;
//            Log.d(TAG, String.valueOf(row));
            for (int j = 0; j < 4; j++) {
                res[7 - i][3 - j] = ((row >> j) & 1) == 1;
            }
            charCode /= 16;
        }
        for (int dy = 0; dy < 8; dy++) {
            for (int dx = 0; dx < 4; dx++) {
                log += res[dy][dx] ? '#' : '·';
                log += "    ";
            }
            log += '\n';
        }
        Log.d(TAG, log);
        return res;
    }

    private JSONObject loadJson(String filename) {
        try {

            InputStream is = context.getAssets().open(filename);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            JSONObject JSON = new JSONObject(new String(buffer, "UTF-8"));

            return JSON;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public Scene stop() {
        timer.cancel();
        return this;
    }

    @Override
    public String getName() {
        return "Font test";
    }
}
