package EffectiveJava3rd.gLambdasAndStreams;

//44 优先使用标准的函数式接口
public class G44 {
    //java.util.function包提供了大量标准函数式接口供你使用。
    //在java.util.function中有43个接口。基本接口操作于对象引用类型。
    //Operator接口表示方法的结果和参数类型相同。
    //Predicate接口表示其方法接受一个参数并返回一个布尔值。
    //Function接口表示方法其参数和返回类型不同。
    //Supplier接口表示一个不接受参数和返回值(或"供应")的方法。
    //Consumer表示该方法接受一个参数而不返回任何东西，本质上就是使用它的参数。
    //接口                  //方法                    //示例
    //UnaryOperator<T>     //T apply(T t)           //String::toLowerCase
    //BinaryOperator<T>    //T apply(T t1, T t2)    //BigInteger::add
    //Predicate<T>         //boolean test(T t)      //Collection::isEmpty
    //Function<T,R>        //R apply(T t)           //Arrays::asList
    //Supplier<T>          //T get()                //Instant::now
    //Consumer<T>          //void accept(T t)       //System.out::println

    //不要试图使用基本的函数式接口来装箱基本类型的包装 类而不是基本类型的函数式接口。

    //始终使用@FunctionalInterface注解标注你的函数式接口。
    //ExecutorService的submit方法可以采用Callable<T>或Runnable接口，并且可以编写需要强制类型转换以指示正确的重载的客户端程序(条目52)。
    //避免此问题的最简单方法是不要编写在相同的参数位置中使用不同函数式接口的重载。
}
