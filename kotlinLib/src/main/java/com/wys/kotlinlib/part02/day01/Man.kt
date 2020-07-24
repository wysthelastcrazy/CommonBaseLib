package com.wys.kotlinlib.part02.day01

/**
 * @author wangyasheng
 * @date 2020/7/9
 * @Describe:
 */
class Man(override var name: String,
          /***
           * 继承
           * Kotlin中所有类都有一个共同的超类Any，这对于没有超类声明的类是默认超类。
           * Any有三个方法：equals()、hashCode()、与toString()。因此所有kotlin类都有这是那个方法。
           *
           * 默认情况下，Kotlin类是final的：它们不能被继承。要使一个类可继承，需要使用open关键字标记它。
           * open class Person //该类开放继承
           * 如需声明一个显式的超类型，请在类头中把超类型放到冒号之后：
           * open class Base(p:Int)
           * class Derived(p:Int):Base(p)
           *
           * 如果派生类有一个主构造函数，其基类可以（并且必须）用派生类主构造函数的参数就地初始化。
           * 如果派生类没有主构造函数，那么每个次构造函数必须使用super关键字初始化其基类型，或
           * 委托另一个构造函数做到这一点。在这种情况下，不同的次构造函数可以调用基类型的不同构造函数
           * class MyView : View {
           * constructor(ctx: Context) : super(ctx)
           * constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
           * }
           *
           *
           * 覆盖方法：
           * Kotlin对于可覆盖的成员（也称之为开放）以及覆盖后的成员需要显式修饰符：
           * open class Person{
           *      open fun run(){...}
           *      fun eat(){...}
           * }
           * class Man():Person(){
           * override fun run(){...}
           * }
           *
           * 如果函数没有标注open，那么子类中不允许定义相同签名的函数，不论加不加override。
           * 将open修饰符添加到final类（即没有open的类）的成员上不起作用。
           * 标记为override的成员本身是开放的，可以在子类中覆盖。如果想禁止再次覆盖，
           * 使用final关键字：
           * final override fun run(){...}
           *
           * 覆盖属性：
           * 覆盖属性与方法覆盖类似；在超类中声明然后在派生类中重新声明的属性必须以override开头，
           * 并且它们必须具有兼容的类型。每个声明的属性可以由具有初始化器的属性
           * 或者具有get方法的属性覆盖。
           * open class Person{
           *      open val age:Int = 28
           * }
           * class Man:Person(){
           *      override val age:Int = 18
           * }
           * 也可以用一个var属性覆盖一个val属性，但反之则不行；因为一个val属性本质声明
           * 了一个get方法，而将器覆盖为var只是在子类中额外声明一个set方法。
           *
           *
           * 派生类初始化顺序:
           * 第一步完成其基类的初始化（在之前只有对基类构造函数参数的求值），因此发生在派生类的初始化逻辑运行之前。
           * 这意味着，基类构造函数执行时，派生类中声明或覆盖的属性都还没初始化。如果在基类初始化逻辑中
           * （直接或通过另一个覆盖的open成员的实现间接）使用了任何一个这种属性，那么都可能导致不正确的
           * 行为或运行时故障。设计一个基类时，应该避免在构造函数、属性初始化器以及init块中使用open成员。
           *
           *
           * 调用超类实现：
           * 派生类中的代码可以使用super关键字调用其超类的函数和属性访问器的实现；
           * 在一个内部类中访问外部类的超类，可以通过由外部类名限定的super关键字来实现：super@Person.run();
           *
           *
           * 覆盖规则：
           * 在Kotlin中，实现继承由下述规则规定：
           *  如果一个类从它的直接超类继承相同成员的多个实现，它必须覆盖这个成员并提供自己的实现（也许用继承来的其中之一）；
           *  open class Rectangle {
           *      open fun draw() { /* …… */ }
           * }

           * interface Polygon {
           *      fun draw() { /* …… */ } // 接口成员默认就是“open”的
           * }

           * class Square() : Rectangle(), Polygon {
           * // 编译器要求覆盖 draw()：
           *      override fun draw() {
           *          super<Rectangle>.draw() // 调用 Rectangle.draw()
           *          super<Polygon>.draw() // 调用 Polygon.draw()
           *      }
           * }
           */
          val age: Int?): Person(name) {
    override fun run() {
//        super.run()
        println("$name is running")
    }

    inner class C{
        fun test(){
            super@Man.run()
            run()
            println("test")
        }
    }
}