package com.wys.commonbaselib.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wys.commonbaselib.R;
import com.wys.downloader.DownloadManager;
import com.wys.downloader.DownloadRecord;
import com.wys.module_common_ui.widget.recycler.adapter.BaseRecyclerAdapter;
import com.wys.module_common_ui.widget.recycler.adapter.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by yas on 2020-03-03
 * Describe:
 */
public class TaskListAdapter extends BaseRecyclerAdapter<TaskListAdapter.TaskListViewHolder,DownloadRecord> {
    public TaskListAdapter(Context mContext, ArrayList<DownloadRecord> mList) {
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

    public class TaskListViewHolder extends BaseViewHolder<DownloadRecord>{
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

            final DownloadRecord record = getItemEntity(pos);
            btnState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (record!=null&&record.getDownloadState() == DownloadManager.STATE_DOWNLOADING) {
                        DownloadManager.getInstance().pause(record.getTaskId());
                    } else if (record!=null&&record.getDownloadState() == DownloadManager.STATE_PAUSED) {
                        DownloadManager.getInstance().reEnqueue(record.getTaskId());
                    }else if (record!=null&&record.getDownloadState() == DownloadManager.STATE_FAILED) {
                        DownloadManager.getInstance().reEnqueue(record.getTaskId());
                    }else if (record!=null&&record.getDownloadState() == DownloadManager.STATE_FINISHED){
                        getList().remove(record);
                        notifyDataSetChanged();
                        DownloadManager.getInstance().deleteTask(record.getTaskId());
                    }
                }
            });
        }

        @Override
        public void setValues(final DownloadRecord record) {
            if (record!=null){
                switch (record.getDownloadState()){
                    case DownloadManager.STATE_DOWNLOADING:
                        btnState.setText("暂停");
                        tvProgress.setText(record.getProgress()+"%");
                        break;
                    case DownloadManager.STATE_PAUSED:
                        btnState.setText("继续");
                        tvProgress.setText("已暂停");
                        break;
                    case DownloadManager.STATE_INITIAL:
                        btnState.setText("等待开始");
                        tvProgress.setText("未开始");
                        break;
                    case DownloadManager.STATE_FINISHED:
                        btnState.setText("打开");
                        tvProgress.setText("已完成");
                        break;
                    case DownloadManager.STATE_REENQUEUE:
                        btnState.setText("等待开始");
                        tvProgress.setText("已暂停");
                        break;
                    case DownloadManager.STATE_FAILED:
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
