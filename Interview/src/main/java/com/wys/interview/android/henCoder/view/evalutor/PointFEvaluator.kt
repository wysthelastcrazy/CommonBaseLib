package com.wys.interview.android.henCoder.view.evalutor

import android.animation.TypeEvaluator
import android.graphics.PointF

/**
 * @author wangyasheng
 * @date 2020/8/26
 * @Describe:
 */
class PointFEvaluator:TypeEvaluator<PointF> {
    private val newPoint = PointF()
    override fun evaluate(fraction: Float, startValue: PointF?, endValue: PointF?): PointF {
        val startX = startValue?.x?:0f
        val startY = startValue?.y?:0f
        val endX = endValue?.x?:0f
        val endY = endValue?.y?:0f

        val x = startX + (endX - startX)*fraction
        val y = startY +(endY - startY)*fraction

        newPoint.set(x,y)
        return newPoint
    }
}

