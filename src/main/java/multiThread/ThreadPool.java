package multiThread;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: ThreadPool
 * @Description: 线程池练习
 * @Author: gx
 * @Date: 2021/1/4 11:48
 * @Version: 1.0
 */
public class ThreadPool {
    public static void main(String[] args) {
        fixedThreadPool();
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
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end task " + name);
    }
}
