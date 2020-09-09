package com.wys.interview.android.bean

import android.util.Log

/**
 * @author wangyasheng
 * @date 2020/8/31
 * @Describe:
 */
class Man:Person {
    init {
        Log.d("wys","[Man&init]+++++++++++++")
    }
    constructor():super("wys"){
        Log.d("wys","[Man&constructor]+++++++++++++")
    }
}