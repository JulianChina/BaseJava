package EffectiveJava3rd.kConcurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

//81 并发工具优于wait和notify
public class K81 {
    //既然正确地使用wait和notify比较困难，就应该用更高级的并发工具来代替。
    //java.util.concurrent中更高级的工具分成三类：Executor Framework、并发集合(Concurrent Collection)以及同步器(Synchronizer)。
    //并发集合中不可能排除并发活动；将它锁定没有什么作用，只会使程序的速度变慢。
    //并发集合导致同步的集合大多被废弃了。比如， 应该优先使用 ConcurrentHashMap ，而不是使用 Collections.synchronizedMap 。
    //只要用并发 Map 替换同步 Map ，就可以极大地提升并发应用程序的性能。

    //大多数ExecutorService实现(包括ThreadPoolExecutor)都使用了一个BlockingQueue。

    //同步器(Synchronizer)是使线程能够等待另一个线程的对象，允许它们协调动作。
    // 最常用的同步器是CountDownLatch和Semaphore。
    // 较不常用的是CyclicBarrier和Exchanger。
    // 功能最强大的同步器是Phaser。
    //倒计数锁存器(Countdown Latch)是一次性的障碍，允许一个或者多个线程等待一个或者多个其他线程来做某些事情。
    // CountDownLatch的唯一构造器带有一个int类型的参数，这个int参数是指允许所有在等待的线程被处理之前，必须在锁存器上调用countDown方法的次数。
    // Simple framework for timing concurrent execution
    public static long time(Executor executor, int concurrency, Runnable action) throws InterruptedException {
        CountDownLatch ready = new CountDownLatch(concurrency);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(concurrency);
        for (int i = 0; i < concurrency; i++) {
            executor.execute(() -> {
                ready.countDown();
                // Tell timer we're ready
                try {
                    start.await();
                    // Wait till peers are ready
                    action.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                    // Tell timer we're done
                }
            });
        }
        ready.await();
        // Wait for all workers to be ready
        long startNanos = System.nanoTime();
        start.countDown();
        // And they're off!
        done.await();
        // Wait for all workers to finish
        return System.nanoTime() - startNanos;
    }
    //还有一些细节值得注意。传递给time方法的executor必须允许创建至少与指定并发级别一样多的线程，否则这个测试就永远不会结束。这就是线程饥饿死锁(thread starvationdeadlock)
    //对于间歇式的定时，始终应该优先使用System.nanoTime，而不是使用System.currentTimeMillis。因为System.nanoTime更准确，也更精确，它不受系统的实时时钟的调整所影响。
    //那三个倒计数锁存器其实可以用一个CyclicBarrier或者Phaser来代替。这样得到的代码更加简洁，但是理解起来比较困难。

    //wait方法被用来使线程等待某个条件。它必须在同步区域内部被调用，这个同步区域将对象锁定在了调用wait方法的对象上。
    /*
    // The standard idiom for using the wait method
    synchronized (obj) {
        while (<condition does not hold>)
        obj.wait();
        // (Releases lock, and reacquires on wakeup)
        ... // Perform action appropriate to condition
    }
    */
    //始终应该使用wait循环模式来调用wait方法；永远不要在循环之外调用wait方法。循环会在等待之前和之后对条件进行测试。

    //没有理由在新代码中使用wait方法和notify方法，即使有，也是极少的。
    //如果你在维护使用wait方法和notify方法的代码，务必确保始终是利用标准的模式从while循环内部调用wait方法。
    // 一般情况下，应该优先使用notifyAll方法，而不是使用notify方法。如果使用notify方法，请一定要小心，以确保程序的活性。
}
