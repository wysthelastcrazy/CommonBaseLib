package com.wys.commonbaselib.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aixuexi.gushi.R;
import com.wys.m3u8.M3U8DownloadManager;
import com.wys.m3u8.M3U8DownloadRecord;
import com.wys.module_common_ui.widget.recycler.adapter.BaseRecyclerAdapter;
import com.wys.module_common_ui.widget.recycler.adapter.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by yas on 2020-03-03
 * Describe:
 */
public class M3U8TaskListAdapter extends BaseRecyclerAdapter<M3U8TaskListAdapter.TaskListViewHolder, M3U8DownloadRecord> {
    public M3U8TaskListAdapter(Context mContext, ArrayList<M3U8DownloadRecord> mList) {
        super(mContext, mList);
    }

    @Override
    protected int getItemLayout() {
        return R.layout.item_download_task;
    }

    @Override
    protected TaskListViewHolder getViewHolder(View itemView) {
        return new TaskListViewHolder(itemView);
    }

    public class TaskListViewHolder extends BaseViewHolder<M3U8DownloadRecord>{
        private ProgressBar progressBar;
        private TextView tvProgress;
        private Button btnState;
        private TextView tvTaskName;
        public TaskListViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initView() {
            progressBar = itemView.findViewById(R.id.progressBar);
            tvProgress = itemView.findViewById(R.id.tvProgress);
            btnState = itemView.findViewById(R.id.btnState);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);

            final M3U8DownloadRecord record = getItemEntity(pos);
            btnState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (record!=null&&record.getDownloadState() == M3U8DownloadManager.STATE_DOWNLOADING) {
                        M3U8DownloadManager.getInstance().pause(record.getTaskId());
                    } else if (record!=null&&record.getDownloadState() == M3U8DownloadManager.STATE_PAUSED) {
                        M3U8DownloadManager.getInstance().reEnqueue(record);
                    }else if (record!=null&&record.getDownloadState() == M3U8DownloadManager.STATE_FAILED) {
                        M3U8DownloadManager.getInstance().reEnqueue(record);
                    }else if (record!=null&&record.getDownloadState() == M3U8DownloadManager.STATE_FINISHED){
                        getList().remove(record);
                        notifyDataSetChanged();
                        M3U8DownloadManager.getInstance().deleteTask(record.getTaskId());
                    }
                }
            });
        }

        @Override
        public void setValues(final M3U8DownloadRecord record) {
            if (record!=null){
                switch (record.getDownloadState()){
                    case M3U8DownloadManager.STATE_DOWNLOADING:
                        btnState.setText("暂停");
                        tvProgress.setText(record.getProgress()+"%");
                        break;
                    case M3U8DownloadManager.STATE_PAUSED:
                        btnState.setText("继续");
                        tvProgress.setText("已暂停");
                        break;
                    case M3U8DownloadManager.STATE_INITIAL:
                        btnState.setText("等待开始");
                        tvProgress.setText("未开始");
                        break;
                    case M3U8DownloadManager.STATE_FINISHED:
                        btnState.setText("打开");
                        tvProgress.setText("已完成");
                        break;
                    case M3U8DownloadManager.STATE_REENQUEUE:
                        btnState.setText("等待开始");
                        tvProgress.setText("已暂停");
                        break;
                    case M3U8DownloadManager.STATE_FAILED:
                        btnState.setText("重试");
                        tvProgress.setText("下载失败");
                        break;
                }

                tvTaskName.setText(record.getDownloadName()+"  type:"+record.getTaskType());
                progressBar.setProgress(record.getProgress());
            }
        }
    }
}
