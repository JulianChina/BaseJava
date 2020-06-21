package EffectiveJava3rd.kConcurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

//78 同步访问共享的可变数据
public class K78 {
    //关键字synchronized可以保证在同一时刻，只有一个线程可以执行某一个方法，或者某一个代码块。
    //同步不仅可以阻止一个线程看到对象处于不一致的状态之中，它还可以保证进入同步方法或者同步代码块的每个线程，都能看到由同一个锁保护的之前所有的修改效果。

    //Java语言规范保证读或者写一个变量是原子的(atomic)，除非这个变量的类型为long或者double。
    //换句话说，读取一个非long或double类型的变量，可以保证返回值是某个线程保存在该变量中的，即使多个线程在没有同步的情况下并发地修改这个变量也是如此。
    //为了在线程之间进行可靠的通信，也为了互斥访问，同步是必要的。
    //千万不要使用Thread.stop方法。

    // Broken! - How long would you expect this program to run?
    public static class BrokenStopThread {
        private static Boolean stopRequested;

        public static void main(String[] args)
                throws InterruptedException {
            Thread backgroundThread = new Thread(() -> {
                int i = 0;
                while (!stopRequested)
                    i++;
            });
            backgroundThread.start();
            TimeUnit.SECONDS.sleep(1);
            stopRequested = true;
        }
    }

    // Properly synchronized cooperative thread termination
    public static class ProperStopThread {
        private static Boolean stopRequested;

        private static synchronized void requestStop() {
            stopRequested = true;
        }

        private static synchronized Boolean stopRequested() {
            return stopRequested;
        }

        public static void main(String[] args)
                throws InterruptedException {
            Thread backgroundThread = new Thread(() -> {
                int i = 0;
                while (!stopRequested())
                    i++;
            });
            backgroundThread.start();
            TimeUnit.SECONDS.sleep(1);
            requestStop();
        }
    }
    //除非读和写操作都被同步，否则无法保证同步能起作用。

    //虽然volatile修饰符不执行互斥访问，但它可以保证任何一个线程在读取该字段的时候都将看到最近刚刚被写入的值。
    // Cooperative thread termination with a volatile field
    public static class StopThread {
        private static volatile Boolean stopRequested;

        public static void main(String[] args)
                throws InterruptedException {
            Thread backgroundThread = new Thread(() -> {
                int i = 0;
                while (!stopRequested)
                    i++;
            });
            backgroundThread.start();
            TimeUnit.SECONDS.sleep(1);
            stopRequested = true;
        }
    }

    //使用AtomicLong类，它是java.util.concurrent.atomic的组成部分。这个包为在单个变量上进行免锁定、线程安全的编程提供了基本类型。虽然volatile只提供了同步的通信效果，但这个包还提供了原子性。
    // Lock-free synchronization with java.util.concurrent.atomic
    private static final AtomicLong nextSerialNum = new AtomicLong();
    public static long generateSerialNumber() {
        return nextSerialNum.getAndIncrement();
    }

    //避免本条目中所讨论到的问题的最佳办法是不共享可变的数据。要么共享不可变的数据，要么压根不共享。换句话说，将可变数据限制在单个线程中。

    //当多个线程共享可变数据的时候，每个读或者写数据的线程都必须执行同步。
    //未能同步共享可变数据会造成程序的活性失败(liveness failure)和安全性失败(safety failure)。
    //如果只需要线程之间的交互通信，而不需要互斥，volatile修饰符就是一种可以接受的同步形式，但要正确地使用它可能需要一些技巧。
}
