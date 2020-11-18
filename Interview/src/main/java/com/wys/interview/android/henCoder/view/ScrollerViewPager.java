package com.wys.interview.android.henCoder.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @author wangyasheng
 * @date 2020/11/16
 * @Describe:
 */
class ScrollerViewPager extends ViewGroup {
    private Scroller mScroller;
    /**拖动的最小移动像素点*/
    private int mTouchSlop;
    /**手指按下屏幕的x坐标*/
    private float mDownX;
    /**手指当前所在的坐标*/
    private float mMoveX;
    /**记录上一次触发 按下的坐标*/
    private float mLastMoveX;
    /**界面可以滚动的左边界*/
    private int mLeftBorder;
    /**界面可以滚动的右边界*/
    private int mRightBorder;
    public ScrollerViewPager(Context context) {
        this(context,null);
    }

    public ScrollerViewPager(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollerViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }

    /**
     * 测量child宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int index = 0;index < childCount - 1;index++){
            View childView = getChildAt(index);
            //为ScrollerViewPager中的每一个子控件测量大小
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
        }
    }

    /**
     * 测量完成之后，拿到child的大小然后开始对号入座
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed){
            int childCount = getChildCount();
            for (int i = 0; i<childCount -1;i++){
                View childView = getChildAt(i);
                childView.layout(
                        i*childView.getMeasuredWidth(),
                        0,
                        (i+1)*childView.getMeasuredWidth(),
                        childView.getMeasuredHeight()
                        );
            }
            mLeftBorder = getChildAt(0).getLeft();
            mRightBorder = getChildAt(childCount - 1).getRight();
        }
    }

    /**
     * 重写拦截事件
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mLastMoveX = mDownX;
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveX = ev.getRawX();
                float adsDiff = Math.abs(mMoveX - mDownX);
                mLastMoveX = mMoveX;
                if (adsDiff > mTouchSlop){
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE){
            mMoveX = event.getRawX();
            int scrolledX = (int) (mLastMoveX - mMoveX);
            if (getScrollX()+scrolledX < mLeftBorder){
                scrollTo(mLeftBorder,0);
                return true;
            }else if (getScrollX()+getWidth()+scrolledX>mRightBorder){
                scrollTo(mRightBorder - getWidth(),0);
                return true;
            }
            scrollBy(scrolledX,0);
            mLastMoveX = mMoveX;
        }else if (event.getAction() == MotionEvent.ACTION_UP) {
            int targetIndex = (getScrollX()+getWidth()/2)/getWidth();
            int dx = targetIndex*getWidth() - getScrollX();
            mScroller.startScroll(getScrollX(),0,dx,0);
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }
}
