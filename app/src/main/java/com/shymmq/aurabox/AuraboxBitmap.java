package com.shymmq.aurabox;

//import com.google.common.base.Preconditions;

/**
 * Created by szyme on 22.01.2017.
 */

public class AuraboxBitmap {
    private final Color[][] pixels;
    private final int height;
    private final int width;

    public AuraboxBitmap(Color[][] pixels) {
        this.pixels = pixels;
        height = pixels.length;
        width = pixels[0].length;
    }

    public AuraboxBitmap() {
        this(Color.BLACK);
    }

    public AuraboxBitmap(Color color) {
        this(color, 10, 10);
    }

    public AuraboxBitmap(Color color, int width, int height) {
        this.pixels = new Color[height][width];
        this.width = width;
        this.height = height;
        fill(color);
    }

    public AuraboxBitmap(String source) {
//        Preconditions.checkArgument(source.length() == 100);
        this.height = 10;
        this.width = 10;
        this.pixels = new Color[10][10];
        int i = 0;
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x += 2) {
                int code1 = source.charAt(i) - '0';
                int code2 = source.charAt(i + 1) - '0';
                setPixel(x + 1, y, Color.fromCode(code1));
                setPixel(x, y, Color.fromCode(code2));
                i += 2;
            }
        }
    }

    public AuraboxBitmap(boolean[][] cells) {
        this.width = cells[0].length;
        this.height = cells.length;
        this.pixels = new Color[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                setPixel(x, y, cells[y][x]);
            }
        }
    }

    public AuraboxBitmap print(boolean[][] cells, int startX, int startY) {
        int BBh = cells.length;
        int BBw = cells[0].length;
        for (int dy = 0; dy < BBh; dy++) {
            for (int dx = 0; dx < BBw; dx++) {
                setPixel(startX+dx, startY+dy, cells[dy][dx]);
            }
        }
        return this;
    }

    public AuraboxBitmap subBitmap(int startX, int startY) {
        Color[][] resPixels = new Color[10][10];
        for (int dy = 0; dy < 10; dy++) {
            for (int dx = 0; dx < 10; dx++) {
                resPixels[dy][dx] = pixels[(startY + dy) % height][(startX + dx) % width];
            }
        }
        return new AuraboxBitmap(resPixels);
    }

    public byte[] toByteArray() {
        byte[] result = new byte[50];
        int i = 0;
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x += 2) {
                int part1 = pixels[y][x + 1].getCode() << 4;
                int part2 = pixels[y][x].getCode();
                result[i] = (byte) (part1 | part2);
                i++;
            }
        }
        return result;
    }


    public AuraboxBitmap setPixel(int x, int y, boolean on) {
        return setPixel(x, y, on ? Color.WHITE : Color.BLACK);
    }

    public AuraboxBitmap setPixel(int x, int y, Color color) {
        pixels[y][x] = color;
        return this;
    }

    public AuraboxBitmap fill(Color color) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                setPixel(x, y, color);
            }
        }
        return this;
    }
}
