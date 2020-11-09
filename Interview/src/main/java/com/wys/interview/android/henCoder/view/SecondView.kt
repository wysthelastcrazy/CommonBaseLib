package com.wys.interview.android.henCoder.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.wys.baselib.utils.ResourceUtil
import com.wys.interview.R
import kotlin.math.min

/**
 * @author wangyasheng
 * @date 2020/8/17
 * @Describe:自定义View 1-1 绘制基础 - 02
 */
class SecondView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {

    constructor(context: Context?,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context?):this(context,null)

    private val mPaint = Paint().apply {
        color = Color.YELLOW
        isAntiAlias = true
    }
    private val path = Path()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.RED)

        /**
         * 画线
         * drawLine(float startX,float startY,float stopX,float stopY,Paint paint)
         * 由于直线不是封闭图形，所以Paint.setStyle(style)对直线没有影响。
         */
        canvas?.drawLine(
                0f,
                0f,
                measuredWidth.toFloat(),
                measuredHeight.toFloat(),
                mPaint
        )

        /**
         * 批量画线
         * drawLines(float[] pts,int offset,int count,Paint paint)
         * drawLines(float[] pts,Paint paint)
         */
        val paints = floatArrayOf(0f,measuredHeight.toFloat()/2,
                measuredWidth.toFloat()/2,0f,
                measuredWidth.toFloat()/2,0f,
                measuredWidth.toFloat(),measuredHeight.toFloat()/2,
                measuredWidth.toFloat(),measuredHeight.toFloat()/2,
                measuredWidth.toFloat()/2,measuredHeight.toFloat(),
                measuredWidth.toFloat()/2,measuredHeight.toFloat(),
                0f,measuredHeight.toFloat()/2)
        canvas?.drawLines(paints,mPaint)


        /**
         * 画圆角矩形
         * drawRoundRect(float left,float top,float right,float bottom,float rx,float ry,Paint paint)
         * left,top,right,bottom是四条边的坐标，rx和ry是圆角横向半径和纵向半径。
         *
         * 另外，它还有一个重载方法：
         * drawRoundRect(RectF rect, float rx,float ry,Paint paint)
         */
        val margin = ResourceUtil.getDimen(context, R.dimen.dp_5)
        canvas?.drawRoundRect(
                0f+margin,
                0f+margin,
                measuredWidth.toFloat()/2 - margin,
                measuredHeight.toFloat()/4,
                50f,
                50f,
                    mPaint
        )

        /**
         * 绘制弧形或扇形
         * drawArc(float left,float top,float right,float bottom,float startAngle,
         *          float sweepAngle,boolean useCenter,Paint paint)
         * drawArc()是使用一个椭圆来描述弧形的。
         * left,top,right,bottom描述的是这个弧形所在的椭圆；
         * startAngle是弧形的起始角度（x轴的正向是0度的位置；顺时针为正角度，逆时针为负角度），
         * sweepAngle是弧形划过的角度；
         * useCenter表示是否链接到圆心，如果不连接到圆心，就是弧形，如果链接到
         * 圆心就是扇形。
         */
        canvas?.drawArc(
                measuredWidth.toFloat()/2f,
                0f,
                measuredWidth.toFloat(),
                measuredHeight.toFloat()/2f,
                0f,
                100f,
                true,
                mPaint
        )

        /**
         * 通过drawPath()绘制自定义图形
         * drawPath(Path path, Paint paint)
         * 此方法通过描述路径的方式来绘制图形。
         * path参数用来描述图形路径的对象。
         *
         * Path可以描述直线、二次曲线、三次曲线、圆、椭圆、弧形、矩形、圆角矩形。
         * 把这些图形结合起来，就可以描述出很多复杂的图形。
         *
         * Path有两类方法，一类是直接描述路径的，另一类是辅助设置或计算。
         *
         */

        path.addArc(0f,
                measuredHeight.toFloat()/2,
                measuredWidth.toFloat()/4,
                3f*measuredHeight.toFloat()/4f,
                -225f,
                225f)
        path.arcTo(measuredWidth.toFloat()/4,
                measuredHeight.toFloat()/2,
                measuredWidth.toFloat()/2,
                3f*measuredHeight.toFloat()/4f,
                -180f,
                225f,
                false)
        path.lineTo(measuredWidth.toFloat()/4,measuredHeight.toFloat())
//        canvas?.drawPath(path,mPaint)

        /**
         * Path方法第一类：直接描述路径
         * 这类方法还可以细分为两组：添加子图形和画线（直线或曲线）
         */
        /**
         *
         * 第一组：addXXX() -- 添加子图形
         *
         * - addCircle(float x,float y,float radius,Direction dir)添加圆
         *   x，y，radius这三个参数是圆的基本信息，最后一个参数dir是画圆的路径的方向
         *   路径方向有两种：顺时针(CW)和逆时针(CCW)。对于普通情况，这个参数填CW还是CCW
         *   没有影响。它只是在需要填充图形（Paint.Style为FILL或FILL_AND_STROKE），
         *   并且图形出现相交时，用于判断填充范围的。
         *
         * 添加椭圆
         * - addOval(float left,float top,float right,float bottom,Direction dir)
         * - addOval(RectF oval,Direction dir)
         *
         * 添加矩形
         * - addRect(float left, float top,float right,float bottom,Direction dir)
         * - addRect(RectF rect,Direction dir)
         *
         * 添加圆角矩形
         * - addRoundRect(float left,float top,float right,float bottom,float rx,float ry,Direction dir)
         * - addRoundRect(RectF rect,float rx,float ry,Direction dir)
         *
         * 添加另一个Path
         * - addPath(Path path)
         *
         * 以上几个方法和addCircle()的使用差不多。
         **/
        mPaint.color = Color.GREEN
        path.addCircle(
                3f*measuredWidth.toFloat()/4f,
                3f*measuredHeight.toFloat()/4f,
                min(measuredHeight/4,measuredWidth/4).toFloat(),
                Path.Direction.CW
        )
        canvas?.drawPath(path,mPaint)
        /**
         * 第二组：xxxTo() -- 画线（直线或曲线）
         * 这一组和第一组addXXX()的区别在于，第一组是添加完整封闭图形（出了addPath()）,
         * 而这一组添加的只是一条线。
         *
         * 画直线
         * - lineTo(float x,float y)
         * - rLineTo(float x,float y)
         * 从当前位置向目标位置画一条直线，x和y是目标位置的坐标。
         * 这两个方法的区别是，lineTo(x,y)的参数是绝对坐标，而rLineTo(x,y)的
         * 参数是相对当前位置的相对坐标（前缀r指relatively 相对的）。
         *
         * 当前位置：即最后一次调用画path的方法的终点位置，初始值为原点(0,0)
         *
         *
         * 画二次贝塞尔曲线
         * - quadTo(float x1,float y1,float x2,float y2)
         * - rQuadTo(float dx1,float dy1,float dx2,float dy2)
         * 这条二次贝塞尔曲线的起点为当前位置，参数中的x1，y1和x2，y2则
         * 分别是控制点和终点的坐标。rQuadTo()的参数也是相对坐标。
         *
         * 画三次贝塞尔曲线
         * - cubicTo(float x1,float y1,float x2,float y2,float x3,float y3)
         * - rCubicTo(float x1,float y1,float x2,float y2,float x3,float y3)
         * 和上面quadTo(),rQuadTo()的二次贝塞尔曲线同理，但是有两个控制点。
         *
         * 移动到目标位置
         * - moveTo(float x,float y)
         * - rMoveTo(float x,float y)
         * 此方法虽然不添加图形，但是它会设置图形的起点，改变当前位置。是一个
         * 非常重要的辅助方法。
         *
         * 画弧形
         * - arcTo(float left,float top,float right,float bottom,float startAngle,float sweepAngle,boolean forceMoveTo)
         * - arcTo(RectF oval,float startAngle,float sweepAngle,boolean forceMoveTo)
         * 这个方法和Canvas.drawArc()比起来，少了一个参数useCenter，而多了一个参数forceMoveTo。
         * 少了useCenter是因为arcTo()只用来画弧形而不画扇形，所以不再需要useCenter参数；而多出来
         * 的这个forceMoveTo参数的意思是，绘制是要'抬一下笔移动过去'，还是'直接拖着笔过去'，
         * 区别在于是否留下移动的痕迹。
         *
         * - addArc(float left,float top,float right,float bottom,float startAngle,float sweepAngle)
         * - addArc(RectF oval,float startAngle,float sweepAngle)
         * 这也是画弧形的方法，和arcTo()的区别在于，addArc()是直接使用了forceMoveTo = true的简化版arcTo().
         *
         *
         * 封闭当前子图形
         * - close()
         * 它的作用是把当前的子图形封闭，即由当前子图形的起点绘制一条直线
         * 另外，不是所有的子图形都需要使用close()来封闭。当需要填充图形时
         * 即Paint.Style为FILL或FILL_AND_STROKE是，path会自动封闭子图形。
         */


        /**
         * Path方法第二类：辅助的设置或计算
         * 此类方法的使用场景比较少，在此先只了解一下setFillType方法
         *
         * 设置填充方式
         * - setFillType(Path.FillType ft)
         * 此方法是用来设置图形自相交时的填充算法的。
         * 参数Path.FillType不同，就会有不同的填充效果。Path.FillType的取值有四个：
         * - EVEN_ODD
         * - WINDING（默认值）
         * - INVERSE_EVEN_ODD
         * - INVERSE_WINDING
         * 两个带有INVERSE_前缀的，只是前两个的反色版本，所以只要把前两个搞明白就可以了。
         * WINDING是'全填充'；EVEN_ODD是'交叉填充'
         *
         *
         */

        path.addCircle(
                measuredWidth.toFloat() / 2 - 5f*margin,
                measuredHeight.toFloat() / 4f,
                measuredHeight.toFloat() / 4f,
                Path.Direction.CW)
        path.addCircle(
                measuredWidth.toFloat() / 2 + 5f*margin,
                measuredHeight.toFloat() / 4f,
                measuredHeight.toFloat() / 4f,
                Path.Direction.CW)
        //交叉填充
        path.fillType = Path.FillType.EVEN_ODD
        canvas?.drawPath(path,mPaint)
    }
}