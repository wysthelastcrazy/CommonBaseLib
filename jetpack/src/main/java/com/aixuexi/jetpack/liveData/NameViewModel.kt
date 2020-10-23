package com.aixuexi.jetpack.liveData

import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * @author wangyasheng
 * @date 2020/8/5
 * @Describe:
 */
class NameViewModel : ViewModel() {
    //Create a LiveDate with a String
    private val currentName : MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            loadData()
        }
    }

    /**
     * map只是对LiveData里面的值进行转换，switchMap是直接对LiveData进行转化。
     */
//    fun getCurrentName():LiveData<String> = currentName.map {
//        it+"sss"
//    }
    fun getCurrentName():LiveData<String> = currentName.switchMap {
        liveData { emit(getSymbol(it)) }
    }
    private suspend fun getSymbol(id:String):String{
        delay(1000L)
        if (id == "1111"){
            return "AAPL"
        }
        return "Google"
    }
    private fun loadData(){
        /**
         * ViewModel的扩展属性，会在ViewModel的onCleared()方法中调用协程
         * 的cancel方法。也就是在Activity或Fragment的onDestroy中调用
         */
        viewModelScope.launch() {
            var i = 0
            while (isActive){
                delay(2000L)
                currentName.value = "AAPL$i"
                i++
            }
        }
    }
}