package com.wys.commonbaselib.kotlin

/**
 * @author wangyasheng
 * @date 2020-06-08
 * @Describe:
 */
class Day02{
    fun day02Test(){

        /**==========返回也跳转========**/

        //Kotlin有三种结构化的跳转表达式
        // - return ：默认从最直接包围它的函数或者匿名函数返回
        // - break ：终止最直接包围它的循环
        // - continue ： 继续下一次最直接包围它的循环

        //Break与Continue标签
        //在kotlin中任何表达式都可以用标签来标示。标签的格式为标识符后跟@符号，
        //要为一个表达式加标签，我们只要在其前加标签即可。

        //现在，我们可以用标签限制break或者continue
        //标签限制的break跳转到刚好位于该标签指定的循环后面的执行点。
        //continue继续标签指定的循环的下一次
        loop@ for (i in 1..100){
            for (j in 1..100){
                if (j == i)break@loop
            }
        }

        //返回到标签
        //kotlin有函数字面量、局部函数和对象表达式。因此kotlin的函数可以被嵌套。
        //标签限制的return允许我们从外层函数返回。最重要的一个用途就是从lambda表达式中返回
        foo()
        //这个return表达式从最直接包围它的函数即foo中返回
        //如果我们需要从lambda表达式中返回，我们必须给它加标签用于限制return
        foo2()
    }

    fun foo(){
        listOf(1,2,3,4,5).forEach {
            //非局部直接返回到foo的调用者
            if (it == 3) return
            print(it)
        }
        println("this point is unreachable")
    }
    fun foo2(){
        //现在，它只会从lambda表达式中返回
//        listOf(1,2,3,4,5).forEach lit@{
//            if (it == 3) return@lit
//            print(it)
//        }
//        print("done with explicit label")

        //通常情况下使用隐式标签更方便，该标签与接受该lambda的函数同名
        listOf(1,2,3,4,5).forEach{
            if (it == 3) return@forEach
            print(it)
        }
        print("done with implicit label")
        //或者，我们用一个匿名函数替代lambda表达式，
        //匿名函数内部的return语句将从改匿名函数自身返回

//        listOf(1,2,3,4,5).forEach(fun(value : Int){
//            if (value == 3) return
//            print(value)
//        })
//        print("done with anonymous function")

        //上边三个示例中使用的局部返回类似与在常规循循环中使用continue。
        //并没有break的直接等价，不过可以通过增加另一层lambda表达式病虫其中非局部返回模拟
        run loop@{
            listOf(1,2,3,4,5).forEach{
                //从传入run的lambda表达式非局部返回
                if (it == 3) return@loop
                print(it)
            }
        }
        print("done with nested loop")
    }
}