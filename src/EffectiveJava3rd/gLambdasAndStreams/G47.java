package EffectiveJava3rd.gLambdasAndStreams;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

//47 优先使用Collection而不是Stream来作为方法的返回类型
public class G47 {
    //如果该方法仅用于启用for-each循环，或者返回的序列不能实现某些Collection方法(通常是contains(Object))，则使用迭代(Iterable)接口。
    //如果返回的元素是基本类型或有严格的性能要求，则使用数组。
    //流不会使迭代过时：编写好的代码需要明智地结合流和迭代。
    // Adapter from  Stream<E> to Iterable<E>
    public static <E> Iterable<E> iterableOf(Stream<E> stream) {
        return stream::iterator;
    }

    //使用此适配器，可以使用for-each语句迭代任何流:
//    public static void testMethod() {
//        for (ProcessHandle p : iterableOf(ProcessHandle.allProcesses())) {
//            // Process the process
//        }
//    }

    // Adapter from Iterable<E> to Stream<E>
    public static <E> Stream<E> streamOf(Iterable<E> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    //如果你正在编写一个返回对象序列的方法，并且它只会在流管道中使用，那么当然可以自由地返回流。
    //类似地，返回仅用于迭代的序列的方法应该返回一个Iterable 。
    //Collection接口是Iterable的子类型，并且具有stream方法，因此它提供迭代和流访问。
    //Collection或适当的子类型通常是公共序列返回方法的最佳返回类型。数组还使用Arrays.asList和Stream.of方法提供简单的迭代和流访问。
    //如果返回的序列小到足以容易地放入内存中，那么最好返回一个标准集合实现，例如ArrayList或HashSet。但是不要在内存中存储大的序列，只是为了将它作为集合返回。

    // Returns the power set of an input set as custom collection
    public static class PowerSet {
        public static final <E> Collection<Set<E>> of(Set<E> s) {
            List<E> src = new ArrayList<>(s);
            if (src.size() > 30)
                throw new IllegalArgumentException("Set too big " + s);
            return new AbstractList<Set<E>>() {
                @Override
                public int size() {
                    return 1 << src.size(); // 2 to the power srcSize
                }

                @Override
                public boolean contains(Object o) {
                    return o instanceof Set && src.containsAll((Set) o);
                }

                @Override
                public Set<E> get(int index) {
                    Set<E> result = new HashSet<>();
                    for (int i = 0; index != 0; i++, index >>= 1)
                        if ((index & 1) == 1)
                            result.add(src.get(i));
                    return result;
                }
            };
        }
    }

    // Returns a stream of all the sublists of its input list
    public static class SubLists {
        public static <E> Stream<List<E>> of(List<E> list) {
            return Stream.concat(Stream.of(Collections.emptyList()), prefixes(list).flatMap(SubLists::suffixes));
        }

        private static <E> Stream<List<E>> prefixes(List<E> list) {
            return IntStream.rangeClosed(1, list.size()).mapToObj(end -> list.subList(0, end));
        }

        private static <E> Stream<List<E>> suffixes(List<E> list) {
            return IntStream.range(0, list.size()).mapToObj(start -> list.subList(start, list.size()));
        }
    }

    // Returns a stream of all the sublists of its input list
    public static <E> Stream<List<E>> of(List<E> list) {
        return IntStream.range(0, list.size())
                .mapToObj(start ->
                        IntStream.rangeClosed(start + 1, list.size())
                            .mapToObj(end -> list.subList(start, end)))
                .flatMap(x -> x);
    }

    //如果返回集合是可行的，请执行此操作。
    //如果已经拥有集合中的元素，或者序列中的元素数量足够小，可以创建一个新的元素，那么返回一个标准集合，比如ArrayList。否则，请考虑实现自定义集合。
}
