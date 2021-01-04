package multiThread;

/**
 * @ClassName: ThreadCreate
 * @Description: 线程创建的两种方式
 * @Author: gx
 * @Date: 2021/1/4 11:07
 * @Version: 1.0
 */
public class ThreadCreate {

    public static void main(String[] args) {
        // 创建线程方式1，直接调用继承了Thread类的start()方法
        CreateThreadMethod01 createThreadMethod01 = new CreateThreadMethod01();
        createThreadMethod01.start();

        // 创建线程方式2，创建Thread对象时，传入一个Runnable实列
        Thread createThreadMethod02 = new Thread(new CreateThreadMethod02());
        createThreadMethod02.start();

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