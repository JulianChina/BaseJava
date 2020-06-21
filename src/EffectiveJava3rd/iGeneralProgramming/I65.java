package EffectiveJava3rd.iGeneralProgramming;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

//65 接口优于反射
public class I65 {
    //核心反射机制java.lang.reflect提供对任意类的编程访问。
    //给定一个Class对象，你可以获得Constructor、Method和Field实例，分别代表了该Class实例所表示的类的构造器、方法和字段。这些对象提供对类的成员名、字段类型、方法签名等的编程访问。
    //你可以通过调用Constructor、Method和Field实例上的方法，可以构造底层类的实例、调用底层类的方法，并访问底层类中的字段。
    //反射允许一个类使用另一个类，即使在编译前者时后者并不存在。然而，这种能力是有代价的：
    // 你失去了编译时类型检查的所有好处，包括异常检查。
    // 执行反射访问所需的代码既笨拙又冗长。
    // 性能降低。反射方法调用比普通方法调用慢得多。

    //通过非常有限的形式使用反射，你可以获得反射的许多好处，同时花费的代价很少。
    //可以用反射方式创建实例，并通过它们的接口或超类正常地访问它们。
    // Reflective instantiation with interface access
    public static void main(String[] args) {
        // Translate the class name into a Class object
        Class<? extends Set<String>> cl = null;
        try {
            cl = (Class<? extends Set<String>>) Class.forName(args[0]);  // Unchecked cast!
        } catch (ClassNotFoundException e) {
            fatalError("Class not found");
        }

        if (cl == null) {
            System.err.println("cl == null");
            return;
        }

        // Get the constructor
        Constructor<? extends Set<String>> cons = null;
        try {
            cons = cl.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            fatalError("No parameterless constructor");
        }

        if (cons == null) {
            System.err.println("cons == null");
            return;
        }

        // Instantiate the set
        Set<String> s = null;
        try {
            s = cons.newInstance();
        } catch (IllegalAccessException e) {
            fatalError("Constructor not accessible");
        } catch (InstantiationException e) {
            fatalError("Class not instantiable");
        } catch (InvocationTargetException e) {
            fatalError("Constructor threw " + e.getCause());
        } catch (ClassCastException e) {
            fatalError("Class doesn't implement Set");
        }

        if (s == null) {
            System.err.println("s == null");
            return;
        }

        // Exercise the set
        s.addAll(Arrays.asList(args).subList(1, args.length));
        System.out.println(s);
    }

    public static void fatalError(String msg) {
        System.err.println(msg);
        System.exit(1);
    }
    //如果编写的程序必须在编译时处理未知的类，则应该尽可能只使用反射实例化对象，并使用在编译时已知的接口或超类访问对象。
}
