package EffectiveJava3rd.cMethodsCommonToAllObjects;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

//14 考虑实现Comparable接口
public class C14 {
    public static class WordList {
        public static void main(String[] args) {
            Set<String> s = new TreeSet<>();
            Collections.addAll(s, args);
            System.out.println(s);
        }
    }
    //几乎Java平台类库中的所有值类以及所有枚举类型都实现了Comparable接口。
    //compareTo返回值可能为负数，零或正数，则此对象对应小于，等于或大于指定的对象。如果指定对象的类型与此对象不能进行比较，则引发ClassCastException异常。
    //compareTo方法所实施的平等测试必须遵守equals方法约定所施加的相同限制: 自反性，对称性和传递性。
    //声明compareTo相等性测试，通常应该返回与equals方法相同的结果。
    //曾经推荐如果比较整型基本类型的属性，使用关系运算符"<"和">"，对于浮点类型基本类型的属性，使用Double.compare和Float.compare静态方法。
    //在Java7中，静态比较方法被添加到Java的所有包装类中。在compareTo方法中使用关系运算符"<"和">"是冗长且容易出错的，不再推荐。

    // Comparator based on static compare method
    static Comparator<Object> hashCodeOrder = new Comparator<Object>() {
        @Override
        public int compare(Object o1, Object o2) {
            return Integer.compare(o1.hashCode(), o2.hashCode());
        }
    };
    // Comparator based on Comparator construction method
    static Comparator<Object> hashCodeOrder2 = Comparator.comparingInt(o -> o.hashCode());

    //无论何时实现具有合理排序的值类，你都应该让该类实现Comparable接口，以便在基于比较的集合中轻松对其实例进行排序，搜索和使用。
    //比较compareTo方法的实现中的字段值时，请避免使用"<"和">"运算符。相反，使用包装类中的静态compare方法或Comparator接口中的构建方法。
}
