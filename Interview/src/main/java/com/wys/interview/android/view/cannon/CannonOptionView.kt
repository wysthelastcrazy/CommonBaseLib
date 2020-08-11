package com.wys.interview.android.view.cannon

import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView

/**
 * @author wangyasheng
 * @date 2020/8/11
 * @Describe:
 */
class CannonOptionView(context: Context, private val optionType: Int) : FrameLayout(context) {

    init {
        when(optionType){
            1 -> {
                val ivOption = ImageView(context).apply {

                }
            }
            2 ->{

            }
        }
    }
}