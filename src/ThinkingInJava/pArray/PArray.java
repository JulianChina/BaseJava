package ThinkingInJava.pArray;

import net.mindview.util.Generated;
import net.mindview.util.Generator;
import net.mindview.util.RandomGenerator;
import ThinkingInJava.zUtils.Print;

import java.util.*;

public class PArray {
    //16.1 数组为什么特殊
        //数组与其他容器之间的区别有三方面：效率、类型、保存基本类型的能力；
        //数组硕果仅存的优点就是效率；
    //16.2 数组是第一级对象
        //对象数组保存的是引用，基本类型数组直接保存基本类型的值；
    //16.3 返回一个数组
        //其实是返回一个数组引用；
    //16.4 多维数组
        //多维数组中构成矩阵的每个向量都可以具有任意的长度，这被称为粗糙数组；
        //Arrays.deepToString()方法对基本类型数组和对象数组都起作用；
        public static class MultiDimWrapperArray {
            public static void main(String[] args) {
                int[][] a = {{1, 2, 3}, {4, 5, 6}};
                Double[][] b = {{1.1, 2.2, 3.3}, {4.4, 5.5, 6.6}};
                String[][] c = {{"I", "am", "a", "programmer"}, {"Trust", "me"}, {"Happy", "New", "Year"}};
                Print.print(Arrays.deepToString(a));
                Print.print(Arrays.deepToString(b));
                Print.print(Arrays.deepToString(c));
            }
        }
    //16.5 数组与泛型
        //通常，数组与泛型不能很好地结合；你不能实例化具有参数化类型的数组；
        //可以参数化数组本身的类型；
        static class ClassParameter<T> {
           public T[] f(T[] arg) { return arg; }
        }
        static class MethodParameter {
            public static <T> T[] f(T[] arg) { return arg; }
        }
        public static class ParameterizedArrayType {
            public static void main(String[] args) {
                Integer[] ints = {1, 2, 3, 4, 5};
                Double[] doubles = {1.1, 2.2, 3.3, 4.4, 5.5};
                Integer[] ints2 = new ClassParameter<Integer>().f(ints);
                Double[] doubles2 = new ClassParameter<Double>().f(doubles);
                ints2 = MethodParameter.f(ints);
                doubles2 = MethodParameter.f(doubles);
            }
        }
        //首选参数化方法，因为其可以避免为需要应用的每种不同的类型都使用一个参数去实例化这个参数化类；
        //编译器确实不让你实例化泛型数组，但是，它允许你创建对这种数组的引用；
        //尽管你不能创建实际的持有泛型的数组对象，但是你可以创建非泛型的数组，然后将其转型；
        //一般而言，你会发现泛型在类或方法的边界处很有效，而在类或方法的内部，擦除通常会使泛型变得不适用；
    //16.6 创建测试数据
        //Arrays.fill()
            public static class FillingArray {
                public static void main(String[] args) {
                    int size = 6;
                    boolean[] a1 = new boolean[size];
                    byte[] a2 = new byte[size];
                    char[] a3 = new char[size];
                    short[] a4 = new short[size];
                    int[] a5 = new int[size];
                    long[] a6 = new long[size];
                    float[] a7 = new float[size];
                    double[] a8 = new double[size];
                    String[] a9 = new String[size];
                    Arrays.fill(a1, true);
                    Arrays.fill(a2, (byte) 11);
                    Arrays.fill(a3, 'x');
                    Arrays.fill(a4, (short) 17);
                    Arrays.fill(a5, 19);
                    Arrays.fill(a6, 23);
                    Arrays.fill(a7, 29);
                    Arrays.fill(a8, 47);
                    Arrays.fill(a9, "Hello");
                    Print.print("a1 = " + Arrays.toString(a1));
                    Print.print("a2 = " + Arrays.toString(a2));
                    Print.print("a3 = " + Arrays.toString(a3));
                    Print.print("a4 = " + Arrays.toString(a4));
                    Print.print("a5 = " + Arrays.toString(a5));
                    Print.print("a6 = " + Arrays.toString(a6));
                    Print.print("a7 = " + Arrays.toString(a7));
                    Print.print("a8 = " + Arrays.toString(a8));
                    Print.print("a9 = " + Arrays.toString(a9));
                    Arrays.fill(a9, 3, 5, "World");
                    Print.print("a9 = " + Arrays.toString(a9));
                }
            }
        //数据生成器
            //通过选择Generator的类型来创建任何类型的数据，这是策略设计模式的一个实例————每个不同的Generator都表示一个不同的策略；
            //CountingGenerator.java;RandomGenerator.java;
        //从Generator中创建数组
            //Generated.java;
    //16.7 Arrays实用功能
        //java.util.Arrays.equals()/deepEquals()/fill()/sort()/binarySearch()/toString()/hashCode();
        //复制数组
            public static class CopyingArrays {
                public static void main(String[] args) {
                    int[] i = new int[7];
                    int[] j = new int[10];
                    Arrays.fill(i, 47);
                    Arrays.fill(j, 99);
                    Print.print("i = " + Arrays.toString(i));
                    Print.print("j = " + Arrays.toString(j));
                    System.arraycopy(i, 0, j, 0, i.length);
                    Print.print("j = " + Arrays.toString(j));
                    int[] k = new int[5];
                    System.arraycopy(i, 0, k, 0, k.length);
                    Print.print("k = " + Arrays.toString(k));
                    Arrays.fill(k, 103);
                    System.arraycopy(k, 0, i, 0, k.length);
                    Print.print("i = " + Arrays.toString(i));
                    Integer[] u = new Integer[10];
                    Integer[] v = new Integer[5];
                    Arrays.fill(u, 47);
                    Arrays.fill(v, 99);
                    Print.print("u = " + Arrays.toString(u));
                    Print.print("v = " + Arrays.toString(v));
                    System.arraycopy(v, 0, u, u.length/2, v.length);
                    Print.print("u = " + Arrays.toString(u));
                }
            }
            //如果复制对象数组，那么只是复制了对象引用————而不是对象本身的拷贝，此乃浅复制；
            //System.arrayCopy()不会执行自动包装和自动拆包，两个数组必须具有相同的确切类型；
        //数组的比较
            //Arrays提供了重载后的equals()方法：数组相等，必须元素个数相等，且对应位置的元素也相等；
        //数组元素的比较
            //【策略模式】将不同的比较方式封装成策略对象，保持算法的不变性；
            //Java有两种方式来提供比较功能：
            //java.lang.Comparable;
            public static class CompType implements Comparable<CompType> {
                int i, j;
                private static int count = 1;
                public CompType(int n1, int n2) {
                    i = n1;
                    j = n2;
                }
                public String toString() {
                    String result = "[i = " + i + ", j = " + j + "]";
                    if (count++ % 3 == 0) {
                        result += "\n";
                    }
                    return result;
                }
                @Override
                public int compareTo(CompType rv) {
                    return Integer.compare(i, rv.i);
                }

                private static Random r = new Random(47);
                public static Generator<CompType> generator() {
                    return new Generator<CompType>() {
                        @Override
                        public CompType next() {
                            return new CompType(r.nextInt(100), r.nextInt(100));
                        }
                    };
                }

                public static void main(String[] args) {
                    CompType[] a = Generated.array(new CompType[12], generator());
                    Print.print("before sorting:");
                    Print.print(Arrays.toString(a));
                    Arrays.sort(a);
                    Print.print("after sorting:");
                    Print.print(Arrays.toString(a));
                }
            }

            //java.util.Comparator;
            public static class Reverse {
                public static void main(String[] args) {
                    CompType[] a = Generated.array(new CompType[12], CompType.generator());
                    Print.print("before sorting:");
                    Print.print(Arrays.toString(a));
                    Arrays.sort(a, Collections.reverseOrder());
                    Print.print("after sorting:");
                    Print.print(Arrays.toString(a));
                }
            }

            //编写自己的Comparator；
            static class CompTypeComparator implements Comparator<CompType> {
                @Override
                public int compare(CompType o1, CompType o2) {
                    return Integer.compare(o1.j, o2.j);
                }
            }

            public static class ComparatorTest {
                public static void main(String[] args) {
                    CompType[] a = Generated.array(new CompType[12], CompType.generator());
                    Print.print("before sorting:");
                    Print.print(Arrays.toString(a));
                    Arrays.sort(a, new CompTypeComparator());
                    Print.print("after sorting:");
                    Print.print(Arrays.toString(a));
                }
            }
        //数组排序
            //使用内置的排序方法，就可以对任意的基本类型数组排序，也可以对任意的对象数组进行排序；只要该对象实现了Comparable接口或具有相关联的Comparator；
            public static class StringSorting {
                public static void main(String[] args) {
                    String[] sa = Generated.array(new String[20], new RandomGenerator.String(5));
                    Print.print("Before sorting:" + Arrays.toString(sa));
                    Arrays.sort(sa);
                    Print.print("After sorting:" + Arrays.toString(sa));
                    Arrays.sort(sa, Collections.reverseOrder());
                    Print.print("Reverse sorting:" + Arrays.toString(sa));
                    Arrays.sort(sa, String.CASE_INSENSITIVE_ORDER);
                    Print.print("Case-insensitive sorting:" + Arrays.toString(sa));
                }
            }
            //Java标准类库中针对基本类型设计的"快速排序"，以及针对对象设计的"稳定归并排序"；
        //在已排序的数组中查找
            //如果数组已经排好序了，就可以使用Arrays.binarySearch()执行快速查找；
            //如果需要对没有重复元素的数组排序，可以使用TreeSet(保持排序顺序)，或者LinkedHashSet(保持插入顺序)；
            //如果使用Comparator排序了某个对象数组(基本类型数组无法使用Comparator进行排序)，在使用binarySearch()时必须提供同样的Comparator；
    //16.8 总结
        //优选容器而不是数组；
}
