package EffectiveJava3rd.eGenerics;

//32 合理地结合泛型和可变参数
public class E32 {
    //当你调用一个可变参数方法时，会创建一个数组来保存可变参数；那个应该是实现细节的数组是可见的。
    //SafeVarargs注解只对不能被重写的方法是合法的，因为不可能保证每个可能的重写方法都是安全的。

    //可变参数和泛型不能很好地交互，因为可变参数机制是在数组上面构建的脆弱的抽象，并且数组具有与泛型不同的类型规则。
    //如果选择使用泛型(或参数化)可变参数编写方法，请首先确保该方法是类型安全的，然后使用@SafeVarargs注解对其进行标注，以免造成使用不愉快。
}
