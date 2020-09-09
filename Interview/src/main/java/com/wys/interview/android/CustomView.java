package com.wys.interview.android;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * @author wangyasheng
 * @date 2020/8/6
 * @Describe:
 */
class CustomView extends ViewGroup {
    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        List<?> list = new ArrayList<Integer>();
        list = new ArrayList<String>();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("TAG","[dispatchTouchEvent] +++++++++++++");
        return super.dispatchTouchEvent(event);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("TAG","[onInterceptTouchEvent] +++++++++++++");
        return super.onInterceptTouchEvent(ev);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("TAG","[onTouchEvent] down +++++++++++++");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("TAG","[onTouchEvent] up +++++++++++++");
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return true;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }
}
