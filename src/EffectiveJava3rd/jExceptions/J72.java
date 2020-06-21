package EffectiveJava3rd.jExceptions;

//72 优先使用标准的异常
public class J72 {
    //专家级程序员与缺乏经验的程序员一个最主要的区别在于，专家追求并且通常也能够实现高度的代码重用。
    //代码重用是值得提倡的，这是一条通用的规则，异常也不例外。
    //最经常被重用的异常类型是IllegalArgumentException。
    //另一个经常被重用的异常是IllegalStateException。
    //如果调用者在某个不允许null值的参数中传递了null，习惯的做法就是抛出NullPointerException异常。
    //如果调用者在表示序列下标的参数中传递了越界的值，应该抛出的就是IndexOutOfBoundsException异常。
    //另一个值得了解的通用异常是ConcurrentModificationException。
    //最后一个值得注意的标准异常是UnsupportedOperationException。

    //不要直接重用Exception、RuntimeException、Throwable或者Error。
    //异常                                //使用场合
    //IllegalArgumentException           //非null的参数值不正确
    //IllegalStateException              //不适合方法调用的对象状态
    //NullPointerException               //在禁止使用null的情况下参数值为null
    //IndexOutOfBoundsException          //下标参数值越界
    //ConcurrentModificationException    //在禁止并发修改的情况下，检测到对象的并发修改
    //UnsupportedOperationException      //对象不支持用户请求的方法
}
