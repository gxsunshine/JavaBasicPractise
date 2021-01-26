package multiThread;

/**
 * @ClassName: ThreadSynchronization
 * @Description: 线程同步练习
 * @Author: gx
 * @Date: 2021/1/14 20:05
 * @Version: 1.0
 */
public class ThreadSynchronization {
    public static void main(String[] args) throws Exception {

        //获取当前堆的大小 byte 单位
        long heapSize = Runtime.getRuntime().totalMemory();
        System.out.println(heapSize);

        //获取堆的最大大小byte单位
        //超过将抛出 OutOfMemoryException
        long heapMaxSize = Runtime.getRuntime().maxMemory();
        System.out.println(heapMaxSize);

        //获取当前空闲的内存容量byte单位
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        System.out.println(heapFreeSize);

        /**
         *  例子1：使用几个线程对一个变量进行相同的增1和减1的操作，但是每次的运行结果不一定都为0。
         *  因为 n = n+1; 并不是原子性的，它主要分为ILOAD,IADD,ISTORE三个指令执行，如果执行的顺序是
         *  a-ILOAD(100) a-IADD b-ILOAD(100) a-ISTORE(101) b-IADD b-ISTORE(101) 虽然两个线程分别加了一次，但是存入内存的值还是101
         *
         *  注意：这里用的机器是16G的内存，导致用两个线程测试了很多次，都是0。调整运行是的JVM也没有效果，具体原因不明。
         *      但是可以通过加线程，和让加减线程sleep一下，可以不是0
         */
//        AddThread add = new AddThread();
//        DecThread dec = new DecThread();
//        AddThread add2 = new AddThread();
//        DecThread dec2 = new DecThread();
//        add.start();
//        dec.start();
//        add2.start();
//        dec2.start();
//        System.out.println("waiting....");
//        add.join();
//        dec.join();
//        add2.join();
//        dec2.join();
//        System.out.println(Counter.count);

        /**  例子2：使用synchronized代码块锁住共享变量。
         *   使用方法：
         *      1，找出修改共享变量的线程代码块；
         *      2，选择一个共享实例作为锁；
         *      3，使用synchronized(lockObject) { ... }。
         */
//        AddThread2 add = new AddThread();
//        DecThread2 dec = new DecThread();
//        add.start();
//        dec.start();
//        System.out.println("waiting...");
//        add.join();
//        dec.join();
//        System.out.println(Counter.count);
//        System.out.println("ending...");


        /** 例子3：使用线程安全类
         *  让线程自己选择锁对象往往会使得代码逻辑混乱，也不利于封装。更好的方法是把synchronized逻辑封装起来。
         */
        Counter3 counter3a = new Counter3();
        System.out.println("waiting...");
        Thread thread3a = new Thread(() -> {
            counter3a.add();
        });
        Thread thread3b = new Thread(() -> {
            counter3a.dec();
        });
        thread3a.start();
        thread3b.start();
        System.out.println("count: " + counter3a.getCount());
        System.out.println("end...");
    }
}

class Counter {
    public static int count = 0;
}

class AddThread extends Thread {
    public void run() {
        for (int i=0; i<10000; i++) {
            Counter.count += 1;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class DecThread extends Thread {
    public void run() {
        for (int i=0; i<10000; i++) {
            Counter.count -= 1;
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Counter2 {
    public static final Object lock = new Object();
    public static int count = 0;
}

class AddThread2 extends Thread {
    public void run() {
        for (int i=0; i<10000; i++) {
            /**
             * 使用Counter.lock实列作为锁，两个线程在执行各自的synchronized(Counter.lock){...}代码块时，必须先获得锁，才能进入代码块运行。
             * 执行结束后，会在synchronized语句块结束会自动释放锁。这样确保了保证了Counter.count不会同时被多个线程读写操作。
             * 但是synchronized会降低程序的性能。因为synchronized代码块无法并发执行， 并且每次加锁和解锁也需要时间。
             */
            synchronized (Counter2.lock){  // 要锁住的是同一个对象，不然不能同步
                Counter.count += 1;
            }  // 这里会释放锁
        }
    }
}

class DecThread2 extends Thread {
    public void run() {
        for (int i=0; i<10000; i++) {
            synchronized (Counter2.lock){
                Counter.count -= 1;
            }
        }
    }
}

/***
 * @Description: 线程安全类；如果一个类被设计为允许多线程正确访问，我们就说这个类就是“线程安全”的（thread-safe）。
 * @Author: gx
 * @Date: 2021/1/19 11:56
 * @Param:
 * @Return:
 **/
class Counter3{
    private int count = 0;

    // 当我们锁住的是this实例时，实际上可以用synchronized修饰这个方法。
//    public void add(){
//        synchronized (this){
//            count += 1;
//        }
//    }
    public synchronized void add(){
            count += 1;
    }
    public synchronized void dec(){
            count -= 1;
    }
    public int getCount(){
        return count;
    }
}

