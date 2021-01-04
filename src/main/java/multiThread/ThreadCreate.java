package multiThread;

import java.util.concurrent.*;

/**
 * @ClassName: ThreadCreate
 * @Description: 线程创建的两种方式
 * @Author: gx
 * @Date: 2021/1/4 11:07
 * @Version: 1.0
 */
public class ThreadCreate {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        // 创建线程方式1，直接调用继承了Thread类的start()方法
        CreateThreadMethod01 createThreadMethod01 = new CreateThreadMethod01();
        createThreadMethod01.start();

        // 创建线程方式2，创建Thread对象时，传入一个Runnable实列
        Thread createThreadMethod02 = new Thread(new CreateThreadMethod02());
        createThreadMethod02.start();

        //创建线程方式3 -- 实现Callable接口，重写call()方法
        Callable<String> callable = new CreateTheradMethod03();

        //  Future就是对于具体的Runnable或者Callable任务的执行结果进行取消、查询是否完成、获取结果。
        FutureTask<String> futureTask = new FutureTask<>(callable);
        Thread thread = new Thread(futureTask);
        thread.start();
        // 使用Future的get()方法来获取返回结果，此处会堵塞，但是可以设置超时时间，如果过了超时时间还未执行完，会抛出TimeoutException异常
        System.out.println(futureTask.get(1, TimeUnit.SECONDS));

        System.out.println("线程" + Thread.currentThread().getName());
    }
}

/***
 * @Description: 创建线程方式1 -- 继承Thread类，重写run()方法
 * @Author: gx
 * @Date: 2021/1/4 11:16
 * @Param:
 * @Return:
 **/
class CreateThreadMethod01 extends Thread{

    @Override
    public void run(){
        System.out.println("继承Thread类，创建的线程启动了！线程名称："+ Thread.currentThread().getName());
    }
}

/***
 * @Description: 创建线程方式2 -- 实现Runnable接口，重写run()方法
 * @Author: gx
 * @Date: 2021/1/4 11:28
 * @Param:
 * @Return:
 **/
class CreateThreadMethod02 implements Runnable{

    @Override
    public void run() {
        System.out.println("实现Runnable接口，创建的线程启动了!");
    }
}

/***
 * @Description: 创建线程方式3 -- 实现Callable接口，重写call()方法；好处：有返回值
 * @Author: gx
 * @Date: 2021/1/4 20:14
 * @Param:
 * @Return:
 **/
class CreateTheradMethod03 implements Callable<String>{

    /**
     *  Runnable 和 Callable 的区别
     *  1、Runnable是自从java1.1就有了，而Callable是1.5之后才加上去的。
     *
     * 2、Callable规定的方法是call(),Runnable规定的方法是run()。
     *
     * 3、Callable的任务执行后可返回值，而Runnable的任务是不能返回值(是void)。
     *
     * 4、call方法可以抛出异常，run方法不可以。
     *
     * 5、运行Callable任务可以拿到一个Future对象，表示异步计算的结果。它提供了检查计算是否完成的方法，以等待计算的完成，
     *    并检索计算的结果。通过Future对象可以了解任务执行情况，可取消任务的执行，还可获取执行结果。
     */
    @Override
    public String call() throws Exception {
        System.out.println("实现Callable接口，创建的线程启动了!");
        Thread.sleep(5000);
        return "Callable线程call()方法返回的参数";
    }
}

