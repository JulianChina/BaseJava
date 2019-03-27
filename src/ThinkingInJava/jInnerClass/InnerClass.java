package ThinkingInJava.jInnerClass;

import ThinkingInJava.zUtils.Print;

public class InnerClass {
    //将一个类的定义放在另一个类的定义内部，这就是内部类。
    //10.1 创建内部类
        //如果想从外部类的非静态方法之外的任意位置创建某个内部类的对象，那么必须具体地指明这个对象的类型：OuterClassName.InnerClassName。
    //10.2 链接到外部类
        //内部类拥有其外围类的所有元素的访问权。
    //10.3 使用.this与.new
        //如果你需要生成对外部类对象的引用，可以使用外部类的名字后面紧跟".this"。
        //有时可能要在一个类中创建另外一个类的内部类的对象，那么必须在".new"表达式前面提供对其他外部类对象的引用。
    //10.4 内部类与向上转型
    //10.5 在方法和作用域内的内部类
        //一个定义在方法中的类；[局部内部类]
        //一个定义在作用域内的类，此作用域在方法的内部；[局部内部类]
        //一个实现了接口的匿名类；
        //一个匿名类，它扩展了有非默认构造器的类；
        //一个匿名类，它执行字段初始化；
        //一个匿名类，它通过实例初始化实现构造（匿名类不可能有构造器）。
    //10.6 匿名内部类
        //自动向上转型；
        //构造器带参数的匿名内部类；
        //在匿名类中定义字段时，还能够对其执行初始化操作；
        //如果定义一个匿名内部类，并希望他使用一个在其外部定义的对象，那么编译器会要求其参数引用是final的；
        //实例初始化；
        abstract static class Base {
            public Base(int i) {
                Print.print("Base Constructor, i = " + i);
            }
            public abstract void f();
        }
        public static class AnonymousConstructor {
            public static Base getBase(int i) {
                return new Base(i) {
                    { Print.print("Inside instance initializer"); }
                    @Override
                    public void f() {
                        Print.print("In anonymous f()");
                    }
                };
            }
        }
        public static class TestInner {
            public static void main(String[] args) {
                Base base = AnonymousConstructor.getBase(47);
                base.f();

                Parcel10 parcel10 = new Parcel10();
                parcel10.destination("Chengdu", 577.6F);
            }
        }

        abstract static class Destination {}
        public static class Parcel10 {
            public Destination destination(final String dest, final float price) {
                return new Destination() {
                    private int cost;
                    //Instance initialization for each Object
                    {
                        cost = Math.round(price);
                        if (cost>100) {
                            Print.print("Over budget!");
                        }
                    }
                    private String label = dest;
                    public String readLabel() {
                        return label;
                    }
                };
            }
        }
        //再访工厂方法
            //没有必要去创建作为工厂的具名类，它们可以作为产品类的匿名内部类，用于初始化产品类的静态成员变量。
    //10.7 嵌套类(静态内部类)
        //1.要创建嵌套类的对象，并不需要其外围类的对象；
        //2.不能从嵌套类的对象中访问非静态的外围类对象；
        //3.普通内部类不能有static数据和static字段，也不能包含嵌套类；但嵌套类可以包含所有这些东西。
        //接口内部的类
            //嵌套类可以作为接口的一部分，放到接口中的任何类都自动地是public和static的。
        //从多层嵌套类中访问外部类的成员
    //10.8 为什么需要内部类
        //每个内部类都能独立地继承自一个（接口的）实现，所以无论外围类是否已经继承了某个（接口的）实现，对于内部类都没有影响。
        //1.内部类可以有多个实例，每个实例都有自己的状态信息，并且与其外围类对象的信息相互独立；
        //2.在单个外围类中，可以让多个内部类以不同的方式实现同一个接口，或继承同一个类；
        //3.创建内部类对象的时刻并不依赖于外围类对象的创建；
        //4.内部类并没有令人疑惑的"is-a"关系，它就是一个独立的实体。
        //闭包与回调
            //闭包是一个可调用的对象，它记录了一些信息，这些信息来自于创建它的作用域。
            //内部类是面向对象的闭包。
            //回调的价值在于它的灵活性————可以在运行时动态地决定需要调用什么方法。
        //内部类与控制框架
            //模板方法；
            //设计模式总是将变化的事物与保持不变的的事物分离开；
            //命令方法；
    //10.9 内部类的继承
        //见zAssistant/innerclass/InheritInner.java
    //10.10 内部类可以被覆盖吗
    //10.11 局部内部类
        //局部内部类可以访问当前代码块内的常量，以及此外围类的所有成员；
        //使用局部内部类而不是匿名内部类的的理由是，需要一个已命名的构造器，或者需要重载构造器，或者需要不止一个该内部类的对象。
    //10.12 内部类标识符
    //10.13 总结
}
