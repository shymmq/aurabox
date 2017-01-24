package com.shymmq.aurabox;

/**
 * Created by szyme on 24.01.2017.
 */

public class StringDisplayScene implements Scene {
    private final static String source = "0000000000101111110110111111011011111101505577330350557733034044662202404466220240446622020000000000";

    @Override
    public Scene start(AuraboxService service) {
        AuraboxBitmap bitmap = new AuraboxBitmap(source);

        service.send(bitmap);
        return this;
    }

    @Override
    public Scene stop() {
        return this;
    }

    @Override
    public String getName() {
        return "From String";
    }
}
