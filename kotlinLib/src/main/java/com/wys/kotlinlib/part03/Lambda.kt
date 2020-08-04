package com.wys.kotlinlib.part03

/**
 * @author wangyasheng
 * @date 2020/7/29
 * @Describe:
 */

/**
 * Lambda表达式与匿名函数
 *
 * lambda表达式与匿名函数是"函数字面值"，即未声明的函数，
 * 但立即作为表达式传递。
 *
 * Lambda表达式语法
 * Lambda表达式的完整语法形式如下：
 * val sum: (Int, Int) -> Int = {x: Int,y: Int -> x + y}
 * lambda表达式总是括在花括号中，完整语法形式的参数声明放在花括号内，
 * 并有可选的类型标注，函数体跟在一个 ->符号之后。如果推断出的该lambda
 * 的返回值不是Unit，那么该lambda主体中的最后一个表达式会视为返回值。
 *
 * 传递末尾的lambda表达式
 * 在Kotlin中又一个约定：如果函数的最后一个参数是函数，那么作为相应
 * 参数传入的lambda表达式可以放在圆括号之外:
 * val product = items.fold(1){acc,e -> acc * e }
 * 这种语法也称为拖尾lambda表达式。如果该lambda表达式是调用时的
 * 唯一参数，那么圆括号可以完全省略：
 * run{println("...")}
 *
 * it: 单个参数的隐式名称
 *
 * 一个lambda表达式只有一个参数是很常见的，如果编译器自己可以识别出签名，
 * 也可以不用声明唯一的参数并忽略->。该参数会隐式声明为it：
 * ints.filter{ it > 0 }
 *
 *
 * 从lambda表达式中返回一个值
 *
 * 我们可以使用限定的返回语法从lambda显式返回一个值，
 * 否则，将隐式返回最后一个表达式的值。这一约定连同
 * 在圆括号外传递lambda表达式一起支持LINQ-风格的代码：
 * strings.filter{ it.length == 5 }.sortedBy{ it }.map{ it.toUpperCase() }
 *
 * 下划线用于未使用的变量（自1.1起）
 * 如果lambda表达式的参数未使用，那么可以用下划线取代起名称：
 * map.forEach{_, value -> println($value)}
 *
 * 匿名函数
 * 上面提供的lambda表达式语法缺少一个东西是指定函数的返回类型
 * 的能力。在大多数情况下，这是不必要的。因为返回类型可以自动推断
 * 出来。然而，如果确实需要显式指定，可以使用另一种语法：匿名函数。
 * 匿名函数看起来非常像一个常规函数声明，除了其名称省略了。其函数体
 * 可以是表达式或代码块。
 * 匿名函数的返回类型推断机制与正常函数一样：对于具有表达式函数题的匿名
 * 函数将自动推断返回类型，而具有代码块函数体的返回类型必须显式指定（或为Unit）。
 * 匿名函数参数总是在括号内传递。允许将函数留在圆括号外的简写语法仅适用于lambda表达式。
 *
 * Lambda表达式与匿名函数之间的另一个区别是非局部返回的行为。一个不带标签
 * 的return语句总是在用fun关键字声明的函数中返回。这意味着lambda表达式中
 * 的return将从包含它的函数返回，而匿名函数中的return将从匿名函数自身返回。
 *
 *
 * 闭包
 * Lambda表达式或者匿名函数（以及局部函数和对象表达式）可以访问其闭包，
 * 即在外部作用域中声明的变量。
 * var sum = 0
 * ints.filter{ it > 0 }.forEach{
 *   sum += it
 * }
 * println(sum)
 *
 *
 * 带有接收者的函数字面值
 * 带有接收者的函数类型，例如A.(B) -> C，可以用椰树形式的函数字面值实例化
 * --带有接收者的函数字面值。
 * Kotlin提供了调用带有接收者的函数类型实例的能力。
 * 在这样的函数字面值内部，传给调用的接收者对象成为隐式的this，以便
 * 访问接收者对象的成员而无需任何额外的限定符，亦可使用this表达式访问接收者对象。
 * 这里有一个带有接收者的函数字面值及其类型的示例，其中在接收者对象上调用了plus：
 * val sum: Int.(Int) -> Int = { other -> plus}
 * */


fun lambdaTest(){
    val sum = {x: Int, y: Int -> x+y}
    //只读类型，无add和remove等放法
    val ints = listOf<Int>(-2,-1,1,2,3,4)


    //这个字面值是"（it: Int）-> Boolean"
    ints.filter { it > 0 }



    //一下两个写法是等价的
    ints.filter {
        val shouldFilter = it > 0

        shouldFilter
    }
    ints.filter {
        val shouldFilter = it > 0
        return@filter shouldFilter
    }

    //LINQ-风格的代码
    val strings = listOf<String>("aaa","bbb","ccc")
    strings.filter { it.length == 5 }.sortedBy { it }.map { it.toUpperCase() }

    val strings1 = mutableListOf<String>("a","b")
    strings1.filter(fun (s: String): Boolean{
        return s != "" && s != null
    })


    //匿名函数语法,一下两种写法等价
    ints.filter(fun(item) = item > 0)
    ints.filter(fun (item): Boolean {
        return item > 0
    })

    //lambda访问闭包，即外部作用域中声明的变量
    var i = 0
    ints.filter { it > 0 }.forEach{
        i += it
    }
    println("i = $i")

    val sum1: Int.(Int) -> Int = { other -> plus(other)}
    val sum2 = fun Int.(other: Int): Int = this + other

    println(1.sum1(3))
    println(1.sum2(4))
}