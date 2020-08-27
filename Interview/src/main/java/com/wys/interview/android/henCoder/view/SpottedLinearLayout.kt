package com.wys.interview.android.henCoder.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import com.wys.baselib.utils.ResourceUtil
import com.wys.interview.R

/**
 * @author wangyasheng
 * @date 2020/8/25
 * @Describe:自定义view绘制顺序
 */
class SpottedLinearLayout(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr) {
    constructor(context: Context?,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context?):this(context,null)

    /**
     * 绘制过程简述
     *
     * 绘制过程中最典型的两个部分就是主体和子View，除此之外，绘制过程还包含
     * 一些其他内容的绘制。一个完整的绘制过程会一次绘制一下几个内容:
     * - 背景；
     * - 主体（onDraw()）；
     * - 子View(dispatchDraw())；
     * - 滑动边缘渐变和滑动条；
     * - 前景（前景的支持在Android 6.0才加入）。
     */
    private val paint = Paint().apply {
        color = Color.CYAN
        isAntiAlias = true
    }
    private val radius = ResourceUtil.getDimen(context, R.dimen.dp_20)
    private val marginTop = ResourceUtil.getDimen(context,R.dimen.dp_10)
    /**
     * 绘制主体
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    /**
     * 绘制子View的方法
     * 在绘制过程汇总，每个View和ViewGroup都会先调用onDraw()方法来绘制主体，
     * 再调用dispatchDraw()方法来绘制子View。
     */
    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        canvas?.let {
            it.drawCircle(
                    measuredWidth.toFloat() - 2*radius,
                    measuredWidth.toFloat() - 2*radius,
                    radius,paint)
            it.drawCircle(measuredWidth-radius*2f,
                    radius*4f,
                    radius/2f,paint)
            it.drawCircle(2*radius,
                    measuredHeight - 4*radius,
                    radius/2f,paint)
            it.drawCircle(2*radius,4*radius,radius/4f,paint)
        }
    }

    /**
     * 绘制滑动边缘渐变和滑动条、前景
     *
     * 滑动边缘渐变和滑动条可以通过xml的android:scrollbarXXX系列属性
     * 或Java代码的View.setXXXScrollbarXXX()系列方法设置；
     *
     * 前景可以通过xml的android:foreground属性或Java代码的View.setForeground()设置。
     *
     * 这个方法是在API 23才引入的，所以在重写这个方法的时候要确认minSdk达到了23，
     * 不然低版本手机没有效果
     * */
    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        canvas?.let {
            it.save()
            paint.color = Color.RED
            it.drawRect(
                    0f,
                    marginTop,
                    ResourceUtil.getDimen(context,R.dimen.dp_80),
                    ResourceUtil.getDimen(context,R.dimen.dp_35),
                    paint
            )
            it.restore()
            paint.color = Color.WHITE
            paint.textSize = ResourceUtil.getDimen(context,R.dimen.sp_14)
            it.drawText(
                    "New",
                    0f,
                    marginTop+ResourceUtil.getDimen(context,R.dimen.dp_20),
                    paint
            )
        }

    }

    /**
     * draw()总调度方法
     * draw()是绘制过程的总调度方法。一个View的整个绘制过程都发生在
     * draw()方法里。前面的背景、主体、子View、滑动相关以及前景的绘制，
     * 它们其实都是在draw()方法里调用的。
     */
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
    }

    /**
     * 注意
     * 1、出于效率的考虑，ViewGroup默认会绕过draw()方法，直接执行
     * dispatchDraw()，以此开简化绘制流程。在ViewGroup的子类中
     * 重写除dispatchDraw()以外的绘制方法时，可能需要调用
     * setWillNotDraw(false);
     *
     * 2、有时候，一段代码写在不同的绘制方法中效果是一样的，此时可以按照
     * 自己习惯的绘制方法来重写。但是有一个例外：如果绘制代码既可以写在
     * onDraw()里也可以写在其他绘制方法里，那么优先写在onDraw()，因为
     * Android有相关的优化，可以在不需要重绘的时候自动跳过onDraw()的
     * 重复执行，以提升效率。享受这种优化的只有onDraw()一个方法。
     */
}