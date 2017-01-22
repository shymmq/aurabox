package com.shymmq.aurabox;

/**
 * Created by szyme on 22.01.2017.
 */

public enum Color {
    BLACK(0),
    RED(1),
    GREEN(2),
    YELLOW(3),
    BLUE(4),
    PURPLE(5),
    CYAN(6),
    WHITE(7);

    private final int code;

    Color(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
