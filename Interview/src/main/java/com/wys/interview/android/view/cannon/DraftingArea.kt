package com.wys.interview.android.view.cannon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.wys.baselib.utils.ResourceUtil
import com.wys.interview.R
import com.wys.interview.android.view.util.AngleCalculator
import kotlin.math.min

/**
 * @author wangyasheng
 * @date 2020/8/11
 * @Describe:
 */
class DraftingArea(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    constructor(context: Context?): this(context,null)
    private val mPaint = Paint().apply {
        color = Color.WHITE
        alpha = (255 * 0.8).toInt()
        style = Paint.Style.STROKE
        strokeWidth = ResourceUtil.getDimen(context, R.dimen.dp_3)
        pathEffect = DashPathEffect(floatArrayOf(15f,10f),0f)
    }

    /**
     * 最大与最小转动角度
     */
    private var maxAngle: Float = 0f
    private var minAngle: Float = 0f

    /**
     * 控件的宽高
     */
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    /**
     * down事件落下的点位置
     */
    private var mTouchDownX = 0f
    private var mTouchDownY = 0f

    /**
     * move事件位置坐标
     */
    private var mTouchX = 0f
    private var mTouchY = 0f

    /**
     * 基点位置，用于计算旋转角度
     */
    private var BASE_POINT_X = 0
    private var BASE_POINT_Y = 0

    /**
     * 指示线top
     */
    private var top1 = 0
    private var top2 = 0

    /**
     * 当前旋转角度，默认为90度
     */
    private var currentAngle = 90f

    /**旋转角度计算等功能工具类*/
    private lateinit var angleCalculator: AngleCalculator

    private var downTime = 0L
    private var upTime = 0L

    private var callback: CannonCallback? = null
    init {
        setLayerType(LAYER_TYPE_SOFTWARE,null)
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        BASE_POINT_X = measuredWidth / 2
        BASE_POINT_Y = measuredHeight /2
        angleCalculator = AngleCalculator(BASE_POINT_X.toFloat(),BASE_POINT_Y.toFloat())
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                downTime = System.currentTimeMillis()
                mTouchDownX = event.x
                mTouchDownY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                mTouchX = event.x
                mTouchY= event.y
                disposeTouchEvent()
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL ->{
                upTime = System.currentTimeMillis()
                //如果down事件和up事件相隔相差小于100ms，则认为是点击事件
                if (upTime - downTime < 100){
                    angleCalculator?.getXWithAngle(currentAngle,top2.toFloat())?.let { callback?.onClick(it,top2) }
                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
//        angleCalculator?.getXWithAngle(currentAngle,top2.toFloat())?.let {
//            canvas?.drawLine(BASE_POINT_X.toFloat(),BASE_POINT_Y.toFloat(),it.toFloat(),top2.toFloat(),mPaint)
//        }
        drawLine(canvas)
        super.onDraw(canvas)
    }

    private fun disposeTouchEvent(){
        invalidate()
    }

    /**
     * 设置指示线上边界
     * top1：未指向选项时的上边界
     * top2：指向选项时的上边界（选项下侧）
     */
    public fun setTops(top1: Int,top2: Int){
        this.top1 = top1
        this.top2 = top2
    }
    /**
     * 更新指示线位置
     */
    public fun updateLine(angle: Float){
        currentAngle = angle
        invalidate()
    }

    public fun setCallback(callback: CannonCallback){
        this.callback = callback
    }

    /**
     * 画指示线
     */
    private fun drawLine(canvas: Canvas?){
        currentAngle += angleCalculator.calculateAngleWithBasePoint(mTouchDownX,mTouchDownY) -
                angleCalculator.calculateAngleWithBasePoint(mTouchX,mTouchY)
        mTouchDownY = mTouchY
        mTouchDownX = mTouchX

        if (currentAngle > maxAngle){
            currentAngle = maxAngle
        }else if (currentAngle < minAngle){
            currentAngle = minAngle
        }

        val currentX = angleCalculator.getXWithAngle(currentAngle,0f)
        var y = if (callback?.onTuring(currentX.toFloat(),0f,currentAngle) == true) top2 else top1

        var x = when {
            currentAngle < minAngle -> measuredWidth
            currentAngle > maxAngle -> 0
            else -> angleCalculator.getXWithAngle(currentAngle, y.toFloat())
        }
        canvas?.drawLine(BASE_POINT_X.toFloat(),BASE_POINT_Y.toFloat(),x.toFloat(),y.toFloat(),mPaint)
    }

   public interface CannonCallback{
       fun onTuring(touchX: Float, touchY:Float, currentAngle: Float): Boolean
       fun onClick(x: Int, y: Int)
   }
}