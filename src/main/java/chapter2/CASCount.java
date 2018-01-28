package chapter2;


import org.w3c.dom.css.Counter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CASCount {

    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private int i = 0;

    public static void main(String[] args) {
        final CASCount cas = new CASCount();
        List<Thread> ts = new ArrayList<Thread>(600);
        Long satrt = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10000; j++) {
                        cas.count();
                        cas.safeCount();
                    }
                }
            });
            ts.add(t);
        }
        //执行所有线程
        for (Thread t : ts) {
            t.start();
        }
        //等待所有线程执行完毕
        for (Thread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                ;
            }
        }
        System.out.println(cas.i);
        System.out.println(cas.atomicInteger.get());
        System.out.println(System.currentTimeMillis() - satrt);

    }

    /**
     * cas实现的程序安全计数器
     */
    private void safeCount() {
        for (; ; ) {
            //获取当前值
            int i = atomicInteger.get();
            //如果当前值 == 预期值 则更新到新值，i预期值，++i新值
            boolean suc = atomicInteger.compareAndSet(i, ++i);
            if (suc) {
                break;
            }
        }
    }

    /**
     * 非线程安全计数器
     */
    private void count() {
        ++i;
    }
}
