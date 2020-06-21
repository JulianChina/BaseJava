package EffectiveJava3rd.bCreatingAndDestroyingObjects;

import java.util.regex.Pattern;

//06 避免创建不必要的对象
public class B06 {
    //在每次需要时，重用一个对象而不是创建一个新的相同功能对象通常是恰当的。重用可以更快更流行。如果对象是不可变的，它总是可以被重用。
    //通过使用静态工厂方法(static factory methods)，可以避免创建不需要的对象。
    //除了重用不可变对象，如果知道它们不会被修改，还可以重用可变对象。
    //一些对象的创建比其他对象的创建要昂贵得多。如果要重复使用这样一个"昂贵的对象"，建议将其缓存起来以便重复使用。
    //【性能极其糟糕的用法】
    static boolean isRomanNumeral(String s) {
        return s.matches("^(?=.)M*(C[MD]|D?C{0,3})"
                + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
    }
    //【性能极其糟糕的用法】
    //创建Pattern实例是昂贵的，因为它需要将正则表达式编译成有限状态机(finite state machine)。
    //为了提高性能，作为类初始化的一部分，将正则表达式显式编译为一个Pattern实例(不可变)，缓存它，并在isRomanNumeral方法的每个调用中重复使用相同的实例。
    public static class RomanNumerals {
        private static final Pattern ROMAN = Pattern.compile("^(?=.)M*(C[MD]|D?C{0,3})"
                + "(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

        static boolean isRomanNumeral(String s) {
            return ROMAN.matcher(s).matches();
        }
    }

    //除非池中的对象非常重量级，否则通过维护自己的对象池来避免对象创建是一个坏主意。
    //未能在需要的情况下防御性复制会导致潜在的错误和安全漏洞；而不必要地创建对象只会影响程序的风格和性能。

}
