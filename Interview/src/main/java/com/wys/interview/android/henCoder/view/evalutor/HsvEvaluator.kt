package com.wys.interview.android.henCoder.view.evalutor

import android.animation.TypeEvaluator
import android.graphics.Color

/**
 * @author wangyasheng
 * @date 2020/8/26
 * @Describe:
 */
class HsvEvaluator:TypeEvaluator<Int> {
    private val startHsv = floatArrayOf(0f,0f,0f)
    private val endHsv = floatArrayOf(0f,0f,0f)
    private val outHsv = floatArrayOf(0f,0f,0f)
    override fun evaluate(fraction: Float, startValue: Int, endValue: Int): Int {
        //把ARGB转换成HSV
        startValue?.let { Color.colorToHSV(it,startHsv) }
        endValue?.let { Color.colorToHSV(it,endHsv) }

        //计算当前动画完成度（fraction）所对应的颜色值
        if (endHsv[0] - startHsv[0] > 180f){
            endHsv[0] -= 360f
        }else if (endHsv[0] - startHsv[0] < - 180f){
            endHsv[0] += 360f
        }
        outHsv[0] = startHsv[0] + (endHsv[0] - startHsv[0]*fraction)
        if (outHsv[0] > 360f){
            outHsv[0] -= 360f
        }else if (outHsv[0] < 0f){
            outHsv[0] += 360f
        }
        outHsv[1] = startHsv[1] + (endHsv[1] - startHsv[1]*fraction)
        outHsv[2] = startHsv[2] + (endHsv[2] - startHsv[2]*fraction)

        //计算当前动画完成度对应的透明度
        val alpha = startValue.shr(24) + ((endValue.shr(24) - startValue.shr(24)*fraction)).toInt()
        //把HSV转换回ARGB返回
        return Color.HSVToColor(alpha,outHsv)
    }
}