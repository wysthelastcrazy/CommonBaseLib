package com.wys.interview.android.view.henCoder

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wys.baselib.utils.RegularUtils
import com.wys.baselib.utils.ResourceUtil
import com.wys.interview.R

/**
 * @author wangyasheng
 * @date 2020/8/18
 * @Describe:自定义View 1-2 Paint详解
 */
class ThirdView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {

    constructor(context: Context?,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context?):this(context,null)

    /**
     * Paint的API大致可以分为4类：
     * - 颜色
     * - 效果
     * - drawText()相关
     * - 初始化
     */
    private val mPaint = Paint().apply {

        /**
         * 颜色
         */
        color = Color.CYAN
        isAntiAlias = true
    }
    private val bitmap = BitmapFactory.decodeResource(context?.resources,R.drawable.img_j1)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.RED)

        /**
         * 画Bitmap
         * - drawBitmap(Bitmap bitmap,float left,float top,Paint paint)
         * left和top是bitmap绘制的位置坐标。
         *
         * - drawBitmap(Bitmap bitmap,Rect src,RectF dst,Paint paint)
         * - drawBitmap(Bitmap bitmap,Rect src,Rect dst,Paint paint)
         * 第一个参数为要绘制的bitmap对象，第二个参数为要绘制的Bitmap对象的矩形区域，
         * 第三个参数为要将bitmap绘制在屏幕的什么地方
         *
         * - drawBitmap(Bitmap bitmap,Matrix matrix,Paint paint)
         * matrix：绘制bitmap时用于变换的矩阵
         *
         *
         * 绘制Bitmap对象，就是把这个Bitmap中的像素内容贴过来。
         *
         *
         * drawBitmap()还有一个兄弟方法drawBitmapMesh()，可以绘制具有网格拉伸
         * 效果的Bitmap。
         */

        bitmap?.let {
            val bWidth = bitmap.width
            val bHeight = bitmap.height

            val matrix = Matrix().apply {
                //设置起点
                postTranslate(0f,0f)
                //设置缩放
                setScale(measuredWidth.toFloat()/bWidth/2f,measuredHeight.toFloat()/bHeight/2f)
            }
//            canvas?.drawBitmap(it,0f,0f,mPaint)
            canvas?.drawBitmap(it,matrix,mPaint)
        }

        /**
         * 绘制文字
         * - drawText(String text,float x,float y,Paint paint)
         * text：要绘制的字符串
         * x和y：绘制的起点坐标
         */
        mPaint.textSize = ResourceUtil.getDimen(context,R.dimen.sp_12)
        canvas?.drawText(
                "Hello HenCoder",
                measuredWidth.toFloat()/2f,
                measuredHeight.toFloat()/4f,
                mPaint
        )
    }

}