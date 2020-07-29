package com.wys.kotlinlib.part03

/**
 * @author wangyasheng
 * @date 2020/7/29
 * @Describe:高阶函数：将函数用作参数或返回值的函数，称为高阶函数
 */


/***
 * 函数类型
 *
 * Kotlin使用类似(Int) -> String的一系列函数类型来处理函数的声明：
 * val onClick: () -> Unit = ...
 *
 * 这些类型具有与函数签名相对应的特殊表示法，即他们的参数和返回值：
 * - 所有函数类型都有一个圆括号括起来的参数类型列表以及一个返回类型
 *   (A,B) -> C 表示接受类型分别为A与B两个参数并返回一个C类型值
 *   的函数类型。参数类型列表可以为空，如() -> A，Unit返回类型不可省略。
 * - 函数类型可以有一个额外的接收者类型，它在表示法中的点之前指定：
 *   类型A.(B) -> C表示可以在A的接受者对象上以B类型参数来调用并
 *   返回一个C类型值的函数。带有接收者的函数字面值通常与这些类型一起使用。
 * - 挂起函数属于特殊种类的函数类型，它的表示法中有一个suspend修饰符，
 *   例如suspend() -> Unit或者suspend A.(B) -> C
 *
 * 函数类型表示法可以选择性的包含函数的参数名：(x: Int, y:Int) ->Point.
 * 这些名称可用于表明参数的含义。
 * 如需将函数类型指定为可空，请使用圆括号((Int, Int) -> Int)?
 * 函数类型可以使用圆括号进行接合：(Int) -> ((Int) -> Unit)
 *
 * 还可以通过使用类型别名给函数类型起一个别称：
 * typealias ClickHandler = (Button,ClickEvent) -> Unit
 */

/**
 * 函数类型实例化
 *
 * 有几种方法可以获得函数类型的实例：
 *
 * - 使用函数字面值的代码块，采用一下形式之一：
 *    - lambda表达式：{a,b -> a+b}
 *    - 匿名函数：fun (s: String): Int{ return s.toIntOrNull()?: 0}
 *   带有接收者的函数字面值可用作带有接收者的韩式类型的值。
 *
 * - 使用已有声明的可调用引用：
 *   - 顶层、局部、成员、扩展函数： ::isOdd、String::toInt
 *   - 顶层、成员、扩展属性：List<Int>::size
 *   - 构造函数： ::Regex
 *   这包括指向特定实例成员的绑定的可调用引用：foo::toString
 *
 * - 使用实现函数类型接口的自定义类的实例：
 *   class IntTransformer:(Int) ->Int{
 *   override fun invoke(p1: Int): Int {
 *     TODO("Not yet implemented")
 *     }
 *   }
 */

/**
 * 带与不带接收者的函数类型非字面值可以互换，其中接收者可以替代第一个参数，
 * 反之亦然。例如 (A,B) -> C类型的值可以传给或赋值给
 * 期待A.(B) -> C的地方，反之亦然:
 */

fun test001(){
    val repeatFun: String.(Int) -> String = {times ->this.repeat(times)}
    val twoParameters:(String, Int) -> String = repeatFun
    //如果有足够信息，编译器可以推断变量的函数类型
    val a = {i: Int -> i+1}

    fun runTransformation(f: (String, Int) -> String): String{
        return f("hello",2)
    }

    val result = runTransformation(twoParameters)
    println("result = $result")
}
/**
 * 函数类型实例调用
 *
 * 函数类型的值可以通过invoke(...)操作符调用：f.invoke(x)或者直接f(x)
 * 如果该值具有接收者类型，那么应该将接收者对象作为第一个参数传递。
 * 调用带有接收者的函数类型值的另一个方式是在其前面加上接收者对象，
 * 就好比该值是一个扩展函数：1.foo(2)
 */
fun test002(){
    val stringPlus:(String ,String) -> String = String::plus
    val intPlus: Int.(Int) -> Int = Int::plus

    println(stringPlus.invoke("<-","->"))
    println(stringPlus("Hello,","world!"))
    println(intPlus.invoke(1,1))
    println(intPlus(1,2))
    //类扩展调用
    println(2.intPlus(3))
}
fun highOrderFunctionTest(){
    val items = listOf<Int>(1,2,3,4,5)

    val i = items.fold(0,{
        acc: Int, i: Int ->
        print("acc = $acc, i = $i ")
        val result = acc + i
        println("result = $result")
        result
    })

    val joinedToString = items.fold("Element:",
            {acc, i ->  acc+" "+i})

    val product = items.fold(1,Int::times)

    println("joinedToString = $joinedToString, i = $i")
    println("product = $product")

    test001()
    test002()
}