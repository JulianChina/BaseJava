package EffectiveJava3rd.cMethodsCommonToAllObjects;

//13 谨慎地重写clone方法
public class C13 {
    //如果一个类实现了Cloneable接口，那么Object的clone方法将返回该对象的逐个属性(field-by-filed)拷贝；否则会抛出CloneNotSupportedException异常。
    //对于Cloneable接口，它会修改父类上受保护方法的行为。

    //返回的对象应该独立于被克隆的对象。为了实现这种独立性，在返回对象之前，可能需要修改由super.clone返回的对象的一个或多个属性。
    //不可变类永远不应该提供clone方法，因为这只会浪费复制。

    //在数组上调用clone会返回一个数组，其运行时和编译时类型与被克隆的数组相同。这是复制数组的首选习语。数组是clone机制的唯一有力的用途。

    // Recursive clone method for class with complex mutable state
    public static class HashTable implements Cloneable {
        private Entry[] buckets = null;

        private static class Entry {
            final Object key;
            Object value;
            Entry next;

            Entry(Object key, Object value, Entry next) {
                this.key = key;
                this.value = value;
                this.next = next;
            }

            // Recursively copy the linked list headed by this Entry
            Entry deepCopy() {
                return new Entry(key, value, next == null ? null : next.deepCopy());
            }

            // Iteratively copy the linked list headed by this Entry
            Entry deepCopy(String iterative) {
                Entry result = new Entry(key, value, next);
                for (Entry p = result; p.next != null; p = p.next) {
                    p.next = new Entry(p.next.key, p.next.value, p.next.next);
                }
                return result;
            }
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            try {
                HashTable result = (HashTable) super.clone();
                result.buckets = new Entry[buckets.length];
                for (int i = 0; i < buckets.length; i++) {
                    if (buckets[i] != null) {
                        result.buckets[i] = buckets[i].deepCopy();
                    }
                }
                return result;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }

    //克隆复杂可变对象的最后一种方法是调用super.clone，将结果对象中的所有属性设置为其初始状态，然后调用更高级别的方法来重新生成原始对象的状态。
    //clone方法绝对不可以在构建过程中，调用一个可以重写的方法。
    //如果clone方法调用一个在子类中重写的方法，则在子类有机会在克隆中修复它的状态之前执行该方法，很可能导致克隆和原始对象的损坏。
    //Object类的clone方法被声明为抛出CloneNotSupportedException异常，但重写方法时不需要。

    //在为继承设计一个类时，通常有两种选择，但无论选择哪一种，都不应该实现Cloneable接口。
    //如果你编写一个实现了Cloneable的线程安全的类，记得它的clone方法必须和其他方法一样需要正确的同步。

    //实现Cloneable的所有类应该重写公共clone方法，而这个方法的返回类型是类本身。这个方法应该首先调用super.clone，然后修复任何需要修复的属性。
    //如果你继承一个已经实现了Cloneable接口的类，你别无选择，只能实现一个行为良好的clone方法。
    //对象复制更好的方法是提供一个复制构造方法或复制工厂。
    //复制构造方法接受参数，其类型为包含此构造方法的类：
    // Copy constructor
    public C13(C13 c13) {

    }

    //复制工厂类似于复制构造方法的静态工厂：
    // Copy factory
    public static C13 newInstance(C13 c13) {
        return c13;
    }

    //复制构造方法或复制工厂可以接受类型为该类实现的接口的参数。
    //基于接口的复制构造方法和复制工厂(更适当地称为转换构造方法和转换工厂)允许客户端选择复制的实现类型，而不是强制客户端接受原始实现类型。
    //假设你有一个HashSet，并且你想把它复制为一个TreeSet。 clone方法不能提供这种功能，但使用转换构造方法很容易: new TreeSet<>(s)。
    //复制功能最好由构造方法或工厂提供。这个规则的一个明显的例外是数组，它最好用clone方法复制。
}
