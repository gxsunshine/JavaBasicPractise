package multiThread;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.locks.*;

/**
 * @Author: gx
 * @Date: Created in 2021/1/29 21:09
 * @Description:  生产者和消费者问题
 */
public class ProducerAndConsumer {

    @Test
    public void test() throws InterruptedException {
        Company company = new Company();
        List<Thread> threadList = new ArrayList<>();
        List<Thread> producerThreadList = new ArrayList<>();
        List<Thread> consumerThreadList = new ArrayList<>();
        int producerSum = 100;
        int consumerSum = 100;
        int readSum = 10000;

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
            threadList.add(thread);
        }

        // 消费
        for(int i = 0; i < consumerSum; i++){
            Thread thread = new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "消费了商品：" + company.consumer());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            threadList.add(thread);
        }

        // 获取库存
        for(int i = 0; i < readSum; i++){
            Thread thread = new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "当前库存：" + company.getStock() + "个" );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            threadList.add(thread);
        }

//        Thread.sleep(2000);

        // 主线程等待所有线程结束
        for(Thread thread : threadList){
            thread.join();
        }
//        for(Thread thread : consumerThreadList){
//            thread.join();
//        }
//        for(Thread thread : producerThreadList){
//            thread.join();
//        }

        System.out.println("end...");
        System.out.println("耗时：" + (new Date().getTime() - startTime) + " ms");
    }
}


class Company{

    // 仓库
    Queue<String> warehouse = new LinkedList<>();
    // 仓库容量
    int warehouseSize = 10;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock rLock = rwLock.readLock();
    private final Lock wLock = rwLock.writeLock();
    private final Condition condition = wLock.newCondition();

    /**
     * 生产者
     * @param goods
     */
    public  void producer(String goods) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "生产商品材料准备...");
        Thread.sleep(10);
        wLock.lock();
        try {
            while (warehouse.size() == warehouseSize){
                System.out.println(Thread.currentThread().getName() +"仓库满了，等待消费...");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            warehouse.add(goods);
            System.out.println(Thread.currentThread().getName() + "生产了商品：" + goods);
            Thread.sleep(1);
            condition.signalAll();
        } finally {
            wLock.unlock();
        }
    }

    /**
     * 消费者
     * @return
     */
    public  String consumer() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "销售商品售前检查...");
        Thread.sleep(10);
        wLock.lock();
        try {
            while (warehouse.isEmpty()){
                System.out.println(Thread.currentThread().getName() + "仓库没有货物了，需要等待生产...");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            condition.signalAll();
            return warehouse.remove();
        } finally {
            wLock.unlock();
        }
    }

    /***
     * @Description: 获取库存
     * @Author: gx
     * @Date: 2021/2/1 11:08
     **/
    public int getStock() throws InterruptedException {

        rLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "清理仓库库存...");
            Thread.sleep(1);
            return warehouse.size();
        } finally {
            rLock.unlock();
        }
    }
}