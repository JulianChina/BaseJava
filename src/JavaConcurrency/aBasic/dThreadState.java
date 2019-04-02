package JavaConcurrency.aBasic;

import net.mindview.util.Print;

//4 Java线程的状态及主要转化方法
public class dThreadState {
    //4.1 操作系统中的线程状态转换
        //在现在的操作系统中，线程是被视为轻量级进程的，所以操作系统线程的状态其实和操作系统进程的状态是一致的。
        //https://redspider.gitbook.io/concurrent/di-yi-pian-ji-chu-pian/4#41-cao-zuo-xi-tong-zhong-de-xian-cheng-zhuang-tai-zhuan-huan
    //4.2 Java线程的6个状态
        //NEW
        //RUNNABLE
        //BLOCKED
        //WAITING
            //调用如下3个方法会使线程进入等待状态：
                //Object.wait()：使当前线程处于等待状态直到另一个线程唤醒它；
                //Thread.join()：等待线程执行完毕，底层调用的是Object实例的wait方法；
                //LockSupport.park()：除非获得调用许可，否则禁用当前线程进行线程调度。
        //TIMED_WAITING
            //调用如下方法会使线程进入超时等待状态：
                //Thread.sleep(long millis)：使当前线程睡眠指定时间；
                //Object.wait(long timeout)：线程休眠指定时间，等待期间可以通过notify()/notifyAll()唤醒；
                //Thread.join(long millis)：等待当前线程最多执行millis毫秒，如果millis为0，则会一直执行；
                //LockSupport.parkNanos(long nanos)： 除非获得调用许可，否则禁用当前线程进行线程调度指定时间；
                //LockSupport.parkUntil(long deadline)：同上，也是禁止线程进行调度指定时间；
        //TERMINATED
    //4.3 线程状态的转换
        //https://redspider.gitbook.io/concurrent/di-yi-pian-ji-chu-pian/4#41-cao-zuo-xi-tong-zhong-de-xian-cheng-zhuang-tai-zhuan-huan
        //BLOCKED与RUNNABLE状态的转换
            static class ThreadStateChangeDemo {
                public void blockedTest() {
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            testMethod();
                        }
                    }, "a");
                    Thread b = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            testMethod();
                        }
                    }, "b");
                    a.start();
                    b.start();
                    Print.print(a.getName() + ":" + a.getState()); // 输出？
                    Print.print(b.getName() + ":" + b.getState()); // 输出？
                }

                // 同步方法争夺锁
                private synchronized void testMethod() {
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                public void blockedTest2() throws InterruptedException {
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            testMethod();
                        }
                    }, "a2");
                    Thread b = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            testMethod();
                        }
                    }, "b2");
                    a.start();
                    Thread.sleep(1000L); // 需要注意这里main线程休眠了1000毫秒，而testMethod()里休眠了2000毫秒
                    b.start();
                    System.out.println(a.getName() + ":" + a.getState()); // 输出？
                    System.out.println(b.getName() + ":" + b.getState()); // 输出？
                }


                public static void main(String[] args) throws InterruptedException {
//                    new ThreadStateChangeDemo().blockedTest();
                    new ThreadStateChangeDemo().blockedTest2();
                }
            }
        //WAITING状态与RUNNABLE状态的转换
        //TIMED_WAITING与RUNNABLE状态转换
        //线程中断
            //Thread.interrupt()：中断线程。这里的中断线程并不会立即停止线程，而是设置线程的中断状态为true（默认是flase）；
            ////Thread.interrupted()：测试当前线程是否被中断。线程的中断状态受这个方法的影响，意思是调用一次使线程中断状态设置为true，连续调用两次会使得这个线程的中断状态重新转为false；
            //Thread.isInterrupted()：测试当前线程是否被中断。与上面方法不同的是调用这个方法并不会影响线程的中断状态。
}
