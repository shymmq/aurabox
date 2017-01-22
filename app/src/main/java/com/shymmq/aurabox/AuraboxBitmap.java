package com.shymmq.aurabox;

/**
 * Created by szyme on 22.01.2017.
 */

public class AuraboxBitmap {
    private Color[][] pixels = new Color[10][10];

    public AuraboxBitmap(Color[][] pixels) {
        this.pixels = pixels;
    }

    public AuraboxBitmap() {
        this(Color.BLACK);
    }

    public AuraboxBitmap(Color color) {
        fill(color);
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

    public AuraboxBitmap setPixel(int x, int y, Color color) {
        pixels[y][x] = color;
        return this;
    }

    public AuraboxBitmap fill(Color color) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                setPixel(x, y, color);
            }
        }
        return this;
    }
}
