package com.shymmq.aurabox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private String TAG = "aurabox-debug";
    AuraboxService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button sendButton = (Button) findViewById(R.id.button);
        service = new AuraboxService(this);
        service.connect();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuraboxBitmap auraboxBitmap = new AuraboxBitmap("2134560721426375104234560721346375104263560721345675104263750721345607104263751021345607214263751042");
//                for (Color i : Color.values()) {
//                    auraboxBitmap.setPixel(i.getCode(),0,i);
//                }
                service.send(auraboxBitmap);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        service.smoothBluetooth.stop();
    }


}
