package com.wys.interview.android.henCoder.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.wys.baselib.utils.ResourceUtil
import com.wys.interview.R
import kotlin.math.min

/**
 * @author wangyasheng
 * @date 2020/8/7
 * @Describe:自定义View 1-1 绘制基础 - 01
 */
class FirstView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    constructor(context: Context?): this(context,null)
    constructor(context: Context?,attrs: AttributeSet?): this(context,null,0)

    /**
     * Paint类几个常用的方法：
     * - Paint.setStyle(Style style)设置绘制模式
     * - Paint.setColor(int color)设置颜色
     * - Paint.setStrokeWidth(float width)设置线条宽度
     * - Paint.setTextSize(float textSize)设置文字大小
     * - Paint.setAntiAlias(boolean aa)设置抗锯齿开关
     *
     */
    private val paint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        /**
         * Canvas的一系列drawXXX()方法绘制内容
         */

        /**
         * drawColor(),在整个绘制区域统一涂上指定颜色
         * 类似方法还有drawRGB(int r,int g,int b)
         * 和drawARGB(int a,int r,int g,int b)
         * 只是使用方式不同，作用都一样。这类颜色填充方法一般用于在会之前
         * 设置底色，或者在绘制之后设置半透明蒙版。
         */
        canvas?.drawColor(Color.RED)
        /**
         * drawCircle(float centerX,float centerY,float radius,Paint paint)
         * 画圆，前两个参数centerX和centerY是圆心坐标，第三个参数是半径，单位都是像素。
         * 坐标系以该View的左上角的那个点为原点，水平方向x轴，右正左负；
         * 竖直方向y轴，下正上负。
         */
        //以中心点为远点，宽高较小的值为直径，绘制一个圆
        /**
         * style具体有三种类型：
         * - FILL：填充
         * - STROKE：画线模式（即勾边模式）
         * - FILL_AND_STROKE：既画线又填充
         * 它的默认值是FILL填充模式
         */
//        paint.style = Paint.Style.STROKE
//        paint.strokeWidth = ResourceUtil.getDimen(context, R.dimen.dp_10)
        canvas?.drawCircle(
                (measuredWidth/4).toFloat(),
                (measuredHeight/4).toFloat(),
                min((measuredWidth/4).toFloat(),(measuredHeight/4).toFloat()),
                paint
        )
        /**
         * 画矩形
         * drawRect(float left,float top,float right,float bottom,Paint paint)
         * 其中left，top，right，bottom是四个端点的坐标。
         * 还有两个重载方法drawRect(RectF rect, Paint paint)和drawRect(Rect rect,Paint paint)
         * 直接填写RectF和Rect对象绘制矩形
         */
        canvas?.drawRect(
                (measuredWidth/2).toFloat(),
                0f,
                measuredWidth.toFloat(),
                (measuredHeight/2).toFloat(),
                paint)

        /**
         * 画点
         * drawPoint(float x,float y, Paint paint)
         * x和y是点的坐标。点的大小可以通过paint.setStrokeWidth(width)设置；
         * 点的形状可以通过paint.setStrokeCap(cap)来设置：ROUND画出来的是
         * 圆形的点，SQUARE或BUTT画出来的是方形的点。
         *
         * 注：paint.setStrokeCap(cap)可以设置点的形状，但是这个方法并不是专门
         * 用来设置点的形状的，而是一个设置线条端点形状的方法。端点有圆头（ROUND）、
         * 平头（BUTT）和方头（SQUARE）三种。
         */
        paint.strokeWidth = ResourceUtil.getDimen(context,R.dimen.dp_6)
        paint.strokeCap = Paint.Cap.BUTT
        canvas?.drawPoint((measuredWidth/4).toFloat(),(3*measuredHeight/4).toFloat(),paint)

        /**
         * 画点（批量）
         * drawPoints(float[] pts,int offset,int count,Paint paint)
         * drawPoints(float[] pts,Paint paint)
         *
         * pts：点坐标数组，每两个成一对；
         * offset：表示跳过数组的前几个数再开始记坐标；
         * count：表示一共要绘制几个数。
         */
        val margin = ResourceUtil.getDimen(context,R.dimen.dp_5)
        val points = floatArrayOf(0f,0f,
                measuredWidth/2+margin,measuredHeight/2+margin,
                measuredWidth - margin,measuredHeight/2 + margin,
                measuredWidth/2 + margin,measuredHeight - margin,
                measuredWidth - margin, measuredHeight - margin)
        canvas?.drawPoints(points,2,6,paint)

        /**
         * 画椭圆
         * drawOval(float left, float top, float right, float bottom, Paint paint)
         * 只能绘制横着的或者竖着的椭圆，不能直接绘制斜的椭圆（要实现斜的绘制需要配合集合变换）；
         * left，top，right，bottom是这个椭圆的左、上、右、下四个边界点的坐标.
         * 还有一个重载方法drawOval(RectF rect,Paint paint),直接使用RectF对象绘制椭圆。
         */
        canvas?.drawOval(
                0f,
                measuredHeight.toFloat()/2f,
                measuredWidth.toFloat()/2f,
                3f*measuredHeight.toFloat()/4f,
                paint)
    }
}