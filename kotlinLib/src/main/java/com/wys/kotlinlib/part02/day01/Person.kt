package com.wys.kotlinlib.part02.day01

/**
 * @author wangyasheng
 * @date 2020/7/2
 * @Describe:类与继承
 */
open class Person constructor(open var name: String){
    /**
     * Kotlin中使用关键字class声明类
     * 类声明由类名、类头（指定其类型参数、主构造函数等）以及类体构成
     * 如果一个类没有类体，可以省略花括号
     *
     * 构造函数
     *
     * 在Kotlin中的一个类可以由一个主构造函数以及一个或多个次构造函数。
     * 主构造函数是类头的一部分：它跟在类名后;
     * 如果主构造函数没有任何注解或者可见性修饰符，可以省略constructor关键字。
     * class Person(name: String)
     * 主构造函数不能包含任何代码。初始化的代码可以放到init关键字作为前缀的初始化块（initializer blocks）中。
     * 在实例化期间，初始化块按照它们出现在类中的顺序执行，与属性初始化器交织在一起
     *
     * 主构造函数的参数可以在初始化块中使用，它们也可以在类体内声明的属性初始化器中使用
     * 事实上，声明属性以及从主构造函数初始化属性，由更简洁的语法:
     * class Person constructor(val name: String)
     * 与普通属性一样，主构造函数中声明的属性可以是var或val。
     * 如果构造函数有注解或可见行修饰符，这个constructor关键字是必需的，并且这些修饰符在它前边。
     */
//    init {
//        println("First initializer blocks that prints $name")
//    }
//    init {
//        println("Second initializer blocks that prints ${name.length}")
//    }
    /**
     * 次构造函数
     * 如果类有一个主构造函数，每个次构造函数需要委托给主构造函数，
     * 可以直接委托或者通过其它次构造函数间接委托。
     * 委托到同一个类的另一个构造函数用this关键字。
     *
     * 初始化块中的代码实际上会成为主构造函数的一部分。委托给主构造函数会作为
     * 次构造函数的第一条语句，因此所有初始化块与属性初始化器中的代码都会在次构造
     * 函数体之前执行。即使该类没有主构造函数，这种委托让会隐式发生
     * */
    constructor(name: String ,age: Int) : this(name){
        println("this constructor +++")
    }
    /**
     * 类成员：
     * - 构造函数与初始化块
     * - 函数
     * - 属性
     * - 嵌套类与内部类
     * - 对象声明
     */
    open fun run(){
        println("Person ${name} is running")
    }
    fun eat(){}
}