package com.wys.commonbaselib.music;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import com.wys.commonbaselib.R;
import com.wys.module_common_ui.widget.recycler.XRecyclerView;
import com.wys.module_common_ui.widget.recycler.adapter.OnItemClickListener;

import java.util.ArrayList;

public class MusicListActivity extends AppCompatActivity {
    private XRecyclerView rlv_music;
    private MusicAdapter mAdapter;

    private MusicConnection connection;
    private MusicService.MusicBinder musicBinder;

    private Button btnGotoPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        btnGotoPlay = findViewById(R.id.btn_goto_play);
        btnGotoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicBinder!=null&&!musicBinder.isPlayEnd()){
                    gotoDetail(musicBinder.getCurrPosition(),0);
                }
            }
        });
        rlv_music = findViewById(R.id.rlv_music);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rlv_music.setLayoutManager(layoutManager);

        Intent intent = new Intent(this,MusicService.class);
        connection = new MusicConnection();
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    private ArrayList<MusicInfo> getMusicInfos(){
        ArrayList<MusicInfo> list = new ArrayList<>();
        MusicInfo info = new MusicInfo();
        info.musicId = 4;
        info.musicName = "春晓";
        info.url = "https://gushiimage.egaosi.com/watch/audio/2019-08-13/1565687533_93b90105e878fa5d3ed9d3bec81c42b4_0tftf.mp3";
        list.add(info);

        info = new MusicInfo();
        info.musicId = 44;
        info.musicName = "乡村四月";
        info.url = "https://gushiimage.egaosi.com/watch/audio/2019-08-13/1565687546_1d6ad101fb3d88d142f2de59b24d3412_4zqiq.mp3";
        list.add(info);

        info = new MusicInfo();
        info.musicId = 15;
        info.musicName = "江畔独步寻花";
        info.url = "https://gushiimage.egaosi.com/watch/audio/2019-08-13/1565687527_99f17ebaf8ad14b3422f8baf36b63841_nn0xr.mp3";
        list.add(info);

        return list;
    }

    private class MusicConnection implements ServiceConnection{

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
        mAdapter = new MusicAdapter(this,getMusicInfos());
        mAdapter.setOnItemClickListener(new OnItemClickListener<MusicInfo>() {
            @Override
            public void onItemClick(View itemView, MusicInfo bean, int position) {
                musicBinder.setMusicList(mAdapter.getList());
                gotoDetail(position,bean.musicId);
            }
        });

        rlv_music.setAdapter(mAdapter);

        //正在处于播放状态或者暂停状态时，添加快捷跳转
        if (musicBinder.isPlaying()){
            btnGotoPlay.setText("正在播放");
        }else if (!musicBinder.isPlayEnd()){
            btnGotoPlay.setText("播放处于暂停中");
        }else{
            btnGotoPlay.setText("当前无播放");
        }
    }

    private void gotoDetail(int position,int musicId){
        Intent intent = new Intent(this,MusicDetailActivity.class);
        intent.putExtra("position",position);
        intent.putExtra("musicId",musicId);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }
}
