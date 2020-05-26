package com.wys.commonbaselib.activity;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.aigushi.videoplayer.CustomVideoPlayerView;
import com.aigushi.videoplayer.IMediaPlayListener;
import com.easefun.m3u8.IDownloadListener;
import com.easefun.m3u8.M3U8DownloadManager;
import com.easefun.m3u8.M3U8DownloadRecord;
import com.easefun.m3u8.M3U8DownloadRequest;
import com.wys.commonbaselib.R;

import java.io.File;


/**
 * Created by yas on 2020-03-09
 * Describe:
 */
public class VideoPlayerActivity extends Activity {
    private final String TAG = "VideoPlayerActivity";
    private CustomVideoPlayerView playerView;
    private ImageView iv_test;
    private String testUrl = "https://c-vod.egaosi.com/sv/56a648dc-170ae4584ef/56a648dc-170ae4584ef.m3u8?auth_key=1583982412-7a60684524924618b2589b60b8148a28-0-f8a9b8754663c30fe34d3477bb30694b";
    private String testUrl2 = "https://gushiimage.egaosi.com/poetry/video/2019-11-06/1573027292_780101e74c5553afaf83f5cb5e9207e8_1b1zb.mp4";
    private String testUrl3 = "https://gushiimage.egaosi.com/sample/math/144669059_enc/test.m3u8";

    private boolean isFinished;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        M3U8DownloadManager.getInstance().init(this,1);
        M3U8DownloadManager.getInstance().registerListener(downloadListener);

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
        playerView.prepare(testUrl3);
        String uri = getDownloadDir("test2")+File.separator+"风.m3u8";
//        playerView.prepare(uri);
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        requestPermissions(permissions,100);
        iv_test = findViewById(R.id.iv_test);

//        Glide.with(this)
//                .load(getDownloadDir("test2")+File.separator+"144669059-1-290.ts")
//                .into(iv_test);
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
//                M3U8DownloadManager.getInstance().pause("2_3");
                break;
            case R.id.btn_download:
//                download(testUrl);
                download();
                break;
        }
    }

    private void download(){
        M3U8DownloadRequest request = M3U8DownloadRequest.newBuilder()
                .downloadUrl(testUrl3)
                .downloadDir(getDownloadDir("test4"))
                .downloadName("风.m3u8")
                .taskId("2_3")
                .taskType(2)
                .build();
        M3U8DownloadManager.getInstance().enqueue(request);
    }
    private String getDownloadDir(String name){
        return Environment.getExternalStorageDirectory().getPath() + File.separator
                + "m3u8"+File.separator+name;
    }
    private IDownloadListener downloadListener = new IDownloadListener() {
        @Override
        public void onNewTaskAdd(final M3U8DownloadRecord record) {
            Log.d(TAG,"[onNewTaskAdd] +++++++++++++++++++++++++");
            Log.d(TAG,"[onNewTaskAdd] record:"+record.getTaskId());
        }
        @Override
        public void onEnqueue(final M3U8DownloadRecord record) {
            Log.d(TAG,"[onEnqueue] +++++++++++++++++++++++++");
            Log.d(TAG,"[onEnqueue] record:"+record.getTaskId());
        }
        @Override
        public void onStart(final M3U8DownloadRecord record) {
            Log.d(TAG,"[onStart] +++++++++++++++++++++++++");
            Log.d(TAG,"[onStart] record:"+record.getTaskId());
        }
        @Override
        public void onProgress(final M3U8DownloadRecord record) {
            Log.d(TAG,"[onProgress] +++++++++++++++++++++++++");
            Log.d(TAG,"[onProgress] record:"+record.getTaskId()+",progress:"+record.getProgress()+"%");
        }
        @Override
        public void onPaused(final M3U8DownloadRecord record) {
            Log.d(TAG,"[onPaused] +++++++++++++++++++++++++");
            Log.d(TAG,"[onPaused] record:"+record.getTaskId());

        }
        @Override
        public void onFinish(final M3U8DownloadRecord record) {
            Log.d(TAG,"[onFinish] +++++++++++++++++++++++++");
            Log.d(TAG,"[onFinish] record:"+record.getTaskId());
        }

        @Override
        public void onFailed(final M3U8DownloadRecord record, final String errMsg) {
            Log.d(TAG,"[onFailed] +++++++++++++++++++++++++");
            Log.d(TAG,"[onFailed] record:"+record.getTaskId()+",errMsg:"+errMsg);
        }

    };
}
