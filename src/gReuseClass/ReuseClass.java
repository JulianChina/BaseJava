package gReuseClass;

import zUtils.Print;

public class ReuseClass {
    //7.1 组合语法
        //定义对象处初始化
        //构造器中初始化
        //惰性初始化
        //实例初始化
    //7.2 继承语法
        //即使一个类只有包访问权限，其public main()仍然是可访问的。
        //初始化基类
            //当创建一个导出类对象时，该对象包含了一个基类的子对象。
    //7.3 代理
        //继承与组合之间的中庸之道；【代理】模式。
    //7.4 结合使用组合和继承
        //确保正确清理
        //名称屏蔽
            //导出类可以重载基类的方法，所有重载的方法在导出类均可使用；导出类也可以覆盖基类的方法，覆盖后基类的被覆盖方法在导出类中将不可见。
    //7.5 在组合与继承之间选择
        //组合和继承都允许在新的类中放置子对象，组合是显式地这样做，继承是隐式地这样做。
        //继承：is-a；组合：has-a。
    //7.6 protected关键字
    //7.7 向上转型
        public void play() { }
        static void tune(ReuseClass i) {
            i.play();
        }
        //为什么称为向上转型
        //再论组合与继承
    //7.8 final关键字
        //"这是无法改变的"。
        //final数据
            //一个永不改变的编译时常量；一个在运行时被初始化的值，而你不希望它被改变。
            //一个既是static又是final的域只占据一段不能改变的存储空间。
            //空白final
                //必须在域的定义处或者每个构造器中用表达式对final进行赋值。
            //final参数
                //主要用来向匿名内部类来传递参数。
        //final方法
            //final和private关键字
                //类中所有的private方法都隐式地指定为是final的，不允许被覆盖，或者说不可能被覆盖。
        //final类
            //final类中所有的方法都隐式指定为是final的，因为无法覆盖它们。
        //有关final的忠告
    //7.9 初始化及类的加载
        //类的代码在初次使用时才加载。通常指，创建类的第一个对象，或访问static域或者static方法。
        //继承与初始化
            //基类static域--导出类static域--基类域、构造器--导出类域、构造器
        static class Insect{
            private int i = 9;
            protected int j;
            Insect() {
                Print.print("i = " + i + ", j = " + j);
                j = 39;
            }
            private static int x1 = printInit("static Insect.x1 initialized");
            static int printInit(String s) {
                Print.print(s);
                return 47;
            }
        }

        public static class Beetle extends Insect {
            private int k = printInit("Beetle.k initialized");
            public Beetle() {
                printInit("k = " + k);
                printInit("j = " + j);
            }

            private static int x2 = printInit("static Beetle.x2 initialized");
            public static void main(String[] args) {
                Print.print("Beetle constructor");
                Beetle b = new Beetle();
            }
        }
    //7.10 总结
        //组合一般是将现有类型作为新类型底层实现的一部分来加以复用，而继承复用的是接口。
}

class SubReuseClass extends ReuseClass {
    public static void main(String[] args) {
        SubReuseClass src = new SubReuseClass();
        ReuseClass.tune(src);
    }
}