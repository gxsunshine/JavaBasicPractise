package basic;

import org.junit.Test;

/**
 * @ClassName: ConstructorMethod
 * @Description: 构造方法练习
 * @Author: gx
 * @Date: 2021/2/5 17:58
 * @Version: 1.0
 */
public class ConstructorMethod {

    /***
     * @Description: 无参构造方法的作用
     * @Author: gx
     * @Date: 2021/2/5 18:08
     * @Param: []
     * @Return: void
     **/
    @Test
    public void test(){
        /**
         *  1，调用子类的无参方法时，会首先调用父类的无参构造方法，如果父类没有无参构造方法就会报错。
         *  2，可以使用super(..) 来指定特定的父类构造方法，也不会报错
         *  3，为了防止报错，最好的解决方法是在父类中加一个什么也不做的无参构造方法
         */
        Son son = new Son();
    }
}

class Parent{

    private String name;

//    public Parent() {
//        System.out.println("父类的无参构造方法被调用了！");
//    }

    public Parent(String name) {
        System.out.println("父类的有参构造方法被调用了！");
        this.name = name;
    }
}

class Son extends Parent{

    private String age;

    public Son() {
        super("few");
        System.out.println("子类的无参构造方法被调用了！");
    }
}
