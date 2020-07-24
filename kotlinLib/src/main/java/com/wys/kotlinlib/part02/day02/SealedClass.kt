package com.wys.kotlinlib.part02.day02

/**
 * @author wangyasheng
 * @date 2020/7/16
 * @Describe:密封类
 */

/**
 * 密封类
 * 密封类用来表示受限的类继承结构：当一个值为有限几种的类型、而不能有任何其他
 * 类型时。
 * 在某种意义上，它们时枚举类的扩展：枚举类型的值集合也是受限的，但是每个枚举
 * 常量只存在一个实例，而密封类的一个子类可以有可包含状态的多个实例。
 *
 * 要声明一个密封类，需要在类名前面添加sealed修饰符。虽然密封类也可以有子类，
 * 但是所有子类都必须在与密封类自身相同的文件中声明(在1.1之前，子类必须嵌套在密封类声明的内部)。
 *
 * 一个密封类是自身抽象的，它不能直接实例化并可以有抽象成员。
 * 密封类不允许有非private构造函数（其构造函数默认为private）。
 * 请注意，扩展密封类子类的类（间接继承者）可以放在任何位置，而无需在同一个文件中。
 *
 * 使用密封类的关键好处在于使用when表达式的时候，如果能够验证语句覆盖了所有情况，
 * 就不需要为该语句再添加一个else子句了。当然，只有当用when作为表达式而不是作为语句时才有用。
 * */

sealed class Expr

data class Const(val number: Double): Expr()
data class Sum(val e1: Expr,val e2: Expr): Expr()
object NotANumber: Expr()

fun eval(expr: Expr):Double = when(expr){
    is Const -> expr.number
    is Sum -> eval(expr.e1)+ eval(expr.e2)
    NotANumber -> Double.NaN
    //不在需要else子句，因为我们已经覆盖了所有的情况
}