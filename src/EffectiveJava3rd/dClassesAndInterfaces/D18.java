package EffectiveJava3rd.dClassesAndInterfaces;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

//18 组合优于继承
public class D18 {
    //与方法调用不同，继承打破了封装。从普通的具体类跨越包级边界继承，是危险的。
    //不要继承一个现有的类，而应该给你的新类增加一个私有属性，该属性是现有类的实例引用，这种设计被称为组合(composition)，因为现有的类成为新类的组成部分。
    //新类中的每个实例方法调用现有类的包含实例上的相应方法并返回结果。这被称为转发(forwarding)，而新类中的方法被称为转发方法。
    public static class ForwardingSet<E> implements Set<E> {
        private final Set<E> s;

        public ForwardingSet(Set<E> s) {
            this.s = s;
        }


        @Override
        public void clear() {
            s.clear();
        }

        @Override
        public boolean contains(Object o) {
            return s.contains(o);
        }

        @Override
        public boolean isEmpty() {
            return s.isEmpty();
        }

        @Override
        public int size() {
            return s.size();
        }

        @Override
        public Iterator<E> iterator() {
            return s.iterator();
        }

        @Override
        public boolean add(E e) {
            return s.add(e);
        }

        @Override
        public boolean remove(Object o) {
            return s.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return s.contains(c);
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            return s.addAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return s.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return s.retainAll(c);
        }

        @Override
        public Object[] toArray() {
            return s.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return s.toArray(a);
        }

        @Override
        public boolean equals(Object obj) {
            return s.equals(obj);
        }

        @Override
        public int hashCode() {
            return s.hashCode();
        }

        @Override
        public String toString() {
            return s.toString();
        }
    }

    // Wrapper class - uses composition in place of inheritance
    public static class InstrumentedSet<E> extends ForwardingSet<E> {
        private int addCount = 0;

        public InstrumentedSet(Set<E> s) {
            super(s);
        }

        @Override
        public boolean add(E e) {
            addCount++;
            return super.add(e);
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            addCount += c.size();
            return super.addAll(c);
        }

        public int getAddCount() {
            return addCount;
        }
    }

    //包装类不适合在回调框架(callback frameworks)中使用，其中对象将自我引用传递给其他对象以用于后续调用("回调")。
}
