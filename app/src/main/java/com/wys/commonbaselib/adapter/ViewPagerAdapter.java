package com.wys.commonbaselib.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yas on 2019/11/5
 * Describe:
 */
public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> list;
    public ViewPagerAdapter(Context context,ArrayList<String> list){
        this.list = list;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        if (list!=null){
            if (list.size()>1) {
                return list.size() + 2;
            }else{
                return list.size();
            }
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        TextView itemView = new TextView(mContext);
        itemView.setText(getStr(position));

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private String getStr(int position){
        if (position == 0){
            return list.get(list.size()-1);
        }else if (position == list.size()+1){
            return list.get(0);
        }else{
            return list.get(position-1);
        }
    }
}
