package com.wys.kotlinlib.part04

/**
 * @author wangyasheng
 * @date 2020/8/3
 * @Describe:集合操作
 */

/**
 * 集合操作概述
 *
 * Kotlin标准库提供了用于集合执行操作的多种函数。这包括简单的操作，
 * 例如获取或添加元素，以及更复杂的操作，包括搜索、排序、过滤、转换等。
 */

/**
 * 扩展与成员函数
 *
 * 集合操作自私标准库中以两种方式声明：集合接口的成员函数和扩展函数。
 *
 * 成员函数定义了对于集合类型是必不可少的操作。例如Collection包含
 * 函数isEmpty()来检测其是否为空；List包含用于对元素进行索引访问
 * 的get()等等。
 *
 * 创建自己的集合接口实现时，必须实现其成员函数。为了使新实现的创建
 * 更加容易，请使用标准库中集合接口的框架实现：AbstractCollection、
 * AbstractList、AbstractSet、AbstractMap及其相应可变抽象类。
 *
 * 其他集合操作被声明为扩展函数。这些是过滤、转换、排序和其他集合处理功能。
 *
 */

/**
 * 公共操作
 *
 * 公共操作用于只读集合与可变集合。常见操作分为一下几类：
 *
 * - 集合转换
 * - 集合过滤
 * - plus与minus操作符
 * - 分组
 * - 取集合的一部分
 * - 取单个元素
 * - 集合排序
 * - 集合聚合操作
 *
 * 这些操作将返回其结果，而不会影响原始集合。
 *
 * 对于某些集合操作，有一个选项可以指定目标对象。目标是一个可变集合，该函数
 * 将其结果项附加到该可变对象中，而不是在新对象中返回它们。对于执行带有目标
 * 的操作，有单独函数，其名称中带有To后缀，例如，用filterTo()代替filter()
 * 以及用associateTo()代替associate()。这些函数将目标集合作为附加参数。
 *
 * 为了方便起见，这些函数将目标集合返回了，因此可以子啊函数
 * 调用的相应参数中直接创建它：
 * val result = numbers.mapTo(HashSet()){it.length}
 * 具有目标的函数可用于过滤、关联、分组、展平以及其他操作。
 */
fun operateTest1(){
    val numbers = listOf("one","two","three","four")
    val filterResults = mutableListOf<String>()
    numbers.filterTo(filterResults){ it.length > 3 }
    numbers.filterIndexedTo(filterResults){ index, _ ->  index ==0 }
    //包含两个操作的结果
    println(filterResults)

    val result = numbers.mapTo(ArrayList()){ it.length }
    println("distinct item length are $result")
}

/**
 * 写操作
 *
 * 对于可变集合，还存在可变更集合状态的写操作。这些操作包括添加、删除和更新元素。
 * 对于某些操作，有成对的函数可以执行相同的操作：一个函数就地应用该操作，
 * 另一个函数将结果作为单独的集合返回。例如，sort()就地对可变集合进行排序，
 * 因此其状态发生了变化；sorted()创建一个新集合，该集合包含按排序顺序相同
 * 的元素。
 */
fun operateTest2(){
    val numbers = mutableListOf("one","two","three","four")
    val sortedNumbers = numbers.sorted()
    //false
    println(numbers == sortedNumbers)
    numbers.sort()
    //true
    println(numbers == sortedNumbers)
}

/**
 * 集合转换
 *
 * Kotlin标准库为集合转换提供了一组扩展函数。这些函数根据提供的转换规则'
 * 从现有集合中构建新集合。
 *
 * 1、映射
 * 映射转换从另一集合的元素上的函数结果创建一个集合。基本的映射函数是map().
 * 它将给定的lambda函数应用于每个后续元素，并返回lambda结果列表。结果顺序
 * 与元素的原始顺序相同。如需用到元素索引作为参数的转换，请使用mapIndexed().
 *
 * 如果转换在某些元素上产生null值，则可以通过调用mapNotNull()函数取代map()
 * 或mapIndexedNotNull()取代mapIndexed()来从结果集中过滤掉null值。
 *
 * 映射转换时，有两个选择：转换建，使值保持不变或者转换值，使建不变。
 * 要将指定转换应用于键，使用mapKeys();反过来mapValues()转换值。
 * 这两个函数都使用将映射条目作为参数的转换，因此可以操作其键与值。
 *
 * 2、双路合并
 * 双路合并转换是根据两个集合中具有相同位置的元素构建配对。在Kotlin标准库中，
 * 这是通过zip()扩展函数完成的。在一个集合（或数组）上以另一个集合（或数组）作为
 * 参数调用时，zip()函数返回Pair对象的列表（List）。接收者集合的元素是这些配对
 * 中的第一个元素。如果集合的大小不同，则zip()的结果为较小集合的大小；结果中不
 * 包含较大集合的后续元素。zip()也可以中缀形式调用 a zip b 。
 *
 * 也可以使用带有两个参数的转换函数来调用zip()：接收者元素和参数元素。在这种
 * 情况下，结果List包含在具有相同位置的接收者对和参数元素对上调用的转换函数的
 * 返回值。
 *
 * 当拥有Pair的List时，可以进行反向转换unzipping--从这些键值中构建两个列表：
 * - 第一个列表包含原始列表中每个Pair的键。
 * - 第二个列表包含原始列表中每个Pair的值。
 * 要分隔键值对列表，使用unzip()函数。
 *
 *
 * 3、关联
 * 关联转换允许从集合元素和与其关联的某些值构建Map。在不同的关联类型中，元素可
 * 以是关联Map中键或值。
 *
 * 基本的关联函数associateWith()创建一个Map，其中原始集合的元素为键，并通过
 * 给定的转换函数从中产生值。如果两个元素相等，则仅最后一个保留在Map中。
 *
 * 为了使用集合元素作为值来构建Map，有一个函数associateBy()。它需要一个函数，
 * 该函数根据元素的值返回键。如果两个元素相等，则仅最后一个保留在Map中。还可以使用
 * 值转换函数来调用associateBy().
 *
 * 另外一种构建Map的方法是使用函数associate()，其中Map键和值都是通过集合元素
 * 生成的。它需要一个lambda函数，该函数返回Pair。
 * 注意，associate()会生成临时的Pair对象，这可能会影响性能。因此，当性能
 * 不是很关键或比其他选项更可取时，再使用associate()。
 *
 * 4、打平
 * 如需操作嵌套的集合，则可能会发现提供对嵌套集合元素进行打平访问的标准库函数很有用。
 *
 * 第一个函数为flatten()，可以在一个集合的集合（例如一个Set组成的List）上调用它。
 * 该函数返回嵌套集合中的所有元素的一个List。
 *
 * 另一个函数 -- flatMap()提供了一种灵活的方式来处理嵌套的集合。它需要一个函数将
 * 一个集合元素映射到另一个集合。因此，flatMap()返回单个列表其中包含所有元素的值。
 * 所以以flatMap()表现为map()与flatten()的连续调用。
 *
 *
 * 5、字符串表示
 * 如果需要以可读格式检索集合内容，请使用将集合转换为字符串的函数：joinToString()与
 * joinTo()。
 * joinToString()根据提供的参数从集合元素构建单个String。joinTo()执行相同的
 * 操作，但是结果附加到给定的Appendable对象。
 * 当使用默认参数调用时，函数返回的结果类似于在集合上调用toString()。
 *
 * 要构建自定义字符串表示形式，可以在函数参数separator、prefix与postfix中指定
 * 其参数。结果字符串将以prefix开头，以postfix结尾。除最后一个元素外，separator
 * 将位于每个元素之后。
 *
 * 对于较大的集合，可能需要指定limit--将包含在结果中元素的数量。如果集合
 * 大小超出limit，所有其他元素将被truncated参数的单个值替换。
 */
data class StringContainer(val values: List<String>)
fun operateTest3(){
    /**映射转换*/
    val numbersSet = setOf(1,2,3)
    println(numbersSet.map { it * 3 })
    println(numbersSet.mapIndexed { index, value -> value * index })

    //去除null值
    println(numbersSet.mapNotNull { if (it == 2) null else it*3 })
    println(numbersSet.mapIndexedNotNull{
        index, value -> if (index == 0) null else value * index
    })
    //map转换key与转换值
    val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key11" to 11)
    println(numbersMap.mapKeys { it.key.toUpperCase() })
    println(numbersMap.mapValues { it.value + it.key.length })



    /**双路合并*/
    //双路合并，获取List<Pair>
    val colors = listOf("red","brown","grey")
    val animals = listOf<String>("fox","bear","wolf")
    println(colors zip animals)
    val twoAnimals = listOf<String>("fox","bear")
    println(colors.zip(twoAnimals))
    //双路合并
    println(colors.zip(animals){
        color, animal -> "The ${animal.capitalize()} is $color"
    })
    //unzip
    val numberPairs = listOf("one" to 1,"two" to 2,"three" to 3)
    println(numberPairs.unzip())


    /**关联转换*/
    val numbersList = listOf<String>("one","two","three","four")
    //转换value
    println(numbersList.associateWith { it.length })
    //转换key
    println(numbersList.associateBy { it.first().toUpperCase() })
    //key与value都转换
    println(numbersList.associateBy (keySelector = {it.first().toUpperCase()},
            valueTransform = {it.length}))

    /**打平*/
    //flatten
    val numberSets = listOf(setOf(1,2,3),setOf(4,5,6),setOf(7,8,9))
    println(numberSets.flatten())
    //flatMap() 表现为map()与flatten()的连续调用。
    val containers = listOf(
            StringContainer(listOf("one","two","three")),
            StringContainer(listOf("four","five","six")),
            StringContainer(listOf("seven","eight"))
    )
    println(containers.flatMap { it.values })


    /**字符串表示*/
    val numbers = listOf("one","two","three","four")
    println(numbers)
    println(numbers.joinToString())
    println(numbers.joinToString(separator = " | ",prefix = "start:",
            postfix = ":end"))
    //自定义元素的表示形式
    println(numbers.joinToString{"Element: ${it.toUpperCase()}"})
    //执行和joinToString一样的操作，但结果附加到listString参数对象上
    val listString = StringBuffer("The list of numbers:")
    numbers.joinTo(listString)
    println(listString)

    val numbers2 = (1..100).toList()
    //limit--包含在结果中的元素数量，truncated超出数量后的替代文案（不过多出几个元素，此值只一次）
    println(numbers2.joinToString(limit = 10, truncated = "<...>"))

}
fun operateTest(){
//    operateTest1()
//    operateTest2()
    operateTest3()
}