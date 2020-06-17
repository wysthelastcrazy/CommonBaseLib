package com.wys.commonbaselib.jetpack.kotlin

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * @author wangyasheng
 * @date 2020-06-09
 * @Describe:
 */
class LoginModule : LifecycleObserver{
    val TAG = "LoginModule"
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        Log.d(TAG,"[LoginModule&onResume]++++++++++++")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(){
        Log.d(TAG,"[LoginModule&onPause]++++++++++++")
    }

}