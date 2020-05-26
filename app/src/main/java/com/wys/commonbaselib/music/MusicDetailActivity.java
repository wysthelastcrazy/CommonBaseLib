package com.wys.commonbaselib.music;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wys.commonbaselib.R;

public class MusicDetailActivity extends AppCompatActivity implements IGSMusicPlayListener {
   private final String TAG = "MusicDetailActivity";

    private MusicDetailActivity.MusicConnection connection;
    private MusicService.MusicBinder musicBinder;
    private int position;
    private int musicId;

    private TextView tvName;
    private Button btnPause;
    private Button btnPre;
    private Button btnNext;
    private boolean isPause;

    private SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);
        position = getIntent().getIntExtra("position",0);
        musicId = getIntent().getIntExtra("musicId",0);
        initViews();
        Intent intent = new Intent(this,MusicService.class);
        connection = new MusicDetailActivity.MusicConnection();
        bindService(intent,connection,BIND_AUTO_CREATE);

    }
    private void initViews(){
        tvName = findViewById(R.id.tv_name);

        btnPre = findViewById(R.id.btn_pre);
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicBinder.playPre()){
                    tvName.setText(musicBinder.getCurrMusicInfo().musicName);
                }else{
                    tvName.setText("这已经是第一首了");
                }
            }
        });
        btnPause = findViewById(R.id.btn_pause);
        btnPause .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPause){
                    musicBinder.play();
                    btnPause.setText("暂停");
                }else{
                    musicBinder.pause();
                    btnPause.setText("播放");
                }
                isPause = !isPause;
            }
        });
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicBinder.playNext()){
                    tvName.setText(musicBinder.getCurrMusicInfo().musicName);
                }else{
                    tvName.setText("这已经是最后一首了");
                }
            }
        });

        seekBar = findViewById(R.id.sk_progress);
    }

    @Override
    public void onShowBuffering() {
        Log.d(TAG,"[onShowBuffering]+++++++++++++");
    }

    @Override
    public void onHideBuffering() {
        Log.d(TAG,"[onHideBuffering]+++++++++++++");
    }

    @Override
    public void onAudioReady(long duration) {
        Log.d(TAG,"[onAudioReady]+++++++++++++duration:"+duration);
        seekBar.setMax((int) duration);
        seekBar.setProgress(0);
    }

    @Override
    public void onAudioPlay() {
        Log.d(TAG,"[onAudioPlay]+++++++++++++");
        btnPause.setText("暂停");
    }

    @Override
    public void onAudioPause() {
        Log.d(TAG,"[onAudioPause]+++++++++++++");
        btnPause.setText("播放");
    }

    @Override
    public void onAudioUpdateProgress(long currentPosition, long bufferedPosition) {
        seekBar.setProgress((int) currentPosition);
    }

    @Override
    public void onAudioEnd() {
        Log.d(TAG,"[onAudioEnd]+++++++++++++");
    }

    @Override
    public void onAudioError(int code, String errorMsg) {
        Log.d(TAG,"[onAudioError]+++++++++++++");
    }

    @Override
    public void onGetPoetryInfoCallback(MusicInfo infoBean) {
        Log.d(TAG,"[onGetPoetryInfoCallback]+++++++++++++");
        tvName.setText(infoBean.musicName);
    }

    @Override
    public void onMusicListEnd() {
        Log.d(TAG,"[onMusicListEnd]+++++++++++++");
    }

    private class MusicConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (MusicService.MusicBinder) service;
            onMusicServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    private void onMusicServiceConnected(){
        musicBinder.register(this);
        musicBinder.prepare(position);

        Intent intent1 = new Intent(this, MusicService.class);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            startForegroundService(intent1);
        }else {
            startService(intent1);
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        if (musicBinder!=null){
            musicBinder.unRegister(this);
        }
        super.onDestroy();
    }
}
