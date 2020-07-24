package com.wys.kotlinlib.part02.day01

import com.wys.kotlinlib.part02.day01.Person

/**
 * @author wangyasheng
 * @date 2020/7/9
 * @Describe:
 */
abstract class Woman(name:String) : Person(name){
    /**
     * 抽象类
     * 类以及其中的某些成员可以声明为abstract。
     * 抽象成员在本类中可以不用实现，需要注意的是，我们并不需要用open标注一个抽象类或函数。
     * 可以用一个抽象成员覆盖一个非抽象的开发成员
     *
     * open class Person{
     *      open fun run{...}
     * }
     * abstract class Woman: Person(){
     *  abstract override fun run()
     * }
     */
    abstract override fun run()

    /**
     * 伴生对象：
     * 如果需要写一个可以无需用一个类的实例来调用、但需要访问类内部的函数，
     * 可以把它写成该类内对象声明中的一员。
     * 更具体的讲，如果在类内声明了一个伴生对象，就可以访问其成员，只是以类名作为限定符
     */
}