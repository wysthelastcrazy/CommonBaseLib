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

import com.wys.commonbaselib.R;
import com.wys.commonbaselib.adapter.TaskListAdapter;
import com.wys.downloader.DownloadManager;
import com.wys.downloader.DownloadRecord;
import com.wys.downloader.DownloadRequest;
import com.wys.downloader.IDownloadListener;
import com.wys.module_common_ui.widget.recycler.XRecyclerView;

import java.util.ArrayList;

/**
 * Created by yas on 2020-03-02
 * Describe:
 */
public class DownloadActivity extends Activity implements View.OnClickListener {
    private final String TAG = "DownloadActivity";
    private EditText edTaskUrl;
    private Button btnAddTask;
    private XRecyclerView rlvTask;
    private TaskListAdapter mAdapter;
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
        DownloadManager.getInstance().init(this,1);
        DownloadManager.getInstance().registerListener(downloadListener);
        ArrayList<DownloadRecord> list= new ArrayList<>(DownloadManager.getInstance().getAllTasks());
        mAdapter = new TaskListAdapter(this,list);
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
            DownloadRequest request = DownloadRequest.newBuilder()
                    .downloadUrl(url)
                    .downloadDir(getDownloadDir())
                    .downloadName(getDownloadName())
                    .taskId(createTaskId())
                    .taskType(getTaskType())
                    .build();
            DownloadManager.getInstance().enqueue(request);
            index++;
        }
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
        return "第"+index+"个任务.mp4";
    }
    private String createTaskId(){
        return "taskId"+index;
    }
    private IDownloadListener downloadListener = new IDownloadListener() {
        @Override
        public void onNewTaskAdd(final DownloadRecord record) {
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
        public void onEnqueue(final DownloadRecord record) {
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
        public void onStart(final DownloadRecord record) {
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
        public void onProgress(final DownloadRecord record) {
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
        public void onPaused(final DownloadRecord record) {
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
        public void onFinish(final DownloadRecord record) {
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
        public void onFailed(final DownloadRecord record, final String errMsg) {
            Log.d(TAG,"[onFailed] +++++++++++++++++++++++++");
            Log.d(TAG,"[onFailed] record:"+record.getTaskId());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int position = findRecordIndex(record);
                    mAdapter.notifyItemChanged(position,"payload");
                    Toast.makeText(DownloadActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }

    };
    private int findRecordIndex(DownloadRecord record) {
        for(int i = 0; i<mAdapter.getItemCount(); i++){
            if(record == mAdapter.getItemEntity(i))
                return i;
        }
        return -1;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadManager.getInstance().unRegisterListener(downloadListener);
        DownloadManager.getInstance().destroy();
    }
}
