package com.wys.kotlinlib.part02

/**
 * @author wangyasheng
 * @date 2020/7/9
 * @Describe:类与对象笔记
 */
class Note {
    /**=================类与继承====================**/
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

    /**
     * 类成员：
     * - 构造函数与初始化块
     * - 函数
     * - 属性
     * - 嵌套类与内部类
     * - 对象声明
     */

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

    /**
     * 抽象类
     * 类以及其中的某些成员可以声明为abstract。
     * 抽象成员在本类中可以不用实现，需要注意的是，我们并不需要用open标注一个抽象类或函数。
     * 可以用一个抽象成员覆盖一个非抽象的开发成员
     *
     * open class Person{
     *      open fun run{...}
     * }
     * abstract class Woman: Person(){
     *  abstract override fun run()
     * }
     */

    /**
     * 伴生对象：
     * 如果需要写一个可以无需用一个类的实例来调用、但需要访问类内部的函数，
     * 可以把它写成该类内对象声明中的一员。
     * 更具体的讲，如果在类内声明了一个伴生对象，就可以访问其成员，只是以类名作为限定符
     */

    /**=================类与继承End====================**/

    /**=================属性与字段====================**/
    /**
     * 声明属性：
     * Kotlin中的属性既可以用关键字var声明为可变的，也可以用关键字val声明为只读的；
     * 要是用一个属性，只要用名称引用它即可：
     * class Address{
     *      var name:String = "wys"
     * }
     * val result = Address()
     * var name = result.name
     */
    /**
     * Getters与Setters
     * 声明一个属性的完整语法是：
     * var<propertyName>[:<PropertyType>][= <property_initializer>]
     *    [<getter>]
     *    [<setter>]
     * 其初始器（initializer）、getter和setter都是可选的。属性类型如果可以从初始器或者
     * 从其getter返回值中推断出来，也可以省略。
     * 例如：
     * var age: Int? //错误：需要显式初始化器，隐含默认getter和setter
     * var age = 18  //正确 类型Int、默认getter会让setter
     *
     * 一个只读属性的语法和一个可变的属性的语法有两个方面的不同：
     * 1、只读属性的用val代替var
     * 2、只读属性不允许setter
     * val age: Int? //类型Int、默认getter，必须在构造函数中初始化
     * val age = 18  //类型Int，默认getter
     *
     * 我们可以为属性定义自定义的访问器。如果我们定义了一个自定义的getter，那么每次访问
     * 该属性时都会调用它：
     * val isEmpty:Boolean
     *     get() = this.size == 0
     *
     * 如果定义一个自定义的setter，那么每次给属性赋值时都会调用它：
     *  var name: String
     *      get() = this.toString
     *      set(value){
     *           //解析字符串并赋值给其他属性
     *          setDataFromString(value)
     *      }
     * 按照惯例，setter参数的名称是value，也可以选择其他名称；
     * 自Kotlin1.1起，如果可以从getter推断出属性类型，则可以省略它：
     * val isEmpty get() = this.size == 0
     *
     * 如果需要改变一个访问器的可见性或者对其注解，但不需要改变默认实现，
     * 可以定义访问器而不定义其实现：
     * var name: String = "abc"
     *     private @Inject set
     *
     */
    /**
     * 幕后字段
     * 在kotlin中不能直接声明字段。然而，当一个属性需要一个幕后字段时，Kotlin
     * 会自动提供，这个幕后字段可以使用field标识符在访问器中引用：
     * var counter = 0
     *     set(value){
     *         if(value >= 0) field = value
     *     }
     * field标识符只能用在属性的访问器内。
     * 如果属性至少一个访问器使用默认实现，或者自定义访问器通过field引用
     * 幕后字段，将会为该属性生成一个幕后字段。
     * 例如，下面的情况下，就没有幕后字段：
     * val isEmpty:Boolean
     *     get() = this.size == 0
     *
     * 幕后属性
     * 如果你的需求不符合这套"隐式的幕后字段"方案，那么可以使用幕后属性：
     * private var _table: Map<String,Int> ? = null
     * public val table:Map<String,Int>
     *     get(){
     *         if(_table == null){
     *             _table = HashMap()//类型参数已推断出
     *         }
     *         return _table?:throw AssertionError("Set to null by another thread")
     *     }
     */
    /**
     * 编译器常量
     * 如果只读属性的值在编译期是已知的，那么可以使用const修饰符将其标记为编译器常量。
     * 这种属性需要满足一下要求：
     * - 位于顶层或者object声明或companion object的一个成员
     * - 以String或者原生类型值初始化
     * - 没有自定义getter
     */
    /**
     * 延迟初始化属性与变量
     * 一般地，属性声明为非空类型必须在构造函数中初始化。然而，这经常不放便，
     * 例如：属性可以通过依赖注入来初始化，或者在单元测试的setup方法中初始化。
     * 这种情况下，你不能在构造函数内提供一个非空初始器。但仍然想在类体中引用该属性时避免空检测
     * 为处理这种情况，可以使用lateinit修饰符标记该属性：
     * public class MyTest{
     *     lateinit var subject:TestSubject
     *
     *     @SetUp fun setup(){
     *         subject = TestSubject()
     *     }
     *     @Test fun test(){
     *         subject.method()
     *     }
     * }
     *
     * 该修饰符只能用于类体中的属性（不是在主构造函数中声明的var属性，并且
     * 仅当该属性没有自定义getter或setter时），而自Kotlin1.2起，也用于顶层属性与局部变量。
     * 该属性或变量必须为非空类型，并且不能是原生类型。
     * 在初始化前访问一个lateinit属性会抛出一个特定异常，该异常明确标识该属性
     * 被访问以及它没有初始化的事实。
     *
     * 检测一个lateinit var是否已经初始化过（自1.2起）：
     * if(foo::bar.isInitialized){
     *      foo.bar
     * }
     */
    /**=================属性与字段End====================**/

    /**=================接口====================**/
    /**
     * Kotlin的接口可以既包含抽象方法的声明也包含实现。
     * 与抽象类不同的是，接口无法保存状态，它可以有属性但是必须
     * 声明为抽象或提供访问器实现。
     * 使用关键字interface定义接口：
     * interface MyInterface{
     *      fun bar()
     *      fun foo(){
     *          //可选的方法体
     *      }
     * }
     * 实现接口：一个类或者对象可以实现一个或多个接口
     * class Child : MyInterface{
     *      overrider fun bar(){
     *          //方法体
     *      }
     * }
     *
     * 接口中的属性：
     * 可以在接口中第一属性。在接口中声明的属性要么是抽象的，要么提供访问器的实现。
     * 在接口中声明属性不能有幕后字段，因此接口中声明的访问器不能引用它们。
     * interface MyInterface{
     *      val prop: Int //抽象的
     *      val propertyWithImplementation: String
     *          get() = "foo"
     *      fun foo(){
     *          print(prop)
     *      }
     * }
     * class Child: MyInterface{
     *     override val prop: Int = 29
     * }
     *
     * 接口继承
     * 一个接口可以从其他接口派生，从而既提供基类型成员的实现也声明新的函数与属性。
     * 实现这样接口的类只需定义所缺少的实现。
     *
     * 解决覆盖冲突
     * 实现多个接口时，可能会遇到同意方法继承多个实现的问题，此时我们需要
     * 实现从多个接口继承的所有方法，并在类中指明如何实现它们
     *
     * interface A{
     *     fun foo() { print("A") }
     *     fun bar()
     * }
     *
     * interface B{
     *     fun foo(){ print("B") }
     *     fun bar(){ print("bar") }
     * }
     * class C: A{
     *     override fun bar(){ print("bar") }
     * }
     * class D: A,B{
     *     overrider fun foo(){
     *         super<A>.foo()
     *         super<B>.foo()
     *     }
     *     override fun bar(){
     *         super<B>.bar()
     *     }
     * }
     */
    /**=================接口End====================**/

    /**=================可见性修饰符End====================**/
    /**
     * 可见性修饰符
     * 类、对象、接口、构造函数、方法、属性和它们的setter都可以有可见性修饰符。
     * （getter总是与属性有着相同的可见性）。
     * 在Kotlin中有这四个可见性修饰符：private、protected、internal和public。
     * 如果没有显式指定修饰符的话，默认可见性是public。
     * private:只在声明它的文件内可见（顶层元素）或只在类内部可见（类和接口）；
     * protected:和private一样+在子类中可见
     * internal:本模块内可见
     * public:随处可见
     * 注意：Kotlin中外部类不能访问内部类的private成员
     *
     * 模块：一个模块是编译在一起的一套Kotlin文件
     * - 一个Intellij IDEA模块；
     * - 一个Maven项目；
     * - 一个Gradle源集
     * - 一次<KotlinC>Ant任务执行所编译的一套文件。
     *
     */
    /**=================可见性修饰符End====================**/
}