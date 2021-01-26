package multiThread;

/**
 * @ClassName: ThreadDeadlock
 * @Description: 线程死锁
 * @Author: gx
 * @Date: 2021/1/26 19:37
 * @Version: 1.0
 */
public class ThreadDeadlock {
    /**
     * 1，Java的synchronized锁是可重入锁；
     * 2，死锁产生的条件是多线程各自持有不同的锁，并互相试图获取对方已持有的锁，导致无限等待；
     * 3，避免死锁的方法是多线程获取锁的顺序要一致。
     */
    public static void main(String[] args) throws InterruptedException {
        Thread thread01 = new Thread(() -> add());
        Thread thread02 = new Thread(() -> dec());
        thread01.start();
        thread02.start();
        System.out.println("waiting...");
        thread01.join();
        thread02.join();
        System.out.println("end");
    }

    public static void add() {
        synchronized (Counter4.lockA){
            Counter4.count += 1;
            try {
                Thread.sleep(1000);  // 休息1S, 让另外一个线程获得LockB，以至于形成死锁
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (Counter4.lockB){
                Counter4.count += 2;
            }
        }
    }

    public static void dec() {
        synchronized (Counter4.lockB){
            Counter4.count -= 1;
            synchronized (Counter4.lockA){
                Counter4.count -= 2;
            }
        }
    }
}

class Counter4{
    public static Object lockA = new Object();
    public static Object lockB = new Object();

    public static int count;
}