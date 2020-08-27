package com.wys.interview.android.henCoder.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.wys.baselib.utils.ResourceUtil
import com.wys.interview.R
import java.lang.Float.min

/**
 * @author wangyasheng
 * @date 2020/8/26
 * @Describe:
 */
class SixthView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    constructor(context: Context?,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context?):this(context,null)

    val paint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
    }
    val margin = ResourceUtil.getDimen(context, R.dimen.dp_20)
    private val translation = ResourceUtil.getDimen(context, R.dimen.dp_100)
//    var color = Color.GREEN
//    set(value) {
//        field = value
//        paint.color = field
//        invalidate()
//    }
//    var position = PointF()
//    set(value) {
//        field = value
//
//    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(
                measuredWidth.toFloat()/2f,
                measuredHeight.toFloat()/2f,
                min((measuredWidth.toFloat() - paddingLeft - paddingRight)/2f,
                        (measuredHeight.toFloat() - paddingTop - paddingBottom)/2f),
                paint
        )
    }
    fun setColor(color:Int){
        paint.color = color
        invalidate()
    }
    fun setPosition(pointF: PointF){
        translationX = pointF.x*translation
        translationY = pointF.y*translation
    }
}