package EffectiveJava3rd.eGenerics;

import java.util.HashSet;
import java.util.Set;
import java.util.function.UnaryOperator;

//30 优先使用泛型方法
public class E30 {
    //正如类可以是泛型的，方法也可以是泛型的。对参数化类型进行操作的静态工具方法通常都是泛型的。
    //集合中的所有"算法"方法(如binarySearch和sort)都是泛型的。
    // Uses raw types - unacceptable! [Item 26]
    public static Set union(Set s1, Set s2) {
        Set result = new HashSet(s1);
        result.addAll(s2);
        return result;
    }

    //声明类型参数的类型参数列表位于方法的修饰符和返回类型之间。
    //类型参数的命名约定对于泛型方法和泛型类型是相同的(条目29和68)。
    // Generic method
    public static <E> Set<E> unionG(Set<E> s1, Set<E> s2) {
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }

    //可以使用单个对象进行所有必需的类型参数化，但是需要编写一个静态工厂方法来重复地为每个请求的类型参数化分配对象。这种称为泛型单例工厂(generic singleton factory)的模式用于方法对象(function objects)(条目42)。
    // Generic singleton factory pattern
    private static UnaryOperator<Object> IDENTITY_FN = (t) -> t;

    @SuppressWarnings("unchecked")
    public static <T> UnaryOperator<T> identityFunction() {
        return (UnaryOperator<T>) IDENTITY_FN;
    }

    // Sample program to exercise generic singleton
    public static void main(String[] args) {
        String[] strings = { "jute", "hemp", "nylon" };
        UnaryOperator<String> sameString = identityFunction();
        for (String s : strings) {
            System.out.println(sameString.apply(s));
        }

        Number[] numbers = { 1, 2.0, 3L };
        UnaryOperator<Number> sameNumber = identityFunction();
        for (Number n : numbers) {
            System.out.println(sameNumber.apply(n));
        }
    }

    //虽然相对较少，类型参数受涉及该类型参数本身的某种表达式限制是允许的。这就是所谓的递归类型限制(recursive type bound)。
    //限定的类型<E extends Comparable <E >>可以理解为"任何可以与自己比较的类型E"。

    //像泛型类型一样，泛型方法比需要客户端对输入参数和返回值进行显式强制转换的方法更安全，更易于使用。像类型一样，你应该确保你的方法可以不用强制转换，这通常意味着它们是泛型的。
}
