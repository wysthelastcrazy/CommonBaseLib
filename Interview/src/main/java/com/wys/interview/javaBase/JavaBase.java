package com.wys.interview.javaBase;

import android.os.Build;
import android.util.Log;

import com.wys.interview.OuterClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.BiConsumer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * @author wangyasheng
 * @date 2020/8/5
 * @Describe:
 */
class JavaBase{
    private static final String TAG = "JavaBase";
    public static void test(){
        testInteger();
    }

    private static void testInteger(){
        //装箱 调用了Integer.valueOf(2)
        Integer x = 2;
        //拆箱 调用了 x.initValue()
        int y = x;

        /**
         * new Integer(123)每次都会创建一个对象；
         * Integer.valueOf(123)会使用缓存池中的对象，对此调用会取得同一个对象的引用
         */
        //创建新对象
        int i = new Integer(123);
        //在缓存池中获取，java8中，Integer缓存池大小默认为-128～127
        int j = Integer.valueOf(123);

        Integer x1 = new Integer(123);
        Integer y1 = new Integer(123);
        //false
        System.out.println(x1 == y1);

        Integer x2 = Integer.valueOf(123);
        Integer y2 = Integer.valueOf(123);
        //true
        System.out.println(x2 == y2);

        Integer x3 = Integer.valueOf(200);
        Integer y3 = Integer.valueOf(200);
//        Integer.valueOf("1");
//        Integer.parseInt("1");
        //false 200不在Integer缓存池中
        System.out.println(x3 == y3);

        Integer m = 123;
        Integer n = 123;
        //true 编译器会在自动装箱过程中调用valueOf()方法
        System.out.println(m == n);

        short s = 1;
        //Java 不能隐式执行向下转型，因为这会是精度降低。
//        float f = 1.1; //错误,1.1字面量属于double类型，不能直接赋值给float变量
        float f = 1.1f;
    }

    private static void testString(){
        /**
         * Java8中，String内部使用char数组存储数据；
         * Java9之后，String类的实现改用byte数组存储字符串，同时
         * 使用coder来标示使用了那种编码。
         */

        /**
         * String Pool
         *
         */
        String s = "abc";

        //非静态内部类依赖于外部类的实例，需要先创建外部类实例才能用
        //这个实例去创建非静态内部类实例，而静态内部类不需要。
        OuterClass outerClass = new OuterClass();
        OuterClass.InnerClass innerClass = outerClass.new InnerClass();
        //静态内部类不能访问外部类的非静态变量和方法
        OuterClass.StaticInnerClass staticInnerClass = new OuterClass.StaticInnerClass();

        /**
         * 初始化顺序
         *
         * 父类（静态变量、静态语句块）
         * 子类（静态变量、静态语句块）
         * 父类（实例变量、普通语句块）
         * 父类（构造函数）
         * 子类（实例变量、普通语句块）
         * 子类（构造函数）
         * 其中静态变量和静态语句块的初始化顺序，取决于它们在代码中的顺序。
         */


        /**
         *
         */
    }

    private static void testThread(){
        MyThread thread = new MyThread();
        thread.start();
        thread.interrupt();
    }
    private static class MyThread extends Thread{
        @Override
        public void run() {
            try {
                sleep(1000);
                Log.d("wys","[run]+++");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
class EqualExample implements Cloneable{
    private int x;
    private int y;
    private int z;

    public EqualExample(int x,int y,int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 自反性：对于任何非空引用值x，x.equals(x)都应该返回true；
     * 对称性：对于任何非空引用值x和y，当且仅当y.equals(x)返回true时x.equals(y)才能返回true；
     * 传递性：对于任何非空引用值x、y和z，如果x.equals(y)=true，并且y.equals(z)=true，那么x.equals(z)应该为true；
     * 一致性：对于任何非空引用值x和y，多次调用x.equals(y)始终返回相同结果，前提是对象上equals比较中所用的信息没有被修改；
     * 非空性：对于任何非空应用值x，x.equals(null)都应返回false。
     * @param o
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean equals(Object o) {
        HashMap<String,String> map = new HashMap<>();
        map.size();
        map.forEach((key, value) -> {
        });
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EqualExample that = (EqualExample) o;
        return x == that.x &&
                y == that.y &&
                z == that.z;
    }

    /**
     * hashCode()返回哈希值，而equals判断对象是否等价。
     * 等价的两个对象哈希值一定相等，哈希值相等的两个对象，
     * 不一定等价。
     *
     * 在覆盖equals()方法是应当总是覆盖hashCode()，保证
     * 等价的两个对象的哈希值也相等。
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    /**
     * 默认返回EqualExample@4554617c这种形式，
     * 其中@后面的数值为散列码的无符号十六进制表示。
     * @return
     */
    @Override
    public String toString() {
        return "EqualExample{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    /**
     * 只有实现了Cloneable接口的类，才可以调用此方法，
     * 否则会抛出CloneNotSupportedException异常
     * @return
     * @throws CloneNotSupportedException
     */
    @NonNull
    @Override
    protected EqualExample clone() throws CloneNotSupportedException {
        return (EqualExample) super.clone();
    }
}
