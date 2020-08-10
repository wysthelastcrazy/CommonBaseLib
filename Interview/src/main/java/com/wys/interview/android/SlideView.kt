package com.wys.interview.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * @author wangyasheng
 * @date 2020/8/7
 * @Describe:
 */
class SlideView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    companion object{
        val TAG = "SlideView"
    }
    /**
     * 记录上次滑动的坐标
     */
    private var mLastX = 0
    private var mLastY = 0

    /**
     * 初始化画笔
     */
    val paint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        strokeWidth = 3f
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                mLastX = event.x.toInt()
                mLastY = event.y.toInt()
                Log.d(TAG,"down point: x = $mLastX  y = $mLastY")
            }
            MotionEvent.ACTION_MOVE ->{
                //计算view新的位置
                var offsetX = event.x.toInt() - mLastX
                var offsetY = event.y.toInt() - mLastY
                layout(left + offsetX, top + offsetY, right + offsetX, bottom + offsetY)
            }
            MotionEvent.ACTION_UP ->{

            }
        }
        //消耗事件
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(300f,300f,150f,paint)
    }
}