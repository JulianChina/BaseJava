package EffectiveJava3rd.fEnumsAndAnnotations;

import java.util.Arrays;
import java.util.Collection;

//38 使用接口模拟可扩展的枚举
public class F38 {
    // Emulated extensible enum using an interface
    public interface Operation {
        double apply(double x, double y);
    }

    public enum BasicOperation implements Operation {
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

        BasicOperation(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }
    }

    // Emulated extension enum
    public enum ExtendedOperation implements Operation {
        EXP("^") {
            public double apply(double x, double y) {
                return Math.pow(x, y);
            }
        },
        REMAINDER("%") {
            public double apply(double x, double y) {
                return x % y;
            }
        };
        private final String symbol;

        ExtendedOperation(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }
    }

    //不必在枚举中声明apply抽象方法，就像您在具有实例特定方法实现的非扩展枚举中所做的那样(第162页)。这是因为抽象方法(apply)是接口(Operation)的成员。

    public static void main(String[] args) {
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        test(ExtendedOperation.class, x, y);

        test(Arrays.asList(ExtendedOperation.values()), x, y);
    }

    private static <T extends Enum<T> & Operation> void test(Class<T> opEnumType, double x, double y) {
        for (Operation op : opEnumType.getEnumConstants())
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
    }

    private static void test(Collection<? extends Operation> opSet, double x, double y) {
        for (Operation op : opSet)
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
    }

    //虽然不能编写可扩展的枚举类型，但是你可以编写一个接口来配合实现接口的基本的枚举类型，来对它进行模拟。
}
