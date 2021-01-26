package multiThread;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @ClassName: ThreadCommonMethod
 * @Description:  线程的常用方法
 * @Author: gx
 * @Date: 2021/1/26 20:12
 * @Version: 1.0
 */
public class ThreadCommonMethod {

    /***
     * @Description: wait 和 notify 方法
     * @Author: gx
     * @Date: 2021/1/26 20:17
     * @Param:
     * @Return:
     **/
    @Test
    public void waitNotifyMethod() throws InterruptedException {

        /**
         * wait 和 notify 方法总结
         * 1，在synchronized内部可以调用wait()使线程进入等待状态；
         * 2，必须在已获得的锁对象上调用wait()方法；
         * 3，在synchronized内部可以调用notify()或notifyAll()唤醒其他等待线程；
         * 4，必须在已获得的锁对象上调用notify()或notifyAll()方法；
         * 5，已唤醒的线程还需要重新获得锁后才能继续执行。
         */

        TaskQueue taskQueue = new TaskQueue();
        List<Thread> threadList = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            Thread t = new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "获得了" + taskQueue.getTask());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
            threadList.add(t);
        }

        Thread t2 = new Thread(() -> {
           for(int i = 0; i < 5; i++){
               taskQueue.addTask("task-" + i);
               try {
                   Thread.sleep(100);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });
        t2.start();
        t2.join();
        Thread.sleep(1000);
        for(Thread thread : threadList){
            thread.interrupt();
        }
        System.out.println("end!");
    }
}

class TaskQueue{
    Queue<String> queue = new LinkedList<>();

    public synchronized void addTask(String s){
        this.queue.add(s);
        // 使用notifyAll()将一次性全部唤醒。通常来说，notifyAll()更安全。有些时候，如果我们的代码逻辑考虑不周，用notify()会导致只唤醒了一个线程，而其他线程可能永远等待下去醒不过来了。
        this.notifyAll();
    }

    public synchronized String getTask() throws InterruptedException {
        // 这里一定要使用while循环不停的判断是否为空，因为可能有多个线程在wait后，被notifyAll全部唤醒了以后，如果直接remove，会remove多次
        // 并且queue已经可能被其他线程remove变成null了，这个时候线程再去remove就会报错，所以要用while循环判空。
        while (this.queue.isEmpty()){
            // 获取了this锁
            System.out.println("线程：" + Thread.currentThread().getName() + "wait..");
            this.wait();
            // 释放了 this锁
            System.out.println("线程：" + Thread.currentThread().getName() + "notify..");
        }
        return this.queue.remove();
    }
}
