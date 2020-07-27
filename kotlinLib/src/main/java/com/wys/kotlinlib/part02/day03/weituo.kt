package com.wys.kotlinlib.part02.day03

import org.omg.CORBA.portable.Delegate

/**
 * @author wangyasheng
 * @date 2020/7/27
 * @Describe:委托
 */

/**
 * 委托模式已经证明是实现继承的一个很好的替代方式，而Kotlin可以零样板代码的原生支持它。
 *
 * Derived 类可以通过将其所有共有成员都委托给指定对象来实现一个接口Base，
 * Derived 的超类型列表中的by子句表示b将会在Derived中内部存储，并且编译器将生成
 * 转发给b的所有Base的方法
 */
interface Base{
    fun print()
}

class BaseImpl(val x: Int):Base{
    override fun print() {
        print(x)
    }
}

class Derived(b: Base):Base by b



fun weituoTest(){
    val b = BaseImpl(10)
    Derived(b).print()
}