package com.wys.interview.android.view.cannon

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.wys.baselib.utils.ResourceUtil
import com.wys.interview.R

/**
 * @author wangyasheng
 * @date 2020/8/10
 * @Describe:
 */
class CannonGameView constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr){
    constructor(context: Context?,attrs: AttributeSet?): this(context,attrs,0)
    constructor(context: Context?):this(context,null)

    private var cannonView: CannonView? = null
    private var draftingArea: DraftingArea? = null
    init {
//        draftingArea = DraftingArea(context).apply {
//            layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT)
//            setCallback(object :DraftingArea.CannonCallback{
//                override fun onTuring(touchX: Float, touchY: Float, currentAngle: Float): Boolean {
//                    cannonView?.turnAngle(90 - currentAngle)
//                    return false
//                }
//
//                override fun onClick(x: Int, y: Int) {
//                }
//
//            })
//        }
//        addView(draftingArea)

        cannonView = CannonView(context).apply {
            val width = ResourceUtil.getDimen(context,R.dimen.dp_310).toInt()
            val height = ResourceUtil.getDimen(context,R.dimen.dp_360).toInt()
            layoutParams = LayoutParams(width,height).apply {
                addRule(CENTER_HORIZONTAL)
                addRule(ALIGN_PARENT_BOTTOM)
            }
            setImageResource(R.drawable.cannon)
            scaleType = ImageView.ScaleType.FIT_XY
        }
        addView(cannonView)
    }
}