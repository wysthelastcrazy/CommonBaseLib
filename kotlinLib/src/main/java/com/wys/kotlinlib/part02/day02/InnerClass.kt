package com.wys.kotlinlib.part02.day02

/**
 * @author wangyasheng
 * @date 2020/7/20
 * @Describe:嵌套类与内部类
 */
class Outer{
    private val bar: Int =1
    /**嵌套类*/
    class Nested{
         fun foo() = 2
    }
    /**内部类*/
    inner class Inner{
        fun foo() = bar
    }
}

fun innerClassTest(){
    val demo = Outer.Nested().foo()
    val inDemo = Outer().Inner().foo()
}