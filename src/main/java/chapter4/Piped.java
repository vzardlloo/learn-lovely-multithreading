package chapter4;


import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * 线程间的数据传输：管道
 */
public class Piped {

    public static void main(String[] args) throws Exception {
        PipedWriter out = new PipedWriter();
        PipedReader in = new PipedReader();
        out.connect(in);
        Thread printThread = new Thread(new Print(in), "PrintThread");
        printThread.start();
        printThread.sleep(20000);
        int receive = 0;
        while ((receive = System.in.read()) != -1) {
            out.write(receive);
        }
    }

    static class Print implements Runnable {
        private PipedReader in;

        public Print(PipedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            int receive = 0;
            try {
                while ((receive = in.read()) != -1) {
                    System.out.print((char) receive);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
