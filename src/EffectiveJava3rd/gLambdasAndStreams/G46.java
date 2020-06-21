package EffectiveJava3rd.gLambdasAndStreams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

import java.util.stream.Collectors;

//46 优先考虑流中无副作用的函数
public class G46 {
    //流范式中最重要的部分是将计算结构化为一系列转换，其中每个阶段的结果尽可能接近前一阶段结果的纯函数(pure function)。
    //纯函数的结果仅取决于其输入：它不依赖于任何可变状态，也不更新任何状态。为了实现这一点，你传递给流操作的任何函数对象(中间操作和终结操作)都应该没有副作用。
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("test.txt");

        // Uses the streams API but not the paradigm--Don't do this!
        final Map<String, Long> freq = new HashMap<>();
//        try (Stream<String> words = new Scanner(file).tokens()) {
//            words.forEach(word -> { freq.merge(word.toLowerCase(), 1L, Long::sum); });
//        }

        // Proper use of streams to initialize a frequency table Map<String, Long> freq;
//        try (Stream<String> words = new Scanner(file).tokens()) {
//            Map<String, Long> freq2 = words.collect(Collectors.groupingBy(String::toLowerCase, Collectors.counting()));
//        }

        //forEach操作应仅用于报告流计算的结果，而不是用于执行计算。

        //改进后的代码使用了收集器(collector)，这是使用流必须学习的新概念。Collectors的API令人生畏：它有39个方法，其中一些方法有多达5个类型参数。
        //将流的元素收集到真正的集合中的收集器非常简单。有三个这样的收集器: toList()、toSet()和toCollection(collectionFactory)。它们分别返回集合、列表和程序员指定的集合类型。
        // Pipeline to get a top-ten list of words from a frequency table
        List<String> topTen = freq.keySet().stream()
                .sorted(Comparator.comparing(freq::get).reversed())
                .limit(10)
                .collect(Collectors.toList());

        //静态导入收集器的所有成员是一种惯例和明智的做法，因为它使流管道更易于阅读。

    }

    // Using a toMap collector to make a map from string to enum
//    private static final Map<String, G42.Operation> stringToEnum = Stream.of(values()).collect(Collectors.toMap(Object::toString, e -> e));

    //groupingBy方法返回收集器以生成基于分类器函数(classifier function)将元素分组到类别中的map。

    //编程流管道的本质是无副作用的函数对象。这适用于传递给流和相关对象的所有许多函数对象。
    //终结操作forEach仅应用于报告流执行的计算结果，而不是用于执行计算。
    //为了正确使用流，必须了解收集器。最重要的收集器工厂是toList，toSet，toMap，groupingBy和join。
}
