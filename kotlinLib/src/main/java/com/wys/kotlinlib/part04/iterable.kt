package com.wys.kotlinlib.part04

/**
 * @author wangyasheng
 * @date 2020/7/31
 * @Describe:迭代器
 */

/**
 * Iterable<T>
 *
 * 对于遍历集合元素，Kotlin标准库支持迭代器的常用机制
 * -- 对象可按顺序提供对元素的访问权限，而不会暴露集合
 * 的底层结构。当需要逐个处理集合的所有元素时，迭代器非常
 * 有用。
 *
 * Iterable<T>接口的继承者（包括Set与List）可以通过
 * 调用iterator()函数获取迭代器。一旦获得迭代器它就指向
 * 集合的第一个元素；调用next()函数将返回此元素，并将迭代
 * 器指向下一个元素（如果下一个元素存在）。一旦迭代器通过
 * 了最后一个元素，它就不能再用于检索元素；也无法重新指向
 * 以前的任何位置。要再次遍历集合，需要创建一个新的迭代器。
 *
 * 遍历Iterable集合的另一种方法是for循环。在集合中使用
 * for循环时，将隐式获取迭代器。
 * 还有一个好用的forEach()函数，可自动迭代集合并为每个元素
 * 执行给定的代码。
 */
fun iterableTest1(){
    val numbers = listOf("one","two","three","four")
//    val numbersIterator = numbers.iterator()
//    while (numbersIterator.hasNext()){
//        println(numbersIterator.next())
//    }
    //与上述while等效
    for( item in numbers){
        println(item)
    }
    //与上述循环等效
    numbers.forEach{
        println(it)
    }
}

/**
 * List迭代器
 *
 * 对于列表，有一个特殊的迭代器实现：ListIterator，它支持
 * 列表的双向迭代：正向与反向。反向迭代由hasPrevious()和
 * previous()函数实现。此外，ListIterator通过nextIndex()
 * 与previousIndex()函数提供有关元素索引的信息。
 * 具有双向迭代的能力意味着ListIterator在到达最后
 * 一个元素后仍可以使用。
 */
fun iterableTest2(){
    val numbers = listOf("one","two","three","four")
    val listIterator = numbers.listIterator()

    while (listIterator.hasNext()){
        print("Index: ${listIterator.nextIndex()}")
        println(",value: ${listIterator.next()}")
    }
    println("Iterating backwards:")
    while (listIterator.hasPrevious()){
        print("Index: ${listIterator.previousIndex()}")
        println(",value: ${listIterator.previous()}")
    }

}

/**
 * 可变迭代器
 *
 * 为了迭代可变集合，于是有了MutableIterator来扩展Iterator使
 * 其具有元素删除函数remove()。因此，可以在迭代时从集合汇总删除元素。
 * 除了删除元素，MutableIterator还可以在迭代列表时插入和替换元素。
 */
fun iterableTest3(){
    val numbers = mutableListOf("one","two","three","four")
    val mutableIterator = numbers.listIterator()

    mutableIterator.next()
    mutableIterator.add("two")
    println("After add: $numbers")
    mutableIterator.next()
    mutableIterator.set("three")
    println("After set: $numbers")
    mutableIterator.remove()
    println("After remove: $numbers")
}
fun iterableTest(){
//    iterableTest1()
//    iterableTest2()
    iterableTest3()
}

/**
 * 区间和数列
 *
 * Kotlin可通过调用kotlin.ranges包中的rangeTo()函数
 * 以及其操作符形式的..轻松地创建两个值的区间。通常，rangeTo()
 * 会辅以in或!in函数。
 *
 * 整数类型区间（IntRange、LongRange、CharRange）还有
 * 一个拓展特性：可以对其进行迭代。这些区间也是相应整数类型
 * 的等差数列。这种区间通常用于for循环中的迭代。
 */

fun rangeTest1(){
    val i = 2
    //等同于1 <= i && i<= 4
    if (i in 1..4){
        println(i)
    }

    for (i in 1 .. 4){
        println("i = $i")
    }
    //要反向迭代数字，请使用downTo函数而不是 .. 操作符
    for (i in 4 downTo 1){
        println("i = $i")
    }
    //也可以通过任意步长迭代数字
    for (i in 1 .. 8 step 2){
        println("i = $i")
    }
    for (i in 8 downTo 1 step 2){
        println("i = $i")
    }
    //要迭代不包含其结束元素的数字区间，请使用until函数
    for (i in 1 until 10){
        print("$i ")
    }
}

/**
 * 区间
 *
 * 区间从数学意义上定义了一个封闭的间隔：它由两个端点值定义，
 * 这两个端点值都包含在该区间内。区间是为可比较类型定义的，
 * 具有顺序，可以定义任意实例是否在两个给定实例之间的区间内。
 *
 * 区间的主要操作是contains，通常以in与!in操作符的形式使用。
 *
 * 要为类创建一个区间，请在区间起始值上调用rangeTo()函数，并
 * 提供结束值作为参数。rangTo()通常以操作符 .. 形式调用。
 */
class Version(val major: Int,val minor: Int): Comparable<Version>{
    override fun compareTo(other: Version): Int {
        if (this.major != other.major){
            return this.major - other.major
        }
        return this.minor - other.minor
    }

}
fun rangeTest2(){
    val versionRange = Version(1,11) .. Version(1,30)
    println(Version(0,9) in versionRange)
    println(Version(1,20) in versionRange)
}

/**
 * 数列
 *
 * 整数类型的区间（例如Int、Long、Char）可视为等差数列。在Kotlin中，
 * 这些数列由特殊类型定义：IntProgression、LongProgression与CharProgression。
 *
 * 数列具有三个基本属性：first元素、last元素和一个非零的step。
 * 首个元素为first，后续元素是前一个元素加上一个step。以确定的
 * 步长在数列上进行迭代等效于java中基于索引的for循环：
 * for(int i = first; i <= last; i += step){...}
 *
 * 通过迭代数列隐式创建区间时，此数列的first与last元素是区间的端点，step为1。
 * 要指定数列步长，可使用step函数。
 * 数列的last元素是这样计算的：
 * - 对于正步长：不大于结束值且满足（last - first）% step == 0的最大值
 * - 对于负步长：不小于结束值且满足（last - first）% step == 0的最小值。
 * 因此，last元素并非总与指定的结束值相同。
 *
 * 数列实现Iterable<E>，其中E分别为Int、Long、Char，因此可以在各种
 * 集合函数（如map、filter等等）中使用它们。
 */
fun rangeTest3(){
    IntProgression
    println((1 .. 10).filter { it % 2 == 0 })
}
fun rangeTest(){
//    rangeTest1()
//    rangeTest2()
    rangeTest3()
}