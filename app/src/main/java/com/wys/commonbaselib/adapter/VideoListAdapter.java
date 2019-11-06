package com.wys.commonbaselib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wys.commonbaselib.R;
import com.wys.commonbaselib.bean.VideoBean;
import com.wys.module_common_ui.widget.recycler.adapter.BaseRecyclerAdapter;
import com.wys.module_common_ui.widget.recycler.adapter.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by yas on 2019/11/6
 * Describe:
 */
public class VideoListAdapter extends BaseRecyclerAdapter<VideoListAdapter.VideoListViewHolder, VideoBean> {

    public VideoListAdapter(Context mContext, ArrayList<VideoBean> mList) {
        super(mContext, mList);
    }

    @Override
    protected int getItemLayout() {
        return R.layout.item_view_video;
    }

    @Override
    protected VideoListViewHolder getViewHolder(View itemView) {
        return new VideoListViewHolder(itemView);
    }

    public class VideoListViewHolder extends BaseViewHolder<VideoBean>{
        private ImageView ivShow;
        private TextView tvDuration;
        public VideoListViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initView() {
            ivShow = itemView.findViewById(R.id.iv_show);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            RecyclerView.LayoutParams itemParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (pos%3 == 0){
                itemParams.leftMargin = (int) VideoListAdapter.this.mContext.getResources().getDimension(R.dimen.dp_5);
                itemParams.rightMargin = (int) VideoListAdapter.this.mContext.getResources().getDimension(R.dimen.dp_5);
            }else if (pos%3 == 2){
                itemParams.leftMargin = 0;
                itemParams.rightMargin = (int) VideoListAdapter.this.mContext.getResources().getDimension(R.dimen.dp_5);
            }else{
                itemParams.leftMargin = 0;
                itemParams.rightMargin = (int) VideoListAdapter.this.mContext.getResources().getDimension(R.dimen.dp_5);
            }
        }

        @Override
        public void setValues(VideoBean videoBean) {
            if (videoBean!=null){
                tvDuration.setText(formatDuration(videoBean.duration));
                if (videoBean.thumbnail!=null){
                    ivShow.setImageBitmap(videoBean.thumbnail);
                }
            }
        }

        private String formatDuration(long duration){
            long m = (duration / 1000) / 60;
            long s = (duration / 1000) % 60;
            return formatLong(m) + ":" + formatLong(s);
        }
        private  String formatLong(long lon){
            String str="";
            if (lon<10){
                str="0"+lon;
            }else{
                str=lon+"";
            }
            return str;
        }
    }
}
