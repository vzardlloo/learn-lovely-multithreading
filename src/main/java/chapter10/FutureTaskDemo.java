package chapter10;


import java.util.concurrent.*;

public class FutureTaskDemo {
    static ExecutorService executor = Executors.newFixedThreadPool(10);
    static Worker work1 = new Worker(1000);
    static Worker work2 = new Worker(2000);

    public static void main(String[] args) {

        /**
         * 可以先获取Future再Executor.executor()，也可以先Executor.submit()再Future.run()
         * 区别在于前者的执行线程是线程池中的，而后者的执行线程是主线程
         */
        FutureTask<String> result1 = new FutureTask<String>(work1);
        FutureTask<String> result2 = new FutureTask<String>(work2);


        while (true) {
            try {
//            result1.run();
//            result2.run();
                executor.execute(result1);
                executor.execute(result2);
                if (result1.isDone() && result2.isDone()) {
                    System.out.println("all task is done!");
                    System.out.println("result1: " + result1.get());
                    System.out.println("result2: " + result2.get());
                    executor.shutdown();
                    break;
                }
                if (!result1.isDone()) {
                    System.out.println("result1 not done yet!");
                }
                if (!result2.isDone()) {
                    System.out.println("result2 not done yet!");
                }

            } catch (InterruptedException | ExecutionException e) {
                e.getStackTrace();
            }

        }

    }


}

class Worker implements Callable<String> {
    private long waitTime;

    public Worker(long waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(waitTime);
        return Thread.currentThread().getName();
    }
}
