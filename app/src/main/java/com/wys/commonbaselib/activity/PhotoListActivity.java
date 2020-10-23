package com.wys.commonbaselib.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.aixuexi.gushi.R;
import com.wys.commonbaselib.adapter.ViewPagerAdapter;

import java.util.ArrayList;

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

    private ViewPager viewPager;
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

        viewPager = findViewById(R.id.viewPager);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        arrayList.add("4");
        new ViewPagerAdapter(this, arrayList, viewPager, new ViewPagerAdapter.OnPageChangeCallback() {
            @Override
            public void onPageChange(int position) {
                Log.d("PhotoListActivity","[onPageChange] position:"+position);
            }
        });
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
