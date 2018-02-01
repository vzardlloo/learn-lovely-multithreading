package chapter8;


import java.util.concurrent.CyclicBarrier;


/**
 * CyclicBarrier的字面意思是可循环使用（Cyclic）的屏障（Barrier）。它要做的事情是，让一
 * 组线程到达一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，屏障才会
 * 开门，所有被屏障拦截的线程才会继续运行。说白了就是要等人到齐了在开动
 */
public class CyclicBarrierDemo {

    static CyclicBarrier c = new CyclicBarrier(2);

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    c.await();
                } catch (Exception e) {
                    e.getStackTrace();
                }
                System.out.println(1);
            }

        }).start();
        try {
            c.await();
        } catch (Exception e) {
            e.getStackTrace();
        }
        System.out.println(2);
    }


}
