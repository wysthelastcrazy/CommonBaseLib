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
     * width：文字区域的宽度，文字到达这个宽度后就会自动换行；
     * align：文字的对齐方向；
     * spacingmult：是行间距的倍数，通常情况下填1即可；
     * spacingadd：行间距的额外增加值，通常情况下填0；
     * includepad：是否在文字上下添加额外的空间，来避免某些过高的字符绘制出现越界。
     *
     * Canvas.drawText()只能绘制单行的文字，而不能换行（不能在View的边缘自动折行），
     * 也不能在换行符'\n'处换行。
     * 如果需要绘制多行的文字，必须把文字切断后分多次使用drawText()来绘制。
     * 使用StaticLayout也可以实现多行文字绘制。StaticLayout是android.text.Layout的子类，
     * 它是纯粹用来绘制文字的。StaticLayout支持换行，它既可以为文字设置宽度上限来让
     * 文字自动换行，也会在'\n'处主动换行。
     *
     * 如果需要进行多行文字的绘制，并且对文字的排列和样式没有太复杂的花式要求，那么使用StaticLayout就好。
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

    /**
     * 2、Paint对文字绘制的辅助
     * Paint对文字绘制的辅助，有两类方法：设置显示效果的和测量文字尺寸的。
     */
    /**
     * 2.1 设置显示效果类方法
     *
     * 2.1.1 setTextSize(float textSize) 设置文字大小。
     *
     * 2.1.2 setTypeface(Typeface typeface) 设置字体。
     * 在assets文件夹中获取'.ttf'文件设置不同的字体。
     *
     * 2.1.3 setFakeBoldText(boolean fakeBoldText) 设置粗体。
     *
     * 2.1.4 setStrikeThruText(boolean strikeThruText) 设置删除线。
     *
     * 2.1.5 setUnderlineText(boolean underlineText) 设置下划线。
     *
     * 2.1.6 setTextSkewX(float skewX) 设置文字横向错切角度（文字倾斜度）。
     *
     * 2.1.7 setTextScaleX(float scaleX) 设置文字横向缩放。
     *
     * 2.1.8 setLetterSpacing(float letterSpacing) 设置字符间距，默认值为0。
     *
     * 2.1.9 setFontFeatureSetting(String settings) 用 CSS 的 font-feature-settings 的方式来设置文字。
     *
     * 2.1.10 setTextAlign(Paint.Align align) 设置文字的对齐方式。
     *
     * 2.1.11 setTextLocale(Locale locale)/setTextLocales(LocaleList locales)
     * 设置绘制所使用的Locale（地区）。
     *
     * 2.1.12 setHinting(int mode)设置是否启用字体的hinting（微调）。
     */
    /**
     * 2.2 测量文字尺寸类
     * 无论是文字，还是图形或bitmap，只有知道了尺寸才能更好的确定应该摆放的位置。
     * 由于文字的绘制和图形或Bitmap的绘制比起来，尺寸的计算复杂很多，所以它有一
     * 整套的方法来计算文字尺寸。
     *
     * 2.2.1 float getFontSpacing() 获取推荐的行间距。
     * 即推荐的两行文字的baseline的距离。这个值是系统根据文字的字体和字号自动计算的。
     * 它的作用是当要手动绘制多行文字（而不是使用StaticLayout）的时候，可以在换行的
     * 时候给y坐标加上这个值来下移文字。
     *
     * 2.2.2 FontMetrics getFontMetrics()
     * Paint根据当前的字体和字号，获取Paint的FontMetrics。
     * FontMetrics是个相对专业的工具类，它提供了几个文字排印方面的数值：
     * ascent/descent：它们的作用是限制普通字符的顶部和底部范围。普通字符
     *                 上不会高过ascent，下不会低过descent。具体在Android
     *                 的绘制中，ascent的值是和baseline的相对位移，它的值为负（在baseline上方）。
     *                 descent的值是和baseline的相对位移，它的值为正（在baseline下边）。
     * top/bottom：它们的作用是限制所有字形的顶部和底部范围（除了普通字符还有特殊字符）。
     *            值也是相对baseline的位移。
     * leading：行的额外间距，即对于上下相邻的两行，上行的bottom线和下行的top线的距离。
     *
     * 另外ascent和descent还可以通过Paint.ascent()he Paint.descent()快捷获取。
     *
     *
     * 2.2.3 getTextBounds(String text,int start,int end,Rect bounds)
     * 获取文字的显示范围。
     * text：要测量的文字；
     * start/end：文字的起始和结束位置；
     * bounds：储存文字显示范围的对象。
     *
     * 2.2.4 float measureText(String text)
     * 测量文字的宽度并返回。
     * measureText()测量的是文字的显示范围，而getTextBounds()获取的好似显示范围，
     * 因为每个字符之间都有空隙的，所以measureText()比getTextBounds()测量处的宽度要大一些。
     *
     * 2.2.5 getTextWidths(String text,float[] widths)
     * 获取字符串中每个字符的宽度，并把结果填入参数widths中。
     *
     * 2.2.6 int breakText(String text,boolean measureForwards,float maxWidth,float[] measuredWidth)
     * 这个方法也是用来测了文字宽度的。但是和measureText()的区别是，
     * breakText()是在给出宽度上限的前提下测量文字的宽度。如果文字的宽度超出了上限，
     * 那么在临近超限的位置截断文字。
     * 返回值是截取的文字个数。
     */
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