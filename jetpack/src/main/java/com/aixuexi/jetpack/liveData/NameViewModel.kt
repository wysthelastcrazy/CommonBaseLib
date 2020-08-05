package com.aixuexi.jetpack.liveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author wangyasheng
 * @date 2020/8/5
 * @Describe:
 */
class NameViewModel : ViewModel() {
    //Create a LiveDate with a String
    val currentName : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

}