package com.wys.kotlinlib.part02.day02

/**
 * @author wangyasheng
 * @date 2020/7/20
 * @Describe:枚举类
 */

enum class Direction{
    NORTH,SOUTH,WEST,EAST
}

/**
 * 每个枚举都是枚举类的实例，所以它们可以这样初始化过的
 */
enum class Color(val rgb:Int){
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF)
}

/***
 * 枚举常量还可以声明其带有相应方法以及覆盖了基类方法的匿名类
 */
enum class ProtocolState{
    WAITING {
        override fun signal() = TALKING
    },

    TALKING {
        override fun signal() = WAITING
    };
    abstract fun signal(): ProtocolState
}