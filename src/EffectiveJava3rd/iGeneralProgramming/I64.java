package EffectiveJava3rd.iGeneralProgramming;

//64 通过接口引用对象
public class I64 {
    //如果存在合适的接口类型，那么应该使用接口类型声明参数、返回值、变量和字段。
    //惟一真正需要引用对象的类的时候是使用构造函数创建它的时候。
    //如果你养成了使用接口作为类型的习惯，那么你的程序将更加灵活。
    //如果没有合适的接口存在，那么用类引用对象是完全合适的。例如，考虑值类，如String和BigInteger。
    //没有合适接口类型的第二种情况是属于框架的对象，框架的基本类型是类而不是接口。如果一个对象属于这样一个基于类的框架，那么最好使用相关的基类来引用它。在java.io类中许多诸如OutputStream之类的就属于这种情况。
    //没有合适接口类型的最后一种情况是，实现接口但同时提供接口中不存在的额外方法的类。例如，PriorityQueue有一个在Queue接口上不存在的比较器方法。

    //如果没有合适的接口，就使用类层次结构中提供所需功能的最底层的类。
}