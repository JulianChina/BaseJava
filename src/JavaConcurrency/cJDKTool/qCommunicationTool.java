package JavaConcurrency.cJDKTool;

import java.util.Random;
import java.util.concurrent.*;

//17 通信工具类
public class qCommunicationTool {
    //17.1 Semaphore--限制线程的数量
        //Semaphore介绍
            //这个工具类提供的功能就是多个线程彼此"打信号"。
            //可以在构造函数中传入初始资源总数，以及是否使用“公平”的同步器。默认情况下，是非公平的。
        //Semaphore案例
            //Semaphore往往用于资源有限的场景中，去限制线程的数量。
            static class SemaphoreDemo {
                static class MyThread implements Runnable {
                    private int value;
                    private Semaphore semaphore;
                    public MyThread(int value, Semaphore semaphore) {
                        this.value = value;
                        this.semaphore = semaphore;
                    }

                    @Override
                    public void run() {
                        try {
                            semaphore.acquire();  //Semaphore默认的acquire方法是会让线程进入等待队列，且会抛出中断异常。
                            System.out.println(String.format("当前线程是%d, 还剩%d个资源，还有%d个线程在等待",
                                    value, semaphore.availablePermits(), semaphore.getQueueLength()));
                            Random rand = new Random();
                            Thread.sleep(rand.nextInt(1000));
                            semaphore.release();
                            System.out.println(String.format("线程%d释放了资源", value));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                public static void main(String[] args) {
                    Semaphore semaphore = new Semaphore(3);
                    for (int i = 0; i < 10; i++) {
                        new Thread(new MyThread(i, semaphore)).start();
                    }
                }
            }
        //Semaphore原理
            //Semaphore内部有一个继承了AQS的同步器Sync，重写了tryAcquireShared方法。在这个方法里，会去尝试获取资源。
            //如果获取失败（想要的资源数量小于目前已有的资源数量），就会返回一个负数（代表尝试获取资源失败）。然后当前线程就会进入AQS的等待队列。
    //17.2 Exchanger--两个线程交换数据
        //Exchanger类用于两个线程交换数据。它支持泛型，也就是说你可以在两个线程之间传送任何数据。
        static class ExchangerDemo {
            public static void main(String[] args) throws InterruptedException {
                Exchanger<String> exchanger = new Exchanger<>();
                new Thread(() -> {
                    try {
                        System.out.println("这是线程A，得到了另一个线程的数据：" + exchanger.exchange("这是来自线程A的数据"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                System.out.println("这个时候线程A是阻塞的，在等待线程B的数据");
                Thread.sleep(1000);

                new Thread(() -> {
                    try {
                        System.out.println("这是线程B，得到了另一个线程的数据：" + exchanger.exchange("这是来自线程B的数据"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
        //Exchanger类还有一个有超时参数的方法，如果在指定时间内没有另一个线程调用exchange，就会抛出一个超时异常。
        //exchange是可以重复使用的。也就是说。两个线程可以使用Exchanger在内存中不断地再交换数据。
    //17.3 CountDownLatch--线程等待直到计数器减为0时开始工作
        //CountDownLatch介绍
            //假设某个线程在执行任务之前，需要等待其它线程完成一些前置任务，必须等所有的前置任务都完成，才能开始执行本线程的任务。
        //CountDownLatch案例
            static class CountDownLatchDemo {
                static class PreTaskThread implements Runnable {
                    private String task;
                    private CountDownLatch countDownLatch;

                    public PreTaskThread(String task, CountDownLatch countDownLatch) {
                        this.task = task;
                        this.countDownLatch = countDownLatch;
                    }

                    @Override
                    public void run() {
                        try {
                            Random rand = new Random();
                            Thread.sleep(rand.nextInt(1000));
                            System.out.println(task + " - 任务完成");
                            countDownLatch.countDown();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                public static void main(String[] args) {
                    CountDownLatch countDownLatch = new CountDownLatch(3);
                    new Thread(() -> {
                        try {
                            System.out.println("等待数据加载...");
                            System.out.println(String.format("还有%d个前置任务", countDownLatch.getCount()));
                            countDownLatch.await();
                            System.out.println("数据加载完成，正式开始游戏！");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();

                    new Thread(new PreTaskThread("加载地图数据", countDownLatch)).start();
                    new Thread(new PreTaskThread("加载人物模型", countDownLatch)).start();
                    new Thread(new PreTaskThread("加载背景音乐", countDownLatch)).start();
                }
            }
        //CountDownLatch原理
            //CountDownLatch类的原理挺简单的，内部同样是一个基层了AQS的实现类Sync，且实现起来还很简单，可能是JDK里面AQS的子类中最简单的实现了。
            //构造器中的计数值（count）实际上就是闭锁需要等待的线程数量。这个值只能被设置一次，而且CountDownLatch没有提供任何机制去重新设置这个计数值。
    //17.4 CyclicBarrier--作用跟CountDownLatch类似，但是可以重复使用
        //CyclicBarrier介绍
            //CyclicBarrier拥有CountDownLatch的所有功能，还可以使用reset()方法重置屏障。
        //CyclicBarrier Barrier被破坏
            //如果在参与者（线程）在等待的过程中，Barrier被破坏，就会抛出BrokenBarrierException。可以用isBroken()方法检测Barrier是否被破坏。
        //CyclicBarrier案例
            static class CyclicBarrierDemo {
                static class PreTaskThread implements Runnable {

                    private String task;
                    private CyclicBarrier cyclicBarrier;

                    public PreTaskThread(String task, CyclicBarrier cyclicBarrier) {
                        this.task = task;
                        this.cyclicBarrier = cyclicBarrier;
                    }

                    @Override
                    public void run() {
                        // 假设总共三个关卡
                        for (int i = 1; i < 6; i++) {
                            try {
                                Random random = new Random();
                                Thread.sleep(random.nextInt(1000));
                                System.out.println(String.format("关卡%d的任务%s完成", i, task));
                                cyclicBarrier.await();
                            } catch (InterruptedException | BrokenBarrierException e) {
                                e.printStackTrace();
                            }
                            cyclicBarrier.reset(); // 重置屏障
                        }
                    }
                }

                public static void main(String[] args) {
                    CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> {
                        System.out.println("本关卡所有前置任务完成，开始游戏...");
                    });

                    new Thread(new PreTaskThread("加载地图数据", cyclicBarrier)).start();
                    new Thread(new PreTaskThread("加载人物模型", cyclicBarrier)).start();
                    new Thread(new PreTaskThread("加载背景音乐", cyclicBarrier)).start();
                }
            }
            //一旦调用await()方法的线程数量等于构造方法中传入的任务总量，就代表达到屏障了。
            //CyclicBarrier允许我们在达到屏障的时候可以执行一个任务，可以在构造方法传入一个Runnable类型的对象。
        //CyclicBarrier原理
            //CyclicBarrier虽说功能与CountDownLatch类似，但是实现原理却完全不同，CyclicBarrier内部使用的是Lock + Condition实现的等待/通知模式。
    //17.5 Phaser--增强的CyclicBarrier
        //Phaser介绍
            //CyclicBarrier在构造方法里传入"任务总量"parties之后，就不能修改这个值了，并且每次调用await()方法也只能消耗一个parties计数。但Phaser可以动态地调整任务总量！
            //Phaser终止的两种途径，Phaser维护的线程执行完毕或者onAdvance()返回true。
            //Phaser还能维护一个树状的层级关系，对于Task执行时间短的场景（竞争激烈），也就是说有大量的party，那可以把每个Phaser的任务量设置较小，多个Phaser共同继承一个父Phaser，构造的时候new Phaser(parentPhaser)。
        //Phaser案例
            static class PhaserDemo {
                static class PreTaskThread implements Runnable {

                    private String task;
                    private Phaser phaser;

                    public PreTaskThread(String task, Phaser phaser) {
                        this.task = task;
                        this.phaser = phaser;
                    }

                    @Override
                    public void run() {
                        try {
                            for (int i = 1; i < 5; i++) {
                                if (i >= 2 && "加载新手教程".equals(task)) {
                                    continue;
                                }
                                Random rand = new Random();
                                Thread.sleep(rand.nextInt(1000));
                                System.out.println(String.format("关卡%d，需要加载%d个模块，当前模块【%s】", i, phaser.getRegisteredParties(), task));
                                if (i == 1 && "加载新手教程".equals(task)) {
                                    System.out.println("下次关卡移除加载【新手教程】模块");
                                    phaser.arriveAndDeregister();
                                } else {
                                    phaser.arriveAndAwaitAdvance();
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                public static void main(String[] args) {
                    Phaser phaser = new Phaser(4) {
                        @Override
                        protected boolean onAdvance(int phase, int registeredParties) {
                            System.out.println(String.format("第%d次关卡准备完成", phase + 1));
                            return phase == 3 || registeredParties == 0;
                        }
                    };

                    new Thread(new PreTaskThread("加载地图数据", phaser)).start();
                    new Thread(new PreTaskThread("加载人物模型", phaser)).start();
                    new Thread(new PreTaskThread("加载背景音乐", phaser)).start();
                    new Thread(new PreTaskThread("加载新手教程", phaser)).start();
                }
            }
            //Phaser类用来控制某个阶段的线程数量很有用，但它并不在意这个阶段具体有哪些线程arrive，只要达到它当前阶段的parties值，就触发屏障。
            //Phaser是没有分辨具体是哪个线程的功能的，它在意的只是数量。
        //Phaser原理
            //Phaser类的原理相比起来要复杂得多，它内部使用了两个基于Fork-Join框架的原子类辅助。
}
