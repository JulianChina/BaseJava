package JavaConcurrency.bPrinciple;

//9 synchronized与锁
public class iSynchronizedAndLock {
    //Java多线程的锁都是基于对象的，Java中的每一个对象都可以作为一个锁。
    //9.1 synchronized关键字
    //9.2 几种锁
        //在Java6及其以后，一个对象其实有四种锁状态，它们级别由低到高依次是：无锁状态、偏向锁状态、轻量级锁状态、重量级锁状态；
        //Java对象头
            //https://redspider.gitbook.io/concurrent/di-er-pian-yuan-li-pian/9
            //每个Java对象都有对象头。如果是非数组类型，则用2个字宽来存储对象头，如果是数组，则会用3个字宽来存储对象头。在32位处理器中，一个字宽是32位；在64位虚拟机中，一个字宽是64位。
        //偏向锁
            //偏向锁在资源无竞争情况下消除了同步语句，连CAS操作都不做了，提高了程序的运行性能。
            //偏向锁使用了一种等到竞争出现才释放锁的机制，所以当其他线程尝试竞争偏向锁时，持有偏向锁的线程才会释放锁。
        //轻量级锁
            //自旋：不断尝试去获取锁，一般用循环来实现。
        //重量级锁
            //重量级锁依赖于操作系统的互斥量（mutex） 实现的，而操作系统中线程间状态的转换需要相对比较长的时间，所以重量级锁效率很低，但被阻塞的线程不会消耗CPU。
        //总结锁的升级流程
            //每一个线程在准备获取共享资源时：
            //第一步，检查MarkWord里面是不是放的自己的ThreadId ,如果是，表示当前线程是处于"偏向锁"。
            //第二步，如果MarkWord不是自己的ThreadId，锁升级，这时候，用CAS来执行切换，新的线程根据MarkWord里面现有的ThreadId，通知之前线程暂停，之前线程将MarkWord的内容置为空。
            //第三步，两个线程都把锁对象的HashCode复制到自己新建的用于存储锁的记录空间，接着开始通过CAS操作， 把锁对象的MarkWord的内容修改为自己新建的记录空间的地址的方式竞争MarkWord。
            //第四步，第三步中成功执行CAS的获得资源，失败的则进入自旋。
            //第五步，自旋的线程在自旋过程中，成功获得资源(即之前获的资源的线程执行完成并释放了共享资源)，则整个状态依然处于轻量级锁的状态，如果自旋失败。
            //第六步，进入重量级锁的状态，这个时候，自旋的线程进行阻塞，等待之前线程执行完成并唤醒自己。
        //各种锁的优缺点对比
            //https://redspider.gitbook.io/concurrent/di-er-pian-yuan-li-pian/9
}
