package ThinkingInJava.hPolymorphism;

import ThinkingInJava.zUtils.Print;

public class Polymorphism {
    //8.1 再论向上转型
        //忘记对象类型
    //8.2 转机
        //方法调用绑定
            //Java中除了static方法和final方法之外，其他所有的方法都是后期绑定。
        //产生正确的行为
        //可扩展性
            //多态让程序员"将改变的事物与未变的事物分离开来"。
        //缺陷："覆盖"私有方法
            //只有非private方法才可以被覆盖。
        //缺陷：域与静态方法
            //只有普通的方法调用可以是多态的。域与静态方法都不具有多态性。静态方法是与类，而非单个对象相关联的。
        static class Super {
            public int field = 0;
            public int getField() { return field; }
        }
        static class Sub extends Super {
            public int field = 1;
            public int getField() { return field; }
            public int getSuperField() { return super.field; }
        }
        public static class FieldAccess {
            public static void main(String[] args) {
                Super sup = new Sub();
                Print.print("sup.field = " + sup.field + ", sup.getField() = " + sup.getField());
                Sub sub = new Sub();
                Print.print("sub.field = " + sub.field + ", sub.getField() = " + sub.getField() + ", sub.getSuperField() = " + sub.getSuperField());
            }
        }

        static class StaticSuper {
            public static String staticGet() {
                return "Base staticGet()";
            }
            public String dynamicGet() {
                return "Base dynamicGet()";
            }
        }
        static class StaticSub extends StaticSuper {
            public static String staticGet() {
                return "Derived staticGet()";
            }
            public String dynamicGet() {
                return "Derived dynamicGet()";
            }
        }
        public static class StaticPolymorphism {
            public static void main(String[] args) {
                StaticSuper sup = new StaticSub();
                Print.print(sup.staticGet());
                Print.print(sup.dynamicGet());
            }
        }
    //8.3 构造器和多态
        //构造器的调用顺序
            //成员变量初始化 > 构造器；基类初始化 > 导出类初始化；
        //继承与清理
            //销毁的顺序应和初始化顺序相反，对于字段，意味着和声明的顺序相反。
            //如果存在对象共享的情况，那么就要使用引用计数的方式来跟踪仍旧访问着共享对象的对象数量。
        //构造器内部的多态方法的行为
            //对象初始化的实际过程：
                //在其他任何事物发生之前，将分配给对象的存储空间初始化成二进制的零；
                //调用基类构造器；
                //按照声明的顺序调用成员的初始化方法；
                //调用当前类的构造器主体。
            //在构造器内，唯一能够安全调用的那些方法是基类中的final方法。
    //8.4 协变返回类型
        //在导出类中的被覆盖方法可以返回基类方法的返回类型的某种导出类型。
        static class Grain {
            public String toString() { return "Grain"; }
        }
        static class Wheat extends Grain {
            public String toString() { return "Wheat"; }
        }

        static class Mill {
            Grain process() { return new Grain(); }
        }
        static class WheatMill extends Mill {
            Wheat process() { return new Wheat(); }
        }
        public static class CovariantReturn {
            public static void main(String[] args) {
                Mill m = new Mill();
                Grain g = m.process();
                Print.print(g);
                m = new WheatMill();
                g = m.process();
                Print.print(g);
            }
        }
    //8.5 用继承进行设计
        //用继承表达行为间的差异，并用字段表达状态上的变化。
        //纯继承与扩展
        //向下转型与运行时类型识别
    //8.6 总结
}