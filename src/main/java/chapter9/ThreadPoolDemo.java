package chapter9;


import java.util.concurrent.*;

public class ThreadPoolDemo {

    /**
     * 创建一个固定长度的线程池，每次提交任务就会创建线程，知道达到最大线程数。如果线程发生Exception死掉，会新补充线程进来。
     * 默认工作队列最大长度是Integer.MXA_VALUE。认为是一个无界的队列
     */
    static Executor executor1 = Executors.newFixedThreadPool(100);

    /**
     * 创建一个可缓存的线程池，如果线程池的当前规模超出了处理需求，就回收空闲线程，如果需求增加就添加新的线程。
     * 线程值规模不受限制，所以在使用的时候，操作不当可能创建很多线程导致OOM。
     * 使用的队列是SynchronousQueue
     */
    static Executor executor2 = Executors.newCachedThreadPool();

    /**
     * 创建固定长度线程池，而且以延迟或定时的方式来执行任务
     */
    static Executor executor3 = Executors.newScheduledThreadPool(100);
    /**
     * 创建一个单线程的Executor，如果单个线程出现Exeception死掉，就是创建一个线程来替代。
     * 他可以确保任务队列中的任务是顺序执行的。
     */
    static Executor executor4 = Executors.newSingleThreadExecutor();

    static ExecutorService executor5 = Executors.newFixedThreadPool(100);

    public static void main(String[] args) {
        executor1.execute(new Worker());
        executor2.execute(new Worker());
        executor3.execute(new Worker());
        executor4.execute(new Worker());
        executor5.execute(new Worker());
        executor5.submit(new Worker());

    }

    static class Worker implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is Running!");
        }
    }
}
