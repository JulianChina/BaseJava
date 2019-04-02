package JavaConcurrency.cJDKTool;

//12 线程池原理
public class lThreadPool {
    //12.1 为什么要使用线程池？
        //复用已创建的线程；
        //控制并发的数量（主要原因）；
        //可以对线程做统一管理；
    //12.2 线程池的原理
        //ThreadPoolExecutor提供的构造方法
        //ThreadPoolExecutor的策略
            //ThreadPoolExecutor类中定义了一个volatile int变量runState来表示线程池的状态 ，分别为RUNNING、SHURDOWN、STOP、TIDYING 、TERMINATED。
        //线程池主要的任务处理流程
            //https://redspider.gitbook.io/concurrent/di-san-pian-jdk-gong-ju-pian/12#121-wei-shen-me-yao-shi-yong-xian-cheng-chi
            //1.线程总数量<corePoolSize，无论线程是否空闲，都会新建一个核心线程执行任务（让核心线程数量快速达到corePoolSize，在核心线程数量<corePoolSize时）。注意，这一步需要获得全局锁。
            //2.线程总数量>=corePoolSize时，新来的线程任务会进入任务队列中等待，然后空闲的核心线程会依次去缓存队列中取任务来执行（体现了线程复用）。
            //3.当缓存队列满了，说明这个时候任务已经多到爆棚，需要一些“临时工”来执行这些任务了。于是会创建非核心线程去执行这个任务。注意，这一步需要获得全局锁。
            //4.缓存队列满了， 且总线程数达到了maximumPoolSize，则会采取上面提到的拒绝策略进行处理。
        //ThreadPoolExecutor如何做到线程复用的？
            //ThreadPoolExecutor在创建线程时，会将线程封装成工作线程worker,并放入工作线程组中，然后这个worker反复从阻塞队列中拿任务去执行。
    //12.3 四种常见的线程池
        //newCachedThreadPool
            //corePoolSize==0，不创建核心线程，线程池最大为Integer.MAX_VALUE。
            //当需要执行很多短时间的任务时，CacheThreadPool的线程复用率比较高， 会显著的提高性能。而且线程60s后会回收，意味着即使没有任务进来，CacheThreadPool并不会占用很多资源。
        //newFixedThreadPool
            //corePoolSize==maximumPoolSize，只会创建核心线程。
            //没有任务的情况下， FixedThreadPool占用资源更多。
        //newSingleThreadExecutor
            //corePoolSize == maximumPoolSize=1，有且仅有一个核心线程。
            //使用了LinkedBlockingQueue（容量很大），所以，不会创建非核心线程。所有任务按照先来先执行的顺序执行。
        //newScheduledThreadPool
            //创建一个定长线程池，支持定时及周期性任务执行。
}
