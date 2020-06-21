package EffectiveJava3rd.lSerialization;

import java.util.Arrays;

//89 对于实例控制，枚举类型优于readResolve
public class L89 {
    public static class Elvis {
        public static final Elvis INSTANCE = new Elvis();

        private Elvis() {
        }

        public void leaveTheBuilding() {
        }

        //如果Elvis类要实现Serializable接口，下面的readResolve方法就足以保证它的单例属性：
        // readResolve for instance control - you can do better!
        private Object readResolve() {
            // Return the one true Elvis and let the garbage collector
            // take care of the Elvis impersonator.
            return INSTANCE;
        }
    }
    //如果在这个类上面增加implements Serializable的字样，它就不是一个单例。
    //任何一个readObject方法，不管是显式的还是默认的，都会返回一个新建的实例，这个新建的实例不同于类初始化时创建的实例。
    //readResolve特性允许你用readObject创建的实例代替另外一个实例。
    //如果依赖readResolve进行实例控制，带有对象引用类型的所有实例字段都必须声明为transient。

    //如果将一个可序列化的实例受控的类编写为枚举，Java就可以绝对保证出了所声明的常量之外，不会有其他实例，除非攻击者恶意的使用了享受特权的方法。
    // 如AccessibleObject.setAccessible。能够做到这一点的任何一位攻击者，已经拥有了足够的特权来执行任意的本地代码，后果不堪设想。
    // Enum singleton - the preferred approach
    public enum ElvisEnum {
        INSTANCE;
        private String[] favoriteSongs = {"Hound Dog", "Heartbreak Hotel"};

        public void printFavorites() {
            System.out.println(Arrays.toString(favoriteSongs));
        }
    }
    //readResolve的可访问性(accessibility)十分重要。
    // 如果把readResolve方法放在一个final类上面，它应该是私有的。
    // 如果把readResolve方法放在一个非final类上，就必须认真考虑它的的访问性。

    //应该尽可能的使用枚举类型来实施实例控制的约束条件。
    //如果做不到，同时又需要一个即可序列化又可以实例受控的类，就必须提供一个readResolve方法，并确保该类的所有实例化字段都是基本类型，或者是transient的。
}
