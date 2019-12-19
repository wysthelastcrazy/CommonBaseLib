package com.wys.module_common_ui.widget.recycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;


/**
 * Created by yas on 2019/11/20
 * Describe:
 */
public abstract class BaseViewPagerAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener {
    private List<T> mList;
    private Context mContext;
    private boolean isLoop = false; //是否需要循环
    private ViewPager viewPager;


    public BaseViewPagerAdapter(Context context,ViewPager viewPager,List<T> mList){
        this(context,viewPager,mList,false);
    }
    public BaseViewPagerAdapter(Context context,ViewPager viewPager,List<T> mList,boolean isLoop){
        this.mContext = context;
        this.mList = mList;
        this.isLoop = isLoop;
        this.viewPager = viewPager;
        if (viewPager!=null)
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public int getCount() {
        if (mList == null)
            return 0;
        if (isLoop&&mList.size()>1){
            return mList.size() + 2;
        }else{
            return mList.size();
        }
    }



    private T getBean(int position){
        if (isLoop) {
            if (position == 0) {
                return mList.get(mList.size() - 1);
            } else if (position == mList.size() + 1) {
                return mList.get(0);
            } else {
                return mList.get(position - 1);
            }
        }else{
            return mList.get(position);
        }
    }
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (getCount()>1) {
                int position = viewPager.getCurrentItem();
                if (position == 0) {
                    viewPager.setCurrentItem(getCount() - 2, false);
                } else if (position == getCount() - 1) {
                    viewPager.setCurrentItem(1, false);
                }
            }
        }
    }
}
