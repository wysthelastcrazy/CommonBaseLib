package com.wys.commonbaselib.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.aixuexi.gushi.R;


public class AVEditorActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout rl_video;
    private Button btn_play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aveditor);
        rl_video = findViewById(R.id.rl_video);

        btn_play = findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:
                break;
        }
    }
}
