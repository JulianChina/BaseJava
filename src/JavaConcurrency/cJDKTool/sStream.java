package JavaConcurrency.cJDKTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Stream;

//19 Java 8 Stream并行计算原理
public class sStream {
    //19.1 Java 8 Stream简介
        //从Java 8 开始，我们可以使用Stream接口以及lambda表达式进行"流式计算"。
        //Stream接口有非常多用于集合计算的方法。
    //19.2 Stream单线程串行计算
    static class StreamDemo {
        public static void main(String[] args) {
            Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
                    .reduce((a, b) -> {
                        System.out.println(String.format("%s: %d + %d = %d",
                                Thread.currentThread().getName(), a, b, a + b));
                        return a + b;
                    })
                    .ifPresent(System.out::println);
        }
    }
    //19.3 Stream多线程并行计算
    static class StreamParallelDemo {
        public static void main(String[] args) {
            Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
                    .parallel()
                    .reduce((a, b) -> {
                        System.out.println(String.format("%s: %d + %d = %d",
                                Thread.currentThread().getName(), a, b, a + b));
                        return a + b;
                    })
                    .ifPresent(System.out::println);
        }
    }
    //19.4 从源码看Stream并行计算原理
        //Stream的并行计算底层其实是使用的Fork/Join框架。
    //19.5 Stream并行计算的性能提升
    static class StreamParallelDemo2 {
        public static void main(String[] args) {
            System.out.println(String.format("本计算机的核数：%d", Runtime.getRuntime().availableProcessors()));

            // 产生1000w个随机数(1 ~ 100)，组成列表
            Random random = new Random();
            List<Integer> list = new ArrayList<>(1000_0000);

            for (int i = 0; i < 1000_0000; i++) {
                list.add(random.nextInt(100));
            }

            long prevTime = getCurrentTime();
            list.stream().reduce((a, b) -> a + b).ifPresent(System.out::println);
            System.out.println(String.format("单线程计算耗时：%d", getCurrentTime() - prevTime));

            prevTime = getCurrentTime();
            list.stream().parallel().reduce((a, b) -> a + b).ifPresent(System.out::println);
            System.out.println(String.format("多线程计算耗时：%d", getCurrentTime() - prevTime));
        }

        private static long getCurrentTime() {
            return System.currentTimeMillis();
        }
    }
}
