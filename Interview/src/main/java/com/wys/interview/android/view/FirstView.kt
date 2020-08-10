package com.wys.interview.android.view

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * @author wangyasheng
 * @date 2020/8/7
 * @Describe:
 */
class FirstView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    constructor(context: Context?): this(context,null)
    constructor(context: Context?,attrs: AttributeSet?): this(context,null,0)


    fun test(){}
}