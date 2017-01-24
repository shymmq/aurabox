package com.shymmq.aurabox;

/**
 * Created by szyme on 24.01.2017.
 */

interface Scene {
    Scene start(AuraboxService service);

    Scene stop();

    String getName();

}
