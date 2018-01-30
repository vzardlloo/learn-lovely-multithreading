package chapter4;


public class CreateThread {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("I'am a Thread");
        });

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("I'am also a thread");
            }
        });

        thread.start();
        thread1.start();
    }


}
