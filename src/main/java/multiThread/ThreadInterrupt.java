package multiThread;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ClassName: ThreadInterrupt
 * @Description: 线程的中断
 * @Author: gx
 * @Date: 2021/1/5 11:30
 * @Version: 1.0
 */
public class ThreadInterrupt {

    public static void main(String[] args) throws InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

//        MyThread myThread = new MyThread();
//        myThread.start();
//        Thread.sleep(100);
//        // 只是发错中断请求，该线程并不一定马上结束
//        myThread.interrupt();
////        Thread.currentThread().interrupt();
////        myThread.join();  // 等待myThread线程结束
//        System.out.println("end!");

        /** interrupt() 会让 join()方法抛出 InterruptedException **/
        /**
         * 如果线程处于等待状态，例如，t.join()会让main线程进入等待状态，此时，如果对main线程调用interrupt()，\
         * join()方法会立刻抛出InterruptedException，因此，目标线程只要捕获到join()方法抛出的InterruptedException，
         * 就说明有其他线程对其调用了interrupt()方法，通常情况下该线程应该立刻结束运行。
//         */
//        Thread thread1 = new Thread1();
//        thread1.start();
//        Thread.sleep(100);
//        thread1.interrupt();
//        System.out.println("end");

        /** 使用标志位来中断线程 **/
        Thread3 thread3 = new Thread3();
        thread3.start();
        Thread.sleep(100);
        thread3.running = false;
    }
}

class MyThread extends Thread{
    @Override
    public void run() {
        int n = 0;
        while (!Thread.currentThread().isInterrupted()){
            n++;
            System.out.println("+"+ n + "hello!");
        }
    }
}

class Thread1 extends Thread{
    @Override
    public void run() {
        Thread thread2 = new Thread2();
        thread2.start();
        try {
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("线程thread2 意外退出");
        }
        thread2.interrupt();
    }
}

class Thread2 extends Thread{
    @Override
    public void run() {
        int n  = 0;
        while (!Thread.currentThread().isInterrupted()){
            n++;
            System.out.println("+"+ n + "hello!");
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("线程thread3 意外退出");
                break;
            }
        }
    }
}

class Thread3 extends Thread{
    // 线程间共享变量需要使用volatile关键字标记，确保每个线程都能读取到更新后的变量值。
    /**
     *  volatile关键字的目的是告诉虚拟机：
     *      1，每次访问变量时，总是获取主内存的最新值；
     *      2，每次修改变量后，立刻回写到主内存。
     *  volatile关键字解决的是可见性问题：当一个线程修改了某个共享变量的值，其他线程能够立刻看到修改后的值。
     */
    public volatile boolean running = true;
    @Override
    public void run() {
        int n = 0;
        while (running) {
            n ++;
            System.out.println(n + " hello!");
        }
        System.out.println("end!");
    }
}