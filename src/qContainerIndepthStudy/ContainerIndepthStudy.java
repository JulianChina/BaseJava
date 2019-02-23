package qContainerIndepthStudy;

import net.mindview.util.*;
import zUtils.Print;

import java.util.*;

public class ContainerIndepthStudy {
    //17.1 完整的容器分类法
        //见P459图；
        //Java SE5新增：
            //Queue\PriorityQueue\BlockingQueue;
            //ConcurrentMap\ConcurrentHashMap;
            //CopyOnWriteArrayList\CopyOnWriteArraySet;
            //EnumSet\EnumMap;
            //在Collections类中的多个便利方法；
    //17.2 填充容器
        static class StringAddress {
           private String s;
           public StringAddress(String s) { this.s = s; }
           public String toString() { return super.toString() + " " + s; }
        }
        public static class FillingLists {
            public static void main(String[] args) {
                List<StringAddress> list = new ArrayList<>(Collections.nCopies(4, new StringAddress("Hello")));
                Print.print(list);
                Collections.fill(list, new StringAddress("World"));  //只能替换已经存在在List中的元素，而不能添加新的元素；
                Print.print(list);
            }
        }
        //一种Generator解决方案
            //所有的Collection子类型都有一个接收另一个Collection对象的构造器，用所接收的Collection对象中的元素来填充新的容器；
            //addAll()方法是所有Collection子类型的一部分；
            //CollectionData是【适配器】设计模式的一个实例，它将Generator适配到Collection的构造器上；
            public static class CollectionDataGeneration {
                public static void main(String[] args) {
                    Print.print(new ArrayList<>(CollectionData.list(new RandomGenerator.String(9), 10)));
                    Print.print(new HashSet<>(new CollectionData<>(new RandomGenerator.Integer(), 10)));
                }
            }
        //Map生成器
            //泛型便利方法可以减少在创建MapData类时所必须的类型检查数量；
            static class Letters implements Generator<Pair<Integer, String>>, Iterable<Integer> {
                private int size = 9;
                private int number = 1;
                private char letter = 'A';
                @Override
                public Pair<Integer, String> next() {
                    return new Pair<>(number++, "" + letter++);
                }
                @Override
                public Iterator<Integer> iterator() {
                    return new Iterator<Integer>() {
                        @Override
                        public boolean hasNext() { return number<size; }
                        @Override
                        public Integer next() { return number++; }
                        @Override
                        public void remove() { throw new UnsupportedOperationException(); }
                    };
                }
            }
            public static class MapDataTest {
                public static void main(String[] args) {
                    Print.print(MapData.map(new Letters(), 11));
                    Print.print(MapData.map(new CountingGenerator.Character(), new RandomGenerator.String(3), 8));
                    Print.print(MapData.map(new CountingGenerator.Character(), "Value", 6));
                    Print.print(MapData.map(new Letters(), new RandomGenerator.String(3)));
                    Print.print(MapData.map(new Letters(), "Pop"));
                }
            }
            //可以使用工具来创建任何用于Map或Collection的生成数据集，然后通过构造器或Map.putAll()和Collection.addAll()方法来初始化Map和Collection；
        //使用Abstract类
            //对于产生用于容器的测试数据问题，另一种解决方式是创建定制的Collection和Map实现，即继承相应容器的Abstract类；
            //【享元模式】使得对象的一部分可以被具体化；
            //net.mindview.util.Countries.class; net.mindview.util.CountingIntegerList.class; net.mindview.util.CountingMapData.class;
    //17.3 Collection的功能方法
        //见P470表；
        public static class CollectionMethods {
            public static void main(String[] args) {
                Collection<String> c = new ArrayList<>();
                c.addAll(Countries.names(6));
                c.add("ten");
                c.add("eleven");
                Print.print(c);
                Object[] array = c.toArray();
                String[] str = c.toArray(new String[0]);
                Print.print("Collections.max(c) = " + Collections.max(c));
                Print.print("Collections.min(c) = " + Collections.min(c));
                Collection<String> c2 = new ArrayList<>();
                c2.addAll(Countries.names(6));
                c.addAll(c2);
                Print.print(c);
                c.remove(Countries.DATA[0][0]);
                Print.print(c);
                c.remove(Countries.DATA[1][0]);
                Print.print(c);
                c.removeAll(c2);
                Print.print(c);
                c.addAll(c2);
                Print.print(c);
                String val = Countries.DATA[3][0];
                Print.format("c.contains(%s) = %b\n", val, c.contains(val));
                Print.format("c.containsAll(c2) = %b\n", c.containsAll(c2));
                Collection<String> c3 = ((ArrayList<String>) c).subList(3, 5);
                c2.retainAll(c3);
                Print.print(c2);
                c2.removeAll(c3);
                Print.format("c2.isEmpty() = %b\n", c2.isEmpty());
                c = new ArrayList<>();
                c.addAll(Countries.names(6));
                Print.print(c);
                c.clear();
                Print.print("after c.clear():" + c);
            }
        }
    //17.4 可选操作
        //执行各种不同的添加和移除的方法在Collection接口中都是可选操作；
        //动态语言可以在任何对象上调用任何方法，并且可以在运行时发现某个特定调用是否可以工作；
        //1.UnSupportedOperationException必须是一种罕见事件；
        //2.如果一个操作是未获支持的，那么在实现接口的时候可能就会导致UnsupportedOperationException异常，而不是将产品程序交给客户以后才出现此异常；
        //未获支持的操作
        public static class Unsupported {
            static void test(String msg, List<String> list) {
                Print.print("----- " + msg + " -----");
                Collection<String> c = list;
                Collection<String> subList = list.subList(1, 8);
                Collection<String> c2 = new ArrayList<>(subList);
                try { c.retainAll(c2); } catch (Exception e) { Print.print("retainAll():" + e); }
                try { c.removeAll(c2); } catch (Exception e) { Print.print("removeAll():" + e); }
                try { c.clear(); } catch (Exception e) { Print.print("clear():" + e); }
                try { c.add("X"); } catch (Exception e) { Print.print("add():" + e); }
                try { c.addAll(c2); } catch (Exception e) { Print.print("addAll():" + e); }
                try { c.remove("C"); } catch (Exception e) { Print.print("remove():" + e); }
                try { list.set(0, "X"); } catch (Exception e) { Print.print("List.set():" + e); }
            }
            public static void main(String[] args) {
                List<String> list = Arrays.asList("A B C D E F G H I J K L".split(" "));
                test("Modifiable copy", new ArrayList<>(list));
                test("Arrays.asList()", list);
                test("unmodifiableList()", Collections.unmodifiableList(new ArrayList<>(list)));
            }
        }
    //17.5 List的功能方法
        //Lists.java;
    //17.6 Set和存储顺序
        //Set需要一种方式来维护存储顺序；
        //见P477表：
            //Set[equals()]
                //HashSet[hashCode()]\TreeSet[Comparable]\LinkedHashSet[hashCode()];
        //应该在覆盖equals()方法时，总是同时覆盖hashCode()方法；
        //TypesForSets.java;
            //对于没有重新定义hashCode()方法的SetType或TreeType，如果将他们放置到任何散列实现中都会产生重复值；
            //如果尝试着在TreeSet中使用没有实现Comparable的类型，将抛出一个异常；
        //SortedSet
            public static class SortedSetDemo {
                public static void main(String[] args) {
                    SortedSet<String> sortedSet = new TreeSet<>();
                    Collections.addAll(sortedSet, "one two three four five six seven eight".split(" "));
                    Print.print(sortedSet);
                    String low = sortedSet.first();
                    String high = sortedSet.last();
                    Print.print(low);
                    Print.print(high);
                    Iterator<String> it = sortedSet.iterator();
                    for (int i = 0; i <= 6; i++) {
                        if (i == 3) { low = it.next(); }
                        else if (i == 6) { high = it.next(); }
                        else { it.next(); }
                    }
                    Print.print(low);
                    Print.print(high);
                    Print.print(sortedSet.subSet(low, high));
                    Print.print(sortedSet.headSet(high));  //不包含边界
                    Print.print(sortedSet.tailSet(low));   //包含边界
                }
            }
    //17.7 队列
        //除了并发应用，Queue在JavaSE5中仅有的两个实现是LinkedList和PriorityQueue，他们的差异在于排序行为而不是性能；
        //QueueBehavior.java;
        //优先级队列
            //ToDoList.java;
        //双向队列
            //net.mindview.util.Deque;
    //17.8 理解Map
        //性能
            //见P485表：
                //HashMap\LinkedHashMap\TreeMap\WeakHashMap\ConcurrentHashMap\IdentityHashMap;
            //对Map中使用的键的要求与对Set中的元素的要求一样；
            //Maps.java;
        //SortedMap
            public static class SortedMapDemo {
                public static void main(String[] args) {
                    TreeMap<Integer, String> sortedMap = new TreeMap<>(new CountingMapData(10));
                    Print.print(sortedMap);
                    Integer low = sortedMap.firstKey();
                    Integer high = sortedMap.lastKey();
                    Print.print(low);
                    Print.print(high);
                    Iterator<Integer> it = sortedMap.keySet().iterator();
                    for (int i = 0; i <= 6; i++) {
                        if (i == 3) { low = it.next(); }
                        else if (i == 6) { high = it.next(); }
                        else { it.next(); }
                    }
                    Print.print(low);
                    Print.print(high);
                    Print.print(sortedMap.subMap(low, high));
                    Print.print(sortedMap.headMap(high));  //不包含边界
                    Print.print(sortedMap.tailMap(low));   //包含边界
                }
            }
        //LinkedHashMap
            public static class LinkedHashMapDemo {
                public static void main(String[] args) {
                    LinkedHashMap<Integer, String> linkedMap = new LinkedHashMap<>(new CountingMapData(9));
                    Print.print(linkedMap);
                    linkedMap = new LinkedHashMap<>(16, 0.75f, true);  //LRU算法；
                    linkedMap.putAll(new CountingMapData(9));
                    Print.print(linkedMap);
                    for (int i = 0; i < 6; i++) {
                        linkedMap.get(i);
                    }
                    Print.print(linkedMap);
                    linkedMap.get(0);
                    Print.print(linkedMap);
                }
            }
    //17.9 散列与散列码
        //正确的equals()方法必须满足下列5个条件：自反性、对称性、传递性、一致性、对任何不是null的x，x.equals(null)一定返回false；
        //理解hashCode()
        //为速度而散列
        //覆盖hashCode()
            //无论何时，对同一个对象调用hashCode()都应该生成同样的值；
            //散列码不是独一无二的（应该更关注生成速度，而不是唯一性），但是通过hashCode()和equals()，必须能够完全确定对象的身份；
            //好的hashCode()应该产生分布均匀的散列码；见P496表；
            //为新类编写正确的hashCode()和equals()是很需要技巧的；
    //17.10 选择接口的不同实现
        //容器之间的区别通常归结为由什么在背后"支持"它们；
        //性能测试框架
            //Test.java; TestParam.java; Tester.java;
        //对List的选择
            //ListPerformance.java;
        //微基准测试的危险
            //必须仔细分析试验，并理解它们的局限性；
        //对Set的选择
            //SetPerformance.java;
        //对Map的选择
            //MapPerformance.java;
            //HashMap的性能因子：容量、初始容量、尺寸、负载因子；再散列；HashMap使用的默认负载因子是0.75；
    //17.11 实用方法
        //见P512表：java.util.Collections;
        //Utilities.java;
        //List的排序和查询
            //Collections.sort(list);
            //Collections.binarySearch(list,key);
        //设定Collection或者Map为不可修改的元素
            //Collections.unmodifiableCollection();
            //Collections.unmodifiableList();
            //Collections.unmodifiableSet();
            //Collections.unmodifiableSortedSet();
            //Collections.unmodifiableMap();
            //Collections.unmodifiableSortedMap();
        //Collection或Map的同步控制
            public static class Synchronization {
                public static void main(String[] args) {
                    Collection<String> c = Collections.synchronizedCollection(new ArrayList<>());
                    Set<String> s = Collections.synchronizedSet(new HashSet<>());
                    Set<String> ss = Collections.synchronizedSortedSet(new TreeSet<>());
                    Map<String, String> m = Collections.synchronizedMap(new HashMap<>());
                    Map<String, String> mm = Collections.synchronizedSortedMap(new TreeMap<>());
                }
            }
            //快速报错：ConcurrentModificationException;
    //17.12 持有引用
        //java.lang.ref.Reference:
            //SoftReference：实现内存敏感的高速缓存；
            //WeakReference：为实现"规范映射"而设计的，不妨碍垃圾回收器回收映射的"键"或"值"；"规范映射"中对象的实例可以在程序的多处被同时使用，以节省存储空间；
            //PhantomReference：用以调度回收前的清理工作，比Java终止机制更灵活；
        //使用SoftReference和WeakReference时，可以选择是否要将它们放入ReferenceQueue，而PhantomReference只能依赖于ReferenceQueue；
        //References.java;
        //WeakHashMap
            //被用来保存WeakReference，它使得规范映射更易于使用，在这种映射中，每个值只保存一份实例以节省存储空间；
            //CanonicalMapping.java;
    //17.13 Java1.0/1.1容器
        //Vector和Enumeration
        //Hashtable
            //没有理由再使用Hashtable而不使用HashMap；
        //Stack
            //如果需要栈的行为，应该使用LinkedList；
        //BitSet
            //EnumSet;
    //17.14 总结
}
