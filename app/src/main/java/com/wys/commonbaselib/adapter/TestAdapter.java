package com.wys.commonbaselib.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wys.commonbaselib.R;
import com.wys.module_common_ui.widget.recycler.adapter.BaseRecyclerAdapter;
import com.wys.module_common_ui.widget.recycler.adapter.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by yas on 2019/6/5
 * Describe:
 */
public class TestAdapter extends BaseRecyclerAdapter<TestAdapter.TestViewHolder,String> {

    public TestAdapter(Context mContext, ArrayList<String> mList) {
        super(mContext, mList);
    }

    @Override
    protected int getItemLayout() {
        return R.layout.test_item_view;
    }

    @Override
    protected TestViewHolder getViewHolder(View itemView) {
        return new TestViewHolder(itemView);
    }

    class TestViewHolder extends BaseViewHolder<String> {
        private TextView tv_info;
        public TestViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initView() {
            tv_info = itemView.findViewById(R.id.tv_info);
        }

        @Override
        public void setValues(String s) {
            tv_info.setText(s);
        }
    }
}
