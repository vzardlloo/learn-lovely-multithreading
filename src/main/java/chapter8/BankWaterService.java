package chapter8;

import java.util.Map;
import java.util.concurrent.*;

/**
 * CyclicBarrier的应用场景:银行流水
 */
public class BankWaterService implements Runnable {

    //创建4个屏障，处理完之后执行当前类的run方法
    private CyclicBarrier c = new CyclicBarrier(4, this);
    //创建4个线程,假设只有4个sheet
    private Executor executors = Executors.newFixedThreadPool(4);
    //保存每个sheet计算出的银流结果
    private ConcurrentHashMap<String, Integer> sheetBankWaterCount = new ConcurrentHashMap<>();

    private void count() {
        for (int i = 0; i < 4; i++) {
            executors.execute(new Runnable() {
                @Override
                public void run() {
                    sheetBankWaterCount.put(Thread.currentThread().getName(), 2);

                    try {
                        c.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void run() {
        int result = 0;
        for (Map.Entry<String, Integer> sheet : sheetBankWaterCount.entrySet()) {
            result += sheet.getValue();
        }
        //将结果输出
        sheetBankWaterCount.put("result", result);
        System.out.println(result);
    }

    public static void main(String[] args) {
        BankWaterService bankWaterService = new BankWaterService();
        bankWaterService.count();
    }
}
