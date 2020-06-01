package com.wys.commonbaselib.adapter;

import android.content.Context;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by yas on 2020-05-28
 * Describe:
 * @author wangyasheng
 */
public abstract class BaseBannerAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener {
    protected Context mContext;
    protected List<T> mDataList;
    private List<View> mViews;
    private ViewPager mViewPager;
    private int mCurrentPosition;
    public BaseBannerAdapter(Context context, List<T> dataList, ViewPager viewPager){
        mContext = context;
        mDataList = dataList;
        mViewPager = viewPager;
        boolean flag = false;
        if (dataList!=null){
            mViews = new LinkedList<>();
            if (mDataList.size()>1){
                T first = mDataList.get(0);
                T last = mDataList.get(mDataList.size()-1);
                mDataList.add(0,last);
                mDataList.add(first);
            }
        }
        mViewPager.setAdapter(this);
        if (mDataList!=null&&mDataList.size()>=3){
            //如果总长度大于等于3，则可滑动，初始位置变更
            mViewPager.setCurrentItem(1,false);
        }
        mViewPager.addOnPageChangeListener(this);
    }
    @Override
    public int getCount() {
        if (mDataList!=null) {
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            int position = mViewPager.getCurrentItem();
            if (position == 0){
                mViewPager.setCurrentItem(getCount()-2,false);
            } else if (position == getCount()-1) {
                mViewPager.setCurrentItem(1,false);
            }
        }
    }
}
