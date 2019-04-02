package JavaConcurrency.cJDKTool;

import java.util.concurrent.locks.StampedLock;

//14 锁接口和类
public class nLock {
    //14.1 synchronized的不足之处
    //14.2 锁的几种分类
        //可重入锁和非可重入锁
            //锁支持一个线程对资源重复加锁。
            //synchronized关键字就是使用的重入锁。
            //ReentrantLock的中文意思就是可重入锁。
        //公平锁和非公平锁
            //如果对一个锁来说，先对锁获取请求的线程一定会先被满足，后对锁获取请求的线程后被满足，那这个锁就是公平的。反之，那就是不公平的。
            //一般情况下，非公平锁能提升一定的效率。但是非公平锁可能会发生线程饥饿（有一些线程长时间得不到锁）的情况。
            //ReentrantLock支持非公平锁和公平锁两种。
        //读写锁和排它锁
            //"排它锁"，这些锁在同一时刻只允许一个线程进行访问。
            //读写锁可以在同一时刻允许多个读线程访问。注意，即使用读写锁，在写线程访问时，所有的读线程和其它写线程均被阻塞。
    //14.3 JDK中有关锁的一些接口和类
        //抽象类AQS/AQLS/AOS
            //AQS（AbstractQueuedSynchronizer）
            //AQLS（AbstractQueuedLongSynchronizer）
            //AOS（AbstractOwnableSynchronizer）
        //接口Condition/Lock/ReadWriteLock
        //ReentrantLock
            //ReentrantLock是一个非抽象类，它是Lock接口的JDK默认实现，实现了锁的基本功能。
        //ReentrantReadWriteLock
            //这个类也是一个非抽象类，它是ReadWriteLock接口的JDK默认实现。
            //ReentrantReadWriteLock实现了读写锁，但它有一个小弊端，就是在"写"操作的时候，其它线程不能写也不能读。我们称这种现象为"写饥饿"。
        //StampedLock
            //在读的时候如果发生了写，应该通过重试的方式来获取新的值，而不应该阻塞写操作。这种模式也就是典型的无锁编程思想，和CAS自旋的思想一样。
            //这种操作方式决定了StampedLock在读线程非常多而写线程非常少的场景下非常适用，同时还避免了写饥饿情况的发生。
            static class Point {
                private double x, y;
                private final StampedLock sl = new StampedLock();
                //写锁的使用
                void move(double deltaX, double deltaY) {
                    long stamp = sl.writeLock();  //读取写锁
                    try {
                        x += deltaX;
                        y += deltaY;
                    } finally {
                        sl.unlockWrite(stamp);  //释放写锁
                    }
                }
                //乐观读锁的使用
                double distanceFromOrigin() {
                    long stamp = sl.tryOptimisticRead();  //获取乐观读锁
                    double currentX = x, currentY = y;
                    if (!sl.validate(stamp)) {  //检查获取乐观读锁之后，是否有其他写锁发生，有则返回false
                        stamp = sl.readLock();  //获取悲观读锁
                        try {
                            currentX = x;
                            currentY = y;
                        } finally {
                            sl.unlockRead(stamp);  //释放悲观读锁
                        }
                    }
                    return Math.sqrt(currentX * currentX + currentY * currentY);
                }
                //悲观读锁以及读锁升级写锁的使用
                void moveIfAtOrigin(double newX, double newY) {
                    long stamp = sl.readLock();  //获取悲观读锁
                    try {
                        while (x == 0.0 && y == 0.0) {
                            //读锁尝试转换为写锁：转换成功后相当于获取了写锁，转换失败相当于有写锁被占用
                            long ws = sl.tryConvertToWriteLock(stamp);
                            if (ws != 0L) {  //如果转换成功
                                stamp = ws;  //读锁的票据更新为写锁
                                x = newX;
                                y = newY;
                                break;
                            } else {  //如果转换失败
                                sl.unlockRead(stamp);  //释放读锁
                                stamp = sl.writeLock();  //强制获取写锁
                            }
                        }
                    } finally {
                        sl.unlock(stamp);  //释放所有锁
                    }
                }
            }
            //乐观读锁的意思就是先假定在这个锁获取期间，共享变量不会被改变，既然假定不会被改变，那就不需要上锁。
            //在获取乐观读锁之后进行了一些操作，然后又调用了validate方法，这个方法就是用来验证tryOptimisticRead之后，是否有写操作执行过，
            //如果有，则获取一个悲观读锁，这里的悲观读锁和ReentrantReadWriteLock中的读锁类似，也是个共享锁。
            //https://redspider.gitbook.io/concurrent/di-san-pian-jdk-gong-ju-pian/14#141-synchronized-de-bu-zu-zhi-chu
            //StampedLock的性能是非常优异的，基本上可以取代ReentrantReadWriteLock的作用。
}
