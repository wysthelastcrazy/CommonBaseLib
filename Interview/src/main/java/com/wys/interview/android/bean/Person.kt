package com.wys.interview.android.bean

import android.util.Log

/**
 * @author wangyasheng
 * @date 2020/8/31
 * @Describe:
 */
open class Person constructor(name:String) {
    private val str = name
    init {
        Log.d("wys","[Person&init]+++++++++++++")
    }
    constructor(name: String,age:Int) : this(name) {
        Log.d("wys","[Person&constructor]+++++++++++++")
    }

}