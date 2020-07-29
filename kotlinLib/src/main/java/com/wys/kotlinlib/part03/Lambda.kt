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
 * */


fun lambdaTest001(){
    val sum = {x: Int, y: Int -> x+y}
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

    //匿名函数语法
    ints.filter(fun (item) = item > 0)
}