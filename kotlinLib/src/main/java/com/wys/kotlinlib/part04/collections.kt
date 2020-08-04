package com.wys.kotlinlib.part04

import java.util.*
import kotlin.collections.HashSet

/**
 * @author wangyasheng
 * @date 2020/7/30
 * @Describe:集合
 */


/**
 * 集合概述
 *
 * - List 有序集合：可通过索引访问元素；
 * - Set 唯一元素的集合，无序；
 * - Map（字典）键值对：键唯一，值可重复。
 *
 * 集合类型：
 * Kotlin标准库提供了基本集合类型的实现：set、list以及map。
 * 一对接口代表了美中集合类型：
 * - 一个可读接口，提供访问集合元素的操作。
 * - 一个可变接口，通过写操作扩展相应的只读接口：添加、删除和更新元素。
 * 注意：更改可变集合不需要它是以var定义的变量：写操作修改同一个可变集合
 * 对象，因此引用不会改变。但是不能对val集合重新赋值。
 *
 *
 * 只读集合类型是型变的。这意味着，集合类型与元素类型具有相同的子类型关系。
 * map在值类型上是型变的，但在键类型上不是。
 *
 * 可变集合不是型变的；否则将导致运行时故障。如果MutableList<Rectangle>
 * 是MutableList<Shape>的子类型，你可以在其中插入其他Shape的继承者
 * （比如Circle），从而违反了它的Rectangle类型参数
 *
 */

/**
 * Collection
 * Collection<E>是集合层次结构的根。
 * 此接口表示一个只读集合的共同行为：检索大小、检测是否为成员等等。
 * Collection继承自Iterable<E>接口，它定义了迭代元素的操作。
 * 可以使用Collection作为适用于不同集合类型的函数的参数。对于更
 * 具体的情况，使用Collection的继承者：List与Set。
 *
 *
 * MutableCollection是一个具有写操作的Collection接口，例如add以及remove。
 */
fun printAll(strings: Collection<String>){
    for (s in strings)
        print("$s ")
    println()
}

fun List<String>.getShortWordsTo(shortWords: MutableList<String>,
                                maxLength: Int){
    this.filterTo(shortWords){
        it.length <= maxLength
    }
    val articles = setOf("a","A","an","An","the","The")

    shortWords -= articles
}

/**
 * List
 * List<E>以指定的顺序存储元素，并提供使用索引访问元素的方法。
 * 索引从0开始直到最后一个元素的索引（list.size - 1）。
 * List元素（包括空值）可以重复：List可以包含任意数量的相同
 * 对象或单个对象的出现。如果两个List在相同的位置具有相同大小和
 * 相同结构的元素，则认为它们是相等的。
 * MutableList<E>是可以进行写操作的List。
 *
 * 在某些方面List与数组（Array）非常相似。但是，有一个重要的区别：
 * 数组的大小是在初始化时定义的，永远不会改变；反之，List没有预定义
 * 的大小；作为写操作的结果，可以更改List的大小。
 *
 * 在Kotlin中，List的默认实现时ArrayList。
 */
data class Person(var name: String,var age: Int){

}
fun collectionsTest1(){
    val bob = Person("Bob",30)
    val person = listOf(Person("Adam",20),bob,bob)
    val person2 = listOf(Person("Adam",20), Person("Bob",30),bob)

    println(person == person2)
    bob.age = 32
    println(person == person2)
}

/**
 * Set
 * Set<T>存储唯一的元素；它们的顺序通常是未定义的。null元素也是唯一的：
 * 一个Set只能包含一个null。当两个set具有相同的大小并且对于一个set中的
 * 每个元素都能在另一个set中存在相同的元素，则两个set相等。
 *
 * MutableSet是一个带有来自MutableCollection的写操作接口的Set。
 *
 * Set的默认实现 - LinkedHashSet - 保留元素插入的顺序。因此依赖顺序
 * 的函数，例如first()或last()，会在这些set上返回可预测的结果。
 *
 * 另一种实现方式 - HashSet - 不声明元素的顺序，所以在它上面调用
 * 这些函数返回不可预测的结果。但是HashSet只需要较少的内存来存储
 * 相同数量的元素。
 */
fun collectionsTest2(){
    val numbers = setOf<Int>(1,2,3,4)
    println("Number of elements: ${numbers.size}")
    if (numbers.contains(1)) println("1 is in the set")

    val numbersBackwards = setOf<Int>(4,3,2,1)
    println("The sets are equal: ${numbers == numbersBackwards}")

    println(numbers.first() == numbersBackwards.first())
    println(numbers.first() == numbersBackwards.last())
}

/**
 * Map
 * Map<K,V>不是Collection的接口继承者；但是它也是Kotlin的一种集合类型。
 * Map存储键值对；键是唯一的，但是不同的键可以与相同的值配对。Map接口提供
 * 特定的函数进行通过键访问值、搜索键和值等操作。
 *
 * 无论键值对的顺序如何，包含相同键值对的两个Map是相等的。
 *
 * MutableMap是一个具有写操作的Map接口，可以使用该接口
 * 添加一个新的键值对或更新给定键的值。
 *
 * Map的默认实现 - LinkedHashMap - 迭代Map时保留元素插入顺序。
 * 反之，另一种实现 - HashMap - 不声明元素的顺序
 */
fun collectionsTest3(){
    val numbersMap = mapOf("key1" to 1,"key2" to 2,"key3" to 3)
    println("All keys: ${numbersMap.keys}")
    println("All values: ${numbersMap.values}")
    if ("key2" in numbersMap)
        println("Value by key \"key2\": ${numbersMap["key2"]}")

    if (1 in numbersMap.values)
        println("The value 1 is in the map")
    //同上
    if (numbersMap.containsValue(1))
        println("The value 1 is in the map")

    val anotherMap = mapOf("key2" to 2,"key1" to 1,"key3" to 3)
    println("The maps are equal: ${numbersMap == anotherMap}")

    val mutableMap = mutableMapOf("one" to 1,"two" to 2)
    mutableMap.put("three",3)
    mutableMap["one"] = 11
    println(mutableMap)
}

/**
 * 构造集合
 *
 * 1、由元素构造
 * 创建集合的最常用方法是使用标准函数listOf<T>()、setOf<T>()、
 * mutableListOf<T>()、mutableSetOf<T>().
 * 如果以逗号分隔的集合元素列表作为参数，编译器会自动检测元素类型。
 * 创建空集合时，须明确指定类型。
 *
 * 同样的，Map也有这样的函数mapOf()与mutableMapOf()。映射的键和值
 * 作为Pair对象传递（通常使用中缀函数 to 创建）。to符号创建了一个
 * 短期存活的Pair对象，因此建议仅在性能不重要时才使用它。为避免过多
 * 的内存使用，请使用其他方法。例如，可以创建可写Map并使用写入操作填充它。
 * apply()函数可以帮助保持初始化流畅。
 *
 * 2、空集合
 * 还有用于创建没有任何元素的合集的函数：emptyList()、emptySet()与
 * emptyMap()。创建空集合时，应指定集合将包含的元素类型。
 *
 *
 * 复制
 *
 * 要创建与现有集合具有相同元素的集合，可以使用复制操作。标准库中的集合
 * 复制操作创建了具有相同元素引用的浅复制集合。因此，对集合元素所做的更
 * 改会反映到其所在副本中。
 *
 * 在特定时刻通过集合复制函数，例如，toList()、toMutableList（）、
 * toSet()等等。创建了集合的快照。结果是创建了一个具有相同元素的新集合
 * 如果源集合中添加或删除元素，则不会影响副本。副本也可以独立于源集合进行
 * 更改。
 *
 * 这些函数还可用于集合转换为其他类型，例如根据List构建Set，反之亦然。
 */
fun collectionsTest4(){
    val numberSet = setOf("one","two","three","four")
    val emptySet = mutableSetOf<String>()
//    val numberMap = mapOf("one" to 1,"two" to 2)

    val numberMap = mutableMapOf<String,String>()
            .apply { this["one"] = "1";this["two"] = "2"}

    //空集合
    val empty = emptyList<String>()

    //list的初始化函数
    //对于List，有一个接受List的大小与初始化函数的构造函数，
    //该初始化函数根据索引定义元素的值。
    val doubled = List(3) { index -> index*2 }
    println(doubled)

    /**
     * 具体类型构造函数
     * 要创建具体类型的集合，可以使用这些类型的构造函数。
     * 类似的构造函数对于Set与Map的个实现中均有提供
     */

    val linkedList = LinkedList<String>(listOf("one","two","three"))
    val preSizedSet = HashSet<Int>()

    //复制
    val sourceList = mutableListOf(1,2,3)
    val copyList = sourceList.toMutableList()
    val readOnlyCopyList = sourceList.toList()
    sourceList.add(4)
    println("Copy size: ${copyList.size}")
//    readOnlyCopyList.add(4) //编译异常
    println("Read-only copy size: ${readOnlyCopyList.size}")

    val copySet = sourceList.toMutableSet()
    copySet.add(3)
    copySet.add(4)
    println(copySet)

    //其他集合函数
    val numbers  = listOf("one","two","three","four")
    val longerThan3 = numbers.filter { it.length > 3 }
    println(longerThan3)

    //映射生成转换结果列表：
    val numbers2 = setOf(1,2,3)
    println(numbers2.map { it*3 })
    println(numbers2.mapIndexed{
        index, i ->
        index * i
    })

    //关联生成Map
    val numbers3 = listOf("one","two","three","four")
    println(numbers3.associateWith{ it.length })
}


fun collectionsTest(){
    val stringList = listOf("one","two","one")
    printAll(stringList)

    val stringSet = setOf<String>("one","two","three")
    printAll(stringSet)

    val words = "A long time ago in a galaxy far far away".split(" ")
    val shortWords = mutableListOf<String>()
    words.getShortWordsTo(shortWords,3)
    println(shortWords)

//    collectionsTest1()
//    collectionsTest2()
//    collectionsTest3()
    collectionsTest4()
}