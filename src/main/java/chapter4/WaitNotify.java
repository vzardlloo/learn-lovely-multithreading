package chapter4;


import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 等待/通知机制：一个对象尝试获取对象监视器，获取失败则进入同步队列等待，当持有对象的线程调用对象的notify()方法，并等持有对象的线程
 * 放弃锁后，再次尝试获取对象监视器，已经持有对象的线程若调用对象的wait()方法，释放持有的对象的锁，则进入等待队列，
 * 稍后迁移到同步队列等待其他线程调用notify()唤醒
 */
public class WaitNotify {
    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String[] args) {
        Thread waitThread = new Thread(new Wait(), "WaitThread");
        waitThread.start();
        SleepUtils.second(1);
        Thread notifyThread = new Thread(new Notify(), "NotifyThread");
        notifyThread.start();
    }

    static class Wait implements Runnable {


        @Override
        public void run() {
            //对lock加锁
            synchronized (lock) {
                while (flag) {
                    try {
                        System.out.println(Thread.currentThread() + " flag is true.wait@ " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread() + "flag is false. running@" + new SimpleDateFormat("HH:mm:ss").format(new Date()));

            }
        }
    }

    static class Notify implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {

                System.out.println(Thread.currentThread() + " hold lock notify @ " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                flag = false;
                lock.notifyAll();
                SleepUtils.second(5);
            }

            synchronized (lock) {
                System.out.println(Thread.currentThread() + " hold lock again @" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                SleepUtils.second(5);
            }
        }
    }


}
