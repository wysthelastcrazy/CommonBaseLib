package com.wys.commonbaselib.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.aixuexi.gushi.R;
import com.wys.module_common_ui.widget.recycler.adapter.BaseMultiTypeRecyclerAdapter;
import com.wys.module_common_ui.widget.recycler.adapter.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by yas on 2019/11/19
 * Describe:
 */
public class TestMulAdapter extends BaseMultiTypeRecyclerAdapter<String> {
    private int TYPE_TITLE = 1;
    private int TYPE_ITEM = 2;
    public TestMulAdapter(Context mContext, ArrayList<String> mList) {
        super(mContext, mList);
    }

    @Override
    protected int getItemLayout(int viewType) {
        if (viewType == TYPE_TITLE){
            return R.layout.test_item_view_title;
        }else {
            return R.layout.test_item_view_content;
        }
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int viewType) {
        if (viewType == TYPE_TITLE){
            return new TestMulViewHolderTitle(itemView);
        }else{
            return new TestMulViewHolderContent(itemView);
        }
    }

    @Override
    protected int getTypeByPos(int position) {
        ArrayList<String> list = getList();
        if (list!=null&&list.size()>position){
            if (list.get(position).startsWith("aaa")){
                return TYPE_TITLE;
            }
        }
        return TYPE_ITEM;
    }

    private class TestMulViewHolderTitle extends BaseViewHolder<String>{
        private TextView tvTitle;
        public TestMulViewHolderTitle(View itemView) {
            super(itemView);
        }

        @Override
        public void initView() {
            tvTitle = itemView.findViewById(R.id.tv_title);
        }

        @Override
        public void setValues(String s) {
            tvTitle.setText(s);
        }
    }
    private class TestMulViewHolderContent extends BaseViewHolder<String>{
        private TextView rvContent;
        public TestMulViewHolderContent(View itemView) {
            super(itemView);
        }

        @Override
        public void initView() {
            rvContent = itemView.findViewById(R.id.tv_content);
        }

        @Override
        public void setValues(String s) {
            rvContent.setText(s);
        }
    }
}
