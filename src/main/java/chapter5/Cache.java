package chapter5;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cache {
    static Map<String, Object> map = new HashMap<>();
    static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    static Lock r = rwl.readLock();
    static Lock w = rwl.writeLock();
    static Random random = new Random();
    static CountDownLatch start = new CountDownLatch(1);

    //读操作
    public static final Object get(String key) {
        r.lock();
        try {
            return map.get(key);
        } finally {
            r.unlock();
        }
    }

    //写操作
    public static final Object put(String key, Object value) {
        w.lock();
        try {
            return map.put(key, value);
        } finally {
            w.unlock();
        }

    }

    //也是写操作
    public static final void clear() {
        w.lock();
        try {
            map.clear();
        } finally {
            w.lock();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Wirter wirter = new Wirter();
            Reader reader = new Reader();
            wirter.start();
            reader.start();
        }
        start.countDown();
        System.out.println("当前读锁被获取的次数： " + rwl.getReadLockCount());
        System.out.println("当前写锁被获取的次数： " + rwl.getWriteHoldCount());


    }

    static class Wirter extends Thread {
        @Override
        public void run() {
            try {
                start.await();
                put(String.valueOf(random.nextInt(10)), random.nextInt(10));
                sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Reader extends Thread {
        @Override
        public void run() {
            try {
                start.await();
                System.out.println("read : " + get(String.valueOf(random.nextInt(10))));
                sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
