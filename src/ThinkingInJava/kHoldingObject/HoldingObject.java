package ThinkingInJava.kHoldingObject;

import ThinkingInJava.zUtils.Print;

import java.util.*;

public class HoldingObject {
    //11.1 泛型和类型安全的容器
    //11.2 基本概念
        //Collection(List、Set、Queue): 序列；
        //Map: 字典；
    //11.3 添加一组元素
        //Arrays;
            //Arrays.asList(): 底层表示是数组，不能修改该List的尺寸；
        //Collections;
            //Collections.addAll(): 首选方式，更灵活；
        static class AddingGroups {
            public static void main(String[] args) {
                Collection<Integer> collection = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
                Integer[] moreInts = {6, 7, 8, 9, 10};
                collection.addAll(Arrays.asList(moreInts));
                Collections.addAll(collection, 11, 12, 13, 14, 15);
                Collections.addAll(collection, moreInts);
                Print.print(Arrays.toString(collection.toArray()));
                List<Integer> list = Arrays.asList(16, 17, 18, 19, 20);
                list.set(1, 99);
                Print.print(Arrays.toString(list.toArray()));
//                list.add(21);
            }
        }

        static class Snow {}
        static class Powder extends Snow {}
        static class Light extends Powder {}
        static class Heavy extends Powder {}
        static class Crusty extends Snow {}
        static class Slush extends Snow {}
        static class AsListInference {
            public static void main(String[] args) {
                List<Snow> snow1 = Arrays.asList(new Crusty(), new Slush(), new Powder());
                List<Snow> snow2 = Arrays.asList(new Light(), new Heavy());
                List<Snow> snow3 = new ArrayList<Snow>();
                Collections.addAll(snow3, new Light(), new Heavy());
                List<Snow> snow4 = Arrays.<Snow>asList(new Light(), new Heavy());  //显示类型参数说明

            }
        }
    //11.4 容器的打印
        //你必须使用Arrays.toString()来产生数组的可打印表示，但是打印容器无需任何帮助。
    //11.5 List
        //ArrayList;
        //LinkedList;
        //List有一个重载的addAll()方法，使得我们可以在初始List的中间插入新的列表。
    //11.6 迭代器
        //迭代器方法，迭代器通常被称为轻量级对象；
        //ListIterator
    //11.7 LinkedList
        //getFirst()\element()\peek();remove()\removeFirst()\poll();addFirst()\offer();add()\addLast();removeLast();
    //11.8 Stack
        //LinkedList具有能够直接实现栈的所有功能的方法；可以将LinkedList当做栈来使用。
    //11.9 Set
        //实际上Set就是Collection，只是行为不同。
        //HashSet、LinkedHashSet、TreeSet;
        //TextFile;
    //11.10 Map
        //keySet();values();entrySet();
        public static class Statistics {
            public static void main(String[] args) {
                Random rand = new Random(47);
                Map<Integer, Integer> map = new HashMap<>();
                for (int i = 0; i < 10000; ++i) {
                    int r = rand.nextInt(20);
                    Integer freq = map.get(r);
                    map.put(r, freq == null ? 1 : freq + 1);
                }
                Print.print(map);
            }
        }
    //11.11 Queue
        //LinkedList实现了Queue接口；
        //PriorityQueue
            //传入自定义Comparator；
    //11.12 Collection和Iterator
        public static class CollectionSequence extends AbstractCollection<String> {
            private String[] pets = "Look, there is a cat. So cute, hou~".split(" ");
            public int size() { return pets.length; }
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private int index = 0;
                    @Override
                    public boolean hasNext() {
                        return index<pets.length;
                    }

                    @Override
                    public String next() {
                        return pets[index++];
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
            public static void main(String[] args) {
                CollectionSequence cs = new CollectionSequence();
            }
        }

        static class StringSequence {
            protected String[] pets = "Look, there is a cat. So cute, hou~".split(" ");

        }
        public static class NonCollectionSequence extends StringSequence {
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private int index = 0;
                    @Override
                    public boolean hasNext() {
                        return index<pets.length;
                    }

                    @Override
                    public String next() {
                        return pets[index++];
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
            public static void main(String[] args) {
                NonCollectionSequence cs = new NonCollectionSequence();
                cs.iterator();
            }
        }
        //生成Iterator是将队列与消费队列的方法连在一起耦合度最小的方式，并且与实现Collection相比，它在序列类上所施加的约束也少得多。
    //11.13 Foreach与迭代器
        //如果你创建了任何实现Iterable接口的类，都可以将他用于foreach语句中；
        //Iterable接口包含一个能够产生Iterator的iterator()方法；
        public static class EnvironmentVariables {
            public static void main(String[] args) {
                for (Map.Entry entry : System.getenv().entrySet()) {
                    Print.print("Env key->value " + entry.getKey() + ":" + entry.getValue());
                }
            }
        }
        //适配器方法惯用法
            //【适配器方法】：当你有一个接口并需要另外一个接口时，编写适配器就可以解决问题。
            static class ReversibleArrayList<T> extends ArrayList<T> {
                public ReversibleArrayList(Collection<T> c) {
                    super(c);
                }
                public Iterable<T> reversed() {
                    return new Iterable<T>() {
                        @Override
                        public Iterator<T> iterator() {
                            return new Iterator<T>() {
                                int current = size()-1;
                                @Override
                                public boolean hasNext() {
                                    return current>-1;
                                }

                                @Override
                                public T next() {
                                    return get(current--);
                                }

                                @Override
                                public void remove() {
                                    throw new UnsupportedOperationException();
                                }
                            };
                        }
                    };
                }
            }
            public static class AdapterMethodIdiom {
                public static void main(String[] args) {
                    ReversibleArrayList<String> ral = new ReversibleArrayList<>(Arrays.asList("To be or not to be".split(" ")));
                    for (String s : ral) {
                        Print.printnb(s + " ");
                    }
                    Print.print();
                    for (String s : ral.reversed()) {
                        Print.printnb(s + " ");
                    }
                }
            }

            public static class MultiIterableClass implements Iterable<String> {
                protected String[] words = "And that is how we know the Earth to be banana-shaped.".split(" ");
                @Override
                public Iterator<String> iterator() {
                    return new Iterator<String>() {
                        private int index = 0;
                        @Override
                        public boolean hasNext() {
                            return index<words.length;
                        }

                        @Override
                        public String next() {
                            return words[index++];
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                }

                public Iterable<String> reversed() {
                    return new Iterable<String>() {
                        @Override
                        public Iterator<String> iterator() {
                            return new Iterator<String>() {
                                private int current = words.length-1;
                                @Override
                                public boolean hasNext() {
                                    return current>-1;
                                }

                                @Override
                                public String next() {
                                    return words[current--];
                                }

                                @Override
                                public void remove() {
                                    throw new UnsupportedOperationException();
                                }
                            };
                        }
                    };
                }

                public Iterable<String> randomized() {
                    return new Iterable<String>() {
                        @Override
                        public Iterator<String> iterator() {
                            List<String> shuffled = new ArrayList<String>(Arrays.asList(words));
                            Collections.shuffle(shuffled, new Random(47));
                            return shuffled.iterator();
                        }
                    };
                }

                public static void main(String[] args) {
                    MultiIterableClass mic = new MultiIterableClass();
                    for (String s : mic) {
                        Print.print(s);
                    }
                    Print.print();
                    for (String s : mic.reversed()) {
                        Print.print(s);
                    }
                    Print.print();
                    for (String s : mic.randomized()) {
                        Print.print(s);
                    }
                }
            }

            public static class ModifyingArraysAsList {
                public static void main(String[] args) {
                    Random rand = new Random(47);
                    Integer[] ia = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
                    List<Integer> list1 = new ArrayList<Integer>(Arrays.asList(ia));
                    Print.print("Before shuffling: " + list1);
                    Collections.shuffle(list1, rand);
                    Print.print("After shuffling: " + list1);
                    Print.print("array: " + Arrays.toString(ia));

                    List<Integer> list2 = Arrays.asList(ia);
                    Print.print("Before shuffling: " + list2);
                    Collections.shuffle(list2, rand);
                    Print.print("After shuffling: " + list2);
                    Print.print("array: " + Arrays.toString(ia));
                }
            }
            //意识到Arrays.asList()产生的List对象会使用底层数组作为其物理实现是很重要的。
    //11.14 总结
        //1)数组将数字与对象联系起来；
        //2)Collection保存单一的元素，而Map保存相关联的键值对；
        //3)像数组一样，List也建立数字索引与对象的关联，数组和List都是排好序的容器，List能够自动扩充容量；
        //4)如果要进行大量的随机访问，就使用ArrayList；如果要经常从表中间插入或删除元素，则应该使用LinkedList；
        //5)各种Queue以及栈的行为，由LinkedList提供支持；
        //6)Map是一种将对象（而非数字）与对象相关联的设计；HashMap设计用来快速访问，TreeMap保持"键"始终处于排序状态，LinkedHashMap保持元素插入的顺序，但也通过散列提供了快速访问的能力；
        //7)Set不接受重复的元素；HashSet提供最快的查询速度，TreeSet保持元素处于排序状态，LinkedHashSet以插入顺序保存元素；
        //8)新程序中不应该使用过时的Vector、Hashtable、Stack。
        //简单的容器分类(见P246图片)
        //各种不同的集合类在方法上的差异(见P246-247Code)
}
