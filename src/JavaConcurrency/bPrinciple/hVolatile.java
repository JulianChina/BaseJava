package JavaConcurrency.bPrinciple;

//8 volatile
public class hVolatile {
    //8.1 几个基本概念
        //内存可见性
            //内存可见性，指的是线程之间的可见性，当一个线程修改了共享变量时，另一个线程可以读取到这个修改后的值。
        //重排序
        //happens-before规则
    //8.2 volatile的内存语义
        //volatile主要有两个功能：保证变量的内存可见性；禁止volatile变量与普通变量重排序；
        //内存可见性
        //禁止重排序
            //硬件层面，内存屏障分两种：读屏障（Load Barrier）和写屏障（Store Barrier）。
            //内存屏障有两个作用：
                //1.阻止屏障两侧的指令重排序；
                //2.强制把写缓冲区/高速缓存中的脏数据等写回主内存，或者让缓存中相应的数据失效。
            //编译器在生成字节码时，会在指令序列中插入内存屏障来禁止特定类型的处理器重排序。
            //编译器选择了一个比较保守的JMM内存屏障插入策略，这样可以保证在任何处理器平台，任何程序中都能得到正确的volatile内存语义。这个策略是：
                //1.在每个volatile写操作前插入一个StoreStore屏障；
                //2.在每个volatile写操作后插入一个StoreLoad屏障；
                //3.在每个volatile读操作后插入一个LoadLoad屏障；
                //4.在每个volatile读操作后再插入一个LoadStore屏障。
            //volatile与普通变量的重排序规则:
                //1.如果第一个操作是volatile读，那无论第二个操作是什么，都不能重排序；
                //2.如果第二个操作是volatile写，那无论第一个操作是什么，都不能重排序；
                //3.如果第一个操作是volatile写，第二个操作是volatile读，那不能重排序。
    //8.3 volatile的用途
        static class Singleton {
            private static volatile Singleton instance;  //volatile是防止下面重排序的关键

            public static Singleton getInstance() {
                if (instance == null) {
                    synchronized (Singleton.class) {
                        if (instance == null) {
                            instance = new Singleton();
                        }
                    }
                }
                return instance;
            }
        }
}
