package EffectiveJava3rd.eGenerics;

import java.util.Arrays;
import java.util.EmptyStackException;

//29 优先考虑泛型
public class E29 {
    // Object-based collection - a prime candidate for generics
    public static class Stack {
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
            if (size == 0)
                throw new EmptyStackException();
            Object result = elements[--size];
            elements[size] = null; // Eliminate obsolete reference
            return result;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        private void ensureCapacity() {
            if (elements.length == size)
                elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }

    // Initial attempt to generify Stack - won't compile!
    public static class Stack2<E> {
        private E[] elements;
        private int size = 0;
        private static final int DEFAULT_INITIAL_CAPACITY = 16;

        @SuppressWarnings("unchecked")
        public Stack2() {
            elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
        }

        public void push(E e) {
            ensureCapacity();
            elements[size++] = e;
        }

        public E pop() {
            if (size == 0) {
                throw new EmptyStackException();
            }
            E result = elements[--size];
            elements[size] = null; // Eliminate obsolete reference
            return result;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        private void ensureCapacity() {
            if (elements.length == size)
                elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }

    //子类型关系被定义为每个类型都是自己的子类型。
    //泛型类型比需要在客户端代码中强制转换的类型更安全，更易于使用。
    //如果你有任何现有的类型，应该是泛型的但实际上却不是，那么把它们泛型化。这使这些类型的新用户的使用更容易，而不会破坏现有的客户端。
}
