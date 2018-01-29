package chapter4;

/**
 * join的示例
 */
public class JoinExample {

    public static void main(String[] args) {
        Thread previous = Thread.currentThread();
        //System.out.println(previous.getName()+"---");
        for (int i = 0; i < 10; i++) {

            Thread thread = new Thread(new Domino(previous), String.valueOf(i));
            thread.start();
            previous = thread;
            //System.out.println("--"+previous.getName()+"--");
        }
        SleepUtils.second(5);
        System.out.println(Thread.currentThread() + " terminate");
    }

    static class Domino implements Runnable {
        private Thread thread;

        public Domino(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " terminate");

        }


    }

}
