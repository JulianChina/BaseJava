package JavaConcurrency.aBasic;

import net.mindview.util.Print;

import java.util.stream.IntStream;

//3 线程组和线程优先级
public class cThreadGroupAndPriority {
    //3.1 线程组
        static class ThreadGroupDemo {
            public static void main(String[] args) {
                Thread testThread = new Thread(() -> {
                    Print.print("testThread当前线程组名字：" + Thread.currentThread().getThreadGroup().getName());
                    Print.print("testThread线程名字：" + Thread.currentThread().getName());
                });
                testThread.start();
                Print.print("执行main方法线程名字：" + Thread.currentThread().getName());
            }
        }
        //ThreadGroup是一个标准的向下引用的树状结构，这样设计的原因是防止"上级"线程被"下级"线程引用而无法有效地被GC回收。
    //3.2 线程的优先级
        //Java程序中对线程所设置的优先级只是给操作系统一个建议，操作系统不一定会采纳。而真正的调用顺序，是由操作系统的线程调度算法决定的。
        static class ThreadPriorityDemo {
            static class T1 extends Thread {
                @Override
                public void run() {
                    super.run();
                    Print.print(String.format("当前执行的线程是：%s，优先级：%d",
                            Thread.currentThread().getName(),
                            Thread.currentThread().getPriority()));
                }
            }

            public static void main(String[] args) {
                IntStream.range(1, 10).forEach(i -> {
                    Thread thread = new Thread(new T1());
                    thread.setPriority(i);
                    thread.start();
                });
            }
        }
        //Java提供一个线程调度器来监视和控制处于RUNNABLE状态的线程。线程的调度策略采用抢占式，优先级高的线程比优先级低的线程会有更大的几率优先执行。
        //在优先级相同的情况下，按照“先到先得”的原则。每个Java程序都有一个默认的主线程，就是通过JVM启动的第一个线程main线程。

        //还有一种线程称为守护线程（Daemon），守护线程默认的优先级比较低。
        static class DaemonThreadDemo {
            public static void main(String[] args) {
                ThreadGroup threadGroup = new ThreadGroup("t1");
                threadGroup.setMaxPriority(6);
                Thread thread = new Thread(threadGroup, "thread");
                thread.setPriority(9);
                Print.print("我是线程组的优先级：" + threadGroup.getMaxPriority());
                Print.print("我是线程的优先级：" + thread.getPriority());

                //复制一个线程数组到一个线程组
                Thread[] threads = new Thread[threadGroup.activeCount()];
                ThreadGroup threadGroup1 = new ThreadGroup("copy");
                threadGroup1.enumerate(threads);
            }
        }
        //如果某个线程优先级大于线程所在线程组的最大优先级，那么该线程的优先级将会失效，取而代之的是线程组的最大优先级。
    //3.3 线程组的常用方法及数据结构
        //线程组的常用方法
            //获取当前的线程组名字：Thread.currentThread().getThreadGroup().getName()
            //复制线程组
            //线程组统一异常处理
                static class ThreadGroupException {
                    public static void main(String[] args) {
                        ThreadGroup threadGroup = new ThreadGroup("threadGroupException") {
                            @Override
                            public void uncaughtException(Thread t, Throwable e) {
                                Print.print(t.getName() + ": " + e.getMessage());
                            }
                        };
                        Thread thread = new Thread(threadGroup, new Runnable() {
                            @Override
                            public void run() {
                                throw new RuntimeException("测试异常");
                            }
                        });
                        thread.start();
                    }
                }
        //线程组的数据结构
            //SecurityManager sm = System.getSecurityManager();
            //线程组可以起到统一控制线程的优先级和检查线程的权限的作用。
}
