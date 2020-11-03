package com.wys.interview.designpattern.part1.singleton;

/**
 * @author wangyasheng
 * @date 2020/11/3
 * @Describe:单例
 * intent：确保一个累只有一个实例，并提供实例的全局访问点。
 */
public class Singleton {

    private Singleton(){}
    /**
     * Ⅰ 懒汉式-线程不安全
     * 私有静态变量instance被延迟实例化，好处是，如果没有用到该类，
     * 那么就不会实例化instance，从而节约资源。
     * 这种实现在多线程环境下是不安全的，如果多个线程能够同时进去if
     * 语句，并且此时instance为null，那么会有多个线程执行new语句，
     * 导致实例化多次instance。
     */
    private volatile static Singleton instance;
    public static Singleton getInstance(){
        if (instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    /**
     * Ⅱ 饿汉式-线程安全
     * 单例的线程不安全问题只要是由于instance被实例化多次，
     * 采用直接实例化instance的方式就不会产生线程不安全问题。
     * 但是直接实例化的方式也丢失了延迟实例化带来的节约资源的好处。
     */
    private static Singleton instance2 = new Singleton();
    public static Singleton getInstance2(){
        return instance2;
    }

    /**
     * Ⅲ 懒汉式-线程安全
     * 对getInstance()方法加锁，那么在一个时间点只能一个线程
     * 能够入该方法，从而避免了实例化多次instance。
     * 但是当一个线程进去该方法之后，其它试图进入该方法的线程都必须等待，
     * 即使instance已经被实例化了。这会让线程阻塞时间过长，因此该方法
     * 有性能问题，不推荐使用。
     */
    public static synchronized Singleton getInstance3(){
        if (instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    /**
     * Ⅳ 双重校验锁-线程安全
     * instance只需要被实例化一次，之后就可以直接使用了。所以加锁
     * 操作只需要对实例化那部分的代码进行，只有当instance没有被
     * 实例化时，才需要进行加锁。
     *
     * 双重校验锁先判断instance是否已经被实例化，如果没有被实例化，
     * 那么才对实例化语句进行加锁。
     *
     * 如果只使用一个if语句，在instance==null的情况下，如果两个线程
     * 都执行了if语句，那么两个线程都会进入if语句块内。虽然if语句快内
     * 有加锁操作，但是两个线程都会执行new Singleton()语句，只是先后我哪天。
     * 那么就会进行两次实例化。因此必须用双重校验锁：第一个if语句用来避免instance
     * 已经被实例化之后的加锁操作，第二个if语句进行了加锁，只能一个线程进入，就
     * 不会出现instance == null时两个线程同时进行实例化操作。
     *
     * instance采用volatile关键字修饰也是很有必要的，instance = new Singleton()
     * 这段代码其实是分为三步执行：
     * 1、为instance分配内存空间
     * 2、初始化instance
     * 3、将instance指向分配的内存地址
     * 但是由于JVM具有指令重排的特性，执行顺序有可能变成1->3->2。指令
     * 重排在单线程环境下不会出现问题，但是在多线程环境下会导致一个线程获得
     * 还没初始化的实例。
     * 例如，线程T1执行了1和3，此时T2调用getInstance()后发现instance
     * 不为空，因此返回instance，但此时instance还未被初始化。
     *
     * 使用volatile可以禁止JVM的指令重排，保证在多线程环境下也能正常运行。
     */
    public static Singleton getInstance4(){
        if (instance == null){
            synchronized (Singleton.class){
                if (instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    /**
     * Ⅴ 静态内部类实现
     * 当Singleton类被加载时，静态内部类SingletonHolder没有被加载进内存。
     * 只有当调用getInstance()方法从而触发SingletonHolder.INSTANCE时
     * SingletonHolder才会被加载，此时初始化INSTANCE实例，并且JVM能确保
     * INSTANCE只被实例化一次。
     *
     * 这种方式不仅具有延迟初始化的好处，而且由JVM提供了对线程安全的支持。
     */
    private static class SingletonHolder{
        private static final Singleton INSTANCE = new Singleton();
    }
    public static Singleton getInstance5(){
        return SingletonHolder.INSTANCE;
    }
}
