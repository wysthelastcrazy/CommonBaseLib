package com.wys.commonbaselib.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.wys.commonbaselib.R;
import com.wys.commonbaselib.activity.BigPhotoActivity;

/**
 * Created by yas on 2019/9/23
 * Describe:
 */
public class PhotoListActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_ll01_img01;
    private ImageView iv_ll01_img02;
    private ImageView iv_ll02_img01;
    private ImageView iv_ll02_img02;
    private ImageView iv_bottom;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        iv_ll01_img01 = findViewById(R.id.iv_ll01_img01);
        iv_ll01_img02 = findViewById(R.id.iv_ll01_img02);
        iv_ll02_img01 = findViewById(R.id.iv_ll02_img01);
        iv_ll02_img02 = findViewById(R.id.iv_ll02_img02);

        iv_ll01_img01.setOnClickListener(this);
        iv_ll01_img02.setOnClickListener(this);
        iv_ll02_img01.setOnClickListener(this);
        iv_ll02_img02.setOnClickListener(this);
        iv_bottom = findViewById(R.id.iv_bottom);
        iv_bottom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_ll01_img01:
//                showBigPhoto(iv_ll01_img01);
                test(iv_ll01_img01,"iv_ll01_img01");
                break;
            case R.id.iv_ll01_img02:
//                showBigPhoto(iv_ll01_img02);
                test(iv_ll01_img02,"iv_ll01_img02");
                break;
            case R.id.iv_ll02_img01:
//                showBigPhoto(iv_ll02_img01);
                test(iv_ll02_img01,"iv_ll02_img01");
                break;
            case R.id.iv_ll02_img02:
//                showBigPhoto(iv_ll02_img02);
                test(iv_ll02_img02,"iv_ll02_img02");
                break;
            case R.id.iv_bottom:
//                showBigPhoto(iv_bottom);
                test(iv_bottom,"image_bottom");
                break;
        }
    }

    private void test(View view,String name){
        Intent intent = new Intent(this, BigPhotoActivity.class);
        intent.putExtra("name",name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this,
                    view,name).toBundle());
        }else{
            startActivity(intent);
        }
    }
}
