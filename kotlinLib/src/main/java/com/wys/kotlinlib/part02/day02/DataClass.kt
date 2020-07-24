package com.wys.kotlinlib.part02.day02

/**
 * @author wangyasheng
 * @date 2020/7/16
 * @Describe:数据类
 */


/**
 * 数据类
 * 我们经常创建一些只保存数据的类，在这些类中，一些标准函数往往是从数据机械推导而来的。
 * kotlin中，这叫做数据类并标记为data。
 *
 * 编译器自动从主构造函数中声明的所有属性导出以下成员：
 * - equals()/hashCode()对
 * - toString()格式是"User(name = dnd,age = 18)"
 * - componentN()函数安声明顺序对应与所有属性
 * - copy()函数
 *
 * 为了确保生成的代码的一致性以及有意义的行为，数据类必须满足以下要求：
 * - 主构造函数需要至少一个参数
 * - 主构造函数的所有参数需要标记为val或var
 * - 数据类不能是抽象、开发、密封或者内部的
 * - （在1.1之前）数据类只能实现接口
 *
 * 成员生成遵循关于成员继承的这些规则：
 * - 如果在数据类体中有显式实现equals()、hashCode()或者toString()，或者这些
 *   函数在父类中有final实现，那么不会生成这些函数，而会使用现有函数
 * - 如果超类型具有open的componentN()函数并且返回兼容的类型，那么会为数据类生成
 *   相应的函数，并覆盖超类的实现。如果超类型的这些函数由于签名不兼容或者是final而
 *   导致无法覆盖，那么会报错
 * - 从一个已具copy()函数且签名匹配的类型派生一个数据类在Kotlin 1.2中已弃用，并且
 *   在Kotlin1.3中已禁用
 * - 不允许为componentN()以及copy()函数提供显式的实现
 *
 * 自1.1起，数据类可以扩展其他类。
 * 在jvm中，如果生成的类需要含有一个无参的构造函数，则所有的属性必须指定默认值。
 *
 * 在类体中声明的属性
 * 对于那些自动生成的函数，编译器只使用在构造函数内部定义的属性。如需在生成的实现
 * 中排除一个属性，请将其声明在类体中：在toString()、equals()、hashCode()
 * 以及copy（）的实现中不会有sex属性，并且只有两个component函数，虽然两个
 * Person对象可以有不同的性别，但是它们会视为相等
 * */
data class Person(var name: String = "", var age: UInt = 0u){
    var sex: Int = 0
}

/**
 * 复制
 * 在很多情况下，我们需要复制一个对象改变它的一些属性，但其余部分保持不变。
 * copy()函数就是为此而生成，对于Person类，其实现类似下面这样：
 * fun copy(name:String = this.name,age: Int = this.age) = Person(name,age)
 *
 * val jack = Person(name = "Jack",age = 1)
 * val olderJack = jack.copy(age = 2)
 *
 * 数据类与解构声明
 * 为数据类生成的Component函数使它们可在解构声明中使用：
 * */
fun dateClassTest01(){
    /**复制copy()*/
    val jack = Person("Jack",18U)
//    jack.age = 20U
    val olderJack = jack.copy(age = 60U)

    println(jack)
    println(olderJack)
}
fun dateClassTest02(){
    /**解构声明*/
    val jane = Person("Jane",35U)
    val (name,age) = jane
    //打印"Jane,35 years of age"
    println("$name,$age years of age")
}

/**
 * 标准数据类
 * 标准库提供了Pair与Triple。尽管在很多情况下具名数据类是更好的设计选择，
 * 因为它们通过为属性提供有意义的名称使代码更具可读性。
 * */