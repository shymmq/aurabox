package com.shymmq.aurabox;

/**
 * Created by szyme on 24.01.2017.
 */

public class ColorTestScene implements Scene{

    @Override
    public Scene start(AuraboxService service) {
        AuraboxBitmap bitmap = new AuraboxBitmap();
        for(Color c: Color.values()){
            for (int y = 0;y<10;y++){
                bitmap.setPixel(c.getCode(),y,c);
            }
        }
        service.send(bitmap);
        return this;
    }

    @Override
    public Scene stop() {
        return this;
    }

    @Override
    public String getName() {
        return "Color test";
    }
}
