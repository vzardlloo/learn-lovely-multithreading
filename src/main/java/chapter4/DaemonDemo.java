package chapter4;


import java.util.concurrent.TimeUnit;

public class DaemonDemo {

    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner(), "DaeonRunner");
        thread.setDaemon(true);
        thread.start();
    }

    static class DaemonRunner implements Runnable {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("DaemonThread finally run.");
            }
        }
    }

}
