package com.wys.kotlinlib.part04


/**
 * @author wangyasheng
 * @date 2020/8/3
 * @Describe:序列
 */

/**
 * 序列
 *
 * 除了集合之外，Kotlin标准库还包含另一种容器类型--序列Sequence<T>.
 * 序列提供与Iterable相同的函数，但是实现另一种方法来进行多步骤集合处理。
 *
 * 当Iterable的处理包含多个步骤时，它们会优先执行：每个步骤完成并返回
 * 其结果（中间集合）。在此结果上执行下边的步骤。
 * 而序列的多步处理在可能的情况下会延迟执行：仅当请求整个处理链的结果时
 * 才进行实际计算。
 *
 * 操作执行的顺序也不同：Sequence对每个元素逐个执行所有处理步骤；而Iterable
 * 完成整个集合的每个步骤，然后进行下一步。
 *
 * 因此，这些序列可避免生成中间步骤的结果，从而提高了整个集合处理链的性能。
 * 但是，序列的延迟性质增加了一个开销，这些开销在处理较小的集合或进行更简单的计算时
 * 可能很重要。因此，应该同时考虑使用Sequence与Iterable，并确定在那种情况
 * 下更合适。
 */

/**
 * 构造
 *
 * 1、由元素
 * val numbersSequence = sequenceOf("four","three","two","one")
 *
 * 2、由Iterable
 * 如果已经有一个Iterable对象（List或Set），则可以通过调用asSequence()创建序列。
 * val numbers = listOf("one","two","three","four")
 * val numbersSequence = numbers.asSequence()
 *
 * 3、由函数
 * 创建序列的另一种方法时通过使用计算其元素的函数来构建序列。要基于函数构建序列，
 * 请以该函数作为参数调用generateSequence()。可以将第一个元素指定为显式值
 * 或函数调用的结果。当提供的函数返回null时，序列生成停止。因此一下示例中的序列
 * 时无限的:
 * val oddNumbers = generateSequence(1){it + 2}
 * 要使用generateSequence()创建有限序列，请提供一个在需要的最后一个元素之后
 * 返回null的函数：
 * val oddNumbersLessThan10 = generateSequence(1){ if(it < 10) it + 2 else null }
 *
 * 4、由组块
 * 最后，有一个函数可以逐个或按任意大小的组块生成序列元素--sequence()函数。
 * 此函数采用一个lambda表达式，其中包含yield()与yieldAA()函数的调用。
 * 它们将一个元素返回给序列使用者，并暂停sequence()的执行，直到使用者请求
 * 下一个元素。yield()使用单个元素作为参数；yieldAll()中可以采用Iterable对象\
 * Iterable或其他Sequence。yieldAll()的Sequence参数可以是无限的。
 * 当然，这样的调用必须是最后一个：之后的所有调用者都永远不会执行：
 * val oddNumbers = sequence{
 *     yield(1)
 *     yieldAll(listOf(3,5))
 *     yieldAll(generateSequence(7){ it +2 })
 * }
 */
fun sequenceTest1(){
    val oddNumbersLessThan10 = generateSequence(1){ if(it < 10) it + 2 else null }
    println(oddNumbersLessThan10.toList())
    println("count = ${oddNumbersLessThan10.count()}")

    val oddNumbers = sequence{
        yield(1)
        yieldAll(listOf(3,5))
        yieldAll(generateSequence(7){ it +2 })
    }
    println(oddNumbers.take(5).toList())
}

/**
 * 序列的操作
 *
 * 关于序列操作，根据其状态要求可以分为一下几类：
 * - 无状态操作 不需要状态，并且可以独立处理每个元素，例如map()或filter（）。
 *   无状态操作还可能需要少量常数个状态来处理元素，例如take()与drop()。
 * - 有状态操作需要大量状态，通常与序列中元素的数列成比例。
 *
 * 如果序列操作返回延迟生成的另一个序列，则称为中间序列。否则，该操作作为
 * 末端操作。末端操作的示例为toList()或sum()。只能通过末端操作才能检索
 * 序列元素。
 *
 * 序列可以多次迭代；但是，某些序列实现可能会约束自己仅迭代一次。
 *
 */
fun sequenceTest2(){
    /**通过示例看Iterable与Sequence之间的区别*/

    /**
     * Iterable
     * 假定有一个单词列表。下面代码过滤长于三个字符的单词，并打印前四个单词的长度。
     * 运行此代码时，会看到filter()与map()函数的执行顺序与代码中出现的顺序相同。
     * 首先，将看到filter:对于所有元素，然后是length：对于过滤后剩余元素，
     * 然后是最后两行的输出。
     * */
    val words = "The quick brown fox jumps over the lazy dog.".split(" ")
    val lengthList = words.filter{
        println("filter: $it")
        it.length > 3
    }.map {
        println("length: ${it.length}")
        it.length
    }.take(4)
    println("Lengths of first 4 words longer than 3 chars:")
    println(lengthList)

    /**
     * Sequence
     * 用序列写相同的逻辑，输出表明，仅在构建结果列表时才调用filter()与map()函数。
     * 因此，首先看到文本"Lengths of..."的行，然后开始进行序列处理。注意，对于
     * 过滤后剩余的元素，映射在过滤下一个元素之前执行。当结果大小达到4个时，
     * 处理将停止，因为它是take(4)可以返回的最大大小。
     * */
    val wordsSequence = words.asSequence()
    val lengthSequence = wordsSequence.filter {
        println("filter: $it")
        it.length > 3
    }.map {
        println("Length: ${it.length}")
        it.length
    }.take(4)
    println("Lengths of first 4 words longer than 3 chars:")
    //末端操作：以列表形式获取结果
    println(lengthSequence.toList())
}

fun sequenceTest(){
//    sequenceTest1()
    sequenceTest2()
}