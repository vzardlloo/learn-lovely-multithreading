package chapter1;

/**
 * 死锁示例
 */
public class DeadLockDemo {

    private static String A = "A";
    private static String B = "B";

    public static void main(String[] args) {
        new DeadLockDemo().deadLock();
    }

    private void deadLock() {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                //当前持有A的锁
                synchronized (A) {
                    try {
                        Thread.currentThread().setName("thread-1");
                        Thread.currentThread().sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //尝试获取B的锁
                    synchronized (B) {
                        System.out.println(Thread.currentThread().getName() + " try to get B");
                    }
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                Thread.currentThread().setName("thread-2");
                //当前持有B的锁
                synchronized (B) {
                    //尝试获取A的锁
                    synchronized (A) {
                        System.out.println(Thread.currentThread().getName() + " try to get A");
                    }
                }
            }
        });

        t1.start();
        t2.start();
    }


}
