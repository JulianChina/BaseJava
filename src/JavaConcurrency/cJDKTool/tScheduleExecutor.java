package JavaConcurrency.cJDKTool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//20 计划任务
public class tScheduleExecutor {
    //自JDK 1.5 开始，JDK提供了ScheduledThreadPoolExecutor类用于计划任务（又称定时任务），这个类有两个用途：
        //1.在给定的延迟之后运行任务
        //2.周期性重复执行任务
    //20.1 使用案例
    static class ThreadPoolDemo {
        private static final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1, Executors.defaultThreadFactory());
        private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public static void main(String[] args) {
            executor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    if (haveMsgAtCurrentTime()) {
                        System.out.println(sdf.format(new Date()));
                        System.out.println("大家注意了，我要发消息了");
                    }
                }
            }, 1, 1, TimeUnit.SECONDS);
        }

        public static boolean haveMsgAtCurrentTime() {
            return true;
        }
    }
    //20.2 类结构
        //scheduleAtFixedRate: 开始执行任务后，定时器每隔period时长检查该任务是否完成，如果完成则再次启动任务，否则等该任务结束后才再次启动任务。
        //scheduleWithFixDelay: 开始执行任务后，每当任务执行完成后，等待delay时长，再次执行任务。
    //20.3 主要方法介绍
        //schedule
            //https://redspider.gitbook.io/concurrent/di-san-pian-jdk-gong-ju-pian/20#201-shi-yong-an-li
            //Delayed接口
            //ScheduledFuture接口
            //RunnableScheduledFuture接口
            //ScheduledFutureTask类
        //scheduleAtFixedRate
        //scheduleAtFixedDelay
        //delayedExecute
    //20.4 DelayedWorkQueue
        //ScheduledThreadPoolExecutor使用了DelayedWorkQueue保存等待的任务。
        //该等待队列队首应该保存的是最近将要执行的任务，所以worker只关心队首任务即可，如果队首任务的开始执行时间还未到，worker也应该继续等待。
        //DelayedWorkQueue是一个无界优先队列，使用数组存储，底层是使用堆结构来实现优先队列的功能。
        //take
        //offer
    //20.5 总结
        //内部使用优化的DelayQueue来实现，由于使用队列来实现定时器，有出入队调整堆等操作，所以定时并不是非常非常精确。
}
