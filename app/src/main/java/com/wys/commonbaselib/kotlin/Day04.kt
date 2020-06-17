package com.wys.commonbaselib.kotlin

/**
 * @author wangyasheng
 * @date 2020/6/11
 * @Describe:
 */
class Day04 {
    var name: String = "Paul"
    get() = "i am getter,name is Paul"
    set(value){
        //field为幕后字段，只能用于setter和getter中
        field = "name is $value"
    }
    abstract class MyAbstractClass{
        abstract fun bar()
    }

    /**
     * kotlin的接口可以既包含抽象方法的声明也包含实现。
     * 与抽象类不同的是，接口无法保存状态。它可以有属性
     * 但必须声明为抽象或提供访问器实现
     */
    interface MyInterface{
        //抽象的
        val prop: Int
        //提供访问器
        val properWithImplementation: String
            get() = "foo"
        fun bar()
        fun foo(){
            //可选的方法体
        }
    }
    class Child: MyInterface{
        override val prop: Int
            get() = 29

        override fun bar() {
            TODO("Not yet implemented")
        }

    }
}