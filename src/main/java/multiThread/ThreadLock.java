package multiThread;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: ThreadLock
 * @Description: 线程锁练习
 * @Author: gx
 * @Date: 2021/1/27 9:54
 * @Version: 1.0
 */
public class ThreadLock {

    /***
     * @Description: 可重入锁测试
     * @Author: gx
     * @Date: 2021/1/27 9:58
     **/
    @Test
    public void ReentrantLockTest() throws InterruptedException {

        /**
         * ReentrantLock 总结：
         * 1，ReentrantLock可以替代synchronized进行同步；
         * 2，ReentrantLock获取锁更安全；
         * 3，必须先获取到锁，再进入try {...}代码块，最后使用finally保证释放锁；
         * 4，可以使用tryLock()尝试获取锁。
         */
        Counter5 counter = new Counter5();

        Thread addThread = new Thread(() -> counter.add());
        Thread decThread = new Thread(() -> counter.dec());

        addThread.start();
        decThread.start();
        System.out.println("waiting...");

        addThread.join();
        decThread.join();

        System.out.println("count:" + counter.count);
        System.out.println("end...");
    }
}

class Counter5{
    private final Lock lock = new ReentrantLock();
    public  int count = 0;

//    public void add(){
//        lock.lock();
//        try {
//            this.count += 1;
//        } finally {   // 确保无论如何都会正确释放锁
//            lock.unlock();
//        }
//    }
//
//    public void dec(){
//        lock.lock();
//        try {
//            this.count -= 1;
//        } finally { // 确保无论如何都会正确释放锁
//            lock.unlock();
//        }
//    }


    public void add() {
        lock.lock();
        try {
            this.count += 1;
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {   // 确保无论如何都会正确释放锁
            lock.unlock();
        }
    }

    public void dec(){
        /**
         *  ReentrantLock是可重入锁，它和synchronized一样，一个线程可以多次获取同一个锁。
         *  和synchronized不同的是，ReentrantLock可以尝试获取锁。
         */
        try {
            if(lock.tryLock(3, TimeUnit.SECONDS)){ // 尝试获取锁，等待3秒后，获取不到就去执行其他逻辑
                    this.count -= 1;
            }else {
                System.out.println("没有获取到锁，不减了，执行了其他逻辑");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
