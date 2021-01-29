package multiThread;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: gx
 * @Date: Created in 2021/1/29 21:09
 * @Description:  生产者和消费者问题
 */
public class ProducerAndConsumer {

    @Test
    public void test() throws InterruptedException {
        Company company = new Company();
        List<Thread> producerThreadList = new ArrayList<>();
        List<Thread> consumerThreadList = new ArrayList<>();
        int producerSum = 100;
        int consumerSum = 100;

        Long startTime = new Date().getTime();

        // 生产
        for(int i = 0; i < producerSum; i++){
            String goodsName = "goods" + i;
            Thread thread = new Thread(() -> {
                try {
                    company.producer(goodsName);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            producerThreadList.add(thread);
        }

        // 消费
        for(int i = 0; i < consumerSum; i++){
            Thread thread = new Thread(() -> {
                try {
                    System.out.println(company.consumer());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            consumerThreadList.add(thread);
        }

//        Thread.sleep(2000);

        // 主线程等待所有线程结束
        for(Thread thread : consumerThreadList){
            thread.join();
        }
        for(Thread thread : producerThreadList){
            thread.join();
        }

        System.out.println("end...");
        System.out.println("耗时：" + (new Date().getTime() - startTime) + " ms");
    }
}


class Company{

    // 仓库
    Queue<String> warehouse = new LinkedList<>();
    // 仓库容量
    int warehouseSize = 10;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    /**
     * 生产者
     * @param goods
     */
    public  void producer(String goods) throws InterruptedException {
        System.out.println("生产商品材料准备...");
        Thread.sleep(10);
        lock.lock();
        try {
            while (warehouse.size() == warehouseSize){
                System.out.println("仓库满了，等待消费...");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            warehouse.add(goods);
            Thread.sleep(1);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 消费者
     * @return
     */
    public  String consumer() throws InterruptedException {
        System.out.println("销售商品售前检查...");
        Thread.sleep(10);
        lock.lock();
        try {
            while (warehouse.isEmpty()){
                System.out.println("仓库没有货物了，需要等待生产...");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            condition.signalAll();
            return warehouse.remove();
        } finally {
            lock.unlock();
        }
    }
}