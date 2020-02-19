package com.wys.module_common_ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.wys.module_common_ui.R;

/**
 * Created by yas on 2020-01-10
 * Describe:
 */
@SuppressLint("AppCompatCustomView")
public class TextThumbSeekBar extends SeekBar {
    private float mThumbWidth;//绘制滑块宽度
    private float mThumbHeight;
    private TextPaint mTextPaint;//绘制文本的大小
    private int mSeekBarMin=0;//滑块开始值

    private Rect rectThumb;

    public TextThumbSeekBar(Context context) {
        this(context, null);
    }

    public TextThumbSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }

    public TextThumbSeekBar(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.TextThumbSeekBar);

        mThumbWidth= array.getDimension(R.styleable.TextThumbSeekBar_thumb_width,10f);
        mThumbHeight= array.getDimension(R.styleable.TextThumbSeekBar_thumb_height,5f);
        mTextPaint = new TextPaint();
        float textSize = array.getDimension(R.styleable.TextThumbSeekBar_thumbTextSize,10f);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int unsignedMin = mSeekBarMin < 0 ? mSeekBarMin * -1 : mSeekBarMin;
        String progressText = String.valueOf(getProgress()+unsignedMin);
        Paint bgRect=new Paint();
        bgRect.setStyle(Paint.Style.FILL);
        bgRect.setColor(Color.parseColor("#3300ff00"));

        rectThumb = getThumb().getBounds();
//        canvas.drawRect(rectThumb, bgRect);
        float left = rectThumb.left - getThumbOffset() +getPaddingLeft();
        Rect rect = new Rect((int) left,0,(int)(left+mThumbWidth), (int) mThumbHeight);
//        canvas.drawRect(rect, bgRect);
        //计算baseline
        Paint.FontMetrics fontMetrics=mTextPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline=rect.centerY()+distance;
        canvas.drawText(progressText, rect.centerX(), baseline, mTextPaint);
    }

    public void setMix(int min){
        mSeekBarMin=min;
    }
}