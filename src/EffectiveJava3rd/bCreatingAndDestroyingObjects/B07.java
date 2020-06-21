package EffectiveJava3rd.bCreatingAndDestroyingObjects;

import java.util.Arrays;
import java.util.EmptyStackException;

//07 消除过期的对象引用
public class B07 {
    public class Stack {
        private Object[] elements;
        private int size = 0;
        private static final int DEFAULT_INITIAL_CAPACITY = 16;

        public Stack() {
            elements = new Object[DEFAULT_INITIAL_CAPACITY];
        }

        public void push(Object e) {
            ensureCapacity();
            elements[size++] = e;
        }

        public Object pop() {
            if (size == 0) {
                throw new EmptyStackException();
            }
            return elements[--size];
        }

        private void ensureCapacity() {
            if (elements.length == size) {
                elements = Arrays.copyOf(elements, 2 * size + 1);  //扩展数组长度
            }
        }

        public Object rightWayPop() {
            if (size == 0) {
                throw new EmptyStackException();
            }
            Object result = elements[--size];
            elements[size] = null;
            return result;
        }
    }

    //如果一个栈增长后收缩，那么从栈弹出的对象不会被垃圾收集，即使使用栈的程序不再引用这些对象。这是因为栈维护对这些对象的过期引用(obsolete references)。
    //过期引用简单来说就是永远不会解除的引用。在这种情况下，元素数组"活动部分(active portion)"之外的任何引用都是过期的。活动部分是由索引下标小于size的元素组成。
    //垃圾收集语言中的内存泄漏(更适当地称为无意的对象保留unintentional object retentions)是隐蔽的。如果无意中保留了对象引用，那么不仅这个对象排除在垃圾回收之外，而且该对象引用的任何对象也是如此。
    //这类问题的解决方法很简单: 一旦对象引用过期，将它们设置为null。
    //清空对象引用应该是例外而不是规范。
    //消除过期引用的最好方法是让包含引用的变量超出范围。如果在最近的作用域范围内定义每个变量，这种自然就会出现这种情况。
    //一般来说，当一个类自己管理内存时，程序员应该警惕内存泄漏问题。每当一个元素被释放时，元素中包含的任何对象引用都应该被清除。

    //另一个常见的内存泄漏来源是缓存。
    //只要在缓存之外存在对某个项(entry)的键(key)引用，那么这项就是明确有关联的，就可以用WeakHashMap来表示缓存。
    //只有当缓存中某个项的生命周期是由外部引用到键(key)而不是值(value)决定时，WeakHashMap才有用。
    //可以通过一个后台线程(也许是ScheduledThreadPoolExecutor)或将新的项添加到缓存时顺便清理。LinkedHashMap类使用它的removeEldestEntry方法实现了后一种方案。对于更复杂的缓存，可能直接需要使用java.lang.ref。

    //第三个常见的内存泄漏来源是监听器和其他回调。
    //如果你实现了一个API，其客户端注册回调，但是没有显式地撤销注册回调，除非采取一些操作，否则它们将会累积。确保回调被垃圾收集的一种方法是只存储弱引用(weak references)。

}
