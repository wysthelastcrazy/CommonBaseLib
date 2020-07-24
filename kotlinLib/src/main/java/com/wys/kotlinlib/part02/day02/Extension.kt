package com.wys.kotlinlib.part02.day02

/**
 * @author wangyasheng
 * @date 2020/7/16
 * @Describe:扩展等（函数、属性）
 */

/**
 * 扩展函数
 * 声明一个扩展函数，需要用一个接受者类型，也就是被扩展的类型来作为函数前缀.
 *
 * 扩展是静态解析的
 * 扩展不能真正的修改它们所扩展的类，通过定义一个扩展，并没有在类中插入新成员，
 * 仅仅是通过该类型的变量用点表达式去调用这个新函数。扩展函数是静态分发的，即
 * 它们不是根据接受者类型的虚方法，调用的扩展函数是由函数调用所在的表达式的类型
 * 来决定的，而不是由表达式运行时求值结果决定的。
 *
 * 如果一个类定义有一个成员函数与一个扩展函数，而这两个函数又有相同的接受者类型、
 * 相同的名字，并且都适用给定的参数，这种情况总是取成员函数。
 * 扩展函数重载同样名字但是不同参数成员函数也完全可以。
 *
 * 可空接受者
 * 可以为可空的接受者类型定义扩展。这样的扩展可以在对象变量上调用，即使其值为null
 * 并且可以在函数体内检测this == null，这能让你在没有检测null的时候
 * 调用Kotlin中的toString()，因为检测发生在扩展函数的内部。
 */
fun Any?.toString():String{
    if (this == null) return "null"
    return toString()
}
fun <T>MutableList<T>.swap(index1:Int,index2:Int){
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}
class Help {
    fun test(){
        val list = mutableListOf(1,2,3)
        list.swap(0,2)
        print(list)
    }
}
open class Shape

class Rectangle:Shape(){
    fun printFunctionType(){
        println("Class method")
    }
}

fun Shape.getName() = "Shape"
fun Rectangle.getName() = "Rectangle"

fun printClassName(s:Shape){
    println(s.getName())
}
fun Rectangle.printFunctionType(){
    println("Extension method")
}
fun Rectangle.printFunctionType(i:Int){
    println("Extension method $i")
}

/**
 *
 * 扩展属性
 * 与函数类似，Kotlin也支持扩展属性
 *
 * 由于扩展没有实际的将成员插入类中，因此对扩展属性来说幕后字段是无效的。
 * 这就是为什么扩展属性不能有初始化器。它们的行为只能由显示提供的getters/setters 定义
 * val Rectangle.number = 1 //错误：扩展属性不能有初始化器
 *
 * */
 val <T> List<T>.lastIndex:Int
        get() = size - 1

/**
 * 伴生对象的扩展
 * 如果一个类定义有一个伴生对象，也可以为伴生对象定义扩展函数与属性。
 * 就像伴生对象的常规成员一样，可以只使用类名作为限定符来调用伴生对象的扩展成员
 *
 * */

class MyClass{
    companion object{}
}
fun MyClass.Companion.printCompanion(){
    println("companion")
}

/**
 * 扩展的作用域
 * 大多数时候在顶层定义扩展，在包里直接使用：
 * package org.example.declarations
 * fun List<String>.getLongestString() { /*……*/}
 * 要使用所定义包之外的一个扩展，需要在调用方导入：
 * package org.example.usage
 * import org.example.declarations.getLongestString
 * fun main() {
 * val list = listOf("red", "green", "blue")
 * list.getLongestString()
 * }
 * */

/**
 * 扩展声明为成员
 * 在一个类内部可以为另一个类声明扩展。这样在扩展内部，有多个隐式接收者
 * 其中的对象成员可以无需通过限定符访问。扩展神明所在的类的实例称为分发
 * 接收者，扩展方法调用所在的接收者类型的实例称为扩展接收者。
 * */

class Host(val hostName:String){
    fun printHostName(){
        print(hostName)
    }
    fun printPort(){
        print("8080")
    }
}
class Connection(val host:Host,val port:Int){
    fun printPort(){
        println(port)
    }

    fun Host.printConnectionString(){
        printHostName() //调用Host.printHostname()
        print(":")
        printPort()
        //调用Connection.printPort()
        //对于分发者与扩展接收者的成员名字冲突，扩展接收者优先。
        //要引用分发接收者的成员，可以使用限定的this语法
        this@Connection.printPort()
    }

    fun connect(){
        host.printConnectionString() //调用扩展函数
    }
}

/**
 * 声明为成员的扩展可以声明为open并在子类中覆盖。这意味着这些函数分发对于
 * 分发接收者类型是虚拟的，但是对于扩展接收者类型是静态的。
 */
open class Base()
class Derived: Base()
open class BaseCaller{
    open fun Base.printFunctionInfo(){
        println("Base extension function in BaseCaller")
    }
    open fun Derived.printFunctionInfo(){
        println("Derived extension function in BaseCaller")
    }
    fun call(b:Base){
        //调用扩展函数
        b.printFunctionInfo()
    }
}
class DerivedCaller: BaseCaller(){
    override fun Base.printFunctionInfo() {
        println("Base extension function in DerivedCaller")
    }

    override fun Derived.printFunctionInfo() {
        println("Derived extension function in DerivedCaller")
    }
    fun call(d:Derived){
        d.printFunctionInfo()
    }
}
/**
 * 关于可见性的说明
 * 扩展的可见性与相同作用域声明的其他实体的可见性相同：
 * - 在文件顶层声明的扩展可以访问同一文件中的其他private顶层声明；
 * - 如果扩展是在其接收者类型外部声明的，那么该扩展不能访问接收者的private成员
 */


