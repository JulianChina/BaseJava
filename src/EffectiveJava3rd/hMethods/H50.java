package EffectiveJava3rd.hMethods;

//50 必要时进行防御性拷贝
public class H50 {
    //愉快使用Java的原因，它是一种安全的语言(safe language)。
    //必须防御性地编写程序，假定类的客户端尽力摧毁类其不变量。
    //从Java8开始，使用Instant(或LocalDateTime或ZonedDateTime)代替Date。
    //防御性拷贝是在检查参数(条目49)的有效性之前进行的，有效性检查是在拷贝上而不是在原始实例上进行的。虽然这看起来不自然，但却是必要的。
    //不要使用clone方法对其类型可由不可信任子类化的参数进行防御性拷贝。
    //在访问器中，与构造方法不同，允许使用clone方法来制作防御性拷贝。
    //参数的防御性拷贝不仅仅适用于不可变类。每次编写在内部数据结构中存储对客户端提供的对象的引用的方法或构造函数时，请考虑客户端提供的对象是否可能是可变的。如果是，请考虑在将对象输入数据结构后，你的类是否可以容忍对象的更改。
    //在将内部组件返回给客户端之前进行防御性拷贝也是如此。无论你的类是否是不可变的，在返回对可变的内部组件的引用之前，都应该三思。可能的情况是，应该返回一个防御性拷贝。
    //记住，非零长度数组总是可变的。因此，在将内部数组返回给客户端之前，应该始终对其进行防御性拷贝。或者，可以返回数组的不可变视图。

    //如果一个类有从它的客户端获取或返回的可变组件，那么这个类必须防御性地拷贝这些组件。
    //如果拷贝的成本太高，并且类信任它的客户端不会不适当地修改组件，则可以用文档替换防御性拷贝，该文档概述了客户端不得修改受影响组件的责任。
}
