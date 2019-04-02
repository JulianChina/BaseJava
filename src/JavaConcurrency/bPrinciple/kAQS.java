package JavaConcurrency.bPrinciple;

//11 AQS
public class kAQS {
    //11.1 AQS简介
        //AQS是AbstractQueuedSynchronizer的简称，即抽象队列同步器。
        //AQS是一个用来构建锁和同步器的框架，使用AQS能简单且高效地构造出应用广泛的同步器，比如ReentrantLock，Semaphore，ReentrantReadWriteLock，SynchronousQueue，FutureTask等等皆是基于AQS的。
    //11.2 AQS的数据结构
        //AQS内部使用了一个volatile的变量state来作为资源的标识。
        //AQS类本身实现的是一些排队和阻塞的机制，它内部使用了一个先进先出（FIFO）的双端队列，并使用了两个指针head和tail用于标识队列的头部和尾部。
    //11.3 资源共享模式
        //独占模式（Exclusive）：资源是独占的，一次只能一个线程获取。如ReentrantLock。
        //共享模式（Share）：同时可以被多个线程获取，具体的资源个数可以通过参数指定。如Semaphore/CountDownLatch。
    //11.4 AQS的主要方法源码解析
        //获取资源
        //释放资源
}
