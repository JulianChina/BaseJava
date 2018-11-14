package iInterface;

import zUtils.Print;

import java.util.Arrays;

public class Interface {
    //接口和内部类为我们提供了一种将接口与实现分离的更加结构化的方法。
    //抽象类是普通类与接口之间的一种中庸之道。
    //9.1 抽象类和抽象方法
    //9.2 接口
    //9.3 完全解耦
        //创建一个能够根据所传递的参数对象的不同而具有不同行为的方法，被称为策略设计模式。这类方法包含所要执行的算法中固定不变的部分。
        //而"策略"包含变化的部分。策略就是传递进去的参数对象。
        //适配器设计模式(FilterAdapter)。
        static class Processor {
            public String name() {
                return getClass().getSimpleName();
            }
            Object process(Object input) { return input; }
        }
        static class Upcase extends Processor {
            String process(Object input) {
                return ((String) input).toUpperCase();
            }
        }
        static class Downcase extends Processor {
            String process(Object input) {
                return ((String) input).toLowerCase();
            }
        }
        static class Splitter extends Processor {
            String process(Object input) {
                return Arrays.toString(((String) input).split(" "));
            }
        }

        public static class Apply {
            public static void process(Processor p, Object s) {
                Print.print("Using Processor " + p.name());
                Print.print(p.process(s));
            }
            public static String s = "Disagreement with beliefs is by definition incorrect";
            public static void main(String[] args) {
                process(new Upcase(), s);
                process(new Downcase(), s);
                process(new Splitter(), s);
            }
        }
    //9.4 Java的多重继承

    //9.5 通过继承来扩展接口
    //9.6 适配接口
    //9.7 接口中的域
    //9.8 嵌套接口
    //9.9 接口与工厂
    //9.10 总结
}
