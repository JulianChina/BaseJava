package JavaConcurrency.aBasic;

import net.mindview.util.Print;

import java.util.concurrent.*;

//2 Java多线程入门类和接口
public class bMultiThread {
    //2.1 Thread类和Runnable接口
        //继承Thread类
        //实现Runnable接口
        //Thread类构造方法
        //Thread类的几个常用方法
            //currentThread()
            //start()
            //yield()
            //sleep()
            //join()
        //Thread类与Runnable接口的比较
            //通常优先使用"实现Runnable接口"这种方式来自定义线程类；
    //2.2 Callable、Future与FutureTask
        //Callable接口
            //Callable一般是配合线程池工具ExecutorService来使用的。
            static class Task implements Callable<Integer> {
                @Override
                public Integer call() throws Exception {
                    Thread.sleep(2);
                    return 2;
                }

                public static void main(String[] args) throws ExecutionException, InterruptedException {
                    ExecutorService exec = Executors.newCachedThreadPool();
                    Task task = new Task();
                    Future<Integer> result = exec.submit(task);
                    Print.print("Callable Result:" + result.get());

                    //FutureTask
                    ExecutorService executor = Executors.newCachedThreadPool();
                    FutureTask<Integer> futureTask = new FutureTask<>(new Task());
                    executor.submit(futureTask);
                    Print.print("FutureTask Result:" + futureTask.get());
                }
            }
        //Future接口
            //cancel方法是试图取消一个线程的执行。
            //如果为了可取消性而使用Future但又不提供可用的结果，则可以声明Future<?>形式类型、并返回null作为底层任务的结果。
        //FutureTask类
            //在很多高并发的环境下，有可能Callable和FutureTask会创建多次。FutureTask能够在高并发环境下确保任务只执行一次。
        //FutureTask的几个状态
            //state可能的状态转变路径如下：
                // NEW -> COMPLETING -> NORMAL
                // NEW -> COMPLETING -> EXCEPTIONAL
                // NEW -> CANCELLED
                // NEW -> INTERRUPTING -> INTERRUPTED
}
