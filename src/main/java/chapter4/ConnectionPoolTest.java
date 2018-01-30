package chapter4;


import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPoolTest {
    static ConnectionPool pool = new ConnectionPool(15);

    static CountDownLatch start = new CountDownLatch(1);

    static CountDownLatch end;

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        int threadCount = 20;
        int count = 250;
        end = new CountDownLatch(threadCount);
        //default 0
        AtomicInteger got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();
        end = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new ConnetionRunner(count, got, notGot), "ConnectionRunnerThread-" + i);
            thread.start();
        }
        //锁存器倒数为0,所有线程开始一起执行
        start.countDown();
        //在每个线程都会使end自减，在所有线程没有执行完毕的情况下，一直等待
        end.await();
        //一共发起threadCount个线程，每个线程发起count次连接
        System.out.println("total invoke connection: " + (threadCount * count));
        System.out.println("got connection: " + got);
        System.out.println("not got connection: " + notGot);
        System.out.println("total time: " + (System.currentTimeMillis() - startTime) + " ms");


    }

    static class ConnetionRunner implements Runnable {
        //控制每个线程发起的连接数量
        int count;
        AtomicInteger got;
        AtomicInteger notGot;

        public ConnetionRunner(int count, AtomicInteger got, AtomicInteger notGot) {
            this.count = count;
            this.got = got;
            this.notGot = notGot;
        }

        @Override
        public void run() {
            try {
                //在锁存器倒数为0之前一直等待
                start.await();
            } catch (Exception e) {
                e.getStackTrace();
            }
            while (count > 0) {
                try {
                    Connection connection = pool.fetchConnection(1000);
                    if (connection != null) {
                        try {
                            connection.createStatement();
                            connection.commit();
                        } finally {
                            System.out.println("current pool size: " + pool.size());
                            pool.releaseConnection(connection);

                            //原子方式自增1，用于统计成功获得连接的数量
                            got.incrementAndGet();
                        }
                    } else {
                        //用于统计未获取连接的数量
                        notGot.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                } finally {
                    count--;
                }
            }
            end.countDown();
        }
    }


}
