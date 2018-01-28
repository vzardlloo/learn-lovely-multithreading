package chapter3;


public class VolatileSyncExample {
    volatile long value = 0L;

    public void setValue(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void getAndIncrement() {
        value++;
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileSyncExample vse = new VolatileSyncExample();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(2000);
                    System.out.println(Thread.currentThread().getName() + " set value");
                    vse.setValue(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ": " + vse.getValue());
            }
        });

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(2000);
                    System.out.println(Thread.currentThread().getName() + " increment");
                    vse.getAndIncrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        });

        thread1.start();
        thread1.join();
        thread3.start();
        thread3.join();
        thread2.start();


    }
}
