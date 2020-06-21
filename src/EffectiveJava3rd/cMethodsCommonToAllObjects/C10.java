package EffectiveJava3rd.cMethodsCommonToAllObjects;

//10 重写equals方法时遵守通用约定
public class C10 {
    //每个类的实例都是固有唯一的。Object提供的equals实现对这些类完全是正确的行为。
    //类不需要提供一个"逻辑相等"的测试功能。
    //父类已经重写了equals方法，则父类行为完全适合于该子类。
    //类是私有的或包级私有的，可以确定它的equals方法永远不会被调用。如果你非常厌恶风险，可以重写equals方法，以确保不会被意外调用。

    //如果一个类包含一个逻辑相等的概念，此概念有别于对象标识，而且父类还没有重写过equals方法。这通常用在值类的情况。值类只是一个表示值的类。
    //一种不需要equals方法重写的值类是使用实例控制的类，以确保每个值至多存在一个对象。枚举类型属于这个类别。

    //equals方法实现了一个等价关系，有如下属性：
    //自反性：对于任何非空引用x，x.equals(x)必须返回true。
    //对称性：对于任何非空引用x和y，如果且仅当y.equals(x)返回true时，x.equals(y)必须返回true。
    //传递性：对于任何非空引用x、y、z，如果x.equals(y)返回true，y.equals(z)返回true，则x.equals(z)必须返回true。
    //一致性：对于任何非空引用x和y，如果在equals比较中使用的信息没有修改，则x.equals(y)的多次调用必须始终返回true或始终返回false。
    //非空性：对于任何非空引用x，x.equals(null)必须返回false。

    //编写高质量的equals方法的配方：
    //1.使用==运算符检查参数是否为该对象的引用。如果是，返回true。
    //2.使用instanceOf运算符来检查参数是否具有正确的类型。如果不是，则返回false。
    //3.参数转换为正确的类型。因为转换操作在instanceOf中已经处理过，所以它肯定会成功。
    //4.对于类中的每个"重要"的属性，请检查该参数属性是否与该对象对应的属性相匹配。如果所有这些测试成功，返回true，否则返回false。

    public static final class PhoneNumber {
        private final short areaCode, prefix, lineNum;

        public PhoneNumber(int areaCode, int prefix, int lineNum) {
            this.areaCode = rangeCheck(areaCode, 999, "area code");
            this.prefix = rangeCheck(prefix, 999, "prefix");
            this.lineNum = rangeCheck(lineNum, 999, "line num");
        }

        private static short rangeCheck(int val, int max, String arg) {
            if (val < 0 || val > max) {
                throw new IllegalArgumentException(arg + ": " + val);
            }
            return (short) val;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof PhoneNumber)) {
                return false;
            }
            PhoneNumber pn = (PhoneNumber) o;
            return pn.lineNum == lineNum && pn.prefix == prefix && pn.areaCode == areaCode;
        }
    }

    //1.当重写equals方法时，同时也要重写hashCode方法。
    //2.不要让equals方法试图太聪明。如果只是简单地测试用于相等的属性，那么要遵守equals约定并不困难。
    //3.在equals时方法声明中，不要将参数Object替换成其他类型。

    //在很多情况下，不要重写equals方法，从Object继承的实现完全是你想要的。如果你确实重写了equals方法，那么一定要比较这个类的所有重要属性，并且以保护前面equals约定里5个规定的方式去比较。
}
