package EffectiveJava3rd.iGeneralProgramming;

//62 当使用其他类型更合适时应避免使用字符串
public class I62 {
    //字符串是其他值类型的糟糕替代品。
    //字符串是枚举类型的糟糕替代品。
    //字符串是聚合类型的糟糕替代品。
    //字符串不能很好地替代capabilities。

    //java.lang.ThreadLocal提供的API，除了解决基于字符串的问题之外，它比任何基于键的API都更快、更优雅。

    //当存在或可以编写更好的数据类型时，应避免将字符串用来表示对象。
}
