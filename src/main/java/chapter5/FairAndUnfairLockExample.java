package chapter5;


import jdk.nashorn.internal.scripts.JO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 公平锁与非公平锁
 */
public class FairAndUnfairLockExample {
    //公平锁
    private static FairOrUnfairLock fairLock = new FairOrUnfairLock(true);
    //非公平锁
    private static FairOrUnfairLock unfairLock = new FairOrUnfairLock(false);

    static CountDownLatch start = new CountDownLatch(1);

    public static void main(String[] args) {
        startJobs(unfairLock);
        start.countDown();
        //startJobs(unfairLock);
    }

    private static void startJobs(FairOrUnfairLock lock) {
        for (int i = 0; i < 200; i++) {
            Job job = new Job(lock);
            job.start();
            //
        }
    }

    private static class Job extends Thread {
        private FairOrUnfairLock lock;

        public Job(FairOrUnfairLock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            try {
                start.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                lock.lock();

                System.out.printf("Lock by [ " + currentThread().getName() + " ],");
                System.out.printf("Waiting by " + lock.getQueuedThreads());
                System.out.println();
            } finally {
                lock.unlock();
            }
        }
    }


}

class FairOrUnfairLock extends ReentrantLock {
    public FairOrUnfairLock(boolean fair) {
        super(fair);
    }

    public List getQueuedThreads() {
        List<Thread> arrayList = new ArrayList<Thread>(
                super.getQueuedThreads()
        );
        return arrayList.stream().map(Thread::getName).collect(Collectors.toList());

    }
}
