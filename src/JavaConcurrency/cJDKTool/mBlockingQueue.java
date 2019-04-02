package JavaConcurrency.cJDKTool;

import net.mindview.util.Print;

import java.util.concurrent.ArrayBlockingQueue;

//13 阻塞队列
public class mBlockingQueue {
    //13.1 阻塞队列的由来
        //生产者-消费者模式
        //BlockingQueue是Java util.concurrent包下重要的数据结构，区别于普通的队列，BlockingQueue提供了线程安全的队列访问方式，并发包下很多高级同步类的实现都是基于BlockingQueue实现的。
        //BlockingQueue一般用于生产者-消费者模式，生产者是往队列里添加元素的线程，消费者是从队列里拿元素的线程。BlockingQueue就是存放元素的容器。
    //13.2 BlockingQueue的操作方法
        //https://redspider.gitbook.io/concurrent/di-san-pian-jdk-gong-ju-pian/13#13-3-1-arrayblockingqueue
        //可能阻塞：put(2), take()
        //可能阻塞指定时长：offer(e, time, unit), poll(time, unit)
    //13.3 BlockingQueue的实现类
        //ArrayBlockingQueue
            //由数组结构组成的有界阻塞队列;
            //默认是非公平锁；
        //LinkedBlockingQueue
            //由链表结构组成的有界阻塞队列；
            //默认队列大小是Integer.MAX_VALUE，按先进先出排序；
        //DelayQueue
            //该队列中的元素只有当其指定的延迟时间到了，才能够从队列中获取到该元素。注入其中的元素必须实现java.util.concurrent.Delayed接口。
            //DelayQueue是一个没有大小限制的队列，因此往队列中插入数据的操作（生产者）永远不会被阻塞，而只有获取数据的操作（消费者）才会被阻塞。
        //PriorityBlockingQueue
            //基于优先级的无界阻塞队列（优先级的判断通过构造函数传入的Comparator对象来决定），内部控制线程同步的锁采用的是公平锁。
        //SynchronousQueue
            //没有任何内部容量，甚至连一个队列的容量都没有。并且每个put必须等待一个take，反之亦然。
        //生产者生产数据的速度绝对不能快于消费者消费数据的速度，否则时间一长，会最终耗尽所有的可用堆内存空间。
    //13.5 阻塞队列的原理
        //阻塞队列的原理很简单，利用了Lock锁的多条件（Condition）阻塞控制。
        //构造器，除了初始化队列的大小和是否是公平锁之外，还对同一个锁(ReentrantLock)lock初始化了两个监视器(Condition)，分别是notEmpty(标记消费者)和notFull(标记生产者)。
    //13.6 示例和使用场景
        //生产者-消费者模型
        static class ProducerConsumerTest {
            private int queueSize = 10;
            private ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(queueSize);
            class Producer extends Thread {
                @Override
                public void run() {
                    produce();
                }
                private void produce() {
                    while (true) {
                        try {
                            queue.put(1);
                            Print.print("向队列中插入一个元素，队列剩余空间：" + (queueSize - queue.size()));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            class Consumer extends Thread {
                @Override
                public void run() {
                    consume();
                }
                private void consume() {
                    while (true) {
                        try {
                            queue.take();
                            Print.print("从队列取走一个元素，队列剩余" + queue.size() + "个元素");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            public static void main(String[] args) {
                ProducerConsumerTest test = new ProducerConsumerTest();
                Producer producer = test.new Producer();
                Consumer consumer = test.new Consumer();
                producer.start();
                consumer.start();
            }
        }
        //线程池中使用阻塞队列
            //Java中的线程池就是使用阻塞队列实现的，在了解阻塞队列之后，无论是使用Executors类中已经提供的线程池，还是自己通过ThreadPoolExecutor实现线程池，都会更加得心应手。
}
