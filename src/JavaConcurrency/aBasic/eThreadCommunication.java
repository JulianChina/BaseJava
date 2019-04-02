package JavaConcurrency.aBasic;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

//5 Java线程间的通信
public class eThreadCommunication {
    //锁与同步
    //等待/通知机制
        //notify()方法会随机叫醒一个正在等待的线程，而notifyAll()会叫醒所有正在等待的线程。
        static class WaitAndNotify {
            private static Object lock = new Object();
            static class ThreadA implements Runnable {
                @Override
                public void run() {
                    synchronized (lock) {
                        for (int i = 0; i < 5; i++) {
                            try {
                                System.out.println("ThreadA: " + i);
                                lock.notify();
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        lock.notify();
                    }
                }
            }
            static class ThreadB implements Runnable {
                @Override
                public void run() {
                    synchronized (lock) {
                        for (int i = 0; i < 5; i++) {
                            try {
                                System.out.println("ThreadB: " + i);
                                lock.notify();
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        lock.notify();
                    }
                }
            }
            public static void main(String[] args) throws InterruptedException {
                new Thread(new ThreadA()).start();
                Thread.sleep(1000);
                new Thread(new ThreadB()).start();
            }
        }
    //信号量
        static class Signal {
            private static volatile int signal = 0;
            static class ThreadA implements Runnable {
                @Override
                public void run() {
                    while (signal < 5) {
                        if (signal % 2 == 0) {
                            System.out.println("threadA: " + signal);
                            synchronized (this) {
                                signal++;
                            }
                        }
                    }
                }
            }
            static class ThreadB implements Runnable {
                @Override
                public void run() {
                    while (signal < 5) {
                        if (signal % 2 == 1) {
                            System.out.println("threadB: " + signal);
                            synchronized (this) {
                                signal = signal + 1;
                            }
                        }
                    }
                }
            }
            public static void main(String[] args) throws InterruptedException {
                new Thread(new ThreadA()).start();
                Thread.sleep(1000);
                new Thread(new ThreadB()).start();
            }
        }
        //volatile变量需要进行原子操作。signal++并不是一个原子操作，所以我们需要使用synchronized给它“上锁”。
    //管道
        //管道是基于“管道流”的通信方式。JDK提供了PipedWriter、PipedReader、PipedOutputStream、PipedInputStream。
        static class Pipe {
            static class ReaderThread implements Runnable {
                private PipedReader reader;

                public ReaderThread(PipedReader reader) {
                    this.reader = reader;
                }

                @Override
                public void run() {
                    System.out.println("this is reader");
                    int receive = 0;
                    try {
                        while ((receive = reader.read()) != -1) {
                            System.out.print((char)receive);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            static class WriterThread implements Runnable {

                private PipedWriter writer;

                public WriterThread(PipedWriter writer) {
                    this.writer = writer;
                }

                @Override
                public void run() {
                    System.out.println("this is writer");
                    int receive = 0;
                    try {
                        writer.write("test");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            public static void main(String[] args) throws IOException, InterruptedException {
                PipedWriter writer = new PipedWriter();
                PipedReader reader = new PipedReader();
                writer.connect(reader); // 这里注意一定要连接，才能通信

                new Thread(new ReaderThread(reader)).start();
                Thread.sleep(1000);
                new Thread(new WriterThread(writer)).start();
            }
        }
        //使用管道多半与I/O流相关。当我们一个线程需要先另一个线程发送一个信息（比如字符串）或者文件等等时，就需要使用管道通信了。
    //其他通信相关
        //join方法
        static class Join {
            static class ThreadA implements Runnable {

                @Override
                public void run() {
                    try {
                        System.out.println("我是子线程，我先睡一秒");
                        Thread.sleep(1000);
                        System.out.println("我是子线程，我睡完了一秒");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            public static void main(String[] args) throws InterruptedException {
                Thread thread = new Thread(new ThreadA());
                thread.start();
                thread.join();
                System.out.println("如果不加join方法，我会先被打出来，加了就不一样了");
            }
        }
        //sleep方法
        //ThreadLocal类
            //ThreadLocal是一个本地线程副本变量工具类。内部是一个弱引用的Map来维护。
            //ThreadLocal类并不属于多线程间的通信，而是让每个线程有自己”独立“的变量，线程之间互不影响。它为每个线程都创建一个副本，每个线程可以访问自己内部的副本变量。
            static class ThreadLocalDemo {
                static class ThreadA implements Runnable {
                    private ThreadLocal<String> threadLocal;

                    public ThreadA(ThreadLocal<String> threadLocal) {
                        this.threadLocal = threadLocal;
                    }

                    @Override
                    public void run() {
                        threadLocal.set("A");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("ThreadA输出：" + threadLocal.get());
                    }
                }

                static class ThreadB implements Runnable {
                    private ThreadLocal<String> threadLocal;

                    public ThreadB(ThreadLocal<String> threadLocal) {
                        this.threadLocal = threadLocal;
                    }

                    @Override
                    public void run() {
                        threadLocal.set("B");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("ThreadB输出：" + threadLocal.get());
                    }
                }

                public static void main(String[] args) {
                    ThreadLocal<String> threadLocal = new ThreadLocal<>();
                    new Thread(new ThreadA(threadLocal)).start();
                    new Thread(new ThreadB(threadLocal)).start();
                }
            }

    //InheritableThreadLocal
        //InheritableThreadLocal类与ThreadLocal类稍有不同，它不仅仅是当前线程可以存取副本值，而且它的子线程也可以存取这个副本值。
}
