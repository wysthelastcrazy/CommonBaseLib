package com.wys.commonbaselib.kotlin

import android.content.Context
import android.util.AttributeSet
import android.view.View
import java.util.jar.Attributes
import kotlin.properties.Delegates

/**
 * @author wangyasheng
 * @date 2020-06-08
 * @Describe:类与对象
 */
class Day03 {

    fun day03Enter(){
        part01()
        val man = Man("wys",28)
        val Person = Person("dnd",18)
    }

    fun part01(){
        //Kotlin中使用关键字class声明类
        //类声明由类名、类头（指定其类型参数、主构造函数等）
        //以及由花括号包围的类体构成；
        //类头和类体都是可选的，如果一个类没有类体，可以省略花括号

        //构造函数
        //在kotlin中的一个类可以由一个主构造函数以及一个或多个次构造函数。
        //主构造函数是类头的一部分：它跟在类名（与可选的类型参数）后

        //类成员
        // - 构造函数与初始化块
        // - 函数
        // - 属性
        // - 嵌套类与内部类
        // - 对象声明

    }
    fun part02(){
        //继承
        //在kotlin中所有类都有一个共同的超类Any，对于没有没有超类声明的类是默认超类
        //Any有三个方法：equals(),hashCode()与toString()
        //默认情况下，kotlin类是final的：它们不能被继承，要是类可以继承，请使用open关键字标记
        //例：open  class Base(p: Int)
        //class Derived(p: Int):Base(p)

        //如果派生类有一个主构造函数，其基类可以（并且必须）用派生类的主构造函数的参数就地初始化
        //如果派生类没有主构造函数，那么每个次构造函数必须使用super关键字初始化其基类型，
        //或者委托个另一个构造函数做到这一点，此时，不同次构造函数可以调用基类型的不同的构造函数

//        class MyView : View {
//            constructor(ctx: Context):super(ctx)
//            constructor(ctx: Context,attrs : AttributeSet):super(ctx,attrs)
//        }

        //覆盖方法
        //Kotlin对于可覆盖的成员（称之为开放）以及覆盖后的成员需要显式修饰符
        //没有标记open修饰符添加到final类的成员上不起作用
        //标记为override的成员本身是开放的
//        open class Shape{
//            open fun draw(){}
//            fun fill(){}
//        }
//        class Circle: Shape() {
//            override fun draw() {
//                super.draw()
//            }
//        }

        //覆盖属性
        //属性覆盖与方法覆盖类似：在超类中声明然后在派生类中重新声明的属性必须以override开头，
        //并且它们必须具有兼容的类型。
        //每个声明的属性可以由具有初始化器的的属性或者具有get方法的属性覆盖

        //也可以用一个var属性覆盖一个val属性，但反之则不行。
        //因为一个val属性本质上声明了一个get方法，而将其覆盖为var只是在子类中额外声明一个set方法
//        open class Shape{
//            open val vertexCount: Int = 0
//        }
//        class Rectangle: Shape(){
//            override val vertexCount = 4
//        }

        //可以在主构造函数中使用override关键字作为属性声明的一部分
//        class Rectangle(override val vertexCount: Int = 4 ):Shape
//        class Polygon : Shape{
//            override var vertexCount: Int = 0
        }
    }
    interface Shape{
        val vertexCount: Int
    }
    /**
     *  如果主构造函数没有任何注解或者可见性修饰符，可以省略constructor关键字
     *  主构造函数不能包含任何的代码。初始化的代码可以放到以init关键字最为前缀的初始化块中
     */
    class Man (name: String,age: Int){
        init {
            println("First initializer block that prints ${name}")
        }
        init {
            println("Second initializer block that prints ${name.length}")
        }
        //主构成的参数可以在初始化块中使用。它们也可以在类体内声明的属性初始化器中使用：
        val customerKey = name.toUpperCase()
    }
    /**
     * 事实上，声明属性以及从主构造函数初始化属性，kotlin由简洁的语法
     */
    class Person constructor(val name: String,var age: Int){
        //与普通属性一样，主构造函数中声明的属性可以是可变的（var）或只读的（val）
        //如果构造函数由注解或可见性修饰符，这个constructor关键字是必须的，并且这些修饰符在它前边

        //次构造函数
        //类也可以声明前缀由constructor的次构造函数
        var children: MutableList<Person> = mutableListOf()
        lateinit var p: Person
        var a: Int = 0

        //如果一个类有主构造函数，每个次构造函数都需要委托给主构造函数，
        //可以直接委托，也可以通过别的次构造函数简介委托。委托到同一个类的另一个构造函数用this关键字即可
        constructor(name: String, age: Int, parent: Person) : this(name,age) {
            parent.children.add(this)
            p = parent
            println("constructor")
        }
        //注意：初始化代码块中的代码，实际上会称为主构造函数的一部分。
        //委托给主构造函数或作为次构造函数的第一条语句，因此所有初始化块
        //与属性初始化器中的代码都会在次构造函数体之前执行，即使该类没有主构造函数，
        //这种委托仍会隐式发生，并且仍会执行初始化块
        init {
            println("init block")
        }
        fun ss(){
            age = 18
            println("ss $name")
        }
    }
