package EffectiveJava3rd.gLambdasAndStreams;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

//45 明智审慎地使用Stream
public class G45 {
    //在Java8中添加了Stream API，提供了两个关键的抽象：流(Stream)，表示有限或无限的数据元素序列，以及流管道(stream pipeline)，表示对这些元素的多级计算。
    //Stream中的元素可以来自任何地方。常见的源包括集合，数组，文件，正则表达式模式匹配器，伪随机数生成器和其他流。
    //流中的数据元素可以是对象引用或基本类型。支持三种基本类型：int，long和double。
    //流管道由源流(source stream)的零或多个中间操作和一个终结操作组成。中间操作都将一个流转换为另一个流，其元素类型可能与输入流相同或不同。终结操作对流执行最后一次中间操作产生的最终计算。
    // Prints all large anagram groups in a dictionary iteratively
    public static class Anagrams {
        public static void main(String[] args) throws IOException {
            File dictionary = new File(args[0]);
            int minGroupSize = Integer.parseInt(args[1]);
            Map<String, Set<String>> groups = new HashMap<>();
            try (Scanner s = new Scanner(dictionary)) {
                while (s.hasNext()) {
                    String word = s.next();
                    groups.computeIfAbsent(alphabetize(word), (unused) -> new TreeSet<>()).add(word);
                }
            }

            for (Set<String> group : groups.values())
                if (group.size() >= minGroupSize)
                    System.out.println(group.size() + ": " + group);
        }

        private static String alphabetize(String s) {
            char[] a = s.toCharArray();
            Arrays.sort(a);
            return new String(a);
        }
    }

    //过度使用流使程序难于阅读和维护。

    // Tasteful use of streams enhances clarity and conciseness
    public static class Anagrams2 {
        public static void main(String[] args) throws IOException {
            Path dictionary = Paths.get(args[0]);
            int minGroupSize = Integer.parseInt(args[1]);
            try (Stream<String> words = Files.lines(dictionary)) {
                words.collect(groupingBy(word -> alphabetize(word)))
                        .values()
                        .stream()
                        .filter(group -> group.size() >= minGroupSize)
                        .forEach(g -> System.out.println(g.size() + ": " + g));
            }
        }
        // alphabetize method is the same as in original version
        private static String alphabetize(String s) {
            char[] a = s.toCharArray();
            Arrays.sort(a);
            return new String(a);
        }
    }

    //使用辅助方法对于流管道中的可读性比在迭代代码中更为重要，因为管道缺少显式类型信息和命名临时变量。
    //在没有显式类型的情况下，仔细命名lambda参数对于流管道的可读性至关重要。
    //理想情况下，应该避免使用流来处理char值。
    //重构现有代码以使用流，并仅在有意义的情况下在新代码中使用它们。

    static Stream<BigInteger> primes() {
        return Stream.iterate(BigInteger.valueOf(2), BigInteger::nextProbablePrime);
    }
    public static void main(String[] args) {
        primes().map(p -> BigInteger.valueOf(2).pow(p.intValueExact()).subtract(BigInteger.valueOf(1)))
                .filter(mersenne -> mersenne.isProbablePrime(50))
                .limit(20)
                .forEach(System.out::println);
    }

    private enum Suit {}
    private enum Rank {}
    private static class Card {
        private Suit suit;
        private Rank rank;
        Card(Suit suit, Rank rank) {
            this.suit = suit;
            this.rank = rank;
        }
    }
    // Iterative Cartesian product computation
    private static List<Card> newDeck() {
        List<Card> result = new ArrayList<>();
        for (Suit suit : Suit.values())
            for (Rank rank : Rank.values())
                result.add(new Card(suit, rank));
        return result;
    }

    // Stream-based Cartesian product computation
    private static List<Card> newDeck2() {
        return Stream.of(Suit.values()).flatMap(
                suit -> Stream.of(Rank.values()).map(
                        rank -> new Card(suit, rank)))
                .collect(toList());
    }

    //如果不确定一个任务是通过流还是迭代更好地完成，那么尝试这两种方法，看看哪一种效果更好。
}
