package com.wys.interview.android.henCoder.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

/**
 * @author wangyasheng
 * @date 2020/8/21
 * @Describe:自定义View 1-3 drawText()文字绘制
 */
class FourthView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    constructor(context: Context?,attrs: AttributeSet?): this(context,attrs,0)
    constructor(context: Context?):this(context,null)

    /**
     * 1、Canvas绘制文字的方式
     * Canvas的文字绘制方法有三个：drawText(),drawTextRun()和drawTextOnPath().
     *
     * 1.1 drawText(String text,float x,float y,Paint paint)
     * 此方法是Canvas最基本的绘制文字的方法：给出文字的内容和位置，Canvas按要求绘制文字。
     * x,y：是文字的坐标，但是需要注意，这个坐标并不是文字的左上角，而是一个与左下角比较近
     * 的位置。
     *
     * 参数中的y，指的是基线位置。
     * 因为不同的语言和文字，每个字符的高度和上下位置都是不一样的。要让不同的文字并排
     * 显示的时候整体开起来稳当，需要让它们上下对齐。但这个对齐的方式，不能是简单的
     * '底部对齐'或'顶部对齐'或'中间对齐'，而应该是一种类似于重心对齐的方式。
     * 而这个用来让所有文字互相对齐的基准线，就是基线。
     *
     * x值在第一个字符左边再往左一点点。因为绝大多数的字符，它们的宽度都是要略微大于
     * 实际显示的宽度的。字符的左右两边会留出一部分空隙，用于文字中间的间隔。
     *
     *
     * 1.2 drawTextRun()
     *
     * 1.3 drawTextOnPath(String text,Path path,float hOffset,float vOffset,Paint paint)
     * 沿着一条Path来绘制文字。
     * hOffset,vOffset：是文字相对于Path的水平偏移量和竖直偏移量，利用它们可以
     * 调整文字的位置。
     *
     * 1.4 StaticLayout(CharSequence source,TextPaint paint,int width,Layout.Alignment align
     *     float spacingmult,float spacingadd,boolean includepad)
     * Canvas.drawText()只能绘制单行的文字，而不能换行（不能在View的边缘自动折行），
     * 也不能在换行符'\n'处换行。
     * 如果需要绘制多行的文字，必须把文字切断后分多次使用drawText()来绘制。
     * 使用StaticLayout也可以实现多行文字绘制。StaticLayout是android.text.Layout的子类，
     * 它是纯粹用来绘制文字的。StaticLayout支持换行，它既可以为文字设置宽度上限来让
     * 文字自动换行，也会在'\n'处主动换行。
     *
     */
    val paint = TextPaint().apply {
        color = Color.BLUE
    }
    val text1 = "Lorem Ipsum is simply dummy text of the printing and typesetting industry"
    val text2 = "a\nbc\ndefghi\njklm\nnopqrst\nuvwx\nyz"
    val staticLayout1 = StaticLayout(
            text1,
            paint,
            600,
            Layout.Alignment.ALIGN_NORMAL,
            1f,
            0f,
            true
    )
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        StaticLayout
        canvas?.let {
            it.save()
            it.translate(50f,100f)
            staticLayout1.draw(it)
            it.restore()
        }
    }
}