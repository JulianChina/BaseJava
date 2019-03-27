package ThinkingInJava.fAccessControl;

public class AccessControl {
    //6.1 包：库单元
        //代码组织
        //创建独一无二的包名
        //定制工具库：Print/Range
        //用import改变行为
        //对使用包的忠告
    //6.2 Java访问权限修饰词
        //包访问权限
        //public：接口访问权限
            //默认包
        //private：你无法访问
        //protected：继承访问权限
            //相同包内的其他类可以访问protected元素
            //不同包内的继承关系可以访问protected元素
            //public > protected > (default) > private
    //6.3 接口和实现
    //6.4 类的访问权限
        //类仅有两个访问权限：default和public
        //将构造器设定为private，可以阻止任何人创建该类的对象
        static class Soup1 {
            private Soup1() { }
            public static Soup1 makeSoup() {
                return new Soup1();
            }
        }

        static class Soup2 {
            private Soup2() { }
            //Singleton
            private static Soup2 ps = new Soup2();
            public static Soup2 access() {
                return ps;
            }
            public void f() { }
        }
    //6.5 总结
}

class Lunch {
    void testPrivate() {
//        AccessControl.Soup1 soup = new AccessControl.Soup1();
    }

    void testStatic() {
        AccessControl.Soup1 soup = AccessControl.Soup1.makeSoup();
    }

    void testSingleton() {
        AccessControl.Soup2.access().f();
    }
}