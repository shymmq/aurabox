package com.shymmq.aurabox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "aurabox-debug";
    private AuraboxService service;
    private ViewGroup container;
    private List<Scene> scenes = new ArrayList<>();
    private Scene currentScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scenes.add(new GOLScene());
        scenes.add(new ColorTestScene());
        scenes.add(new StringDisplayScene());
        scenes.add(new ScrollTestScene());
        container = (ViewGroup) findViewById(R.id.container);

        service = new AuraboxService(this);
        service.connect(new Runnable() {
            @Override
            public void run() {
                for (final Scene s : scenes) {
                    Button button = new Button(getApplicationContext());
                    button.setText(s.getName());
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currentScene != null) currentScene.stop();
                            currentScene = s.start(service);
                        }
                    });
                    container.addView(button);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        service.smoothBluetooth.stop();
    }


}
