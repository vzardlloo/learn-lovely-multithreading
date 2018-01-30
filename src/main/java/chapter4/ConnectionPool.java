package chapter4;


import java.sql.Connection;
import java.util.LinkedList;

public class ConnectionPool {
    private LinkedList<Connection> pool = new LinkedList<Connection>();

    //初始化线程池
    public ConnectionPool(int initialSize) {
        for (int i = 0; i < initialSize; i++) {
            pool.addLast(ConnectionDriver.createConnection());
        }
    }

    //用户释放连接，还给线程池,执行该方法的线程相当于生成者
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            synchronized (pool) {
                pool.add(connection);
                pool.notify();
            }
        }
    }

    public int size() {
        return pool.size();
    }

    //用户尝试从连接池获取连接，在mills内未能获取连接返回null，执行该方法的线程相当于生产者
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool) {
            System.out.println(Thread.currentThread().getName());
            //传统的模式，一直wait等待唤醒
            if (mills <= 0) {
                while (pool.isEmpty()) {
                    pool.wait();
                }
                return pool.removeFirst();
            } else {
                long future = System.currentTimeMillis() + mills;
                long remaining = mills;
                while (pool.isEmpty() && remaining > 0) {
                    pool.wait(remaining);
                    remaining = future - System.currentTimeMillis();
                }
                Connection result = null;
                if (!pool.isEmpty()) {
                    result = pool.removeFirst();
                }
                return result;
            }

        }
    }
}
