package com.wys.kotlinlib

import com.wys.kotlinlib.part01.Constant
import com.wys.kotlinlib.part02.day01.Man
import com.wys.kotlinlib.part02.day02.*
import com.wys.kotlinlib.part03.highOrderFunctionTest
import com.wys.kotlinlib.part03.lambdaTest
import com.wys.kotlinlib.part04.*

fun main(){
    println("hello kotlin")
//    val person: Person = Person("wys",18)
    val man: Man = Man("wys", null)
    man.C().test()
    Constant.LandType.LAND_TYPE_BASE


//    extensionTest()
//    dataClassTest()
//    sealedClassTest()
//    highOrderFunctionTest()
//    lambdaTest()
//    collectionsTest()
//    iterableTest()
//    rangeTest()
//    sequenceTest()
    operateTest()
}

/**
 * 空安全
 */
fun nullTest(){
    /**
     * Kotlin中'？'和'！！'的区别：
     * '？'：表示当前是否对象可以为空；
     * '！！'：表示当前对象不为空的情况下执行。
     *
     * '？'加在变量名后，系统在任何情况下不会报空指针异常，
     * '！！'加载变量名后，如果对象为null，那么系统一定会报异常。
     *
     * 在java中：
     * ArrayList<String> list = null;
     * Log.d("TAG","-->> list Size = "+list.size());
     * 此时会报空指针异常
     *
     */
    val list:ArrayList<String>? = null

    /**
     * 不会报异常，打印'-->> list Size = null'
     * '？'的用法：
     * 1、在声明对象时，把它跟在类名后面，表示这个类允许为null；
     * 2、在调用对象时，把它更在对象后面，表示如果为null程序就会视而不见。
     * 所以加'？'是一种安全的写法，体现了Kotlin null safety的特性。
     */
    println("-->> list Size = ${list?.size}")

    /**
     * if(list?.size> 0){...}
     * 上边写法有问题，因为当list为null时，它的size返回就是"null"，
     * 但是"null"不可以和int值比较大小，所以编译器会建议我们改成if(list?.size!! > 0)。
     * 但是当list为null时会报空指针异常，显然不是想要的结果。
     *
     * 在kotlin中可以使用'?:'表达式来代替Java中的 条件表达式？表达式1：表达式2这个三元表达式
     * '?:'表示的意思是，当对象A值为null的时候，那么它就会返回后面的对象B
     */
//    if(list?.size!! > 0){
//        println("-->> list Size > 0")
//    }
    if(list?.size?:0 > 0){
        println("-->> list Size > 0")
    }

    /**
     * 报kotlin.KotlinNullPointerException异常
     */
//    println("-->> list Size = ${list!!.size}")
}

/**
 * 扩展函数
 */
fun extensionTest(){

    val list = mutableListOf(1,2,3,4)
    list.swap(0,2)
    println(list)

    printClassName(Rectangle())
    val rectangle = Rectangle()
    rectangle.printFunctionType()
    rectangle.printFunctionType(2)

    MyClass.printCompanion()

    Connection(Host("kotlin.in"),443).connect()
//    Host("kotlin.in").printConnectionString() //错误，该扩展函数在Connection外不可用


    //"Base extension function in BaseCaller"
    BaseCaller().call(Base())
    //"Base extension function in DerivedCaller"
    DerivedCaller().call(Base())
    //"Base extension function in DerivedCaller"
    DerivedCaller().call(Base())
    //"Derived extension function in DerivedCaller"
    DerivedCaller().call(Derived())
}

/**
 * 数据类
 */
fun dataClassTest(){
    dateClassTest01()
    dateClassTest02()
}

/**
 * 密封类
 */
fun sealedClassTest(){
    val sum = eval(Sum(Const(2.0),Const(3.0)))
    println("sum is $sum")
}
