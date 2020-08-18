package com.wys.interview.javaBase;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangyasheng
 * @date 2020/8/14
 * @Describe:
 */
public class ThreadNote {
    private final String TAG = "ThreadNote";

    /**
     * 使用线程的三种方法
     * - 实现Runnable接口；
     * - 实现Callable接口；
     * - 继承Thread类。
     *
     * 实现Runnable和Callable接口的类只能当作一个可以在线程中运行的任务，
     * 不是真正意义上的线程，因此最后还需要通过Thread来调用。可以理解为任务
     * 是通过线程驱动从而执行的。
     *
     * 三种使用线程的方法中，实现接口更好一些，因为：
     * - Java不支持多重继承，因此继承了Thread类就无法继承其它类。但是Java
     *   可以实现多个接口；
     * - 类可能只要求可执行就行，继承整个Thread类开销过大。
     */

    /**
     * 实现Runnable接口
     */
    public class MyRunnable implements Runnable{
        @Override
        public void run() {
            //...
        }
    }

    /**
     * 实现Callable接口
     * 与Runnable相比，Callable可以有返回值，返回值通过
     * FutureTask进行封装
     */
    public class MyCallable implements Callable<Integer>{
        @Override
        public Integer call() throws Exception {
            return 123;
        }
    }

    /**
     * 继承Thread类
     * 同样需要实现run()方法，因为Thread类也实现了Runnable接口。
     * 当调用start()方法启动一个线程时，虚拟机会将该线程放入就绪
     * 队列中等待被调度，当一个线程被调度时会执行该线程的run()方法。
     */
    public class MyThread extends Thread{
        @Override
        public void run() {
            //...
        }
    }

    public void test(){
        //使用Runnable实例再创建一个Thread实例，然后调用Thread实例的start()
        //来启动线程
        MyRunnable runnable = new MyRunnable();
        Thread thread = new Thread(runnable);
        thread.start();

        //使用Callable实例启动线程
        MyCallable callable = new MyCallable();
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        Thread thread1 = new Thread(futureTask);
        thread1.start();
        try {
            Log.d(TAG,"[test] i:"+futureTask.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //继承Thread启动线程
        MyThread thread2 = new MyThread();
        thread2.start();
    }

    /**
     * 基础线程机制
     *
     * Executor
     * Executor管理多个异步任务的执行，而无需开发人员显式的管理线程的声明周期。
     * 这里的异步是指多个任务的执行互不干扰，不需要进行同步操作。
     *
     * 主要有三种Executor：
     * - CachedThreadPoll：一个任务创建一个线程；
     * - FixedThreadPool：所有任务只能使用固定大小的线程；
     * - SingleThreadExecutor：相当于大小为1的FixedThreadPool。
     */

    public void test2(){
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        for (int i = 0;i < 5; i++){
//            executorService.execute(new MyRunnable());
//        }
//        executorService.shutdown();

        /**
         * 推荐使用ThreadPoolExecutor的方式创建线程池，这样的处理
         * 方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
         * Executors各个方法的弊端：
         * - newFixedThreadPool和newSingleThreadExecutor：
         *   主要问题是堆积的请求处理队列可能会耗费非常大的内存，甚至OOM。
         * - newCachedThreadPool和newScheduledThreadPoll：
         *   主要问题是线程数最大数是Integer.MAX_VALUE，可能会创建数量
         *   非常多的线程，甚至OOM。
         *
         * ThreadPoolExecutor参数解释：
         *
         * corePoolSize & maximumPoolSize：核心线程数和最大线程数。
         * 当一个新任务被提交到池中，如果当前运行线程小于核心线程数，即使
         * 当前有空闲线程，也会新建一个线程来处理新提交的任务；如果当前运
         * 行线程数大于核心线程数并小于最大线程数，只有当等待队列已满的情
         * 况下才会新建线程。
         *
         * keepAliveTime & unit：超过corePoolSize线程数量的线程最大
         * 空闲时间，unit为时间单位。
         *
         * workQueue：等待队列
         *
         */

        int corePoolSize = 3;
        int maximumPoolSize = 6;
        long keepAliveTime = 2;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(2);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue);
        for (int i = 0;i < 5; i++){
            pool.execute(new MyRunnable());
        }
        pool.shutdown();
    }

    /**
     * 互斥同步
     *
     * Java提供了两种锁机制来控制多个线程对共享资源的互斥访问，第一个是
     * JVM实现的synchronized，另一个是JDK实现的ReentrantLock。
     */
    /**
     * synchronized
     */
    public class SynchronizedExample{
        public void func1(){
            /**
             * 它只作用于一个对象，如果调用两个对象上的同步代码块，就不会进行同步。
             * 两个线程调用同一个对象时，这两个线程会进行同步，当一个线程进入同步
             * 语句块时，另一个线程必须等待
             */
            synchronized (this){
                for (int i = 0; i< 10 ;i ++){
                    Log.d(TAG,"[func1] i:"+i);
                }
            }
        }
    }
    public void test3(){
        SynchronizedExample e1 = new SynchronizedExample();

        int corePoolSize = 3;
        int maximumPoolSize = 6;
        long keepAliveTime = 2;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(2);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue);

        pool.execute(()->e1.func1());
        pool.execute(()->e1.func1());
    }
}
