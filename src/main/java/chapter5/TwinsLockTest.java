package chapter5;


import kit.SleepUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 利用同步器自定义锁
 */
public class TwinsLockTest {
    static final Lock twinsLock = new TwinsLock();
    static CountDownLatch start = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 100; i++) {
            Worker worker = new Worker();
            worker.setDaemon(true);
            worker.start();
        }
        start.countDown();
        for (int i = 0; i < 100; i++) {
            SleepUtils.second(1);
            System.out.println();
        }

    }

    static class Worker extends Thread {

        @Override
        public void run() {
            try {
                start.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                twinsLock.lock();
                try {
                    SleepUtils.second(1);
                    System.out.println(Thread.currentThread().getName());
                    SleepUtils.second(1);
                } finally {
                    twinsLock.unlock();
                }
            }
        }
    }
}

