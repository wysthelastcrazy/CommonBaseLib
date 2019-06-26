package com.wys.commonbaselib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wys.audio_video_editor.camera.CameraView;

public class AVEditorActivity extends AppCompatActivity {
    private CameraView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aveditor);
        cameraView = findViewById(R.id.cameraView);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        //更改视图在树中的z顺序，因此它位于其他同级视图之上。
//        cameraView.bringToFront();
//
//    }
}
