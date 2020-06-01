package com.wys.commonbaselib.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yas on 2019/11/5
 * Describe:
 */
public class ViewPagerAdapter extends BaseBannerAdapter<String> {
    private OnPageChangeCallback changeCallback;
    public ViewPagerAdapter(Context context, List dataList, ViewPager viewPager,OnPageChangeCallback changeCallback) {
        super(context, dataList, viewPager);
        this.changeCallback = changeCallback;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        TextView itemView = new TextView(mContext);
        itemView.setText(mDataList.get(position));

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        super.onPageScrollStateChanged(state);

    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        if (changeCallback!=null){
            changeCallback.onPageChange(position);
        }
    }

    public interface OnPageChangeCallback{
        /**
         * 切换页卡
         * @param position
         */
        void onPageChange(int position);
    }
}
