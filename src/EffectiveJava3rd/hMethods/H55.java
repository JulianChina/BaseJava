package EffectiveJava3rd.hMethods;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import java.util.*;

//55 明智审慎地返回Optional
public class H55 {
    //在Java8之前，编写在特定情况下无法返回任何值的方法时，可以采用两种方法。要么抛出异常，要么返回null(假设返回类型是对象是引用类型)。但这两种方法都不完美。
    //在Java8中，还有第三种方法来编写可能无法返回任何值的方法。Optional<T>类表示一个不可变的容器，它可以包含一个非null的T引用，也可以什么都不包含。
    //不包含任何内容的Optional被称为空(empty)。非空的包含值称的Optional被称为存在(present)。Optional的本质上是一个不可变的集合，最多可以容纳一个元素。
    // Returns maximum value in collection - throws exception if empty
    public static <E extends Comparable<E>> E max(Collection<E> c) {
        if (c.isEmpty())
            throw new IllegalArgumentException("Empty collection");
        E result = null;
        for (E e : c)
            if (result == null || e.compareTo(result) > 0)
                result = Objects.requireNonNull(e);
        return result;
    }

    // Returns maximum value in collection as an Optional<E>
    public static <E extends Comparable<E>> Optional<E> max2(Collection<E> c) {
        if (c.isEmpty())
            return Optional.empty();
        E result = null;
        for (E e : c)
            if (result == null || e.compareTo(result) > 0)
                result = Objects.requireNonNull(e);
        return Optional.of(result);
    }

    //Optional.empty()返回一个空的Optional，Optional.of(value)返回一个包含给定非null值的Optional。
    //永远不要通过返回Optional的方法返回一个空值：它破坏Optional设计的初衷。
    //Stream上的很多终止操作返回Optional。
    // Returns max val in collection as Optional<E> - uses stream
    public static <E extends Comparable<E>> Optional<E> max3(Collection<E> c) {
        return c.stream().max(Comparator.naturalOrder());
    }


    public static void main(String[] args) {
        //如果方法返回一个Optional，则客户端可以选择在方法无法返回值时要采取的操作。可以指定默认值：
        // Using an optional to provide a chosen default value
        List<String> words = new ArrayList<>();
        String lastWordInLexicon = max3(words).orElse("No words...");

        //或者可以抛出任何适当的异常。
        // Using an optional to throw a chosen exception
        String myToy = max3(words).orElseThrow(IllegalStateException::new);

        //如果你能证明Optional非空，你可以从Optional获取值，而不需要指定一个操作来执行。
        // Using optional when you know there’s a return value
//        Element lastNobleGas = max(Elements.NOBLE_GASES).get();
    }

    //容器类型，包括集合、映射、Stream、数组和Optional，不应该封装在Optional中。
    //如果可能无法返回结果，并且在没有返回结果，客户端还必须执行特殊处理的情况下，则应声明返回Optional的方法。
    //除了"次要基本类型(minor primitive types)" Boolean，Byte，Character，Short和Float之外，永远不应该返回装箱的基本类型的Optional。

    //如果发现自己编写的方法不能总是返回值，并且认为该方法的用户在每次调用时考虑这种可能性很重要， 那么或许应该返回一个Optional的方法。
    //应该意识到，返回Optional会带来实际的性能后果；对于性能关键的方法，最好返回null或抛出异常。
    //除了作为返回值之外，不应该在任何其他地方中使用Optional。
}
