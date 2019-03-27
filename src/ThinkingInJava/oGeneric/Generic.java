package ThinkingInJava.oGeneric;

import net.mindview.util.RandomGenerator;
import net.mindview.util.Tuple;
import net.mindview.util.TwoTuple;
import ThinkingInJava.zAssistant.generics.Mixins;
import ThinkingInJava.zUtils.*;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Generic {
    //基类->接口->泛型；
    //15.1 与C++的比较
        //C++的模板；
    //15.2 简单泛型
        public static class Holder<T> {
            private T a;
            public Holder(T a) { this.a = a; }
            public void set(T a) { this.a = a; }
            public T get() { return a; }

            public static void main(String[] args) {
                Holder<AtomicBoolean> h = new Holder<>(new AtomicBoolean(true));
                AtomicBoolean ab = h.get();
                Print.print(ab);
            }
        }
        //一个元组类库
            //仅一次方法调用就能返回多个对象；创建专门的类、元组；
            //元组是将一组对象直接打包存储于其中的一个单一对象，仅允许读取其中的元素，不允许向其中存放新的对象；
            //元组可以具有任意长度；元组中的对象可以是任意不同类型；
            public class TwoTurple<AA, B> {
                public final AA first;
                public final B second;
                public TwoTurple(AA a, B b) { first = a; second = b; }
                public String toString() { return "(" + first + ", " + second + ")"; }
            }
            //可以利用继承机制实现长度更长的元组；
            public class ThreeTurple<AA, B, C> extends TwoTurple<AA, B> {
                public final C third;
                public ThreeTurple(AA a, B b, C c) {
                    super(a, b);
                    third = c;
                }
                public String toString() { return "(" + first + ", " + second + ", " + third + ")"; }
            }
        //一个堆栈类
            public static class LinkedStack<T> {
                public static class Node<U> {
                    U item;
                    Node<U> next;
                    Node() { item = null; next = null; }
                    Node(U item, Node<U> next) { this.item = item; this.next = next; }
                    boolean end() { return (item==null && next==null); }
                }
                private Node<T> top = new Node<T>();
                public void push(T item) {
                    top = new Node<T>(item, top);
                }
                public T pop() {
                    T result = top.item;
                    if (!top.end()) {
                        top = top.next;
                    }
                    return result;
                }

                public static void main(String[] args) {
                    LinkedStack<String> lss = new LinkedStack<>();
                    for (String s : "Phasers or stun!".split(" ")) lss.push(s);
                    String s;
                    while((s = lss.pop()) != null) {
                        Print.print(s);
                    }
                }
            }
        //RandomList
        public static class RandomList<T> {
            private ArrayList<T> storage = new ArrayList<>();
            private Random rand = new Random(47);
            public void add(T item) { storage.add(item); }
            public T select() { return storage.get(rand.nextInt(storage.size())); }

            public static void main(String[] args) {
                RandomList<String> rs = new RandomList<>();
                for (String s : ("Today is first February, the fifth of a week!").split(" ")) {
                    rs.add(s);
                }
                for (int i = 0; i < 11; i++) {
                    Print.print(rs.select());
                }
            }
        }
    //15.3 泛型接口
        //泛型也可以应用于接口；
        //生成器是一种专门负责创建对象的类，这是【工厂方法】设计模式的一种运用；生成器创建对象时不需要任何参数，工厂方法一般需要参数；
        public static class CoffeeGenerator implements Generator<Coffee>, Iterable<Coffee> {
            private Class[] types = { Coffee.Latte.class, Coffee.Mocha.class, Coffee.Cappuccino.class,
                    Coffee.Americano.class, Coffee.Breve.class };
            private static Random rand = new Random(47);
            public CoffeeGenerator() { }
            private int size = 0;
            public CoffeeGenerator(int sz) { size = sz; }
            public Coffee next() {
                try {
                    return (Coffee) types[rand.nextInt(types.length)].newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            class CoffeeIterator implements Iterator<Coffee> {
                int count = size;
                public boolean hasNext() { return count>0; }
                public Coffee next() {
                    count--;
                    return CoffeeGenerator.this.next();
                }
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            }
            public Iterator<Coffee> iterator() {
                return new CoffeeIterator();
            }

            public static void main(String[] args) {
                CoffeeGenerator gen = new CoffeeGenerator();
                for (int i = 0; i < 5; i++) {
                    Print.print(gen.next());
                }
                for (Coffee c : new CoffeeGenerator(5)) {
                    Print.print(c);
                }
            }
        }
        //【适配器】模式实例：P360
    //15.4 泛型方法
        //是否拥有泛型方法，与其所在的类是否是泛型没有关系；
        //泛型方法使得该方法能够独立于类而产生变化；
        //1.如果使用泛型方法能够取代将整个类泛型化，那么就应该只使用泛型方法；
        //2.对于一个static的方法而言，无法访问泛型类的类型参数，所以，如果static方法需要使用泛型能力，就必须使其成为泛型方法；
        //要定义泛型方法，只需将泛型参数列表置于返回值之前；
        public static class GenericMethods {
            public <T> void f(T x) {
                Print.print(x.getClass().getSimpleName());
            }

            public static void main(String[] args) {
                GenericMethods gm = new GenericMethods();
                gm.f("");
                gm.f(1);
                gm.f(1.0);
                gm.f(1.0F);
                gm.f('c');
                gm.f(gm);
            }
        }
        //使用泛型类时，必须在创建对象的时候指定类型参数的值，而使用泛型方法的时候，通常不必指明参数类型；
        //杠杆利用类型参数推断
            //类型推断只对赋值操作有效；若将泛型方法调用的结果作为参数，传递给另一个方法，编译器不会执行类型推断；
            //显示的类型说明".<>"；
        //可变参数与泛型方法
            //泛型方法与可变参数列表能够很好地共存；
            public static class GenericVarags {
                public static <T> List<T> makeList(T... args) {
                    List<T> result = new ArrayList<>();
                    Collections.addAll(result, args);
                    return result;
                }

            public static void main(String[] args) {
                List<String> ls = makeList("A");
                Print.print(ls);
                ls = makeList("A", "B", "C", "D");
                Print.print(ls);
                ls = makeList("ABCDEFGHIJKLMNOPQRSTUVWXYZ".split(""));
                Print.print(ls);
            }
            }
        //用于Generator的泛型方法
        public static class Generators {
            public static <T> Collection<T> fill(Collection<T> coll, Generator<T> gen, int n) {
                for (int i = 0; i < n; i++) {
                    coll.add(gen.next());
                }
                return coll;
            }

            public static void main(String[] args) {
                Collection<Coffee> coffees = fill(new ArrayList<>(), new CoffeeGenerator(), 4);
                Print.print(coffees);
            }
        }
        //一个通用的Generator
        public static class CountedObject {
            private static long counter = 0;
            private final long id = counter++;
            public long id() { return id; }
            public String toString() { return "CountedObject " + id; }
        }
        public static class BaseGeneratorDemo {
            public static void main(String[] args) {
                Generator<CountedObject> gen = BaseGenerator.create(CountedObject.class);
                for (int i = 0; i < 5; i++) {
                    Print.print(gen.next());
                }
            }
        }
        //简化元组的使用
            //类型参数推断，以及static方法；
        //一个Set实用工具
            //Sets.java;
            public enum Watercolors {
                ZINC, LEMON_YELLOW, MEDIUM_YELLOW, DEEP_YELLOW, ORANGE, BRILLIANT_RED,
                CRIMSON, MAGENTA, ROSE_MADDER, VIOLET, CERULEAN_BLUE_HUE, PHTHALO_BLUE,
                ULTRAMARINE, COBALT_BLUE_HUE, PERMANENT_GREEN, VIRIDIAN_HUE, SAP_GREEN,
                YELLOW_OCHRE, BURNT_SIENNA, RAW_UMBER, BURNT_UMBER, PAYNES_GRAY, IVORY_BLACK
            }
            public static class WatercolorSets {
                public static void main(String[] args) {
                    Set<Watercolors> set1 = EnumSet.range(Watercolors.BRILLIANT_RED, Watercolors.VIRIDIAN_HUE);
                    Set<Watercolors> set2 = EnumSet.range(Watercolors.CERULEAN_BLUE_HUE, Watercolors.BURNT_UMBER);
                    Print.print("set1: " + set1);
                    Print.print("set2: " + set2);
                    Print.print("Sets.union(set1, set2): " + Sets.union(set1, set2));
                    Set<Watercolors> subSet = Sets.intersection(set1, set2);
                    Print.print("Sets.intersection(set1, set2): " + subSet);
                    Print.print("Sets.difference(set1, subSet): " + Sets.difference(set1, subSet));
                    Print.print("Sets.difference(set2, subSet): " + Sets.difference(set2, subSet));
                    Print.print("Sets.complement(set1, set2): " + Sets.complement(set1, set2));
                }
            }
            //ContainerMethodDifferences.java;
    //15.5 匿名内部类
        //泛型还可以应用于内部类及匿名内部类；
    //15.6 构建复杂模型
        //泛型的一个重要好处是能够简单而安全地创建复杂的模型；
    //15.7 擦除的神秘之处
        static class Frob { }
        static class Fnorkle { }
        static class Quark<Q> { }
        static class Particle<POSITION, MOMENTUM> { }
        public static class LostInformation {
            public static void main(String[] args) {
                List<Frob> list = new ArrayList<>();
                Map<Frob, Fnorkle> map = new HashMap<>();
                Quark<Fnorkle> quark = new Quark<>();
                Particle<Long, Double> p = new Particle<>();
                Print.print(Arrays.toString(list.getClass().getTypeParameters()));
                Print.print(Arrays.toString(map.getClass().getTypeParameters()));
                Print.print(Arrays.toString(quark.getClass().getTypeParameters()));
                Print.print(Arrays.toString(p.getClass().getTypeParameters()));
            }
        }
        //在泛型代码内部，无法获得任何有关泛型参数类型的信息；
        //Java泛型是使用擦除来实现的，当在使用泛型时，任何具体的类型信息都被擦除了；
        //C++的方式
            //泛型类型参数将擦除到它的第一个边界；
        //迁移兼容性
        //擦除的问题
            //擦除主要的正当理由是从非泛化代码到泛化代码的转变过程，以及在不破坏现有类库的情况下，将泛型融入Java语言；
            //泛型不能用于显示地引用运行时类型的操作中，例如转型、instanceof操作和new表达式，因为所有关于参数的类型信息都丢失了；
            //无论何时，当你在编写泛型代码时，必须时刻提醒自己，你只是看起来好像拥有有关参数的类型信息而已；
        //边界处的动作
            public static class ArrayMaker<T> {
                private Class<T> kind;
                public ArrayMaker(Class<T> kind) { this.kind = kind; }
                @SuppressWarnings("unckecked")
                T[] create(int size) { return (T[]) Array.newInstance(kind, size); }

                public static void main(String[] args) {
                    ArrayMaker<String> stringMaker = new ArrayMaker<>(String.class);
                    String[] stringArray = stringMaker.create(9);
                    Print.print(Arrays.toString(stringArray));
                }
            }
            //对于在泛型中创建数组，Array.newInstance()是推荐的方式；
            //边界：对象进入和离开方法的地点，这些正是编译器在编译期执行类型检查并插入转型代码的地方；
    //15.8 擦除的补偿
        //有时必须通过引入类型标签来对擦除进行补偿，意味着需要显式地传递类型的Class对象；
        static class Building { }
        static class House extends Building {}
        public static class ClassTypeCapture<T> {
            Class<T> kind;
            public ClassTypeCapture(Class<T> kind) { this.kind = kind; }
            public boolean f(Object arg) { return kind.isInstance(arg); }

            public static void main(String[] args) {
                ClassTypeCapture<Building> ctt1 = new ClassTypeCapture<>(Building.class);
                Print.print(ctt1.f(new Building()));
                Print.print(ctt1.f(new House()));
                ClassTypeCapture<House> ctt2 = new ClassTypeCapture<>(House.class);
                Print.print(ctt2.f(new Building()));
                Print.print(ctt2.f(new House()));
            }
        }
        //创建类型实例
            //显式的工厂
            interface FactoryI<T> { T create(); }
            static class Foo2<T> {
                private T x;
                public <F extends FactoryI<T>> Foo2(F factory) {
                    x = factory.create();
                }
            }
            static class IntegerFactory implements FactoryI<Integer> {
                public Integer create() {
                    return new Integer(0);
                }
            }
            static class Widget {
                public static class Factory implements FactoryI<Widget> {
                    public Widget create() {
                        return new Widget();
                    }
                }
            }
            public static class FactoryConstraint {
                public static void main(String[] args) {
                    new Foo2<Integer>(new IntegerFactory());
                    new Foo2<Widget>(new Widget.Factory());
                }
            }
            //【模板方法】设计模式
            static abstract class GenericWithCreate<T> {
                final T element;
                GenericWithCreate() { element = create(); }
                abstract T create();
            }
            static class X { }
            static class Creator extends GenericWithCreate<X> {
                X create() { return new X(); }
                void f() { Print.print(element.getClass().getSimpleName()); }
            }
            static class CreatorGeneric {
                public static void main(String[] args) {
                    Creator c = new Creator();
                    c.f();
                }
            }
        //泛型数组
            //一般的解决方案是在任何想要创建泛型数组的地方都使用ArrayList；
            //成功创建泛型数组的唯一方式是创建一个被擦除类型的新数组，然后对其转型；
            //没有任何方式可以推翻底层的数组类型；
            public static class GenericArrayWithTypeToken<T> {
                private T[] array;
                public GenericArrayWithTypeToken(Class<T> type, int sz) {
                    array = (T[]) Array.newInstance(type, sz);
                }
                public void put(int index, T item) {
                    array[index] = item;
                }
                public T get(int index) { return array[index]; }
                public T[] rep() { return array; }

            public static void main(String[] args) {
                GenericArrayWithTypeToken<Integer> gai = new GenericArrayWithTypeToken<>(Integer.class, 10);
                Integer[] ia = gai.rep();
            }
            }
    //15.9 边界
        //边界使得你可以在用于泛型的参数类型上设置限制条件；采用extends关键字来设置；
        //通配符被限制为单一边界；
    //15.10 通配符
        //可以向导出类型的数组赋予基类型的数组引用；
        //编译器有多聪明
        static class Fruit { }
        static class Apple extends Fruit { }
        static class Orange extends Fruit { }
        public static class HolderN<T> {
            private T value;
            public HolderN() { }
            public HolderN(T val) { value = val; }
            public void set(T val) { value = val; }
            public T get() { return value; }
            public boolean equals(Object obj) {
                return value.equals(obj);
            }

            public static void main(String[] args) {
                HolderN<Apple> appleHolder = new HolderN<>(new Apple());
                Apple d = appleHolder.get();
                appleHolder.set(d);
                HolderN<? extends Fruit> fruit = (HolderN<? extends Fruit>) appleHolder;
                Fruit p = fruit.get();  //读取始终是安全的；
                d = (Apple) fruit.get();
                try {
                    Orange c= (Orange) fruit.get();
                } catch (Exception e) {
                    Print.print(e);
                }
                Print.print(fruit.equals(d));
            }
        }
        //逆变
            //超类型通配符：<? super T>
            //向一个泛型类型"写入"（传递给一个方法），从一个泛型类型中"读取"（从一个方法中返回）
            public static class GenericWriting {
                static <T> void writeExact(List<T> list, T item) {
                    list.add(item);
                }
                static List<Apple> apples = new ArrayList<>();
                static List<Fruit> fruits = new ArrayList<>();
                static void f1() {
                    writeExact(apples, new Apple());
                    writeExact(fruits, new Apple());
                }
                static <T> void writeWithWildcard(List<? super T> list, T item) {
                    list.add(item);
                }
                static void f2() {
                    writeWithWildcard(apples, new Apple());
                    writeWithWildcard(fruits, new Apple());
                }

                public static void main(String[] args) {
                    f1();
                    f2();
                }
            }

            public static class GenericReading {
                static <T> T readExact(List<T> list) {
                    return list.get(0);
                }
                static List<Apple> apples = Arrays.asList(new Apple());
                static List<Fruit> fruits = Arrays.asList(new Fruit());
                static void f1() {
                    Apple a = readExact(apples);
                    Fruit f = readExact(fruits);
                    f = readExact(apples);
                }
                static class Reader<T> {
                    T readExact(List<T> list) { return list.get(0); }
                }
                static void f2() {
                    Reader<Fruit> fruitReader = new Reader<>();
                    Fruit f = fruitReader.readExact(fruits);
//                    Fruit a = fruitReader.readExact(apples);
                }
                static class CovariantReader<T> {
                    T readCovaraint(List<? extends T> list) { return list.get(0); }
                }
                static void f3() {
                    CovariantReader<Fruit> fruitReader = new CovariantReader<>();
                    Fruit f = fruitReader.readCovaraint(fruits);
                    Fruit a = fruitReader.readCovaraint(apples);
                }

                public static void main(String[] args) {
                    f1();
                    f2();
                    f3();
                }
            }
        //无界通配符
            //<?>;
            public static class UnboundedWildcards1 {
                static List list1;
                static List<?> list2;
                static List<? extends Object> list3;
                static void assign1(List list) {
                    list1 = list;
                    list2 = list;
                    list3 = list;
                }
                static void assign2(List<?> list) {
                    list1 = list;
                    list2 = list;
                    list3 = list;
                }
                static void assign3(List<? extends Object> list) {
                    list1 = list;
                    list2 = list;
                    list3 = list;
                }

                public static void main(String[] args) {
                    assign1(new ArrayList());
                    assign2(new ArrayList());
                    assign3(new ArrayList());
                    assign1(new ArrayList<String>());
                    assign2(new ArrayList<String>());
                    assign3(new ArrayList<String>());
                    List<?> wildList = new ArrayList();
                    wildList = new ArrayList<String>();
                    assign1(wildList);
                    assign2(wildList);
                    assign3(wildList);
                }
            }

            public static class Wildcards {
                static void rawArgs(HolderN holder, Object arg) {
                    holder.set(arg);
                    holder.set(new Wildcards());
                    Object obj = holder.get();
                }
                static void unboundedArg(HolderN<?> holder, Object arg) {
//                    holder.set(arg);
//                    holder.set(new Wildcards());
                    Object obj = holder.get();
                }
                static <T> T exact1(HolderN<T> holder) {
                    T t = holder.get();
                    return t;
                }
                static <T> T exact2(HolderN<T> holder, T arg) {
                    holder.set(arg);
                    T t = holder.get();
                    return t;
                }
                static <T> T wildSubtype(HolderN<? extends T> holder, T arg) {
//                    holder.set(arg);
                    T t = holder.get();
                    return t;
                }
                static <T> void wildSupertype(HolderN<? super T> holder, T arg) {
                    holder.set(arg);
//                    T t = holder.get();
                    Object obj = holder.get();
                }

                public static void main(String[] args) {
                    HolderN raw = new HolderN<Long>();
                    raw = new HolderN();
                    HolderN<Long> qualified = new HolderN<Long>();
                    HolderN<?> unbounded = new HolderN<Long>();
                    HolderN<? extends Long> bounded = new HolderN<Long>();
                    Long lng = 1L;
                    rawArgs(raw, lng);
                    rawArgs(qualified, lng);
                    rawArgs(unbounded, lng);
                    rawArgs(bounded, lng);
                    unboundedArg(raw, lng);
                    unboundedArg(qualified, lng);
                    unboundedArg(unbounded, lng);
                    unboundedArg(bounded, lng);

                    Object r1 = exact1(raw);
                    Long r2 = exact1(qualified);
                    Object r3 = exact1(unbounded);
                    Long r4 = exact1(bounded);

                    Long r5 = exact2(raw, lng);
                    Long r6 = exact2(qualified, lng);
//                    Long r7 = exact2(unbounded, lng);
//                    Long r8 = exact2(bounded, lng);

                    Long r9 = wildSubtype(raw, lng);
                    Long r10 = wildSubtype(qualified, lng);
                    Object r11 = wildSubtype(unbounded, lng);
                    Long r12 = wildSubtype(bounded, lng);

                    wildSupertype(raw, lng);
                    wildSupertype(qualified, lng);
//                    wildSupertype(unbounded, lng);
//                    wildSupertype(bounded, lng);
                }
            }
            //从泛型参数中返回类型确定的返回值；(? extends T)
            //向泛型参数传递类型确定的参数；(? super T)
        //捕获转换
            public static class CaptureConversion {
                static <T> void f1(HolderN<T> holder) {
                    T t = holder.get();
                    Print.print(t.getClass().getSimpleName());
                }
                static void f2(HolderN<?> holder) {
                    f1(holder);
                }

                public static void main(String[] args) {
                    HolderN raw = new HolderN<Integer>(1);
                    f1(raw);
                    f2(raw);
                    HolderN rawBasic = new HolderN();
                    rawBasic.set(new Object());
                    f2(rawBasic);
                    HolderN<?> wildcarded = new HolderN<Double>(1.0);
                    f2(wildcarded);
                }
            }
            //参数类型在调用f2()的过程中被捕获，它可以在对f1()的调用中被使用；
            //捕获转换只有在这样的情况下可以工作：即在方法内部，你需要使用确切的类型；
    //15.11 问题
        //任何基本类型都不能作为类型参数
            //自动包装机制不能运用于数组；
            static class FArray {
                public static <T> T[] fill(T[] a, net.mindview.util.Generator<T> gen) {
                    for (int i = 0; i < a.length; i++) {
                        a[i] = gen.next();
                    }
                    return a;
                }
            }
            public static class PrimitiveGenericTest {
                public static void main(String[] args) {
                    String[] strings = FArray.fill(new String[7], new RandomGenerator.String(10));
                    for (String s : strings) {
                        Print.print(s);
                    }
                    Integer[] integers = FArray.fill(new Integer[7], new RandomGenerator.Integer());
                    for (int i : integers) {
                        Print.print(i);
                    }
//                    int[] b = FArray.fill(new int[7], new RandomGenerator.Integer());
                }
            }
        //实现参数化接口
            //一个类不能实现同一个泛型接口的两种变体，由于擦除的原因，这两个变体会成为相同的接口；
        //转型和警告
            public class ClassCasting {
                public void f(String[] args) throws Exception {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(args[0]));
//                    List<Widget> lw1 = List<Widget>.class.cast(in.readObject());
                    List<Widget> lw2 = List.class.cast(in.readObject());
                }
            }
        //重载
            //当被擦除的参数不能产生唯一的参数列表时，必须提供明显有区别的方法名；
        //基类劫持了接口
    //15.12 自限定的类型
        static class SelfBounded<T extends SelfBounded<T>> {
            T element;
            SelfBounded<T> set(T arg) {
                element = arg;
                return this;
            }
            T get() { return element; }
        };
        static class A extends SelfBounded<A> { }
        //古怪的循环泛型
            //我在创建一个新类，它继承自一个泛型类型，这个泛型类型接受我的类的名字作为其参数；
            //基类用导出类替代其参数；在所产生的类中将使用确切类型而不是基类型；
        //自限定
            //自限定将采取额外的步骤，强制泛型当作其自己的边界参数来使用；这保证类型参数必须与正在被定义的类相同；
            //自限定惯用法不是可强制执行的；自限定限制只能强制作用于继承关系；
            public static class SelfBoundingMethods {
                static <T extends SelfBounded<T>> T f(T arg) {
                    return arg.set(arg).get();
                }

                public static void main(String[] args) {
                    A a = f(new A());
                }
            }
        //参数协变
            //自限定类型的价值在于它们可以产生协变参数类型————方法参数类型会随子类而变化；
            //导出类方法应该能够返回比他覆盖的基类方法更具体的类型；
            interface SelfBoundSetter<T extends SelfBoundSetter<T>> {
                void set(T arg);
            }
            interface Setter extends SelfBoundSetter<Setter> { }
            public class SelfBoundingAndCovariantArguments {
                void testA(Setter s1, Setter s2, SelfBoundSetter sbs) {
                    s1.set(s2);
//                    s1.set(sbs);
                }
            }
            //如果不使用自限定，将重载参数类型；如果使用了自限定，只能获得某个方法的一个版本，它将接受确切的参数类型；
    //15.13 动态类型安全
        static class Pet { }
        static class Cat extends Pet { }
        static class Dog extends Pet { }
        public static class CheckedList {
            static void oldStyleMethod(List probablyDogs) {
                probablyDogs.add(new Cat());
            }

            public static void main(String[] args) {
                List<Dog> dogs1 = new ArrayList<>();
                oldStyleMethod(dogs1);
                List<Dog> dogs2 = Collections.checkedList(new ArrayList<>(), Dog.class);
                try {
                    oldStyleMethod(dogs2);
                } catch (Exception e) {
                    Print.print(e);
                }
                List<Pet> pets = Collections.checkedList(new ArrayList<>(), Pet.class);
                pets.add(new Dog());
                pets.add(new Cat());
            }
        }
    //15.14 异常
        //catch语句不能捕获泛型类型的异常，因为在编译期和运行时都必须知道异常的确切类型；
        //泛型类也不能直接或间接继承自Throwable；
        interface Processor<T, E extends Exception> {
            void process(List<T> resultCollector) throws E;
        }
        static class ProcessRunner<T, E extends Exception> extends ArrayList<Processor<T, E>> {
            List<T> processAll() throws E {
                List<T> resultCollector = new ArrayList<>();
                for (Processor<T, E> processor : this) {
                    processor.process(resultCollector);
                }
                return resultCollector;
            }
        }
        static class Failure1 extends Exception { }
        static class Processor1 implements Processor<String, Failure1> {
            static int count = 3;
            @Override
            public void process(List<String> resultCollector) throws Failure1 {
                if (count-- > 1) {
                    resultCollector.add("Hep!");
                } else {
                    resultCollector.add("Ho!");
                }
                if (count < 0) {
                    throw new Failure1();
                }
            }
        }
        static class Failure2 extends Exception { }
        static class Processor2 implements Processor<Integer, Failure2> {
            static int count = 2;
            @Override
            public void process(List<Integer> resultCollector) throws Failure2 {
                if (count-- == 0) {
                    resultCollector.add(47);
                } else {
                    resultCollector.add(11);
                }
                if (count < 0) {
                    throw new Failure2();
                }
            }
        }
        public static class ThrowGenericException {
            public static void main(String[] args) {
                ProcessRunner<String, Failure1> runner = new ProcessRunner<>();
                for (int i = 0; i < 3; i++) {
                    runner.add(new Processor1());
                }
                try {
                    Print.print(runner.processAll());
                } catch (Failure1 failure1) {
                    Print.print(failure1);
                }
                ProcessRunner<Integer, Failure2> runner2 = new ProcessRunner<>();
                for (int i = 0; i < 3; i++) {
                    runner2.add(new Processor2());
                }
                try {
                    Print.print(runner2.processAll());
                } catch (Failure2 failure2) {
                    Print.print(failure2);
                }
            }
        }
    //15.15 混型
        //C++中的混型
            //Mixins.cpp;
            //Java泛型类不能直接继承自一个泛型参数；
        //与接口混合
            //使用接口来产生泛型效果；
            //Mixins.java;
        //使用装饰器模式
            //装饰器模式使用分层对象来动态透明地向单个对象中添加责任；
            //装饰器指定包装在最初的对象周围的所有对象都具有相同的基本接口；
            //装饰器是通过使用组合和形式化结构（可装饰物/装饰器层次结构）来实现的，而混型是基于继承的；
            static class Basic {
                private String value;
                public void set(String val) { value = val; }
                public String get() { return value; }
            }
            static class Decorator extends Basic {
                protected Basic basic;
                public Decorator(Basic basic) {
                    this.basic = basic;
                }
                public void set(String val) { basic.set(val); }
                public String get() { return basic.get(); }
            }
            static class TimeStamped extends Decorator {
                private final long timeStamp;
                public TimeStamped(Basic basic) {
                    super(basic);
                    timeStamp = new Date().getTime();
                }
                public long getStamp() { return timeStamp; }
            }
            static class SerialNumbered extends Decorator {
                private static long counter = 1;
                private final long serialNumber = counter++;
                public SerialNumbered(Basic basic) { super(basic); }
                public long getSerialNumber() { return serialNumber; }
            }
            public static class Decoration {
                public static void main(String[] args) {
                    TimeStamped t = new TimeStamped(new Basic());
                    TimeStamped t2 = new TimeStamped(new SerialNumbered(new Basic()));
                    SerialNumbered s = new SerialNumbered(new Basic());
                    SerialNumbered s2 = new SerialNumbered(new TimeStamped(new Basic()));
                }
            }
            //对于装饰器来说，它只能有效地工作于装饰器中的最后一层；而混型方法则更自然一些；
            //装饰器只是对由混型提出的问题的一种局限的解决方案；
        //与动态代理混合
            //通过使用动态代理，所产生的类的动态类型将会是已经混入的组合类型；
            //由于动态代理的限制，每个被混入的类都必须是某个接口的实现；
            static class MixinProxy implements InvocationHandler {
                Map<String, Object> delegatesByMethod;
                public MixinProxy(TwoTuple<Object, Class<?>>... pairs) {
                    delegatesByMethod = new HashMap<>();
                    for (TwoTuple<Object, Class<?>> pair : pairs) {
                        for (Method method : pair.second.getMethods()) {
                            String methodName = method.getName();
                            if (!delegatesByMethod.containsKey(methodName)) {
                                delegatesByMethod.put(methodName, pair.first);
                            }
                        }
                    }
                }
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    String methodName = method.getName();
                    Object delegate = delegatesByMethod.get(methodName);
                    return method.invoke(delegate, args);
                }
                public static Object newInstance(TwoTuple... pairs) {
                    Class[] interfaces = new Class[pairs.length];
                    for (int i = 0; i < pairs.length; i++) {
                        interfaces[i] = (Class) pairs[i].second;
                    }
                    ClassLoader cl = pairs[0].first.getClass().getClassLoader();
                    return Proxy.newProxyInstance(cl, interfaces, new MixinProxy(pairs));
                }
            }
            public static class DynamicProxyMixin {
                public static void main(String[] args) {
                    Object mixin = MixinProxy.newInstance(
                            Tuple.tuple(new Mixins.BasicImp(), ThinkingInJava.zAssistant.generics.Mixins.Basic.class),
                            Tuple.tuple(new Mixins.TimeStampedImp(), ThinkingInJava.zAssistant.generics.Mixins.TimeStamped.class),
                            Tuple.tuple(new Mixins.SerialNumberedImp(), ThinkingInJava.zAssistant.generics.Mixins.SerialNumbered.class)
                    );
                    ThinkingInJava.zAssistant.generics.Mixins.Basic b = (ThinkingInJava.zAssistant.generics.Mixins.Basic) mixin;
                    ThinkingInJava.zAssistant.generics.Mixins.TimeStamped t = (ThinkingInJava.zAssistant.generics.Mixins.TimeStamped) mixin;
                    ThinkingInJava.zAssistant.generics.Mixins.SerialNumbered s = (ThinkingInJava.zAssistant.generics.Mixins.SerialNumbered) mixin;
                    b.set("Hello");
                    Print.print(b.get());
                    Print.print(t.getStamp());
                    Print.print(s.getSerialNumber());
                }
            }
    //15.16 潜在类型机制
        //潜在类型机制是一种代码组织和复用机制；
        //两种支持潜在类型机制的语言实例是Python和C++；
        //Python是动态类型语言，所有类型检查都发生在运行时；C++是静态类型语言，类型检查发生在编译期；潜在类型机制不要求静态或动态类型检查；
        //见P419；
    //15.17 对缺乏潜在类型机制的补偿
        //反射
        static class Mime {
            public void walkgainstTheWind() { }
            public void sit() { Print.print("Pretending to sit"); }
            public void pushInvisibleWalls() { }
            public String toString() { return "Mime"; }
        }
        static class SmartDog {
            public void speak() { Print.print("Woof!"); }
            public void sit() { Print.print("Sitting"); }
            public void reproduce() { }
        }
        static class CommunicateReflectively {
            public static void perform(Object speaker) {
                Class<?> spkr = speaker.getClass();
                try {
                    try {
                        Method speak = spkr.getMethod("speak");
                        speak.invoke(speaker);
                    } catch (NoSuchMethodException e) {
                        Print.print(speaker + "cannot speak");
                    }
                    try {
                        Method sit = spkr.getMethod("sit");
                        sit.invoke(speaker);
                    } catch (NoSuchMethodException e) {
                        Print.print(speaker + "cannot sit");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(speaker.toString(), e);
                }
            }
        }
        public static class LatentReflection {
            public static void main(String[] args) {
                CommunicateReflectively.perform(new SmartDog());
                CommunicateReflectively.perform(new Mime());
            }
        }
        //将一个方法应用于序列
            //Apply.java;
        //当你并未碰巧拥有正确的接口时
            //见P423；没有预见到对"Addable"接口的需要；
        //用适配器仿真潜在类型机制
            //从我们拥有的接口中编写代码来产生我们需要的接口，这是【适配器】模式的一个典型示例；
    //15.18 将函数对象用作策略
        //函数对象的价值在于，与普通方法不同，他们可以传递出去，并且还可以拥有在多个调用之间持久化的状态；
        //Functional.java;
    //15.19 总结：转型真的如此之糟吗？
}
