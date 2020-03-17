package com.wys.commonbaselib.activity;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easefun.m3u8.IDownloadListener;
import com.easefun.m3u8.M3U8DownloadManager;
import com.easefun.m3u8.M3U8DownloadRecord;
import com.easefun.m3u8.M3U8DownloadRequest;
import com.wys.commonbaselib.R;
import com.wys.commonbaselib.adapter.M3U8TaskListAdapter;
import com.wys.module_common_ui.widget.recycler.XRecyclerView;

import java.util.ArrayList;

/**
 * Created by yas on 2020-03-02
 * Describe:
 */
public class M3U8DownloadActivity extends Activity implements View.OnClickListener {
    private final String TAG = "DownloadActivity";
    private EditText edTaskUrl;
    private Button btnAddTask;
    private XRecyclerView rlvTask;
    private M3U8TaskListAdapter mAdapter;
    private int index = 1;
    private String downloadDir1 = Environment.getExternalStorageDirectory()+"/wys/type01";
    private String downloadDir2 = Environment.getExternalStorageDirectory()+"/wys/type02";
    private String downloadDir3 = Environment.getExternalStorageDirectory()+"/wys/type03";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        edTaskUrl = findViewById(R.id.ed_url);
        btnAddTask = findViewById(R.id.btn_add_task);
        btnAddTask.setOnClickListener(this);

        rlvTask = findViewById(R.id.rlv_task);
        rlvTask.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        M3U8DownloadManager.getInstance().init(this,1);
        M3U8DownloadManager.getInstance().registerListener(downloadListener);
        ArrayList<M3U8DownloadRecord> list= new ArrayList<>(M3U8DownloadManager.getInstance().getAllTasks());
        mAdapter = new M3U8TaskListAdapter(this,list);
        rlvTask.setAdapter(mAdapter);
        index = list.size()+1;


        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        requestPermissions(permissions,100);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_task:
                addTask();
                break;
        }
    }
    private void addTask(){
        String url = edTaskUrl.getEditableText().toString();
        if (!TextUtils.isEmpty(url)&&url.startsWith("http")){
            M3U8DownloadRequest request = M3U8DownloadRequest.newBuilder()
                    .downloadUrl(url)
                    .downloadDir(getDownloadDir())
                    .downloadName(getDownloadName())
                    .taskId(createTaskId())
                    .taskType(getTaskType())
                    .coverUrl(getCoverUrl())
                    .build();
            M3U8DownloadManager.getInstance().enqueue(request);
            index++;
        }
    }

    private String getCoverUrl(){
        return "https://gushiimage.egaosi.com/poetry/2018-08-31/1535705935_340e6d13658f19b32b39fca6a7bac539_2ja9f.png";
    }
    private int getTaskType(){
        if (index%3==1){
            return 1;
        }else if (index%3==2){
            return 2;
        }else {
            return 3;
        }
    }
    private String getDownloadDir(){
        if (index%3==1){
            return downloadDir1;
        }else if (index%3==2){
            return downloadDir2;
        }else {
            return downloadDir3;
        }
    }

    private String getDownloadName(){
        return "第"+index+"个任务.m3u8";
    }
    private String createTaskId(){
        return "taskId"+index;
    }
    private IDownloadListener downloadListener = new IDownloadListener() {
        @Override
        public void onNewTaskAdd(final M3U8DownloadRecord record) {
            Log.d(TAG,"[onNewTaskAdd] +++++++++++++++++++++++++");
            Log.d(TAG,"[onNewTaskAdd] record:"+record.getTaskId());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.getList().add(record);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
        @Override
        public void onEnqueue(final M3U8DownloadRecord record) {
            Log.d(TAG,"[onEnqueue] +++++++++++++++++++++++++");
            Log.d(TAG,"[onEnqueue] record:"+record.getTaskId());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int position = findRecordIndex(record);
                    mAdapter.notifyItemChanged(position,"payload");
                }
            });
        }
        @Override
        public void onStart(final M3U8DownloadRecord record) {
            Log.d(TAG,"[onStart] +++++++++++++++++++++++++");
            Log.d(TAG,"[onStart] record:"+record.getTaskId());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int position = findRecordIndex(record);
                    mAdapter.notifyItemChanged(position,"payload");
                }
            });
        }
        @Override
        public void onProgress(final M3U8DownloadRecord record) {
            Log.d(TAG,"[onProgress] +++++++++++++++++++++++++");
            Log.d(TAG,"[onProgress] record:"+record.getTaskId());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int position = findRecordIndex(record);
                    mAdapter.notifyItemChanged(position,"payload");
                }
            });
        }
        @Override
        public void onPaused(final M3U8DownloadRecord record) {
            Log.d(TAG,"[onPaused] +++++++++++++++++++++++++");
            Log.d(TAG,"[onPaused] record:"+record.getTaskId());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int position = findRecordIndex(record);
                    mAdapter.notifyItemChanged(position,"payload");
                }
            });

        }
        @Override
        public void onFinish(final M3U8DownloadRecord record) {
            Log.d(TAG,"[onFinish] +++++++++++++++++++++++++");
            Log.d(TAG,"[onFinish] record:"+record.getTaskId());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int position = findRecordIndex(record);
                    mAdapter.notifyItemChanged(position,"payload");
                }
            });
        }

        @Override
        public void onFailed(final M3U8DownloadRecord record, final String errMsg) {
            Log.d(TAG,"[onFailed] +++++++++++++++++++++++++");
            Log.d(TAG,"[onFailed] record:"+record.getTaskId());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int position = findRecordIndex(record);
                    mAdapter.notifyItemChanged(position,"payload");
                    Toast.makeText(M3U8DownloadActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };
    private int findRecordIndex(M3U8DownloadRecord record) {
        for(int i = 0; i<mAdapter.getItemCount(); i++){
            if(record == mAdapter.getItemEntity(i))
                return i;
        }
        return -1;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        M3U8DownloadManager.getInstance().unRegisterListener(downloadListener);
        M3U8DownloadManager.getInstance().destroy();
    }
}
