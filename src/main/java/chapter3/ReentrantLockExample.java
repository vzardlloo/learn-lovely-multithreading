package chapter3;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReetrantLock用例
 */
public class ReentrantLockExample {
    int a = 0;
    ReentrantLock lock = new ReentrantLock();

    public void write() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " increment");
            a++;
        } finally {
            lock.unlock();
        }
    }

    public int reader() {
        lock.lock();
        try {
            int i = a;
            System.out.println(Thread.currentThread().getName() + " get: " + i);
            return i;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockExample rle = new ReentrantLockExample();
        Long start = System.currentTimeMillis();
        List<Thread> threadList = new ArrayList<Thread>(600);
        for (int i = 0; i < 200; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    rle.write();
                }
            });
            threadList.add(t);
        }

        for (Thread t : threadList) {
            t.start();
            t.join();
        }

        Long end = System.currentTimeMillis() - start;

        System.out.println("time: " + end);
        System.out.println("result: " + rle.reader());
    }

}
