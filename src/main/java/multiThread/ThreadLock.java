package multiThread;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

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

    /***
     * @Description: condition 测试
     * @Author: gx
     * @Date: 2021/1/27 15:59
     * @Param: []
     * @Return: void
     **/
    @Test
    public void conditionTest() throws InterruptedException {

        /**
         * Condition 总结：
         * 1，Condition可以替代wait和notify；
         * 2，Condition对象必须从Lock对象获取。
         */
        TaskQueue2 taskQueue = new TaskQueue2();
        List<Thread> threadList = new ArrayList<>();

        for(int i=0; i<5; i++){
            Thread thread = new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "--" + taskQueue.getTask());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            threadList.add(thread);
        }

        Thread addTaskThread = new Thread(() -> {
            for(int i=0; i<5; i++){
                taskQueue.addTask("task" + i);
            }
        });
        addTaskThread.start();

        addTaskThread.join();

        Thread.sleep(1000);

        for(Thread thread : threadList){
            thread.interrupt();
        }

        System.out.println("end...");
    }

    /***
     * @Description: 读写锁测试
     * @Author: gx
     * @Date: 2021/1/27 18:07
     **/
    @Test
    public void readWriteLockTest() throws InterruptedException {
        /***
         * 读写锁总结：
         * 1，ReadWriteLock可以提高读取效率；
         * 2，ReadWriteLock只允许一个线程写入；
         * 3，ReadWriteLock允许多个线程在没有写入时同时读取；
         * 4，ReadWriteLock适合读多写少的场景。
         **/
        Counter6 counter = new Counter6();
        List<Thread> threadList = new ArrayList<>();
        for(int i=0; i<10; i++){
            Thread thread = new Thread(() -> counter.add());
            thread.start();
            threadList.add(thread);
        }
        for(int i=0; i<1000; i++){
            Thread thread = new Thread(() -> {
                System.out.println(counter.get());
            });
            thread.start();
            threadList.add(thread);
        }
        for (Thread thread : threadList){
            thread.join();
        }
        System.out.println("end...");
    }

    /***
     * @Description: stampedLock 测试
     * @Author: gx
     * @Date: 2021/1/29 18:11
     * @Param: []
     * @Return: void
     **/
    @Test
    public void stampedLockTest() throws InterruptedException {
        /**
         * stampedLock 总结：
         * 1，StampedLock提供了乐观读锁，可取代ReadWriteLock以进一步提升并发性能；
         * 2，StampedLock是不可重入锁。
         */
        Point point = new Point();
        List<Thread> threadList = new ArrayList<>();
        for(int i=0; i<10; i++){
            double x = i + 0.1;
            Thread thread = new Thread(() -> {
                point.move(x, 10 + x);
            });
            thread.start();
            threadList.add(thread);
        }
        for(int i=0; i<1000; i++){
            Thread thread = new Thread(() -> {
                System.out.println(point.distanceFromOrigin());
            });
            thread.start();
            threadList.add(thread);
        }
        for (Thread thread : threadList){
            thread.join();
        }
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

class Counter6 {
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock rLock = rwLock.readLock();
    private final Lock wLock = rwLock.writeLock();
    public int count = 0;

    public void add(){
        wLock.lock(); // 加写锁
        try {
            this.count += 1;
        } finally {   // 确保无论如何都会正确释放锁
            wLock.unlock(); // 释放写锁
        }
    }

    public int get(){
        rLock.lock();   // 加读锁
        try {
            return this.count;
        } finally { // 确保无论如何都会正确释放锁
            rLock.unlock();  // 释放读锁
        }
    }
}

class TaskQueue2{
    private final Lock lock = new ReentrantLock();
    // Condition对象必须从Lock对象获取。
    private final Condition condition = lock.newCondition();
    private Queue<String> queue = new LinkedList<>();

    public void addTask(String task){
        lock.lock();
        try {
            this.queue.add(task);
            // signalAll 相当于 synchronized 的 notifyAll方法，唤醒其他所有线程
            this.condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String getTask() throws InterruptedException {
        lock.lock();
        try {
            while (this.queue.isEmpty()) {
                // await 相当于 synchronized 的 wait 方法， 让线程等待
                this.condition.await();
            }
            return this.queue.remove();
        }  finally {
            lock.unlock();
        }
    }
}

class Point {
    private final StampedLock stampedLock = new StampedLock();

    private double x;
    private double y;

    public void move(double deltaX, double deltaY) {
        long stamp = stampedLock.writeLock(); // 获取写锁
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            stampedLock.unlockWrite(stamp); // 释放写锁
        }
    }

    public double distanceFromOrigin(){
        long stamp = stampedLock.tryOptimisticRead(); // 获取一个乐观的读锁
        // 注意下面两行不是原子操作
        // 此时读取了（100,200）
        double currentX = x;
        // 可能这里被改成了（300,400）
        double currentY = y;
        // 最后读取的数据可能是（100,400）,错误

        /**
         * 和ReadWriteLock相比，写入的加锁是完全一样的，不同的是读取。注意到首先我们通过tryOptimisticRead()获取一个乐观读锁，
         * 并返回版本号。接着进行读取，读取完成后，我们通过validate()去验证版本号，如果在读取过程中没有写入，版本号不变，验证成功，
         * 我们就可以放心地继续后续操作。如果在读取过程中有写入，版本号会发生变化，验证将失败。在失败的时候，我们再通过获取悲观读锁再次读取。
         * 由于写入的概率不高，程序在绝大部分情况下可以通过乐观读锁获取数据，极少数情况下使用悲观读锁获取数据。
         */
        if(!stampedLock.validate(stamp)){  // 检测乐观读锁后是否还有其他写锁发生
            stamp = stampedLock.readLock();  // 获取一个悲观的读锁
            try {
                currentX = x;
                currentY = y;
            } finally {
                stampedLock.unlockRead(stamp);  // 释放悲观的读锁
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
}