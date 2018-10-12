package eInitializationAndCleanup;

import sun.print.CUPSPrinter;

import static java.lang.System.out;

public class InitializationAndCleanup {
    //5.1 用构造器确保初始化
    //5.2 方法重载
        //区分重载方法：独一无二的参数类型列表
        //涉及基本类型的重载：存在歧义
        //以返回值区分重载方法：不可
    //5.3 默认构造器
    //5.4 this关键字
        //在构造器中调用构造器
        //static的含义：在static方法的内部不能调用非静态方法，反过来倒是可以的。
    //5.5 清理：终结处理和垃圾回收
        //finalize()的用途何在：不是进行普通的清理工作的合适场所
        //你必须实施清理：无论是"垃圾回收"还是"终结"，都不保证一定会发生。
        //终结条件
        static class Book {
            boolean checkedOut = false;

            Book(boolean checkOut) {
                checkedOut = checkOut;
            }
            void checkIn() {
                checkedOut = false;
            }

            protected void finalize() {
                if (checkedOut) {
                    out.println("Error: checked out!");
                }
                //super.finalize();
            }
        }
        //垃圾回收器如何工作：引用计数；停止-复制；标记-清扫；（"自适应的、分代的、停止-复制、标记-清扫"式垃圾回收器）
    //5.6 成员初始化
    //5.7 构造器初始化
            //无法阻止自动初始化的进行，它将在构造器被调用之前发生。
        //初始化顺序：类内部，变量定义的先后顺序决定了初始化的顺序，即使其散布于方法定义之间。
        static class Window {
            public Window(int marker) {
                out.println("Window(" + marker + ")");
            }
        }
        static class House {
            Window w1 = new Window(1);
            House() {
                out.println("House()");
                w3 = new Window(33);
            }
            Window w2 = new Window(2);
            void f() {
                out.println("f()");
            }
            Window w3 = new Window(3);
        }
        //静态数据的初始化：静态数据只占用一份存储区域，static关键字不能应用于局部变量。
            //a.首次创建对象时，或者静态方法/静态域首次被访问时，Java解释器必须查找类路径，以定位class文件；
            //b.然后载入class文件，静态初始化只在Class对象首次加载的时候进行一次；
            //c.当new创建对象时，在堆上分配存储空间，并将存储空间清零；
            //d.执行所有出现于字段定义处的初始化动作，包括执行类域的代码块；
            //e.执行构造器；
        static class Bowl {
            public Bowl(int marker) {
                out.println("Bowl(" + marker + ")");
            }
            void f1(int marker) {
                out.println("f1(" + marker + ")");
            }
        }
        static class Table {
            static Bowl bowl1 = new Bowl(1);
            Table() {
                out.println("Table()");
                bowl2.f1(1);
            }

            void f2(int marker) {
                out.println("f2(" + marker + ")");
            }

            static Bowl bowl2 = new Bowl(2);
        }

        static class Cupboard {
            Bowl bowl3 = new Bowl(3);
            static Bowl bowl4 = new Bowl(4);
            Cupboard() {
                out.println("Cupboard()");
                bowl4.f1(2);
            }
            void f3(int marker) {
                out.println("f3(" + marker + ")");
            }
            static Bowl bowl5 = new Bowl(5);
        }
        //显示的静态初始化
        static class Cup {
            Cup(int marker) {
                out.println("Cup(" + marker + ")");
            }
            void f(int marker) {
                out.println("f(" + marker + ")");
            }
        }
        static class Cups {
            static Cup cup1;
            static Cup cup2;
            static {
                cup1 = new Cup(1);
                cup2 = new Cup(2);
            }
            Cups() {
                out.println("Cups()");
            }
        }
        //非静态实例初始化
        static class Mug {
            Mug(int marker) {
                out.println("Mug(" + marker + ")");
            }
            void f(int marker) {
                out.println("f(" + marker + ")");
            }
        }
        Mug mug1;
        Mug mug2;
        {
            mug1 = new Mug(1);
            mug2 = new Mug(2);
            out.println("mug1 & mug2 initialized");
        }
        InitializationAndCleanup() {
            out.println("InitializationAndCleanup()");
        }
        InitializationAndCleanup(int i) {
            out.println("InitializationAndCleanup(int)");
        }
    //5.8 数组初始化
            //Arrays.toString()
        //可变参数列表：...，优化尾部可变参数列表，可变参数类型信息，AutoBoxing可变参数列表，重载可变参数列表
        static class VarargType {
            static void f(Character... args) {
                out.print(args.getClass());
                out.println(" ---- length " + args.length);
            }
            static void g(int... args) {
                out.print(args.getClass());
                out.println(" ---- length " + args.length);
            }
        }
    //5.9 枚举类型
        static enum Spiciness {
            NOT, MILD, MEDIUM, HOT, FLAMING
        }
        //用于switch参数
    //5.10 总结
    public static void main(String[] args) {
//        Book novel = new Book(true);
//        novel.checkIn();
//        new Book(true);
//        System.gc();
//
//        House h = new House();
//        h.f();

//        out.println("Creating new Cupboard() in main");
//        new Cupboard();
//        out.println("Creating new Cupboard() in main");
//        new Cupboard();
//        table.f2(1);
//        cupboard.f3(1);

//        out.println("Inside main()");
//        Cups.cup1.f(99);

//        out.println("Inside main()");
//        new InitializationAndCleanup();
//        out.println("new InitializationAndCleanup() completed");
//        new InitializationAndCleanup(1);
//        out.println("new InitializationAndCleanup(1) completed");

//        VarargType.f('a');
//        VarargType.f();
//        VarargType.g(1);
//        VarargType.g();
//        out.print("int[]: " + (new int[0]).getClass());

        for (Spiciness s : Spiciness.values()) {
            out.println(s + ", ordinal " + s.ordinal());
        }
    }
//    static Table table = new Table();
//    static Cupboard cupboard = new Cupboard();

//    static Cups cups1 = new Cups();
//    static Cups cups2 = new Cups();
}
