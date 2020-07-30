package com.wys.kotlinlib.part03

/**
 * @author wangyasheng
 * @date 2020/7/30
 * @Describe:内联函数
 */

/**
 * 内联函数
 *
 * 使用高阶函数会带来一些运行时的效率损失：每个函数都是一个对象，并且会捕获一个闭包。
 * 即那些在函数体内会访问到的变量。内存分配（对于函数对象和类）和虚拟调用会引入
 * 运行时间的开销。
 *
 * 但是在许多情况下通过内联化lambda表达式可以消除这类的开销。
 * 下述函数是这种情况的很好的例子：lock(l){ foo() }
 * 即lock()函数可以很容易的在调用处内联。
 */
fun hasZeros(ints: List<Int>): Boolean{
    ints.forEach{
        if (it == 0) return true
    }
    return false
}
fun inLineTest(){

}
