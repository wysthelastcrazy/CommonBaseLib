package com.wys.commonbaselib.kotlin

import java.lang.IllegalArgumentException
import java.lang.Integer.parseInt

/**
 * 顶层函数
 */
fun printMessage(){

}
/**
 * @author wangyasheng
 * @date 2020-06-03
 * @Describe:
 */
class Day01 {
    fun test(){
        var i = -100

        //有符号左移
        println("i=================" +(i shl 1))
        //有符号右移
        println("i=================" +(i shr 1))
        //无符号右移
        println("i=================" +(i ushr 1))
        //位与
        println("i=================" +(i and 1))
        //位或
        println("i=================" +(i or 1))
        //位异或
        println("i=================" +(i xor 1))
        //位非
        println("i=================" +(i.inv()))
    }

    fun check(c: Char){
        if (c == '1'){
            //
        }
    }

    /**
     * char
     */
    fun decimalDigitValue(c: Char) :Int{
        if (c !in '0'..'9')
            throw IllegalArgumentException("out of range")
        return c.toInt()
    }

    /**
     * 数组
     */
    fun initAcrray(){
        //[1,2,3]
        val x: IntArray = intArrayOf(1,2,3)
        //大小为5、值为[0,0,0,0,0]的整型数组
        val arr = IntArray(5)

        //可以用常量初始化数组中的值，例如：
        //大小为5、初始值为[42,42,42,42,42]的整型数组
        val arr2 = IntArray(5){42}

        //也可以使用lambda表达式来初始化数组中的值，例如：
        //大小为5、值为[0,1,2,3,4]的整型数组
        val arr3 = IntArray(5){it*1}

        val asc = Array(5) {
            i -> (i*i).toString()
        }
        asc.forEach {
            println(it)
        }
    }

    /**
     * 字符串
     */
    fun str(){
        val s = "abcd"
        //循环迭代字符串
        for (c in s){
            println(c)
        }
        //可以使用+ 操作符链接字符串，也适用与连接字符串与其他类型的值
        println(s + 1 + "def")
        //注意，在大多数情况下，优先使用字符串模版
        val i = 2
        println("$s.length add $i def")
    }

    /**
     * 导包
     */
    fun mpackage(){
        //可以导入单独的名字，例如：
        //import org.example.Message

        //也可以导入一个作用域下的所有内容（包、类、对象等）例如：
        //import org.example.*

        //如果出现名字冲突，可以使用as关键字在本地重命名冲突项来消除歧义

        //import org.example.Message // Message 可访问
        //import org.test.Message as testMessage // testMessage 代表“org.test.Message”

        //import关键字不仅限于导入类；也可以导入其他声明
        //--顶层函数和属性
        //--在对象中声明的函数和属性
        //--枚举常量
    }

    /**
     * 控制流
     */
    fun flowOfControl(){

        /**===========if============**/
        //在kotlin中，if是一个表达式，即它会返回一个值，因此就不需要三元运算符（条件？然后：否则），
        //因为普通的if就能胜任这个角色

        var a = 10
        var b = 20
        //传统用法
        var max = a
        if (a < b) max = b

        //with else
        if (a > b){
            max = a
        }else{
            max = b
        }

        //作为表达式
        max = if (a > b) a else b

        //if分支还可以是代码块，最后的表达式作为该块的值
        max = if (a > b){
            println("choose a")
            a
        }else{
            println("choose b")
            b
        }
        /**如果使用if作为表达式而不是语句，则该表达式需要有else分支**/


        /**==========when=============**/
        //when取代了类C语言的switch操作符
        var i = 1
        when(i){
            1 -> println("i == 1")
            2 -> println("i == 2")
            else -> {
                println("x is neither 1 or 2")
            }
        }
        //多分支相同处理，可以把多个分支放在一起，用逗号隔开
        when(i){
            0,1 -> println("i == 0 or i == 1")
            else -> println("other wise")
        }
        //可以使用任意表达式（而不只是常量）作为分支条件
        var s = "2"
        when(i){
            parseInt(s) -> println("s encode i")
            else -> println("s does not encode s")
        }

        //检查一个值是或者不是一个特定类型
        var flag = hasPrefix(s)

        //when也可以用来取代 if-else if链。
        //如果不提供参数，所有的分支条件都是简单的布尔表达式，
        //当一个分支为true时则执行该分支
        val num :Int = 4
        when{
            isOdd(num) -> println("num is odd")
            isEven(num) -> println("num is even")
            else -> println("funny")
        }

        /**==================for====================**/
        //for循环可以对任何提供迭代器的对象进行遍历，相当于c语言中的foreach

        val collection = intArrayOf(1,2,3)
        for (item in collection) println(item)
        for (item in collection){
            //....
        }
        //for可以遍历任何提供了迭代器的对象，即：
        //有一个成员函数或者扩展函数iterator()，它的返回类型
        //  - 有一个成员函数或扩展函数next(),并且
        //  - 有一个成员函数或扩展函数hasNext()返回Boolean
        //这三个函数都需要标记为operator

        //在数字区间上迭代
        //等同于1 <= i&&i<=3
        for (i in 1..3){
            println(i)
        }
        //不包含结束元素的数字 1 <= i&&i<3
        for (i in 1 until 3){
            println(i)
        }
        for (i in 6 downTo 0 step 2){
            println(i)
        }
        //类的命名区间
        val versionRange = Version(1,11) .. Version(1,30)
        println(Version(0,9) in versionRange)
        println(Version(1,20) in versionRange)

        //根据索引遍历数组或者list：
        for (i in collection.indices){
            println(collection[i])
        }
        //或者使用库函数withIndex
        for ((index,value) in collection.withIndex()){
            println("the element at $index is $value")
        }


        /**================While=================**/
        var x = 6
        while (x > 0){
            x --
        }
        do {
            x++
            val y = x
        }while (y<10)//y在此处可见
    }
    private fun isEven(i: Int): Boolean{
        return true
    }
    private fun isOdd(i: Int): Boolean{
        return true
    }
    private fun hasPrefix(x: Any) = when(x){
        is String -> x.startsWith("prefix")
        else -> false
    }
    public class Version(val major: Int,val minor: Int): Comparable<Version>{
        override fun compareTo(other: Version): Int {
            if (this.major != other.major){
                return this.major - other.major
            }
            return this.minor - other.minor
        }

    }
}