package EffectiveJava3rd.eGenerics;

//31 使用限定通配符来增加API的灵活性
public class E31 {
    /*
    public static class Stack {
        // Wildcard type for a parameter that serves as an E producer
        public void pushAll(Iterable<? extends E> src) {
            for (E e : src)
                push(e);
        }

        // popAll method without wildcard type - deficient!
        public void popAll(Collection<E> dst) {
            while (!isEmpty())
                dst.add(pop());
        }
    }
    */
    //为了获得最大的灵活性，对代表生产者或消费者的输入参数使用通配符类型。
    //如果一个输入参数既是一个生产者又是一个消费者，那么通配符类型对你没有好处：你需要一个精确的类型匹配，这就是没有任何通配符的情况。
    //PECS代表: producer-extends，consumer-super。
    //如果一个参数化类型代表一个T生产者，使用<? extends T>；如果它代表T消费者，则使用<? super T>。
    //在Stack中。pushAll方法的src参数生成栈使用的E实例，因此src的合适类型为Iterable<? extends E>；popAll方法的dst参数消费Stack中的E实例，因此dst的合适类型是Collection<? super E>。

    //public static <E> Set<E> union(Set<? extends E> s1, Set<? extends E> s2)
    //不要使用限定通配符类型作为返回类型。

    //Comparable实例总是消费者，所以通常应该使用Comparable<? super T>优于Comparable<T>。
    //Comparator实例总是消费者，所以通常应该使用Comparator<? super T>优于Comparator<T>。

    // Two possible declarations for the swap method
    //public static <E> void swap(List<E> list, int i, int j);
    //public static void swap(List<?> list, int i, int j);
    //如果类型参数在方法声明中只出现一次，请将其替换为通配符。如果它是一个无限制的类型参数，请将其替换为无限制的通配符；如果它是一个限定类型参数，则用限定通配符替换它。

    /*
    public static void swap(List<?> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }
    */
    //看起来我们不能把一个元素放回到我们刚刚拿出来的列表中。问题是列表的类型是List<?>，并且不能将除null外的任何值放入List<?>中。

    //写一个私有辅助方法来捕捉通配符类型。辅助方法必须是泛型方法才能捕获类型。
    /*
    public static void swapG(List<?> list, int i, int j) {
        swapHelper(list, i, j);
    }
    // Private helper method for wildcard capture
    private static <E> void swapHelper(List<E> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }
    */
}
