package com.wys.interview.android.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import com.wys.baselib.utils.ResourceUtil
import com.wys.interview.R

/**
 * @author wangyasheng
 * @date 2020/8/7
 * @Describe:
 */
class JigsawGameView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : RelativeLayout(context, attrs, defStyleAttr) {
    constructor(context: Context?,attrs: AttributeSet?): this(context,attrs,0)
    constructor(context: Context?): this(context,null)
    private var dragViewGroup: DragViewGroup? = null

    private var ivBackground: ImageView? = null

    private var j1: ImageView? = null
    private var j2: ImageView? = null
    private var j3: ImageView? = null
    private var j4: ImageView? = null

    private var iv1: ImageView? = null
    private var iv2: ImageView? = null
    private var iv3: ImageView? = null
    private var iv4: ImageView? = null

    //itemSize 选项的大小
    private val itemSize = ResourceUtil.getDimen(context,R.dimen.dp_100).toInt()
    //item之间的间隔
    private val gap = ResourceUtil.getDimen(context,R.dimen.dp_5).toInt()
    //内容区域距离左右两边的margin
    private val marginX = ResourceUtil.getDimen(context,R.dimen.dp_50).toInt()
    //上边距
    private val marginTop = ResourceUtil.getDimen(context,R.dimen.dp_10).toInt()
    init{
        //初始化dragViewGroup
        initDragView()
        //初始化lockChild(问题)
        initQuestions()
        //初始化选项（可拖拽项）
        initOptions()
    }
    private fun initDragView(){
        dragViewGroup = DragViewGroup(context)
        dragViewGroup?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)

//        dragViewGroup?.visibility = View.INVISIBLE
        addView(dragViewGroup)
    }
    private fun initQuestions(){
        ivBackground = ImageView(context)
        val layoutParams = LayoutParams(itemSize*2+gap*3,itemSize*2+gap*3)
        layoutParams.setMargins(marginX,marginTop,0,0)
        ivBackground?.setImageResource(R.drawable.bg_jigsaw)
        ivBackground?.layoutParams = layoutParams

        dragViewGroup?.addLockView(ivBackground)

        j1 = ImageView(context)
        val layoutParamsJ1 = LayoutParams(itemSize, itemSize)
        layoutParamsJ1.setMargins(marginX + gap,marginTop + gap,0,0)
        j1?.layoutParams = layoutParamsJ1
        j1?.setBackgroundColor(Color.RED)
        j1?.tag = 1
        dragViewGroup?.addLockView(j1)

        j2 = ImageView(context)
        val layoutParamsJ2 = LayoutParams(itemSize, itemSize)
        layoutParamsJ2.setMargins(marginX + itemSize + gap*2,marginTop + gap,0,0)
        j2?.layoutParams = layoutParamsJ2
        j2?.setBackgroundColor(Color.BLUE)
        j2?.tag = 2
        dragViewGroup?.addLockView(j2)

        j3 = ImageView(context)
        val layoutParamsJ3 = LayoutParams(itemSize, itemSize)
        layoutParamsJ3.setMargins(marginX + gap,marginTop + itemSize + gap*2,0,0)
        j3?.layoutParams = layoutParamsJ3
        j3?.setBackgroundColor(Color.YELLOW)
        j3?.tag = 3
        dragViewGroup?.addLockView(j3)

        j4 = ImageView(context)
        val layoutParamsJ4 = LayoutParams(itemSize, itemSize)
        layoutParamsJ4.setMargins(marginX + itemSize + gap*2,
                marginTop + itemSize + gap*2,0,0)
        j4?.layoutParams = layoutParamsJ4
        j4?.setBackgroundColor(Color.GREEN)
        j4?.tag = 4
        dragViewGroup?.addLockView(j4)
    }
    private fun initOptions(){

        iv1 = ImageView(context).apply {
            layoutParams = LayoutParams(itemSize,itemSize).apply {
                setMargins(0,top + gap,marginX + itemSize + gap*2,0)
                addRule(ALIGN_PARENT_RIGHT)
            }
            setImageResource(R.drawable.img_j1)
            dragViewGroup?.addView(this)
            tag = 1
        }

        iv2 = ImageView(context).apply {
            layoutParams = LayoutParams(itemSize,itemSize).apply {
                setMargins(0,top + gap,marginX + gap,0)
                addRule(ALIGN_PARENT_RIGHT)
            }
            setImageResource(R.drawable.img_j2)
            tag = 2
            dragViewGroup?.addView(this)
        }

        iv3 = ImageView(context).apply {
            layoutParams = LayoutParams(itemSize,itemSize).apply {
                setMargins(0,top + itemSize + gap*2,marginX + itemSize + gap*2,0)
                addRule(ALIGN_PARENT_RIGHT)
            }
            setImageResource(R.drawable.img_j3)
            tag = 3
            dragViewGroup?.addView(this)
        }

        iv4 = ImageView(context).apply {
            layoutParams = LayoutParams(itemSize,itemSize).apply {
                setMargins(0,top + itemSize + gap*2,marginX + gap,0)
                addRule(ALIGN_PARENT_RIGHT)
            }
            setImageResource(R.drawable.img_j4)
            tag = 4
            dragViewGroup?.addView(this)
        }

    }
}