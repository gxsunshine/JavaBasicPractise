package multiThread;

/**
 * @ClassName: Singleton
 * @Description: 双重校验锁实现单例
 * @Author: gx
 * @Date: 2021/2/20 10:37
 * @Version: 1.0
 */
public class Singleton {

    // volatile 关键字是不可少的，禁止指令重排
    private static volatile Singleton uniqueInstance;

    // 构造方法私有化
    private Singleton(){
    }

    // 提供一个获取实例的静态方法
    public static Singleton getInstance(){
        // 第一次校验，如果不为空，直接返回了，提升代码执行效率
        if(uniqueInstance == null){
            synchronized (Singleton.class){
                // 第二次校验，防止二次创建实例
                /**
                 * 可能有一种情况，当singleton还未创建的时候，线程t1调用了getInstance()方法，通过了第一次校验，进入了同步代码块，
                 * 然后被资源被线程t2抢占了，线程t2也会通过第一次校验，进入了同步代码块。两个线程都能继续往下执行，这样就会创建了两个实例。
                 * 所以双重校验是都不能缺少的。
                 */
                if(uniqueInstance == null){
                    uniqueInstance = new Singleton();
                }
            }
        }
        return uniqueInstance;
    }

    public static void main(String[] args) {
        for(int i = 0; i < 1000; i++){
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "--" + Singleton.getInstance().hashCode());
            }).start();
        }
    }
}
