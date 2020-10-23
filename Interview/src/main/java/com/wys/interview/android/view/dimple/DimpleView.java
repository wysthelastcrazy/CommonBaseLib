package com.wys.interview.android.view.dimple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;

/**
 * @author wangyasheng
 * @date 2020/10/20
 * @Describe:
 */
public class DimpleView extends View {
    //粒子集合
    private List<Particle> particleList = new ArrayList<>();
    private Paint paint;
    private float centerX;
    private float centerY;
    public DimpleView(Context context) {
        this(context,null);
    }

    public DimpleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DimpleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w/2;
        centerY = h/2;
        Random random = new Random();
        int nextX = 0;
        for(int i = 0; i<500;i++){
            nextX = random.nextInt((int) (centerX*2));
            particleList.add(new Particle(nextX,centerY,2,2,100));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        for (Particle p :particleList){
            canvas.drawCircle(p.x,p.y,p.radius,paint);
        }
    }
}
