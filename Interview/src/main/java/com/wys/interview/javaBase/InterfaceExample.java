package com.wys.interview.javaBase;

import android.util.Log;

/**
 * @author wangyasheng
 * @date 2020/8/6
 * @Describe:
 */
interface InterfaceExample {
    void func1();
    default void func2(){
        Log.d("InterfaceExample","func2");
    }
}
