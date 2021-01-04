package multiThread;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * @ClassName: ThreadPool
 * @Description: 线程池练习
 * @Author: gx
 * @Date: 2021/1/4 11:48
 * @Version: 1.0
 */
public class ThreadPool {
    public static void main(String[] args) {
        // 固定大小的线程池
//        fixedThreadPool();
        // 动态线程池
//        cachedThreadPool();
        // 指定范围的动态线程池例子
//        specifiedRangeThreadPool();
        // 定期执行任务
        regularScheduledThreadPool();
    }

    /***
     * @Description: 固定大小的线程池
     * @Author: gx
     * @Date: 2021/1/4 12:03
     * @Param: []
     * @Return: void
     **/
    public static void fixedThreadPool(){
        // 创建固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for(int i = 1; i <= 6; i++){
            Task task = new Task(i + "");
            executorService.submit(task);
        }
        executorService.shutdown();
    }

    /***
     * @Description: 动态线程池，最大是Integer.MAX_VALUE
     * @Author: gx
     * @Date: 2021/1/4 19:19
     * @Param: []
     * @Return: void
     **/
    public static void cachedThreadPool(){
        // 创建动态线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i = 1; i <= 10; i++){
            Task task = new Task(i + "");
            executorService.submit(task);
        }
        executorService.shutdown();
    }

    /***
     * @Description: 指定范围的动态线程池例子
     * @Author: gx
     * @Date: 2021/1/4 19:22
     * @Param: []
     * @Return: void
     **/
    public static void specifiedRangeThreadPool(){
        int min = 0;
        int max = 10;
        /**
         * 注意：当要创建的线程数量大于线程池的最大线程数的时候，新的任务就会被拒绝，此时可以直接选对应策略或者自定义策略。
         * 参考：https://blog.csdn.net/qq_25806863/article/details/71172823
         */
        ExecutorService executorService = new ThreadPoolExecutor(min, max, 1L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        for(int i = 1; i <= 10; i++){
            Task task = new Task(i + "");
            executorService.submit(task);
        }
        executorService.shutdown();
    }

    /***
     * @Description: 定期调度的线程池
     * @Author: gx
     * @Date: 2021/1/4 19:42
     * @Param: []
     * @Return: void
     **/
    public static void regularScheduledThreadPool(){
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);
        // 5秒后执行一次任务，只执行一次
        // scheduledExecutorService.schedule(new Task("task1"), 5, TimeUnit.SECONDS);

        // 2秒后开始执行定时任务，每3秒执行一次（如果定时任务执行花费了10秒，那就等定时任务执行完后，立马开始执行新的任务）
        scheduledExecutorService.scheduleAtFixedRate(new Task("task2"), 2, 3, TimeUnit.SECONDS);

        // 2秒后开始执行定时任务，以固定时间为间隔(任务执行完后)
//        scheduledExecutorService.scheduleWithFixedDelay(new Task("task3"), 2, 3, TimeUnit.SECONDS);
//        scheduledExecutorService.shutdown();
    }

}

class Task extends Thread{

    private String name;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("start task " + name);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end task " + name);
    }
}
