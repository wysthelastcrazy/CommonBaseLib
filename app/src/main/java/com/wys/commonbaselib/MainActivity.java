package com.wys.commonbaselib;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.wys.commonbaselib.activity.DownloadActivity;
import com.wys.commonbaselib.activity.M3U8DownloadActivity;
import com.wys.commonbaselib.activity.MediaRecorderActivity;
import com.wys.commonbaselib.activity.PhotoListActivity;
import com.wys.commonbaselib.activity.RequestActivity;
import com.wys.commonbaselib.activity.VideoListActivity;
import com.wys.commonbaselib.activity.VideoPlayerActivity;
import com.wys.commonbaselib.jetpack.kotlin.LoginActivity;
import com.wys.commonbaselib.kotlin.Day03;
import com.wys.commonbaselib.music.MusicListActivity;
import com.wys.commonbaselib.zxing.activity.CaptureActivity;

/**
 * @author wangyasheng
 */
public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        requestPermissions(permissions,100);
        Day03 day03 = new Day03();
        day03.day03Enter();
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_download:
                Intent intent = new Intent(this, DownloadActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_m3u8:
                intent = new Intent(this, M3U8DownloadActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_music:
                intent = new Intent(this, MusicListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_player:
                intent = new Intent(this, VideoPlayerActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_zxing:
                intent = new Intent(this, CaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_video_list:
                intent = new Intent(this, VideoListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_photo_list:
                intent = new Intent(this, PhotoListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_mediaRecorder:
                intent = new Intent(this, MediaRecorderActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_jetpack:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_net:
                intent = new Intent(this, RequestActivity.class);
                startActivity(intent);
                break;
        }
    }
}
