package com.wys.commonbaselib.activity;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.aigushi.videoplayer.CustomVideoPlayerView;
import com.aigushi.videoplayer.IMediaPlayListener;
import com.wys.commonbaselib.R;


/**
 * Created by yas on 2020-03-09
 * Describe:
 */
public class VideoPlayerActivity extends Activity {
    private final String TAG = "VideoPlayerActivity";
    private CustomVideoPlayerView playerView;
    private String testUrl = "http://c-vod.egaosi.com/sv/56a648dc-170ae4584ef/56a648dc-170ae4584ef.m3u8?auth_key=1583722735-150ad4e137de4dc88f75bda298d31699-0-13b9450f5dc45eb2ce824259562e807c";
    private String testUrl2 = "https://gushiimage.egaosi.com/poetry/video/2019-11-06/1573027292_780101e74c5553afaf83f5cb5e9207e8_1b1zb.mp4";
    private String testUrl3 = "https://gushiimage.egaosi.com/sample/math/144669059_enc/test.m3u8";

    private boolean isFinished;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        playerView = findViewById(R.id.video_player);
        playerView.addPlayListener(new IMediaPlayListener() {
            @Override
            public void onShowBuffering() {
                Log.d(TAG,"[onShowBuffering]+++++++++++++++++++");
            }

            @Override
            public void onHideBuffering() {
                Log.d(TAG,"[onHideBuffering]+++++++++++++++++++");
            }

            @Override
            public void onReady(long duration) {
                Log.d(TAG,"[onReady]+++++++++++++++++++duration:"+duration);
            }

            @Override
            public void onUpdateProgress(long currentPosition, long bufferedPosition) {
                Log.d(TAG,"[onUpdateProgress]+++++++++++++++++++");
                Log.d(TAG,"[onUpdateProgress] currentPosition:"+currentPosition+",bufferedPosition:"+bufferedPosition);
            }

            @Override
            public void onPlayFinished() {
                Log.d(TAG,"[onPlayFinished]+++++++++++++++++++");
                isFinished = true;
            }

            @Override
            public void onPlayError(int code, String errorMsg) {
                Log.d(TAG,"[onPlayError]+++++++++++++++++++");
            }
        });
        playerView.prepare(testUrl);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_play:
                if (isFinished) {
                    playerView.seekTo(0);
                    isFinished = false;
                }
                playerView.start();
                break;
            case R.id.btn_pause:
                playerView.pause();
                break;
        }
    }

}
