package com.wys.kotlinlib.part02.day02

/**
 * @author wangyasheng
 * @date 2020/7/16
 * @Describe:泛型
 */
/**
 * 泛型
 * 与Java类似，Kotlin中的类也可以有类型参数。一般来说，要创建这样类的实例，
 * 我们需要提供类型参数。但是如果类型参数可以推断出来，例如从构造函数的参数或者
 * 从其他途径，允许省略类型参数。
 * */
class Box<T>(t:T){
    var value = t
}

fun generalityTest(){
//    val box: Box<Int> = Box<Int>(1)
    val box = Box(1)
}