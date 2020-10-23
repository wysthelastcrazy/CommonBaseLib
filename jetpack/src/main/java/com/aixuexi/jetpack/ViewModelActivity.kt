package com.aixuexi.jetpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aixuexi.jetpack.bean.User
import com.aixuexi.jetpack.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.activity_viewmodel.*

class ViewModelActivity : AppCompatActivity() {
    private val TAG = "ViewModelActivity"
    private var name = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewmodel)

        val model: MyViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        model.getUsers().observe(this, Observer {
            users -> Log.d(TAG,"update UI $users")
        })
        model.getName().observe(this, Observer {

        })
        Handler().postDelayed(Runnable {
            model.updateUser()
        },1000)

        btnUpdate.setOnClickListener {
        }
        /**
         * let函数是只有一个lambda函数块block作为参数的函数，
         * 调用T类型对象的let函数，则该对象为函数的参数。
         * 在函数快中可以通过it指代该对象。返回值为函数块的最后
         * 一行或指定return表达式。
         *
         * 适用场景：
         * 1、最常用的场景就是适用let函数处理需要针对一个可null的对象统一做判空处理；
         * 2、然后就是需要去明确一个变量所处特定的作用域范围内可用。
         */
        val result = model.let {
            100
        }
        /**
         * with函数不是以扩展的形式存在的，它是将某个对象作为函数的参数，
         * 在函数块内可以通过this指代该对象。返回值为函数块的最后一行
         * 或指定return表达式。
         * 由于with函数最后一个参数是一个函数，可以把函数提到圆括号外部。
         *
         * 适用场景：适用于调用同一个类的多个方法时，可以省去类名重复，
         *          直接调用类的方法即可，经常用于Android中RecyclerView
         *          中onBinderViewHolder中，数据model的属性映射到UI上。
         */
        val str = "ddd"
        val result1 = with(str){
            this.length
        }

        /**
         * run函数只接收一个lambda函数为参数，以闭包形式返回，
         * 返回值为最后一行的值或者指定return的表达式。
         *
         * 适用场景：适用于let、with函数的任何场景。因为run函数是let、with两个
         * 函数的结合体，准确来说它弥补了let函数在函数体内必须适用it参数替代对象，
         * 在run函数中可以像with函数一样可以省略，直接访问实例的共有属性和方法，
         * 另一方面它弥补了with函数传入对象判空问题，在run函数中可以像let函数一样做
         * 判空处理。
         */
        val result2 = str.run {
            this.length
        }
        /**
         * apply函数
         * 从结构上来看apply函数和run函数很像，唯一不同点就是它们格子返回值不一样，
         * run函数是以闭包形式返回最后一行代码的值，而apply函数返回的是传入的对象本身。
         *
         * 适用场景：整体作用功能和run函数很像，唯一不同点就是它返回的值是对象本身，
         * 正是基于这一点差异它的适用场景稍微与run函数有点不同。apply一般用于一个对象
         * 实例初始化的时候，需要对对象的属性进行赋值。或者动态inflate出一个XML的View的
         * 时候需要给View绑定数据也会用到，这种情景非常常见，特别是在我们开发中会有
         * 一些数据model向View model转化实例化的过程中需要到。
         */
        str.apply {  }
        /**
         * also函数
         * also函数的结构实际上和let很像，唯一区别就是返回值不同，also返回传入的对象本身。
         *
         * 适用场景：适用于let函数的任何场景，因为返回值为传入的对象本身，一般可
         * 用于多个扩展函数链式调用。
         */
        str.also {

        }

        /**
         * 总结let、with、run、apply、also函数的区别
         *
         * 函数名          定义inline的结构         函数体内使用的对象       返回值               适用场景
         *
         * let        fun<T,R> T.let(block:(T)    it指代当前对象       闭包形式返回
         *              -> R):R = block(this)
         *
         * with       fun<T,R>with(receiver:T,   this指代当前对象      闭包形式返回
         *              block:T.()->R):R =
         *              receiver.block()
         *
         * run        fun<T,R> T.run(block:T.()  this指代当前对象      闭包形式返回
         *              -> R):R = block()
         *
         * apply      fun T.apply(block:T.()->   this指代当前对象      返回当前对象
         *              Unit):T{ block()
         *              return this}
         *
         * also       fun T.also(block:(T) ->    it指代当前对象        返回当前对象
         *              Unit):T{ block(this)
         *              return this }
         */
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}