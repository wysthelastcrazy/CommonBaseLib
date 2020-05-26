package com.wys.commonbaselib.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.wys.commonbaselib.R;
import com.wys.commonbaselib.adapter.VideoListAdapter;
import com.wys.commonbaselib.bean.VideoBean;
import com.wys.commonbaselib.utils.FileUtils;
import com.wys.module_common_ui.widget.recycler.XRecyclerView;

import java.util.ArrayList;

/**
 * Created by yas on 2019/11/6
 * Describe:
 */
public class VideoListActivity extends AppCompatActivity {
    private XRecyclerView rlvVideo;
    private VideoListAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        rlvVideo = findViewById(R.id.rlv_video);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        rlvVideo.setLayoutManager(gridLayoutManager);

        mAdapter = new VideoListAdapter(this, FileUtils.getVideos(this));
        rlvVideo.setAdapter(mAdapter);
    }

    private ArrayList<VideoBean> getVideos(){
        ArrayList<VideoBean> list = new ArrayList<>();
        for (int i = 0 ; i<9 ; i++){
            list.add(new VideoBean());
        }
        return list;
    }
}
