package iInterface;

import zUtils.Print;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Interface {
    //接口和内部类为我们提供了一种将接口与实现分离的更加结构化的方法。
    //抽象类是普通类与接口之间的一种中庸之道。
    //9.1 抽象类和抽象方法
    //9.2 接口
    //9.3 完全解耦
        //创建一个能够根据所传递的参数对象的不同而具有不同行为的方法，被称为【策略】设计模式。这类方法包含所要执行的算法中固定不变的部分。
        //而"策略"包含变化的部分。策略就是传递进去的参数对象。
        //【适配器】设计模式(FilterAdapter)。
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
        //如果知道某事物应该成为一个基类，那么第一选择应该是使它成为一个接口。
        interface CanFight {
            void fight();
        }
        interface CanSwim {
            void swim();
        }
        interface CanFly {
            void fly();
        }
        static class ActionCharacter {
            public void fight() { }
        }
        static class Hero extends ActionCharacter implements CanFight, CanSwim, CanFly{
            public void swim() { }
            public void fly() { }
        }

        public static class Adventure {
            public static void t(CanFight x) { x.fight(); }
            public static void u(CanSwim x) { x.swim(); }
            public static void v(CanFly x) { x.fly(); }
            public static void w(ActionCharacter x) { x.fight(); }
            public static void main(String[] args) {
                Hero h = new Hero();
                t(h);
                u(h);
                v(h);
                w(h);
            }
        }
    //9.5 通过继承来扩展接口
        //组合接口时的名字冲突
    //9.6 适配接口
        //接口一种最常用的方法就是策略模式。
        public static class RandomWords implements Readable {
            private static Random rand = new Random(47);
            private static final char[] capitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
            private static final char[] lowers = "abcdefghijlkmnopqrstuvwxyz".toCharArray();
            private static final char[] vowels = "aeiou".toCharArray();
            private int count;
            public RandomWords(int count) {
                this.count = count;
            }

            public int read(CharBuffer cb) {
                if (count-- == 0) {
                    return -1;
                }
                cb.append(capitals[rand.nextInt(capitals.length)]);
                for (int i = 0; i < 4; ++i) {
                    cb.append(vowels[rand.nextInt(vowels.length)]);
                    cb.append(lowers[rand.nextInt(lowers.length)]);
                }
                cb.append(" ");
                return 10;
            }
            public static void main(String[] args) {
                Scanner s = new Scanner(new RandomWords(10));
                while(s.hasNext()) {
                    Print.print(s.next());
                }
            }
        }
        //被适配的类可以通过继承和实现接口来创建。
        public static class RandomDoubles {
            private static Random rand = new Random(47);
            public double next() {
                return rand.nextDouble();
            }
            public static void main(String[] args) {
                RandomDoubles rd = new RandomDoubles();
                for (int i = 0; i < 7; i++) {
                    Print.print(rd.next() + " ");
                }
            }
        }

        public static class AdapterRandomDoubles extends RandomDoubles implements Readable {
            private int count;
            public AdapterRandomDoubles(int count) {
                this.count = count;
            }
            public int read(CharBuffer cb) {
                if (count-- == 0) {
                    return -1;
                }
                String result = Double.toString(next()) + " ";
                cb.append(result);
                return result.length();
            }
            public static void main(String[] args) {
                Scanner s = new Scanner(new AdapterRandomDoubles(7));
                while(s.hasNext()) {
                    Print.print(s.nextDouble() + " ");
                }
            }
        }
    //9.7 接口中的域
        //初始化接口中的域
            //接口中定义的域不能是"空final"，但可以被非常量表达式初始化。这些域不是接口的一部分，它们的值被存储在该接口的静态存储区域内。
    //9.8 嵌套接口
    static class A {
        interface B {
            void f();
        }
        public class BImp implements B {
            @Override
            public void f() {

            }
        }
        private class BImp2 implements B {
            @Override
            public void f() {

            }
        }

        public interface C {
            void f();
        }
        class CImp implements C {
            @Override
            public void f() {

            }
        }
        private class CImp2 implements C {
            @Override
            public void f() {

            }
        }

        private interface D {
            void f();
        }
        private class DImp implements D {
            @Override
            public void f() {

            }
        }
        public class DImp2 implements D {
            @Override
            public void f() {

            }
        }
        public D getD() {
            return new DImp2();
        }
        private D dRef;

        public void receiveD(D d) {
            dRef = d;
            dRef.f();
        }
    }

    interface E {
        interface G {
            void f();
        }
        public interface H {
            void f();
        }
        void g();
    }

    public static class NestingInterfaces {
        public class BImp implements A.B {
            @Override
            public void f() {

            }
        }
        class CImp implements A.C {
            @Override
            public void f() {

            }
        }
        class DImp implements A.D {  //为何可以，难道是因为都是内部成员吗？
            @Override
            public void f() {

            }
        }
        class EImp implements E {
            @Override
            public void g() {

            }
        }
        class EGImp implements E.G {
            @Override
            public void f() {

            }
        }
        class EImp2 implements E {
            @Override
            public void g() {

            }
            class EG implements E.G {
                @Override
                public void f() {

                }
            }
        }
        public static void main(String[] args) {
            A a = new A();
            A.D ad = a.getD();
            A.DImp2 di2 = (A.DImp2) a.getD();
            a.getD().f();
            A a2 = new A();
            a2.receiveD(a.getD());
        }
    }
    //9.9 接口与工厂
        //【工厂方法】模式：透明地将某个实现替换为另一个实现。
        interface Service {
            void method1();
            void method2();
        }
        interface ServiceFactory {
            Service getService();
        }
        class Implementation1 implements Service {
            Implementation1() { }
            public void method1() { Print.print("Implementation1 method1"); }
            public void method2() { Print.print("Implementation1 method2"); }
        }
        class Implementation1Factory implements ServiceFactory {
            public Service getService() {
                return new Implementation1();
            }
        }
        class Implementation2 implements Service {
            Implementation2() { }
            public void method1() { Print.print("Implementation2 method1"); }
            public void method2() { Print.print("Implementation2 method2"); }
        }
        class Implementation2Factory implements ServiceFactory {
            public Service getService() {
                return new Implementation2();
            }
        }
        public static class Factories {
            public static void serviceConsumer(ServiceFactory fact) {
                Service s = fact.getService();
                s.method1();
                s.method2();
            }
            public static void main(String[] args) {
                serviceConsumer(new Interface().new Implementation1Factory());
                serviceConsumer(new Interface().new Implementation2Factory());
            }
        }

        //zAssistant.iInterface.Games

    //9.10 总结
        //恰当的原则应该是优先选择类而不是接口。从类开始，如果接口的必需性变得非常明确，那么就进行重构。
}
