package com.wys.kotlinlib.part03

/**
 * @author wangyasheng
 * @date 2020/7/27
 * @Describe:
 */

/**
 * 使用fun关键字声明；
 * 使用点表示法；
 * 参数使用Pascal表示法，即name：type，多个参数用逗号隔开，每个参数必须有显式类型；
 * 函数参数可以有默认值，当省略相应的参数时，使用默认值
 */
fun double(x: Int = 0): Int{
    return 2*x
}


/**覆盖方法总是使用与基类类型方法相同的默认参数值。
 * 当覆盖一个带有默认参数的方法时，必须省略默认参数值。
 *
 * 如果一个默认参数在一个无默认值参数之前，那么该默认参数
 * 只能通过具名参数调用该函数来使用:A().foo(j = 2)。
 *
 * 如果在默认参数之后的最后一个参数是lambda表达式，那么它
 * 既可以作为具名参数在括号内传入，也可以在括号外传入。
 *
 * 当一个函数调用混用位置参数与具名参数时，所有位置参数都要
 * 放在第一个具名之前，例如：允许调用f(1,y = 2)但不允许f(x = 1,2)
 *
 * 可以通过使用星号操作符将可变数量参数以具名形式传入：
 * fun foo(vararg strings: String){....}
 * foo(strings = *arrayOf("a","b","c"))
 * 对于JVM平台，在调用Java函数时不能使用具名参数语法，
 * 因为Java字节码并不总是保留函数刹那火速的名称。
 * */
open class A{
    open fun foo(i: Int = 10, j: Int){}

    fun foo1(bar: Int = 0, baz: Int = 1,qux: () -> Unit){}

    fun foo(vararg strings: String){
    }
}
class B: A(){
    //不能有默认参数
    override fun foo(i: Int, j: Int) {
        super.foo(i,j)
    }
}

/**
 * 返回Unit的函数
 *
 * 如果一个函数不反回任何有用的值，它的返回类型是Unit。
 * Unit是一种只有一个值--Unit的类型，这个值不需要显式返回。
 */

fun printHello(name: String?): Unit{
    if (name != null){
        println("Hello $name")
    }else{
        println("Hi there")
    }

    //return Unit或者return是可选的
    //Unit返回声明也是可选的
}

/**
 * 单表达式函数
 *
 * 当函数返回单个表达式时，可以省略花括号并且在=符号之后指定代码体即可
 * 当返回值类型可以由编译器推断时，显式声明返回类型时可选的
 */
fun double1(x: Int) = x*2

/**
 * 显式返回类型
 *
 * 具有块代码体的函数必须始终显式指定返回类型，除非它们返回Unit（这种情况下是可选的）。
 * kotlin不推断具有块代码体的函数的返回类型，因为这样的函数在代码体中可能由复杂的控制流，
 * 并且返回类型对于读者是不明显的。
 */

/**
 * 可变数量的参数（Varargs）
 *
 * 函数的参数（通常是最后一个）可以用vararg修饰符标记，
 * 允许将可变数量的参数传递给函数。
 *
 * 在函数内部，类型T的vararg参数的可见方式是作为T数组，
 * 即例子中ts变量具有类型Array<out T>。
 * 只有一个刹那火速可以标注为vararg。如果vararg 参数不是列表
 * 中的最后一个参数，可以使用具名参数语法传递其后的参数的值，
 * 或者，如果参数具有函数类型，则通过一个括号外部传一个lambda。
 *
 * 当我们调用vararg函数时，我们可以一个接一个的传参，例如asList(1,2,3)
 * 或者，如果我们已经有一个数组并希望将其内容传给该函数，可以使用伸展操作符
 * 在数组前面加*
 * val a = arrayOf(1,2,3)
 * val list = asList(-1,0,*a,4)
 */
fun<T> asList(vararg ts: T): List<T>{
    val result = ArrayList<T>()
    for (t in ts){
        result.add(t)
    }
    return result
}

/**
 * 中缀表示法
 * 标有infix关键字的函数也可以使用中缀表示法（忽略该调用的点和圆括号）调用。
 * 中缀函数必须满足一下要求：
 * - 它们必须是成员韩式或扩展函数；
 * - 它们必须只有一个参数；
 * - 其参数不得接受可变数量的参数且不能有默认值
 *
 * 中缀函数调用的优先级低于算术操作符、类型转换以及rangeTo操作符。
 * 一下表达式是等价的：
 * - 1shl 2+3 等价与 1 shl (2+3)
 * - 0 until n*2 等价于 0 until(n*2)
 * - xs union ys as Set<*> 等价于 xs union （ys as Set<*>）
 *
 * 另一方面，中缀函数调用的优先级高于布尔操作符&&、||、 is- 与 in-检测
 * 以及其他一些操作符。这些表达式也是等价的：
 * - a && b xor c 等价于 a && (b xor c)
 * - a xor b in c 等价于 (a xor b) in c
 *
 * 注意：中缀函数总是要求指定接收者与参数。当使用中缀表示法在当前接受者
 * 上调用方法时，需要显式使用this；不能像常规方法调用那样省略。
 * 这是确保非模糊解析所必须的。
 */
infix fun Int.shl(x:Int): Int{
    return x
}
class MyStringCollection{
    infix fun add(s: String){}

    fun build(){
        this add "abd"
        add("abc")
//        add "abc"  //错误：必须指定接收者
    }
}

/**
 * 函数作用域
 *
 * 在Kotlin中函数可以在文件顶层声明，这意味着不需要像Java那样需要创建
 * 一个类来保存一个函数。此外除了顶层函数，Kotlin函数也可以声明在局部
 * 作用域、作为成员函数以及扩展函数
 *
 * 局部函数
 * 局部函数，即一个函数在另一个函数内部
 * 局部函数可以访问外部函数的局部变量
 *
 * 成员函数
 * 成员函数是在类或对象内部定义的函数
 *
 * 泛型函数
 * 函数可以有泛型参数，通过在函数名前使用尖括号指定，例子asList函数
 *
 * 内联函数
 *
 * 扩展函数
 *
 * 高阶函数函数和Lambda表达式
 *
 * 尾递归函数
 * Kotlin支持一种称为尾递归的函数式编程风格。
 * 这允许一些通常用循环写的算法该用递归函数来写，而
 * 无堆栈溢出的风险。
 * 当一个韩式用tailrec修饰符标记并满足所需的形式时，
 * 编译器会优化该递归，留下一个快速而高效的基于循环的版本
 */

fun dfs(){
    var visited = true
    fun dfs(name: String?){
        if (visited) {
            printHello(name)
        }
    }
    dfs("kotlin")
}

//尾递归函数
fun findFixPoint(x: Double = 1.0): Double
    = if (Math.abs(x - Math.cos(x)) < 1E-10)x else findFixPoint(Math.cos(x))


fun test(){
    val result = double(2)
    val result2 = double()

    //具名参数
    A().foo(j = 2)

    val a = A()
    //使用默认值 baz = 1
    a.foo1( 1){ println("hello")}
    //使用两个默认值bar = 0与baz = 1
    a.foo1(qux = { println("hello")})
    //使用两个默认值bar = 0与baz = 1
    a.foo1 { println("hello") }

    a.foo(strings = *arrayOf("a","b","c"))
    a.foo("a","b","c")

    val list = asList(1,2,3)

    dfs()

    //中缀表示法调用该函数
    //等同于1.shl(2)
    val i = 1 shl 2
    MyStringCollection() add  "abc"
}