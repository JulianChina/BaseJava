package JavaConcurrency.cJDKTool;

//15 并发集合容器简介
public class oConcurrentCollections {
    //15.1 同步容器与并发容器
        //即使是Vector这样线程安全的类，在面对多线程下的复合操作的时候也是需要通过客户端加锁的方式保证原子性。
        //并发容器是Java5提供的在多线程编程下用于代替同步容器，针对不同的应用场景进行设计，提高容器的并发访问性，同时定义了线程安全的复合操作。
    //15.2 并发容器类介绍
        //https://redspider.gitbook.io/concurrent/di-san-pian-jdk-gong-ju-pian/15
        //并发Map
            //ConcurrentMap接口：putIfAbsent\remove\replace\replace
            //ConcurrentHashMap类：提供了一种粒度更细的加锁机制来实现在多线程下更高的性能，这种机制叫分段锁(Lock Striping)。
                //在并发环境下将实现更高的吞吐量，而在单线程环境下只损失非常小的性能。
                //可以这样理解分段锁，就是将数据分段，对每一段数据分配一把锁。当一个线程占用锁访问其中一个段数据的时候，其他段的数据也能被其他线程访问。
            //ConcurrentNavigableMap接口与ConcurrentSkipListMap类
                //跳表（SkipList）的数据结构，是一种"空间换时间"的数据结构，可以使用CAS来保证并发安全性。
        //并发Queue
            //JDK并没有提供线程安全的List类，因为对List来说，很难去开发一个通用并且没有并发瓶颈的线程安全的List。
            //JDK提供了对队列和双端队列的线程安全的类：ConcurrentLinkedDeque和ConcurrentLinkedQueue。
        //并发Set
            //JDK提供了ConcurrentSkipListSet，是线程安全的有序的集合。底层是使用ConcurrentSkipListMap实现。
            //谷歌的guava框架实现了一个线程安全的ConcurrentHashSet。
}
