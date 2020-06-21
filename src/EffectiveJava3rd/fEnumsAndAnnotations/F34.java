package EffectiveJava3rd.fEnumsAndAnnotations;

//34 使用枚举类型替代整型常量
public class F34 {
    //Java支持两种引用类型的特殊用途的系列：一种称为枚举类型的类和一种称为注解类型的接口。
    //Java的枚举类型是完整的类，比其他语言中的其他语言更强大，其枚举本质本上是int值。
    //Java枚举类型背后的基本思想很简单：它们是通过公共静态final属性为每个枚举常量导出一个实例的类。
    //由于客户既不能创建枚举类型的实例也不能继承它，除了声明的枚举常量外，不能有任何实例。换句话说，枚举类型是实例控制的。
    //除了纠正int枚举的缺陷之外，枚举类型还允许添加任意方法和属性并实现任意接口。它们提供了所有Object方法的高质量实现，它们实现了Comparable和Serializable。
    // Enum type with data and behavior
    public enum Planet {
        MERCURY(3.302e+23, 2.439e6),
        VENUS(4.869e+24, 6.052e6),
        EARTH(5.975e+24, 6.378e6),
        MARS(6.419e+23, 3.393e6),
        JUPITER(1.899e+27, 7.149e7),
        SATURN(5.685e+26, 6.027e7),
        URANUS(8.683e+25, 2.556e7),
        NEPTUNE(1.024e+26, 2.477e7);
        private final double mass;  // In kilograms
        private final double radius;  // In meters
        private final double surfaceGravity;  // In m / s^2
        // Universal gravitational constant in m^3 / kg s^2
        private static final double G = 6.67300E-11;

        // Constructor
        Planet(double mass, double radius) {
            this.mass = mass;
            this.radius = radius;
            surfaceGravity = G * mass / (radius * radius);
        }

        public double mass() {
            return mass;
        }

        public double radius() {
            return radius;
        }

        public double surfaceGravity() {
            return surfaceGravity;
        }

        public double surfaceWeight(double mass) {
            return mass * surfaceGravity; // F = ma
        }
    }

    //要将数据与枚举常量相关联，请声明实例属性并编写一个构造方法，构造方法带有数据并将数据保存在属性中。
    public static class WeightTable {
        public static void main(String[] args) {
            double earthWeight = Double.parseDouble(args[0]);
            double mass = earthWeight / Planet.EARTH.surfaceGravity();
            for (Planet p : Planet.values())
                System.out.printf("Weight on %s is %f%n", p, p.surfaceWeight(mass));
        }
    }

    //一些与枚举常量相关的行为只需要在定义枚举的类或包中使用。这些行为最好以私有或包级私有方式实现。
    //在枚举类型中声明一个抽象的apply方法，并用常量特定的类主体中的每个常量的具体方法重写它。这种方法被称为特定于常量(constant-specific)的方法实现。
    // Enum type with constant-specific class bodies and data
    public enum Operation {
        PLUS("+") {
            public double apply(double x, double y) {
                return x + y;
            }
        },
        MINUS("-") {
            public double apply(double x, double y) {
                return x - y;
            }
        },
        TIMES("*") {
            public double apply(double x, double y) {
                return x * y;
            }
        },
        DIVIDE("/") {
            public double apply(double x, double y) {
                return x / y;
            }
        };
        private final String symbol;

        Operation(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public abstract double apply(double x, double y);
    }

    public static void main(String[] args) {
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        for (Operation op : Operation.values())
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
    }

    //除了编译时常量属性(条目34)之外，枚举构造方法不允许访问枚举的静态属性。此限制是必需的，因为静态属性在枚举构造方法运行时尚未初始化。这种限制的一个特例是枚举常量不能从构造方法中相互访问。

    // The strategy enum pattern
    enum PayrollDay {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY(PayType.WEEKEND), SUNDAY(PayType.WEEKEND);
        private final PayType payType;
        PayrollDay(PayType payType) { this.payType = payType; }
        PayrollDay() { this(PayType.WEEKDAY); } // Default
        int pay(int minutesWorked, int payRate) {
            return payType.pay(minutesWorked, payRate);
        }
        // The strategy enum type
        private enum PayType {
            WEEKDAY {
                int overtimePay(int minsWorked, int payRate) {
                    return minsWorked <= MINS_PER_SHIFT ? 0 : (minsWorked - MINS_PER_SHIFT) * payRate / 2;
                }
            },
            WEEKEND {
                int overtimePay(int minsWorked, int payRate) {
                    return minsWorked * payRate / 2;
                }
            };
            abstract int overtimePay(int mins, int payRate);
            private static final int MINS_PER_SHIFT = 8 * 60;
            int pay(int minsWorked, int payRate) {
                int basePay = minsWorked * payRate;
                return basePay + overtimePay(minsWorked, payRate);
            }
        }
    }

    //一个枚举类型中的常量集不需要一直保持不变。枚举功能是专门设计用于允许二进制兼容的枚举类型的演变。

    //许多枚举不需要显式构造方法或成员，但其他人则可以通过将数据与每个常量关联并提供行为受此数据影响的方法而受益。
    //使用单一方法关联多个行为可以减少枚举。在这种相对罕见的情况下，更喜欢使用常量特定的方法来枚举自己的值。
    //如果一些(但不是全部)枚举常量共享共同行为，请考虑策略枚举模式。
}
