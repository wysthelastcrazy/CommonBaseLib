package com.wys.commonbaselib.activity;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wys.commonbaselib.R;

/**
 * Created by yas on 2019/9/23
 * Describe:
 */
public class BigPhotoActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout linearLayout;
    private ImageView imageView;
    private String name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_photo);
        name = getIntent().getStringExtra("name");
        initView();
    }

    private void initView(){
        linearLayout = findViewById(R.id.ll_img);
        imageView = findViewById(R.id.img);
//        if (!TextUtils.isEmpty(name)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setTransitionName(name);
            }
//        }
        initValue();
    }

    protected void initValue() {
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageView.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == imageView){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            }else{
                finish();
            }
        }
    }
}
