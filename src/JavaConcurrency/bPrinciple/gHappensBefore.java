package JavaConcurrency.bPrinciple;

//7 重排序与happens-before
public class gHappensBefore {
    //7.1 什么是重排序？
        //指令重排一般分为以下三种：
            //编译器优化重排：不改变单线程程序语义
            //指令并行重排：不存在数据依赖性
            //内存系统重排
    //7.2 顺序一致性模型与JMM的保证
        //数据竞争与顺序一致性
            //volatile、final、synchronized等；
        //顺序一致性模型
            //每个操作必须立即对任意线程可见。但是JMM没有这样的保证。
        //JMM中同步程序的顺序一致性效果
            //JMM的具体实现方针是：在不改变（正确同步的）程序执行结果的前提下，尽量为编译期和处理器的优化打开方便之门。
        //JMM中未同步程序的顺序一致性效果
            //未同步程序在JMM和顺序一致性内存模型中的执行特性有如下差异：
                //1.顺序一致性保证单线程内的操作会按程序的顺序执行；JMM不保证单线程内的操作会按程序的顺序执行。（因为重排序，但是JMM保证单线程下的重排序不影响执行结果）
                //2.顺序一致性模型保证所有线程只能看到一致的操作执行顺序，而JMM不保证所有线程能看到一致的操作执行顺序。（因为JMM不保证所有操作立即可见）
                //3.JMM不保证对64位的long型和double型变量的写操作具有原子性，而顺序一致性模型保证对所有的内存读写操作都具有原子性。
    //7.3 happens-before
        //什么是happens-before?
            //如果操作A happens-before操作B，那么操作A在内存上所做的操作对操作B都是可见的，不管它们在不在一个线程。
        //天然的happens-before关系
            //在Java中，有以下天然的happens-before关系：
                //程序顺序规则：一个线程中的每一个操作，happens-before于该线程中的任意后续操作。
                //监视器锁规则：对一个锁的解锁，happens-before于随后对这个锁的加锁。
                //volatile变量规则：对一个volatile域的写，happens-before于任意后续对这个volatile域的读。
                //传递性：如果A happens-before B，且B happens-before C，那么A happens-before C。
                //start规则：如果线程A执行操作ThreadB.start()启动线程B，那么A线程的ThreadB.start()操作happens-before于线程B中的任意操作、
                //join规则：如果线程A执行操作ThreadB.join()并成功返回，那么线程B中的任意操作happens-before于线程A从ThreadB.join()操作成功返回。
}
