package multiThread;

/**
 *  小结：
 *      1，守护线程是为其他线程服务的线程；
 *      2，所有非守护线程都执行完毕后，虚拟机退出；
 *      3，守护线程不能持有需要关闭的资源（如打开文件等）。
 */

/**
 * @ClassName: DaemonThread
 * @Description: 守护线程
 * @Author: gx
 * @Date: 2021/1/11 11:42
 * @Version: 1.0
 */
public class DaemonThread {
    public static void main(String[] args) throws InterruptedException {
        Thread5 thread5 = new Thread5();
        thread5.setDaemon(true);  // 设置为守护线程
        thread5.start();
        Thread.sleep(1000);
        System.out.println("end");
        /**
         * 此处如果Thread5线程不是守护线程的的话，main一直不会结束，直到Thread5线程运行完毕。
         * 但是设置成了守护线程，运行到这main线程就直接结束了。
         */
    }
}

class Thread5 extends Thread{
    @Override
    public void run() {
        int n = 0;
        while (true){
            try {
                System.out.println("hello" + n++);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("thread01 线程意外结束");
            }
        }
    }
}
