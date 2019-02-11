package nTypeInformation;

import zAssistant.typeinfo.Factory;
import zUtils.Print;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.regex.Pattern;

public class TypeInformation {
    //运行时类型信息使得你可以在程序运行时发现和使用类型信息。
    //14.1 为什么需要RTTI
        //在运行时，识别一个对象的类型。
    //14.2 Class对象
        //Java使用Class对象来执行其RTTI。
        //每个类都有一个Class对象。
        //为了生成这个类的对象，运行这个程序的Java虚拟机(JVM)将使用被称为"类加载器"的子系统；
        //类加载器子系统实际上可以包含一条类加载器链，但是只有一个原生类加载器；
        //原生类加载器是JVM实现的一部分，加载的是所谓的可信类，包括Java API类；
        //所有的类都是在对其第一次使用时，动态加载到JVM中的；
        //Java程序的各个部分是在必需时才加载的。
        interface HasBatteries { }
        interface Waterproof { }
        interface Shoots { }
        public static class Toy {
            Toy() { }
            Toy(int i) { }
        }
        public static class FancyToy extends Toy implements HasBatteries, Waterproof, Shoots {
            FancyToy() { super(1); }
        }
        public static class ToyTest {
            static void printInfo(Class cc) {
                System.out.format("Class name: %s is interface? [%b]\n", cc.getName(), cc.isInterface());
                System.out.format("Simple name: %s\n", cc.getSimpleName());
                System.out.format("Canonical name: %s\n", cc.getCanonicalName());
            }

            public static void main(String[] args) {
                Class c = null;
                try {
                    c = Class.forName("nTypeInformation.TypeInformation$FancyToy");
                } catch (ClassNotFoundException e) {
                    Print.print("Can't find FancyToy");
                    System.exit(1);
                }
                printInfo(c);
                for (Class face : c.getInterfaces()) {
                    printInfo(face);
                }
                Class up = c.getSuperclass();
                Object obj = null;
                try {
                    obj = up.newInstance();
                } catch (InstantiationException e) {
                    Print.print("Can't instantiate");
                    System.exit(1);
                } catch (IllegalAccessException e) {
                    Print.print("Can't access");
                    System.exit(1);
                }
                printInfo(obj.getClass());
            }
        }
        //在传递给forName()的字符串中，必须使用全限定名（包含包名）；
        //使用newInstance()来创建的类，必须带有默认的构造器。
        //类字面常量
            //对Class对象的引用；
            //当使用".class"来创建对Class对象的引用时，不会自动地初始化该Class对象；
            //为了使用类而做的准备工作实际包含三个步骤：加载(创建一个Class对象)、链接(为静态域分配存储空间)、初始化(执行静态初始化器和静态初始化块)。
            //初始化有效地实现了尽可能的"惰性"。
        //泛化的Class引用
            //当将泛型语法用于Class对象时，newInstance()将返回该对象的确切类型。
        //新的转型语法
            //Class.cast()/asSubclass()
    //14.3 类型转换前先做检查
        //1.传统的类型转换；
        //2.代表对象的类型的Class对象。通过查询Class对象可以获取运行时所需的信息；
        //3.instanceof；
        //使用类字面常量
        //动态的instanceof
            //Class.isInstance();
        //递归计数
            //Class.isAssignableFrom();
    //14.4 注册工厂
        //【工厂方法】设计模式；
        static class Part {
            public String toString() {
                return getClass().getSimpleName();
            }
            static List<Factory<? extends Part>> partFactories = new ArrayList<>();
            static {
                partFactories.add(new FuelFilter.Factory());
                partFactories.add(new AirFilter.Factory());
                partFactories.add(new CarbinAirFilter.Factory());
                partFactories.add(new OilFilter.Factory());
                partFactories.add(new FanBelt.Factory());
                partFactories.add(new GeneratorBelt.Factory());
                partFactories.add(new PowerSteeringBelt.Factory());
            }

            private static Random rand = new Random(47);
            public static Part createRandom() {
                int n = rand.nextInt(partFactories.size());
                return partFactories.get(n).create();
            }
        }
        static class Filter extends Part { }
        static class FuelFilter extends Filter {
            public static class Factory implements zAssistant.typeinfo.Factory<FuelFilter> {
                public FuelFilter create() { return new FuelFilter(); }
            }
        }
        static class AirFilter extends Filter {
            public static class Factory implements zAssistant.typeinfo.Factory<AirFilter> {
                public AirFilter create() { return new AirFilter(); }
            }
        }
        static class CarbinAirFilter extends Filter {
            public static class Factory implements zAssistant.typeinfo.Factory<CarbinAirFilter> {
                public CarbinAirFilter create() { return new CarbinAirFilter(); }
            }
        }
        static class OilFilter extends Filter {
            public static class Factory implements zAssistant.typeinfo.Factory<OilFilter> {
                public OilFilter create() { return new OilFilter(); }
            }
        }
        static class Belt extends Part { }
        static class FanBelt extends Belt {
            public static class Factory implements zAssistant.typeinfo.Factory<FanBelt> {
                public FanBelt create() { return new FanBelt(); }
            }
        }
        static class GeneratorBelt extends Belt {
            public static class Factory implements zAssistant.typeinfo.Factory<GeneratorBelt> {
                public GeneratorBelt create() { return new GeneratorBelt(); }
            }
        }
        static class PowerSteeringBelt extends Belt {
            public static class Factory implements zAssistant.typeinfo.Factory<PowerSteeringBelt> {
                public PowerSteeringBelt create() { return new PowerSteeringBelt(); }
            }
        }
        public static class RegisteredFactories {
            public static void main(String[] args) {
                for (int i = 0; i < 10; ++i) {
                    Print.print(Part.createRandom());
                }
            }
        }
    //14.5 instanceof与Class的等价性
        //instanceof;isInstance;==;equals;
    //14.6 反射：运行时的类信息
        //Class类和java.lang.reflect类库一起对反射的概念进行了支持，该类库包含了Field、Method以及Constructor类(每个类都实现了Member接口)；
        //对RTTI来说，编译器在编译时打开和检查.class文件；对于反射机制来说，在运行时打开和检查.class文件。
        //类方法提取器
            //见zUtils.ShowMethods
    //14.7 动态代理
        //【代理】是基本的设计模式之一；
        interface Interface {
            void doSomething();
            void somethingElse(String arg);
        }
        static class RealObject implements Interface {
            public void doSomething() { Print.print("doSomething"); }
            public void somethingElse(String arg) { Print.print("somethingElse " + arg); }
        }
        static class SimpleProxy implements Interface {
            private Interface proxied;
            public SimpleProxy(Interface proxied) { this.proxied = proxied; }
            public void doSomething() {
                Print.print("SimpleProxy doSomething");
                proxied.doSomething();
            }
            public void somethingElse(String arg) {
                Print.print("SimpleProxy somethingElse " + arg);
                proxied.somethingElse(arg);
            }
        }
        static class SimpleProxyDemo {
            public static void consumer(Interface iface) {
                iface.doSomething();
                iface.somethingElse("bonobo");
            }
            public static void main(String[] args) {
                consumer(new RealObject());
                consumer(new SimpleProxy(new RealObject()));
            }
        }
        //动态代理可以动态地创建代理并动态地处理对所代理方法的调用；在动态代理上所做的所有调用都会被重定向到单一的调用处理器上；
        static class DynamicProxyHandler implements InvocationHandler {
            private Object proxied;
            public DynamicProxyHandler(Object proxied) {
                this.proxied = proxied;
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Print.format("*** proxy: %s, method: %s, args: %s", proxy.getClass(), method, args);
                if (args!=null) {
                    for (Object arg : args) {
                        Print.print(" " + arg);
                    }
                }
                Print.print();
                return method.invoke(proxied, args);
            }
        }
        static class SimpleDynamicProxy {
            public static void consumer(Interface iface) {
                iface.doSomething();
                iface.somethingElse("bonobo");
            }
            public static void main(String[] args) {
                RealObject real = new RealObject();
                consumer(real);
                Interface proxy = (Interface) Proxy.newProxyInstance(Interface.class.getClassLoader(),
                        new Class[]{Interface.class}, new DynamicProxyHandler(real));
                consumer(proxy);
            }
        }

        static class MethodSelector implements InvocationHandler {
            private Object proxied;
            public MethodSelector(Object proxied) {
                this.proxied = proxied;
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("interesting")) {
                    Print.print("Proxy detected the interesting method!");
                }
                return method.invoke(proxied, args);
            }
        }
        interface SomeMethods {
            void boring1();
            void boring2();
            void interesting(String arg);
            void boring3();
        }
        static class Implementation implements SomeMethods {
            public void boring1() { Print.print("boring1"); }
            public void boring2() { Print.print("boring2"); }
            public void interesting(String arg) { Print.print("interesting " + arg); }
            public void boring3() { Print.print("boring3"); }
        }
        static class SelectingMethods {
            public static void main(String[] args) {
                SomeMethods proxy = (SomeMethods) Proxy.newProxyInstance(SomeMethods.class.getClassLoader(),
                        new Class[]{SomeMethods.class}, new MethodSelector(new Implementation()));
                proxy.boring1();
                proxy.boring2();
                proxy.interesting("bonobo");
                proxy.boring3();
            }
        }
    //14.8 空对象
        //创建一个标记接口；
            public interface Null { }
            public interface Operation {
                String description();
                void command();
            }
            public interface Robot {
                String name();
                String model();
                List<Operation> operations();
                class Test {
                    public static void test(Robot r) {
                        if (r instanceof Null) {
                            Print.print("[Null Robot]");
                        }
                        Print.print("Robot name:" + r.name());
                        Print.print("Robot model:" + r.model());
                        for (Operation operation : r.operations()) {
                            Print.print(operation.description());
                            operation.command();
                        }
                    }
                }
            }
            public static class SnowRemovalRobot implements Robot {
                private String name;
                public SnowRemovalRobot(String name) { this.name = name; }

                @Override
                public String name() { return name; }

                @Override
                public String model() { return "SnowBot version 11"; }

                @Override
                public List<Operation> operations() {
                    return Arrays.asList(
                            new Operation() {
                                @Override
                                public String description() {
                                    return name + " can shovel snow";
                                }

                                @Override
                                public void command() {
                                    Print.print(name + " shoveling snow");
                                }
                            },
                            new Operation() {
                                @Override
                                public String description() {
                                    return name + " can chip ice";
                                }

                                @Override
                                public void command() {
                                    Print.print(name + " chipping ice");
                                }
                            },
                            new Operation() {
                                @Override
                                public String description() {
                                    return name + " can clear the roof";
                                }

                                @Override
                                public void command() {
                                    Print.print(name + " clearing the roof");
                                }
                            }
                    );
                }

                public static void main(String[] args) {
                    Robot.Test.test(new SnowRemovalRobot("Slusher"));
                }
            }

            static class NullRobotProxyHandler implements InvocationHandler {
                private String nullName;
                private Robot proxied = new NRobot();
                NullRobotProxyHandler(Class<? extends Robot> type) {
                    nullName = type.getSimpleName() + " NullRobot";
                }
                private class NRobot implements Null, Robot {
                    @Override
                    public String name() { return nullName; }

                    @Override
                    public String model() { return nullName; }

                    @Override
                    public List<Operation> operations() { return Collections.emptyList(); }
                }

                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return method.invoke(proxied, args);
                }
                public static class NullRobot {
                    public static Robot newNullRobot(Class<? extends Robot> type) {
                        return (Robot) Proxy.newProxyInstance(NullRobot.class.getClassLoader(),
                                new Class[]{ Null.class, Robot.class }, new NullRobotProxyHandler(type));
                    }

                    public static void main(String[] args) {
                        Robot[] bots = { new SnowRemovalRobot("SnowBee"), newNullRobot(SnowRemovalRobot.class) };
                        for (Robot bot : bots) {
                            Robot.Test.test(bot);
                        }
                    }
                }
            }
        //模拟对象与桩
            //空对象的逻辑变体是模拟对象和桩；
            //模拟对象和桩之间的差异在于程度不同；模拟对象往往是轻量级和自测试的；桩是重量级的，经常在测试之间被复用；
    //14.9 接口与类型信息
        //interface关键字的一种重要目标就是允许程序员隔离构件，进而降低耦合性；
        //使用包访问权限，屏蔽在包外调用接口实现类的非接口内方法；但反射仍可调用；
        //看起来没有任何方式可以阻止反射到达并调用那些非公共访问权限的方法；但是，final域实际上在遭遇修改时是安全的。
    //14.10 总结
        //RTTI允许通过匿名基类的引用来发现类型信息；
        //面向对象编程语言的目的是让我们在凡是可以使用的地方都使用多态机制，只在必需的时候使用RTTI；
        //RTTI有时能解决效率问题。
}
