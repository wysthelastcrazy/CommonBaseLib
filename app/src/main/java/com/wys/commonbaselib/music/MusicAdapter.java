package com.wys.commonbaselib.music;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.aixuexi.gushi.R;
import com.wys.module_common_ui.widget.recycler.adapter.BaseRecyclerAdapter;
import com.wys.module_common_ui.widget.recycler.adapter.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by yas on 2020-05-26
 * Describe:
 */
public class MusicAdapter extends BaseRecyclerAdapter<MusicAdapter.MusicViewHolder,MusicInfo> {
    public MusicAdapter(Context mContext, ArrayList<MusicInfo> mList) {
        super(mContext, mList);
    }

    @Override
    protected int getItemLayout() {
        return R.layout.item_view_music;
    }

    @Override
    protected MusicViewHolder getViewHolder(View itemView) {
        return new MusicViewHolder(itemView);
    }

    public class MusicViewHolder extends BaseViewHolder<MusicInfo>{
        private TextView tv_name;
        public MusicViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initView() {
            tv_name = itemView.findViewById(R.id.tv_name);
        }

        @Override
        public void setValues(MusicInfo musicInfo) {
            tv_name.setText(musicInfo.musicName);
        }
    }
}
