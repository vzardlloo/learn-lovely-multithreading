package chapter8;


import java.util.concurrent.*;

public class ExchangerDemo {

    private static final Exchanger<String> exgr = new Exchanger<>();
    private static ExecutorService threadPool = Executors.newFixedThreadPool(2);
    //static CyclicBarrier c= new CyclicBarrier(3);

    public static void main(String[] args) {

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //c.await();
                    //A录入一行流水
                    String A = "银行流水A";
                    A = exgr.exchange(A);
                    System.out.println(A);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // c.await();
                    String B = "银行流水B";
                    String A = exgr.exchange(B);
                    System.out.println("A和B数据是否一致：" + A.equals(B) + ",A录入的是：" + A + ",B的录入是：" + B);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //c.await();
//                    String C = "银行流水C";
//                    String D = exgr.exchange(C);
//                    System.out.println(D);
//                }catch (InterruptedException  e){
//                    e.printStackTrace();
//                }
//            }
//        });

        threadPool.shutdown();
    }
}
