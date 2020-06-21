package EffectiveJava3rd.hMethods;

import java.util.List;

//52 明智审慎地使用重载
public class H52 {
    //在编译时选择要调用哪个重载方法。
    //重载(overloaded)方法之间的选择是静态的，而重写(overridden)方法之间的选择是动态的。
    //根据调用方法的对象的运行时类型，在运行时选择正确版本的重写方法。作为提醒，当子类包含与父类中具有相同签名的方法声明时，会重写此方法。
    //如果在子类中重写实例方法并且在子类的实例上调用，则无论子类实例的编译时类型如何，都会执行子类的重写方法。
    class Wine {
        String name() { return "wine"; }
    }
    class SparklingWine extends Wine {
        @Override String name() { return "sparkling wine"; }
    }
    class Champagne extends SparklingWine {
        @Override String name() { return "champagne"; }
    }
//    public static class Overriding {
//        public static void main(String[] args) {
//            List<Wine> wineList = List.of(new Wine(), new SparklingWine(), new Champagne());
//            for (Wine wine : wineList)
//                System.out.println(wine.name());
//        }
//    }
    //当调用重写方法时，对象的编译时类型对执行哪个方法没有影响；总是会执行"最具体(most specific)"的重写方法。
    //应该避免混淆使用重载。
    //一个安全和保守的策略是永远不要导出两个具有相同参数数量的重载。
    //如果一个方法使用了可变参数，保守策略是根本不重载它。总是可以为方法赋予不同的名称，而不是重载它们。
    //对于构造方法，无法使用不同的名称：类的多个构造函数总是被重载。在许多情况下，可以选择导出静态工厂而不是构造方法(条目1)。
    //使用构造方法，不必担心重载和重写之间的影响，因为构造方法不能被重写。

    //List<E>接口对remove方法有两个重载：remove(E)和remove(int)。
    //自动装箱和泛型在重载时增加了谨慎的重要性。
    //不要在相同参数位置重载采用不同函数式接口的方法。

    //最好避免重载具有相同数量参数的多个签名的方法。在某些情况下，特别是涉及构造方法的情况下，可能无法遵循此建议。
}
