package JavaConcurrency.bPrinciple;

//10 CAS与原子操作
public class jCASAndAtomic {
    //10.1 乐观锁与悲观锁的概念
        //悲观锁：总是认为每次访问共享资源时会发生冲突，所以必须对每次数据操作加上锁，以保证临界区的程序同一时间只能有一个线程在执行。
        //乐观锁：总是假设对共享资源的访问没有冲突，线程可以不停地执行，无需加锁也无需等待。而一旦多个线程发生冲突，乐观锁通常是使用一种称为CAS的技术来保证线程执行的安全性。
        //乐观锁天生免疫死锁。
        //乐观锁多用于"读多写少"的环境，避免频繁加锁影响性能；而悲观锁多用于"写多读少"的环境，避免频繁失败和重试影响性能。
    //10.2 CAS的概念
        //CAS的全称是：比较并交换（Compare And Swap）。在CAS中，有这样三个值：V：要更新的变量(var)；E：预期值(expected)；N：新值(new)
        //比较并交换的过程如下：判断V是否等于E，如果等于，将V的值设置为N；如果不等，说明已经有其它线程更新了V，则当前线程放弃更新，什么都不做。
    //10.3 Java实现CAS的原理-Unsafe类
        //LockSupport中的支持线程挂起和恢复的park和unpark就是调用Unsafe类来实现的；
    //10.4 原子操作-AtomicInteger类源码解析
        //依然是依靠Unsafe类来实现的；
    //10.5 CAS实现原子操作的三大问题
        //ABA问题
            //ABA问题的解决思路是在变量前面追加上版本号或者时间戳。从JDK1.5开始，JDK的atomic包里提供了一个类AtomicStampedReference类来解决ABA问题。
        //循环时间长，开销大
            //CAS多与自旋结合。如果自旋CAS长时间不成功，会占用大量的CPU资源。解决思路是让JVM支持处理器提供的pause指令。
        //只能保证一个共享变量的原子操作
            //1.使用JDK1.5开始就提供的AtomicReference类保证对象之间的原子性，把多个变量放到一个对象里面进行CAS操作；
            //2.使用锁。锁内的临界区代码可以保证只有当前线程能操作。
}
