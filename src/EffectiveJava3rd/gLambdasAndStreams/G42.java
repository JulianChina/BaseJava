package EffectiveJava3rd.gLambdasAndStreams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import static java.util.Comparator.comparingInt;

//42 lambda表达式优于匿名类
public class G42 {
    //在Java8中，添加了函数式接口，lambda表达式和方法引用，以便更容易地创建函数对象。Stream API随着其他语言的修改一同被添加进来，为处理数据元素序列提供类库支持。

    public static void main(String[] args) {
        List<String> words = new ArrayList<>();
        words.add("go go go");
        words.add("berlitz");
        words.add("harvord");

        // Anonymous class instance as a function object - obsolete!
        Collections.sort(words, new Comparator<String>() {
            public int compare(String s1, String s2) {
                return Integer.compare(s1.length(), s2.length());
            }
        });

        // Lambda expression as function object (replaces anonymous class)
        Collections.sort(words, (s1, s2) -> Integer.compare(s1.length(), s2.length()));
        //编译器使用称为类型推断的过程从上下文中推导出这些类型。

        //如果使用比较器构造方法代替lambda，则代码中的比较器可以变得更加简洁。
        Collections.sort(words, comparingInt(String::length));

        //通过利用添加到Java8中的List接口的sort方法，可以使片段变得更简短。
        words.sort(comparingInt(String::length));
    }

    // Enum type with constant-specific class bodies & data
    public enum Operation {
        PLUS("+") {
            public double apply(double x, double y) {
                return x + y;
            }
        },
        MINUS("-") {
            public double apply(double x, double y) {
                return x - y;
            }
        },
        TIMES("*") {
            public double apply(double x, double y) {
                return x * y;
            }
        },
        DIVIDE("/") {
            public double apply(double x, double y) {
                return x / y;
            }
        };
        private final String symbol;

        Operation(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public abstract double apply(double x, double y);
    }

    //第34条目说，枚举实例属性比常量特定的类主体更可取。Lambdas可以很容易地使用前者而不是后者来实现常量特定的行为。
    //仅仅将实现每个枚举常量行为的lambda传递给它的构造方法。构造方法将lambda存储在实例属性中，apply方法将调用转发给lambda。
    public enum Operation2 {
        PLUS("+", (x, y) -> x + y),
        MINUS("-", (x, y) -> x - y),
        TIMES("*", (x, y) -> x * y),
        DIVIDE("/", (x, y) -> x / y);
        private final String symbol;
        private final DoubleBinaryOperator op;

        Operation2(String symbol, DoubleBinaryOperator op) {
            this.symbol = symbol;
            this.op = op;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public double apply(double x, double y) {
            return op.applyAsDouble(x, y);
        }
    }

    //lambda没有名称和文档；如果计算不是自解释的，或者超过几行，则不要将其放入lambda表达式中。
    //枚举构造方法中的lambda表达式不能访问枚举的实例成员。
    //如果枚举类型具有难以理解的常量特定行为，无法在几行内实现，或者需要访问实例属性或方法，那么常量特定的类主体仍然是行之有效的方法。
    //Lambda仅限于函数式接口。如果你想创建一个抽象类的实例，你可以使用匿名类来实现，但不能使用lambda。
    //lambda不能获得对自身的引用。在lambda中，this关键字引用封闭实例，这通常是你想要的。在匿名类中，this关键字引用匿名类实例。如果你需要从其内部访问函数对象，则必须使用匿名类。
    //应该很少(如果有的话)序列化一个lambda(或一个匿名类实例)。

    //除非必须创建非函数式接口类型的实例，否则不要使用匿名类作为函数对象。
}
