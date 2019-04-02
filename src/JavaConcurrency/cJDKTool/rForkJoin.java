package JavaConcurrency.cJDKTool;

import ThinkingInJava.tAnnotation.TAnnotation;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

//18 Fork/Join框架
public class rForkJoin {
    //18.1 什么是Fork/Join
        //Fork/Join框架是一个实现了ExecutorService接口的多线程处理器，它专为那些可以通过递归分解成更细小的任务而设计，最大化的利用多核处理器来提高应用程序的性能。
        //与其他ExecutorService相关的实现相同的是，Fork/Join框架会将任务分配给线程池中的线程。而与之不同的是，Fork/Join框架在执行任务时使用了工作窃取算法。
        //fork就是要使一个大任务分解成若干个小任务，而join就是最后将各个小任务的结果结合起来得到大任务的结果。
    //18.2 工作窃取算法
        //工作窃取算法指的是在多线程执行不同任务队列的过程中，某个线程执行完自己队列的任务后从其他线程的任务队列里窃取任务来执行。
        //当一个线程窃取另一个线程的时候，为了减少两个任务线程之间的竞争，我们通常使用双端队列来存储任务。被窃取的任务线程都从双端队列的头部拿任务执行，而窃取其他任务的线程从双端队列的尾部执行任务。
        //当一个线程在窃取任务时要是没有其他可用的任务了，这个线程会进入阻塞状态以等待再次"工作"。
    //18.3 Fork/Join的具体实现
        //ForkJoinTask
            //ForkJoinTask是一个类似普通线程的实体，但是比普通线程轻量得多。
            //fork()方法:使用线程池中的空闲线程异步提交任务；其实fork()只做了一件事，那就是把任务推入当前工作线程的工作队列里。
            //join()方法：等待处理任务的线程处理完毕，获得返回值。
            //Thread.join()会使线程阻塞，而ForkJoinPool.join()会使线程免于阻塞。
            //通常情况下，在创建任务的时候我们一般不直接继承ForkJoinTask，而是继承它的子类RecursiveAction和RecursiveTask。
            //RecursiveAction可以看做是无返回值的ForkJoinTask，RecursiveTask是有返回值的ForkJoinTask。
        //ForkJoinPool
            //ForkJoinPool是用于执行ForkJoinTask任务的执行（线程）池。
            //ForkJoinPool管理着执行池中的线程和任务队列，此外，执行池是否还接受任务，显示线程的运行状态也是在这里处理。
            //ForkJoinPool与传统线程池最显著的区别就是它维护了一个工作队列数组（volatile WorkQueue[] workQueues，ForkJoinPool中的每个工作线程都维护着一个工作队列）。
    //18.4 Fork/Join的使用
        //ForkJoinPool负责管理线程和任务，ForkJoinTask实现fork和join操作。
        static class FibonacciTest {
            class Fibonacci extends RecursiveTask<Integer> {
                int n;
                public Fibonacci(int n) {
                    this.n = n;
                }

                // 主要的实现逻辑都在compute()里
                @Override
                protected Integer compute() {
                    // 这里先假设 n >= 0
                    if (n <= 1) {
                        return n;
                    } else {
                        // f(n-1)
                        Fibonacci f1 = new Fibonacci(n - 1);
                        f1.fork();
                        // f(n-2)
                        Fibonacci f2 = new Fibonacci(n - 2);
                        f2.fork();
                        // f(n) = f(n-1) + f(n-2)
                        return f1.join() + f2.join();
                    }
                }
            }

            @TAnnotation.Test
            public void testFib() throws ExecutionException, InterruptedException {
                ForkJoinPool forkJoinPool = new ForkJoinPool();
                System.out.println("CPU核数：" + Runtime.getRuntime().availableProcessors());
                long start = System.currentTimeMillis();
                Fibonacci fibonacci = new Fibonacci(40);
                Future<Integer> future = forkJoinPool.submit(fibonacci);
                System.out.println(future.get());
                long end = System.currentTimeMillis();
                System.out.println(String.format("耗时：%d millis", end - start));
            }

            // 普通递归，复杂度为O(2^n)
            public int plainRecursion(int n) {
                if (n == 1 || n == 2) {
                    return 1;
                } else {
                    return plainRecursion(n -1) + plainRecursion(n - 2);
                }
            }

            @TAnnotation.Test
            public void testPlain() {
                long start = System.currentTimeMillis();
                int result = plainRecursion(40);
                long end = System.currentTimeMillis();
                System.out.println("计算结果:" + result);
                System.out.println(String.format("耗时：%d millis",  end -start));
            }

            // 通过循环来计算，复杂度为O(n)
            private int computeFibonacci(int n) {
                // 假设n >= 0
                if (n <= 1) {
                    return n;
                } else {
                    int first = 1;
                    int second = 1;
                    int third = 0;
                    for (int i = 3; i <= n; i ++) {
                        // 第三个数是前两个数之和
                        third = first + second;
                        // 前两个数右移
                        first = second;
                        second = third;
                    }
                    return third;
                }
            }

            @TAnnotation.Test
            public void testComputeFibonacci() {
                long start = System.currentTimeMillis();
                int result = computeFibonacci(40);
                long end = System.currentTimeMillis();
                System.out.println("计算结果:" + result);
                System.out.println(String.format("耗时：%d millis",  end -start));
            }

            public static void main(String[] args) throws ExecutionException, InterruptedException {
                FibonacciTest test = new FibonacciTest();
                test.testFib();
                test.testPlain();
                test.testComputeFibonacci();
            }
        }
        //Java 8 Stream的并行操作底层就是用到了Fork/Join框架。
}
