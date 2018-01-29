package chapter4;


import java.util.concurrent.TimeUnit;

/**
 * 线程通过检查自身是否被中断来进行响应，线程通过方法isInterrupted()来进行判断是否 被中断，
 * 也可以调用静态方法Thread.interrupted()对当前线程的中断标识位进行复位。如果该 线程已经处于终结状态，
 * 即使该线程被中断过，在调用该线程对象的isInterrupted()时依旧会返 回false。
 */
public class Interrupted {

    public static void main(String[] args) throws Exception {
        Thread sleepThread = new Thread(new SleepRunner(), "sleepThread");
        sleepThread.setDaemon(true);
        Thread busyThread = new Thread(new BusyRunner(), "busyRunner");
        busyThread.start();
        sleepThread.start();
        //休眠5s让两个线程充分运行
        TimeUnit.SECONDS.sleep(5);
        sleepThread.interrupt();
        busyThread.interrupt();
        System.out.println("Sleep interrupted is " + sleepThread.isInterrupted());
        System.out.println("Busy interrupted is " + busyThread.isInterrupted());

        TimeUnit.SECONDS.sleep(3);


    }


    /**
     * 一直sleep的线程
     */
    static class SleepRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 一直运行的线程
     */
    static class BusyRunner implements Runnable {
        @Override
        public void run() {
            while (true) {

            }
        }
    }

}
