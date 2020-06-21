package EffectiveJava3rd.hMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//54 返回空的数组或集合，不要返回null
public class H54 {
    //如果有证据表明分配空集合会损害性能，可以通过重复返回相同的不可变空集合来避免分配，因为不可变对象可以自由共享(条目17)。
    private final List<Object> cheesesInStock = new ArrayList<>();
    // Optimization - avoids allocating empty collections
    public List<Object> getCheesesList() {
        return cheesesInStock.isEmpty() ? Collections.emptyList() : new ArrayList<>(cheesesInStock);
    }

    //数组的情况与集合的情况相同。永远不要返回null，而是返回长度为零的数组。
    //如果你认为分配零长度数组会损害性能，则可以重复返回相同的零长度数组，因为所有零长度数组都是不可变的。
    // Optimization - avoids allocating empty arrays
    private static final Object[] EMPTY_CHEESE_ARRAY = new Object[0];
    public Object[] getCheesesArray() {
        return cheesesInStock.toArray(EMPTY_CHEESE_ARRAY);
    }

    //永远不要返回null来代替空数组或集合。它使你的API更难以使用，更容易出错，并且没有性能优势。
}
