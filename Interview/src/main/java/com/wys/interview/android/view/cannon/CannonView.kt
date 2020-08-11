package com.wys.interview.android.view.cannon

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.wys.baselib.utils.ResourceUtil
import com.wys.interview.R

/**
 * @author wangyasheng
 * @date 2020/8/10
 * @Describe:可旋转的ImageView
 */
class CannonView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int ) : AppCompatImageView(context, attrs, defStyleAttr) {
    constructor(context: Context?,attrs: AttributeSet?): this(context,attrs,0)
    constructor(context: Context?): this(context,null)
    private val TAG = "CannonView"
    private var angle = 0f
    private val marginBottom = ResourceUtil.getDimen(context,R.dimen.dp_40)
    var dw = ResourceUtil.getDimen(context,R.dimen.dp_83).toInt()
    var dy = ResourceUtil.getDimen(context,R.dimen.dp_154).toInt()


    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.rotate(angle,(measuredWidth/2).toFloat(),measuredHeight.toFloat() )
            it.translate(0f,marginBottom)

        }
//        drawable?.setBounds( dw/2,measuredHeight - dy ,
//                dw/2+dw, measuredHeight)
        drawable?.setBounds( (measuredWidth - dw)/2,measuredHeight - dy ,
                (measuredWidth - dw)/2+dw, measuredHeight)
        super.onDraw(canvas)
    }

    public fun turnAngle(angle: Float){
        this.angle = angle
        invalidate()
    }
}