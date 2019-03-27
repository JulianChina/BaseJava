package ThinkingInJava.sEnumerationType;

import net.mindview.util.Enums;
import net.mindview.util.OSExecute;
import ThinkingInJava.zUtils.Generator;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.*;

import static ThinkingInJava.sEnumerationType.EnumerationType.AlarmPoints.*;
import static ThinkingInJava.zUtils.Print.*;

public class EnumerationType {
    //关键字enum可以将一组具名的值的有限集合创建为一种新的类型，而这些具名的值可以作为常规的程序组件使用；
    //19.1 基本enum特性
        //创建enum时，编译器会为你生成一个相关的类，这个类继承自java.lang.Enum；
        //EnumClass.java;
        //将静态导入用于enum
    //19.2 向enum中添加新方法
        //除了不能继承自一个enum之外，我们可以向enum中添加方法，enum甚至可以有main()方法；
        public static enum OzWitch {
            WEST("The first"),
            NORTH("The second"),
            EAST("The third"),
            SOUTH("The fourth");  //分号是必须的；
            private String description;
            private OzWitch(String description) {
                this.description = description;
            }
            public String getDescription() { return description; }
            public static void main(String[] args) {
                for (OzWitch ozWitch : OzWitch.values()) {
                    print(ozWitch + ": " + ozWitch.getDescription());
                }
            }
        }
        //覆盖enum的方法
            //覆盖enum的toString()方法与覆盖一般类的方法没有区别；
    //19.3 switch语句中的enum
        enum Signal { GREEN, YELLOW, RED, }
        public static class TrafficLight {
            Signal color = Signal.RED;
            public void change() {
                switch (color) {
                    case RED:
                        color = Signal.GREEN;
                        break;
                    case GREEN:
                        color = Signal.YELLOW;
                        break;
                    case YELLOW:
                        color = Signal.RED;
                        break;
                }
            }
            public String toString() { return "The traffic light is " + color; }

            public static void main(String[] args) {
                TrafficLight t = new TrafficLight();
                for (int i = 0; i < 7; i++) {
                    print(t);
                    t.change();
                }
            }
        }
    //19.4 values()的神秘之处
        enum Explore { HERE, THERE, }
        public static class Reflection {
            public static Set<String> analyze(Class<?> enumClass) {
                print("------ Analyzing " + enumClass + " ------");
                print("Interfaces: ");
                for (Type t : enumClass.getGenericInterfaces()) {
                    print(t);
                }
                print("Bases: " + enumClass.getSuperclass());
                print("Methods: ");
                Set<String> methods = new TreeSet<>();
                for (Method m : enumClass.getMethods()) {
                    methods.add(m.getName());
                }
                print(methods);
                return methods;
            }
            public static void main(String[] args) {
                Set<String> exploreMethods = analyze(Explore.class);
                Set<String> enumMethods = analyze(Enum.class);
                print("Explore.containsAll(Enum)? " + exploreMethods.containsAll(enumMethods));
                printnb("Explore.removeAll(Enum) ");
                exploreMethods.removeAll(enumMethods);
                print(exploreMethods);
                OSExecute.command("javap Explore");
            }
        }
        //values()是由编译器添加的static方法；
    //19.5 实现，而非继承
        //所有的enum都继承自java.lang.Enum类，所以自定义enum时只能实现接口；
        enum CartoonCharacter implements Generator<CartoonCharacter> {
            SLAPPY, SPANKY, PUNCHY, SILLY, BOUNCY, NUTTY, BOB;
            private Random rand = new Random(47);

            @Override
            public CartoonCharacter next() {
                return values()[rand.nextInt(values().length)];
            }
        }
        public static class EnumImplementation {
            public static <T> void printNext(Generator<T> rg) {
                printnb(rg.next() + ", ");
            }
            public static void main(String[] args) {
                CartoonCharacter cc = CartoonCharacter.BOB;
                for (int i = 0; i < 10; i++) {
                    printNext(cc);
                }
            }
        }
    //19.6 随机选取
        //Enums.class;
        enum Activity { SITTING, LYING, STANDING, RUNNING, }
        public static class RandomTest {
            public static void main(String[] args) {
                for (int i = 0; i < 20; i++) {
                    printnb(Enums.random(Activity.class) + " ");
                }
            }
        }
    //19.7 使用接口组织枚举
        //在一个接口的内部，创建实现该接口的枚举，以此将元素进行分组，可以达到将枚举元素分类组织的目的；
        public interface Food {
            enum Appetizer implements Food {
                SALAD, SOUP, SPRING_ROLLS;
            }
            enum MainCourse implements Food {
                LASAGNE, BURRITO, LENTILS;
            }
            enum Dessert implements Food {
                TIRAMISU, GELATO, FRUIT;
            }
            enum Coffee implements Food {
                LATTE, CUPPUCCINO, TEA;
            }
        }
        public enum Course {
            APPETIZER(Food.Appetizer.class),
            MAINCOURSE(Food.MainCourse.class),
            DESSERT(Food.Dessert.class),
            COFFEE(Food.Dessert.class);
            private Food[] values;
            private Course(Class<? extends Food> kind) {
                values = kind.getEnumConstants();
            }
            public Food randomSelection() {
                return Enums.random(values);
            }
         }
    //19.8 使用EnumSet替代标志
        //引入EnumSet是为了通过enum创建一种替代品，以替代传统的基于int的"位标志"；
        //使用EnumSet在说明一个二进制位是否存在时，具有更好地表达能力，并且无需担心性能；
        public enum AlarmPoints {
            STAIR1, STAIR2, LOBBY, OFFICE1, OFFICE2, OFFICE3, OFFICE4, BATHROOM, UTILITY, KITCHEN,
        }
        public static class EnumSets {
            public static void main(String[] args) {
                EnumSet<AlarmPoints> points = EnumSet.noneOf(AlarmPoints.class);
                points.add(BATHROOM);
                print(points);
                points.addAll(EnumSet.of(STAIR1, STAIR2, KITCHEN));
                print(points);
                points = EnumSet.allOf(AlarmPoints.class);
                points.removeAll(EnumSet.of(STAIR1, STAIR2, KITCHEN));
                print(points);
                points.removeAll(EnumSet.range(OFFICE1, OFFICE4));
                print(points);
                points = EnumSet.complementOf(points);  //补集；
                print(points);
            }
        }
    //19.9 使用EnumMap
        //一般来说，【命令模式】首先需要一个只有单一方法的接口，然后从该接口实现具有各自不同的行为的多个子类；
        interface Command{ void action(); }
        public static class EnumMaps {
            public static void main(String[] args) {
                EnumMap<AlarmPoints, Command> em = new EnumMap<>(AlarmPoints.class);
                em.put(KITCHEN, () -> print("Kitchen fire!"));
                em.put(BATHROOM, () -> print("Bathroom alert!"));
                for (Map.Entry<AlarmPoints, Command> e : em.entrySet()) {
                    printnb(e.getKey() + ": ");
                    e.getValue().action();
                }
                try {
                    em.get(UTILITY).action();
                } catch (Exception e) {
                    print(e);
                }
            }
        }
        //与EnumSet一样，enum实例定义时的次序决定了其在EnumMap中的顺序；
    //19.10 常量相关的方法
        //允许程序员为enum实例编写方法，从而为每个enum实例赋予各自不同的行为；
        public enum COnstantSpecificMethod {
            DATE_TIME {
                String getInfo() {
                    return DateFormat.getInstance().format(new Date());
                }
            },
            CLASSPATH {
                String getInfo() {
                    return System.getenv("CLASSPATH");
                }
            },
            VERSION {
                String getInfo() {
                    return System.getProperty("java.version");
                }
            };
            abstract String getInfo();

            public static void main(String[] args) {
                for (COnstantSpecificMethod csm : values()) {
                    print(csm.getInfo());
                }
            }
        }
        //每个enum元素都是一个static final实例；
        //除了实现abstract方法以外，程序员可以覆盖常量相关的方法；
        //使用enum的职责链
            //在【职责链】设计模式中，程序员以多种不同的方式来解决一个问题，然后将它们链接在一起，当一个请求到来时，它遍历这个链，直到链中的某个解决方案能够处理该请求；
            //PostOffice.java;
            //enum定义的次序决定了各个解决策略在应用时的次序；
        //使用enum的状态机
            //枚举类型非常适合用来创建状态机；
            //Input.java; VendingMachine.java;
    //19.11 多路分发
        //Java只支持单路分发；
        //Outcome.java; RoShamBo1.java;
        //使用enum分发
            //使用构造器来初始化每个enum实例，并以"一组"结果作为参数；
            //RoShamBo2.java; RoShamBo.java; Competitor.java;
        //使用常量相关的方法
            //常量相关的方法允许我们为每个enum实例提供方法的不同实现；
            //RoShamBo3.java; RoShamBo4.java;
        //使用EnumMap分发
            //使用EnumMap能够实现"真正的"两路分发；
            //RoShamBo5.java;
        //使用二维数组
            //每个enum实例都有一个固定的值（基于其声明的次序），并且可以通过ordinal()方法取得该值；
            //RoShamBo6.java;
            //对于某类问题而言，"表驱动式编码"的概念具有非常强大的功能；
    //19.12 总结
}
