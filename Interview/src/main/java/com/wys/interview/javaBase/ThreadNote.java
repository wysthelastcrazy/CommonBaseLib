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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangyasheng
 * @date 2020/8/14
 * @Describe:
 */
public class ThreadNote {
    private final String TAG = "ThreadNote";

    /**
     * 一、使用线程的三种方法
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
     * 二、基础线程机制
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
     * 三、互斥同步
     *
     * Java提供了两种锁机制来控制多个线程对共享资源的互斥访问，第一个是
     * JVM实现的synchronized，另一个是JDK实现的ReentrantLock。
     */
    /**
     * 1、synchronized
     */
    public static class SynchronizedExample{
        /**
         * 同步一个代码块：
         * 它只作用于一个对象，如果调用两个对象上的同步代码块，就不会进行同步。
         * 两个线程调用同一个对象时，这两个线程会进行同步，当一个线程进入同步
         * 语句块时，另一个线程必须等待
         */
        public void func1(){
            synchronized (this){
                for (int i = 0; i< 50 ;i ++){
                    Log.d("SynchronizedExample","[func1] i:"+i);
                }
            }
        }

        /**
         * 同步一个方法：
         * 和同步代码块一样，作用于同一个对象
         */
        public synchronized void func2(){
            for (int i = 0; i< 50 ;i ++){
                Log.d("SynchronizedExample","[func1] i:"+i);
            }
        }

        /**
         * 同步一个类：
         * 作用于整个类，也就是说两个线程调用同一个类的不同对象这种同步语句，
         * 也会进行同步。
         */
        public void func3(){
            synchronized (SynchronizedExample.class){
                for (int i = 0; i< 50 ;i ++){
                    Log.d("SynchronizedExample","[func1] i:"+i);
                }
            }
        }

        /**
         * 同步一个静态方法：
         * 作用于整个类。
         */
        public synchronized  static void func4(){
            for (int i = 0; i< 50 ;i ++){
                Log.d("SynchronizedExample","[func1] i:"+i);
            }
        }
    }

    /**
     * 2、ReentrantLock
     * ReentrantLock是java.util.concurrent包中的锁。
     */
    public class LockExample{
        private Lock lock = new ReentrantLock();
        public void func(){
            lock.lock();
            try {
                for (int i = 0; i < 50; i++) {
                    Log.d("LockExample","[func] i:"+i);
                }
            }finally {
                //确保释放锁，从而避免发生死锁。
                lock.unlock();
            }
        }

    }
    /**
     * 比较：
     * 1、锁的实现：
     * synchronized是JVM实现的，而ReentrantLock是JDK实现的。
     *
     * 2、性能：
     * 新版本Java对synchronized进行了很多优化，例如自旋锁等。两者性能大致相同。
     *
     * 3、等待可中断：
     * 当持有锁的线程长期不释放锁的时候，正在等待的线程可以选择放弃等待，改为处理
     * 其他事情。ReentrantLock可中断，而synchronized不行。
     *
     * 4、公平锁：
     * 公平锁是指多个线程在等待同一个锁时，必须按照申请锁的时间顺序来一次获得锁。
     * synchronized中的锁是非公平的，ReentrantLock默认情况下也是非公平的，
     * 但是也可以是公平的。
     *
     * 5、锁绑定多个条件：
     * 一个ReentrantLock可以同时绑定多个Condition对象。
     *
     * 使用选择：
     * 除非需要使用ReentrantLock的高级功能，否则优先使用synchronized。这是因为
     * synchronized是JVM实现的一种锁机制，JVM原生地支持它，而ReentrantLock
     * 不是所有JDK都支持。并且使用synchronized不用担心没有释放锁而导致死锁问题。
     * JVM会确保锁的释放。
     */
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


    /**
     * 四、线程之间的协作
     * 当多个线程可以一起工作去解决某个问题时，如果某些部分必须在其它部分
     * 之前完成，那么就需要对线程进行协调。
     */

    /**
     * join()
     * 在线程中调用另一个线程的join()方法，会将当前线程挂起，而不是忙等待，
     * 直到目标线程结束。
     */
    public class JoinExample{
        private class A extends Thread{
            @Override
            public void run() {
                Log.d("JoinExample","this is A");
            }
        }
        private class B extends Thread{
            private A a;
            B(A a){
                this.a = a;
            }
            @Override
            public void run() {
                try{
                    a.join();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                Log.d("JoinExample","this is B");
            }
        }

        public void test(){
            /**
             * 对于一下代码，虽然b线程先启动，但是因为b线程中调用了a线程的
             * join()方法，b线程会等待a线程结束才继续执行，
             * 因此最好保证a线程输出先于b线程。
             */
            A a = new A();
            B b = new B(a);
            b.start();
            a.start();
        }
    }

    /**
     * wait()、notify()、notifyAll()
     * 调用wait()使得线程等待某个条件满足，线程在等待时会被挂起，当其它线程的
     * 运行使得这个条件满足时，其它线程会调用notify()或者notifyAll()来唤醒
     * 挂起的线程。
     *
     * 它们都属于Object的一部分，而不是属于Thread。
     * 只能用在同步方法或者同步控制块中，否则会在运行时抛出IllegalMonitorStateException异常。
     *
     * 使用wait()挂起期间，线程会释放锁。这是因为，如果没有释放锁，那么其它线程
     * 就无法进入对象的同步方法或者同步控制块中，那么就无法执行notify()来唤醒挂起
     * 的线程，造成死锁。
     *
     * wait()和sleep()的区别：
     * wait()是Object的方法，而sleep()是Thread的静态方法；
     * wait()会释放锁，sleep()不会。
     */
    public class WaitNotifyExample{
        public synchronized void before(){
            Log.d("WaitNotifyExample","[before]++++++++");
            notifyAll();
        }
        public synchronized void after(){
            Log.d("WaitNotifyExample","[after]+++++++++");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("WaitNotifyExample","[after]+++++++");
        }
    }
    public void test4(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        WaitNotifyExample example = new WaitNotifyExample();
        executorService.execute(() -> example.after());
        executorService.execute(() -> example.before());
    }

    /**
     * await()、signal()、signalAll()
     * java.util.concurrent类库中提供了Condition类来实现线程之间的协调，
     * 可以在Condition上调用await()方法使线程等待，其它线程调用signal()
     * 或signalAll()方法唤醒等待的线程。
     *
     * 相比于wait()这种等待方式，await()可以指定等待的条件，因此更加灵活。
     */
    public class AwaitSignalExample{
        private Lock lock = new ReentrantLock();
        private Condition condition = lock.newCondition();

        public void before(){
            lock.lock();
            try {
                Log.d("AwaitSignalExample", "[before]++++++");
                condition.signalAll();
            }finally {
                lock.unlock();
            }
        }

        public void after(){
            lock.lock();
            Log.d("AwaitSignalExample", "[after]++++++");
            try {
                condition.await();
                Log.d("AwaitSignalExample", "[after]++++++");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }
    public void test5(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        AwaitSignalExample example = new AwaitSignalExample();
        executorService.execute(() -> example.after());
        executorService.execute(() -> example.before());
    }

    /**
     * 五、线程状态
     *
     * 1、新建（NEW）
     * 2、可运行（RUNABLE）
     * 3、阻塞（BLOCKED）
     * 4、无限期等待（WAITING）
     * 5、限期等待（TIMED_WAITING）
     * 6、死亡（TERMINATED）
     */
}
