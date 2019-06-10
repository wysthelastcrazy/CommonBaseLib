package com.wys.commonbaselib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wys.baselib.utils.ScreenUtil;

public class MainActivity extends AppCompatActivity {
    private TextView tv_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_info = findViewById(R.id.tv_info);
        tv_info.setText("width:"+ScreenUtil.getScreenWidth()+" , height:"+ScreenUtil.getScreenHeight());
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_recycler:
                Intent intent = new Intent(this,RecyclerViewActivity.class);
                startActivity(intent);
                break;
        }
    }
}
